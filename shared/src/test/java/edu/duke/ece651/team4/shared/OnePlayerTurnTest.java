package edu.duke.ece651.team4.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

public class OnePlayerTurnTest {
  @Test
  public void test_one_player_turn() {
    Territory t1 = new Territory("t1", 0);
    Territory t2 = new Territory("t2", 0);
    Territory t3 = new Territory("t3", 0);
    Territory t4 = new Territory("t4", 0);
    Action a1 = new Action(t2, t1, 5);
    Action a2 = new Action(t4, t3, 10);
    Action a3 = new Action(t3, t1, 4);
    Action a4 = new Action(t1, t4, 1);
    OnePlayerTurn turn = new OnePlayerTurn();
    turn.addAttackAction(a1);
    turn.addMoveAction(a2);
    turn.addAttackAction(a3);
    turn.addMoveAction(a4);
    Iterator<Action> moves = turn.getMoves();
    Action m = moves.next();
    assertEquals(m.getTo(), t4);
    m = moves.next();
    assertEquals(m.getTo(), t1);
    assertEquals(moves.hasNext(), false);
    Iterator<Action> attacks = turn.getAttacks();
    Action a = attacks.next();
    assertEquals(a.getFrom(), t1);
    a = attacks.next();
    assertEquals(a.getNumUnits(), 4);
    assertEquals(attacks.hasNext(), false);
    String exp = "So far, you have commited the following moves:\n" + "\tMove 10 units from t3 to t4.\n"
        + "\tMove 1 units from t4 to t1.\n" + "\tAttack 5 units from t1 to t2.\n" + "\tAttack 4 units from t1 to t3.\n";
    assertEquals(exp, turn.toString());
    OnePlayerTurn noTurn = new OnePlayerTurn();
    assertEquals("", noTurn.toString());
  }
}
