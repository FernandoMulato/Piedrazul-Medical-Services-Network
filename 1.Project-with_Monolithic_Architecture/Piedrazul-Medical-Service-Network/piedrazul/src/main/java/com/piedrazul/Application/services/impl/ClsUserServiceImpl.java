package com.piedrazul.Application.services.impl;

import com.piedrazul.Application.services.IUserService;
import com.piedrazul.Domain.entities.ClsUser;

public class ClsUserServiceImpl implements IUserService {

  @Override
  public ClsUser opCreateUser(ClsUser prmUser) {
    // 1. Verificar que el admin este registrado
    // Si no existe
    // 1.1. se lanza una excepción (posiblemente adminService)
    // Si existe
    // 1.2. crear un usuario segun su rol (dominio)
    // 1.3. se validan datos (dominio)
    // 1.3.1. si los datos estan mal se retorna exception (posiblemente desde el dominio)
    // 1.3.2. si los datos están bien, continuamos
    // 1.4. se verifica que el username sea unico
    // 1.4.1. Si no esta disponible: se retorna excepción
    // 1.4.2. Si esta disponible: 
    // 1.4.2.1. se guarda usuario en la base de datos
    // 1.4.2.1.1. Si se recibe confirmación: se retorna el nuevo usuario
    // 1.4.2.1.2. Si se recibe error: se retorna excepción (verificar de que tipo)
    return null;
  }

}
