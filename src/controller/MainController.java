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

    public List<Resource> getResourceCatalog() {
        return resourceService.getAllResources();
    }

    // =========================================================================
    // SECTION 3 – REQUEST & VOLUNTEER MANAGEMENT
    // Owner: Ashish K Biju
    // =========================================================================

    /**
     * Creates a new service or emergency request.
     * TODO [Ashish K Biju] – Implement; persist request, trigger matching if emergency
     *
     * @param requestType  the type of request (use AppConstants.REQUEST_TYPE_*)
     * @param requesterId  the userId raising the request
     * @param description  human-readable description of the need
     * @return the created {@link Request} object, or {@code null} on failure
     */
    public Request createRequest(String requestType, String requesterId, String description) {
        // TODO [Ashish K Biju] – Implement
        return null;
    }

    /**
     * Assigns an available volunteer to an open request.
     * TODO [Ashish K Biju] – Implement; update Request and Volunteer records
     *
     * @param requestId   the ID of the request to fulfil
     * @param volunteerId the ID of the volunteer to assign
     * @return {@code true} if assignment succeeded
     */
    public boolean assignVolunteer(String requestId, String volunteerId) {
        // TODO [Ashish K Biju] – Implement
        return false;
    }

    /**
     * Updates the status of an existing request.
     * TODO [Ashish K Biju] – Implement; validate transitions, notify parties
     *
     * @param requestId the ID of the request to update
     * @param newStatus the new status (use AppConstants.REQUEST_STATUS_*)
     */
    public void updateRequestStatus(String requestId, String newStatus) {
        // TODO [Ashish K Biju] – Implement
    }

    /**
     * Finds the best available volunteer for a given request.
     * TODO [Ashish K Biju] – Implement matching algorithm (availability, proximity, etc.)
     *
     * @param request the {@link Request} requiring a volunteer
     * @return the matched {@link Volunteer}, or {@code null} if none available
     */
    public Volunteer matchVolunteer(Request request) {
        // TODO [Ashish K Biju] – Implement
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

