package com.piedrazul;

import java.sql.SQLException;

import com.piedrazul.Infrastructure.config.IDatabaseConnection;
import com.piedrazul.Infrastructure.config.impl.SQLiteConnection;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) throws SQLException {
    IDatabaseConnection databaseConnection = new SQLiteConnection();
    databaseConnection.connect(); 
  }
}
