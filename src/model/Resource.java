package model;
public class Resource
{
    private String name;
    private int id;
    private boolean availability;

    public Resource(String name,int id,boolean availability)
    {
        this.name=name;
        this.id=id;
        this.availability=availability;
    }
    public String getName()
    {
        return name;
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
