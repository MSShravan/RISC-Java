package edu.duke.ece651.team4.client;

import static org.junit.jupiter.api.Assertions.*;
import edu.duke.ece651.team4.shared.*;
import java.io.*;
import java.net.UnknownHostException;
import java.util.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class TextPlayersTest {
  public TextPlayers textPlatcreater() throws UnknownHostException, IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    PrintStream output = new PrintStream(System.out, true);
    TextPlayers player = new TextPlayers(null, input, output);
    return player;
  }

  @Test
  public void test_name() throws UnknownHostException, IOException {
    TextPlayers player = textPlatcreater();

    player.setName("A");
    assertEquals("A", player.getName());

  }

  @Test
  public void playerNameTest() throws UnknownHostException, IOException {

    ArrayList<Player> playersList = new ArrayList<Player>();
    Player player1 = new Player("playerA");
    Player player2 = new Player("playerB");
    playersList.add(player1);
    playersList.add(player2);

    String exc = "playerA, playerB";

    TextPlayers player = textPlatcreater();

    assertEquals(exc, player.playerName(playersList));
  }

  @Test
  public void testreadUserInput() throws UnknownHostException, IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader(""));
    PrintStream output = new PrintStream(bytes, true);
    PlayerConnection test = mock(PlayerConnection.class);
    TextPlayers player = new TextPlayers(test, input, output);
    assertThrows(Exception.class, () -> player.readUserInput(""));
  }

  @Test
  public void testselectPlayer() throws UnknownHostException, IOException, ClassNotFoundException {
    AbstractMapFactory mapFactory = new TestMap();
    RISCMap gameMap = mapFactory.createMap(2);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("\naaaa\nred\nblue\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    List<String> p = new ArrayList<String>();
    when(test.readData()).thenReturn(p);
    TextPlayers player = new TextPlayers(test, input, output);
    player.selectPlayer(gameMap);
    String except = "Based on the above map, please select a player name. You will play as that player for the rest of the game\n"
        + "The available player names are the following: Red, Blue\n"
        + "Enter player name: \n" + "Not a valid input, please try again\n" + "Enter player name: \n"
        + "Invalid player name, please choose from the available options!\n" + "Enter player name: \n"
        + "Player choice approved!\n";
    assertEquals(except, bytes.toString());
    player.close();
    bytes.reset();
  }

  @Test
  public void testselectPlayer_fail() throws UnknownHostException, IOException, ClassNotFoundException {
    AbstractMapFactory mapFactory = new TestMap();
    RISCMap gameMap = mapFactory.createMap(2);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("aaaa\nred\nblue\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    List<String> p2 = new ArrayList<String>();
    List<String> p1 = new ArrayList<String>();
    p1.add("A");
    when(test.readData()).thenReturn(p1, p2);
    TextPlayers player = new TextPlayers(test, input, output);
    player.selectPlayer(gameMap);
    String except = "Based on the above map, please select a player name. You will play as that player for the rest of the game\n" +
            "The available player names are the following: Red, Blue\n" +
            "Enter player name: \n" +
            "Invalid player name, please choose from the available options!\n" +
            "Enter player name: \n" +
            "Player choice NOT approved!\n" +
            "Uh oh! You were too slow! The following names have already been taken: A\n" +
            "Please choose from remaining options!\n" +
            "\n" +
            "Enter player name: \n" +
            "Player choice approved!\n";
    assertEquals(except, bytes.toString());
    test.close();
    bytes.reset();
  }

  @Test
  public void test_unitPlacement() throws IOException, ClassNotFoundException {
    AbstractMapFactory mapFactory = new TestMap();
    RISCMap gameMap = mapFactory.createMap(2);
    gameMap.setMaxNumUnits(5);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("a\n10\n5\n3\n3\n3\n3\n3\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    TextPlayers player = new TextPlayers(test, input, output);

    player.setName("Red");
    player.unitPlacement(gameMap);

    String except = "You have a total number of 5 units to place across your territories.\n"
        + "For each territory you will be prompted for the number of units to place. Please note that if\n" +
        "you use all your units before getting through all territories, all the remaining territories\n" +
        "will have zero units. Your last territory to receive placement will receive all of the remaining units.\n" +
        "How many units do you want to place in KY? You have 5 available units.\n"
        + "Not a valid input, number of units must be a non-negative integer.\n"
        + "How many units do you want to place in KY? You have 5 available units.\n"
        + "Input must be non-negative and less than your total number of remaining units, 5.\n"
        + "How many units do you want to place in KY? You have 5 available units.\n"
        + "You do not have any units left, so 0 units are placed in VA\n"
        + "You do not have any units left, so 0 units are placed in TN\n"
        + "You do not have any units left, so 0 units are placed in AL\n";
    assertEquals(except, bytes.toString());
    test.close();
    bytes.reset();
  }

  @Test
  public void test_unitPlacement2() throws IOException, ClassNotFoundException {
    AbstractMapFactory mapFactory = new TestMap();
    RISCMap gameMap = mapFactory.createMap(2);
    gameMap.setMaxNumUnits(5);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("1\n1\n1\n1\n1\n1\n1\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    TextPlayers player = new TextPlayers(test, input, output);

    player.setName("Red");
    player.unitPlacement(gameMap);

    String except = "You have a total number of 5 units to place across your territories.\n"
        + "For each territory you will be prompted for the number of units to place. Please note that if\n" +
        "you use all your units before getting through all territories, all the remaining territories\n" +
        "will have zero units. Your last territory to receive placement will receive all of the remaining units.\n"
        + "How many units do you want to place in KY? You have 5 available units.\n" +
        "How many units do you want to place in VA? You have 4 available units.\n" +
        "How many units do you want to place in TN? You have 3 available units.\n" +
        "You only have one territory (AL) left, all the remaining units (2) will be placed in that territory.\n";
    assertEquals(except, bytes.toString());
    test.close();
    bytes.reset();
  }

  @Test
  public void test_doOneTurn() throws IOException, ClassNotFoundException {

    AbstractMapFactory mapFactory = new TestMap();
    RISCMap gameMap = mapFactory.createMap(2);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("m\n5,VA,TN\nD\na\n5,VA,TN\nD\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    String res = null;
    when(test.readData()).thenReturn("fail", res);
    TextPlayers player = new TextPlayers(test, input, output);
    player.playOneTurn(gameMap);
    String moves = "So far, you have commited the following moves:\n";
    String words = "What would you like to do?\n" + "(M)ove\n" + "(A)ttack\n" + "(D)one\n\n";
    String except = words
        + "Please enter the number of units to MOVE, the source territory, and the destination territory, separated by a comma:\n\n"
        + moves + "\tMove 5 units from VA to TN.\n"
        + words + "Unfortunately, your orders were rejected, the issue was:\n\n"
        + "fail\n" + "Please enter all your orders again.\n"
        + words
        + "Please enter the number of units to ATTACK, the source territory, and the destination territory, separated by a comma:\n\n"
        + moves + "\tAttack 5 units from VA to TN.\n"
        + words
        + "You successfully commit all your orders!\n";
    assertEquals(except, bytes.toString());
    test.close();
    bytes.reset();

  }

  @Test
  public void test_doOneTurn_move() throws IOException, ClassNotFoundException {

    AbstractMapFactory mapFactory = new TestMap();
    RISCMap gameMap = mapFactory.createMap(2);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(
        new StringReader("move\n\n-1,VA,TN\n1,VA,TN\nattack\n0,VA,TN\n1,VA,TN\nD\n"));
    PrintStream output = new PrintStream(bytes, true);
    String moves = "So far, you have commited the following moves:\n";
    PlayerConnection test = mock(PlayerConnection.class);
    String res = null;
    when(test.readData()).thenReturn(res);
    TextPlayers player = new TextPlayers(test, input, output);
    player.playOneTurn(gameMap);
    String words = "What would you like to do?\n" + "(M)ove\n" + "(A)ttack\n" + "(D)one\n\n";
    String except = words
        + "Please enter the number of units to MOVE, the source territory, and the destination territory, separated by a comma:\n\n"
        + "Not a valid input, please try again. Make sure that you have spelled the territories correctly.\n"
        + "Please enter the number of units to MOVE, the source territory, and the destination territory, separated by a comma:\n\n"
        + "Not a valid number input (the number of units to move must be non-negative), please try again\n"
        + "Please enter the number of units to MOVE, the source territory, and the destination territory, separated by a comma:\n\n"
        + moves + "\tMove 1 units from VA to TN.\n"
        +words
        + "Please enter the number of units to ATTACK, the source territory, and the destination territory, separated by a comma:\n\n"
        + "Not a valid number input (the number of units to attack must be positive), please try again\n"
      + "Please enter the number of units to ATTACK, the source territory, and the destination territory, separated by a comma:\n\n" + moves + "\tMove 1 units from VA to TN.\n" + "\tAttack 1 units from VA to TN.\n"
        + words
        + "You successfully commit all your orders!\n";
    assertEquals(except, bytes.toString());
    test.close();
    bytes.reset();

  }

  @Test
  public void test_doOneTurn_attact() throws IOException, ClassNotFoundException {

    AbstractMapFactory mapFactory = new TestMap();
    RISCMap gameMap = mapFactory.createMap(2);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(
        new StringReader("\nabb\nD\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    String res = null;
    when(test.readData()).thenReturn(res);
    TextPlayers player = new TextPlayers(test, input, output);
    player.playOneTurn(gameMap);
    String words = "What would you like to do?\n" + "(M)ove\n" + "(A)ttack\n" + "(D)one\n\n";
    String except = words
        + "Input is invalid. Please try again.\n"
        + words
        + "Input is invalid. Please try again.\n"
        + words
        + "You successfully commit all your orders!\n";
    assertEquals(except, bytes.toString());
    test.close();
    bytes.reset();

  }

  @Test
  public void test_playerLose() throws IOException, ClassNotFoundException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("123\nyes\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(false);
    TextPlayers player = new TextPlayers(test, input, output);

    player.playerLose();
    String except = "You have lost the game, do you want to continue watch the game? (Y)es or (N)o\n"
        + "Input is invalid. Please try again.\n"
        + "You have lost the game, do you want to continue watch the game? (Y)es or (N)o\n";

    assertEquals(except, bytes.toString());
    test.close();
    bytes.reset();

  }

  @Test
  public void test_playerLose2() throws IOException, ClassNotFoundException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("no\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(false);
    TextPlayers player = new TextPlayers(test, input, output);

    player.playerLose();
    String except = "You have lost the game, do you want to continue watch the game? (Y)es or (N)o\n";

    assertEquals(except, bytes.toString());
    test.close();
    bytes.reset();

  }

  @Test
  public void test_initGame() throws IOException, ClassNotFoundException {

    AbstractMapFactory mapFactory = new TestMap();
    RISCMap gameMap = mapFactory.createMap(2);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("Yes\nRed\n5\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    List<String> p = new ArrayList<String>();
    when(test.readData()).thenReturn(gameMap, p, gameMap, true);
    TextPlayers player = new TextPlayers(test, input, output);
    player.initGame();
    verify(test, times(3)).readData();
  }

  @Test
  public void test_playTurns_win() throws IOException, ClassNotFoundException {

    Player p = mock(Player.class);
    when(p.getName()).thenReturn("A");
    when(p.isAlive()).thenReturn(true);

    RISCMap gameMap = mock(RISCMap.class);
    when(gameMap.isGameOver()).thenReturn(true);
    when(gameMap.getPlayerByName(anyString())).thenReturn(p);
    ArrayList<Player> plist = new ArrayList<>();
    when(gameMap.getPlayers()).thenReturn(plist.iterator());

    gameMap.addPlayer(p);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("D\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(gameMap, p);
    TextPlayers player = new TextPlayers(test, input, output);
    player.setName("A");

    player.playTurns();
    verify(test, times(2)).readData();

  }

  @Test
  public void test_playTurns_lose_watch() throws IOException, ClassNotFoundException {

    Player p = mock(Player.class);
    when(p.getName()).thenReturn("A");
    when(p.isAlive()).thenReturn(true, true, false);

    RISCMap gameMap = mock(RISCMap.class);
    when(gameMap.isGameOver()).thenReturn(false, false, false, false, true);
    when(gameMap.getPlayerByName(anyString())).thenReturn(p);
    ArrayList<Player> plist = new ArrayList<>();
    when(gameMap.getPlayers()).thenReturn(plist.iterator());
    ArrayList<ResolvedAttack> att = new ArrayList<>();

    when(gameMap.getAllAttacks()).thenReturn(att.iterator());

    gameMap.addPlayer(p);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("D\nD\ny\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(gameMap, null, gameMap, null, gameMap, gameMap, gameMap, p);
    TextPlayers player = new TextPlayers(test, input, output);
    player.setName("A");

    player.playTurns();
    verify(test, times(8)).readData();

  }

  @Test
  public void test_playTurns_lose_exit() throws IOException, ClassNotFoundException {

    Player p = mock(Player.class);
    when(p.getName()).thenReturn("A");
    when(p.isAlive()).thenReturn(true, true, false);

    RISCMap gameMap = mock(RISCMap.class);
    when(gameMap.isGameOver()).thenReturn(false, false, false, false);
    when(gameMap.getPlayerByName(anyString())).thenReturn(p);
    ArrayList<Player> plist = new ArrayList<>();
    when(gameMap.getPlayers()).thenReturn(plist.iterator());
    ArrayList<ResolvedAttack> att = new ArrayList<>();

    when(gameMap.getAllAttacks()).thenReturn(att.iterator());

    gameMap.addPlayer(p);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    BufferedReader input = new BufferedReader(new StringReader("D\nD\nn\n"));
    PrintStream output = new PrintStream(bytes, true);

    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(gameMap, null, gameMap, null, gameMap, gameMap);
    TextPlayers player = new TextPlayers(test, input, output);
    player.setName("A");

    player.playTurns();
    verify(test, times(5)).readData();
  }

  @Test
  public void test_ready_to_play() throws IOException, ClassNotFoundException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("no\nYes\n"));
    PrintStream output = new PrintStream(bytes, true);
    PlayerConnection test = mock(PlayerConnection.class);
    TextPlayers player = new TextPlayers(test, input, output);
    player.readyToPlay();
    String exp = "Once you have read the instructions and are ready to play, type 'Yes' below:\n"
        + "Incorrect input, try again!\n" +
        "Once you have read the instructions and are ready to play, type 'Yes' below:\n"
        + "Great, now it is time to select your player name.\n";
  }

}
