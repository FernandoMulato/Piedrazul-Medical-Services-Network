package com.piedrazul.Infrastructure.config.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.piedrazul.Infrastructure.config.IDatabaseConnection;

public class SQLiteConnection implements IDatabaseConnection {
  private static final String URL = "jdbc:sqlite:database.db";

  public SQLiteConnection() {
  }

  @Override
  public Connection connect() throws SQLException {
    return DriverManager.getConnection(URL);
  }
}