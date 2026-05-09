# E4-US3 — Export Appointment Information

| Field | Value |
|-------|-------|
| **ID** | E4-US3 |
| **Epic** | E4 — Manual Appointment Scheduling |
| **Role** | As a physician or scheduler |
| **Feature** | I want to export appointments |
| **Reason** | To print a daily list |

---

## Acceptance Criteria

### Scenario 1: Successful Export

| Field | Value |
|-------|-------|
| **Given** | Appointments exist |
| **When** | The physician or scheduler selects a date and physician |
| **Then** | The system generates a CSV file with columns: date, time, patient, type of attention |
