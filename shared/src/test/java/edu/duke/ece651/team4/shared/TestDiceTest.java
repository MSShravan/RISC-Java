package edu.duke.ece651.team4.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestDiceTest {
  @Test
  public void test_test_dice() {
    Dice dice = new TestDice();
    int[] nums = new int[] { 11, 5, 4, 1, 20, 17, 16, 3, 15, 9, 11, 5, 4, 1, 20, 17, 16, 3, 15, 9 };
    for (int i = 0; i < 20; i++) {
      assertEquals(dice.roll(), nums[i]);
    }
  }

}
