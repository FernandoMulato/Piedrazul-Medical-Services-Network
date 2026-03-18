package com.piedrazul.Infrastructure.config.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.piedrazul.Infrastructure.config.IDatabaseConnection;

public class SQLiteConnection implements IDatabaseConnection {
  private final String URL;

  public SQLiteConnection() {
      java.io.File dbFile = new java.io.File("database.db");
      if (dbFile.exists() && dbFile.length() > 8192) { 
          // SQLite headers make empty DB around 8KB sometimes, 
          // but we can just fallback to local if it seems populated.
          this.URL = "jdbc:sqlite:database.db";
      } else {
          this.URL = "jdbc:sqlite:/home/juan/repos/Piedrazul-Medical-Services-Network/1.Project-with_Monolithic_Architecture/Piedrazul-Medical-Service-Network/piedrazul/database.db";
      }
  }

  @Override
  public Connection connect() throws SQLException {
    return DriverManager.getConnection(URL);
  }
}