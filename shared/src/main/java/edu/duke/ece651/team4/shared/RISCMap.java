package edu.duke.ece651.team4.shared;

import java.io.Serializable;
import java.util.*;

public class RISCMap implements Serializable {
  private ArrayList<Player> players;
  private HashMap<Territory, ArrayList<Territory>> territoryGraph;
  private int maxTotalUnitForEachPlayer;
  private Dice dice;
  private ArrayList<ResolvedAttack> attacksInLastRound;

  /**
   * constructor for RISCMap
   * It will initilize the territoryGraph
   */
  public RISCMap() {
    this(new TwentySidedDice());
  }

  /**
   * Constructor for RISCMap that takes a dice (for testing).
   */
  public RISCMap(Dice dice) {
    this.territoryGraph = new HashMap<Territory, ArrayList<Territory>>();
    this.players = new ArrayList<Player>();
    this.maxTotalUnitForEachPlayer = 0;
    this.dice = dice;
    this.attacksInLastRound = new ArrayList<>();
  }

  /**
   * addTerritoryNeighbor() will add the neighbor to a territory
   * 
   * @param territory   is the territory we want to add neighbor
   * @param newNeighbor is the new neighbor we want to add
   */
  public void addTerritoryNeighbor(Territory territory, Territory newNeighbor) {
    ArrayList<Territory> neighbors = this.territoryGraph.get(territory);
    if (neighbors == null) {
      neighbors = new ArrayList<Territory>();
      this.territoryGraph.put(territory, neighbors);
    }
    neighbors.add(newNeighbor);
  }

  /**
   * getNeighbors() will return all neighbors of a specified territory
   *
   * @param territory is the name we want to get neighbors
   * @return all neighbors of a territory
   */
  public ArrayList<Territory> getNeighbors(Territory territory) {
    for (Map.Entry<Territory, ArrayList<Territory>> entry : territoryGraph.entrySet()) {
      if (entry.getKey().getName().equalsIgnoreCase(territory.getName()))
        return entry.getValue();
    }
    return null;

  }

  /**
   * getNumTerritory() will return the nmber of territories current map has
   *
   * @return number of territory current map has
   */
  public int getNumTerritory() {
    return this.territoryGraph.size();
  }

  /**
   * addPlayer() will add a new player to current player list
   *
   * @param playerToAdd is the player to add
   */
  public void addPlayer(Player playerToAdd) {
    this.players.add(playerToAdd);
  }

  /**
   * getNumPlayers() will return the number of players in playerList
   *
   * @return the number of players in playerList
   */
  public int getNumPlayers() {
    return this.players.size();
  }

  /**
   * getPlayers() will return a iterator to player list
   *
   * @return iterator of player list
   */
  public Iterator<Player> getPlayers() {
    return this.players.iterator();
  }

  /**
   * getTerritories() will return the set of territories.
   */
  public Set<Territory> getTerritories() {
    return this.territoryGraph.keySet();
  }

  /**
   * Return territory object from map given the string name of that territory.
   *
   * @param terrName is the territory name to search for.
   * @return the Territory with the given name.
   *         precondition: territory with that name exists.
   *         postcondition: returns non-null territory object.
   */
    public Territory getTerritoryByName(String terrName) {
    Territory ans;
    for (Territory t : this.getTerritories()) {
      if (t.getName().equalsIgnoreCase(terrName)) {
        ans = t;
        return ans;
      }
    }
    throw new IllegalArgumentException("Territory does not exist in map;");
  }

  /**
   * Return player object from player list given the player name
   *
   * @param playerName is the player name of the player object we want to get
   * @return the player object if it exists in the player list
   * @return null if it does not exist in the player list
   */
  public Player getPlayerByName(String playerName) {
    for (Player player : this.players) {
      if (player.getName().equals(playerName)) {
        return player;
      }
    }
    return null;
  }

