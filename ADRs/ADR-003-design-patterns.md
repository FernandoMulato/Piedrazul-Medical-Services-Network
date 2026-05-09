# ADR-003: Design Patterns — Factory Method, Observer and Strategy

## Status
Accepted

## Date
2026-04-07 (Updated: 2026-04-09)

## Context

Medical Services Network requires a robust and extensible implementation for:

1. **Entity creation**: Centralize domain object instantiation (User, Appointment, etc.)
2. **View synchronization**: JavaFX components automatically update when state changes
3. **Flexible persistence**: Add new persistence behaviors without modifying existing repositories

References:
- Factory Method: https://refactoring.guru/design-patterns/factory-method
- Observer: https://refactoring.guru/design-patterns/observer
- Strategy: https://refactoring.guru/design-patterns/strategy

## Decision

The following design patterns are adopted:

| Pattern | Category | Purpose | SOLID |
|---------|----------|---------|-------|
| **Factory Method** | Creational | Centralize entity creation | SRP, DIP |
| **Observer** | Behavioral | Synchronize views with state | DIP, OCP |
| **Strategy** | Behavioral | Interchangeable algorithms for persistence | OCP |

### Factory Method — Project Application

```java
public interface UserFactory {
    User create(UserDTO dto);
}

public class DefaultUserFactory implements UserFactory {
    @Override
    public User create(UserDTO dto) {
        // Validation and centralized creation
    }
}
```

### Observer — Project Application

```java
public interface ClinicObserver {
    void onEvent(ClinicEvent event);
}

public class CalendarView extends JPanel implements ClinicObserver {
    @Override
    public void onEvent(ClinicEvent event) {
        refreshCalendar();
    }
}

public class ClinicEventBus {
    private static ClinicEventBus instance;
    private List<ClinicObserver> observers = new ArrayList<>();
    
    public static ClinicEventBus getInstance() { ... }
    public void subscribe(ClinicObserver observer) { ... }
    public void publish(ClinicEvent event) { 
        observers.forEach(o -> o.onEvent(event));
    }
}

public class ClinicEvent {
    private ClinicEventType eventType;
    private Object data;
    private String message;
    private LocalDateTime timestamp;
}
```

### Strategy — Project Application

Strategy pattern solves OCP in repositories:

```java
// Strategy interface
public interface PersistenceStrategy<T> {
    String getSQL();
    void setParameters(T entity, PreparedStatement stmt);
    void postExecute(T entity, PreparedStatement stmt);
}

// Concrete strategies
public class UserInsertStrategy implements PersistenceStrategy<User> { }
public class UserUpdateStrategy implements PersistenceStrategy<User> { }

// Repository uses strategies
public class UserRepository {
    private final UserPersistenceStrategy strategy;
    
    public User save(User user) {
        PersistenceStrategy<User> s = strategy.selectStrategy(user);
        // uses s.getSQL(), s.setParameters(), etc.
    }
}
```

## Rationale

### Factory Method
- **SRP**: Creation logic in one place
- **DIP**: Depends on interface, not concrete implementation
- **Testability**: Eases mocking

### Observer
- **Decoupling**: Views don't depend directly on domain
- **OCP**: New views without modifying subject
- **Runtime**: Subscribers can be added/removed

### Strategy (Applied for OCP)
- **OCP**: New persistence behaviors without modifying save()
- **DIP**: Repository depends on abstraction (PersistenceStrategy)
- **Extensibility**: To add soft delete, audit, etc. just create new strategy

## Alternatives Considered

### Factory Method
| Alternative | Why Rejected |
|-------------|--------------|
| Abstract Factory | Overly complex |
| Builder | Only for complex objects |
| Direct constructor | Couples to concrete classes |

### Observer
| Alternative | Why Rejected |
|-------------|--------------|
| Mediator | Unnecessary intermediate layer |
| Direct callback | Doesn't scale |
| Polling | Inefficient |

### Strategy
| Alternative | Why Adopted |
|-------------|-------------|
| Conditionals in save() | Violates OCP - requires modifying save() |
| Inheritance | Rigid coupling |
| **Strategy (chosen)** | Composition, extensible, testable |

## Consequences

### Positive

**Factory Method:**
- Centralized code for creation
- Easy extension for new entities
- Better testability with mocks

**Observer:**
- Decoupled views from model
- Automatic updates
- Native JavaFX listeners support

**Strategy:**
- OCP compliance: new behaviors without modifying repositories
- DIP compliance: dependency on abstractions
- Testing with strategy mocks

### Negative

**Factory Method:**
- More classes/interfaces

**Observer:**
- Memory leaks if no unsubscribe
- Non-deterministic notification order

**Strategy:**
- More files (valued vs OCP benefit)

## Applies To

### Factory Method
- `src/main/java/com/medical/factory/` — factory classes
- Usage: User, Appointment creation

### Observer
- `src/main/java/com/medical/observer/` — observer interface
- `src/main/java/com/medical/event/` — event classes and EventBus
- Usage: view synchronization (CalendarView, DashboardView, NotificationView)

### Strategy
- `src/main/java/com/medical/repository/strategy/` — persistence strategies
- Usage: save() in repositories (UserRepository, AppointmentRepository)

## Out of Scope

- Structural patterns
- Dependency injection configuration (DI)
- Testing patterns

## Related

- ADR-001: Microservices Architecture
- ADR-002: Java as Primary Language
- ADR-004: SOLID Principles (applies to all patterns)
- ADR-006: PostgreSQL as Database (uses Strategy for persistence)
- ADR-007: JavaFX for Desktop UI