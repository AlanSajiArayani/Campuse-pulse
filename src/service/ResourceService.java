package service;
import java.util.List;
import java.util.ArrayList;
import model.Resource;
public class ResourceService
{
    private List<Resource> resources= new ArrayList<>();
    public void addResource(Resource r)
    {
        resources.add(r);
    }
    public List<Resource> searchResources(String keyword)
    {
        List<Resource> found= new ArrayList<>();
        for(Resource r :resources)
        {
            if (r.getResourceName().toLowerCase().contains(keyword.toLowerCase())) {
                found.add(r);   
            }
        }
        return found;
    }
}
