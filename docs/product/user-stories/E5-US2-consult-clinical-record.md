# E5-US2 — Consult Clinical Record

| Field | Value |
|-------|-------|
| **ID** | E5-US2 |
| **Epic** | E5 — Manage Clinical Records |
| **Role** | As a physician or therapist |
| **Feature** | I want to consult a patient's clinical record |
| **Reason** | To review previous history and diagnoses |

---

## Acceptance Criteria

### Scenario 1: Successful Consultation

| Field | Value |
|-------|-------|
| **Given** | The patient has a registered history |
| **When** | The physician or therapist searches by identification |
| **Then** | The system displays all the patient's historical information |

### Scenario 2: No History

| Field | Value |
|-------|-------|
| **Given** | The patient has no previous records |
| **When** | The physician performs the consultation |
| **Then** | The system informs that no clinical records are registered |
