package controller;

import model.Admin;
import model.Request;
import model.Resource;
import model.User;
import model.Volunteer;
import view.MainView;
import service.ResourceService;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import util.AppConstants;

/**
 * MainController.java
 * Central MVC Controller coordinating views, domain models, services, and repositories.
 *
 * Team Module Ownership:
 * - Alan Saji       : Section 1 (Authentication), Section 4 (Admin Operations), Database Integration
 * - Ardra K Gireesh : Section 2 (Resource Management, Catalog & Inventory)
 * - Ashish K Biju   : Section 3 (Request Lifecycle, Volunteer Management & Dispatch Matching)
 * - Ananya Raju     : Section 5 (Application Startup, View Routing & Screen Architecture)
 */
public class MainController {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private MainView              view;
    private User                  currentUser;
    private model.UserRepository userRepository;
    private AuthController        authController;

    private ResourceService resourceService = new ResourceService();
    private service.RequestService requestService;
    private service.HandoverService handoverService;

    // Request and volunteer data (in-memory for now)
    private List<Request> requests = new ArrayList<>();
    private List<Volunteer> volunteers = new ArrayList<>();

    private int requestCounter = 1;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public MainController(MainView view) {
        this.view           = view;
        this.currentUser    = null;
        this.userRepository = new model.UserRepository();
        this.authController = new AuthController(userRepository);
        this.requestService = new service.RequestService(resourceService);
        this.handoverService = new service.HandoverService(requestService, resourceService);
        initializeSampleData();
    }

    private void initializeSampleData() {
        if (resourceService.getAllResources().isEmpty()) {
            resourceService.addResource(new Resource(
                "RES-001", "Scientific Calculator", "Casio fx-991EX ClassWiz",
                "Electronics", "U004", 5, AppConstants.RESOURCE_STATUS_AVAILABLE
            ));
            resourceService.addResource(new Resource(
                "RES-002", "Digital Multimeter", "Auto-ranging multimeter with probe leads",
                "Electronics", "U004", 2, AppConstants.RESOURCE_STATUS_AVAILABLE
            ));
            resourceService.addResource(new Resource(
                "RES-003", "Algorithms Textbook", "Introduction to Algorithms (CLRS 4th Edition)",
                "Books", "U004", 3, AppConstants.RESOURCE_STATUS_AVAILABLE
            ));
            resourceService.addResource(new Resource(
                "RES-004", "Basketball & Pump", "Spalding standard outdoor basketball with pump",
                "Sports", "U004", 4, AppConstants.RESOURCE_STATUS_AVAILABLE
            ));
            resourceService.addResource(new Resource(
                "RES-005", "Arduino Starter Kit", "Arduino Uno with sensor pack and breadboard",
                "Electronics", "U004", 2, AppConstants.RESOURCE_STATUS_AVAILABLE
            ));
        }

        // Initialize default volunteer if not already registered
        User volUser = userRepository.findById("U003");
        if (volUser != null && volunteers.isEmpty()) {
            Volunteer v = registerVolunteer(volUser);
            if (v != null) {
                List<String> skills = new ArrayList<>();
                skills.add("First Aid");
                skills.add("Lab Assistant");
                skills.add("Tutoring");
                v.setSkills(skills);
            }
        }

        // Initialize sample requests if empty
        if (requests.isEmpty()) {
            createRequest(AppConstants.REQUEST_TYPE_GENERAL, "U001",
                "Need guidance with lab setup for physics experiment", "Lab Assistant");
            createRequest(AppConstants.REQUEST_TYPE_EMERGENCY, "U001",
                "First aid needed for minor sprain on sports ground", "First Aid");
        }
    }

    public ResourceService getResourceService() {
        return resourceService;
    }

    public service.RequestService getRequestService() {
        return requestService;
    }

    public service.HandoverService getHandoverService() {
        return handoverService;
    }

    public Volunteer getVolunteerByUserId(String userId) {
        if (userId == null) {
            return null;
        }
        for (Volunteer v : volunteers) {
            if (userId.equals(v.getUserId())) {
                return v;
            }
        }
        return null;
    }

    // =========================================================================
    // SECTION 1 – AUTHENTICATION
    // Owner: Alan Saji
    // =========================================================================

    public boolean login(String email, String password) {
        // LIMITATION: password is compared in plaintext. Hashing deferred to a later phase.
        boolean success = authController.login(email, password);
        if (success) {
            this.currentUser = authController.getCurrentUser();
        } else {
            // Ensure no stale session remains on failure
            this.currentUser = null;
        }
        return success;
    }

