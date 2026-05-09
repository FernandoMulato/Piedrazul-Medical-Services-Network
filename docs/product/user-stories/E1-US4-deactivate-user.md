# E1-US4 — Deactivate User

| Field | Value |
|-------|-------|
| **ID** | E1-US4 |
| **Epic** | E1 — Manage Users |
| **Role** | As an administrator |
| **Feature** | I want to deactivate users |
| **Reason** | To prevent a user from accessing the system without deleting their information |

---

## Acceptance Criteria

### Scenario 1: Successful Deactivation

| Field | Value |
|-------|-------|
| **Given** | The administrator is authenticated, the target user exists and is in ACTIVE state, and at least one other active administrator exists in the system |
| **When** | The administrator selects the "Deactivate user" option |
| **Then** | The system changes the user status to INACTIVE, prevents future system access, registers the action in audit, and displays a confirmation message |

### Scenario 2: Attempt to Deactivate the Last Administrator

| Field | Value |
|-------|-------|
| **Given** | The target user is the last active administrator |
| **When** | An attempt is made to deactivate them |
| **Then** | The system blocks the operation, maintains the ACTIVE status, and displays the message: "It is not possible to deactivate the last administrator of the system" |

### Scenario 3: Non-existent User

| Field | Value |
|-------|-------|
| **Given** | The user does not exist in the system |
| **When** | The administrator attempts to deactivate them |
| **Then** | The system takes no action and displays an error message indicating that the user does not exist |

### Scenario 4: User Already Inactive

| Field | Value |
|-------|-------|
| **Given** | The user is already in INACTIVE state |
| **When** | The administrator attempts to deactivate them again |
| **Then** | The system makes no changes and displays an informational message: "The user is already inactive" |

### Scenario 5: Login Attempt with Deactivated User

| Field | Value |
|-------|-------|
| **Given** | The user is in INACTIVE state |
| **When** | They attempt to log in |
| **Then** | The system rejects access, displays the message: "Inactive user", and does not create a session |
