package model;

public class StatusHistory {
    private String requestId;
    private String oldStatus;
    private String newStatus;
    private String changedAt;

    public StatusHistory(String requestId, String oldStatus, String newStatus, String changedAt) {
        this.requestId = requestId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
    }

    public String getRequestId() { return requestId; }
    public String getOldStatus() { return oldStatus; }
    public String getNewStatus() { return newStatus; }
    public String getChangedAt() { return changedAt; }

    @Override
    public String toString() {
        return requestId + ": " + oldStatus + " -> " + newStatus + " at " + changedAt;
    }
}
