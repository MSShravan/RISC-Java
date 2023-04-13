package edu.duke.ece651.team4.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Player implements Serializable {
  private String name;
  private ArrayList<Territory> territories;

  /**
   * Constructor for Player
   * It will initilize the Player with input player name
   * and will initialize territories list
   * 
   * @param initName is the name we want this player to be
   */
  public Player(String initName) {
    this.name = initName;
    this.territories = new ArrayList<Territory>();
  }

  /**
   * getName() will return the name of current player
   * 
   * @return name of player
   */
  public String getName() {
    return this.name;
  }

  /**
   * addTerritory() will add territory to current arrayList
   * 
   * @param territoryToAdd is the territory to add
   */
  public void addTerritory(Territory territoryToAdd) {
    this.territories.add(territoryToAdd);
  }

  /**
   * getTerritories() will return the iterator to territories
   * 
   * @return territory iterator
   */
  public Iterator<Territory> getTerritories() {
    return this.territories.iterator();
  }

  /**
   * removeTerritory() will remove territory specified if it exists
   * 
   * @param territory is the territory to remove
   */
  public void removeTerritory(Territory territory) {
    this.territories.remove(territory);
  }

  /**
   * isAlive() will return if current player has territories or not
   * 
   * @return true if this player has more than 0 territories
   * @return false if this player has 0 territory
   */
  public boolean isAlive() {
    return !this.territories.isEmpty();
  }

  /**
   * Validates that this players turn follows the rules of the game.
   * 
   * @param turn is the turn object representing all the moves the player has
   *             requested.
   * @param map  is the RISCMap the game is played on.
   */
  public String validateTurn(OnePlayerTurn turn, RISCMap map) {
    try {
      HashMap<Territory, Integer> tempUnitPlacement = validateMoves(turn.getMoves(), map);
      this.validateAttacks(turn.getAttacks(), map, tempUnitPlacement);
    } catch (IllegalActionException e) {
      return e.getMessage();
    }
    return null;

  }

  /**
   * getTerritoryByName() will return the territory with the same name
   *
   * @param terrName is the name to search in the arrayList
   * @return territory object if it can be found in the arrayList
   * @return null if territory with the same name cannot be found in the arrayList
   *         but it won't happen because we have gone through validation process
   */
  public Territory getTerritoryByName(String terrName) {
    for (Territory territory : this.territories) {
      if (territory.getName().equalsIgnoreCase(terrName)) {
        return territory;
      }
    }
    return null;
  }

  /**
   * assignUnits() will assign values to corresponding territory
   * according to the value players enter
   *
   * @param assignmentMap is the value map that players want to assign to their
   *                      territories
   */
  public void assignUnits(HashMap<Territory, Integer> assignmentMap) {
    for (Map.Entry<Territory, Integer> territoryToAssign : assignmentMap.entrySet()) {
      Territory territory = getTerritoryByName(territoryToAssign.getKey().getName());
      territory.addUnits(territoryToAssign.getValue());
    }
  }

  /**
   * Validates all the moves in one players turn. Asserts that the player owns
   * both territories, that there is a valid move path between each move order and
   * that no territories are left with negative units at the end of the turn.
   * 
   * @param moves is an iterator over all the moves a player has requested.
   * @param map   is the map containing the adjecency list.
   * @return HashMap containing the territory units if the moves were to be
   *         executed.
   * @throws IllegalActionException if the moves are invalid.
   */
  private HashMap<Territory, Integer> validateMoves(Iterator<Action> moves, RISCMap map) throws IllegalActionException {
    HashMap<Territory, Integer> unitPlacement = getCurrPlacement();
    Action currMove;
    while (moves.hasNext()) {
      currMove = moves.next();
      int changeUnits = currMove.getNumUnits();
      Territory to = getTerritoryByName(currMove.getTo().getName());
      Territory from = getTerritoryByName(currMove.getFrom().getName());
      if (to == null || from == null) {
        throw new IllegalActionException(
            "Player must be the owner of both territories in a move. Player does not own both Territory "
                + currMove.getTo().getName()
                + " and Territory " + currMove.getFrom().getName() + ".");
      }
      checkPath(to, from, map);
      unitPlacement.put(from, unitPlacement.get(from) - changeUnits);
      unitPlacement.put(to, unitPlacement.get(to) + changeUnits);
    }
    for (Territory t : unitPlacement.keySet()) {
      if (unitPlacement.get(t) < 0) {
        throw new IllegalActionException(
            "A players move actions must not result in any of their Territories containing negative units. Territory "
                + t.getName() + " is left with negative units.");
      }
    }
    return unitPlacement;
  }

  /**
   * Generates and returns a HashMap that links Territories to their current unit
   * placements.
   * 
   * @return a HashMap that represents a (changeable) representation of the
   *         Player's territories.
   */
  private HashMap<Territory, Integer> getCurrPlacement() {
    HashMap<Territory, Integer> currUnitPlacement = new HashMap<>();
    for (Territory t : territories) {
      currUnitPlacement.put(t, t.getNumUnits());
    }
    return currUnitPlacement;
  }

  /**
   * Determines whether a specific territory is owned by a player.
   * 
   * @param t is the Territory to check for ownership.
   * @return a boolean value indicating whether or not the territory is owned by
   *         Player.
   */
  public boolean ownsTerritory(Territory t) {
    for (Territory myT : territories) {
      if (t.equals(myT)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks for a valid path between two territories within the territories that
   * this player owns.
   * 
   * @param to   is the Territory to traverse to.
   * @param from is the Territory to traverse from.
   * @param map  is the RISCMap containing the adjencency list.
   * @throws IllegalActionException if path does not exist.
   */
  private void checkPath(Territory to, Territory from, RISCMap map) throws IllegalActionException {
    HashSet<Territory> visited = new HashSet<Territory>();
    visited.add(from);
    if (!checkPathHelper(to, from, map, visited)) {
      throw new IllegalActionException(
          "There is no valid move path between Territory " + from.getName() + " and Territory " + to.getName() + ".");
    }
  }

  /**
   * Recursive helper for CheckPath. Iterates through all a territories neighbors
   * to find a potential move path. Only checks territories owned by this Player.
   * 
   * @param to      is the Territory destination.
   * @param from    is the Territory start.
   * @param map     is the map containing the territory adjencies
   * @param visited is the set of visited territories.
   * @return boolean value indicating if there is a valid path.
   */
  private boolean checkPathHelper(Territory to, Territory from, RISCMap map, Set<Territory> visited) {
    if (to.equals(from)) {
      return true;
    }
    for (Territory t : map.getNeighbors(from)) {
      if (!visited.contains(t) && ownsTerritory(t)) {
        visited.add(t);
        boolean ans = checkPathHelper(to, t, map, visited);
        if (ans == true) {
          return ans;
        }
      }
    }
    return false;
  }

  /**
   * Validates an attack set for this player given unit placement after moves.
   * Checks that there are enough units to attack (no territory goes negative),
   * that the player owns the Territory it is attacking from, that it does not own
   * the Territory it is attacking to, and that the to and from Territory are
   * adjacent.
   * 
   * @param attacks   is an iterator over all the attack actions for this Player.
   * @param map       is the RISCMap containing the territory adjacencies.
   * @param tempUnits is the unit placement after the move phase.
   * @throws IllegalActionException if the
   */
  public void validateAttacks(Iterator<Action> attacks, RISCMap map, HashMap<Territory, Integer> tempUnits)
      throws IllegalActionException {
    Action currAttack;
    while (attacks.hasNext()) {
      currAttack = attacks.next();
      Territory from = map.getTerritoryByName(currAttack.getFrom().getName());
      Territory to = map.getTerritoryByName(currAttack.getTo().getName());
      int numUnits = currAttack.getNumUnits();
      if (!ownsTerritory(from) || ownsTerritory(to)) {
        throw new IllegalActionException(
            "For an attack to be valid the player must own the territory it is attacking from and not own the territory it is attacking to. The attack from Territory "
                + from.getName() + " to Territory " + to.getName() + " is not valid.");
      }
      if (!map.getNeighbors(from).contains(to)) {
        throw new IllegalActionException("Territories in an attack must be directly adjacent. Territory "
            + from.getName() + " and Territory " + to.getName() + " are not adjacent.");
      }
      tempUnits.put(from, tempUnits.get(from) - numUnits);
    }
    for (Territory t : tempUnits.keySet()) {
      if (tempUnits.get(t) < 0) {
        throw new IllegalActionException(
            "Attack actions must not result in any Territory ending with negative units. Territory " + t.getName()
                + " has negative units.");
      }
    }
  }
}
