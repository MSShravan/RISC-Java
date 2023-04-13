package edu.duke.ece651.team4.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.duke.ece651.team4.shared.Action;
import edu.duke.ece651.team4.shared.OnePlayerTurn;
import edu.duke.ece651.team4.shared.Player;
import edu.duke.ece651.team4.shared.PlayerConnection;
import edu.duke.ece651.team4.shared.RISCMap;
import edu.duke.ece651.team4.shared.Territory;

public class TextPlayers {
  private String name;
  private PlayerConnection connection;
  final BufferedReader input;
  final PrintStream output;

  /**
   * create a Textplayers which store the network information, BufferedReader, PrintStream
   * 
   * @throws IOException
   * @throws UnknownHostException
   */
  public TextPlayers(PlayerConnection connection, BufferedReader inputSource, PrintStream out)
      throws UnknownHostException, IOException {
    this.connection = connection;
    this.input = inputSource;
    this.output = out;
  }
/**
 * this will close the client connetion
 * @throws IOException
 */
  public void close() throws IOException {
    output.println("The game is over, client will close.");
    connection.close();
  }
/**
 * take the data as input and send it to server
 * @param data
 * @throws IOException
 */
  public void writeData(Object data) throws IOException {
    connection.writeData(data);

  }
/**
 * this read the data from the server
 * @return
 * @throws IOException
 * @throws ClassNotFoundException
 */
  public Object readData() throws IOException, ClassNotFoundException {
    return connection.readData();

  }
/**
 * set the name
 * @param name
 */
  public void setName(String name) {
    this.name = name;
  }
/**
 * get the name
 * @return
 */
  public String getName() {
    return name;
  }
/**
 * the help map to get the user input also check it is not empty
 */
  String readUserInput(String prompt) throws IOException {
    output.println(prompt);
    String type = input.readLine();
    if (type.equals(""))
      throw new IOException("Invalid Format: input cannot be null");
    return type;
  }

  /**
   * get all the player's name(color)
   * 
   * @param isPlayerChoiceApproved
   * @return
   */
  public String playerName(List<Player> isPlayerChoiceApproved) {
    String res = "";
    for (Player p : isPlayerChoiceApproved) {
      res += p.getName() + ", ";
    }
    return res.substring(0, res.length() - 2);
  }


  /**
   * let players to select their color
   * allow all the player to select the color at the same time, if the color
   * already be selected allow user to select again
   */
  public void selectPlayer(RISCMap gameMap) {

    ArrayList<Player> playersList = new ArrayList<>();
    for (Iterator<Player> it = gameMap.getPlayers(); it.hasNext();) {
      playersList.add(it.next());
    }
    String res = playerName(playersList);
    output.println(
        "Based on the above map, please select a player name. You will play as that player for the rest of the game\n"
            + "The available player names are the following: " + res);

   boolean isComplete = false;
    while (!isComplete) {
      try {
        String n = readUserInput("Enter player name: ");
        boolean verifyName = false;
        for (Player p : playersList) {
          if (n.equalsIgnoreCase(p.getName())) {
            writeData(p);
            verifyName = true;
            setName(p.getName());
            break;
          }
        }
        if (!verifyName) {
          output.println("Invalid player name, please choose from the available options!");          
          continue;
        }
        List<String> isPlayerChoiceApproved= (List<String>) readData();
        if (isPlayerChoiceApproved.isEmpty()) {
          output.println("Player choice approved!");
          isComplete = true;
        } else {
          output.println("Player choice NOT approved!");
          String name="";
          for(String s : isPlayerChoiceApproved){
            name+=s + ", ";
          }
          name = name.substring(0,name.length() - 2);
          output.println("Uh oh! You were too slow! The following names have already been taken: "+name+ "\n" + "Please choose from remaining options!\n");

          // maybe need another list with remain player list
        }
      } catch (Exception e) {
        output.println("Not a valid input, please try again");
      }

    }

  }



  /**
   *  unit placement 
   *  if the user do not have enough unit, it will automally put 0
   * if the user only have one terr left, it will autollly put all the remaining unit
   */

