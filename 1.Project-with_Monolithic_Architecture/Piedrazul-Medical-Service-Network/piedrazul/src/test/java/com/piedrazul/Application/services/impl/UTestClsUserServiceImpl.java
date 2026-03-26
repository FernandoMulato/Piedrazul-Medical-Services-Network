package com.piedrazul.Application.services.impl;

import java.util.Collections;
import java.util.List;

import com.piedrazul.Domain.core.events.ClsClinicEventBus;
import com.piedrazul.Domain.entities.ClsClinicalStaff;
import com.piedrazul.Domain.entities.ClsPatient;
import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Profession;
import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Domain.enums.Specialty;
import com.piedrazul.Domain.enums.State;
import com.piedrazul.Infrastructure.repository.IUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UTestClsUserServiceImpl {

  private static final String VALID_USERNAME = "testuser";
  private static final String VALID_FULLNAME = "Test User";
  private static final String VALID_PASSWORD = "password123";

  // --- opVerifyUser (existing) ---

  @Test
  @DisplayName("opVerifyUser returns role from repository for valid credentials")
  void testReturnsRoleFromRepositoryForValidCredentials() {
    // Arrange
    String username = "validUser";
    String password = "validPassword";
    Role expectedRole = Role.PATIENT;

    IUserRepository userRepositoryMock = mock(IUserRepository.class);

    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);

    when(userRepositoryMock.opVerifyUser(eq(username), eq(password))).thenReturn(expectedRole);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    Role result = service.opVerifyUser(username, password);

    // Assert
    assertEquals(expectedRole, result);
    verify(userRepositoryMock).opVerifyUser(username, password);
  }

  @Test
  @DisplayName("opVerifyUser returns null when repository returns null (borderline case)")
  void testReturnsNullWhenRepositoryReturnsNull() {
    // Arrange
    String username = "userWithoutRole";
    String password = "anyPassword";

    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    when(userRepositoryMock.opVerifyUser(eq(username), eq(password))).thenReturn(null);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    Role result = service.opVerifyUser(username, password);

    // Assert
    assertNull(result);
    verify(userRepositoryMock).opVerifyUser(username, password);
  }

  @Test
  @DisplayName("opVerifyUser propagates RuntimeException thrown by repository (error case)")
  void testThrowsRuntimeExceptionWhenRepositoryThrows() {
    // Arrange
    String username = "invalidUser";
    String password = "wrongPassword";

    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    RuntimeException expected = new RuntimeException("Repository error");
    when(userRepositoryMock.opVerifyUser(eq(username), eq(password))).thenThrow(expected);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class,
        () -> service.opVerifyUser(username, password));

    assertEquals("Repository error", thrown.getMessage());
    verify(userRepositoryMock).opVerifyUser(username, password);
  }

  @Test
  @DisplayName("opList returns list of users from repository")
  void testOpListReturnsUsersFromRepository() {
    // Arrange
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    java.util.List<com.piedrazul.Domain.entities.ClsUser> expectedUsers = java.util.Collections.emptyList();
    when(userRepositoryMock.opFindAll()).thenReturn(expectedUsers);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    java.util.List<com.piedrazul.Domain.entities.ClsUser> result = service.opList();

    // Assert
    assertEquals(expectedUsers, result);
    verify(userRepositoryMock).opFindAll();
  }

  @Test
  @DisplayName("opCreateUser calls eventBus.subscribe(null) and returns null (placeholder test)")
  void testOpCreateUserReturnsNullAndCallsEventBus() {
    // Arrange
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    com.piedrazul.Domain.entities.ClsUser user = new com.piedrazul.Domain.entities.ClsUser();

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    com.piedrazul.Domain.entities.ClsUser result = service.opCreateUser(user);

    // Assert
    assertNull(result);
    verify(eventBusMock).subscribe(null);
  }
}
