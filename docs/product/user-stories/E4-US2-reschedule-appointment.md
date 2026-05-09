# E4-US2 — Reschedule Appointment

| Field | Value |
|-------|-------|
| **ID** | E4-US2 |
| **Epic** | E4 — Manual Appointment Scheduling |
| **Role** | As a physician or scheduler |
| **Feature** | I want to reschedule an appointment |
| **Reason** | To change the time slot |

---

## Acceptance Criteria

### Scenario 1: Successful Rescheduling

| Field | Value |
|-------|-------|
| **Given** | There is availability |
| **When** | The physician or scheduler selects a valid time slot |
| **Then** | The system validates the new time slot, updates the appointment, and registers the change in audit |
