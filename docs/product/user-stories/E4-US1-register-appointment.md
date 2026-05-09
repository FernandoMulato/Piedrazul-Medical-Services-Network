# E4-US1 — Register Appointment

| Field | Value |
|-------|-------|
| **ID** | E4-US1 |
| **Epic** | E4 — Manual Appointment Scheduling |
| **Role** | As a physician or scheduler |
| **Feature** | I want to manually register an appointment |
| **Reason** | To assign phone-based appointments |

---

## Acceptance Criteria

### Scenario 1: Successful Scheduling

| Field | Value |
|-------|-------|
| **Given** | There is availability and all required data is entered |
| **When** | The physician or user selects a valid time slot |
| **Then** | The system validates: active professional, no collision, valid interval, and creates the appointment |
