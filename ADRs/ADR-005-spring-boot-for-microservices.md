# ADR-005: Spring Boot for Microservices

## Status
Proposed

## Date
2026-04-30

## Context
We are adopting a microservices architecture (ADR-001). Each service needs a robust framework for creating REST APIs, managing dependencies, and handling cross-cutting concerns (security, logging, configuration).

## Decision
**Spring Boot** will be used as the primary framework for all microservices.

## Rationale

### 1. Ecosystem Maturity
- Extensive community and enterprise support
- Rich documentation and examples
- Large ecosystem of libraries (Spring Data, Spring Security, Spring Cloud)

### 2. Microservices Support
- Embedded server (Tomcat, Jetty) - no need for external container
- Auto-configuration reduces boilerplate
- Spring Cloud provides patterns for service discovery, circuit breakers, config server

### 3. Java Language Integration
- Native Java 21 support
- Seamless integration with existing Java codebase
- Strong typing and IDE support

### 4. Operational Benefits
- Health check endpoints (`/actuator/health`)
- Metrics collection (`/actuator/metrics`)
- Easy externalized configuration via `application.yml`
- Built-in support for REST documentation with OpenAPI

## Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Quarkus** | Faster startup, lower memory | Less mature ecosystem |
| **Micronaut** | GraalVM native support | Smaller community |
| **Spring Boot** (chosen) | Mature, extensive docs, cloud native | Heavier memory footprint |
| **Plain Java + Jetty** | Full control | More boilerplate |

## Architecture with Spring Boot

```
services/
├── api-gateway/           → Spring Cloud Gateway
├── users-service/         → Spring Boot + Spring Data
├── appointments-service/  → Spring Boot + Spring Data
├── professionals-service/ → Spring Boot + Spring Data
├── clinical-records-service/ → Spring Boot + Spring Data
├── reports-service/       → Spring Boot + Spring Data
└── audits-service/       → Spring Boot + Spring Data
```

### Key Spring Dependencies

```xml
<!-- Each service will include -->
<dependencies>
    <!-- Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Data (PostgreSQL) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>

    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Actuator (health checks) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Configuration Convention

Each service follows this structure:

```
service-name/
├── src/main/java/com/medical/
│   ├── ServiceNameApplication.java  ← Main class
│   ├── entity/                     ← JPA entities
│   ├── repository/                 ← Spring Data repositories
│   ├── service/                    ← Business logic
│   ├── controller/                 ← REST endpoints
│   ├── dto/                        ← Request/Response DTOs
│   ├── config/                     ← Configuration classes
│   └── exception/                  ← Exception handlers
├── src/main/resources/
│   └── application.yml             ← Service configuration
└── pom.xml
```

## Port Assignment

| Service | Port | Context Path |
|---------|------|--------------|
| api-gateway | 8080 | / |
| users-service | 8081 | /api/users |
| appointments-service | 8082 | /api/appointments |
| professionals-service | 8083 | /api/professionals |
| clinical-records-service | 8084 | /api/clinical-records |
| reports-service | 8085 | /api/reports |
| audits-service | 8086 | /api/audits |

## Consequences

### Positive
- Rapid development with auto-configuration
- Consistent patterns across all services
- Easy integration with Spring Cloud for service discovery
- Built-in health monitoring
- Strong testing support (MockMvc, TestContainers)

### Negative
- Higher memory footprint compared to Quarkus/Micronaut
- Longer startup time
- More complex configuration for small teams

### Mitigation
- Use Spring Boot's lazy initialization
- Profile-based configuration for different environments
- Start with basic services, add Spring Cloud later

## Applies To
- `services/**` — all microservices
- Each service must have its own `ServiceNameApplication.java`

## Out of Scope
- Spring Cloud configuration (can be added later)
- Kubernetes deployment details
- Service mesh implementation

## Related
- ADR-001: Microservices Architecture
- ADR-002: Java as Primary Language
- ADR-003: Design Patterns
- ADR-004: SOLID Principles
- ADR-006: PostgreSQL as Database