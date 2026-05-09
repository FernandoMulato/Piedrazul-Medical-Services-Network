# E2-US1 — User Login by Role

| Field | Value |
|-------|-------|
| **ID** | E2-US1 |
| **Epic** | E2 — Manage Authentication & Access Control |
| **Role** | As a user |
| **Feature** | I want to log in |
| **Reason** | To access my authorized functionalities |

---

## Acceptance Criteria

### Scenario 1: Successful Login

| Field | Value |
|-------|-------|
| **Given** | Username and password are correct |
| **When** | The user presses login |
| **Then** | The system validates credentials, creates a session, and redirects according to role |

### Scenario 2: Incorrect Username

| Field | Value |
|-------|-------|
| **Given** | The username is incorrect |
| **When** | The user presses login after entering the wrong username |
| **Then** | The system blocks access and displays the message: "Incorrect username" |

### Scenario 3: Incorrect Password

| Field | Value |
|-------|-------|
| **Given** | The password is incorrect |
| **When** | The user presses login after entering the wrong password |
| **Then** | The system increments the attempt counter (on the 4th attempt it displays: "You have one last attempt, otherwise login will be blocked"; on 5 attempts the form fields are blocked. The user must recover their password by clicking: "Recover Password") and displays a generic message (does not indicate which field is wrong) |

### Scenario 4: Inactive User

| Field | Value |
|-------|-------|
| **Given** | The user is inactive |
| **When** | The user presses login while inactive |
| **Then** | The system blocks access and displays the message: "Inactive user" |
