
/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.util.ArrayList;

public class Player
{
    // instance variables - replace the example below with your own
    public Room currentRoom;
    public ArrayList<String> inventory = new ArrayList<String>();
    public int inventorySize = 0;
    
    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
    }
     
     /**
     * Set the current room
     * @param Room the current room
     */
    public void setCurrentRoom(Room currentRoom)
    {
        this.currentRoom = currentRoom;
    }
    
    /**
     * returns the current room
     * @return gives the current room
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }
    
    /**
     * sets the size for the inventory
     * @param int the inventory size
     */
    public void setInventorySize(int inventory)
    {
        this.inventorySize = inventorySize;
    }
    
    /**
     * gets the size of the inventory
     * @return the size of the inventory
     */
    public int getInventorySize()
    {
        return inventorySize;
    }
    
    /**
     * Add an item to the inventory
     * @param Item the item to be added
     */
    public void addItem(String newItem)
    {
        inventory.add(newItem);
    }
    
    /**
     * Return the entire inventory
     * @return Arraylist
     */
    public ArrayList getInventory()
    {
        return inventory;
    }
    
    
}
