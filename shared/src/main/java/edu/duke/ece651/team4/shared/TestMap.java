package edu.duke.ece651.team4.shared;

public class TestMap implements AbstractMapFactory {
  /**
   * RISCMap factory that is specifically for testing. It can only take 2 or 4
   * players, and has a total of 8 territories.
   */
  @Override
  public RISCMap createMap(int numPlayers) {
    if (numPlayers != 4 && numPlayers != 2) {
      throw new IllegalArgumentException("TestMap Only Implemented for 2 and 4 Players.");
    }
    RISCMap testMap = new RISCMap();
    String[] terrs = { "KY", "VA", "TN", "NC", "AL", "GA", "SC", "FL" };
    Territory ky = new Territory("KY", 0);
    Territory va = new Territory("VA", 0);
    Territory tn = new Territory("TN", 0);
    Territory nc = new Territory("NC", 0);
    Territory al = new Territory("AL", 0);
    Territory ga = new Territory("GA", 0);
    Territory sc = new Territory("SC", 0);
    Territory fl = new Territory("FL", 0);
    testMap.addTerritoryNeighbor(ky, va);
    testMap.addTerritoryNeighbor(ky, tn);
    testMap.addTerritoryNeighbor(va, ky);
    testMap.addTerritoryNeighbor(va, nc);
    testMap.addTerritoryNeighbor(va, tn);
    testMap.addTerritoryNeighbor(tn, ky);
    testMap.addTerritoryNeighbor(tn, va);
    testMap.addTerritoryNeighbor(tn, nc);
    testMap.addTerritoryNeighbor(tn, al);
    testMap.addTerritoryNeighbor(tn, ga);
    testMap.addTerritoryNeighbor(nc, va);
    testMap.addTerritoryNeighbor(nc, tn);
    testMap.addTerritoryNeighbor(nc, ga);
    testMap.addTerritoryNeighbor(nc, sc);
    testMap.addTerritoryNeighbor(sc, nc);
    testMap.addTerritoryNeighbor(sc, ga);
    testMap.addTerritoryNeighbor(ga, sc);
    testMap.addTerritoryNeighbor(ga, nc);
    testMap.addTerritoryNeighbor(ga, tn);
    testMap.addTerritoryNeighbor(ga, al);
    testMap.addTerritoryNeighbor(ga, fl);
    testMap.addTerritoryNeighbor(fl, ga);
    testMap.addTerritoryNeighbor(fl, al);
    testMap.addTerritoryNeighbor(al, fl);
    testMap.addTerritoryNeighbor(al, ga);
    testMap.addTerritoryNeighbor(al, tn);
    String[] playerNames = { "Red", "Blue", "Green", "Yellow" };
    Territory[] territories = { ky, va, tn, al, fl, ga, nc, sc};
    int interval = 8 / numPlayers;
    for (int playerNum = 0; playerNum < numPlayers; playerNum++) {
      Player thisPlayer = new Player(playerNames[playerNum]);
      int start = interval * playerNum;
      int end = interval*(playerNum + 1);
      for (int terrNum =start; terrNum < end; terrNum++) {
        thisPlayer.addTerritory(territories[terrNum]);
      }
      testMap.addPlayer(thisPlayer);
    }
    return testMap;
  }

}
