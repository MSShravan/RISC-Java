package edu.duke.ece651.team4.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppTest {

    @Test
    @ResourceLock(value = Resources.SYSTEM_ERR, mode = ResourceAccessMode.READ_WRITE)
    void test_main_invalidNumberOfArguments() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(bytes, true);

        PrintStream oldErr = System.err;

        try {
            System.setErr(err);
            App.main(new String[0]);
        } finally {
            System.setErr(oldErr);
        }
        String actual = bytes.toString();
        String expected = "Usage: java app <port number> <number of players>\n";
        assertEquals(expected, actual);
    }

    @Test
    @ResourceLock(value = Resources.SYSTEM_ERR, mode = ResourceAccessMode.READ_WRITE)
    void test_main_invalidPortNumber() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(bytes, true);

        PrintStream oldErr = System.err;

        try {
            System.setErr(err);
            App.main(new String[]{"abc", "4"});
        } finally {
            System.setErr(oldErr);
        }
        String actual = bytes.toString();
        String expected = "Exception in parsing inputs\nFor input string: \"abc\"\n";
        assertEquals(expected, actual);
    }

    @Test
    @ResourceLock(value = Resources.SYSTEM_ERR, mode = ResourceAccessMode.READ_WRITE)
    void test_main_invalidNumPlayers() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(bytes, true);

        PrintStream oldErr = System.err;

        try {
            System.setErr(err);
            App.main(new String[]{"8080", "a"});
        } finally {
            System.setErr(oldErr);
        }
        String actual = bytes.toString();
        String expected = "Exception in parsing inputs\nFor input string: \"a\"\n";
        assertEquals(expected, actual);
    }

    @Test
    @ResourceLock(value = Resources.SYSTEM_ERR, mode = ResourceAccessMode.READ_WRITE)
    void test_main_NumPlayersGT4() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(bytes, true);

        PrintStream oldErr = System.err;

        try {
            System.setErr(err);
            App.main(new String[]{"8080", "5"});
        } finally {
            System.setErr(oldErr);
        }
        String actual = bytes.toString();
        String expected = "Number of players must be 2-4 but was 5\n";
        assertEquals(expected, actual);
    }

    @Test
    @ResourceLock(value = Resources.SYSTEM_ERR, mode = ResourceAccessMode.READ_WRITE)
    void test_main_NumPlayersLT2() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(bytes, true);

        PrintStream oldErr = System.err;

        try {
            System.setErr(err);
            App.main(new String[]{"8080", "1"});
        } finally {
            System.setErr(oldErr);
        }
        String actual = bytes.toString();
        String expected = "Number of players must be 2-4 but was 1\n";
        assertEquals(expected, actual);
    }

    @Test
    void test_doServerLaunch() throws Exception {
        App app = new App();
        Server server = mock(Server.class);
        doNothing().when(server).launchServer(8080, 2);

        app.doServerLaunch(server, 8080, 2);

        verify(server, times(1)).launchServer(8080, 2);
    }

    @Test
    @ResourceLock(value = Resources.SYSTEM_ERR, mode = ResourceAccessMode.READ_WRITE)
    void test_doServerLaunch_throwsException() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(bytes, true);

        PrintStream oldErr = System.err;

        try {
            System.setErr(err);
            App app = new App();
            Server server = mock(Server.class);
            doThrow(IOException.class).when(server).launchServer(8080, 2);
            app.doServerLaunch(server, 8080, 2);
        } finally {
            System.setErr(oldErr);
        }
        String actual = bytes.toString();
        String expected = "Exiting game due to an error!\nnull\n";
        assertEquals(expected, actual);
    }

}