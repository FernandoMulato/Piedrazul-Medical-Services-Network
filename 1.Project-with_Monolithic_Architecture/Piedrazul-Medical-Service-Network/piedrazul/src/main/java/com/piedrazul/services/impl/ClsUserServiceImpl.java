package com.piedrazul.services.impl;

import java.util.List;

import com.piedrazul.Domain.core.events.ClsClinicEventBus;
import com.piedrazul.Domain.entities.ClsClinicalStaff;
import com.piedrazul.Domain.entities.ClsPatient;
import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Infrastructure.repository.IUserRepository;
import com.piedrazul.services.IUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClsUserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final ClsClinicEventBus eventBus;

    @Override
    public List<ClsUser> opList() {
        return userRepository.opFindAll();
    }

    @Override
    public ClsUser opCreateUser(ClsUser prmUser) {
        validateCommonFields(prmUser);
        validateRoleSpecificFields(prmUser);

        if (userRepository.opExistsByUsername(prmUser.getAttUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        // Se deja sin cifrado por ahora porque PasswordUtil no existe todavía en el
        // proyecto.
        ClsUser created = userRepository.opSave(prmUser);

        if (created == null) {
            throw new RuntimeException("No se pudo registrar el usuario");
        }

        return created;
    }

    private void validateCommonFields(ClsUser prmUser) {
        if (prmUser == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        if (prmUser.getAttUsername() == null || prmUser.getAttUsername().isBlank()
                || prmUser.getAttFullname() == null || prmUser.getAttFullname().isBlank()
                || prmUser.getAttPassword() == null || prmUser.getAttPassword().isBlank()
                || prmUser.getAttRole() == null
                || prmUser.getAttState() == null) {
            throw new IllegalArgumentException("Los campos obligatorios deben estar completos");
        }

        if (prmUser.getAttPassword().length() < 8) {
            throw new IllegalArgumentException("La contraseña no cumple con política mínima");
        }
    }

    private void validateRoleSpecificFields(ClsUser prmUser) {
        if (prmUser instanceof ClsPatient patient) {
            if (patient.getAttCitizenshipCard() <= 0) {
                throw new IllegalArgumentException("El paciente debe tener un documento de identidad válido");
            }

            if (patient.getAttPhoneNumber() == null || patient.getAttPhoneNumber().isBlank()) {
                throw new IllegalArgumentException("El paciente debe tener número de teléfono");
            }
        }

        if (prmUser instanceof ClsClinicalStaff staff) {
            if (staff.getAttProfession() == null) {
                throw new IllegalArgumentException("Debe seleccionar la profesión del personal médico");
            }

            if (staff.getAttSpecialty() == null) {
                throw new IllegalArgumentException("Debe seleccionar la especialidad del personal médico");
            }
        }
    }

    @Override
    public Role opVerifyUser(String prmUsername, String prmPassword) {
        return userRepository.opVerifyUser(prmUsername, prmPassword);
    }

    @Override
    public boolean opDeactivateUser(long id) {
        return userRepository.opDeactivate(id);
    }

    @Override
    public boolean opUpdateUser(ClsUser user) {

        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        // getAttId() devuelve long (primitivo) — solo verificamos que sea > 0
        if (user.getAttId() <= 0) {
            throw new IllegalArgumentException("El usuario debe tener un ID válido para actualizar");
        }

        if (user.getAttUsername() == null || user.getAttUsername().isBlank()
                || user.getAttFullname() == null || user.getAttFullname().isBlank()
                || user.getAttPassword() == null || user.getAttPassword().isBlank()
                || user.getAttRole() == null
                || user.getAttState() == null) {
            throw new IllegalArgumentException("Los campos obligatorios deben estar completos");
        }

        if (user.getAttPassword().length() < 8) {
            throw new IllegalArgumentException("La contraseña no cumple con política mínima");
        }

        // Validar campos específicos por rol (igual que en creación)
        validateRoleSpecificFields(user);

        // Verificar duplicado de username excluyendo al propio usuario
        if (userRepository.opExistsByUsernameExcludingId(user.getAttUsername(), user.getAttId())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        boolean updated = userRepository.opUpdate(user);

        if (!updated) {
            throw new RuntimeException("No se pudo actualizar el usuario");
        }

        return true;
    }

    @Override
    public ClsUser opGetUser(long id) {
        return userRepository.opGet(id);
    }

    // Evitar alerta
    public void nn() {
        eventBus.publish(null);
    }
}