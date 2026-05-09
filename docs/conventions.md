# Project Conventions

## Technology Stack
- **Language**: Java 21 (ADR-002)
- **UI Framework**: JavaFX (ADR-007)
- **Database**: PostgreSQL (ADR-006)
- **Framework**: Spring Boot (ADR-005)
- **Architecture**: Microservices (ADR-001)
- **Design Patterns**: Factory Method, Observer (ADR-003)

---

## Naming

- Packages: lowercase → com.medical.users
- Classes: PascalCase → UserService, UserServiceImpl
- Interfaces: PascalCase, no prefix → UserService (not IUserService)
- Implementations: interface name + Impl → UserServiceImpl
- Methods: camelCase → createUser()
- Variables: camelCase → userId
- Constants: UPPER_SNAKE_CASE → MAX_PAGE_SIZE
- Enums: PascalCase → UserRole, UserStatus
- Database tables: snake_case plural → users, user_roles
- Database columns: snake_case → created_at, full_name

---

## File Structure (Layered Monolith)

```
src/main/java/com/medical/
├── Main.java                       # Entry point (Java desktop app)
├── db/
│   └── DatabaseConnection.java     # SQLite connection manager
├── model/
│   ├── User.java                   # Domain entity
│   └── Appointment.java
├── enums/
│   ├── UserRole.java
│   ├── UserStatus.java
│   └── AppointmentStatus.java
├── dto/
│   ├── UserDTO.java
│   └── AppointmentDTO.java
├── repository/
│   ├── UserRepository.java         # SQLite implementation
│   └── AppointmentRepository.java
├── service/
│   ├── UserService.java            # interface
│   ├── UserServiceImpl.java
│   ├── AppointmentService.java
│   └── AppointmentServiceImpl.java
└── ui/
    ├── MainFrame.java              # Main window
    └── ...                         # Swing components
```

---

## Code Rules

- **UI Layer** (ui/*): Only handles presentation, calls services
- **Service Layer** (service/*): Contains all business logic
- **Repository Layer** (repository/*): Only layer that interacts with SQLite
- **Model/Entity**: Domain objects, no framework annotations
- Use interfaces for service layer
- Prefer constructor injection over field injection
- Avoid static state except constants
- Keep methods small and focused (Single Responsibility)
- Use Optional instead of null for nullable returns
- Validate inputs in service layer

---

## Database Rules

- **Connection**: Single instance via DatabaseConnection (Singleton)
- **Initialization**: Automatic table creation on app start
- **Transactions**: Manual management via JDBC
- **SQL Style**: Native SQL in repositories (no ORM)
- **Indexes**: Create for frequently queried columns

---

## Swing UI Rules

- All UI components must extend Swing classes (JPanel, JFrame, etc.)
- Run UI on Event Dispatch Thread (EDT) via SwingUtilities.invokeLater()
- Use layout managers (BorderLayout, GridBagLayout, etc.)
- Never block the EDT with long-running operations
- Use Model-View pattern: separate data models from UI components

---

## Error Handling

- Custom exception hierarchy:
  - BusinessException → show error dialog to user
  - NotFoundException → show "not found" message
- Never throw generic Exception or RuntimeException directly
- Catch exceptions at service layer and convert to user-friendly messages
- Error logging to console (future: file logging)

---

## Testing Rules

- Tests live in `src/test/` mirroring the structure of `src/main/`
- Mock only repository layer — never mock services
- One test class per implementation class
- Minimum per public method:
  - Happy path
  - At least one failure case
- Naming: shouldCreateUser_whenDataIsValid

---

## Git

- Commit types:
  - feat: new feature
  - fix: bug fix
  - chore: maintenance or config
  - test: tests only
  - docs: documentation only
- Branch naming:
  - feature/user-creation
  - fix/duplicate-email-validation
  - chore/update-dependencies
- Never commit:
  - *.db (SQLite database file)
  - target/
  - .idea/

---

## DTO & Mapping Rules

- Never expose entities directly in UI
- Use DTOs for all data transfer
- Mapper methods in service layer (manual mapping)
- DTOs are plain Java classes (no framework annotations)

---

## Application Startup Flow

1. Main.main() called
2. DatabaseConnection.initializeDatabase() creates tables if not exist
3. MainFrame created and set visible on EDT
4. User interacts with UI components
5. UI calls Service → Service calls Repository → Repository queries SQLite

---

## Future Considerations (Out of Scope for v1.0)

- Logging framework (SLF4J)
- Configuration file (properties/JSON)
- Maven assembly for executable JAR
- Event-driven patterns (implemented: Observer with ClinicEventBus)
- Multi-user concurrency (SQLite limitation)

---

## SOLID Principles

This project follows SOLID principles for maintainable, scalable code.

**Full documentation**: See [ADR-004](/ADRs/ADR-004-solid-principles.md)

**Quick reference**:
- **SRP**: One reason to change per class
- **OCP**: Open for extension, closed for modification
- **LSP**: Subclasses substitutable for base
- **ISP**: Small, focused interfaces
- **DIP**: Depend on abstractions

**Reference**: Use skill `solid-principles` for detailed guidance on applying these principles.