package util;

/**
 * AppConstants.java
 * Shared application-wide constants for roles, statuses, UI labels, and system metadata.
 */
public class AppConstants {

    // -------------------------------------------------------------------------
    // User Roles
    // -------------------------------------------------------------------------
    public static final String ROLE_STUDENT   = "STUDENT";
    public static final String ROLE_VOLUNTEER = "VOLUNTEER";
    public static final String ROLE_STAFF     = "STAFF";
    public static final String ROLE_ADMIN     = "ADMIN";

    // -------------------------------------------------------------------------
    // Resource Status Values
    // -------------------------------------------------------------------------
    public static final String RESOURCE_STATUS_AVAILABLE  = "AVAILABLE";
    public static final String RESOURCE_STATUS_BORROWED   = "BORROWED";
    public static final String RESOURCE_STATUS_UNAVAILABLE = "UNAVAILABLE";

    // -------------------------------------------------------------------------
    // Request Status Values
    // -------------------------------------------------------------------------
    public static final String REQUEST_STATUS_OPEN       = "OPEN";
    public static final String REQUEST_STATUS_ASSIGNED   = "ASSIGNED";
    public static final String REQUEST_STATUS_RESOLVED   = "RESOLVED";
    public static final String REQUEST_STATUS_CANCELLED  = "CANCELLED";

    // -------------------------------------------------------------------------
    // Request Types
    // -------------------------------------------------------------------------
    public static final String REQUEST_TYPE_BORROW    = "BORROW";
    public static final String REQUEST_TYPE_EMERGENCY = "EMERGENCY";
    public static final String REQUEST_TYPE_GENERAL   = "GENERAL";

    // -------------------------------------------------------------------------
    // Volunteer Availability Status
    // -------------------------------------------------------------------------
    public static final String VOLUNTEER_AVAILABLE   = "AVAILABLE";
    public static final String VOLUNTEER_BUSY        = "BUSY";
    public static final String VOLUNTEER_UNAVAILABLE = "UNAVAILABLE";

    // -------------------------------------------------------------------------
    // Application Metadata
    // -------------------------------------------------------------------------
    public static final String APP_NAME    = "CampusPulse";
    public static final String APP_VERSION = "1.0.0";
    
    // -------------------------------------------------------------------------
    // UI Screen Titles
    // -------------------------------------------------------------------------
    public static final String TITLE_LOGIN = "CampusPulse - Login";
    public static final String TITLE_STUDENT_DASHBOARD = "Student Dashboard";
    public static final String TITLE_VOLUNTEER_DASHBOARD = "Volunteer Dashboard";
    public static final String TITLE_STAFF_DASHBOARD = "Staff Dashboard";
    public static final String TITLE_ADMIN_PANEL = "Admin Panel";
    public static final String TITLE_RESOURCE_CATALOG = "Resource Catalog";
    public static final String TITLE_REQUEST_LIST = "Request List";

    // -------------------------------------------------------------------------
    // Common UI Labels
    // -------------------------------------------------------------------------
    public static final String LABEL_LOGIN = "Login";
    public static final String LABEL_LOGOUT = "Logout";
    public static final String LABEL_EMAIL = "Email";
    public static final String LABEL_PASSWORD = "Password";
    public static final String LABEL_BACK = "Back";
    public static final String LABEL_SEARCH = "Search";
    public static final String LABEL_SUBMIT = "Submit";
    public static final String LABEL_CANCEL = "Cancel";

    // Prevent instantiation
    private AppConstants() {}
}

