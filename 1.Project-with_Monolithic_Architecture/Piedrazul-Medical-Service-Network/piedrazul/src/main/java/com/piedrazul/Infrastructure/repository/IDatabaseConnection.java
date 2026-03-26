package com.piedrazul.Infrastructure.repository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Defines a contract for database connection providers.
 * <p>
 * This interface abstracts the process of establishing a connection
 * to a database, allowing different implementations (e.g., SQLite,
 * MySQL, PostgreSQL) without affecting higher-level components.
 * </p>
 *
 * <p>
 * It supports the Dependency Inversion Principle (DIP), enabling
 * the application to depend on abstractions rather than concrete
 * implementations.
 * </p>
 * 
 * @author Henry Fernando Mulato Llanten
 */
public interface IDatabaseConnection {

  /**
   * Establishes a connection to the database.
   *
   * @return a {@link Connection} object representing an active database
   *         connection
   * @throws SQLException if a database access error occurs or the connection
   *                      fails
   */
  Connection connect() throws SQLException;
}