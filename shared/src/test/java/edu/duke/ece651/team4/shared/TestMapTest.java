package edu.duke.ece651.team4.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class TestMapTest {
  @Test
  public void test_illegal_players() {
    AbstractMapFactory fact = new TestMap();
    assertThrows(IllegalArgumentException.class, () -> fact.createMap(1));
    assertThrows(IllegalArgumentException.class, () -> fact.createMap(5));
    assertThrows(IllegalArgumentException.class, () -> fact.createMap(3));
  }

  @Test
  public void test_territories() {
    AbstractMapFactory fact = new TestMap();
    RISCMap map = fact.createMap(2);
    Set<Territory> uniqueTerritories = new HashSet<Territory>();
    assertEquals(map.getNumTerritory(), 8);
    for (Territory t : map.getTerritories()) {
      uniqueTerritories.add(t);
      boolean containsNeighbor = false;
      for (Territory n : map.getNeighbors(t)) {
        for (Territory t2 : map.getNeighbors(n)) {
          if (t2 == t) {
            containsNeighbor = true;
          }
        }
        assertTrue(containsNeighbor);
      }
    }
    assertEquals(uniqueTerritories.size(), 8);
  }

  @Test
  public void test_2players() {
    AbstractMapFactory fact = new TestMap();
    RISCMap map = fact.createMap(2);
    Iterator<Player> player = map.getPlayers();
    Player red = player.next();
    assertEquals(red.getName(), "Red");
    Player blue = player.next();
    assertEquals(blue.getName(), "Blue"); 
  }
}
