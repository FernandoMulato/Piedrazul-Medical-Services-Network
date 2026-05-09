# E8-US3 — Configure Attendance Schedules

| Field | Value |
|-------|-------|
| **ID** | E8-US3 |
| **Epic** | E8 — Manage Physicians & Therapists |
| **Role** | As an administrator |
| **Feature** | I want to configure attendance days and time slots |
| **Reason** | So the system calculates availability correctly |

---

## Acceptance Criteria

### Scenario 1: Validation Scenario

| Field | Value |
|-------|-------|
| **Given** | The administrator is authenticated |
| **When** | An attempt is made to create a time slot that overlaps with another |
| **Then** | The system must prevent the overlap or require prior rescheduling |
