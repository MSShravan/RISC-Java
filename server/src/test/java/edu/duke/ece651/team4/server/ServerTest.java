package edu.duke.ece651.team4.server;

import edu.duke.ece651.team4.shared.PlayerConnection;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerTest {

    @Test
    void test_launchServer() throws IOException, ExecutionException, InterruptedException {
        Server server = new Server();
        Server serverSpy = spy(server);
        doReturn(null).when(serverSpy).createServer(8080);
        doNothing().when(serverSpy).startGame(any(Game.class), anyInt());
        doNothing().when(serverSpy).doGroupAssignments(any(Game.class), any(ExecutorService.class));
        doNothing().when(serverSpy).doPlacement(any(Game.class), any(ExecutorService.class));
        doReturn(false, true).when(serverSpy).isGameOver(any(Game.class));
        doNothing().when(serverSpy).doPlayATurn(any(Game.class), any(ExecutorService.class));
        doNothing().when(serverSpy).doAnnounceWinner(any(Game.class));
        doNothing().when(serverSpy).terminate(any(Game.class));

        serverSpy.launchServer(8080, 2);

        verify(serverSpy, times(1)).startGame(any(Game.class), anyInt());
        verify(serverSpy, times(1)).doGroupAssignments(any(Game.class), any(ExecutorService.class));
        verify(serverSpy, times(1)).doPlacement(any(Game.class), any(ExecutorService.class));
        verify(serverSpy, times(1)).doPlayATurn(any(Game.class), any(ExecutorService.class));
        verify(serverSpy, times(2)).isGameOver(any(Game.class));
        verify(serverSpy, times(1)).doAnnounceWinner(any(Game.class));
        verify(serverSpy, times(1)).terminate(any(Game.class));
    }

    @Test
    void test_launchServer_throwsException() throws Exception {
        Server server1 = new Server();
        Server serverSpy1 = spy(server1);
        doThrow(new IOException()).when(serverSpy1).createServer(8080);
        assertThrows(IOException.class, () -> serverSpy1.launchServer(8080, 2));

        Server server2 = new Server();
        Server serverSpy2 = spy(server2);
        doReturn(null).when(serverSpy2).createServer(8080);
        doNothing().when(serverSpy2).startGame(any(Game.class), anyInt());
        doThrow(new ExecutionException(new IOException())).when(serverSpy2).doGroupAssignments(any(Game.class), any(ExecutorService.class));
        assertThrows(ExecutionException.class, () -> serverSpy2.launchServer(8080, 2));

        Server server3 = new Server();
        Server serverSpy3 = spy(server3);
        doReturn(null).when(serverSpy3).createServer(8080);
        doNothing().when(serverSpy3).startGame(any(Game.class), anyInt());
        doNothing().when(serverSpy3).doGroupAssignments(any(Game.class), any(ExecutorService.class));
        doThrow(new InterruptedException()).when(serverSpy3).doPlacement(any(Game.class), any(ExecutorService.class));
        assertThrows(InterruptedException.class, () -> serverSpy3.launchServer(8080, 2));

    }

    @Test
    void test_terminateGame() throws IOException {
        Game game = mock(Game.class);
        Server server = new Server();
        server.terminate(game);
    }

    @Test
    void test_createServer() throws IOException {
        Server server = new Server();
        ServerSocket serverSocket = server.createServer(8088);
        assertNotNull(serverSocket);
        serverSocket.close();

    }

    @Test
    void test_doPlacement() throws Exception {
        Game game = mock(Game.class);
        ExecutorService executorService = mock(ExecutorService.class);
        Map<PlayerConnection, Future<Object>> futures = mock(Map.class);
        when(game.requestSelection(executorService, false)).thenReturn(futures);

        Server server = new Server();
        server.doPlacement(game, executorService);

        verify(game, times(1)).requestSelection(executorService, false);
        verify(game, times(1)).assignUnits(futures);
        verify(game, times(1)).broadcastGameMap();
    }

    @Test
    void test_doGroupAssignments() throws Exception {
        Game game = mock(Game.class);
        ExecutorService executorService = mock(ExecutorService.class);
        Map<PlayerConnection, Future<Object>> futures = mock(Map.class);
        when(game.requestSelection(executorService, true)).thenReturn(futures);

        Server server = new Server();
        server.doGroupAssignments(game, executorService);

        verify(game, times(1)).requestSelection(executorService, true);
        verify(game, times(1)).assignGroups(futures, executorService);
        verify(game, times(1)).broadcastGameMap();
    }

    @Test
    void test_startGame() throws IOException {
        Game game = mock(Game.class);
        Server server = new Server();
        server.startGame(game, 2);

        verify(game, times(1)).connectPlayers(2);
        verify(game, times(1)).broadcastGameMap();
    }

    @Test
    void test_doPlayATurn() throws Exception {
        Game game = mock(Game.class);
        ExecutorService executorService = mock(ExecutorService.class);
        Map<PlayerConnection, Future<Object>> futures = mock(Map.class);
        when(game.requestSelection(executorService, false)).thenReturn(futures);

        Server server = new Server();
        server.doPlayATurn(game, executorService);

        verify(game, times(1)).requestSelection(executorService, false);
        verify(game, times(1)).playATurn(futures, executorService);
        verify(game, times(1)).broadcastGameMap();
    }

    @Test
    void isGameOver() {
        Game game = mock(Game.class);
        doReturn(true, false).when(game).isGameOver();
        Server server = new Server();
        assertTrue(server.isGameOver(game));
        assertFalse(server.isGameOver(game));
    }

    @Test
    void doAnnounceWinner() throws IOException {
        Game game = mock(Game.class);
        Server server = new Server();
        server.doAnnounceWinner(game);

        verify(game, times(1)).broadcastWinner();
    }

}