    public void logout() {
        authController.logout();
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // =========================================================================
    // SECTION 2 – RESOURCE MANAGEMENT
    // Owner: Ardra K Gireesh
    // =========================================================================

    public void addResource(Resource resource) {
        resourceService.addResource(resource);
    }

    public List<Resource> searchResources(String keyword) {
        return resourceService.searchResources(keyword);
    }

    public boolean borrowResource(String resourceId, String borrowerId) {
        Resource resource = resourceService.findById(resourceId);

        if (resource == null) {
            return false;
        }

        return resource.borrowResource(borrowerId);
    }

    public boolean returnResource(String resourceId, String returnerId) {
        Resource resource = resourceService.findById(resourceId);

        if (resource == null) {
            return false;
        }

        return resource.returnResource(returnerId);
    }

    public List<Resource> getResourceCatalog() {
        return resourceService.getAllResources();
    }

    // =========================================================================
    // SECTION 3 – REQUEST & VOLUNTEER MANAGEMENT
    // Owner: Ashish K Biju
    // =========================================================================

    /**
     * Creates a request without a required skill.
     */
    public Request createRequest(
            String requestType,
            String requesterId,
            String description) {

        return createRequest(
                requestType,
                requesterId,
                description,
                null
        );
    }

    /**
     * Creates a request with an optional required skill.
     */
    public Request createRequest(
            String requestType,
            String requesterId,
            String description,
            String requiredSkill) {

        if (requestType == null
                || requesterId == null
                || description == null
                || description.trim().isEmpty()) {

            return null;
        }

        String requestId =
                String.format("REQ-%03d", requestCounter++);

        String createdAt =
                java.time.LocalDateTime.now()
                        .withNano(0)
                        .toString();

        Request request = new Request(
                requestId,
                requestType,
                requesterId,
                description,
                AppConstants.REQUEST_STATUS_OPEN,
                null,
                createdAt
        );

        request.setRequiredSkill(requiredSkill);

        requests.add(request);

        // Immediately attempt matching when an emergency is created.
        if (AppConstants.REQUEST_TYPE_EMERGENCY.equals(requestType)) {
            assignWaitingRequests();
        }

        return request;
    }

    /**
     * Assigns an available volunteer to an open request.
     */
    public boolean assignVolunteer(
            String requestId,
            String volunteerId) {

        Request request = findRequest(requestId);
        Volunteer volunteer = findVolunteer(volunteerId);

        if (request == null || volunteer == null) {
            return false;
        }

        if (!AppConstants.REQUEST_STATUS_OPEN.equals(
                request.getStatus())) {
            return false;
        }

        if (!AppConstants.VOLUNTEER_AVAILABLE.equals(
                volunteer.getAvailabilityStatus())) {
            return false;
        }

        request.assignVolunteer(volunteerId);

        request.updateRequestStatus(
                AppConstants.REQUEST_STATUS_ASSIGNED
        );

        volunteer.addAssignedRequest(requestId);

        volunteer.setAvailabilityStatus(
                AppConstants.VOLUNTEER_BUSY
        );

        return true;
    }

    /**
     * Updates a request's status when the transition is valid.
     */
    public void updateRequestStatus(
            String requestId,
            String newStatus) {

        Request request = findRequest(requestId);

        if (request == null || newStatus == null) {
            return;
        }

        String currentStatus = request.getStatus();

        boolean allowed =
                (
                    AppConstants.REQUEST_STATUS_OPEN.equals(currentStatus)
                    &&
                    (
                        AppConstants.REQUEST_STATUS_ASSIGNED.equals(newStatus)
                        || AppConstants.REQUEST_STATUS_CANCELLED.equals(newStatus)
                    )
                )
                ||
                (
                    AppConstants.REQUEST_STATUS_ASSIGNED.equals(currentStatus)
                    &&
                    (
                        AppConstants.REQUEST_STATUS_RESOLVED.equals(newStatus)
                        || AppConstants.REQUEST_STATUS_CANCELLED.equals(newStatus)
                    )
                );

        // Ignore invalid status transitions.
        if (!allowed) {
            return;
        }

        request.updateRequestStatus(newStatus);

        // Release the assigned volunteer when work is completed or cancelled.
        if (AppConstants.REQUEST_STATUS_RESOLVED.equals(newStatus)
                || AppConstants.REQUEST_STATUS_CANCELLED.equals(newStatus)) {

            Volunteer volunteer =
                    findVolunteer(request.getAssignedVolunteerId());

            if (volunteer != null) {

                volunteer.removeAssignedRequest(requestId);

                // A volunteer stays busy if other tasks are still assigned.
                if (volunteer.getAssignedRequestIds().isEmpty()) {
                    volunteer.setAvailabilityStatus(
                            AppConstants.VOLUNTEER_AVAILABLE
                    );
                }
            }

            // A newly available volunteer may handle a waiting emergency.
            assignWaitingRequests();
        }
    }

    /**
     * Finds the best available volunteer for a request.
     *
     * Matching rules:
     * 1. Only available volunteers are considered.
     * 2. For normal requests, a specified skill is mandatory.
     * 3. For emergencies, a volunteer without the skill may be used
     *    as a fallback.
     * 4. Volunteers with the required skill receive a higher score.
     * 5. Fewer assigned requests break ties.
     */
    public Volunteer matchVolunteer(Request request) {

        if (request == null) {
            return null;
        }

        String skill = request.getRequiredSkill();

        boolean needsSkill =
                skill != null && !skill.trim().isEmpty();

        boolean emergency =
                AppConstants.REQUEST_TYPE_EMERGENCY.equals(
                        request.getRequestType()
                );

        Volunteer bestVolunteer = null;
        int bestScore = Integer.MIN_VALUE;

        for (Volunteer volunteer : volunteers) {

            if (!AppConstants.VOLUNTEER_AVAILABLE.equals(
                    volunteer.getAvailabilityStatus())) {
                continue;
            }

            boolean skillMatch =
                    needsSkill && volunteer.hasSkill(skill);

            // A normal request cannot be matched to an unskilled volunteer.
            // An emergency may use an unskilled volunteer as a fallback.
            if (needsSkill && !skillMatch && !emergency) {
                continue;
            }

            int currentScore = score(volunteer, skill);

            if (bestVolunteer == null
                    || currentScore > bestScore) {

                bestVolunteer = volunteer;
                bestScore = currentScore;
            }
        }

        return bestVolunteer;
    }

    /**
     * Calculates a volunteer's matching score.
     * A required-skill match is worth 100 points.
     * Each currently assigned request reduces the score by one.
     */
    private int score(Volunteer volunteer, String skill) {

        int score = 0;

        if (skill != null
                && !skill.trim().isEmpty()
                && volunteer.hasSkill(skill)) {

            score += 100;
        }

        if (volunteer.getAssignedRequestIds() != null) {
            score -= volunteer.getAssignedRequestIds().size();
        }

        return score;
    }

    /**
     * Returns requests with emergencies first and older requests
     * before newer requests within the same priority.
     */
    public List<Request> getRequestsByPriority() {

        List<Request> sortedRequests = new ArrayList<>(requests);

        sortedRequests.sort(
                Comparator
                    .comparingInt(
                        (Request request) ->
                            AppConstants.REQUEST_TYPE_EMERGENCY.equals(
                                    request.getRequestType()
                            ) ? 0 : 1
                    )
                    .thenComparing(
                        Request::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())
                    )
        );

        return sortedRequests;
    }

