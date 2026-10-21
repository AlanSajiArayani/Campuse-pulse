package model;
import util.AppConstants;

public class Resource {

    private String resourceId;
    private String resourceName;
    private String description;
    private String category;
    private String ownerId;
    private int    quantity;
    private String status;

    public Resource() {}

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

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean borrowResource(String borrowerId) {
        if (quantity > 0 && AppConstants.RESOURCE_STATUS_AVAILABLE.equals(status)) {
            quantity = quantity - 1;
            if (quantity == 0) {
                status = AppConstants.RESOURCE_STATUS_BORROWED;
            }
            return true;
        }
        return false;
    }

    public boolean returnResource(String returnerId) {
        quantity = quantity + 1;
        status = AppConstants.RESOURCE_STATUS_AVAILABLE;
        return true;
    }

    @Override
    public String toString() {
        return "Resource{resourceId='" + resourceId + "', resourceName='" + resourceName +
               "', category='" + category + "', quantity=" + quantity +
               ", status='" + status + "'}";
    }
}


