package model;
import util.AppConstants;

/**
 * Resource.java
 * Represents a physical or digital resource available in the campus catalog.
 *
 * TODO [Ardra K Gireesh] – Resource catalog, borrowing and handover tracking
 *
 * SHARED MODEL – Do NOT duplicate fields or create a parallel resource class.
 */
public class Resource {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private String resourceId;
    private String resourceName;
    private String description;
    private String category;
    private String ownerId;    // userId of the Staff/Admin who owns this resource
    private int    quantity;
    private String status;     // Use AppConstants.RESOURCE_STATUS_* values

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Default (no-arg) constructor. */
    public Resource() {}

    /** Full constructor. */
    public Resource(String resourceId, String resourceName, String description,
                    String category, String ownerId, int quantity, String status) {
        this.resourceId   = resourceId;
        this.resourceName = resourceName;
        this.description  = description;
        this.category     = category;
        this.ownerId      = ownerId;
        this.quantity     = quantity;
        this.status       = status;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getResourceId()                   { return resourceId; }
    public void   setResourceId(String resourceId)  { this.resourceId = resourceId; }

    public String getResourceName()                     { return resourceName; }
    public void   setResourceName(String resourceName)  { this.resourceName = resourceName; }

    public String getDescription()                    { return description; }
    public void   setDescription(String description)  { this.description = description; }

    public String getCategory()                   { return category; }
    public void   setCategory(String category)    { this.category = category; }

    public String getOwnerId()                { return ownerId; }
    public void   setOwnerId(String ownerId)  { this.ownerId = ownerId; }

    public int  getQuantity()             { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getStatus()               { return status; }
    public void   setStatus(String status)  { this.status = status; }

    // -------------------------------------------------------------------------
    // Resource-specific method placeholders
    // TODO [Ardra K Gireesh] – Implement resource catalog operations below
    // -------------------------------------------------------------------------

    /**
     * Marks this resource as borrowed and decrements available quantity.
     * TODO [Ardra K Gireesh] – Implement borrowing logic and handover tracking
     *
     * @param borrowerId userId of the person borrowing this resource
     * @return {@code true} if borrowing succeeded, {@code false} otherwise
     */
    public boolean borrowResource(String borrowerId) 
    {
         if (quantity > 0 && AppConstants.RESOURCE_STATUS_AVAILABLE.equals(status)) {
            quantity = quantity - 1;
            if (quantity == 0) {
                status = AppConstants.RESOURCE_STATUS_BORROWED;
            }
            return true;
        }
        return false;
    }

    /**
     * Marks this resource as returned and increments available quantity.
     * TODO [Ardra K Gireesh] – Implement handover/return tracking
     *
     * @param returnerId userId of the person returning this resource
     * @return {@code true} if return succeeded, {@code false} otherwise
     */
    public boolean returnResource(String returnerId) {
        // TODO [Ardra K Gireesh] – Implement
        return false;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Resource{resourceId='" + resourceId + "', resourceName='" + resourceName +
               "', category='" + category + "', quantity=" + quantity +
               ", status='" + status + "'}";
    }
}

