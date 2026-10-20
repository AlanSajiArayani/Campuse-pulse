package interfaces;

import java.util.List;
import model.ResourceRequest;
import model.StatusHistory;

public interface Trackable
{
    boolean canMoveTo(String current, String next);

    boolean updateStatus(ResourceRequest req, String next);

    List<StatusHistory> getHistory(String requestId);
}