package com.piedrazul.Domain.enums;

public enum State {
  ACTIVE(1),
  INACTIVE(2);

  private final int id; //Mapeo entre enum y base de datos

  State(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public static State fromId(int id) {
    for (State state : values()) {
      if (state.id == id) {
        return state;
      }
    }
    throw new IllegalArgumentException("Estado no válido: " + id);
  }
}