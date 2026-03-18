package com.piedrazul.Domain.entities;

import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Domain.enums.State;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClsAdministrator extends ClsUser {

  public ClsAdministrator(
      String prmUsername,
      String prmFullname,
      String prmPassword,
      Role prmRole,
      State prmState) {
    super(
        prmUsername,
        prmFullname,
        prmPassword,
        prmRole,
        prmState);
  }

}