  public boolean unitPlacement(RISCMap gameMap) throws IOException, ClassNotFoundException {
    HashMap<Territory, Integer> unitsObj = new HashMap<Territory, Integer>();
    int availableUnit = gameMap.getMaxNumUnits();
    Player player = gameMap.getPlayerByName(name);
    ArrayList<Territory> ownedTerris = new ArrayList<>();
    for (Iterator<Territory> it = player.getTerritories(); it.hasNext();) {
      ownedTerris.add(it.next());
    }
    int size = ownedTerris.size();
    output.println("You have a total number of " + availableUnit + " units to place across your territories.\n"
        + "For each territory you will be prompted for the number of units to place. Please note that if\n" +
        "you use all your units before getting through all territories, all the remaining territories\n" +
        "will have zero units. Your last territory to receive placement will receive all of the remaining units.");

    // iterate ownedTerris to get unitPlacement
    for (Territory t : ownedTerris) {
      // if no available units just add 0
      if (availableUnit == 0) {
        output.println("You do not have any units left, so 0 units are placed in " + t.getName());
        size--;
        unitsObj.put(t, 0);
        continue;
      }
      boolean status = false;
      while (!status) {
        try {
          if (size == 1) {
            output.println("You only have one territory (" + t.getName() + ") left, all the remaining units ("
                + availableUnit + ") will be placed in that territory.");
            status = true;
            unitsObj.put(t, availableUnit);
            continue;
          }
          String inputUnit = readUserInput(
              "How many units do you want to place in " + t.getName() + "? You have " + availableUnit
                  + " available units.");
          int numUnit = Integer.parseInt(inputUnit);
          if (numUnit < 0 || numUnit > availableUnit) {
            output.println("Input must be non-negative and less than your total number of remaining units, "
                + availableUnit + ".");
            continue;
          } else {
            status = true;
            unitsObj.put(t, numUnit);
            size--;
            availableUnit -= numUnit;
          }

        } catch (Exception exception) {
          output.println("Not a valid input, number of units must be a non-negative integer.");
        }
      }

    }
    writeData(unitsObj);
    return true;
  }

  // following is the code to do one turn
/**
 * help method for the move and attact
 * @param input
 * @return
 */
  private ArrayList<String> parseUserInput(String input) {
    ArrayList<String> res = new ArrayList<>();
    int firstParse = input.indexOf(",");
    res.add(input.substring(0, firstParse));
    int secondParse = input.indexOf(",", firstParse + 1);
    res.add(input.substring(firstParse + 1, secondParse));
    res.add(input.substring(secondParse + 1));
    return res;
  }

/**
 * based on user's input to create a new order
 * @param type 
 * @param orders
 * @param gameMap
 * @return
 * @throws IOException
 */
  public int tryCreateNewOrder(String type, OnePlayerTurn orders, RISCMap gameMap) throws IOException {
    String instruction = "Please enter the number of units to "
        + type + ", the source territory, and the destination territory, separated by a comma:\n";
    boolean check = false;
    while (!check) {
      try {
        String input = readUserInput(instruction);
        ArrayList<String> inputs = parseUserInput(input);
        int numUnit = Integer.parseInt(inputs.get(0));
        // get Territory info
        Territory srcTerri = gameMap.getTerritoryByName(inputs.get(1));
        Territory desTerri = gameMap.getTerritoryByName(inputs.get(2));
        // RULE CHECK check srcTerri, desTerri

        // create new order
        if (type.equalsIgnoreCase("move")) {
          if (numUnit < 0) {
            output.println("Not a valid number input (the number of units to move must be non-negative), please try again");
            continue;
          }
          Action tem = new Action(desTerri, srcTerri, numUnit);
          orders.addMoveAction(tem);
          check = true;
        } else {
          if (numUnit < 1) {
            output.println("Not a valid number input (the number of units to attack must be positive), please try again");
            continue;
          }
          Action tem = new Action(desTerri, srcTerri, numUnit);
          orders.addAttackAction(tem);
          check = true;
        }
      } catch (Exception e) {
        output.println("Not a valid input, please try again. Make sure that you have spelled the territories correctly.");
      }

    }

    return 0;
  }

  /**
   * transform the user input to the right type
   * @param type
   * @return
   */
  private String typeCheck(String type) {
    if (type.equalsIgnoreCase("D") || type.equalsIgnoreCase("done")) {
      return "DONE";
    } else if (type.equalsIgnoreCase("M") || type.equalsIgnoreCase("move")) {
      return "MOVE";
    } else if (type.equalsIgnoreCase("A") || type.equalsIgnoreCase("attack")) {
      return "ATTACK";
    } else {
      throw new IllegalArgumentException("Invalid Type Input\n");
    }
  }

