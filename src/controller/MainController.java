package controller;

import model.Admin;
import model.Request;
import model.Resource;
import model.Student;
import model.User;
import model.Volunteer;
import view.MainView;
import service.ResourceService;
import java.util.List;
import java.util.ArrayList;
import util.AppConstants;
/**
 * MainController.java
 * Central controller that wires views, models and future data services.
 * Each team member adds their feature logic in clearly marked sections below.
 *
 * TODO [Alan Saji]       – Authentication, database integration, admin operations, final integration
 * TODO [Ananya Raju]     – UI interactions delegated from MainView
 * TODO [Ardra K Gireesh] – Resource catalog, borrowing and handover tracking
 * TODO [Ashish K Biju]   – Volunteer management, emergency request matching
 */
public class MainController {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private MainView view;
    private User     currentUser;   // The currently logged-in user (null when no session)

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public MainController(MainView view) {
        this.view        = view;
        this.currentUser = null;
        // TODO [Alan Saji] – Initialize data source / repository connections here
    }

    // =========================================================================
    // SECTION 1 – AUTHENTICATION
    // Owner: Alan Saji
    // =========================================================================

    /**
     * Authenticates a user by email and password.
     * On success, sets {@code currentUser} and routes to the correct dashboard.
     *
     * TODO [Alan Saji] – Implement: validate credentials, set currentUser, route
     *
     * @param email    the user's email address
     * @param password the user's password (will be hashed before comparison)
     * @return {@code true} if login succeeded, {@code false} otherwise
     */
    public boolean login(String email, String password) {
        // TODO [Alan Saji] – Implement
        return false;
    }

    /**
     * Logs out the current user and clears the session.
     * TODO [Alan Saji] – Implement session teardown, navigate back to login screen
     */
    public void logout() {
        // TODO [Alan Saji] – Implement
        this.currentUser = null;
    }

    /**
     * Returns the currently authenticated user.
     * TODO [Alan Saji] – Implement session management
     *
     * @return the logged-in {@link User}, or {@code null} if no active session
     */
    public User getCurrentUser() {
        return currentUser;
    }

    // =========================================================================
    // SECTION 2 – RESOURCE MANAGEMENT
    // Owner: Ardra K Gireesh
    // =========================================================================

    /**
     * Adds a new resource to the campus catalog.
     * TODO [Ardra K Gireesh] – Implement; persist to data source
     *
     * @param resource the {@link Resource} object to add
     */
    private ResourceService resourceService = new ResourceService();
    public void addResource(Resource resource) {
        resourceService.addResource(resource);
    }

    /**
     * Searches the resource catalog by name or category keyword.
     * TODO [Ardra K Gireesh] – Implement search/filter logic
     *
     * @param keyword the search term
     * @return a list of matching {@link Resource} objects (empty if none)
     */
    public List<Resource> searchResources(String keyword) {
        return resourceService.searchResources(keyword);
    }

    /**
     * Processes a borrow request for a resource.
     * TODO [Ardra K Gireesh] – Implement; update Resource status, create a BORROW Request
     *
     * @param resourceId the ID of the resource to borrow
     * @param borrowerId the userId of the requester
     * @return {@code true} if the borrow was successful
     */
  
    public boolean borrowResource(String resourceId, String borrowerId) {
        Resource r = resourceService.findById(resourceId);
        if (r == null) {
            return false;
        }
        return r.borrowResource(borrowerId);
    }

    /**
     * Processes the return/handover of a borrowed resource.
     * TODO [Ardra K Gireesh] – Implement; update Resource status and Request status
     *
     * @param resourceId the ID of the resource being returned
     * @param returnerId the userId of the person returning it
     * @return {@code true} if the return was successful
     */
    public boolean returnResource(String resourceId, String returnerId) {
        Resource r = resourceService.findById(resourceId);
        if (r == null) {
            return false;
        }
        return r.returnResource(returnerId);
    }

    // =========================================================================
    // SECTION 3 – REQUEST & VOLUNTEER MANAGEMENT
    // Owner: Ashish K Biju
    // =========================================================================

        // Section 3 data (in-memory for now; Alan's repository replaces this later)
    private List<Request>   requests       = new ArrayList<>();
    private List<Volunteer> volunteers     = new ArrayList<>();
    private int             requestCounter = 1;

    public Request createRequest(String requestType, String requesterId, String description) {
        if (requestType == null || requesterId == null
                || description == null || description.trim().isEmpty()) {
            return null;   // invalid input
        }
        String id  = String.format("REQ-%03d", requestCounter++);
        String now = java.time.LocalDateTime.now().withNano(0).toString();
        Request request = new Request(id, requestType, requesterId, description,
                                      AppConstants.REQUEST_STATUS_OPEN, null, now);
        requests.add(request);

        if (AppConstants.REQUEST_TYPE_EMERGENCY.equals(requestType)) {
            Volunteer match = matchVolunteer(request);
            if (match != null) {
                assignVolunteer(id, match.getVolunteerId());
            }
        }
        return request;
    }

