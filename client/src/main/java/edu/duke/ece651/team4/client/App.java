/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.team4.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.UnknownHostException;

public class App {

  /**
   * this read the port and host to create the client 
   * 
   * @throws ClassNotFoundException
   *
   * @throws IOException
   * @throws UnknownHostException
   */

  public static Client createClient(BufferedReader input, PrintStream output) throws IOException {
    String port = null;
    String ip = null;
    Client client = null;
    // Send welcome message.
    output.println(
        "Welcome to the game of Risk! Below you will input the port number and server IP to connect to the game");
    try {
      output.println("Please type your port number, click enter for the default value (1651):");
      port = input.readLine();
      if (port.equals("")) {
        port = "1651";
      }
      output.println("Please type your server IP, click enter for the default value (vcm-32071.vm.duke.edu)");
      ip = input.readLine();
      if (ip.equals("")) {
        ip = "vcm-32071.vm.duke.edu";
      }
      client = new Client(ip, Integer.parseInt(port));

    } catch (Exception e) {
      output.println("Error: cannot use the input port number and hostname connect to the server. Please try again.");
    }
    return client;

  }
/**
 * this runs the whole client
 * @param input
 * @param output
 */
  public static void run(BufferedReader input, PrintStream output) {
    try {
      TextPlayers player = new TextPlayers(createClient(input, output).getConnection(), input, output);
      player.initGame();
      player.playTurns();
      player.close();
    } catch (Exception e) {
      output.println("client fail,some unknow system error happen");
    }
  }
/**
 * this part set up the input and output and start the game
 * @param args
 * @throws IOException
 */
  public static void main(String[] args) throws IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    PrintStream output = new PrintStream(System.out, true);
    run(input, output);

  }
}
