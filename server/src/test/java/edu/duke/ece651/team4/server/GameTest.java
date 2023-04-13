package edu.duke.ece651.team4.server;

import edu.duke.ece651.team4.shared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameTest {

    ServerSocket serverSocket;
    Socket playerSocket;

    ExecutorService executorService;

    RISCMap gameMap;

    int numPlayers = 2;

    Map<PlayerConnection, Future<Object>> selections;

    @BeforeEach
    void init() throws Exception {
        serverSocket = mock(ServerSocket.class);

        playerSocket = mock(Socket.class);
        when(serverSocket.accept()).thenReturn(playerSocket);

        FileOutputStream fileOutputStream = new FileOutputStream(getClass().getClassLoader().getResource("PlayerConnectionTest.txt").getFile());
        when(playerSocket.getOutputStream()).thenReturn(fileOutputStream);

        FileInputStream fileInputStream = new FileInputStream(getClass().getClassLoader().getResource("PlayerConnectionTest.txt").getFile());
        when(playerSocket.getInputStream()).thenReturn(fileInputStream);

        gameMap = mock(RISCMap.class);

    }

    @Test
    void test_initGame() throws Exception {

        Game game = new Game(serverSocket, gameMap);
        game.connectPlayers(numPlayers);

        verify(serverSocket, times(2)).accept();
        verify(playerSocket, times(2)).getInputStream();
        verify(playerSocket, times(2)).getOutputStream();

    }

    @Test
    void broadcastGameMap() throws Exception {
        Game game = new Game(serverSocket, gameMap);
        game.connectPlayers(numPlayers);
        game.broadcastGameMap();

        verify(serverSocket, times(2)).accept();
        verify(playerSocket, times(2)).getInputStream();
        verify(playerSocket, times(2)).getOutputStream();
    }

    @Test
    void requestSelection() throws Exception {
        Player player = mock(Player.class);
        when(player.getName()).thenReturn("red");

        Future<Object> future = mock(Future.class);
        when(future.isDone()).thenReturn(true);
        when(future.get()).thenReturn(player);

        PlayerConnection playerConnection = mock(PlayerConnection.class);

        selections = new HashMap<>();
        selections.put(playerConnection, future);

        Game game = new Game(serverSocket, gameMap);
        game.assignGroups(selections, executorService);
        game.connectPlayers(numPlayers);

        executorService = mock(ExecutorService.class);
        when(executorService.submit(any(Callable.class))).thenReturn(null);

        game.requestSelection(executorService, false);

        verify(serverSocket, times(2)).accept();
        verify(playerSocket, times(2)).getInputStream();
        verify(playerSocket, times(2)).getOutputStream();
        verify(executorService, times(1)).submit(any(Callable.class));
    }

    @Test
    void assignGroups() throws Exception {
        Player player1 = mock(Player.class);
        when(player1.getName()).thenReturn("red", "blue");

        Future<Object> future1 = mock(Future.class);
        when(future1.isDone()).thenReturn(false, true);
        when(future1.get()).thenReturn(player1);

        Player player2 = mock(Player.class);
        when(player2.getName()).thenReturn("red");

        Future<Object> future2 = mock(Future.class);
        when(future2.isDone()).thenReturn(true);
        when(future2.get()).thenReturn(player2);

        PlayerConnection playerConnection1 = mock(PlayerConnection.class);
        PlayerConnection playerConnection2 = mock(PlayerConnection.class);

        selections = new HashMap<>();
        selections.put(playerConnection1, future1);
        selections.put(playerConnection2, future2);

        Player player3 = mock(Player.class);
        when(player3.getName()).thenReturn("white");

        Future<Object> future3 = mock(Future.class);
        when(future3.isDone()).thenReturn(true);
        when(future3.get()).thenReturn(player3);

        executorService = mock(ExecutorService.class);
        when(executorService.submit(any(Callable.class))).thenReturn(future3);

        Game game = new Game(serverSocket, gameMap);
        game.assignGroups(selections, executorService);

        verify(future1, times(2)).isDone();
        verify(future1, times(1)).get();
        verify(future2, times(1)).isDone();
        verify(future2, times(1)).get();
        verify(future3, times(1)).isDone();
        verify(future3, times(1)).get();
        verify(playerConnection1, times(2)).writeData(any(List.class));
        verify(playerConnection2, times(1)).writeData(any(List.class));
        verify(executorService, times(1)).submit(any(Callable.class));

    }

    @Test
    void assignUnits() throws Exception {
        HashMap<Territory, Integer> units = mock(HashMap.class);

        Future<Object> future1 = mock(Future.class);
        when(future1.isDone()).thenReturn(false, true);
        when(future1.get()).thenReturn(units);

        Future<Object> future2 = mock(Future.class);
        when(future2.isDone()).thenReturn(true);
        when(future2.get()).thenReturn(units);

        PlayerConnection playerConnection1 = mock(PlayerConnection.class);
        PlayerConnection playerConnection2 = mock(PlayerConnection.class);

        selections = new HashMap<>();
        selections.put(playerConnection1, future1);
        selections.put(playerConnection2, future2);

        Game game = new Game(serverSocket, gameMap);
        game.assignUnits(selections);

        verify(future1, times(2)).isDone();
        verify(future1, times(1)).get();
        verify(future2, times(1)).isDone();
        verify(future2, times(1)).get();
        verify(gameMap, times(2)).assignUnitsForOnePlayer(any(), any());
    }

    @Test
    void playATurn() throws Exception {

        Player player1 = mock(Player.class);
        doReturn("player1").when(player1).getName();
        doReturn(true).when(player1).isAlive();
        String nullString = null;
        doReturn("some error", nullString).when(player1).validateTurn(any(), any());
        Player player2 = mock(Player.class);
        doReturn("player2").when(player2).getName();
        doReturn(false).when(player2).isAlive();

        Future<Object> future1 = mock(Future.class);
        doReturn(true).when(future1).isDone();
        doReturn(player1).when(future1).get();
        Future<Object> future2 = mock(Future.class);
        doReturn(true).when(future2).isDone();
        doReturn(player2).when(future2).get();

        PlayerConnection playerConnection1 = mock(PlayerConnection.class);
        PlayerConnection playerConnection2 = mock(PlayerConnection.class);

        selections = new HashMap<>();
        selections.put(playerConnection1, future1);
        selections.put(playerConnection2, future2);

        executorService = mock(ExecutorService.class);

        doReturn(player1).when(gameMap).getPlayerByName("player1");
        doReturn(player2).when(gameMap).getPlayerByName("player2");
        Game game = new Game(serverSocket, gameMap);
        Game gameSpy = spy(game);
        doNothing().when(gameSpy).readerHelper(any(), any(), any());
        doNothing().when(gameSpy).watchGameOrDisconnect(any(), any());
        doReturn(false).when(gameSpy).isPlayerTaken(any());

        gameSpy.assignGroups(selections, executorService);

        OnePlayerTurn turn1 = mock(OnePlayerTurn.class);
        OnePlayerTurn turn2 = mock(OnePlayerTurn.class);

        Future<Object> turnFuture1 = mock(Future.class);
        doReturn(true).when(turnFuture1).isDone();
        doReturn(turn1).when(turnFuture1).get();
        Future<Object> turnFuture2 = mock(Future.class);
        doReturn(false, true).when(turnFuture2).isDone();
        doReturn(turn2).when(turnFuture2).get();

        Map<PlayerConnection, Future<Object>> turnSelections = new HashMap<>();
        turnSelections.put(playerConnection1, turnFuture1);
        turnSelections.put(playerConnection2, turnFuture2);

        gameSpy.playATurn(turnSelections, executorService);

        verify(gameSpy, times(1)).readerHelper(any(), any(), any());
        verify(gameSpy, times(1)).watchGameOrDisconnect(any(), any());
        verify(gameMap, times(1)).doOneTurn(any());

    }

    @Test
    void getChosenNames() throws Exception {
        Player player1 = mock(Player.class);
        doReturn("player1").when(player1).getName();
        doReturn(true).when(player1).isAlive();
        String nullString = null;
        doReturn("some error", nullString).when(player1).validateTurn(any(), any());
        Player player2 = mock(Player.class);
        doReturn("player2").when(player2).getName();
        doReturn(false).when(player2).isAlive();

        Future<Object> future1 = mock(Future.class);
        doReturn(true).when(future1).isDone();
        doReturn(player1).when(future1).get();
        Future<Object> future2 = mock(Future.class);
        doReturn(true).when(future2).isDone();
        doReturn(player2).when(future2).get();

        PlayerConnection playerConnection1 = mock(PlayerConnection.class);
        PlayerConnection playerConnection2 = mock(PlayerConnection.class);

        selections = new HashMap<>();
        selections.put(playerConnection1, future1);
        selections.put(playerConnection2, future2);

        executorService = mock(ExecutorService.class);

        doReturn(player1).when(gameMap).getPlayerByName("player1");
        doReturn(player2).when(gameMap).getPlayerByName("player2");
        Game game = new Game(serverSocket, gameMap);
        Game gameSpy = spy(game);
        doNothing().when(gameSpy).readerHelper(any(), any(), any());
        doNothing().when(gameSpy).watchGameOrDisconnect(any(), any());
        doReturn(false).when(gameSpy).isPlayerTaken(any());

        gameSpy.assignGroups(selections, executorService);

        List<String> expected = new ArrayList<>();

        Game game2 = new Game(serverSocket, gameMap);
        assertEquals(expected, game2.getChosenNames());

        expected.add("player1");
        expected.add("player2");

        List<String> actual = gameSpy.getChosenNames();

        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void watchGameOrDisconnect() throws Exception {
        PlayerConnection playerConnection = mock(PlayerConnection.class);
        Future<Object> future = mock(Future.class);
        doReturn(true).when(future).get();
        Game game = new Game(serverSocket, gameMap);
        game.connectPlayers(numPlayers);

        game.watchGameOrDisconnect(playerConnection, future);

        verify(future, times(1)).get();
        verify(playerConnection, times(0)).close();

        Future<Object> future1 = mock(Future.class);
        doReturn(false).when(future1).get();

        game.watchGameOrDisconnect(playerConnection, future1);

        verify(future, times(1)).get();
        verify(playerConnection, times(1)).close();
    }

    @Test
    void test_isGameOver() throws Exception {
        doReturn(true).when(gameMap).isGameOver();
        Game game = new Game(serverSocket, gameMap);
        assertTrue(game.isGameOver());
    }

    @Test
    void broadcastWinner() throws Exception {
        Player winner = mock(Player.class);
        doReturn(winner).when(gameMap).getWinner();
        Game game = new Game(serverSocket, gameMap);
        Game gameSpy = spy(game);
        gameSpy.connectPlayers(numPlayers);
        gameSpy.broadcastWinner();

        verify(serverSocket, times(2)).accept();
        verify(playerSocket, times(2)).getInputStream();
        verify(playerSocket, times(2)).getOutputStream();
        verify(gameMap, times(1)).getWinner();
    }

    @Test
    void terminateGame() throws Exception {
        Game game = new Game(serverSocket, gameMap);
        game.connectPlayers(numPlayers);
        game.terminateGame();

        verify(serverSocket, times(2)).accept();
        verify(playerSocket, times(2)).getInputStream();
        verify(playerSocket, times(2)).getOutputStream();
        verify(serverSocket, times(1)).close();

    }

}