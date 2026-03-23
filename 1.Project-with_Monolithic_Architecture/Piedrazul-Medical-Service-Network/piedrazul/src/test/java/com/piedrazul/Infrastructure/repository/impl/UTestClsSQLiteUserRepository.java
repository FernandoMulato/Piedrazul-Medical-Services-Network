package com.piedrazul.Infrastructure.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Domain.enums.State;
import com.piedrazul.Infrastructure.config.IDatabaseConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    assertEquals("Acceso denegado - Usuario BLOQUEADO", thrown.getMessage());
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

  // --- opSave (Create) ---

  @Test
  @DisplayName("opSave persists ClsUser and returns user with generated id (normal case)")
  void opSavePersistsClsUserAndReturnsWithGeneratedId() throws Exception {
    // Arrange
    ClsUser prmUser = new ClsUser("newuser", "New User", "password123", Role.ADMINISTRATOR, State.ACTIVE);
    long generatedId = 10L;

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);
    ResultSet generatedKeysMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(pstmtMock);
    when(pstmtMock.executeUpdate()).thenReturn(1);
    when(pstmtMock.getGeneratedKeys()).thenReturn(generatedKeysMock);
    when(generatedKeysMock.next()).thenReturn(true);
    when(generatedKeysMock.getLong(1)).thenReturn(generatedId);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    ClsUser result = repository.opSave(prmUser);

    // Assert
    assertNotNull(result);
    assertEquals(generatedId, result.getAttId());
    verify(pstmtMock).setString(1, "newuser");
    verify(pstmtMock).setString(2, "New User");
    verify(pstmtMock).setString(3, "password123");
    verify(pstmtMock).setInt(4, Role.ADMINISTRATOR.getId());
    verify(pstmtMock).setInt(5, State.ACTIVE.getId());
  }

  @Test
  @DisplayName("opSave returns null when executeUpdate affects zero rows (borderline case)")
  void opSaveReturnsNullWhenExecuteUpdateAffectsZeroRows() throws Exception {
    // Arrange
    ClsUser prmUser = new ClsUser("newuser", "New User", "password123", Role.PATIENT, State.ACTIVE);

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(pstmtMock);
    when(pstmtMock.executeUpdate()).thenReturn(0);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    ClsUser result = repository.opSave(prmUser);

    // Assert
    assertNull(result);
  }

  @Test
  @DisplayName("opSave throws RuntimeException when SQLException occurs (error case)")
  void opSaveThrowsRuntimeExceptionWhenSQLExceptionOccurs() throws Exception {
    // Arrange
    ClsUser prmUser = new ClsUser("newuser", "New User", "password123", Role.PATIENT, State.ACTIVE);
    SQLException sqlException = new SQLException("DB error");

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    when(dbConnectionMock.connect()).thenThrow(sqlException);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> repository.opSave(prmUser));
    assertEquals("Error saving user", thrown.getMessage());
    assertNotNull(thrown.getCause());
    assertEquals(sqlException, thrown.getCause());
  }

  // --- opGet (Query/Read) ---

  @Test
  @DisplayName("opGet returns user when found in database (normal case)")
  void opGetReturnsUserWhenFound() throws Exception {
    // Arrange
    long userId = 1L;
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);
    ResultSet rsMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(pstmtMock);
    when(pstmtMock.executeQuery()).thenReturn(rsMock);
    when(rsMock.next()).thenReturn(true);
    when(rsMock.getLong("USER_ID")).thenReturn(userId);
    when(rsMock.getString("USER_USERNAME")).thenReturn("testuser");
    when(rsMock.getString("USER_FULLNAME")).thenReturn("Test User");
    when(rsMock.getString("USER_PASSWORD")).thenReturn("encrypted");
    when(rsMock.getString("ROLE_TYPE")).thenReturn("PATIENT");
    when(rsMock.getString("STATE_TYPE")).thenReturn("ACTIVE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    ClsUser result = repository.opGet(userId);

    // Assert
    assertNotNull(result);
    assertEquals(userId, result.getAttId());
    assertEquals("testuser", result.getAttUsername());
    assertEquals("Test User", result.getAttFullname());
    assertEquals(Role.PATIENT, result.getAttRole());
    assertEquals(State.ACTIVE, result.getAttState());
    verify(pstmtMock).setLong(1, userId);
  }

  @Test
  @DisplayName("opGet returns null when user not found (borderline case)")
  void opGetReturnsNullWhenUserNotFound() throws Exception {
    // Arrange
    long userId = 999L;
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);
    ResultSet rsMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(pstmtMock);
    when(pstmtMock.executeQuery()).thenReturn(rsMock);
    when(rsMock.next()).thenReturn(false);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    ClsUser result = repository.opGet(userId);

    // Assert
    assertNull(result);
    verify(pstmtMock).setLong(1, userId);
  }

  @Test
  @DisplayName("opGet throws RuntimeException when SQLException occurs (error case)")
  void opGetThrowsRuntimeExceptionWhenSQLExceptionOccurs() throws Exception {
    // Arrange
    SQLException sqlException = new SQLException("Connection lost");
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    when(dbConnectionMock.connect()).thenThrow(sqlException);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> repository.opGet(1L));
    assertEquals("Error obteniendo usuario", thrown.getMessage());
  }

  // --- opFindAll (Query/Read) ---

  @Test
  @DisplayName("opFindAll returns list of users when database has users (normal case)")
  void opFindAllReturnsListOfUsers() throws Exception {
    // Arrange
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);
    ResultSet rsMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(pstmtMock);
    when(pstmtMock.executeQuery()).thenReturn(rsMock);
    when(rsMock.next()).thenReturn(true, true, false);
    when(rsMock.getLong("USER_ID")).thenReturn(1L, 2L);
    when(rsMock.getString("USER_USERNAME")).thenReturn("user1", "user2");
    when(rsMock.getString("USER_FULLNAME")).thenReturn("User One", "User Two");
    when(rsMock.getString("ROLE_TYPE")).thenReturn("PATIENT", "ADMINISTRATOR");
    when(rsMock.getString("STATE_TYPE")).thenReturn("ACTIVE", "ACTIVE");

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    List<ClsUser> result = repository.opFindAll();

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(1L, result.get(0).getAttId());
    assertEquals("user1", result.get(0).getAttUsername());
    assertEquals(2L, result.get(1).getAttId());
    assertEquals("user2", result.get(1).getAttUsername());
  }

  @Test
  @DisplayName("opFindAll returns empty list when database has no users (borderline case)")
  void opFindAllReturnsEmptyListWhenNoUsers() throws Exception {
    // Arrange
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);
    ResultSet rsMock = mock(ResultSet.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(pstmtMock);
    when(pstmtMock.executeQuery()).thenReturn(rsMock);
    when(rsMock.next()).thenReturn(false);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    List<ClsUser> result = repository.opFindAll();

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("opFindAll throws RuntimeException when SQLException occurs (error case)")
  void opFindAllThrowsRuntimeExceptionWhenSQLExceptionOccurs() throws Exception {
    // Arrange
    SQLException sqlException = new SQLException("Query failed");
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(pstmtMock);
    when(pstmtMock.executeQuery()).thenThrow(sqlException);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, repository::opFindAll);
    assertEquals("Error obteniendo usuarios", thrown.getMessage());
  }

  // --- opUpdate (Update) ---

  @Test
  @DisplayName("opUpdate returns true when user updated successfully (normal case)")
  void opUpdateReturnsTrueWhenUserUpdated() throws Exception {
    // Arrange
    ClsUser user = new ClsUser("updated", "Updated Name", "password123", Role.PATIENT, State.ACTIVE);
    user.setAttId(1L);

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(pstmtMock);
    when(pstmtMock.executeUpdate()).thenReturn(1);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    boolean result = repository.opUpdate(user);

    // Assert
    assertTrue(result);
    verify(pstmtMock).setString(1, "updated");
    verify(pstmtMock).setString(2, "Updated Name");
    verify(pstmtMock).setString(3, "password123");
    verify(pstmtMock).setInt(4, Role.PATIENT.getId());
    verify(pstmtMock).setInt(5, State.ACTIVE.getId());
    verify(pstmtMock).setLong(6, 1L);
  }

  @Test
  @DisplayName("opUpdate returns false when no rows affected (borderline case)")
  void opUpdateReturnsFalseWhenNoRowsAffected() throws Exception {
    // Arrange
    ClsUser user = new ClsUser("nonexistent", "Name", "password123", Role.PATIENT, State.ACTIVE);
    user.setAttId(999L);

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(pstmtMock);
    when(pstmtMock.executeUpdate()).thenReturn(0);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    boolean result = repository.opUpdate(user);

    // Assert
    assertFalse(result);
  }

  @Test
  @DisplayName("opUpdate throws RuntimeException when SQLException occurs (error case)")
  void opUpdateThrowsRuntimeExceptionWhenSQLExceptionOccurs() throws Exception {
    // Arrange
    ClsUser user = new ClsUser("user", "Name", "password123", Role.PATIENT, State.ACTIVE);
    user.setAttId(1L);
    SQLException sqlException = new SQLException("Update failed");

    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    when(dbConnectionMock.connect()).thenThrow(sqlException);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> repository.opUpdate(user));
    assertEquals("Error actualizando usuario", thrown.getMessage());
  }

  // --- opDeactivate (Deactivate) ---

  @Test
  @DisplayName("opDeactivate returns true when state updated successfully (normal case)")
  void opDeactivateReturnsTrueWhenSuccessful() throws Exception {
    // Arrange
    long userId = 1L;
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(pstmtMock);
    when(pstmtMock.executeUpdate()).thenReturn(1);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    boolean result = repository.opDeactivate(userId);

    // Assert
    assertTrue(result);
    verify(pstmtMock).setInt(1, State.INACTIVE.getId());
    verify(pstmtMock).setLong(2, userId);
  }

  @Test
  @DisplayName("opDeactivate returns false when no rows affected (borderline case)")
  void opDeactivateReturnsFalseWhenUserNotFound() throws Exception {
    // Arrange
    long userId = 999L;
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    Connection connectionMock = mock(Connection.class);
    PreparedStatement pstmtMock = mock(PreparedStatement.class);

    when(dbConnectionMock.connect()).thenReturn(connectionMock);
    when(connectionMock.prepareStatement(anyString())).thenReturn(pstmtMock);
    when(pstmtMock.executeUpdate()).thenReturn(0);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act
    boolean result = repository.opDeactivate(userId);

    // Assert
    assertFalse(result);
  }

  @Test
  @DisplayName("opDeactivate throws RuntimeException when SQLException occurs (error case)")
  void opDeactivateThrowsRuntimeExceptionWhenSQLExceptionOccurs() throws Exception {
    // Arrange
    SQLException sqlException = new SQLException("DB error");
    IDatabaseConnection dbConnectionMock = mock(IDatabaseConnection.class);
    when(dbConnectionMock.connect()).thenThrow(sqlException);

    ClsSQLiteUserRepository repository = new ClsSQLiteUserRepository(dbConnectionMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> repository.opDeactivate(1L));
    assertEquals("Error actualizando el estado del usuario", thrown.getMessage());
  }
}

