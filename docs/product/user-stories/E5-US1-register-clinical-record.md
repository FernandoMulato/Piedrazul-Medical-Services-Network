# E5-US1 — Register Clinical Record

| Field | Value |
|-------|-------|
| **ID** | E5-US1 |
| **Epic** | E5 — Manage Clinical Records |
| **Role** | As a physician or therapist |
| **Feature** | I want to register a clinical record |
| **Reason** | To store the patient's diagnosis and treatment |

---

## Acceptance Criteria

### Scenario 1: Successful Registration

| Field | Value |
|-------|-------|
| **Given** | The physician or therapist is authenticated and the patient exists |
| **When** | They fill in all required fields and save the information |
| **Then** | The system registers the clinical record correctly and confirms the save |

### Scenario 2: Empty Required Fields

| Field | Value |
|-------|-------|
| **Given** | Required data is missing |
| **When** | The physician or therapist attempts to save |
| **Then** | The system displays an error message indicating the missing fields |

### Scenario 3: Non-existent Patient

| Field | Value |
|-------|-------|
| **Given** | The patient is not registered |
| **When** | The physician or therapist attempts to associate the clinical record |
| **Then** | The system prevents registration and notifies that the patient does not exist |
