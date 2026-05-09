# ADR-006: PostgreSQL as Database

## Status
Proposed

## Date
2026-04-30

## Context
We are building a microservices architecture (ADR-001). Each service needs its own database. After initial consideration, we need to select a relational database that supports ACID compliance, complex queries, and is suitable for enterprise applications.

## Decision
**PostgreSQL** will be used as the primary database for all microservices.

## Rationale

### 1. ACID Compliance
- Full transactional support (BEGIN, COMMIT, ROLLBACK)
- Ensures data integrity across microservices
- Critical for healthcare data (appointments, clinical records)

### 2. Rich Feature Set
- Complex queries and aggregations
- JSON support (for flexible data structures)
- Full-text search
- Geographic data support (PostGIS)
- Window functions for reporting

### 3. Enterprise Ready
- Proven reliability and stability
- Strong community and commercial support
- Excellent documentation
- Horizontal scaling with read replicas

### 4. Open Source
- No licensing costs
- Wide availability in cloud providers (AWS RDS, GCP Cloud SQL, Azure DB)

### 5. JDBC Support
- Excellent Java integration via PostgreSQL JDBC driver
- Works seamlessly with Spring Data JPA

## Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **MySQL** | Popular, easy setup | Less feature-rich, Oracle-owned |
| **PostgreSQL** (chosen) | Feature-rich, ACID, open source | Higher resource usage |
| **MongoDB** | Flexible schema, easy scaling | No ACID (by default), different paradigm |

## Database per Service Pattern

Following ADR-001, each microservice will have its own database:

| Service | Database | Port |
|---------|----------|------|
| users-service | users_db | 5432 |
| appointments-service | appointments_db | 5432 |
| professionals-service | professionals_db | 5432 |
| clinical-records-service | clinical_records_db | 5432 |
| reports-service | reports_db | 5432 |
| audits-service | audits_db | 5432 |

> API Gateway does not have its own database.

## Schema Design

Each service follows:

```
service-name/src/main/resources/schema.sql
```

Example for users-service:

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
```

## Connection Configuration

Each service uses Spring Data JPA with PostgreSQL:

```yaml
# application.yml per service
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/service_db
    username: medical_user
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

## Security

- Role-based access control (RBAC) at database level
- SSL/TLS connections for production
- Separate users per service (principle of least privilege)
- Passwords stored in environment variables

## Backup Strategy

- Daily automated backups via pg_dump
- Point-in-time recovery (PITR) enabled
- Backup rotation (30 days retention)
- Off-site backup storage

## Consequences

### Positive
- ACID compliance ensures data integrity
- Rich query capabilities for reporting
- Excellent Spring Boot integration
- Strong consistency across services
- Scalable with read replicas

### Negative
- Requires running PostgreSQL server
- More complex than embedded databases (initial setup)
- Vertical scaling focus (horizontal needs more work)

### Mitigation
- Use Docker for local development
- Cloud-managed PostgreSQL for production
- Document migration scripts carefully

## Applies To
- `services/**/src/main/resources/schema.sql` — all service schemas
- `services/**/pom.xml` — dependencies

## Out of Scope
- Replication configuration (covered in operations)
- Connection pooling tuning
- Backup automation scripts

## Related
- ADR-001: Microservices Architecture
- ADR-002: Java as Primary Language
- ADR-005: Spring Boot for Microservices