package edu.duke.ece651.team4.client;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team4.shared.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

public class TextViewTest {

  @Test
  public void test_updateInfo() {
    AbstractMapFactory fact = new TestMap();
    RISCMap map = fact.createMap(4);
    TextView view = new TextView();
    String units = "0 units in ";
    String lineBreak = "----------------\n";
    String res = "Below is the current state of the game. Each player is listed and under each player is the territories they own, the number of units in that territory and the neighbors of that territory.\n";
    res += "Red player\n" + lineBreak;
    res += units + "KY (next to: VA, TN)\n";
    res += units + "VA (next to: KY, NC, TN)\n\n";
    res += "Blue player\n" + lineBreak;
    res += units + "TN (next to: KY, VA, NC, AL, GA)\n";
    res += units + "AL (next to: FL, GA, TN)\n\n";
    res += "Green player\n" + lineBreak;
    res += units + "FL (next to: GA, AL)\n";
    res += units + "GA (next to: SC, NC, TN, AL, FL)\n\n";
    res += "Yellow player\n" + lineBreak;
    res += units + "NC (next to: VA, TN, GA, SC)\n";
    res += units + "SC (next to: NC, GA)\n\n";
    assertEquals(res, view.textMapViewString(map));
    res="Below is the map of the game. Each player is listed and under each player is the territories they own and the neighbors of that territory.\n";
    res += "Red player\n" + lineBreak;
    res += "KY (next to: VA, TN)\n";
    res +="VA (next to: KY, NC, TN)\n\n";
    res += "Blue player\n" + lineBreak;
    res += "TN (next to: KY, VA, NC, AL, GA)\n";
    res += "AL (next to: FL, GA, TN)\n\n";
    res += "Green player\n" + lineBreak;
    res += "FL (next to: GA, AL)\n";
    res += "GA (next to: SC, NC, TN, AL, FL)\n\n";
    res += "Yellow player\n" + lineBreak;
    res += "NC (next to: VA, TN, GA, SC)\n";
    res += "SC (next to: NC, GA)\n\n";
    assertEquals(res, view.textMapBeginViewString(map));

  }


  @Test
  public void test_combat() {
    ResolvedAttack testResolved = new ResolvedAttack();
    Player p1 = new Player("A");
    Player p2 = new Player("B");

    Territory t1 = new Territory("t1", 0);
    Territory t2 = new Territory("t2", 0);
    Territory t3 = new Territory("Duke", 0);

    testResolved.addAttackedIt(p2, t1);
    testResolved.addAttackedIt(p2, t2);
    testResolved.setOwner(p1);
    testResolved.setWinner(p1);
    testResolved.setAttackedTerritory(t3);

    TextView view = new TextView();
    ArrayList<ResolvedAttack> combat = new ArrayList<ResolvedAttack>();
    combat.add(testResolved);
    combat.add(testResolved);
  

    view.combatViewSting(combat.iterator());
    String res= "In the last turn, the following Attacks occurred:\n"
    +"Territory Duke, originally owned by Player A was attacked\n"
    +"\tPlayer B attacked from t1, t2\n"
    +"\tPlayer A successfully defended the territory.\n\n"
    +"Territory Duke, originally owned by Player A was attacked\n"
    +"\tPlayer B attacked from t1, t2\n"
    +"\tPlayer A successfully defended the territory.\n\n";
    assertEquals(res, view.combatViewSting(combat.iterator()));
  }
}
