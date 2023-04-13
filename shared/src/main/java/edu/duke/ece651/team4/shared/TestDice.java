package edu.duke.ece651.team4.shared;

import java.util.ArrayList;

/**
 * TestDice that returns predictable response.
 */
public class TestDice implements Dice {
  private int[] nums;
  private int index;

  /**
   * Constructor for test dice.
   */
  public TestDice() {
    nums = new int[] { 11, 5, 4, 1, 20, 17, 16, 3, 15, 9 };
    index = 0;
  }

  /**
   * Roll returns a predictable result. Rolls a sequence of 10 numbers, repeating.
   */
  @Override
  public int roll() {
    int ans = nums[index];
    if (index < 9) {
      index++;
    } else {
      index = 0;
    }
    return ans;
  }

}
