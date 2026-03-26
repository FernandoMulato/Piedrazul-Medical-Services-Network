package com.piedrazul.Infrastructure.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Infrastructure.config.IDatabaseConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UTestClsSQLiteUserRepository {

  @Test
  @DisplayName("opVerifyUser returns Administrator role when user is ACTIVE ADMINISTRATOR")
  void testReturnsAdministratorWhenActiveAdministrator() throws Exception {
    // Arrange
    String username = "adminUser";
    String password = "adminPass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getString("ROLE_TYPE")).thenReturn("ADMINISTRATOR");
    when(resultSetMock.getString("STATE_TYPE")).thenReturn("ACTIVE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    Role result = repository.opVerifyUser(username, password);

    // Assert
    assertEquals(Role.ADMINISTRATOR, result);
    verify(preparedStatementMock).setString(1, username);
    verify(preparedStatementMock).setString(2, password);
  }

  @Test
  @DisplayName("opVerifyUser returns Patient role when user is ACTIVE PATIENT")
  void testReturnsPatientWhenActivePatient() throws Exception {
    // Arrange
    String username = "patientUser";
    String password = "patientPass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getString("ROLE_TYPE")).thenReturn("PATIENT");
    when(resultSetMock.getString("STATE_TYPE")).thenReturn("ACTIVE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    Role result = repository.opVerifyUser(username, password);

    // Assert
    assertEquals(Role.PATIENT, result);
    verify(preparedStatementMock).setString(1, username);
    verify(preparedStatementMock).setString(2, password);
  }

  @Test
  @DisplayName("opVerifyUser returns ClinicalStaff role when user is ACTIVE CLINICALSTAFF")
  void testReturnsClinicalStaffWhenActiveClinicalStaff() throws Exception {
    // Arrange
    String username = "staffUser";
    String password = "staffPass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getString("ROLE_TYPE")).thenReturn("CLINICALSTAFF");
    when(resultSetMock.getString("STATE_TYPE")).thenReturn("ACTIVE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    Role result = repository.opVerifyUser(username, password);

    // Assert
    assertEquals(Role.CLINICALSTAFF, result);
    verify(preparedStatementMock).setString(1, username);
    verify(preparedStatementMock).setString(2, password);
  }

  @Test
  @DisplayName("opVerifyUser returns AppointmentManager role when user is ACTIVE APPOINTMENTMANAGER")
  void testReturnsAppointmentManagerWhenActiveAppointmentManager() throws Exception {
    // Arrange
    String username = "managerUser";
    String password = "managerPass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getString("ROLE_TYPE")).thenReturn("APPOINTMENTMANAGER");
    when(resultSetMock.getString("STATE_TYPE")).thenReturn("ACTIVE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    Role result = repository.opVerifyUser(username, password);

    // Assert
    assertEquals(Role.APPOINTMENTMANAGER, result);
    verify(preparedStatementMock).setString(1, username);
    verify(preparedStatementMock).setString(2, password);
  }

  @Test
  @DisplayName("opVerifyUser throws IllegalArgumentException when role in DB is unknown (error case)")
  void testThrowsWhenActiveWithUnknownRole() throws Exception {
    // Arrange
    String username = "unknownRoleUser";
    String password = "somePass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getString("ROLE_TYPE")).thenReturn("UNKNOWN_ROLE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> repository.opVerifyUser(username, password));
    verify(preparedStatementMock).setString(1, username);
    verify(preparedStatementMock).setString(2, password);
  }

  @Test
  @DisplayName("opVerifyUser throws RuntimeException when user is not found (invalid credentials)")
  void testThrowsRuntimeExceptionWhenUserNotFound() throws Exception {
    // Arrange
    String username = "nonExistingUser";
    String password = "wrongPass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(false);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class,
        () -> repository.opVerifyUser(username, password));

    assertEquals("Usuario o contraseña incorrectos. El usuario no existe en la base de datos.", thrown.getMessage());
    verify(preparedStatementMock).setString(1, username);
    verify(preparedStatementMock).setString(2, password);
  }

  @Test
  @DisplayName("opVerifyUser throws RuntimeException when user is BLOCKED (query excludes non-ACTIVE)")
  void testThrowsRuntimeExceptionWhenUserBlocked() throws Exception {
    // Arrange - SQL filters by STATE_TYPE='ACTIVE', so blocked user returns no rows
    String username = "blockedUser";
    String password = "somePass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(false);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class,
        () -> repository.opVerifyUser(username, password));

    assertEquals("Usuario o contraseña incorrectos. El usuario no existe en la base de datos.", thrown.getMessage());
    verify(preparedStatementMock).setString(1, username);
    verify(preparedStatementMock).setString(2, password);
  }

  @Test
  @DisplayName("opVerifyUser wraps SQLException in RuntimeException (error case)")
  void testThrowsRuntimeExceptionWhenSQLExceptionOccurs() throws Exception {
    // Arrange
    String username = "anyUser";
    String password = "anyPass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    SQLException sqlException = new SQLException("DB down");
    when(dbConnectionMock.connect()).thenThrow(sqlException);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class,
        () -> repository.opVerifyUser(username, password));

    assertEquals("Error verificando usuario", thrown.getMessage());
    assertEquals(sqlException, thrown.getCause());
  }

  @Test
  @DisplayName("opVerifyUser works with borderline empty username/password when user exists")
  void testAllowsEmptyCredentialsWhenRecordMatches() throws Exception {
    // Arrange
    String username = "";
    String password = "";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getString("ROLE_TYPE")).thenReturn("PATIENT");
    when(resultSetMock.getString("STATE_TYPE")).thenReturn("ACTIVE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    Role result = repository.opVerifyUser(username, password);

    // Assert
    assertEquals(Role.PATIENT, result);
    verify(preparedStatementMock).setString(1, username);
    verify(preparedStatementMock).setString(2, password);
  }

  @Test
  @DisplayName("opVerifyUser wraps SQLException when executeQuery throws SQLException")
  void testThrowsRuntimeExceptionWhenExecuteQueryThrowsSQLException() throws Exception {
    // Arrange
    String username = "anyUser";
    String password = "anyPass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    SQLException sqlException = new SQLException("Query failed");

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenThrow(sqlException);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class,
        () -> repository.opVerifyUser(username, password));

    assertEquals("Error verificando usuario", thrown.getMessage());
    assertEquals(sqlException, thrown.getCause());
    verify(preparedStatementMock).setString(1, username);
    verify(preparedStatementMock).setString(2, password);
  }

  @Test
  @DisplayName("opFindAll returns list of users from database")
  void testOpFindAllReturnsUsers() throws Exception {
    // Arrange
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(true, false);
    when(resultSetMock.getString("ROLE_TYPE")).thenReturn("PATIENT");
    when(resultSetMock.getString("USER_USERNAME")).thenReturn("testuser");
    when(resultSetMock.getString("USER_FULLNAME")).thenReturn("Test User");
    when(resultSetMock.getString("STATE_TYPE")).thenReturn("ACTIVE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    java.util.List<com.piedrazul.Domain.entities.ClsUser> result = repository.opFindAll();

    // Assert
    assertEquals(1, result.size());
    assertEquals("testuser", result.get(0).getAttUsername());
    verify(preparedStatementMock).executeQuery();
  }

  
}

