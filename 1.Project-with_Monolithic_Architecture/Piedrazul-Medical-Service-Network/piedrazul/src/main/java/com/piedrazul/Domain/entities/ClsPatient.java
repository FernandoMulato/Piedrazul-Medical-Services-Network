package com.piedrazul.Domain.entities;

import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Domain.enums.State;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClsPatient extends ClsUser {

  private long attCitizenshipCard;
  private String attPhoneNumber;

  public ClsPatient(
      String prmUsername,
      String prmFullname,
      String prmPassword,
      Role prmRole,
      State prmState,
      long prmCitizenshipCard,
      String prmPhoneNumber) {
    super(
        prmUsername,
        prmFullname,
        prmPassword,
        prmRole,
        prmState);

    this.attCitizenshipCard = prmCitizenshipCard;
    this.attPhoneNumber = prmPhoneNumber;
  }

}
