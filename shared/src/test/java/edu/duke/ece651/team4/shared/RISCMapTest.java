package edu.duke.ece651.team4.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class RISCMapTest {
  /**
   * test_get_neighbors() is to test if the map created contains the neighbor we
   * added
   */
  @Test
  public void test_get_neighbors() {
    Territory territory1 = new Territory("territory1", 1);
    Territory neighbor1 = new Territory("neighbor1", 2);
    Territory neighbor2 = new Territory("neighbor2", 3);
    RISCMap testMap = new RISCMap();
    // add neighbor1 as the neighbor of territory1
    testMap.addTerritoryNeighbor(territory1, neighbor1);
    // get neighbors of territory1
    ArrayList<Territory> neighborsList = testMap.getNeighbors(territory1);
    // test size is 1
    assertEquals(1, neighborsList.size());
    // test it contains neighbor1
    assertTrue(neighborsList.contains(neighbor1));
    // add new neighbor
    testMap.addTerritoryNeighbor(territory1, neighbor2);
    // test the size is 2
    assertEquals(2, neighborsList.size());
    // test it contains neighbor2
    assertTrue(neighborsList.contains(neighbor2));
    assertNull(testMap.getNeighbors(neighbor1));
  }

  @Test
  public void test_get_territory_name() {
    AbstractMapFactory fact = new TestMap();
    RISCMap map = fact.createMap(2);
    assertThrows(IllegalArgumentException.class, () -> map.getTerritoryByName("test"));
  }

  /**
   * test_num_territory() is to test if getNumTerritory() will return
   * correct number of territories in the map
   */
  @Test
  public void test_get_num_territory() {
    Territory territory1 = new Territory("territory1", 1);
    Territory neighbor1 = new Territory("neighbor1", 2);
    Territory neighbor2 = new Territory("neighbor2", 3);
    RISCMap testMap = new RISCMap();
    // add neighbor1 as the neighbor of territory1
    testMap.addTerritoryNeighbor(territory1, neighbor1);
    assertEquals(1, testMap.getNumTerritory());
    // add territory1 as the neighbor of neighbor1
    testMap.addTerritoryNeighbor(neighbor1, territory1);
    assertEquals(2, testMap.getNumTerritory());
  }

  /**
   * test_get_num_players() is to test if getNumPlayers() will
   * return correct number of players
   */
  @Test
  public void test_get_num_players() {
    Player player = new Player("testplayer");
    Player player1 = new Player("testplayer1");
    RISCMap testMap = new RISCMap();
    // add 1 player, test the size is 1
    testMap.addPlayer(player);
    assertEquals(1, testMap.getNumPlayers());
    // add 1 more player, test the size is 2
    testMap.addPlayer(player1);
    assertEquals(2, testMap.getNumPlayers());
  }

  /**
   * test_get_players() is to test if getPlayers() will
   * return correct iterator to player list
   */
  @Test
  public void test_get_players() {
    Player player = new Player("testplayer");
    Player player1 = new Player("testplayer1");
    RISCMap testMap = new RISCMap();
    testMap.addPlayer(player);
    testMap.addPlayer(player1);

    Iterator<Player> it = testMap.getPlayers();
    int idx = 0;
    // test first player name to be testplayer
    // test second player name to be testplayer1
    while (it.hasNext()) {
      Player nextPlayer = it.next();
      if (idx == 0) {
        assertEquals("testplayer", nextPlayer.getName());
      }
      if (idx == 1) {
        assertEquals("testplayer1", nextPlayer.getName());
      }
      idx++;
    }
  }

  /**
   * test_do_one_turn_move_only() is to test if we can execute all the
   * move actions correctly
   */
  @Test
  public void test_do_one_turn_move_only() {
    // add move actions to onePlayerTurn

    Territory t1 = new Territory("t1", 10);
    Territory t2 = new Territory("t2", 10);
    Territory t3 = new Territory("t3", 10);
    // t1 -> t2 : 5
    // t2 -> t3 : 15
    Action a1 = new Action(t2, t1, 5);
    Action a2 = new Action(t3, t2, 15);
    OnePlayerTurn onePlayerTurn = new OnePlayerTurn();
    onePlayerTurn.addMoveAction(a1);
    onePlayerTurn.addMoveAction(a2);

    // add onePlayerTurn to arrayList
    ArrayList<OnePlayerTurn> playerTurnList = new ArrayList<>();
    playerTurnList.add(onePlayerTurn);

    RISCMap map = new RISCMap();
    // build map t1-t2-t3
    map.addTerritoryNeighbor(t1, t2);
    map.addTerritoryNeighbor(t2, t1);
    map.addTerritoryNeighbor(t2, t3);
    map.addTerritoryNeighbor(t3, t2);
    // execute action
    map.doOneTurn(playerTurnList);

    // get all the territories, check their number of units
    Territory terr1 = map.getTerritoryByName("t1");
    Territory terr2 = map.getTerritoryByName("t2");
    Territory terr3 = map.getTerritoryByName("t3");

    // In the end , terr1 will have 5, terr2 will have 0, terr 3 will have 25
    // And after adding 1 unit, terr1 will have 6, terr2 will have 1, terr 3 will have 26
    assertEquals(6, terr1.getNumUnits());
    assertEquals(1, terr2.getNumUnits());
    assertEquals(26, terr3.getNumUnits());
  }

  /**
   * test_get_set_max_num_units() is to test
   * if getter and setter for maxNumUnitsForEachPlayer work correctly
   */
  @Test
  public void test_get_set_max_num_units() {
    RISCMap testMap = new RISCMap();
    // initially 0
    assertEquals(0, testMap.getMaxNumUnits());
    // after set -> 20
    testMap.setMaxNumUnits(20);
    // assert it is 20
    assertEquals(20, testMap.getMaxNumUnits());
  }

  /**
   * test_assign_units_given_player_object() is to test if
   * assignUnitsForOnePlayer() will assign units for given players correctly
   */
  @Test
  public void test_assign_units_given_player_object() {
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
    riscMap.addPlayer(p1);
    p1.addTerritory(t1);
    p1.addTerritory(t2);
    // player assign units
    riscMap.assignUnitsForOnePlayer(p1, assignMap);
    // check the actual units in the RISCMap are correct
    // t1 is 5, t2 is 10
    Territory terrRes1 = riscMap.getTerritoryByName("t1");
    assertEquals(5, terrRes1.getNumUnits());
    Territory terrRes2 = riscMap.getTerritoryByName("t2");
    assertEquals(10, terrRes2.getNumUnits());
  }

  /**
   * test_get_player_by_name() is to test if
   * getPlayerByName() will return null when specified player
   * does not exist in the list
   */
  @Test
  public void test_get_player_by_name() {
    RISCMap riscMap = new RISCMap();
    Player p1 = new Player("p1");
    riscMap.addPlayer(p1);
    assertNull(riscMap.getPlayerByName("testName"));
  }

  /**
   * test_execute_attack_one_attack_with_empty_territory() will test the ownership
   * if the target territory does not have and defender
   */
  @Test
  public void test_execute_attack_one_attack_with_empty_territory() {
    RISCMap map = new RISCMap();

    Territory A = new Territory("A", 10);
    Territory B = new Territory("B", 10);
    Territory C = new Territory("C", 10);
    Territory D = new Territory("D", 10);
    Territory E = new Territory("E", 10);
    Territory F = new Territory("F", 10);
    Territory G = new Territory("G", 0);
    Territory H = new Territory("H", 10);

    Player p1 = new Player("p1");
    Player p2 = new Player("p2");
    Player p3 = new Player("p3");

    map.addPlayer(p1);
    map.addPlayer(p2);
    map.addPlayer(p3);

    // build map
    map.addTerritoryNeighbor(A, B);
    map.addTerritoryNeighbor(B, A);
    map.addTerritoryNeighbor(A, C);
    map.addTerritoryNeighbor(C, A);
    map.addTerritoryNeighbor(A, G);
    map.addTerritoryNeighbor(G, A);
    map.addTerritoryNeighbor(B, H);
    map.addTerritoryNeighbor(H, B);
    map.addTerritoryNeighbor(C, D);
    map.addTerritoryNeighbor(D, C);
    map.addTerritoryNeighbor(D, G);
    map.addTerritoryNeighbor(G, D);
    map.addTerritoryNeighbor(D, E);
    map.addTerritoryNeighbor(E, D);
    map.addTerritoryNeighbor(E, G);
    map.addTerritoryNeighbor(G, E);
    map.addTerritoryNeighbor(E, F);
    map.addTerritoryNeighbor(F, E);
    map.addTerritoryNeighbor(F, H);
    map.addTerritoryNeighbor(H, F);
    map.addTerritoryNeighbor(G, H);
    map.addTerritoryNeighbor(H, G);

    // claim ownership
    p1.addTerritory(A);
    p1.addTerritory(B);
    p1.addTerritory(C);

    p2.addTerritory(D);
    p2.addTerritory(E);
    p2.addTerritory(F);

    p3.addTerritory(G);
    p3.addTerritory(H);

    // attack actions
    Action a1 = new Action(G, A, 5);
    OnePlayerTurn onePlayerTurn = new OnePlayerTurn();
    onePlayerTurn.addAttackAction(a1);

    // add onePlayerTurn to arrayList
    ArrayList<OnePlayerTurn> playerTurnList = new ArrayList<>();
    playerTurnList.add(onePlayerTurn);
    map.doOneTurn(playerTurnList);
    assertFalse(p3.ownsTerritory(G));
  }

  /**
   * test_execute_attack_one_attack_with_empty_territory() will test the ownership
   * if the target territory has defender and it has only one attacker
   */
  @Test
  public void test_execute_attack_one_attack() {
    RISCMap map = new RISCMap();

    Territory A = new Territory("A", 10);
    Territory B = new Territory("B", 10);
    Territory C = new Territory("C", 10);
    Territory D = new Territory("D", 10);
    Territory E = new Territory("E", 10);
    Territory F = new Territory("F", 10);
    Territory G = new Territory("G", 10);
    Territory H = new Territory("H", 10);

    Player p1 = new Player("p1");
    Player p2 = new Player("p2");
    Player p3 = new Player("p3");

    map.addPlayer(p1);
    map.addPlayer(p2);
    map.addPlayer(p3);

    // build map
    map.addTerritoryNeighbor(A, B);
    map.addTerritoryNeighbor(B, A);
    map.addTerritoryNeighbor(A, C);
    map.addTerritoryNeighbor(C, A);
    map.addTerritoryNeighbor(A, G);
    map.addTerritoryNeighbor(G, A);
    map.addTerritoryNeighbor(B, H);
    map.addTerritoryNeighbor(H, B);
    map.addTerritoryNeighbor(C, D);
    map.addTerritoryNeighbor(D, C);
    map.addTerritoryNeighbor(D, G);
    map.addTerritoryNeighbor(G, D);
    map.addTerritoryNeighbor(D, E);
    map.addTerritoryNeighbor(E, D);
    map.addTerritoryNeighbor(E, G);
    map.addTerritoryNeighbor(G, E);
    map.addTerritoryNeighbor(E, F);
    map.addTerritoryNeighbor(F, E);
    map.addTerritoryNeighbor(F, H);
    map.addTerritoryNeighbor(H, F);
    map.addTerritoryNeighbor(G, H);
    map.addTerritoryNeighbor(H, G);

    // claim ownership
    p1.addTerritory(A);
    p1.addTerritory(B);
    p1.addTerritory(C);

    p2.addTerritory(D);
    p2.addTerritory(E);
    p2.addTerritory(F);

    p3.addTerritory(G);
    p3.addTerritory(H);

    // attack actions
    Action a1 = new Action(G, A, 5);
    OnePlayerTurn onePlayerTurn = new OnePlayerTurn();
    onePlayerTurn.addAttackAction(a1);

    // add onePlayerTurn to arrayList
    ArrayList<OnePlayerTurn> playerTurnList = new ArrayList<>();
    playerTurnList.add(onePlayerTurn);
    map.doOneTurn(playerTurnList);
    assertTrue(p3.ownsTerritory(G) || p1.ownsTerritory(G));
  }

  /**
   * test_execute_attack_multi_attack() tests the correctness of
   * executeAttackActions()
   * by having many players attacking together at same territory
   *
   */
  @Test
  public void test_execute_attack_multi_attack() {
    RISCMap map = new RISCMap();

    Territory A = new Territory("A", 10);
    Territory B = new Territory("B", 10);
    Territory C = new Territory("C", 10);
    Territory D = new Territory("D", 10);
    Territory E = new Territory("E", 10);
    Territory F = new Territory("F", 10);
    Territory G = new Territory("G", 0);
    Territory H = new Territory("H", 10);
    Territory I = new Territory("I", 10);

    Player p1 = new Player("p1");
    Player p2 = new Player("p2");
    Player p3 = new Player("p3");
    Player p4 = new Player("p4");

    map.addPlayer(p1);
    map.addPlayer(p2);
    map.addPlayer(p3);
    map.addPlayer(p4);

    // build map
    map.addTerritoryNeighbor(A, B);
    map.addTerritoryNeighbor(B, A);
    map.addTerritoryNeighbor(A, C);
    map.addTerritoryNeighbor(C, A);
    map.addTerritoryNeighbor(A, G);
    map.addTerritoryNeighbor(G, A);
    map.addTerritoryNeighbor(B, H);
    map.addTerritoryNeighbor(H, B);
    map.addTerritoryNeighbor(C, D);
    map.addTerritoryNeighbor(D, C);
    map.addTerritoryNeighbor(D, G);
    map.addTerritoryNeighbor(G, D);
    map.addTerritoryNeighbor(D, E);
    map.addTerritoryNeighbor(E, D);
    map.addTerritoryNeighbor(E, G);
    map.addTerritoryNeighbor(G, E);
    map.addTerritoryNeighbor(E, F);
    map.addTerritoryNeighbor(F, E);
    map.addTerritoryNeighbor(F, H);
    map.addTerritoryNeighbor(H, F);
    map.addTerritoryNeighbor(G, H);
    map.addTerritoryNeighbor(H, G);
    map.addTerritoryNeighbor(I, G);
    map.addTerritoryNeighbor(G, I);

    // claim ownership
    p1.addTerritory(A);
    p1.addTerritory(B);
    p1.addTerritory(C);

    p2.addTerritory(D);
    p2.addTerritory(E);
    p2.addTerritory(F);

    p3.addTerritory(G);
    p3.addTerritory(H);

    p4.addTerritory(I);

    // attack actions
    Action a1 = new Action(G, A, 5);
    Action a2 = new Action(G, D, 5);
    Action a3 = new Action(G, E, 2);
    Action a4 = new Action(G, I, 5);
    OnePlayerTurn onePlayerTurn = new OnePlayerTurn();
    onePlayerTurn.addAttackAction(a1);
    onePlayerTurn.addAttackAction(a2);
    onePlayerTurn.addAttackAction(a3);
    onePlayerTurn.addAttackAction(a4);

    // add onePlayerTurn to arrayList
    ArrayList<OnePlayerTurn> playerTurnList = new ArrayList<>();
    playerTurnList.add(onePlayerTurn);
    map.doOneTurn(playerTurnList);
    assertFalse(p3.ownsTerritory(G));
    assertTrue(p1.ownsTerritory(G) || p2.ownsTerritory(G) || p4.ownsTerritory(G));
  }

  @Test
  public void test_player_own_territory_return_null() {
    RISCMap map = new RISCMap();
    Territory t1 = new Territory("t1", 0);
    assertNull(map.playerOwnTerritory(t1));
  }

  @Test
  public void test_multi_attack_with_test_dice() {
    TestDice testDice = new TestDice();
    RISCMap map = new RISCMap(testDice);

    Territory A = new Territory("A", 10);
    Territory B = new Territory("B", 10);
    Territory C = new Territory("C", 10);
    Territory D = new Territory("D", 10);
    Territory E = new Territory("E", 10);
    Territory F = new Territory("F", 10);
    Territory G = new Territory("G", 0);
    Territory H = new Territory("H", 10);
    Territory I = new Territory("I", 10);

    Player p1 = new Player("p1");
    Player p2 = new Player("p2");
    Player p3 = new Player("p3");
    Player p4 = new Player("p4");

    map.addPlayer(p1);
    map.addPlayer(p2);
    map.addPlayer(p3);
    map.addPlayer(p4);

    // build map
    map.addTerritoryNeighbor(A, B);
    map.addTerritoryNeighbor(B, A);
    map.addTerritoryNeighbor(A, C);
    map.addTerritoryNeighbor(C, A);
    map.addTerritoryNeighbor(A, G);
    map.addTerritoryNeighbor(G, A);
    map.addTerritoryNeighbor(B, H);
    map.addTerritoryNeighbor(H, B);
    map.addTerritoryNeighbor(C, D);
    map.addTerritoryNeighbor(D, C);
    map.addTerritoryNeighbor(D, G);
    map.addTerritoryNeighbor(G, D);
    map.addTerritoryNeighbor(D, E);
    map.addTerritoryNeighbor(E, D);
    map.addTerritoryNeighbor(E, G);
    map.addTerritoryNeighbor(G, E);
    map.addTerritoryNeighbor(E, F);
    map.addTerritoryNeighbor(F, E);
    map.addTerritoryNeighbor(F, H);
    map.addTerritoryNeighbor(H, F);
    map.addTerritoryNeighbor(G, H);
    map.addTerritoryNeighbor(H, G);
    map.addTerritoryNeighbor(I, G);
    map.addTerritoryNeighbor(G, I);

    // claim ownership
    p1.addTerritory(A);
    p1.addTerritory(B);
    p1.addTerritory(C);

    p2.addTerritory(D);
    p2.addTerritory(E);
    p2.addTerritory(F);

    p3.addTerritory(G);
    p3.addTerritory(H);

    p4.addTerritory(I);

    // attack actions
    Action a1 = new Action(G, A, 3);
    Action a2 = new Action(G, D, 3);
    Action a3 = new Action(G, E, 1);
    Action a4 = new Action(G, I, 3);
    OnePlayerTurn onePlayerTurn = new OnePlayerTurn();
    onePlayerTurn.addAttackAction(a1);
    onePlayerTurn.addAttackAction(a2);
    onePlayerTurn.addAttackAction(a3);
    onePlayerTurn.addAttackAction(a4);

    // add onePlayerTurn to arrayList
    ArrayList<OnePlayerTurn> playerTurnList = new ArrayList<>();
    playerTurnList.add(onePlayerTurn);
    map.doOneTurn(playerTurnList);
    assertFalse(p3.ownsTerritory(G));
    assertTrue(p1.ownsTerritory(G));
    assertEquals(2, G.getNumUnits());
  }

  @Test
  public void test_multi_attack_with_test_dice_more_test() {
    TestDice testDice = new TestDice();
    RISCMap map = new RISCMap(testDice);

    Territory A = new Territory("A", 10);
    Territory B = new Territory("B", 10);
    Territory C = new Territory("C", 10);
    Territory D = new Territory("D", 10);
    Territory E = new Territory("E", 10);
    Territory F = new Territory("F", 10);
    Territory G = new Territory("G", 3);
    Territory H = new Territory("H", 10);
    Territory I = new Territory("I", 10);

    Player p1 = new Player("p1");
    Player p2 = new Player("p2");
    Player p3 = new Player("p3");
    Player p4 = new Player("p4");

    map.addPlayer(p1);
    map.addPlayer(p2);
    map.addPlayer(p3);
    map.addPlayer(p4);

    // build map
    map.addTerritoryNeighbor(A, B);
    map.addTerritoryNeighbor(B, A);
    map.addTerritoryNeighbor(A, C);
    map.addTerritoryNeighbor(C, A);
    map.addTerritoryNeighbor(A, G);
    map.addTerritoryNeighbor(G, A);
    map.addTerritoryNeighbor(B, H);
    map.addTerritoryNeighbor(H, B);
    map.addTerritoryNeighbor(C, D);
    map.addTerritoryNeighbor(D, C);
    map.addTerritoryNeighbor(D, G);
    map.addTerritoryNeighbor(G, D);
    map.addTerritoryNeighbor(D, E);
    map.addTerritoryNeighbor(E, D);
    map.addTerritoryNeighbor(E, G);
    map.addTerritoryNeighbor(G, E);
    map.addTerritoryNeighbor(E, F);
    map.addTerritoryNeighbor(F, E);
    map.addTerritoryNeighbor(F, H);
    map.addTerritoryNeighbor(H, F);
    map.addTerritoryNeighbor(G, H);
    map.addTerritoryNeighbor(H, G);
    map.addTerritoryNeighbor(I, G);
    map.addTerritoryNeighbor(G, I);

    // claim ownership
    p1.addTerritory(A);
    p1.addTerritory(B);
    p1.addTerritory(C);

    p2.addTerritory(D);
    p2.addTerritory(E);
    p2.addTerritory(F);

    p3.addTerritory(G);
    p3.addTerritory(H);

    p4.addTerritory(I);

    // attack actions
    Action a2 = new Action(G, D, 3);
    Action a3 = new Action(G, E, 1);
    Action a4 = new Action(G, I, 3);
    OnePlayerTurn onePlayerTurn = new OnePlayerTurn();
    onePlayerTurn.addAttackAction(a2);
    onePlayerTurn.addAttackAction(a3);
    onePlayerTurn.addAttackAction(a4);

    // add onePlayerTurn to arrayList
    ArrayList<OnePlayerTurn> playerTurnList = new ArrayList<>();
    playerTurnList.add(onePlayerTurn);
    map.doOneTurn(playerTurnList);
    assertTrue(p3.ownsTerritory(G));
    assertEquals(2, G.getNumUnits());
  }

  @Test
  public void test_multi_attack_with_test_dice_get_attacks_in_last_turn() {
    TestDice testDice = new TestDice();
    RISCMap map = new RISCMap(testDice);

    Territory A = new Territory("A", 0);
    Territory B = new Territory("B", 10);
    Territory C = new Territory("C", 10);
    Territory D = new Territory("D", 10);
    Territory E = new Territory("E", 10);
    Territory F = new Territory("F", 10);
    Territory G = new Territory("G", 3);
    Territory H = new Territory("H", 10);
    Territory I = new Territory("I", 10);

    Player p1 = new Player("p1");
    Player p2 = new Player("p2");
    Player p3 = new Player("p3");
    Player p4 = new Player("p4");

    map.addPlayer(p1);
    map.addPlayer(p2);
    map.addPlayer(p3);
    map.addPlayer(p4);

    // build map
    map.addTerritoryNeighbor(A, B);
    map.addTerritoryNeighbor(B, A);
    map.addTerritoryNeighbor(A, C);
    map.addTerritoryNeighbor(C, A);
    map.addTerritoryNeighbor(A, G);
    map.addTerritoryNeighbor(G, A);
    map.addTerritoryNeighbor(B, H);
    map.addTerritoryNeighbor(H, B);
    map.addTerritoryNeighbor(C, D);
    map.addTerritoryNeighbor(D, C);
    map.addTerritoryNeighbor(D, G);
    map.addTerritoryNeighbor(G, D);
    map.addTerritoryNeighbor(D, E);
    map.addTerritoryNeighbor(E, D);
    map.addTerritoryNeighbor(E, G);
    map.addTerritoryNeighbor(G, E);
    map.addTerritoryNeighbor(E, F);
    map.addTerritoryNeighbor(F, E);
    map.addTerritoryNeighbor(F, H);
    map.addTerritoryNeighbor(H, F);
    map.addTerritoryNeighbor(G, H);
    map.addTerritoryNeighbor(H, G);
    map.addTerritoryNeighbor(I, G);
    map.addTerritoryNeighbor(G, I);

    // claim ownership
    p1.addTerritory(A);
    p1.addTerritory(B);
    p1.addTerritory(C);

    p2.addTerritory(D);
    p2.addTerritory(E);
    p2.addTerritory(F);

    p3.addTerritory(G);
    p3.addTerritory(H);

    p4.addTerritory(I);

    // attack actions
    Action a2 = new Action(G, D, 3);
    Action a3 = new Action(G, E, 1);
    Action a4 = new Action(G, I, 3);
    Action a5 = new Action(A, H, 2);
    OnePlayerTurn onePlayerTurn = new OnePlayerTurn();
    onePlayerTurn.addAttackAction(a2);
    onePlayerTurn.addAttackAction(a3);
    onePlayerTurn.addAttackAction(a4);
    onePlayerTurn.addAttackAction(a5);

    // add onePlayerTurn to arrayList
    ArrayList<OnePlayerTurn> playerTurnList = new ArrayList<>();
    playerTurnList.add(onePlayerTurn);
    map.doOneTurn(playerTurnList);
    assertTrue(p3.ownsTerritory(G));
    assertEquals(2, G.getNumUnits());

    // ensure it only has one element in the attacksInLastTurn
    Iterator<ResolvedAttack> it = map.getAllAttacks();
    int idx = 0;
    while (it.hasNext()) {
      ResolvedAttack nextAttack = it.next();
      // If that the territory of that ResolvedAttack is G
      if (nextAttack.getAttackedTerritory().equals(G)) {
        // p3 is the winner
        assertEquals(p3, nextAttack.getWinner());
        // p3 is the previous owner
        assertEquals(p3, nextAttack.getOwner());
        // G is the attacked territory
        assertEquals(G, nextAttack.getAttackedTerritory());
        // p2 has 2 territory attacking G
        assertEquals(2, nextAttack.getAttackedIt().get(p2).size());
        // 1st attack territory from p2 is D
        assertEquals(D, nextAttack.getAttackedIt().get(p2).get(0));
        // 2nd attack territory from p2 is E
        assertEquals(E, nextAttack.getAttackedIt().get(p2).get(1));
      } else {
        // If that the territory of that ResolvedAttack is A
        // p3 is the winner
        assertEquals(p3, nextAttack.getWinner());
        // p1 is the previous owner
        assertEquals(p1, nextAttack.getOwner());
        // A is the attacked territory
        assertEquals(A, nextAttack.getAttackedTerritory());
        // p3 has 1 territory attacking G
        assertEquals(1, nextAttack.getAttackedIt().get(p3).size());
        // the only one attack territory from p3 is H
        assertEquals(H, nextAttack.getAttackedIt().get(p3).get(0));
      }
      idx++;
    }
    // There are 2 ResolvedAttack objects in the list after attack
    assertEquals(2, idx);
  }

  @Test
  public void test_game_over() {
    RISCMap riscMap = new RISCMap();
    Territory t1 = new Territory("t1", 0);
    Territory t2 = new Territory("t2", 0);

    Player p1 = new Player("p1");
    Player p2 = new Player("p2");

    p1.addTerritory(t1);
    p2.addTerritory(t2);
    riscMap.addPlayer(p1);

    assertTrue(riscMap.isGameOver());

    riscMap.addPlayer(p2);
    assertFalse(riscMap.isGameOver());

    p2.removeTerritory(t2);
    assertTrue(riscMap.isGameOver());

  }

  @Test
  public void test_get_winner() {
    RISCMap riscMap = new RISCMap();
    Territory t1 = new Territory("t1", 0);
    Territory t2 = new Territory("t2", 0);

    Player p1 = new Player("p1");
    Player p2 = new Player("p2");
    Player p3 = new Player("p3");

    p1.addTerritory(t1);
    p2.addTerritory(t2);
    riscMap.addPlayer(p1);

    assertTrue(riscMap.isGameOver());
    assertEquals(p1, riscMap.getWinner());

    riscMap.addPlayer(p2);
    riscMap.addPlayer(p3);
    assertFalse(riscMap.isGameOver());
    assertNull(riscMap.getWinner());

    p2.removeTerritory(t2);
    assertTrue(riscMap.isGameOver());
    assertEquals(p1, riscMap.getWinner());

    p2.addTerritory(t2);
    p1.removeTerritory(t1);
    assertTrue(riscMap.isGameOver());
    assertEquals(p2, riscMap.getWinner());

  }

  @Test
  public void test_get_winner_no_players() {
    RISCMap riscMap = new RISCMap();
    RISCMap riscMapSpy = spy(riscMap);
    doReturn(true).when(riscMapSpy).isGameOver();
    doReturn(new ArrayList<Player>().iterator()).when(riscMapSpy).getPlayers();

    assertNull(riscMapSpy.getWinner());
  }

  @Test
  public void test_get_resolved_attack_return_null() {
    RISCMap riscMap = new RISCMap();
    Territory t1 = new Territory("t1", 0);
    assertNull(riscMap.findResolvedAttack(t1));
  }

}