  /**
   * executeMoveActionsForOnePlayer() will execute all the move actions
   * from one player
   * minus corresponding numUnits from fromTerr
   * plus corresponding numUnits to toTerr
   *
   * @param moveIterator is iterator to all move actions of one player
   */
  private void executeMoveActionsForOnePlayer(Iterator<Action> moveIterator) {
    while (moveIterator.hasNext()) {
      Action nextAction = moveIterator.next();
      Territory fromTerr = getTerritoryByName(nextAction.getFrom().getName());
      Territory toTerr = getTerritoryByName(nextAction.getTo().getName());
      int numUnits = nextAction.getNumUnits();
      fromTerr.addUnits(-numUnits);
      toTerr.addUnits(numUnits);
    }
  }

  /**
   * executeMoveActions() will execute all the move actions
   * got from all the players
   * minus corresponding numUnits from fromTerr
   * plus corresponding numUnits to toTerr
   *
   * @param allPlayerTurns is the arrayList that contains all players' turn
   */
  private void executeMoveActions(ArrayList<OnePlayerTurn> allPlayerTurns) {
    for (OnePlayerTurn eachTurn : allPlayerTurns) {
      executeMoveActionsForOnePlayer(eachTurn.getMoves());
    }
  }

  /**
   * buildToMapForOnePlayer() will iterate through all attack actions from one
   * player,
   * build toMap and send army (minus numUnits from each fromTerr)
   *
   * @param attackIterator is iterator to all attack actions of one player
   * @param toMap          is the HashMap we want to build for each toTerr
   */
  private void buildToMapForOnePlayer(Iterator<Action> attackIterator, HashMap<Territory, ArrayList<Action>> toMap) {
    while (attackIterator.hasNext()) {
      Action nextAction = attackIterator.next();
      Territory fromTerr = getTerritoryByName(nextAction.getFrom().getName());
      Territory toTerr = getTerritoryByName(nextAction.getTo().getName());
      int numUnits = nextAction.getNumUnits();
      // minus numUnits in attack army since it does not attend defense phase
      fromTerr.addUnits(-numUnits);
      // build toMap: add Action to the list of toTerr
      ArrayList<Action> toTerrActionLists = toMap.get(toTerr);
      // If not exist, create a new Action List for this toTerr
      if (toTerrActionLists == null) {
        toTerrActionLists = new ArrayList<Action>();
        toMap.put(toTerr, toTerrActionLists);
      }
      // add this action to this list
      toTerrActionLists.add(nextAction);
    }
  }

  /**
   * buildToMap() will iterate through all attack actions from all players,
   * build toMap and send army (minus numUnits from each fromTerr)
   *
   * @param allPlayerTurns is the arrayList that contains all players' turn
   * @param toMap          is the HashMap we want to build for each toTerr
   */
  public void buildToMap(ArrayList<OnePlayerTurn> allPlayerTurns, HashMap<Territory, ArrayList<Action>> toMap) {
    for (OnePlayerTurn eachTurn : allPlayerTurns) {
      buildToMapForOnePlayer(eachTurn.getAttacks(), toMap);
    }
  }

  /**
   * playerOwnTerritory() return the player that owns input territory
   *
   * @param territory is the territory that player owns
   * @return player object if it exists in the player list
   * @return null if it does not exist in the player list
   */
  public Player playerOwnTerritory(Territory territory) {
    for (Player player : this.players) {
      if (player.ownsTerritory(territory)) {
        return player;
      }
    }
    return null;
  }

  /**
   * actionFromSamePlayer() return if 2 input actions are from the same player
   *
   * @param action1 is the first action we want to test
   * @param action2 is the second action we want to test
   * @return true if 2 actions are from the same player
   * @return false if 2 actions are not from the same player
   */
  private boolean actionFromSamePlayer(Action action1, Action action2) {
    Territory fromTerr1 = action1.getFrom();
    Territory fromTerr2 = action2.getFrom();
    Player player1 = playerOwnTerritory(fromTerr1);
    Player player2 = playerOwnTerritory(fromTerr2);
    return player1 == player2;
  }

