# E8-US5 — Change Professional Status

| Field | Value |
|-------|-------|
| **ID** | E8-US5 |
| **Epic** | E8 — Manage Physicians & Therapists |
| **Role** | As an administrator |
| **Feature** | I want to activate or deactivate a professional |
| **Reason** | To control their availability in the system |

---

## Acceptance Criteria

### Scenario 1: Valid Deactivation

| Field | Value |
|-------|-------|
| **Given** | The physician or therapist has no future appointments |
| **When** | An attempt is made to deactivate the physician or therapist |
| **Then** | The system changes the status to INACTIVE |

### Scenario 2: Invalid Deactivation

| Field | Value |
|-------|-------|
| **Given** | The physician or therapist has future appointments |
| **When** | An attempt is made to deactivate the physician or therapist |
| **Then** | The system blocks the operation and displays a clear message |
