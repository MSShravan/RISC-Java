package edu.duke.ece651.team4.shared;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class CollegeMapFactoryTest {
  @Test
  public void test_illegalnumPlayers() {
    AbstractMapFactory fact = new CollegeMapFactory();
    assertThrows(IllegalArgumentException.class, () -> fact.createMap(1));
    assertThrows(IllegalArgumentException.class, () -> fact.createMap(5));
  }

  @Test
  public void test_all_Players() {
    test_X_Players(2);
    test_X_Players(3);
    test_X_Players(4);
  }

  public void test_X_Players(int numPlayers) {
    AbstractMapFactory fact = new CollegeMapFactory();
    RISCMap map = fact.createMap(numPlayers);
    Iterator<Player> player = map.getPlayers();
    String[] playerNames = { "Red", "Blue", "Green", "Yellow" };
    String[] terrNames = get_strings();
    int counter = 0;
    while (player.hasNext()) {
      Player p = player.next();
      assertEquals(p.getName(), playerNames[counter]);
      Iterator<Territory> terr = p.getTerritories();
      int numT = 0;
      int start_ind = counter * 24 / numPlayers;
      int end_ind = (counter + 1) * 24 / numPlayers;
      HashSet<String> terrs = new HashSet<String>(
          Arrays.asList(Arrays.copyOfRange(terrNames, start_ind, end_ind)));
      while (terr.hasNext()) {
        Territory t = terr.next();
        assertTrue(terrs.contains(t.getName()));
        numT++;
      }
      assertEquals(numT, 24 / numPlayers);
      counter++;
    }
    assertEquals(counter, numPlayers);
    assertEquals(240 / numPlayers, map.getMaxNumUnits());
  }

  @Test
  public void test_mapNumber() {
    AbstractMapFactory fact = new CollegeMapFactory();
    RISCMap map = fact.createMap(2);
    Set<Territory> uniqueTerritories = new HashSet<Territory>();
    assertEquals(map.getNumTerritory(), 24);
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
    assertEquals(uniqueTerritories.size(), 24);
    assertEquals(240 / 2, map.getMaxNumUnits());
  }

  public String[] get_strings() {
    String[] ans = { "Michigan", "Michigan State", "Northwestern", "Indiana", "Syracuse", "Ohio State", "Penn State",
        "UConn", "Villanova", "UPenn", "Maryland", "WVU", "Kentucky", "UVA", "Illinois", "Duke", "Tennessee", "Alabama",
        "Georgia", "Auburn", "South Carolina", "Florida", "UNC", "NC State" };
    return ans;
  }

}
