package edu.duke.ece651.team4.shared;

/**
 * Exception called when an illegal order is given by a player.
 */
public class IllegalActionException extends Exception {
  public IllegalActionException(String message) {
    super(message);
  }
}
