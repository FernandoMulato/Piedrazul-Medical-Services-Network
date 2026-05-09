# ADR-007: JavaFX for Desktop UI

## Status
Proposed

##Date
2026-04-30

## Context
We need a desktop UI framework for the Medical Services Network application. JavaFX provides a modern, rich client platform. Note: Web-based UI may be considered in the future.

## Decision
**JavaFX** will be used as the desktop UI framework for this application.

## Rationale

### 1. Modern Desktop Framework
- Rich UI controls (charts, tables, trees)
- FXML for declarative UI design
- CSS styling support
- Built-in animation framework

### 2. Java Integration
- Native Java 21 support
- Seamless integration with Spring Boot backend
- Strong typing across all layers
- Maven/Gradle build support

### 3. MVC Architecture
- Clear separation: Model (domain), View (FXML), Controller (Java)
- FXML allows designers to work on UI separately
- Controllers are regular Java classes (testable)

### 4. Future Web Migration
- Business logic in services (not in UI)
- Clean API contract between frontend and backend
- Easy to replace JavaFX with React/Vue/Angular later
- Services are already web-ready (REST)

### 5. Deployment Options
- Native packaging (jpackage)
- JAR with dependencies
- Docker container

## Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **JavaFX** (chosen) | Modern, FXML, CSS | Requires Java 8+ (OpenJFX) |
| **Electron** | Web technologies | Requires Node.js, heavier |
| **Qt (C++)** | Performance | Different language, expensive |
| **Web (future)** | Ubiquitous | Requires browser |

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                 JavaFX Client                       │
│   ┌─────────────┐  ┌─────────────┐  ┌───────────┐  │
│   │   Views     │  │ Controllers │  │  Services│  │
│   │  (FXML)     │  │   (Java)    │  │  (REST)  │  │
│   └─────────────┘  └─────────────┘  └───────────┘  │
│         │                │              │         │
│         └────────────────┼──────────────┘         │
│                          ▼                         │
│               ┌─────────────────┐                  │
│               │  HTTP Client    │                  │
│               │  (RestClient)  │                  │
│               └─────────────────┘                  │
└───────────────────────┬─────────────────────────────┘
                        │ REST API
                        ▼
┌─────────────────────────────────────────────────────┐
│              Microservices (ADR-001)                │
│  users-service, appointments-service, etc.         │
└─────────────────────────────────────────────────────┘
```

## Project Structure

```
medical-client/  (JavaFX application)
├── src/main/java/com/medical/
│   ├── Main.java                    ← Application entry
│   ├── MedicalApplication.java      ← JavaFX Application
│   ├── config/
│   │   └── ApiConfig.java          ← REST endpoint config
│   ├── controller/
│   │   ├── LoginController.java
│   │   ├── DashboardController.java
│   │   └── appointments/
│   ├── service/
│   │   ├── AppointmentService.java  ← REST client
│   │   └── UserService.java
│   ├── model/
│   │   └── (shared DTOs from medical-common)
│   └── util/
│       └── SessionManager.java
├── src/main/resources/
│   ├── fxml/
│   │   ├── login.fxml
│   │   ├── dashboard.fxml
│   │   └── appointments/
│   ├── css/
│   │   └── styles.css
│   └── application.properties
└── pom.xml
```

## UI/Service Communication

All communication goes through REST APIs:

```java
// Example: AppointmentService (JavaFX client)
@Service
public class AppointmentService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8082/api/appointments";

    public List<AppointmentDto> findAll() {
        return restTemplate.getForObject(baseUrl, List.class);
    }

    public AppointmentDto create(AppointmentRequest request) {
        return restTemplate.postForObject(baseUrl, request, AppointmentDto.class);
    }
}
```

## Key Dependencies

```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.2</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.2</version>
    </dependency>

    <!-- Spring Web (REST client) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Jackson for JSON -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
</dependencies>
```

## Future Web Migration Path

Since business logic resides in microservices, migration to web is straightforward:

```
Current: JavaFX → REST → Microservices
Future:  React → REST → Microservices
```

Steps for future web migration:
1. Keep microservices unchanged (they expose REST APIs)
2. Create new web frontend (React/Vue/Angular)
3. Move JavaFX client to legacy (optional)
4. JavaFX client still works during transition

## Consequences

### Positive
- Modern UI with rich controls
- Clear MVC separation
- Easy styling with CSS
- Testable controllers
- Future web migration is straightforward

### Negative
- Separate deployment from services
- Requires JavaFX runtime
- Less ubiquity than web

### Mitigation
- Package as native executable (jpackage)
- Use Docker for easy deployment
- Keep REST APIs stable for easy frontend swap

## Applies To
- `medical-client/` — JavaFX application
- All UI development
- REST client implementation

## Out of Scope
- Specific UI component library (use built-in controls)
- Web UI implementation (future consideration)
- Mobile application

## Related
- ADR-001: Microservices Architecture
- ADR-002: Java as Primary Language
- ADR-005: Spring Boot for Microservices
- ADR-006: PostgreSQL as Database