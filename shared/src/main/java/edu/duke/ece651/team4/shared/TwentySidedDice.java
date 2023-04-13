package edu.duke.ece651.team4.shared;

/**
 * TwentySidedDice represents a dice that has twenty sides. Can be rolled to
 * return an integer.
 */
public class TwentySidedDice implements Dice {
  /**
   * Roll the dice. Will return a number between 1 and 20 (inclusive).
   * 
   * @return an integer between 1 and 20 (inclusive).
   */
  @Override
  public int roll() {
    int minVal = 1;
    int maxVal = 20;
    return (int) (Math.random() * (maxVal - minVal + 1) + minVal);
  }
}
