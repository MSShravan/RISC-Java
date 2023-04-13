package edu.duke.ece651.team4.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TerritoryTest {
  /**
   * test_get_name() is to test if getName will return correct territory name
   */
  @Test
  public void test_get_name() {
    Territory territory = new Territory("testing", 1);
    assertEquals("testing", territory.getName());
    assertEquals("testing", territory.toString());
//    assertEquals(1, territory.getNumUnits());
  }
  /**
   * test_get_num_units() is to test if getNumUnits will return correct unit number
   */
  @Test
  public void test_get_num_units() {
    Territory territory = new Territory("testing", 1);
    assertEquals(1, territory.getNumUnits());
  }
  /**
   * test_add_units() is to test after calling addUnits(),
   * the territory will have correct unit number or not
   */
  @Test
  public void test_add_units() {
    Territory territory = new Territory("testing", 1);
    assertEquals(1, territory.getNumUnits());
    territory.addUnits(5);
    assertEquals(6, territory.getNumUnits());
  }

  /**
   * test_add_units() is to test after calling setUnits(),
   * the territory will have correct unit number or not
   */
  @Test
  public void test_set_units() {
    Territory territory = new Territory("testing", 1);
    assertEquals(1, territory.getNumUnits());
    territory.setUnits(5);
    assertEquals(5, territory.getNumUnits());
  }

  /**
   * test_equals() tests if two territory are the same if they have the same
   * numUnits and same name
   */
  @Test
  public void test_equals() {
    Territory territory1 = new Territory("testing", 1);
    Territory territory2 = new Territory("testing", 1);
    Player player = new Player("p1");
    assertTrue(territory1.equals(territory2));
    assertFalse(territory1.equals(player));
  }

}
