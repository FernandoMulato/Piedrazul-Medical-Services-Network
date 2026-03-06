package com.piedrazul.Infrastructure.repository;

import com.piedrazul.Domain.entities.ClsUser;

public interface IUserRepository {
  ClsUser opSave(ClsUser user);

  boolean opDelete(long id);

  boolean opUpdate(ClsUser user);

  ClsUser opGet(long id);
}
