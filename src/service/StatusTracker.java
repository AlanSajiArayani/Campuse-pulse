package service;

import interfaces.Trackable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.ResourceRequest;
import model.StatusHistory;

public class StatusTracker implements Trackable
{
    private List<StatusHistory> history = new ArrayList<>();

    public boolean canMoveTo(String current, String next)
    {
        if (ResourceRequest.STATUS_PENDING.equals(current))
        {
            return ResourceRequest.STATUS_ACCEPTED.equals(next)
                || ResourceRequest.STATUS_DECLINED.equals(next)
                || ResourceRequest.STATUS_CANCELLED.equals(next);
        }
        if (ResourceRequest.STATUS_ACCEPTED.equals(current))
        {
            return ResourceRequest.STATUS_COMPLETED.equals(next)
                || ResourceRequest.STATUS_CANCELLED.equals(next);
        }
        return false;
    }

    public boolean updateStatus(ResourceRequest req, String next)
    {
        String old = req.getStatus();
        if (!canMoveTo(old, next))
        {
            return false;
        }
        req.setStatus(next);
        history.add(new StatusHistory(req.getRequestId(), old, next,
                LocalDateTime.now().toString()));
        return true;
    }

    public List<StatusHistory> getHistory(String requestId)
    {
        List<StatusHistory> result = new ArrayList<>();
        for (StatusHistory h : history)
        {
            if (h.getRequestId().equals(requestId))
            {
                result.add(h);
            }
        }
        return result;
    }
}