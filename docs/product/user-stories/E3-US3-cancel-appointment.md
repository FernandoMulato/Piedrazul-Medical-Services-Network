# E3-US3 — Cancel Appointment

| Field | Value |
|-------|-------|
| **ID** | E3-US3 |
| **Epic** | E3 — Autonomous Appointment Scheduling |
| **Role** | As a patient |
| **Feature** | I want to cancel an appointment |
| **Reason** | To free up the time slot |

---

## Acceptance Criteria

### Scenario 1: Successful Cancellation

| Field | Value |
|-------|-------|
| **Given** | There are scheduled appointments |
| **When** | The patient presses "Cancel appointment" |
| **Then** | The system changes the status to "Cancelled", frees the time slot, and registers an audit entry |
