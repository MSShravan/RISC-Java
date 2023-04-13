package edu.duke.ece651.team4.client;

import java.io.*;
import java.net.*;
import edu.duke.ece651.team4.shared.*;

public class Client {

    public PlayerConnection connection = null;

    /**
     * create a default client connect to the local host on port 8080
     * easy for tesing
     * @throws IOException
     * @throws UnknownHostException
     */
    public Client() throws UnknownHostException, IOException {
        this.connection = new PlayerConnection(new Socket("localhost", 8080));
    }

    /**
     * let user type the port and host
     * 
     * @param port the port number they want to connect
     * @param host the host they want to connet
     * @throws IOException
     * @throws UnknownHostException
     */
    public Client(String host, int port) throws UnknownHostException, IOException {
        this.connection = new PlayerConnection(new Socket(host, port));
    }

    public PlayerConnection getConnection() {
        return connection;
    }
}