    public boolean assignVolunteer(String requestId, String volunteerId) {
        Request   request   = findRequest(requestId);
        Volunteer volunteer = findVolunteer(volunteerId);
        if (request == null || volunteer == null) return false;
        if (!AppConstants.REQUEST_STATUS_OPEN.equals(request.getStatus())) return false;
        if (!AppConstants.VOLUNTEER_AVAILABLE.equals(volunteer.getAvailabilityStatus())) return false;

        request.assignVolunteer(volunteerId);
        request.updateRequestStatus(AppConstants.REQUEST_STATUS_ASSIGNED);
        volunteer.setAvailabilityStatus(AppConstants.VOLUNTEER_BUSY);
        volunteer.addAssignedRequest(requestId);
        return true;
    }

    public void updateRequestStatus(String requestId, String newStatus) {
        Request request = findRequest(requestId);
        if (request == null || newStatus == null) return;

        String current = request.getStatus();
        boolean allowed =
              (AppConstants.REQUEST_STATUS_OPEN.equals(current) &&
                  (AppConstants.REQUEST_STATUS_ASSIGNED.equals(newStatus)
                   || AppConstants.REQUEST_STATUS_CANCELLED.equals(newStatus)))
           || (AppConstants.REQUEST_STATUS_ASSIGNED.equals(current) &&
                  (AppConstants.REQUEST_STATUS_RESOLVED.equals(newStatus)
                   || AppConstants.REQUEST_STATUS_CANCELLED.equals(newStatus)));
        if (!allowed) return;   // invalid transition, ignore

        request.updateRequestStatus(newStatus);

        // Free the volunteer when the work is finished or cancelled
        if (AppConstants.REQUEST_STATUS_RESOLVED.equals(newStatus)
                || AppConstants.REQUEST_STATUS_CANCELLED.equals(newStatus)) {
            Volunteer v = findVolunteer(request.getAssignedVolunteerId());
            if (v != null) {
                v.setAvailabilityStatus(AppConstants.VOLUNTEER_AVAILABLE);
                v.removeAssignedRequest(requestId);
            }
        }
    }

    public Volunteer matchVolunteer(Request request) {
        for (Volunteer v : volunteers) {
            if (AppConstants.VOLUNTEER_AVAILABLE.equals(v.getAvailabilityStatus())) {
                return v;
            }
        }
        return null;   // nobody available
    }

    /** Upgrades an existing user to a volunteer. */
    public Volunteer registerVolunteer(User user) {
        for (Volunteer v : volunteers) {
            if (v.getUserId().equals(user.getUserId())) return v;   // already registered
        }
        String volId = String.format("VOL-%03d", volunteers.size() + 1);
        Volunteer v = new Volunteer(user.getUserId(), user.getName(), user.getEmail(),
                                    user.getPhone(), user.getPassword(),
                                    volId, AppConstants.VOLUNTEER_AVAILABLE);
        volunteers.add(v);
        return v;
    }

    public List<Request>   getRequests()   { return requests; }
    public List<Volunteer> getVolunteers() { return volunteers; }

    private Request findRequest(String id) {
        if (id == null) return null;
        for (Request r : requests) if (id.equals(r.getRequestId())) return r;
        return null;
    }

    private Volunteer findVolunteer(String id) {
        if (id == null) return null;
        for (Volunteer v : volunteers) if (id.equals(v.getVolunteerId())) return v;
        return null;
    }
    // =========================================================================
    // SECTION 4 – ADMIN OPERATIONS
    // Owner: Alan Saji
    // =========================================================================

    /**
     * Retrieves all registered users.
     * TODO [Alan Saji] – Implement; fetch from data source
     *
     * @return list of all {@link User} objects
     */
    public List<User> getAllUsers() {
        // TODO [Alan Saji] – Implement
        return null;
    }

    /**
     * Deactivates a user account.
     * TODO [Alan Saji] – Implement; enforce admin-only access
     *
     * @param userId the ID of the user to deactivate
     */
    public void deactivateUser(String userId) {
        // TODO [Alan Saji] – Implement
    }

    // =========================================================================
    // SECTION 5 – APPLICATION STARTUP / ROUTING
    // Owner: Ananya Raju (UI flow) | Alan Saji (final integration)
    // =========================================================================

    /**
     * Starts the application: shows the login screen.
     * TODO [Ananya Raju] – Implement initial UI routing
     * TODO [Alan Saji]   – Wire session restoration on restart if needed
     */
    public void start() {
        // TODO [Ananya Raju] – Implement
        view.showLoginScreen();
    }
}

