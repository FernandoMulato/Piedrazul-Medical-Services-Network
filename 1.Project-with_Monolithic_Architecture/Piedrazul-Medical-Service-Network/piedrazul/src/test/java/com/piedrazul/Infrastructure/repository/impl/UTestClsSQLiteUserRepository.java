package com.piedrazul.Infrastructure.repository.impl;

import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Infrastructure.config.IDatabaseConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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
    assertEquals(Role.Administrator, result);
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
    assertEquals(Role.Patient, result);
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
    assertEquals(Role.ClinicalStaff, result);
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
    assertEquals(Role.AppointmentManager, result);
  }

  @Test
  @DisplayName("opVerifyUser returns null when user is ACTIVE but role is unknown (borderline case)")
  void testReturnsNullWhenActiveWithUnknownRole() throws Exception {
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
    when(resultSetMock.getString("STATE_TYPE")).thenReturn("ACTIVE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    Role result = repository.opVerifyUser(username, password);

    // Assert
    assertNull(result);
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
  }

  @Test
  @DisplayName("opVerifyUser throws RuntimeException when user is BLOCKED (non-ACTIVE state)")
  void testThrowsRuntimeExceptionWhenUserBlocked() throws Exception {
    // Arrange
    String username = "blockedUser";
    String password = "somePass";

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getString("ROLE_TYPE")).thenReturn("PATIENT");
    when(resultSetMock.getString("STATE_TYPE")).thenReturn("BLOCKED");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class,
        () -> repository.opVerifyUser(username, password));

    assertEquals("Acceso denegado - Usuario BLOQUEADO", thrown.getMessage());
  }

  @Test
  @DisplayName("opVerifyUser wraps SQLException in RuntimeException with message 'Error verificando el usuario. 500'")
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

    assertEquals("Error verificando el usuario. 500", thrown.getMessage());
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
    assertEquals(Role.Patient, result);
  }
}

