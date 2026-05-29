package com.medical.client.config;

/**
 * Centralized API configuration for microservice endpoints.
 * All base URLs for backend services are defined here.
 *
 * @author Medical Services Network Team
 */
public final class ApiConfig {

    private ApiConfig() {
        // Utility class
    }

    /** users-service base URL (port 8081) */
    public static final String USERS_BASE_URL = "http://localhost:8081/api/users";

    /** appointments-service base URL (port 8082) */
    public static final String APPOINTMENTS_BASE_URL = "http://localhost:8082/api/appointments";

    /** clinical-records-service base URL (port 8084) */
    public static final String CLINICAL_RECORDS_BASE_URL = "http://localhost:8084/api/v1/clinical-records";

    /** users-service health endpoint */
    public static final String USERS_HEALTH_URL = "http://localhost:8081/actuator/health";

    /** appointments-service health endpoint */
    public static final String APPOINTMENTS_HEALTH_URL = "http://localhost:8082/actuator/health";

    /** clinical-records-service health endpoint */
    public static final String CLINICAL_RECORDS_HEALTH_URL = "http://localhost:8084/actuator/health";
}
