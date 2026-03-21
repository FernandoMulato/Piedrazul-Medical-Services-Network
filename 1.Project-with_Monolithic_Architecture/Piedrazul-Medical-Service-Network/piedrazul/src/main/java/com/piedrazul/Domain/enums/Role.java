package com.piedrazul.Domain.enums;

public enum Role {
  ADMINISTRATOR(1),
  PATIENT(2),
  CLINICALSTAFF(3),
  APPOINTMENTMANAGER(4); //Mapeo entre roles enum y base de datos

  private final int id;

  Role(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public static Role fromId(int id) {
    for (Role role : values()) {
      if (role.id == id) {
        return role;
      }
    }
    throw new IllegalArgumentException("Rol no válido: " + id);
  }
}