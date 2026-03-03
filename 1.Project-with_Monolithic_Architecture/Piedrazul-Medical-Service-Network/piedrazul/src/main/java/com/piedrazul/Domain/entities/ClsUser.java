package com.piedrazul.Domain.entities;

import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Domain.enums.State;

import lombok.Data;

@Data
public abstract class ClsUser {
  protected long attId;
  protected String attUsername;
  protected String attFullname;
  protected String attPassword;
  protected Role attRole;
  protected State attState;

  public ClsUser(
      String prmUsername,
      String prmFullname,
      String prmPassword,
      Role prmRole,
      State prmState) {
    this.attUsername = prmUsername;
    this.attFullname = prmFullname;
    this.attPassword = prmPassword;
    this.attRole = prmRole;
    this.attState = prmState;
  }
}
