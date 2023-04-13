package edu.duke.ece651.team4.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents all the actions in one player's turn.
 */

public class OnePlayerTurn implements Serializable {
  /**
   * All of a players moves for one turn.
   */
  private ArrayList<Action> moves;
  /**
   * All of a players attacks for one turn.
   */
  private ArrayList<Action> attacks;

  /**
   * Constructor for OnePlayerTurn.
   */
  public OnePlayerTurn() {
    this.moves = new ArrayList<Action>();
    this.attacks = new ArrayList<Action>();
  }

  /**
   * Add a move action to the players turn.
   * 
   * @param action is the Action to add.
   */
  public void addMoveAction(Action action) {
    moves.add(action);
  }

  /**
   * Add an attack action to the players turn.
   * 
   * @param action is the Action to add.
   */
  public void addAttackAction(Action action) {
    attacks.add(action);
  }

  /**
   * Get all moves in an order.
   * 
   * @return Iterator of actions representing all moves.
   */
  public Iterator<Action> getMoves() {
    return moves.iterator();
  }

  /**
   * Get all attacks in an order.
   *
   * @return Iterator of actions representing all attacks.
   */
  public Iterator<Action> getAttacks() {
    return attacks.iterator();
  }
  /**
   * String representation of a OnePlayerTurn object.
   */
  @Override
  public String toString() {
    StringBuilder turn = new StringBuilder();
    if (moves.size() == 0 && attacks.size() == 0) {
      return turn.toString();
    }
    turn.append("So far, you have commited the following moves:\n");
    for (Action m : moves) {
      turn.append("\tMove " + m.getNumUnits() + " units from " + m.getFrom() + " to " + m.getTo() + ".\n");
    }
     for (Action m : attacks) {
      turn.append("\tAttack " + m.getNumUnits() + " units from " + m.getFrom() + " to " + m.getTo() + ".\n");
    }
    return turn.toString();
  }
}
