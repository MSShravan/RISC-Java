package edu.duke.ece651.team4.shared;


/**
 * Interface for creating a RISCMap.
 */
public interface AbstractMapFactory {
  /**
   * Creates and returns a RISCMap
   * @param numPlayers is the number of players playing the game.
   * precondition: numPlayers is between 2 and 4 (inclusive).
   * postcondition: map (non-null) is created and returned.
   */
  RISCMap createMap(int numPlayers);

}
