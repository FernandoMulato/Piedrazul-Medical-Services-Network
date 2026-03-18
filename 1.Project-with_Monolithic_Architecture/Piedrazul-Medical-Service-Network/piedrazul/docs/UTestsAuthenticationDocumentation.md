# Unit Tests Documentation (opVerifyUser)

## Overview
This project includes unit tests for user authentication focused on the `opVerifyUser` operation in:

1. `com.piedrazul.Application.services.impl.ClsUserServiceImpl`
2. `com.piedrazul.Infrastructure.repository.impl.ClsSQLiteUserRepository`

The tests are written with:
- JUnit 5
- Mockito
- AAA pattern (Arrange / Act / Assert)
- Clear, descriptive test method names prefixed with `test...`
- Test classes prefixed with `UTest...`

## Test Run
Command used:

```sh
mvn test
```

Result:
- All unit tests executed successfully (`mvn test` exit code 0).

## Where the Tests Are
- `src/test/java/com/piedrazul/Application/services/impl/UTestClsUserServiceImpl.java`
- `src/test/java/com/piedrazul/Infrastructure/repository/impl/UTestClsSQLiteUserRepository.java`

## What Was Tested

### 1) `UTestClsUserServiceImpl`
Location: `src/test/java/com/piedrazul/Application/services/impl/UTestClsUserServiceImpl.java`

Dependency mocking:
- Mocks `com.piedrazul.Infrastructure.repository.IUserRepository`

Coverage:
- Normal: returns the expected `Role` when the repository returns a role for valid credentials.
- Borderline: returns `null` when the repository returns `null`.
- Error: verifies that a `RuntimeException` thrown by the repository is propagated.

Mockito verification:
- Confirms the service delegates to `userRepositoryMock.opVerifyUser(username, password)`.

### 2) `UTestClsSQLiteUserRepository`
Location: `src/test/java/com/piedrazul/Infrastructure/repository/impl/UTestClsSQLiteUserRepository.java`

Dependency mocking:
- Mocks JDBC abstractions:
  - `com.piedrazul.Infrastructure.config.IDatabaseConnection`
  - `java.sql.Connection`
  - `java.sql.PreparedStatement`
  - `java.sql.ResultSet`

Coverage:
- Normal (ACTIVE users mapped by `ROLE_TYPE`):
  - `ADMINISTRATOR` -> `Role.Administrator`
  - `PATIENT` -> `Role.Patient`
  - `CLINICALSTAFF` -> `Role.ClinicalStaff`
  - `APPOINTMENTMANAGER` -> `Role.AppointmentManager`
- Borderline:
  - Unknown `ROLE_TYPE` with `STATE_TYPE = ACTIVE` returns `null`
  - Empty `username` and `password` are accepted if the mocked query returns a row
- Error:
  - `ResultSet.next()` is `false` -> throws `RuntimeException` with message:
    - `Usuario o contraseña incorrectos. El usuario no existe en la base de datos.`
  - `STATE_TYPE != ACTIVE` -> throws `RuntimeException` with message:
    - `Acceso denegado - Usuario BLOQUEADO`
  - `IDatabaseConnection.connect()` throws `SQLException` -> wraps into `RuntimeException` with message:
    - `Error verificando el usuario. 500`
  - `PreparedStatement.executeQuery()` throws `SQLException` -> wraps into `RuntimeException` with message:
    - `Error verificando el usuario. 500`

Mockito verification:
- Ensures the repository binds credentials to the prepared statement:
  - `setString(1, username)`
  - `setString(2, password)`

