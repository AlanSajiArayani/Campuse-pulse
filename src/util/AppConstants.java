package util;

/**
 * AppConstants.java
 * Shared application-wide constants.
 *
 * TODO [Ananya Raju] – Add UI-related constants (screen titles, labels, etc.)
 * TODO [Alan Saji]   – Add database/config-related constants as needed
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

    // Prevent instantiation
    private AppConstants() {}
}

