# System Overview — Medical Services Network

## Context
Centro de Salud Piedra Azul is a non-profit healthcare organization located in 
Popayán, Colombia, specializing in alternative medicine: neural therapy, 
chiropractic care, and physiotherapy.

The organization currently operates a legacy desktop application that needs 
replacement. Appointment scheduling is handled manually by medical staff via 
phone and WhatsApp, creating operational bottlenecks as patient volume grows.

---

## Problem Statement

| Problem | Impact |
|---------|--------|
| No patient self-service channel | Medical staff spend afternoons manually booking appointments |
| Manual one-by-one scheduling | High error rate, duplicate bookings, staff overload |
| Legacy desktop application | Obsolete technology, difficult to maintain |
| No formal audit trail | No traceability of clinical or administrative actions |
| No structured availability management | Double bookings, scheduling conflicts |

---

## Solution Vision
A modern desktop application for medical appointment management that enables:
- Efficient internal administration for staff and physicians
- Manual and autonomous appointment scheduling
- Full audit trail for clinical and administrative operations
- Simple deployment with local SQLite database

---

## Architecture

The application follows a **Microservices Architecture** (ADR-001):

```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway                              │
│                   (api-gateway:8080)                            │
│                 Routing & Authentication                        │
└─────────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│   users       │    │ appointments  │    │ professionals │
│  service      │    │   service     │    │    service    │
│  (8081)       │    │   (8082)      │    │   (8083)      │
│  users.db     │    │appointments.db│    │professionals.db│
└───────────────┘    └───────────────┘    └───────────────┘
        │                     │                     │
        ▼                     ▼                     ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│clinical-records│   │    reports     │    │   audits      │
│   service     │    │   service     │    │    service    │
│   (8084)      │    │   (8085)      │    │   (8086)      │
│clinical.db    │    │  reports.db   │    │   audits.db   │
└───────────────┘    └───────────────┘    └───────────────┘
```

Each service is an independent Java application with its own PostgreSQL database.

> Each architectural decision is documented in the corresponding ADR.

---

## System Actors

| Actor | Description | Access Level |
|-------|-------------|--------------|
| Patient | Books and manages own appointments | Self-service |
| Physician / Therapist | Manages schedule, records clinical notes | Clinical + scheduling |
| Scheduler | Creates and reschedules appointments manually | Scheduling only |
| Administrator | Full system control, user management, reports | Full access |

---

## Functional Scope

| ID | Feature | Epic |
|----|---------|------|
| FR-001 | User management (CRUD, roles, status) | E1 |
| FR-002 | Authentication and role-based access control | E2 |
| FR-003 | Autonomous patient appointment scheduling | E3 |
| FR-004 | Manual appointment scheduling by staff | E4 |
| FR-005 | Appointment rescheduling with change history | E4 |
| FR-006 | Appointment export to CSV | E4 |
| FR-007 | Clinical records management | E5 |
| FR-008 | Audit logging and transfer reports | E6 |
| FR-009 | Statistical reports | E7 |
| FR-010 | Professional management and scheduling | E8 |

---

## Business Rules

| ID | Rule |
|----|------|
| BR-001 | Document number and email must be unique across the system |
| BR-002 | Only active professionals can receive appointments |
| BR-003 | A professional cannot have two overlapping appointments |
| BR-004 | Access is restricted by role |
| BR-005 | Clinical records are accessible only to authorized clinical roles |
| BR-006 | All critical actions must be audit-logged with user and timestamp |
| BR-007 | Appointments require: patient, professional, date, and time |
| BR-008 | Clinical records are immutable without controlled modification history |
| BR-009 | Patient identity must be validated before scheduling |
| BR-010 | Scheduling must respect professional availability and time intervals |

---

## Non-Functional Requirements

| ID | Quality Attribute | Requirement | Acceptance Criterion |
|----|------------------|-------------|----------------------|
| NFR-001 | Offline Operation | Application works without internet | No network dependencies |
| NFR-002 | Data Persistence | PostgreSQL database | ACID compliance, data survives restart |
| NFR-003 | Ease of Deployment | Docker containers | Consistent environment, easy deployment |
| NFR-004 | Performance | Responsive UI | < 1s for typical operations |
| NFR-005 | Data Backup | Automated pg_dump | Daily backups, 30-day retention |
| NFR-006 | Usability | Intuitive UI for non-technical users | Minimal training required |

---

## Technology Stack

| Layer | Technology | Rationale |
|-------|-----------|-----------|
| Language | Java 21 | LTS, strong ecosystem, team proficiency |
| UI Framework | JavaFX | Modern desktop UI, MVC with FXML |
| Database | PostgreSQL | ACID compliance, enterprise-ready |
| Build | Maven | Standard Java build tooling |
| Testing | JUnit 5 + Mockito | Standard Java testing stack |

> Each technology choice is argued and justified in the corresponding ADR:
> - ADR-002: Java as Primary Language
> - ADR-007: JavaFX for Desktop UI
> - ADR-006: PostgreSQL as Database
> - ADR-005: Spring Boot for Microservices

---

## Repository Structure

```
Medical-Services-Network/
│
├── AGENTS.md                   ← AI agent instructions
├── README.md                   ← Project documentation
├── .gitignore
│
├── ADRs/                       ← Architecture Decision Records
│   ├── ADR-001-microservices-architecture.md
│   ├── ADR-002-java-as-primary-language.md
│   ├── ADR-003-design-patterns.md
│   ├── ADR-004-solid-principles.md
│   ├── ADR-005-spring-boot-for-microservices.md
│   ├── ADR-006-postgresql-as-database.md
│   └── ADR-007-javafx-for-desktop-ui.md
│
├── docs/
│   ├── architecture/
│   │   └── system-overview.md  ← this file
│   ├── conventions.md          ← Project conventions
│   ├── specs/
│   │   ├── system.spec.md
│   │   └── features/
│   └── modeling/
│       └── uml/
│
├── services/                  ← Microservices
│   ├── api-gateway/           ← Routing & auth (port 8080)
│   ├── users-service/         ← User management (port 8081)
│   ├── appointments-service/  ← Appointments (port 8082)
│   ├── professionals-service/ ← Professionals (port 8083)
│   ├── clinical-records-service/ ← Clinical records (port 8084)
│   ├── reports-service/       ← Reports (port 8085)
│   └── audits-service/        ← Audits (port 8086)
│
└── shared/                     ← Shared libraries
    └── medical-common/        ← Common models, DTOs, exceptions
```

---

## Out of Scope

- Web interface
- Mobile application
- Multi-user network deployment
- Cloud deployment
- Email/SMS notifications
- Real-time sync
- Docker containerization
- External API integrations

---

## Stakeholders

| Stakeholder | Role | Primary Interest |
|-------------|------|-----------------|
| Patients | End users | Easy appointment booking |
| Physicians / Therapists | Medical staff | Efficient schedule management |
| Schedulers | Administrative staff | Reduced manual workload |
| Administrators | System managers | Full visibility and control |
| Development Team | Builders | System quality, maintainability, delivery |

---

## Build & Run

### Microservices

```bash
# Build all services
cd services && mvn package

# Run each service (example)
java -jar users-service/target/users-service-1.0.0.jar
java -jar appointments-service/target/appointments-service-1.0.0.jar
```

### JavaFX Client

```bash
# Build client
cd medical-client && mvn package

# Run
java -jar target/medical-client-1.0.0.jar
```

The system requires:
1. PostgreSQL running on port 5432
2. All microservices running (ports 8081-8086)
3. JavaFX client connecting to API Gateway (8080)