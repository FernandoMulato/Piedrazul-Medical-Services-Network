# E5-US3 — Update Clinical Record

| Field | Value |
|-------|-------|
| **ID** | E5-US3 |
| **Epic** | E5 — Manage Clinical Records |
| **Role** | As a physician or therapist |
| **Feature** | I want to update clinical record information |
| **Reason** | To correct or supplement medical data |

---

## Acceptance Criteria

### Scenario 1: Successful Update

| Field | Value |
|-------|-------|
| **Given** | The clinical record exists |
| **When** | The physician or therapist modifies the data and saves changes |
| **Then** | The system updates the information correctly |

### Scenario 2: Unauthorized Access

| Field | Value |
|-------|-------|
| **Given** | The user is not a physician |
| **When** | They attempt to modify the clinical record |
| **Then** | The system denies access |
