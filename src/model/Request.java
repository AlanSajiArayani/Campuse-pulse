package model;
public class Request {

    private String requestId;
    private String requestType;            
    private String requesterId;            
    private String description;
    private String status;                 
    private String assignedVolunteerId;    
    private String createdAt;             

    public Request() {}

    public Request(String requestId, String requestType, String requesterId,
                   String description, String status,
                   String assignedVolunteerId, String createdAt) {
        this.requestId             = requestId;
        this.requestType           = requestType;
        this.requesterId           = requesterId;
        this.description           = description;
        this.status                = status;
        this.assignedVolunteerId   = assignedVolunteerId;
        this.createdAt             = createdAt;
    }

    public String getRequestId()                  
    { return requestId; 

    }
    public void   setRequestId(String requestId)  
    { this.requestId = requestId; 

    }

    public String getRequestType()                    
    { return requestType; 

    }
    public void   setRequestType(String requestType)  
    { this.requestType = requestType; 

    }

    public String getRequesterId()                    
    { return requesterId; 

    }
    public void   setRequesterId(String requesterId)  
    { this.requesterId = requesterId; 

    }

    public String getDescription()                    
    { return description; 

    }
    public void   setDescription(String description)  
    { this.description = description; 

    }

    public String getStatus()               
    { return status; 

    }
    public void   setStatus(String status)  
    { this.status = status; 

    }

    public String getAssignedVolunteerId()                         
    { return assignedVolunteerId; 

    }
    public void   setAssignedVolunteerId(String assignedVolunteerId) 
    { this.assignedVolunteerId = assignedVolunteerId; 

    }

    public String getCreatedAt()                  
    { return createdAt; 

    }
    public void   setCreatedAt(String createdAt)  
    { this.createdAt = createdAt; 
        
    }

    
    public void updateRequestStatus(String newStatus) {
        // TODO [Ashish K Biju] – Implement
    }

    public void assignVolunteer(String volunteerId) {
        // TODO [Ashish K Biju] – Implement
    }

    @Override
    public String toString() {
        return "Request{requestId='" + requestId + "', requestType='" + requestType +
               "', requesterId='" + requesterId + "', status='" + status +
               "', assignedVolunteerId='" + assignedVolunteerId + "'}";
    }
}

