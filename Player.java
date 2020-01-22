
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
        
    public ArrayList<Item> inventory = new ArrayList<Item>();
    
    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
    }
     
     /**
     * Set the current room
     * @param int the current room
     */
    public void setCurrentRoom(Room currentRoom)
    {
        this.currentRoom = currentRoom;
    }
    
    public Room getCurrentRoom()
    {
        return currentRoom;
    }
    
    /**
     * Add an item to the inventory
     * @param Item the item to be added
     */
    public void addItem(Item newItem)
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
