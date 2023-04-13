package edu.duke.ece651.team4.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;

public class PlayerTest {
  /**
   * test_get_name() is to test if getName() will return correct player name
   */
  @Test
  public void test_get_name() {
    Player player = new Player("testplayer");
    assertEquals("testplayer", player.getName());
  }

  /**
   * test_get_territories() is to test if getTerritories() will get correct
   * iterator
   */
  @Test
  public void test_get_territories() {
    Player player = new Player("testplayer");
    Territory territory = new Territory("testing", 1);
    player.addTerritory(territory);
    // testing
    Iterator<Territory> it = player.getTerritories();
    int size = 0;
    // test the name of that iterator is "testing"
    while (it.hasNext()) {
      Territory nextTerritory = it.next();
      assertEquals("testing", nextTerritory.getName());
      size++;
    }
    // test only 1 element in iterator
    assertEquals(1, size);
  }

  /**
   * test_remove_territory() is to test if removeTerritory() can successfully
   * remove specified elements in arrayList
   */
  @Test
  public void test_remove_territory() {
    Player player = new Player("testplayer");
    Territory territory = new Territory("testing", 1);
    Territory territory1 = new Territory("testing1", 2);
    // add territory to list
    player.addTerritory(territory);
    player.addTerritory(territory1);
    // testing
    Iterator<Territory> it = player.getTerritories();
    boolean found = false;
    // check "testing" exists in the arrayList before remove
    while (it.hasNext()) {
      Territory nextTerritory = it.next();
      if (nextTerritory.getName().equals("testing")) {
        found = true;
      }
    }
    // test it can be found
    assertTrue(found);
    // remove it from territories
    player.removeTerritory(territory);
    // test it cannot be found after remove
    it = player.getTerritories();
    while (it.hasNext()) {
      Territory nextTerritory = it.next();
      assertNotEquals("testing", nextTerritory.getName());
    }
  }

  /**
   * test_is_alive() is to test if isAlive() method will
   * return true if there's more than 0 territories
   * return false if there's 0 territory
   */
  @Test
  public void test_is_alive() {
    Player player = new Player("testplayer");
    // false if no territory
    assertFalse(player.isAlive());
    // true if more than 0 territory
    Territory territory = new Territory("testing", 1);
    player.addTerritory(territory);
    assertTrue(player.isAlive());
  }

  @Test
  public void test_validate() {
    OnePlayerTurn turn = new OnePlayerTurn();
    AbstractMapFactory fact = new CollegeMapFactory();
    RISCMap map = fact.createMap(4);
    Iterator<Player> players = map.getPlayers();
    while (players.hasNext()) {
      Player p = players.next();
      assertEquals(p.validateTurn(turn, map), null);
    }
  }

  @Test
  public void test_not_found_return_null() {
    Player p1 = new Player("p1");
    assertNull(p1.getTerritoryByName("t1"));
  }

  @Test
  public void test_assign_units() {
    // init assign map
    Territory t1 = new Territory("t1", 0);
    Territory t2 = new Territory("t2", 0);
    HashMap<Territory, Integer> assignMap = new HashMap<>();
    assignMap.put(t1, 5);
    assignMap.put(t2, 10);
    // init RISCMap
    RISCMap riscMap = new RISCMap();
    riscMap.addTerritoryNeighbor(t1, t2);
    riscMap.addTerritoryNeighbor(t2, t1);
    // init player
    Player p1 = new Player("p1");
    p1.addTerritory(t1);
    p1.addTerritory(t2);
    // player assign units
    p1.assignUnits(assignMap);
    // check the actual units in the RISCMap are correct
    // t1 is 5, t2 is 10
    Territory terrRes1 = riscMap.getTerritoryByName("t1");
    assertEquals(5, terrRes1.getNumUnits());
    Territory terrRes2 = riscMap.getTerritoryByName("t2");
    assertEquals(10, terrRes2.getNumUnits());
  }

