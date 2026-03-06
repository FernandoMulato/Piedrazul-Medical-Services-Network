package com.piedrazul.Presentation.controller;

import com.piedrazul.Application.services.IUserService;
import com.piedrazul.Domain.entities.ClsUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClsUserController {

  private final IUserService userService;

  public ClsUser createUser(ClsUser prmUser) {
    // 1. Enviar al servicio de usuario
    // 2. Si no se pudo crear el usuario retorna excepción
    // 3. si se pudo crear el usuario de retorna el usuario
    return userService.opCreateUser(prmUser); 
  }
}