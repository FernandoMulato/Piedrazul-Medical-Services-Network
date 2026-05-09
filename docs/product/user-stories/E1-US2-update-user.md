# E1-US2 — Update User

| Field | Value |
|-------|-------|
| **ID** | E1-US2 |
| **Epic** | E1 — Manage Users |
| **Role** | As an administrator |
| **Feature** | I want to update user information |
| **Reason** | To keep correct data in the system |

---

## Acceptance Criteria

### Scenario 1: Successful Update

| Field | Value |
|-------|-------|
| **Given** | The user exists |
| **When** | The administrator modifies valid data |
| **Then** | The system must save the changes and register an audit event |

### Scenario 2: Update Denied Due to Invalid Data

| Field | Value |
|-------|-------|
| **Given** | Inconsistent or already-existing data is entered in the update module |
| **When** | The administrator attempts to modify data |
| **Then** | The system must inform that the update has been denied and the entered data is inconsistent |

### Scenario 3: Role Change to Medical Without Professional Association

| Field | Value |
|-------|-------|
| **Given** | No professional is associated |
| **When** | The administrator changes the role to medical |
| **Then** | The system must block the update and display a message indicating that a professional must be associated |