  /**
   * mergeActions() will iterate through all the contents in the toMap
   * For each toTerr, it will merge all the actions from same player
   *
   * @param toMap is the toMap we built before that contains all the destinations
   *              all players want to attack
   */
  private void mergeActions(HashMap<Territory, ArrayList<Action>> toMap) {
    for (Map.Entry<Territory, ArrayList<Action>> eachToTerr : toMap.entrySet()) {
      ArrayList<Action> currList = eachToTerr.getValue();
      // for each toTerr list, merge actions that is from same player
      // set merged value of merged action to -1 since attack action value should
      // always be larger than 1
      for (int i = 0; i < currList.size() - 1; i++) {
        Action action1 = currList.get(i);
        // If this action has been marked -1 (deleted), then skip this action
        if (action1.getNumUnits() == -1) {
          continue;
        }
        for (int j = i + 1; j < currList.size(); j++) {
          Action action2 = currList.get(j);
          // If from same player, merge two actions, mark second one with -1
          // add second value to first one
          if (actionFromSamePlayer(action1, action2)) {
            int numOfUnits1 = action1.getNumUnits();
            int numOfUnits2 = action2.getNumUnits();
            action2.setNumUnits(-1);
            action1.setNumUnits(numOfUnits1 + numOfUnits2);
          }
        }
      }
      // after marking merged to -1, remove all actions with numUnits = -1
      currList.removeIf(action -> action.getNumUnits() == -1);
    }
  }

  /**
   * addToAttackList() will form a <Player:Integer> structure and add to
   * playerList
   *
   * @param playerList is the list we want to add our data to
   * @param player     is the player we want to add in the <Player:Integer>
   *                   structure
   * @param numAttend  is the number we want to add in the <Player:Integer>
   *                   structure
   */
  private void addToAttackList(ArrayList<AbstractMap.SimpleEntry<Player, Integer>> playerList, Player player,
      int numAttend) {
    AbstractMap.SimpleEntry<Player, Integer> ownerEntry = new AbstractMap.SimpleEntry<>(player, numAttend);
    playerList.add(ownerEntry);
  }

  /**
   * initAttackList() will form the player list that includes all the players who
   * want to attack same territory
   * It will remove the territory if in that territory there is no defender
   *
   * @param eachToTerr is the map entry that involves a territory and a list of
   *                   actions that aim at that territory
   * @return an arrayList that involves all attackers and defenders
   */
  private ArrayList<AbstractMap.SimpleEntry<Player, Integer>> initAttackList(
      Map.Entry<Territory, ArrayList<Action>> eachToTerr) {
    // AbstractMap.SimpleEntry is similar to Pair in C++
    ArrayList<AbstractMap.SimpleEntry<Player, Integer>> playerList = new ArrayList<>();
    Territory territoryBeingAttacked = eachToTerr.getKey();
    int currentDefenseNum = territoryBeingAttacked.getNumUnits();
    Player currPlayer = playerOwnTerritory(territoryBeingAttacked);
    addToAttackList(playerList, currPlayer, currentDefenseNum);
    // add all the attacker to the list
    for (Action currAction : eachToTerr.getValue()) {
      Player attackPlayer = playerOwnTerritory(currAction.getFrom());
      int attackNum = currAction.getNumUnits();
      addToAttackList(playerList, attackPlayer, attackNum);
    }
    // if numUnits in that territory is 0, remove it from the list
    playerList.removeIf(player -> player.getValue() == 0);
    return playerList;
  }

  /**
   * fight() return the result of combat
   *
   * @return true if defender wins
   * @return false if attacker wins
   */
  private boolean fight() {
    int defenseDiceRes = dice.roll();
    int attackerDiceRes = dice.roll();
    if (defenseDiceRes >= attackerDiceRes) {
      return true;
    }
    return false;
  }

  /**
   * switchOwnership() will delete that territory from previous player territory
   * list
   * add new territory to newPlayer's territory list
   * and set the remaining numUnits after fighting to the territory
   *
   * @param territory      is the territory we operate on
   * @param newPlayer      contains the new player we want to add territory to its
   *                       territory list
   * @param previousPlayer is the old player we want to remove territory from its
   *                       territory list
   */
  private void switchOwnership(Territory territory, Player newPlayer, Player previousPlayer) {
    previousPlayer.removeTerritory(territory);
    newPlayer.addTerritory(territory);
  }

