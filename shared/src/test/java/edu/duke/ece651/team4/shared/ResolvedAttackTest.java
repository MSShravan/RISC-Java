package edu.duke.ece651.team4.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class ResolvedAttackTest {
  @Test
  public void test_set_get_winner() {
    ResolvedAttack testResolved = new ResolvedAttack();
    Player p1 = new Player("p1");
    testResolved.setWinner(p1);
    assertEquals(p1, testResolved.getWinner());
  }

  @Test
  public void test_set_get_owner() {
    ResolvedAttack testResolved = new ResolvedAttack();
    Player p1 = new Player("p1");
    testResolved.setOwner(p1);
    assertEquals(p1, testResolved.getOwner());
  }

  @Test
  public void test_set_get_territory() {
    ResolvedAttack testResolved = new ResolvedAttack();
    Territory t1 = new Territory("t1", 0);
    testResolved.setAttackedTerritory(t1);
    assertEquals(t1, testResolved.getAttackedTerritory());
  }

  @Test
  public void test_add_get_attackedIt() {
    ResolvedAttack testResolved = new ResolvedAttack();
    Player p1 = new Player("p1");
    Territory t1 = new Territory("t1", 0);
    Territory t2 = new Territory("t2", 0);
    testResolved.addAttackedIt(p1, t1);
    testResolved.addAttackedIt(p1, t2);
    HashMap<Player, ArrayList<Territory>> testAttackedIt = testResolved.getAttackedIt();
    ArrayList<Territory> arr1 = new ArrayList<>();
    arr1.add(t1);
    arr1.add(t2);
    assertEquals(arr1, testAttackedIt.get(p1));
  }
}
