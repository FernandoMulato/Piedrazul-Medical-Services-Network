package com.piedrazul.Infrastructure.config.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.piedrazul.Infrastructure.config.IDatabaseConnection;

/**
 * SQLite implementation of the {@link IDatabaseConnection} interface.
 * <p>
 * This class is responsible for establishing a connection to a SQLite
 * database using JDBC. It encapsulates the database connection details,
 * promoting separation of concerns and enabling flexibility in switching
 * database providers.
 * </p>
 *
 * <p>
 * The database is accessed through a local file defined by the JDBC URL.
 * </p>
 * 
 * @author Henry Fernando Mulato Llanten
 */
public class SQLiteConnection implements IDatabaseConnection {

  /**
   * JDBC connection URL for the SQLite database.
   */
  // private static final String URL = "jdbc:sqlite:database.db";
  private static final String URL = "jdbc:sqlite:E:/Semestre ##6/Software Engineering ll/Proyecto_final_piedra_azul/Piedrazul-Medical-Services-Network/1.Project-with_Monolithic_Architecture/Piedrazul-Medical-Service-Network/piedrazul/database.db";

  /**
   * Default constructor.
   */
  public SQLiteConnection() {
  }

  /**
   * Establishes a connection to the SQLite database.
   *
   * @return a {@link Connection} object representing the database connection
   * @throws SQLException if a database access error occurs or the connection
   *                      fails
   */
  @Override
  public Connection connect() throws SQLException {
    return DriverManager.getConnection(URL);
  }
}