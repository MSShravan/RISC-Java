package edu.duke.ece651.team4.shared;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PlayerConnectionTest {

    @Test
    public void test_playerConnection() throws IOException, ClassNotFoundException {
        Socket playerSocket = mock(Socket.class);

        FileOutputStream fileOutputStream = new FileOutputStream(getClass().getClassLoader().getResource("PlayerConnectionTest.txt").getFile());
        when(playerSocket.getOutputStream()).thenReturn(fileOutputStream);

        FileInputStream fileInputStream = new FileInputStream(getClass().getClassLoader().getResource("PlayerConnectionTest.txt").getFile());
        when(playerSocket.getInputStream()).thenReturn(fileInputStream);

        PlayerConnection playerConnection = new PlayerConnection(playerSocket);

        playerConnection.writeData("data");
        assertEquals("data", playerConnection.readData());
        playerConnection.close();

    }

    @Test
    public void test_playerConnection_throwsException() throws IOException {
        Socket playerSocket = mock(Socket.class);
        when(playerSocket.getOutputStream()).thenThrow(new IOException());

        assertThrows(IOException.class, () -> new PlayerConnection(playerSocket));
    }

    @Test
    public void test_playerConnection_readData_throwsException() throws IOException, ClassNotFoundException {
        PlayerConnection playerConnection = mock(PlayerConnection.class);
        when(playerConnection.readData()).thenThrow(new IOException());

        assertThrows(IOException.class, () -> playerConnection.readData());

        PlayerConnection playerConnection2 = mock(PlayerConnection.class);
        when(playerConnection2.readData()).thenThrow(new ClassNotFoundException());
        assertThrows(ClassNotFoundException.class, () -> playerConnection2.readData());
    }

    @Test
    public void test_playerConnection_writeData_throwsException() throws IOException {
        PlayerConnection playerConnection = mock(PlayerConnection.class);
        doThrow(new IOException()).when(playerConnection).writeData("data");
        assertThrows(IOException.class, () -> playerConnection.writeData("data"));
    }

    @Test
    public void test_playerConnection_close_throwsException() throws IOException {
        PlayerConnection playerConnection = mock(PlayerConnection.class);
        doThrow(new IOException()).when(playerConnection).close();
        assertThrows(IOException.class, () -> playerConnection.close());
    }

}