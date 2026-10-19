package model;

import util.AppConstants;

public class ResourceRequest extends Request
{
    public static final String STATUS_PENDING   = "PENDING";
    public static final String STATUS_ACCEPTED  = "ACCEPTED";
    public static final String STATUS_DECLINED  = "DECLINED";
    public static final String STATUS_CANCELLED = AppConstants.REQUEST_STATUS_CANCELLED;
    public static final String STATUS_COMPLETED = "COMPLETED";

    private String resourceId;
    private int quantity;
    private String handoverCodeHash;

    public ResourceRequest(String requestId, String requesterId,String resourceId, int quantity, String createdAt)
    {
        super(requestId, AppConstants.REQUEST_TYPE_BORROW, requesterId,
              "Borrow request for " + resourceId, STATUS_PENDING, null, createdAt);
        this.resourceId = resourceId;
        this.quantity = quantity;
    }

    public String getResourceId() 
    {
         return resourceId; 
    }
    public int getQuantity() 
    {
         return quantity;
    }
    public String getHandoverCodeHash()
    { 
        return handoverCodeHash;
    }
    public void setHandoverCodeHash(String handoverCodeHash)
    { this.handoverCodeHash = handoverCodeHash; 
        
    }
}