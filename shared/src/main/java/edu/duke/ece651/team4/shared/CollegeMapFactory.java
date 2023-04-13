
package edu.duke.ece651.team4.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Implementation of Abstract Map Factory that creates a map of 24 college
 * territories and their adjencies.
 */
public class CollegeMapFactory implements AbstractMapFactory {
  /**
   * Creates a college RISCMap. Puts in all territories and their neighbors.
   * 
   * @return RISCMap fully constructed.
   */
  @Override
  public RISCMap createMap(int numPlayers) {
    if (numPlayers > 4 || numPlayers < 2) {
      throw new IllegalArgumentException("Number of Players must be between 2 and 4");
    }
    HashMap<String, ArrayList<String>> terrStrings = addTerritories();
    RISCMap collegeMap = mapHelper(terrStrings);
    addPlayers(collegeMap, numPlayers);
    collegeMap.setMaxNumUnits(240 / numPlayers);
    return collegeMap;
  }

  /**
   * Add to a HashMap of territory string names.
   * 
   * @param graph     is the graph to add to.
   * @param territory is the territory name to add as the key.
   * @param neighbors is an array of strings that are the neighbors of the key.
   */
  private void terHelper(HashMap<String, ArrayList<String>> graph, String territory, String[] neighbors) {
    graph.put(territory, new ArrayList<String>(Arrays.asList(neighbors)));
  }

  /**
   * Creates and returns a RISCMap, constructing the territory graph from a graph
   * of strings. Ensures that all territories in the graph are the same object.
   * 
   * @param graph is the String graph to create the map from.
   * @return a RISCMap created from the graph.
   */
  private RISCMap mapHelper(HashMap<String, ArrayList<String>> graph) {
    RISCMap collegeMap = new RISCMap();
    HashMap<String, Territory> territories = new HashMap<String, Territory>();
    for (String tName : graph.keySet()) {
      territories.put(tName, new Territory(tName, 0));
    }
    for (String tName : territories.keySet()) {
      ArrayList<String> neighbors = graph.get(tName);
      for (String neighbor : neighbors) {
        collegeMap.addTerritoryNeighbor(territories.get(tName), territories.get(neighbor));
      }
    }
    return collegeMap;
  }

  /**
   * Creates an adjency list to represent the college map.
   * 
   * @return A HashMap representing the adjencies of each territory (as Strings
   *         and Arrays of Strings).
   */
  private HashMap<String, ArrayList<String>> addTerritories() {
    HashMap<String, ArrayList<String>> terrStrings = new HashMap<String, ArrayList<String>>();
    terHelper(terrStrings, "Michigan",
        new String[] { "Syracuse", "Michigan State", "Northwestern", "Indiana", "Ohio State" });
    terHelper(terrStrings, "Michigan State", new String[] { "Syracuse", "Michigan", "Ohio State" });
    terHelper(terrStrings, "Syracuse",
        new String[] { "Michigan", "Michigan State", "Ohio State", "Penn State", "Villanova", "UConn" });
    terHelper(terrStrings, "UConn", new String[] { "Syracuse", "Villanova" });
    terHelper(terrStrings, "Villanova",
        new String[] { "Syracuse", "UConn", "Penn State", "UPenn", "Maryland" });
    terHelper(terrStrings, "Penn State", new String[] { "Syracuse", "Ohio State", "Villanova", "UPenn", "Maryland" });
    terHelper(terrStrings, "UPenn", new String[] { "Penn State", "Villanova", "Maryland" });
    terHelper(terrStrings, "Ohio State", new String[] { "Michigan", "Michigan State", "Syracuse", "Penn State",
        "Maryland", "WVU", "Kentucky", "Indiana" });
    terHelper(terrStrings, "Northwestern", new String[] { "Michigan", "Indiana", "Illinois" });
    terHelper(terrStrings, "Indiana",
        new String[] { "Michigan", "Ohio State", "Kentucky", "Illinois", "Northwestern" });
    terHelper(terrStrings, "Illinois", new String[] { "Northwestern", "Indiana", "Kentucky", "Alabama" });
    terHelper(terrStrings, "Kentucky",
        new String[] { "Illinois", "Indiana", "Ohio State", "WVU", "UVA", "Duke", "Tennessee" });
    terHelper(terrStrings, "WVU", new String[] { "Kentucky", "Ohio State", "Maryland", "UVA" });
    terHelper(terrStrings, "Maryland", new String[] { "Penn State", "UPenn", "Villanova", "UVA", "WVU", "Ohio State" });
    terHelper(terrStrings, "Alabama", new String[] { "Illinois", "Tennessee", "Georgia", "Auburn" });
    terHelper(terrStrings, "Auburn", new String[] { "Alabama", "Georgia", "Florida" });
    terHelper(terrStrings, "Florida", new String[] { "Auburn", "Georgia", "South Carolina" });
    terHelper(terrStrings, "Georgia",
        new String[] { "Auburn", "Alabama", "Tennessee", "UNC", "South Carolina", "Florida" });
    terHelper(terrStrings, "Tennessee", new String[] { "Alabama", "Kentucky", "Duke", "Georgia", "UNC" });
    terHelper(terrStrings, "UVA", new String[] { "Kentucky", "WVU", "Maryland", "Duke", "NC State" });
    terHelper(terrStrings, "Duke", new String[] { "Tennessee", "Kentucky", "UVA", "NC State", "UNC" });
    terHelper(terrStrings, "UNC", new String[] { "Tennessee", "Duke", "NC State", "South Carolina", "Georgia" });
    terHelper(terrStrings, "NC State", new String[] { "Duke", "UVA", "South Carolina", "UNC" });
    terHelper(terrStrings, "South Carolina", new String[] { "UNC", "NC State", "Florida", "Georgia" });
    return terrStrings;
  }

  /**
   * Add players with their territories to the map.
   * 
   * @param map        is the map to add the players to.
   * @param numPlayers is the number of players to generate.
   */
  private void addPlayers(RISCMap map, int numPlayers) {
    String[] playerNames = { "Red", "Blue", "Green", "Yellow" };
    String[] territoryNames = { "Michigan", "Michigan State", "Northwestern", "Indiana", "Syracuse", "Ohio State",
        "Penn State", "UConn", "Villanova", "UPenn", "Maryland", "WVU", "Kentucky", "UVA", "Illinois", "Duke",
        "Tennessee", "Alabama", "Georgia", "Auburn", "South Carolina", "Florida", "UNC", "NC State" };
    int interval = 24 / numPlayers;
    for (int playerNum = 0; playerNum < numPlayers; playerNum++) {
      Player thisPlayer = new Player(playerNames[playerNum]);
      int start = interval * playerNum;
      int end = interval*(playerNum + 1);
      for (int terrNum =start; terrNum < end; terrNum++) {
        Territory toAdd = map.getTerritoryByName(territoryNames[terrNum]);
        thisPlayer.addTerritory(toAdd);
      }
      map.addPlayer(thisPlayer);
    }
  }

}
