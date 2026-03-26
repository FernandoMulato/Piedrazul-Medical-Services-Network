package com.piedrazul.Presentation.controller;

import java.util.List;

import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.services.IUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserController {

  private final IUserService userService;

  public List<ClsUser> opList() {
    return userService.opList();
  }


  public ClsUser opCreateUser(ClsUser prmUser) {
    // 1. Enviar al servicio de usuario
    // 2. Si no se pudo crear el usuario retorna excepción
    // 3. si se pudo crear el usuario de retorna el usuario
    return userService.opCreateUser(prmUser);
  }

  public boolean opUpdateUser(ClsUser prmUser) {
    return userService.opUpdateUser(prmUser);
  }

  public boolean opDeactivateUser(long id) {
    return userService.opDeactivateUser(id);
  }

  public ClsUser opGetUser(long id) {
    return userService.opGetUser(id);
  }

}