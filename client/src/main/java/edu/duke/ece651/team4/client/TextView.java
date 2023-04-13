package edu.duke.ece651.team4.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.duke.ece651.team4.shared.*;

public class TextView {

  /**
   * this will display the map info to all the user
   * in the format of:
   * player's name
   * unit in the Territory and the neighbours
   * 
   * @return the string used to print
   */
  public String textMapBeginViewString(RISCMap board) {
    String res = "Below is the map of the game. Each player is listed and under each player is the territories they own and the neighbors of that territory.\n";
    Iterator<Player> player = board.getPlayers();
    while (player.hasNext()) {
      Player p = player.next();
      // print the player's name
      res += p.getName() + " player\n" + "----------------" + "\n";
      Iterator<Territory> ter = p.getTerritories();
      while (ter.hasNext()) {
        Territory t = ter.next();
        // print the unit info
        res += t.getName() + " (next to: ";
        for (Territory n : board.getNeighbors(t)) {
          // print the neighbord into
          res += n.getName() + ", ";
        }
        res = res.substring(0, res.length() - 2);
        res = res + ")\n";
      }
      res = res + "\n";
    }
    return res;
  }

  /**
   * this will display the map info to all the user
   * in the format of:
   * player's name
   * unit in the Territory and the neighbours
   * 
   * @return the string used to print
   */
  public String textMapViewString(RISCMap board) {
    String res = "Below is the current state of the game. Each player is listed and under each player is the territories they own, the number of units in that territory and the neighbors of that territory.\n";
    Iterator<Player> player = board.getPlayers();
    while (player.hasNext()) {
      Player p = player.next();
      // print the player's name
      res += p.getName() + " player\n" + "----------------" + "\n";
      Iterator<Territory> ter = p.getTerritories();
      while (ter.hasNext()) {
        Territory t = ter.next();
        // print the unit info
        res += t.getNumUnits() + " units in " + t.getName() + " (next to: ";
        for (Territory n : board.getNeighbors(t)) {
          // print the neighbord into
          res += n.getName() + ", ";
        }
        res = res.substring(0, res.length() - 2);
        res = res + ")\n";
      }
      res = res + "\n";
    }
    return res;
  }
/**
 * show the view at the begining of the game
 * @param combat
 * @return
 */
  public String combatViewSting(Iterator<ResolvedAttack> combat) {
    String res = "In the last turn, the following Attacks occurred:\n";
    while (combat.hasNext()) {
      ResolvedAttack tem = combat.next();
      res += "Territory " + tem.getAttackedTerritory().getName() + ", originally owned by Player "
          + tem.getOwner().getName() + " was attacked\n";
      HashMap<Player, ArrayList<Territory>> attackedIt = tem.getAttackedIt();
      for (HashMap.Entry<Player, ArrayList<Territory>> a : attackedIt.entrySet()) {
        res += "\tPlayer " + a.getKey().getName() + " attacked from ";
        for (Territory t : a.getValue()) {
          res += t.getName() + ", ";
        }
        res = res.substring(0, res.length() - 2);
        res += "\n";
      }
      if (tem.getOwner().getName() == tem.getWinner().getName()) {
        res += "\tPlayer " + tem.getOwner().getName() + " successfully defended the territory.\n\n";
      } else {
        res += "\tPlayer " + tem.getWinner().getName() + " won control of the Territory.\n\n";
      }
    }
    return res;

  }
}
