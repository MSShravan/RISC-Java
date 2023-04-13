package edu.duke.ece651.team4.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * PlayerConnection class for storing information of the client socket and the input/output streams
 */
public class PlayerConnection {

    /**
     * The client/player socket
     */
    private final Socket playerSocket;

    /**
     * Output stream to write data to the client
     */
    private final ObjectOutputStream out;

    /**
     * Input stream to read data from the client
     */
    private final ObjectInputStream in;

    /**
     * Constructor to assign the player socket and create object streams
     *
     * @param playerSocket the client socket
     * @throws IOException for any IO failures
     */
    public PlayerConnection(Socket playerSocket) throws IOException {
        this.playerSocket = playerSocket;
        out = new ObjectOutputStream(this.playerSocket.getOutputStream());
        in = new ObjectInputStream(this.playerSocket.getInputStream());
    }

    /**
     * Closes the object streams and the client socket
     *
     * @throws IOException for any IO failures
     */
    public void close() throws IOException {
        out.close();
        in.close();
        playerSocket.close();
    }

    /**
     * Method to write data for the client
     *
     * @param data to be written to the client
     * @throws IOException for any IO failures
     */
    public void writeData(Object data) throws IOException {
        out.reset();
        out.writeObject(data);
    }

    /**
     * Method to read data from the client
     *
     * @throws IOException            for any IO failures
     * @throws ClassNotFoundException for any class not found failures
     */
    public Object readData() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

}
