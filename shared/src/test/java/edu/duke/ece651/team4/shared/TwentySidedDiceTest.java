package edu.duke.ece651.team4.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TwentySidedDiceTest {
  @Test
  public void test_roll_dice() {
    Dice dice = new TwentySidedDice();
    for (int i = 0; i < 1000; i++) {
      int diceValue = dice.roll();
      assertTrue(diceValue >= 1 && diceValue <= 20);
    }
  }
}
