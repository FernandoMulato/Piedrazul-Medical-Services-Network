# ADR-001: Microservices Architecture

## Status
Accepted

## Date
2026-04-30

## Context
The client needs a scalable and maintainable architecture for the Medical Services Network application. After initial setup, the team is ready to adopt more advanced architectural patterns that will allow independent deployment and scaling of different services.

## Decision
A Microservices Architecture will be adopted, with each domain (Users, Appointments, Professionals, Clinical Records, Reports) as an independent service with its own database schema.

## Rationale
- Independent scalability of services based on load (e.g., Appointments may need more resources than Reports)
- Independent deployment - each service can be deployed without affecting others
- Technology flexibility - each service can use different technologies if needed
- Fault isolation - failure in one service doesn't cascade to others
- Team autonomy - different teams can work on different services

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway                              │
│                   (api-gateway:8080)                            │
└─────────────────────────────────────────────────────────────────┘
                               │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│   users       │    │ appointments  │    │ professionals │
│  service      │    │   service     │    │    service    │
│  (8081)       │    │   (8082)      │    │   (8083)      │
└───────────────┘    └───────────────┘    └───────────────┘
        │                     │                     │
        ▼                     ▼                     ▼
   ┌─────────┐          ┌─────────┐           ┌─────────┐
   │users.db │          │appoint. │           │ prof.  │
   └─────────┘          │  .db    │           │  .db   │
                        └─────────┘           └─────────┘

        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│clinical-records│   │    reports     │    │   audits      │
│   service     │    │   service     │    │    service    │
│   (8084)      │    │   (8085)      │    │   (8086)      │
└───────────────┘    └───────────────┘    └───────────────┘
        │                     │                     │
        ▼                     ▼                     ▼
   ┌─────────┐          ┌─────────┐           ┌─────────┐
   │ clinical │          │ reports │           │ audits  │
   │  .db    │          │  .db    │           │  .db    │
   └─────────┘          └─────────┘           └─────────┘
```

## Microservices

| Service | Port | Database | Responsibility |
|---------|------|----------|----------------|
| api-gateway | 8080 | - | Request routing, authentication |
| users | 8081 | users.db | User management, authentication |
| appointments | 8082 | appointments.db | Appointment scheduling |
| professionals | 8083 | professionals.db | Professional management |
| clinical-records | 8084 | clinical_records.db | Patient clinical records |
| reports | 8085 | reports.db | Statistical reports generation |
| audits | 8086 | audits.db | Audit trail and logs |

## Communication

- **Synchronous**: REST APIs between services
- **Asynchronous**: Message queue (RabbitMQ) for event-driven updates
- **Service Discovery**: Consul or Eureka for service registration

## Alternatives Considered

- **Monolith**: Good for initial delivery, but harder to scale independently
- **Serverless**: Lower operational cost but less control over infrastructure

## Consequences

### Positive
- Independent scaling of each domain
- Fault isolation and resilience
- Technology flexibility per service
- Independent deployments
- Team autonomy

### Negative
- Higher operational complexity
- Distributed transactions challenges
- Data consistency across services
- Increased network latency
- More complex testing (integration across services)

### Mitigation
- Use saga pattern for distributed transactions
- Implement circuit breaker pattern
- Use correlation IDs for distributed tracing
- Establish clear API contracts between services

## Applies To
- `services/**` — all microservices
- `api-gateway/**` — gateway service
- `shared/**` — shared libraries and utilities

## Out of Scope
- Specific container orchestration (Kubernetes)
- Message queue implementation details
- CI/CD pipeline configuration

## Related
- ADR-002: Java as Primary Language
- ADR-003: Design Patterns
- ADR-004: SOLID Principles
- ADR-005: Spring Boot for Microservices
- ADR-006: PostgreSQL as Database
- ADR-007: JavaFX for Desktop UI