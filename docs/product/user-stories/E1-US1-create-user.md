# E1-US1 — Create User

| Field | Value |
|-------|-------|
| **ID** | E1-US1 |
| **Epic** | E1 — Manage Users |
| **Role** | As an administrator |
| **Feature** | I want to create a user in the system |
| **Reason** | So that they can access the system according to their assigned role |

---

## Acceptance Criteria

### Scenario 1: Successful Registration (Required Fields)

| Field | Value |
|-------|-------|
| **Given** | The administrator is authenticated and fills in all required fields correctly |
| **When** | A new user is registered |
| **Then** | The system must validate that the login does not exist, encrypt the password, create the user in active state, and display a confirmation message |

### Scenario 2: Invalid Registration (Missing Required Fields)

| Field | Value |
|-------|-------|
| **Given** | The administrator is authenticated and does NOT fill in all required fields correctly |
| **When** | A new user is registered |
| **Then** | The system must prevent user registration with empty required fields (`p_error_registro_obligatorios`) |

### Scenario 3: Duplicate Login

| Field | Value |
|-------|-------|
| **Given** | A user with the same login already exists |
| **When** | The administrator attempts to save it |
| **Then** | The system must not create the user and display the message: "The username already exists" |

### Scenario 4: Medical Role Without Professional Association

| Field | Value |
|-------|-------|
| **Given** | The selected role is Physician/Therapist and no existing professional is associated |
| **When** | An attempt is made to save |
| **Then** | The system must block the registration and display a message indicating that a professional must be associated |

### Scenario 5: Invalid Password

| Field | Value |
|-------|-------|
| **Given** | The password does not meet the minimum policy (e.g., 8 characters) |
| **When** | An attempt is made to save |
| **Then** | The system must reject the registration and display the message: "The password does not meet the minimum policy" |
