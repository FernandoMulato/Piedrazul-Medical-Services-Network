# E3-US1 — Request Appointment

| Field | Value |
|-------|-------|
| **ID** | E3-US1 |
| **Epic** | E3 — Autonomous Appointment Scheduling |
| **Role** | As a patient |
| **Feature** | I want to schedule an appointment |
| **Reason** | To receive medical attention |

---

## Acceptance Criteria

### Scenario 1: Successful Scheduling

| Field | Value |
|-------|-------|
| **Given** | There is availability |
| **When** | The patient selects a valid time slot |
| **Then** | The system verifies availability, creates the appointment, blocks the time slot, and displays confirmation |

### Scenario 2: Concurrency Collision

| Field | Value |
|-------|-------|
| **Given** | Two users attempt to reserve the same time slot |
| **When** | The second user confirms |
| **Then** | The system detects the conflict, rejects the operation, and displays the message: "Time slot not available" |

### Scenario 3: Invalid Date

| Field | Value |
|-------|-------|
| **Given** | Available dates are displayed |
| **When** | The patient starts the scheduling process |
| **Then** | The system blocks past dates |
