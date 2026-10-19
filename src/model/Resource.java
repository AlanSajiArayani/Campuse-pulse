package model;
public class Resource
{
    private String name;
    private int id;
    private boolean availability;
    private String category;

    public Resource(String name,int id,boolean availability,String category)
    {
        this.name=name;
        this.id=id;
        this.availability=availability;
        this.category=category;
    }
    public String getName()
    {
        return name;
    }
    public String getCategory()
    {
        return category;
    }
    public int getId()
    {
        return id;
    }
    public boolean isAvailable()
    {
        return availability;
    }
    public void setAvailability(boolean availability)
    {
        this.availability = availability;
    }
}
