package com.piedrazul.Infrastructure.repository;

import java.util.List;

import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Role;

public interface IUserRepository {
  ClsUser opSave(ClsUser user);
  boolean opDelete(long id);
  boolean opUpdate(ClsUser user);
  ClsUser opGet(long id);
  List<ClsUser> opFindAll();
  Role opVerifyUser(String username, String password) throws RuntimeException;
}
