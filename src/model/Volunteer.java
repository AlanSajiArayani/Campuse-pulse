
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a volunteer in CampusPulse.
 */
public class Volunteer extends User {

    private String volunteerId;
    private String availabilityStatus;
    private List<String> assignedRequestIds;
    private List<String> skills = new ArrayList<>();

    public Volunteer() {
        super();
        this.assignedRequestIds = new ArrayList<>();
    }

    public Volunteer(String userId, String name, String email, String phone,
                     String password, String volunteerId,
                     String availabilityStatus) {
        super(userId, name, email, phone, password, "VOLUNTEER");
        this.volunteerId = volunteerId;
        this.availabilityStatus = availabilityStatus;
        this.assignedRequestIds = new ArrayList<>();
    }

    public String getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(String volunteerId) {
        this.volunteerId = volunteerId;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public List<String> getAssignedRequestIds() {
        return assignedRequestIds;
    }

    public void setAssignedRequestIds(List<String> requestIds) {
        this.assignedRequestIds = requestIds == null
                ? new ArrayList<>()
                : new ArrayList<>(requestIds);
    }

    public void addAssignedRequest(String requestId) {
        if (requestId != null && !assignedRequestIds.contains(requestId)) {
            assignedRequestIds.add(requestId);
        }
    }

    public void removeAssignedRequest(String requestId) {
        assignedRequestIds.remove(requestId);
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills == null
                ? new ArrayList<>()
                : new ArrayList<>(skills);
    }

    public void addSkill(String skill) {
        if (skill != null && !skill.trim().isEmpty() && !hasSkill(skill)) {
            skills.add(skill.trim());
        }
    }

    public boolean hasSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return false;
        }

        for (String existing : skills) {
            if (existing != null && existing.equalsIgnoreCase(skill.trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Volunteer{volunteerId='" + volunteerId
                + "', name='" + getName()
                + "', availabilityStatus='" + availabilityStatus
                + "', assignedRequests=" + assignedRequestIds.size()
                + ", skills=" + skills + "}";
    }
}
