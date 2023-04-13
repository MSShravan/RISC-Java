package edu.duke.ece651.team4.shared;

import java.io.Serializable;

/**
 * Represents one action.
 */
public class Action implements Serializable {
  /**
   * The territory the action acts on.
   */
  private Territory to;
  /**
   * The territory the action acts from.
   */
  private Territory from;
  /**
   * Number of units involved in the aciton.
   */
  private int numUnits;

  /**
   * Constructor for Action.
   * 
   * @param to       is the territory which the action acts on.
   * @param from     is the territory which the action acts from.
   * @param numUnits is the number of units involved in the aciton.
   */
  public Action(Territory to, Territory from, int numUnits) {
    this.to = to;
    this.from = from;
    this.numUnits = numUnits;
  }

  /**
   * Getter for the "to" territory.
   * 
   * @return the territory which the action acts on.
   */
  public Territory getTo() {
    return to;
  }

  /**
   * Getter for the "from" territory.
   * 
   * @return the territory which the ation
   */
  public Territory getFrom() {
    return from;
  }

  /**
   * Getter for the number of units involved in the action.
   * 
   * @return the number of units involved in the action.
   */
  public int getNumUnits() {
    return numUnits;
  }

  /**
   * Setter for the number of units involved in the action.
   *
   * @param newNumUnits is the new number of units we want to set
   */
  public void setNumUnits(int newNumUnits) {
    numUnits = newNumUnits;
  }
}