  /**
   * findWinnerOfAttack() will return the winner given a list of <Player: Integer>
   *
   * @param playerList is the list of players that are involved in the combat for
   *                   one territory
   * @return player object and the remaining numUnits
   */
  private AbstractMap.SimpleEntry<Player, Integer> findWinnerOfAttack(
      ArrayList<AbstractMap.SimpleEntry<Player, Integer>> playerList) {
    int defenderPlayerNum = 0;
    int attackerPlayerNum;
    boolean singleAttacker = false;
    // only 1 attacker and 1 defender
    if (playerList.size() == 2) {
      singleAttacker = true;
    }
    while (playerList.size() > 1) {
      // If there's only one attacker, then the attacker will always be in the index 1
      // (0 + 1)
      attackerPlayerNum = defenderPlayerNum + 1 < playerList.size() ? defenderPlayerNum + 1 : 0;
      AbstractMap.SimpleEntry<Player, Integer> defendPlayer = playerList.get(defenderPlayerNum);
      AbstractMap.SimpleEntry<Player, Integer> attackPlayer = playerList.get(attackerPlayerNum);

      // fightRes == true -> defender wins
      // fightRes == false -> attacker wins
      boolean fightRes = fight();
      if (fightRes == true) {
        // attacker loses
        attackPlayer.setValue(attackPlayer.getValue() - 1);
      } else {
        // defender loses
        defendPlayer.setValue(defendPlayer.getValue() - 1);
      }

      // Remove attacker or defender with numUnits 0 left
      playerList.removeIf(player -> player.getValue() == 0);

      // If there's only one defender, then the attacker will always be in the index 0
      defenderPlayerNum = singleAttacker ? 0 : (defenderPlayerNum + 1 < playerList.size() ? defenderPlayerNum + 1 : 0);
    }
    // There's only one left and it's the winner
    return playerList.get(0);
  }

  /**
   * findResolvedAttack() will traverse the map and find the ResolvedAttack whose
   * territory is what we are looking for
   *
   * @param territoryToFind is the territory we want to find
   * @return ResolvedAttack if that the territory of that ResolvedAttack is what
   *         we want
   * @return null if we cannot find it
   *
   */
  public ResolvedAttack findResolvedAttack(Territory territoryToFind) {
    for (ResolvedAttack resolvedAttack : attacksInLastRound) {
      if (resolvedAttack.getAttackedTerritory().equals(territoryToFind)) {
        return resolvedAttack;
      }
    }
    return null;
  }

  /**
   * resolveAttack() will resolve all the attack actions by iterating through
   * toMap
   *
   * @param toMap is the to-territory map that we have built before
   */
  private void resolveAttack(HashMap<Territory, ArrayList<Action>> toMap) {
    for (Map.Entry<Territory, ArrayList<Action>> eachToTerr : toMap.entrySet()) {
      ArrayList<AbstractMap.SimpleEntry<Player, Integer>> playerList = initAttackList(eachToTerr);

      Territory currTerr = eachToTerr.getKey();
      Player previousOwner = playerOwnTerritory(currTerr);
      AbstractMap.SimpleEntry<Player, Integer> turnWinner = findWinnerOfAttack(playerList);
      Player turnWinnerPlayer = turnWinner.getKey();
      // Since the order of traversing map is not fixed, the order they are added to
      // list might not be determined
      // Therefore, I have to go through attack action list to find that
      // ResolvedAttack
      ResolvedAttack attackInList = findResolvedAttack(currTerr);
      attackInList.setWinner(turnWinnerPlayer);
      currTerr.setUnits(turnWinner.getValue());

      // If owner changes, switch owner:
      if (previousOwner != turnWinner.getKey()) {
        switchOwnership(currTerr, turnWinnerPlayer, previousOwner);
      }
    }
  }

