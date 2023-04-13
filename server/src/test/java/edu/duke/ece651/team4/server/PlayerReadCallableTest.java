package edu.duke.ece651.team4.server;

import edu.duke.ece651.team4.shared.PlayerConnection;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerReadCallableTest {

    @Test
    public void testCall() throws Exception {
        PlayerConnection playerConnection = mock(PlayerConnection.class);
        when(playerConnection.readData()).thenReturn("test");
        PlayerReadCallable playerReadCallable = new PlayerReadCallable(playerConnection);
        assertEquals("test", playerReadCallable.call());
    }

    @Test
    public void testCall_throwsException() throws Exception {
        PlayerConnection playerConnection = mock(PlayerConnection.class);
        when(playerConnection.readData()).thenThrow(new IOException());
        PlayerReadCallable playerReadCallable = new PlayerReadCallable(playerConnection);
        assertThrows(IOException.class, () -> playerReadCallable.call());
    }

}