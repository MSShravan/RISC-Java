package edu.duke.ece651.team4.client;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team4.shared.PlayerConnection;

import static org.mockito.Mockito.*;

public class ClientTest {

    @Test
    public void initClient_fail() throws UnknownHostException, IOException {
        assertThrows(Exception.class, () -> new Client("host", 8080));
        assertThrows(Exception.class, () -> new Client());
    }


    @Test
    public void getConnection() {
        Client client = mock(Client.class);
        PlayerConnection res = mock(PlayerConnection.class);
        when(client.getConnection()).thenReturn(res);
        assertEquals(res, client.getConnection());
}

}
