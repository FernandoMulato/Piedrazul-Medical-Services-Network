package com.piedrazul.Application.services.impl;

import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Infrastructure.repository.IUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UTestClsUserServiceImpl {

  @Test
  @DisplayName("opVerifyUser returns role from repository for valid credentials")
  void testReturnsRoleFromRepositoryForValidCredentials() {
    // Arrange
    String username = "validUser";
    String password = "validPassword";
    Role expectedRole = Role.Patient;

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    when(userRepositoryMock.opVerifyUser(eq(username), eq(password))).thenReturn(expectedRole);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock);

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

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    when(userRepositoryMock.opVerifyUser(eq(username), eq(password))).thenReturn(null);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock);

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

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    RuntimeException expected = new RuntimeException("Repository error");
    when(userRepositoryMock.opVerifyUser(eq(username), eq(password))).thenThrow(expected);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class,
        () -> service.opVerifyUser(username, password));

    assertEquals("Repository error", thrown.getMessage());
    verify(userRepositoryMock).opVerifyUser(username, password);
  }
}

