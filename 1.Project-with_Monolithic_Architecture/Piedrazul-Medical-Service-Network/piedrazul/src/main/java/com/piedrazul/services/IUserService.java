package com.piedrazul.Services;

import java.util.List;

import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Role;

public interface IUserService {
    ClsUser opCreateUser(ClsUser prmUser);
    boolean opUpdateUser(ClsUser prmUser);
    boolean opDeactivateUser(long id);
    ClsUser opGetUser(long id);
    List<ClsUser> opList();
    Role opVerifyUser(String prmUsername, String prmPassword);
}