  /**
   * let player to play one round the game
   * @param gameMap
   * @return
   * @throws IOException
   */
  public boolean playOneTurn(RISCMap gameMap) throws IOException {
    // display 3 choices and ask user to choose
    boolean commit = false;
    OnePlayerTurn orders = new OnePlayerTurn();

    while (!commit) {
      try {
        output.print(orders);
        String instruction = "What would you like to do?\n" + "(M)ove\n" + "(A)ttack\n" + "(D)one\n";
        String type = readUserInput(instruction);
        type = typeCheck(type);

        if (type.equalsIgnoreCase("DONE")) {
          // send action obejct to server
          writeData(orders);
          // receive boolean from server
          String response = (String) readData();
          if (response == null) {
            output.println("You successfully commit all your orders!");
            commit = true;
          } else {
            output.println("Unfortunately, your orders were rejected, the issue was:\n");
            output.println(response);
            output.println("Please enter all your orders again.");
            orders = new OnePlayerTurn();
          }
        } else {
          // create new Order
          tryCreateNewOrder(type, orders, gameMap);
        }
      } catch (Exception e) {
        output.println("Input is invalid. Please try again.");
      }
    }
    return true;
  }

  /**
   * check the user input format for continue the game
   * @param type
   * @return
   * @throws IOException
   */
  private boolean playerLoseInputCheck(String type) throws IOException {
    if (type.equalsIgnoreCase("Y") || type.equalsIgnoreCase("yes")) {
      writeData(true);
      return true;
    } else if (type.equalsIgnoreCase("N") || type.equalsIgnoreCase("no")) {
      writeData(false);
      return false;
    } else {
      throw new IllegalArgumentException("Invalid Type Input\n");
    }
  }

  /**
   * help method to check the playlose
   * @return
   * @throws IOException
   */
  public boolean playerLose() throws IOException {
    while (true) {
      try {
        String type = readUserInput("You have lost the game, do you want to continue watch the game? (Y)es or (N)o");
        return playerLoseInputCheck(type);
      } catch (Exception e) {
        output.println("Input is invalid. Please try again.");
      }

    }
  }
/**
 * this allow the user to keep play the game
 * @throws ClassNotFoundException
 * @throws IOException
 */
  public void playTurns() throws ClassNotFoundException, IOException {
    boolean firstTurn = false;
    boolean continueWatchGame = false;
    TextView view = new TextView();
    while (true) {
      RISCMap gameMap = (RISCMap) readData();
      if (gameMap.isGameOver()) {
        Player winner = (Player) readData();
        output.println("The winner is " + winner.getName() + ". Great game everyone!");
        break;
      } else {
        if (continueWatchGame) {
          output.println(view.combatViewSting(gameMap.getAllAttacks()));
          output.println(view.textMapViewString(gameMap));

        } else {
          if (gameMap.getPlayerByName(name).isAlive()) {
            if (firstTurn) {
              output.println(view.combatViewSting(gameMap.getAllAttacks()));
            }
            output.println(view.textMapViewString(gameMap));
            output.println("Player " + name + " it is your turn. You may make as many moves as you like.");
            playOneTurn(gameMap);
          } else {
            if (!playerLose()) {
              break;
            } else {
              continueWatchGame = true;
            }

          }
        }
      }
      firstTurn = true;
    }
  }
/**
 * this will set up the basic game
 * @throws ClassNotFoundException
 * @throws IOException
 */
  public void initGame() throws ClassNotFoundException, IOException {
    output.println("You are connected! Please wait for other players to join the game.");
    RISCMap gameMap = (RISCMap) readData();
    TextView view = new TextView();
    File f = new File(
        getClass().getClassLoader().getResource("gameInfo.txt").getFile());
    BufferedReader br = new BufferedReader(new FileReader(f));
    String line;
    while ((line = br.readLine()) != null) {
      output.println(line);
    }
    br.close();
    readyToPlay();
    output.println(view.textMapBeginViewString(gameMap));
    selectPlayer(gameMap);
    output.println(
        "Please wait for other players to select their names. Once finished, the map will again be displayed\nbelow and you will place your initial units.");
    gameMap = (RISCMap) readData();
    output.println(view.textMapBeginViewString(gameMap));
    output.println("Player: " + getName() + ", it is time to place your initial units.");
    unitPlacement(gameMap);
    output.println(
        "You have successfully placed all your units. Now waiting for other players to finish their placements.");
  }

  /**
   * this will show the rule to game
   * @throws IOException
   */
  public void readyToPlay() throws IOException {
    boolean ready = false;
    while (!ready) {
      output.println("Once you have read the instructions and are ready to play, type 'Yes' below:");
      String ans = input.readLine();
      if (ans.equalsIgnoreCase(ans)) {
        ready = true;
      } else {
        output.println("Incorrect input, try again!");
      }
    }
    output.println("Great, now it is time to select your player name.");
  }

}
