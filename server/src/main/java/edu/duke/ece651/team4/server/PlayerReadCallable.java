package edu.duke.ece651.team4.server;

import edu.duke.ece651.team4.shared.PlayerConnection;

import java.util.concurrent.Callable;

/**
 * Callable for reading data from the player connection
 */
public class PlayerReadCallable implements Callable<Object> {

    private final PlayerConnection playerConnection;

    /**
     * Constructor to initialize the player connection
     *
     * @param playerConnection the player connection object
     */
    PlayerReadCallable(PlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    /**
     * Overridden call method to return the data read from the player connection
     *
     * @return data read from the player connection
     * @throws Exception thrown if reading fails
     */
    @Override
    public Object call() throws Exception {
        return playerConnection.readData();
    }

}
