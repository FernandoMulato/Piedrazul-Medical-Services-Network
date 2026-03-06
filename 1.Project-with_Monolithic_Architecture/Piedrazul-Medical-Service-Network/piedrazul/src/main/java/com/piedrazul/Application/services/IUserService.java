package com.piedrazul.Application.services;

import com.piedrazul.Domain.entities.ClsUser;

public interface IUserService {
    ClsUser opCreateUser(ClsUser prmUser);
    boolean opLogin(String prmUsername, String prmPassword);
}
