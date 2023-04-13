package edu.duke.ece651.team4.server;

/**
 * The App class that launches the game server
 */
public class App {

    /**
     * The method to launch the server and start the game
     *
     * @param args must be of length two and has to be a valid integer for port number and number of players
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: java app <port number> <number of players>");
            return;
        }

        int port, numPlayers;

        try {
            port = Integer.parseInt(args[0]);
            numPlayers = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Exception in parsing inputs");
            System.err.println(e.getMessage());
            return;
        }

        if (numPlayers > 4 || numPlayers < 2) {
            System.err.println("Number of players must be 2-4 but was " + numPlayers);
            return;
        }

        App app = new App();
        app.doServerLaunch(new Server(), port, numPlayers);

    }

    /**
     * The method to launch the game server
     *
     * @param server     the game server
     * @param port       the port number
     * @param numPlayers the nuber of players
     */
    protected void doServerLaunch(Server server, int port, int numPlayers) {
        try {
            server.launchServer(port, numPlayers);
        } catch (Exception e) {
            System.err.println("Exiting game due to an error!");
            System.err.println(e.getMessage());
        }
    }
}
