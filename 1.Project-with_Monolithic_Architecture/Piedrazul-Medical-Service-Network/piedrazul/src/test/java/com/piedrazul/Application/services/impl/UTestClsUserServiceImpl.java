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

  // --- opCreateUser (Create) ---

  @Test
  @DisplayName("opCreateUser returns created user when valid ClsUser and username available (normal case)")
  void opCreateUserReturnsCreatedUserWhenValidClsUser() {
    // Arrange
    ClsUser prmUser = new ClsUser(VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.ADMINISTRATOR, State.ACTIVE);
    ClsUser savedUser = new ClsUser(VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.ADMINISTRATOR, State.ACTIVE);
    savedUser.setAttId(1L);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsername(VALID_USERNAME)).thenReturn(false);
    when(userRepositoryMock.opSave(any(ClsUser.class))).thenReturn(savedUser);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    ClsUser result = service.opCreateUser(prmUser);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getAttId());
    assertEquals(VALID_USERNAME, result.getAttUsername());
    verify(userRepositoryMock).opExistsByUsername(VALID_USERNAME);
    verify(userRepositoryMock).opSave(prmUser);
  }

  @Test
  @DisplayName("opCreateUser returns created patient when valid ClsPatient (normal case)")
  void opCreateUserReturnsCreatedPatientWhenValidClsPatient() {
    // Arrange
    ClsPatient prmUser = new ClsPatient(
        VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE,
        12345678L, "555-1234");
    ClsPatient savedUser = new ClsPatient(
        VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE,
        12345678L, "555-1234");
    savedUser.setAttId(2L);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsername(VALID_USERNAME)).thenReturn(false);
    when(userRepositoryMock.opSave(any(ClsUser.class))).thenReturn(savedUser);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    ClsUser result = service.opCreateUser(prmUser);

    // Assert
    assertNotNull(result);
    assertEquals(2L, result.getAttId());
    verify(userRepositoryMock).opSave(prmUser);
  }

  @Test
  @DisplayName("opCreateUser returns created clinical staff when valid ClsClinicalStaff (normal case)")
  void opCreateUserReturnsCreatedClinicalStaffWhenValidClsClinicalStaff() {
    // Arrange
    ClsClinicalStaff prmUser = new ClsClinicalStaff(
        VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.CLINICALSTAFF, State.ACTIVE,
        Profession.MEDIC, Specialty.PHYSIO);
    ClsClinicalStaff savedUser = new ClsClinicalStaff(
        VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.CLINICALSTAFF, State.ACTIVE,
        Profession.MEDIC, Specialty.PHYSIO);
    savedUser.setAttId(3L);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsername(VALID_USERNAME)).thenReturn(false);
    when(userRepositoryMock.opSave(any(ClsUser.class))).thenReturn(savedUser);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    ClsUser result = service.opCreateUser(prmUser);

    // Assert
    assertNotNull(result);
    assertEquals(3L, result.getAttId());
    verify(userRepositoryMock).opSave(prmUser);
  }

  @Test
  @DisplayName("opCreateUser throws when username already exists (borderline case)")
  void opCreateUserThrowsWhenUsernameAlreadyExists() {
    // Arrange
    ClsUser prmUser = new ClsUser(VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE);
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsername(VALID_USERNAME)).thenReturn(true);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opCreateUser(prmUser));
    assertEquals("El nombre de usuario ya existe", thrown.getMessage());
    verify(userRepositoryMock).opExistsByUsername(VALID_USERNAME);
    verify(userRepositoryMock, org.mockito.Mockito.never()).opSave(any());
  }

  @Test
  @DisplayName("opCreateUser throws RuntimeException when repository opSave returns null (error case)")
  void opCreateUserThrowsWhenRepositorySaveReturnsNull() {
    // Arrange
    ClsUser prmUser = new ClsUser(VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE);
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsername(VALID_USERNAME)).thenReturn(false);
    when(userRepositoryMock.opSave(any(ClsUser.class))).thenReturn(null);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.opCreateUser(prmUser));
    assertEquals("No se pudo registrar el usuario", thrown.getMessage());
  }

  @Test
  @DisplayName("opCreateUser throws when user is null (error case)")
  void opCreateUserThrowsWhenUserIsNull() {
    // Arrange
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opCreateUser(null));
    assertEquals("El usuario no puede ser nulo", thrown.getMessage());
    verify(userRepositoryMock, org.mockito.Mockito.never()).opSave(any());
  }

  @Test
  @DisplayName("opCreateUser throws when mandatory fields are blank (error case)")
  void opCreateUserThrowsWhenMandatoryFieldsAreBlank() {
    // Arrange
    ClsUser prmUser = new ClsUser("", "", "", null, null);
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opCreateUser(prmUser));
    assertEquals("Los campos obligatorios deben estar completos", thrown.getMessage());
  }

  @Test
  @DisplayName("opCreateUser throws when password is shorter than 8 characters (error case)")
  void opCreateUserThrowsWhenPasswordTooShort() {
    // Arrange
    ClsUser prmUser = new ClsUser(VALID_USERNAME, VALID_FULLNAME, "short", Role.PATIENT, State.ACTIVE);
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opCreateUser(prmUser));
    assertEquals("La contraseña no cumple con política mínima", thrown.getMessage());
  }

  @Test
  @DisplayName("opCreateUser throws when patient has invalid citizenship card (error case)")
  void opCreateUserThrowsWhenPatientHasInvalidCitizenshipCard() {
    // Arrange
    ClsPatient prmUser = new ClsPatient(
        VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE,
        0L, "555-1234");
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsername(VALID_USERNAME)).thenReturn(false);
    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opCreateUser(prmUser));
    assertEquals("El paciente debe tener un documento de identidad válido", thrown.getMessage());
  }

  @Test
  @DisplayName("opCreateUser throws when patient has blank phone number (error case)")
  void opCreateUserThrowsWhenPatientHasBlankPhoneNumber() {
    // Arrange
    ClsPatient prmUser = new ClsPatient(
        VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE,
        12345678L, "");
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsername(VALID_USERNAME)).thenReturn(false);
    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opCreateUser(prmUser));
    assertEquals("El paciente debe tener número de teléfono", thrown.getMessage());
  }

  @Test
  @DisplayName("opCreateUser throws when clinical staff has null profession (error case)")
  void opCreateUserThrowsWhenClinicalStaffHasNullProfession() {
    // Arrange
    ClsClinicalStaff prmUser = new ClsClinicalStaff(
        VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.CLINICALSTAFF, State.ACTIVE,
        null, Specialty.PHYSIO);
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsername(VALID_USERNAME)).thenReturn(false);
    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opCreateUser(prmUser));
    assertEquals("Debe seleccionar la profesión del personal médico", thrown.getMessage());
  }

  @Test
  @DisplayName("opCreateUser throws when clinical staff has null specialty (error case)")
  void opCreateUserThrowsWhenClinicalStaffHasNullSpecialty() {
    // Arrange
    ClsClinicalStaff prmUser = new ClsClinicalStaff(
        VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.CLINICALSTAFF, State.ACTIVE,
        Profession.MEDIC, null);
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsername(VALID_USERNAME)).thenReturn(false);
    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opCreateUser(prmUser));
    assertEquals("Debe seleccionar la especialidad del personal médico", thrown.getMessage());
  }

  // --- opUpdateUser (Update) ---

  @Test
  @DisplayName("opUpdateUser returns true when valid user and no username conflict (normal case)")
  void opUpdateUserReturnsTrueWhenValidUser() {
    // Arrange
    ClsUser user = new ClsUser(VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE);
    user.setAttId(1L);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsernameExcludingId(VALID_USERNAME, 1L)).thenReturn(false);
    when(userRepositoryMock.opUpdate(any(ClsUser.class))).thenReturn(true);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    boolean result = service.opUpdateUser(user);

    // Assert
    assertTrue(result);
    verify(userRepositoryMock).opExistsByUsernameExcludingId(VALID_USERNAME, 1L);
    verify(userRepositoryMock).opUpdate(user);
  }

  @Test
  @DisplayName("opUpdateUser throws when username exists for another user (borderline case)")
  void opUpdateUserThrowsWhenUsernameExistsForAnotherUser() {
    // Arrange
    ClsUser user = new ClsUser("takenuser", VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE);
    user.setAttId(1L);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsernameExcludingId("takenuser", 1L)).thenReturn(true);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opUpdateUser(user));
    assertEquals("El nombre de usuario ya existe", thrown.getMessage());
    verify(userRepositoryMock, org.mockito.Mockito.never()).opUpdate(any());
  }

  @Test
  @DisplayName("opUpdateUser throws when user is null (error case)")
  void opUpdateUserThrowsWhenUserIsNull() {
    // Arrange
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opUpdateUser(null));
    assertEquals("El usuario no puede ser nulo", thrown.getMessage());
  }

  @Test
  @DisplayName("opUpdateUser throws when user id is zero or negative (error case)")
  void opUpdateUserThrowsWhenUserIdInvalid() {
    // Arrange
    ClsUser user = new ClsUser(VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE);
    user.setAttId(0);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.opUpdateUser(user));
    assertEquals("El usuario debe tener un ID válido para actualizar", thrown.getMessage());
  }

  @Test
  @DisplayName("opUpdateUser throws RuntimeException when repository opUpdate returns false (error case)")
  void opUpdateUserThrowsWhenRepositoryUpdateReturnsFalse() {
    // Arrange
    ClsUser user = new ClsUser(VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE);
    user.setAttId(1L);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opExistsByUsernameExcludingId(VALID_USERNAME, 1L)).thenReturn(false);
    when(userRepositoryMock.opUpdate(any(ClsUser.class))).thenReturn(false);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.opUpdateUser(user));
    assertEquals("No se pudo actualizar el usuario", thrown.getMessage());
  }

  // --- opGetUser (Query/Read) ---

  @Test
  @DisplayName("opGetUser returns user when repository finds user (normal case)")
  void opGetUserReturnsUserWhenFound() {
    // Arrange
    ClsUser expectedUser = new ClsUser(VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE);
    expectedUser.setAttId(1L);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opGet(1L)).thenReturn(expectedUser);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    ClsUser result = service.opGetUser(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getAttId());
    assertEquals(VALID_USERNAME, result.getAttUsername());
    verify(userRepositoryMock).opGet(1L);
  }

  @Test
  @DisplayName("opGetUser returns null when repository returns null (borderline case)")
  void opGetUserReturnsNullWhenNotFound() {
    // Arrange
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opGet(999L)).thenReturn(null);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    ClsUser result = service.opGetUser(999L);

    // Assert
    assertNull(result);
    verify(userRepositoryMock).opGet(999L);
  }

  // --- opList (Query/Read) ---

  @Test
  @DisplayName("opList returns list from repository (normal case)")
  void opListReturnsListFromRepository() {
    // Arrange
    ClsUser user = new ClsUser(VALID_USERNAME, VALID_FULLNAME, VALID_PASSWORD, Role.PATIENT, State.ACTIVE);
    user.setAttId(1L);
    List<ClsUser> expectedList = List.of(user);

    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opFindAll()).thenReturn(expectedList);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    List<ClsUser> result = service.opList();

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getAttId());
    verify(userRepositoryMock).opFindAll();
  }

  @Test
  @DisplayName("opList returns empty list when repository returns empty (borderline case)")
  void opListReturnsEmptyListWhenNoUsers() {
    // Arrange
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opFindAll()).thenReturn(Collections.emptyList());

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    List<ClsUser> result = service.opList();

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(userRepositoryMock).opFindAll();
  }

  // --- opDeactivateUser (Deactivate) ---

  @Test
  @DisplayName("opDeactivateUser returns true when repository deactivates successfully (normal case)")
  void opDeactivateUserReturnsTrueWhenSuccessful() {
    // Arrange
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opDeactivate(1L)).thenReturn(true);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    boolean result = service.opDeactivateUser(1L);

    // Assert
    assertTrue(result);
    verify(userRepositoryMock).opDeactivate(1L);
  }

  @Test
  @DisplayName("opDeactivateUser returns false when repository returns false (borderline case)")
  void opDeactivateUserReturnsFalseWhenUserNotFoundOrAlreadyInactive() {
    // Arrange
    IUserRepository userRepositoryMock = mock(IUserRepository.class);
    ClsClinicEventBus eventBusMock = mock(ClsClinicEventBus.class);
    when(userRepositoryMock.opDeactivate(999L)).thenReturn(false);

    ClsUserServiceImpl service = new ClsUserServiceImpl(userRepositoryMock, eventBusMock);

    // Act
    boolean result = service.opDeactivateUser(999L);

    // Assert
    assertFalse(result);
    verify(userRepositoryMock).opDeactivate(999L);
  }
}
