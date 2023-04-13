package edu.duke.ece651.team4.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ResolvedAttack implements Serializable {
    private Player winner;
    private Player originalOwner;
    private Territory attackedTerritory;
    private HashMap<Player, ArrayList<Territory>> attackedIt;

    /**
     * constructor for ResolvedAttack
     * initialize all the properties
     *
     */
    public ResolvedAttack() {
        this.winner = null;
        this.originalOwner = null;
        this.attackedTerritory = null;
        this.attackedIt = new HashMap<>();
    }

    /**
     * setWinner() will set the winner of current attack
     *
     * @param winnerToSet   is the winner of current attack
     */
    public void setWinner(Player winnerToSet) {
        this.winner = winnerToSet;
    }

    /**
     * getWinner() will return the winner of current attack
     *
     * @return the winner of current attack
     */
    public Player getWinner() {
        return this.winner;
    }

    /**
     * setOwner() will set the previous owner of this territory
     *
     * @param ownerToSet is the previous owner of this territory
     */
    public void setOwner(Player ownerToSet) {
        this.originalOwner = ownerToSet;
    }

    /**
     * getOwner() will get the previous owner of this territory
     *
     * @return the previous owner of this territory
     */
    public Player getOwner() {
        return this.originalOwner;
    }

    /**
     * setAttackedTerritory() will set the attacked territory
     *
     * @param territoryAttackedToSet is the territory being attacked
     */
    public void setAttackedTerritory (Territory territoryAttackedToSet) {
        this.attackedTerritory = territoryAttackedToSet;
    }

    /**
     * getAttackedTerritory() will return the territory being attacked
     *
     * @return the territory that is being attacked
     */
    public Territory getAttackedTerritory () {
        return this.attackedTerritory;
    }

    /**
     * addAttackedIt() will add the territory that attacker use to attack to the map
     *
     * @param attackPlayer is the player that is attacking
     * @param attackTerrToAdd is the territory that player uses to attack
     */
    public void addAttackedIt (Player attackPlayer, Territory attackTerrToAdd) {
        ArrayList<Territory> attackTerrList = this.attackedIt.get(attackPlayer);
        if (attackTerrList == null) {
            attackTerrList = new ArrayList<>();
            this.attackedIt.put(attackPlayer, attackTerrList);
        }
        attackTerrList.add(attackTerrToAdd);
    }

    /**
     * getAttackedIt() will get the map that contains all players with their attack territory
     *
     * @return hashmap attackedIt
     */
    public HashMap<Player, ArrayList<Territory>> getAttackedIt () {
        return this.attackedIt;
    }
}
