package edu.duke.ece651.team4.server;

import edu.duke.ece651.team4.shared.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Game class to play RISC. Connects to the players and initiates gameplay
 */
public class Game {

    /**
     * Server socket
     */
    private final ServerSocket serverSocket;

    /**
     * Map of client connections and players
     */
    private final Map<PlayerConnection, Player> playerAssignments;

    /**
     * The game map
     */
    private final RISCMap gameMap;

    /**
     * Constructor to initialize the server socket and the game map
     *
     * @param serverSocket the server socket
     * @param gameMap      the game map
     */
    Game(ServerSocket serverSocket, RISCMap gameMap) {
        this.serverSocket = serverSocket;
        this.gameMap = gameMap;
        playerAssignments = new HashMap<>();
    }

    /**
     * Method to connect players to the game server
     *
     * @param numPlayers number of players
     */
    public void connectPlayers(int numPlayers) throws IOException {
        for (int i = 0; i < numPlayers; i++) {
            Socket playerSocket = serverSocket.accept();
            PlayerConnection playerConnection = new PlayerConnection(playerSocket);
            playerAssignments.put(playerConnection, null);
        }
    }

    /**
     * Method to send the initial game map to the clients
     */
    public void broadcastGameMap() throws IOException {
        for (PlayerConnection playerConnection : playerAssignments.keySet()) {
            playerConnection.writeData(gameMap);
        }
    }

    /**
     * Method to request player selections
     *
     * @param executorService to launch callable threads
     * @return Map<PlayerConnection, Future < Object>> the map of player connection and the future that will wait for data from the client
     */
    public Map<PlayerConnection, Future<Object>> requestSelection(ExecutorService executorService, boolean allowYetToJoinPlayers) {
        Map<PlayerConnection, Future<Object>> selections = new HashMap<>();
        for (PlayerConnection playerConnection : playerAssignments.keySet()) {
            if (allowYetToJoinPlayers || playerAssignments.get(playerConnection) != null)
                readerHelper(selections, executorService, playerConnection);
        }
        return selections;
    }

    /**
     * Method to assign territory group from clients
     *
     * @param selections      map with player connection object and the future object
     * @param executorService to launch callable threads
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    public void assignGroups(Map<PlayerConnection, Future<Object>> selections, ExecutorService executorService) throws ExecutionException, InterruptedException, IOException {
        Set<PlayerConnection> playerConnectionSet = selections.keySet();
        do {
            Set<PlayerConnection> pendingPlayerConnectionSet = new HashSet<>();
            for (PlayerConnection playerConnection : playerConnectionSet) {
                Future<Object> playerResponse = selections.get(playerConnection);
                if (playerResponse.isDone()) {
                    Player selectedPlayer = (Player) playerResponse.get();
                    if (!isPlayerTaken(selectedPlayer)) {
                        playerAssignments.put(playerConnection, selectedPlayer);
                        playerConnection.writeData(Collections.EMPTY_LIST);
                    } else {
                        pendingPlayerConnectionSet.add(playerConnection);
                        playerConnection.writeData(getChosenNames());
                        readerHelper(selections, executorService, playerConnection);
                    }
                } else {
                    pendingPlayerConnectionSet.add(playerConnection);
                }
            }
            playerConnectionSet = pendingPlayerConnectionSet;
        } while (playerConnectionSet.size() != 0);
    }

    /**
     * Method to return the list with chosen player names
     *
     * @return List<String> chosen player names
     */
    protected List<String> getChosenNames() {
        return playerAssignments.values().stream().filter(p -> p != null).map(p -> p.getName()).collect(Collectors.toList());
    }

