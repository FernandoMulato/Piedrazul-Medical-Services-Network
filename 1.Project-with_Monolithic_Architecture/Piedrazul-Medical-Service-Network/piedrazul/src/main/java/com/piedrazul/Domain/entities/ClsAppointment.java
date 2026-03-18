package com.piedrazul.Domain.entities;

import java.util.Date;

import com.piedrazul.Domain.enums.AppointmentStatus;
import com.piedrazul.Domain.enums.AttentionType;

import lombok.Data;

@Data
public class ClsAppointment {
  private long attId;
  private long attCitizenshipCardPatient;
  private String attPhoneNumber;
  private long attMedicalStaffId;
  private Date attDateAndTime;
  private AttentionType attAttentionType;
  private AppointmentStatus attStatus;
  private String attReason;

  public ClsAppointment(
      long prmCitizenshipCardPatient,
      String prmPhoneNumber,
      long prmMedicalStaffId,
      Date prmDateAndTime,
      AttentionType prmAttentionType,
      String prmReason) {
    this.attCitizenshipCardPatient = prmCitizenshipCardPatient;
    this.attPhoneNumber = prmPhoneNumber;
    this.attMedicalStaffId = prmMedicalStaffId;
    this.attDateAndTime = prmDateAndTime;
    this.attAttentionType = prmAttentionType;
    this.attStatus = AppointmentStatus.SCHEDULED;
    this.attReason = prmReason;
  }
}
