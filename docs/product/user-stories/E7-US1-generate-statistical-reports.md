# E7-US1 — Generate Statistical Reports

| Field | Value |
|-------|-------|
| **ID** | E7-US1 |
| **Epic** | E7 — Manage Reports & Statistics |
| **Role** | As an administrator |
| **Feature** | I want to generate statistical reports |
| **Reason** | To analyze system behavior and medical care |

---

## Acceptance Criteria

### Scenario 1: Report by Date Range

| Field | Value |
|-------|-------|
| **Given** | There are registered appointments |
| **When** | The administrator selects a date range |
| **Then** | The system displays statistics for the number of appointments, cancellations, and attendances |

### Scenario 2: Report by Specialty

| Field | Value |
|-------|-------|
| **Given** | There are physicians with registered specialties |
| **When** | The administrator filters by specialty |
| **Then** | The system displays statistics associated with that specialty |

### Scenario 3: Graphical Visualization

| Field | Value |
|-------|-------|
| **Given** | The report is generated |
| **When** | The system processes the information |
| **Then** | The system displays statistical charts (bar or pie) |