    /**
     * Automatically assigns available volunteers to waiting emergencies.
     * Emergency requests are processed by priority and creation time.
     */
    public void assignWaitingRequests() {

        for (Request request : getRequestsByPriority()) {

            // Only open emergency requests are handled here.
            if (!AppConstants.REQUEST_STATUS_OPEN.equals(
                    request.getStatus())) {
                continue;
            }

            if (!AppConstants.REQUEST_TYPE_EMERGENCY.equals(
                    request.getRequestType())) {
                continue;
            }

            Volunteer match = matchVolunteer(request);

            if (match != null) {
                assignVolunteer(
                        request.getRequestId(),
                        match.getVolunteerId()
                );
            }
        }
    }

    /**
     * Registers an existing user as a volunteer.
     */
    public Volunteer registerVolunteer(User user) {

        if (user == null) {
            return null;
        }

        for (Volunteer volunteer : volunteers) {

            if (volunteer.getUserId().equals(user.getUserId())) {
                return volunteer;
            }
        }

        String volunteerId =
                String.format("VOL-%03d", volunteers.size() + 1);

        Volunteer volunteer = new Volunteer(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                volunteerId,
                AppConstants.VOLUNTEER_AVAILABLE
        );

        volunteers.add(volunteer);

        // A newly registered volunteer may be able to handle a waiting
        // emergency immediately.
        assignWaitingRequests();

        return volunteer;
    }

    /**
     * Updates a volunteer's skills.
     */
    public boolean updateVolunteerSkills(
            String volunteerId,
            List<String> skills) {

        Volunteer volunteer = findVolunteer(volunteerId);

        if (volunteer == null || skills == null) {
            return false;
        }

        volunteer.setSkills(skills);

        // Recheck waiting emergencies after skills change.
        assignWaitingRequests();

        return true;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    /**
     * Finds a request by ID.
     */
    private Request findRequest(String id) {

        if (id == null) {
            return null;
        }

        for (Request request : requests) {

            if (id.equals(request.getRequestId())) {
                return request;
            }
        }

        return null;
    }

    /**
     * Finds a volunteer by volunteer ID.
     */
    private Volunteer findVolunteer(String id) {

        if (id == null) {
            return null;
        }

        for (Volunteer volunteer : volunteers) {

            if (id.equals(volunteer.getVolunteerId())) {
                return volunteer;
            }
        }

        return null;
    }

    // =========================================================================
    // SECTION 4 – ADMIN OPERATIONS
    // Owner: Alan Saji
    // =========================================================================

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void deactivateUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return;
        }
        userRepository.deactivateUser(userId);
    }

    public void activateUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return;
        }
        userRepository.activateUser(userId);
    }

    // =========================================================================
    // SECTION 5 – APPLICATION STARTUP / ROUTING
    // Owner: Ananya Raju (UI flow) | Alan Saji (final integration)
    // =========================================================================

    public void start() {
        view.showLoginScreen();
    }
}