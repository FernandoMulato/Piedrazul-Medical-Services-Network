package com.piedrazul.Application.services.impl;

import java.util.List;

import com.piedrazul.Application.services.IUserService;
import com.piedrazul.Domain.core.events.ClsClinicEventBus;
import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Infrastructure.repository.IUserRepository;

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

    if (userRepository.opExistsByUsername(prmUser.getAttUsername())) {
        throw new IllegalArgumentException("El nombre de usuario ya existe");
    }

    // si quieres cumplir la historia de cifrado
    prmUser.setAttPassword(PasswordUtil.hash(prmUser.getAttPassword()));

    ClsUser created = userRepository.opSave(prmUser);

    if (created == null) {
        throw new RuntimeException("No se pudo registrar el usuario");
    }

    return created;
  }


  @Override
  public Role opVerifyUser(String prmUsername, String prmPassword) {
    return userRepository.opVerifyUser(prmUsername, prmPassword);
  }

  public boolean opDeactivateUser(long id) {
    return userRepository.opDeactivate(id);
  }
  
}