    /**
     * Method to assign territory units from the client
     *
     * @param selections map with player connection object and the future object
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void assignUnits(Map<PlayerConnection, Future<Object>> selections) throws ExecutionException, InterruptedException {
        Set<PlayerConnection> playerConnectionSet = selections.keySet();
        do {
            Set<PlayerConnection> pendingPlayerConnectionSet = new HashSet<>();
            for (PlayerConnection playerConnection : playerConnectionSet) {
                Future<Object> playerResponse = selections.get(playerConnection);
                if (playerResponse.isDone()) {
                    HashMap<Territory, Integer> units = (HashMap<Territory, Integer>) playerResponse.get();
                    gameMap.assignUnitsForOnePlayer(playerAssignments.get(playerConnection), units);
                } else {
                    pendingPlayerConnectionSet.add(playerConnection);
                }
            }
            playerConnectionSet = pendingPlayerConnectionSet;
        } while (playerConnectionSet.size() != 0);
    }

    /**
     * Method to play a turn from all players
     *
     * @param selections map with player connection object and the future object
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    public void playATurn(Map<PlayerConnection, Future<Object>> selections, ExecutorService executorService) throws ExecutionException, InterruptedException, IOException {
        ArrayList<OnePlayerTurn> allPlayerTurns = new ArrayList<>();

        Set<PlayerConnection> playerConnectionSet = selections.keySet();
        do {
            Set<PlayerConnection> pendingPlayerConnectionSet = new HashSet<>();
            for (PlayerConnection playerConnection : playerConnectionSet) {
                Future<Object> playerResponse = selections.get(playerConnection);
                if (playerResponse.isDone()) {
                    Player player = gameMap.getPlayerByName(playerAssignments.get(playerConnection).getName());
                    if (player.isAlive()) {
                        OnePlayerTurn turn = (OnePlayerTurn) playerResponse.get();
                        String result = player.validateTurn(turn, gameMap);
                        playerConnection.writeData(result);
                        if (result != null) {
                            pendingPlayerConnectionSet.add(playerConnection);
                            readerHelper(selections, executorService, playerConnection);
                        } else {
                            allPlayerTurns.add(turn);
                        }
                    } else {
                        watchGameOrDisconnect(playerConnection, playerResponse);
                    }
                } else {
                    pendingPlayerConnectionSet.add(playerConnection);
                }
            }
            playerConnectionSet = pendingPlayerConnectionSet;
        } while (playerConnectionSet.size() != 0);

        gameMap.doOneTurn(allPlayerTurns);
    }

    protected void watchGameOrDisconnect(PlayerConnection playerConnection, Future<Object> playerResponse) throws InterruptedException, ExecutionException, IOException {
        boolean watchGame = (boolean) playerResponse.get();
        if (watchGame) {
            playerAssignments.put(playerConnection, null);
        } else {
            playerConnection.close();
            playerAssignments.remove(playerConnection);
        }
    }

    /**
     * Method to create callable threads to read from clients
     *
     * @param selections       map with player connection object and the future object
     * @param executorService  to launch callable threads
     * @param playerConnection the player connection
     */
    protected void readerHelper(Map<PlayerConnection, Future<Object>> selections, ExecutorService executorService, PlayerConnection playerConnection) {
        Callable<Object> playerReadCallable = new PlayerReadCallable(playerConnection);
        Future<Object> future = executorService.submit(playerReadCallable);
        selections.put(playerConnection, future);
    }

    /**
     * Method to check if player is already picked from other clients
     *
     * @param playerPicked the player picked from a client
     * @return boolean true if player available to pick else false
     */
    protected boolean isPlayerTaken(Player playerPicked) {
        for (Player player : playerAssignments.values()) {
            if (player != null && player.getName().equals(playerPicked.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check if game is over
     *
     * @return boolean true if game over, false otherwise
     */
    public boolean isGameOver() {
        return gameMap.isGameOver();
    }

    /**
     * Method to send the winner to the clients
     */
    public void broadcastWinner() throws IOException {
        Player winner = gameMap.getWinner();
        for (PlayerConnection playerConnection : playerAssignments.keySet()) {
            playerConnection.writeData(winner);
        }
    }

    /**
     * Method to terminate the game by close all sockets and streams
     *
     * @throws IOException
     */
    public void terminateGame() throws IOException {
        for (PlayerConnection playerConnection : playerAssignments.keySet()) {
            playerConnection.close();
        }
        serverSocket.close();
    }


}
