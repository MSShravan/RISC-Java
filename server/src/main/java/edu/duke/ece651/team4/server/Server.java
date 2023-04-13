package edu.duke.ece651.team4.server;

import edu.duke.ece651.team4.shared.AbstractMapFactory;
import edu.duke.ece651.team4.shared.CollegeMapFactory;
import edu.duke.ece651.team4.shared.PlayerConnection;
import edu.duke.ece651.team4.shared.RISCMap;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The Server class that launches the game server and starts
 * the RISC game by allowing 2-4 client(player) connections.
 */
public class Server {

    /**
     * The method to launch the server and start the game
     *
     * @param port       must be of length two and has to be a valid integer for port number and number of players
     * @param numPlayers the number of players
     */
    public void launchServer(int port, int numPlayers) throws IOException, ExecutionException, InterruptedException {

        AbstractMapFactory mapFactory = new CollegeMapFactory();
        RISCMap gameMap = mapFactory.createMap(numPlayers);
        Game game = new Game(createServer(port), gameMap);

        startGame(game, numPlayers);

        ExecutorService executorService = Executors.newFixedThreadPool(numPlayers);

        doGroupAssignments(game, executorService);
        doPlacement(game, executorService);

        while (!isGameOver(game))
            doPlayATurn(game, executorService);

        doAnnounceWinner(game);

        executorService.shutdown();

        terminate(game);

    }

    /**
     * Method to check if game over
     *
     * @param game the game
     */
    protected void doAnnounceWinner(Game game) throws IOException {
        game.broadcastWinner();
    }

    /**
     * Method to check if game over
     *
     * @param game the game
     * @return boolean true if game over else false
     */
    protected boolean isGameOver(Game game) {
        return game.isGameOver();
    }

    /**
     * Method to create a server
     *
     * @param port the port number of the server socket
     * @return the server socket
     * @throws IOException
     */
    protected ServerSocket createServer(int port) throws IOException {
        return new ServerSocket(port);
    }

    /**
     * Method to start the RISC game
     *
     * @param game       the game
     * @param numPlayers the number of players
     * @throws IOException
     */
    protected void startGame(Game game, int numPlayers) throws IOException {
        game.connectPlayers(numPlayers);
        game.broadcastGameMap();
    }

    /**
     * Method to terminate the game
     *
     * @param game the game
     * @throws IOException
     */
    protected void terminate(Game game) throws IOException {
        game.terminateGame();
    }

    /**
     * Method to run placement phase of units into territories
     *
     * @param game            the game object
     * @param executorService to launch callable threads
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    protected void doPlacement(Game game, ExecutorService executorService) throws ExecutionException, InterruptedException, IOException {
        Map<PlayerConnection, Future<Object>> futures = game.requestSelection(executorService, false);
        game.assignUnits(futures);
        game.broadcastGameMap();
    }

    /**
     * Method to perform territory group selection
     *
     * @param game            the game object
     * @param executorService to launch callable threads
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    protected void doGroupAssignments(Game game, ExecutorService executorService) throws ExecutionException, InterruptedException, IOException {
        Map<PlayerConnection, Future<Object>> futures = game.requestSelection(executorService, true);
        game.assignGroups(futures, executorService);
        game.broadcastGameMap();
    }

    /**
     * Method to play a turn from all players
     *
     * @param game            the game object
     * @param executorService to launch callable threads
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    protected void doPlayATurn(Game game, ExecutorService executorService) throws ExecutionException, InterruptedException, IOException {
        Map<PlayerConnection, Future<Object>> futures = game.requestSelection(executorService, false);
        game.playATurn(futures, executorService);
        game.broadcastGameMap();
    }
}