  private void constructAttackResult(HashMap<Territory, ArrayList<Action>> toMap) {
    for (Map.Entry<Territory, ArrayList<Action>> eachToTerr : toMap.entrySet()) {
      // init current ResolvedAttack, set original owner and attacked territory
      ResolvedAttack currAttack = new ResolvedAttack();
      Territory currTerr = eachToTerr.getKey();
      Player currOwner = playerOwnTerritory(currTerr);
      currAttack.setAttackedTerritory(currTerr);
      currAttack.setOwner(currOwner);
      // for each action, add that player and corresponding territory to attackedIt
      for (Action eachAction : eachToTerr.getValue()) {
        Territory attackTerr = eachAction.getFrom();
        Player currAttacker = playerOwnTerritory(attackTerr);
        currAttack.addAttackedIt(currAttacker, attackTerr);
      }
      // add currAttack to attacksInLastRound
      attacksInLastRound.add(currAttack);
    }
  }

  /**
   * executeAttackActions() will execute all attack actions
   * it will update territory ownership after all the actions have been executed
   *
   * @param allPlayerTurns is the arrayList that contains all players' turn
   */
  public void executeAttackActions(ArrayList<OnePlayerTurn> allPlayerTurns) {
    HashMap<Territory, ArrayList<Action>> toMap = new HashMap<>();
    // Step 1: iterate through attack actions,
    // build toMap and send army (minus numUnits from each fromTerr)
    buildToMap(allPlayerTurns, toMap);

    // Step 2: construct attack actions before merging them
    constructAttackResult(toMap);

    // Step 3: merge actions from one player to same attack territory
    mergeActions(toMap);

    // Step 4: Attack Phase
    resolveAttack(toMap);
  }

  /**
   * addOneUnitToAllTerritory() will add one new unit to each of the
   * territories in the RISCMap
   *
   */
  private void addOneUnitToAllTerritory() {
    for (Territory territory : this.getTerritories()) {
      territory.addUnits(1);
    }
  }

  /**
   * doOneTurn() will execute all the actions from all players
   * after it passes validation
   * At the end of the turn, it will add one more unit to each territory
   *
   * @param allPlayerTurns is the arrayList that contains all players' turn
   */
  public void doOneTurn(ArrayList<OnePlayerTurn> allPlayerTurns) {
    this.attacksInLastRound.clear();
    executeMoveActions(allPlayerTurns);
    executeAttackActions(allPlayerTurns);
    addOneUnitToAllTerritory();
  }

  /**
   * getMaxNumUnits() return the maximum number of units each player has
   *
   */
  public int getMaxNumUnits() {
    return this.maxTotalUnitForEachPlayer;
  }

  /**
   * setMaxNumUnits() return the maximum number of units each player has
   *
   */
  public void setMaxNumUnits(int maxNumUnitsToSet) {
    this.maxTotalUnitForEachPlayer = maxNumUnitsToSet;
  }

  /**
   * assignUnitsForOnePlayer() return the maximum number of units each player has
   *
   */
  public void assignUnitsForOnePlayer(Player playerToAssign, HashMap<Territory, Integer> assignMap) {
    Player playerInList = getPlayerByName(playerToAssign.getName());
    playerInList.assignUnits(assignMap);
  }

  /**
   * Method to check if game is over
   *
   * @return boolean true if game over, false otherwise
   */
  public boolean isGameOver() {
    int playerStanding = 0;
    for (Iterator<Player> it = this.getPlayers(); it.hasNext();) {
      Player player = it.next();
      if (player != null && player.isAlive())
        playerStanding++;
    }
    return playerStanding == 1;
  }

  /**
   * Method to get the winner if the game is over
   *
   * @return Player the winner if there's only one player left
   * @return null otherwise
   */
  public Player getWinner() {
    if (this.isGameOver()) {
      for (Iterator<Player> it = this.getPlayers(); it.hasNext();) {
        Player player = it.next();
        if (player != null && player.isAlive())
          return player;
      }
    }
    return null;
  }

  /**
   * Method to get all resolved attacks in last round
   *
   * @return iterator to attacksInLastRound
   */
  public Iterator<ResolvedAttack> getAllAttacks() {
    return this.attacksInLastRound.iterator();
  }

}
