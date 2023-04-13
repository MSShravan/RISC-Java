package edu.duke.ece651.team4.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ActionTest {
  @Test
  public void test_action() {
    Territory t1 = new Territory("t1", 0);
    Territory t2 = new Territory("t2", 0);
    Action a = new Action(t2, t1, 5);
    assertEquals(a.getFrom(), t1);
    assertEquals(a.getTo(), t2);
    assertEquals(a.getNumUnits(), 5);

  }

  @Test
  public void test_set_action() {
    Territory t1 = new Territory("t1", 0);
    Territory t2 = new Territory("t2", 0);
    Action a = new Action(t2, t1, 5);
    assertEquals(5, a.getNumUnits());
    a.setNumUnits(2);
    assertEquals(2, a.getNumUnits());

  }

}
