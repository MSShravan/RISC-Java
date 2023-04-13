package edu.duke.ece651.team4.shared;

import java.io.Serializable;

/**
 * A Dice represent a rollable object.
 */
public interface Dice extends Serializable {
  /**
   * Roll the dice.
   */
  public int roll();
}
