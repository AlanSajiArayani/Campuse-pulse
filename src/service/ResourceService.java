package service;
import java.util.List;
import java.util.ArrayList;
import model.Resource;
import util.AppConstants;
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
    public List<Resource> searchResources(String keyword,String category)
    {
        return searchResources(keyword, category,false);
    }
    public List<Resource> searchResources(String keyword, String category, boolean availableOnly)
    {
        List<Resource> found = new ArrayList<>();
        for (Resource r : resources)
        {
            boolean nameMatches = r.getResourceName().toLowerCase().contains(keyword.toLowerCase());
            boolean categoryMatches = r.getCategory().equalsIgnoreCase(category);
            boolean statusOk = !availableOnly
                    || AppConstants.RESOURCE_STATUS_AVAILABLE.equals(r.getStatus());

            if (nameMatches && categoryMatches && statusOk)
            {
                found.add(r);
            }
        }
        return found;
    }
    public Resource findById(String resourceId)
    {
        for (Resource r : resources)
        {
            if (r.getResourceId().equals(resourceId))
            {
                return r;
            }
        }
        return null;
    }
    public boolean removeResource(String resourceId, String userId)
    {
        Resource r = findById(resourceId);
        if (r == null)
        {
            return false;
        }
        if (!r.getOwnerId().equals(userId))
        {
            return false;
        }
        resources.remove(r);
        return true;
    }
        public boolean editResource(String resourceId, String userId,
                                String newName, String newDescription, String newCategory)
    {
        Resource r = findById(resourceId);
        if (r == null)
        {
            return false;
        }
        if (!r.getOwnerId().equals(userId))
        {
            return false;
        }
        r.setResourceName(newName);
        r.setDescription(newDescription);
        r.setCategory(newCategory);
        return true;
    }
}
