# E6-US1 — Generate Transfer Report

| Field | Value |
|-------|-------|
| **ID** | E6-US1 |
| **Epic** | E6 — Manage Audits |
| **Role** | As an administrator |
| **Feature** | I want to generate a patient transfer report |
| **Reason** | To control movements between areas or medical centers |

---

## Acceptance Criteria

### Scenario 1: Successful Generation

| Field | Value |
|-------|-------|
| **Given** | There are registered transfers |
| **When** | The administrator selects a date range |
| **Then** | The system generates the report with the corresponding data |

### Scenario 2: No Records

| Field | Value |
|-------|-------|
| **Given** | There are no transfers in the selected range |
| **When** | The report is generated |
| **Then** | The system displays a message indicating that no data is available |

### Scenario 3: Export

| Field | Value |
|-------|-------|
| **Given** | The report is generated |
| **When** | The administrator selects the export option |
| **Then** | The system downloads the report in PDF or Excel format |