  @Test
  public void test_move() {
    RISCMap map = new RISCMap();
    Player red = new Player("Red");
    Player blue = new Player("Blue");
    Territory Duke = new Territory("Duke", 10);
    Territory UNC = new Territory("UNC", 5);
    Territory NCState = new Territory("NC State", 3);
    Territory OhioState = new Territory("Ohio State", 15);
    Territory Georgia = new Territory("Georgia", 8);
    OnePlayerTurn invalidToTerr = new OnePlayerTurn();
    invalidToTerr.addMoveAction(new Action(NCState, Duke, 2));
    invalidToTerr.addMoveAction(new Action(OhioState, NCState, 2));
    OnePlayerTurn invalidFromTerr = new OnePlayerTurn();
    invalidFromTerr.addMoveAction(new Action(NCState, NCState, 2));
    invalidFromTerr.addMoveAction(new Action(NCState, Duke, 2));
    red.addTerritory(Duke);
    red.addTerritory(UNC);
    red.addTerritory(NCState);
    red.addTerritory(Georgia);
    blue.addTerritory(OhioState);
    map.addPlayer(red);
    map.addPlayer(blue);
    map.addTerritoryNeighbor(Duke, UNC);
    map.addTerritoryNeighbor(Duke, NCState);
    map.addTerritoryNeighbor(Duke, OhioState);
    map.addTerritoryNeighbor(NCState, UNC);
    map.addTerritoryNeighbor(NCState, Duke);
    map.addTerritoryNeighbor(NCState, OhioState);
    map.addTerritoryNeighbor(OhioState, Duke);
    map.addTerritoryNeighbor(OhioState, UNC);
    map.addTerritoryNeighbor(OhioState, NCState);
    map.addTerritoryNeighbor(UNC, Duke);
    map.addTerritoryNeighbor(UNC, NCState);
    map.addTerritoryNeighbor(UNC, OhioState);
    map.addTerritoryNeighbor(Georgia, null);
    OnePlayerTurn invalidNumMove = new OnePlayerTurn();
    invalidNumMove.addMoveAction(new Action(NCState, Duke, 5));
    invalidNumMove.addMoveAction(new Action(UNC, Duke, 6));
    OnePlayerTurn validMove = new OnePlayerTurn();
    validMove.addMoveAction(new Action(NCState, Duke, 5));
    validMove.addMoveAction(new Action(UNC, Duke, 5));
    validMove.addMoveAction(new Action(NCState, UNC, 3));
    OnePlayerTurn invalidPath = new OnePlayerTurn();
    invalidPath.addMoveAction(new Action(Georgia, Duke, 1));
    assertEquals(
        "Player must be the owner of both territories in a move. Player does not own both Territory Ohio State and Territory NC State.",
        red.validateTurn(invalidToTerr, map));
    assertNotEquals(
        "Player must be the owner of both territories in a move. Player does not own both Territory NC State and Territory Ohio State.",
        red.validateTurn(invalidFromTerr, map));
    assertEquals(
        "A players move actions must not result in any of their Territories containing negative units. Territory Duke is left with negative units.",
        red.validateTurn(invalidNumMove, map));
    assertEquals(null, red.validateTurn(validMove, map));
    assertEquals("There is no valid move path between Territory Duke and Territory Georgia.",
        red.validateTurn(invalidPath, map));
  }

  @Test
  public void test_attack() {
    AbstractMapFactory fact = new TestMap();
    RISCMap map = fact.createMap(2);
    Iterator<Player> players = map.getPlayers();
    OnePlayerTurn wrongOwnerFrom = new OnePlayerTurn();
    OnePlayerTurn wrongOwnerTo = new OnePlayerTurn();
    OnePlayerTurn notAdjacent = new OnePlayerTurn();
    OnePlayerTurn negUnits = new OnePlayerTurn();
    OnePlayerTurn correct = new OnePlayerTurn();
    for (Territory t : map.getTerritories()) {
      t.addUnits(10);
    }
    wrongOwnerTo.addAttackAction(new Action(map.getTerritoryByName("KY"), map.getTerritoryByName("VA"), 5));
    wrongOwnerFrom.addAttackAction(new Action(map.getTerritoryByName("NC"), map.getTerritoryByName("SC"), 5));
    notAdjacent.addAttackAction(new Action(map.getTerritoryByName("SC"), map.getTerritoryByName("TN"), 5));
    negUnits.addMoveAction(new Action(map.getTerritoryByName("KY"), map.getTerritoryByName("VA"), 8));
    negUnits.addAttackAction(new Action(map.getTerritoryByName("NC"), map.getTerritoryByName("VA"), 3));
    correct.addMoveAction(new Action(map.getTerritoryByName("AL"), map.getTerritoryByName("TN"), 5));
    correct.addAttackAction(new Action(map.getTerritoryByName("FL"), map.getTerritoryByName("AL"), 8));
    correct.addAttackAction(new Action(map.getTerritoryByName("GA"), map.getTerritoryByName("AL"), 7));
    while (players.hasNext()) {
      Player p = players.next();
      if (p.getName() == "Red") {
        String wrongTerr = "For an attack to be valid the player must own the territory it is attacking from and not own the territory it is attacking to. The attack from Territory ";
        assertEquals(
            wrongTerr + "VA to Territory KY is not valid.",
            p.validateTurn(wrongOwnerTo, map));
        assertEquals(wrongTerr + "SC to Territory NC is not valid.", p.validateTurn(wrongOwnerFrom, map));
        assertEquals(
            "Territories in an attack must be directly adjacent. Territory TN and Territory SC are not adjacent.",
            p.validateTurn(notAdjacent, map));
        assertEquals(
            "Attack actions must not result in any Territory ending with negative units. Territory VA has negative units.",
            p.validateTurn(negUnits, map));
        assertEquals(null, p.validateTurn(correct, map));
      }

    }
  }
}
