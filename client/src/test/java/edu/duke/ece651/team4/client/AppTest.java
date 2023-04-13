package edu.duke.ece651.team4.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;

class AppTest {

  @Test
  public void test_createClient() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("1234\nlocalhost\n"));
    PrintStream output = new PrintStream(bytes, true);
    String res = "Welcome to the game of Risk! Below you will input the port number and server IP to connect to the game\n"
        +
        "Please type your port number, click enter for the default value (1651):\n"
        + "Please type your server IP, click enter for the default value (vcm-32071.vm.duke.edu)\n"
        + "Error: cannot use the input port number and hostname connect to the server. Please try again.\n";
    App.createClient(input, output);
    assertEquals(res, bytes.toString());
    bytes.reset();
    input = new BufferedReader(new StringReader("1650\n\n"));
    App.createClient(input, output);
    assertEquals(res, bytes.toString());
    bytes.reset();
    input = new BufferedReader(new StringReader("\nlocalhost\n"));
    App.createClient(input, output);
    assertEquals(res, bytes.toString());
  }
}
