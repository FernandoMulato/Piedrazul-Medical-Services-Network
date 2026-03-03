package com.piedrazul.Domain.entities;

import com.piedrazul.Domain.enums.Profession;
import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Domain.enums.Specialty;
import com.piedrazul.Domain.enums.State;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClsClinicalStaff extends ClsUser {

  protected Profession attProfession;
  protected Specialty attSpecialty;

  public ClsClinicalStaff(
      String prmUsername,
      String prmFullname,
      String prmPassword,
      Role prmRole,
      State prmState,
      Profession prmProfession,
      Specialty prmSpecialty) {
    super(
        prmUsername,
        prmFullname,
        prmPassword,
        prmRole,
        prmState);
    this.attProfession = prmProfession;
    this.attSpecialty = prmSpecialty;
  }

}
