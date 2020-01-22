
/**
 * Write a description of class Item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    public int weight;
    public String description;

    /**
     * Constructor for objects of class Item
     */
    public Item(int weight, String description)
    {
        this.weight = 0;
        this.description = description;
    }

     /**
     * @return The description of the item.
     */
    public String getDescription()
    {
        return description;
    }
    
     /**
     * @return The weight of the item.
     */
    public int getWeight()
    {
        return weight;
    }
}
