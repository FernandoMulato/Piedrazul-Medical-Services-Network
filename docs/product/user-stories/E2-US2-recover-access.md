# E2-US2 — Recover Access Credentials

| Field | Value |
|-------|-------|
| **ID** | E2-US2 |
| **Epic** | E2 — Manage Authentication & Access Control |
| **Role** | As a user |
| **Feature** | I want to recover access |
| **Reason** | To reset my password |

---

## Acceptance Criteria

### Scenario 1: Valid Request

| Field | Value |
|-------|-------|
| **Given** | The email is registered |
| **When** | The user requests recovery |
| **Then** | The system generates a temporary token and allows a password change |

### Scenario 2: Non-existent User

| Field | Value |
|-------|-------|
| **Given** | The user does not exist |
| **When** | The user requests recovery |
| **Then** | The system does not reveal whether the user exists or not and displays a message |
