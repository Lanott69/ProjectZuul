
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import java.util.ArrayList;

public class Game 
{
    private Parser parser;
    private Player player;
    private int itemToTake;
    private Room previousRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        createItems();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        // initialise room exits
        outside.setExits(null, theater, lab, pub, null, null);
        theater.setExits(null, null, null, outside, null, null);
        pub.setExits(null, outside, null, null, null, null);
        lab.setExits(outside, office, null, null, null, null);
        office.setExits(null, null, null, lab, null, null);

        player.currentRoom = outside;  // start game outside
    }
    
    private void createItems()
    {
        Item bread, key, mcguffin;
        
        bread = new Item("a piece of bread");
        key = new Item("a key for a lock");
        mcguffin = new Item("probably a quest item");
        
        bread.setItemLocation("pub");
        key.setItemLocation("theater");
        mcguffin.setItemLocation("lab");
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("You are " + player.currentRoom.getDescription());
        System.out.print("Exits: ");
        if(player.currentRoom.northExit != null) {
            System.out.print("north ");
        }
        if(player.currentRoom.eastExit != null) {
            System.out.print("east ");
        }
        if(player.currentRoom.southExit != null) {
            System.out.print("south ");
        }
        if(player.currentRoom.westExit != null) {
            System.out.print("west ");
        }
        if(player.currentRoom.upExit != null) {
            System.out.print("up ");
        }
        if(player.currentRoom.downExit != null) {
            System.out.print("down ");
        }
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("info")) {
            printLocationInfo();
        }
        else if (commandWord.equals("look")) {
            look();
        }
        else if (commandWord.equals("inventory")) {
            player.getInventory();
        }
        else if (commandWord.equals("take"))
        {
            takeItem(command);
        }
        else if (commandWord.equals("back")) {
            back();
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }

    private void look()
    {
        System.out.println(player.currentRoom.getDescription());
    }
    
    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north")) {
            nextRoom = player.currentRoom.northExit;
        }
        if(direction.equals("east")) {
            nextRoom = player.currentRoom.eastExit;
        }
        if(direction.equals("south")) {
            nextRoom = player.currentRoom.southExit;
        }
        if(direction.equals("west")) {
            nextRoom = player.currentRoom.westExit;
        }
        if(direction.equals("up")) {
            nextRoom = player.currentRoom.upExit;
        }
        if(direction.equals("down")) {
            nextRoom = player.currentRoom.downExit;
        }

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            player.currentRoom = nextRoom;
            System.out.println("You are " + player.currentRoom.getDescription());
            System.out.print("Exits: ");
            if(player.currentRoom.northExit != null) {
                System.out.print("north ");
            }
            if(player.currentRoom.eastExit != null) {
                System.out.print("east ");
            }
            if(player.currentRoom.southExit != null) {
                System.out.print("south ");
            }
            if(player.currentRoom.westExit != null) {
                System.out.print("west ");
            }
            System.out.println();
        }
    }
    
    private void back() {
        player.currentRoom = previousRoom;
        System.out.println("You are " + player.currentRoom.getDescription());
        System.out.print("Exits: ");
        if(player.currentRoom.northExit != null) {
            System.out.print("north ");
        }
        if(player.currentRoom.eastExit != null) {
            System.out.print("east ");
        }
        if(player.currentRoom.southExit != null) {
            System.out.print("south ");
        }
        if(player.currentRoom.westExit != null) {
            System.out.print("west ");
        }
        if(player.currentRoom.upExit != null) {
            System.out.print("up ");
        }
        if(player.currentRoom.downExit != null) {
            System.out.print("down ");
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    private void printLocationInfo()
    {
        System.out.println("You are " + player.currentRoom.getDescription());
        System.out.print("Exits: ");
        if(player.currentRoom.northExit != null) {
            System.out.print("north ");
        }
        if(player.currentRoom.eastExit != null) {
            System.out.print("east ");
        }
        if(player.currentRoom.southExit != null) {
            System.out.print("south ");
        }
        if(player.currentRoom.westExit != null) {
            System.out.print("west ");
        }
        System.out.println();
        
    }
    
    private void takeItem(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("What do you want to pick up?");
        }
        
        if(command.secondWord == "bread") {
            if(player.currentRoom == createItems.bread.itemLocation) {
                player.addItem(command);
                }
            }
        }
    
}
