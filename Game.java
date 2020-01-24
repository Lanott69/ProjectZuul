
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
import java.util.Stack;

public class Game 
{
    private Parser parser;
    private Player player;
    private int itemToTake;
    private int event;
    private String tempItem;
    Stack<Room> history = new Stack<Room>();
        
    public static void main(String[] args)
{
    Game mainGame = new Game();
    mainGame.play();
    
}
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        player = new Player();
        createRooms();
        createItems();
        parser = new Parser();        
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room bedroom, hallway, kitchen, square, bakery, butcher, garden, attic;
      
        // create the rooms
        bedroom = new Room("just your average bedroom");
        hallway = new Room("a hallway, what more do you want?");
        kitchen = new Room("your kitchen. For some reason there's an elf in here that is keeping your fridge hostage for bread.");
        square = new Room("a town square with several stores");
        bakery = new Room("the local bakery. it's guarded by an angry gnome who wants a piece of ham");
        butcher = new Room("the local butcher's. A dwarf is keeping the door closed. she wants an apple to open the door");
        garden = new Room("a garden with an apple tree");
        attic = new Room("an attic full of holes, AND YOU FALL INTO ONE!");
        // initialise room exits
        bedroom.setExits(hallway, null, null, null, null, null);
        hallway.setExits(null, null, bedroom, null, attic, kitchen);
        kitchen.setExits(square, null, garden, null, hallway, null);
        square.setExits(null, bakery, kitchen, butcher, null, null);
        bakery.setExits(null, null, null, square, null, null);
        butcher.setExits(null, square, null, null, null, null);
        garden.setExits(kitchen, null, null, null, null, null);
        attic.setExits(null, null, null, null, null, null);
        
        bedroom.setEvents(0);
        hallway.setEvents(0);
        kitchen.setEvents(1);
        square.setEvents(0);
        bakery.setEvents(0);
        butcher.setEvents(0);
        garden.setEvents(0);
        attic.setEvents(1);

        player.currentRoom = bedroom;  // start game outside
    }
    
    /**
     * this method creates the items in the game
     */
    private void createItems()
    {
        Item bread, apple, ham;
        
        bread = new Item("a piece of bread");
        apple = new Item("a key for a lock");
        ham = new Item("probably a quest item");
        
        bread.setItemLocation("bakery");
        apple.setItemLocation("garden");
        ham.setItemLocation("butcher");
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
        System.out.println("Welcome to the Grand Hunger Adventure");
        System.out.println("This is a short game about you waking up at night to get a snack");
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
        System.out.println("You are hungry. You search for food");
        System.out.println("around your house.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go, quit, help, inventory, take,");
        System.out.println("   give, look and back"); 
    }

    /**
     * this method prints the information about what the room looks like again
     */
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
            if(player.currentRoom.event == 1) {
                goBack();
            }
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
    
    /**
     * this method prints the information about your current location
     */
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
    
    /**
     * this method processes the item youŕe trying to take
     * @param command the item youŕe trying to pick up
     */
    private void takeItem(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("What do you want to pick up?");
        }
        if(player.inventorySize < 5) {
            if(command.secondWord == "apple") {
                if(player.currentRoom.getDescription() == "a garden with an apple tree") {
                    player.addItem("apple");
                    System.out.println("You picked up an apple");
                }
            }
            
            
        
            else
            {
                System.out.println("There is no such item here");
            }
            player.inventorySize++;
        }
        
        else {
            System.out.println("Your inventory is already full");
        }
    }
    
    /**
     * this method includes all the interactions between you and the other characters
     * @param command this is the item you are trying to give
     */
    private void giveItem(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("What do you want to give away?");
        }
       
        if(command.secondWord == "bread") {
            player.inventory.forEach((index) -> tempItem = index); {
            if(tempItem == "bread") {
            if(player.currentRoom.getDescription() == "your kitchen. For some reason there's an elf in here that is keeping your fridge hostage for bread.") {
                player.inventory.remove("bread");
                System.out.println("The elf says 'thank you, i was hungry' and leaves");
                System.out.println("Finally, you can get your snack! you open the fridge");
                System.out.println("and see... that you forgot to buy food yesterday...");
                System.out.println("thank you for playing. type quit to end the game");
            }
            else if(player.currentRoom.getDescription() == "the local bakery. it's guarded by an angry gnome who wants a piece of ham") {
                System.out.println("the gnome refuses the bread on the grounds of");
                System.out.println("having a gluten allergy");
            }
            else if(player.currentRoom.getDescription() == "the local butcher's. A dwarf is keeping the door closed. she wants an apple to open the door") {
                System.out.println("the dwarf says she doesn't like bread");
            }
            else {
                System.out.println("there is no one here to give that to");
            }
          }
         }
        }
        
        if(command.secondWord == "apple") {
            player.inventory.forEach((index) -> tempItem = index); {
            if(tempItem == "apple") {
            if(player.currentRoom.getDescription() == "your kitchen. For some reason there's an elf in here that is keeping your fridge hostage for bread.") {
                player.inventory.remove("apple");
                System.out.println("The elf says he's allergic and dies.");
                System.out.println("That was random... ah well.");
                System.out.println("Finally, you can get your snack! you open the fridge");
                System.out.println("and see... that you forgot to buy food yesterday...");
                System.out.println("thank you for playing. type quit to end the game");
            }
            else if(player.currentRoom.getDescription() == "the local bakery. it's guarded by an angry gnome who wants a piece of ham") {
                System.out.println("the gnome throws the apple back at you.");
            }
            else if(player.currentRoom.getDescription() == "the local butcher's. A dwarf is keeping the door closed. she wants an apple to open the door") {
                player.inventory.remove("apple");
                System.out.println("the dwarf thanks you for the apple and gives you");
                System.out.println("a piece of ham.");
                player.inventory.add("ham");
            }
            else {
                System.out.println("there is no one here to give that to");
            }
          }
         }
        }
        
        if(command.secondWord == "ham") {
            player.inventory.forEach((index) -> tempItem = index); {
            if(tempItem == "ham") {
            if(player.currentRoom.getDescription() == "your kitchen. For some reason there's an elf in here that is keeping your fridge hostage for bread.") {
                System.out.println("The elf says he's a vegetarian");
            }
            else if(player.currentRoom.getDescription() == "the local bakery. it's guarded by an angry gnome who wants a piece of ham") {
                player.inventory.remove("ham");
                System.out.println("the gnome goes ham on the ham (get it?)");
                System.out.println("he drops some bread and you pick it up");
                player.inventory.add("bread");
            }
            else if(player.currentRoom.getDescription() == "the local butcher's. A dwarf is keeping the door closed. she wants an apple to open the door") {
                System.out.println("the dwarf says she doesn't need it, because she");
                System.out.println("has an entire butcher's shop here");
            }
            else {
                System.out.println("there is no one here to give that to");
            }
          }
         }
        }
    }
    
    /**
     * this method makes you go back a few rooms
     */
    public void goBack()
    {
         history.push(player.currentRoom);
            history.pop();
            player.currentRoom = history.peek();
            System.out.println("You are " + player.currentRoom.getDescription());
            history.pop();
    }
    

}
