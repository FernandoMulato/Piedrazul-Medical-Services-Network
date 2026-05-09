# E8-US1 — Create Professional

| Field | Value |
|-------|-------|
| **ID** | E8-US1 |
| **Epic** | E8 — Manage Physicians & Therapists |
| **Role** | As an administrator |
| **Feature** | I want to register a physician or therapist |
| **Reason** | So they can be assigned to medical appointments |

---

## Acceptance Criteria

### Scenario 1: Successful Registration (Required Fields)

| Field | Value |
|-------|-------|
| **Given** | The administrator is authenticated |
| **When** | They fill in all required fields |
| **Then** | The system creates the professional in active state |

### Scenario 2: Invalid Registration (Missing Required Fields)

| Field | Value |
|-------|-------|
| **Given** | The administrator is authenticated |
| **When** | They do not fill in all required fields validly |
| **Then** | The system must prevent professional registration with empty or inconsistent required data |

### Scenario 3: Invalid Interval

| Field | Value |
|-------|-------|
| **Given** | The appointment interval is less than or equal to zero |
| **When** | The administrator fills in the appointment interval field |
| **Then** | The system blocks the registration and displays "There is an active appointment in this time interval" |
