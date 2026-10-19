package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Resource;
import model.ResourceRequest;
import util.AppConstants;

public class RequestService
{
    private ResourceService resourceService;
    private List<ResourceRequest> requests = new ArrayList<>();
    private int counter = 0;

    public RequestService(ResourceService resourceService)
    {
        this.resourceService = resourceService;
    }

    public ResourceRequest submitRequest(String requesterId, String resourceId)
    {
        Resource r = resourceService.findById(resourceId);
        if (r == null)
        {
            return null;
        }
        if (r.getOwnerId().equals(requesterId))
        {
            return null;
        }
        if (!AppConstants.RESOURCE_STATUS_AVAILABLE.equals(r.getStatus()))
        {
            return null;
        }
        counter = counter + 1;
        ResourceRequest req = new ResourceRequest("Q" + counter, requesterId,
                resourceId, 1, LocalDate.now().toString());
        requests.add(req);
        return req;
    }
        public ResourceRequest findRequest(String requestId)
    {
        for (ResourceRequest q : requests)
        {
            if (q.getRequestId().equals(requestId))
            {
                return q;
            }
        }
        return null;
    }

    private int countAccepted(String resourceId)
    {
        int count = 0;
        for (ResourceRequest q : requests)
        {
            if (q.getResourceId().equals(resourceId)
                && ResourceRequest.STATUS_ACCEPTED.equals(q.getStatus()))
            {
                count = count + 1;
            }
        }
        return count;
    }

    public boolean accept(String requestId, String ownerId)
    {
        ResourceRequest req = findRequest(requestId);
        if (req == null)
        {
            return false;
        }
        Resource r = resourceService.findById(req.getResourceId());
        if (r == null)
        {
            return false;
        }
        if (!r.getOwnerId().equals(ownerId))
        {
            return false;
        }
        if (!ResourceRequest.STATUS_PENDING.equals(req.getStatus()))
        {
            return false;
        }
        if (countAccepted(req.getResourceId()) >= r.getQuantity())
        {
            return false;
        }
        req.setStatus(ResourceRequest.STATUS_ACCEPTED);
        return true;
    }
}