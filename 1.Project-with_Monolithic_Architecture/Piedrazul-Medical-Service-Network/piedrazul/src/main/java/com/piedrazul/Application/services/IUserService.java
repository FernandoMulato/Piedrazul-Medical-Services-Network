package com.piedrazul.Application.services;

import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Role;

public interface IUserService {
    ClsUser opCreateUser(ClsUser prmUser);
    Role opVerifyUser(String prmUsername, String prmPassword);
}
