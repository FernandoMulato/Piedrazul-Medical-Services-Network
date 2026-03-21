package com.piedrazul.Application.services;

import java.util.List;

import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Role;

public interface IUserService {
    ClsUser opCreateUser(ClsUser prmUser);
    List<ClsUser> opList();
    Role opVerifyUser(String prmUsername, String prmPassword);
}
