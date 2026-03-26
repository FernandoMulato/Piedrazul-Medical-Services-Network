package com.piedrazul.Infrastructure.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Infrastructure.config.IDatabaseConnection;
import com.piedrazul.Infrastructure.repository.IUserRepository;
import com.piedrazul.Domain.enums.Role;
import com.piedrazul.Domain.enums.State;

import lombok.RequiredArgsConstructor;

/**
 * SQLite implementation of the {@link IUserRepository} interface.
 * <p>
 * This class provides persistence operations for {@link ClsUser} entities
 * using JDBC and a SQLite database. It encapsulates all database access
 * logic related to users.
 * </p>
 *
 * <p>
 * It relies on {@link IDatabaseConnection} to obtain database connections,
 * promoting loose coupling and adherence to the Dependency Inversion Principle
 * (DIP).
 * </p>
 *
 * <p>
 * This implementation uses prepared statements to prevent SQL injection
 * and ensure safe query execution.
 * </p>
 * 
 * @author Henry Fernando Mulato Llanten
 */
@RequiredArgsConstructor
public class ClsSQLiteUserRepository implements IUserRepository {

  /**
   * Database connection provider.
   */
  private final IDatabaseConnection databaseConnection;

  /**
   * Persists a new user in the database.
   *
   * @param prmUser the user to be stored
   * @return the persisted user with generated ID, or {@code null} if the
   *         operation fails
   * @throws RuntimeException if a database error occurs
   */
  @Override
  public ClsUser opSave(ClsUser prmUser) {
    String sql = """
        INSERT INTO TBL_USER(user_username, user_email, user_password, user_role, user_profession)
        VALUES (?, ?, ?, ?, ?)
        """;

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, prmUser.getAttUsername());
      pstmt.setString(2, prmUser.getAttPassword());

      int rowsAffected = pstmt.executeUpdate();

      if (rowsAffected > 0) {
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
          prmUser.setAttId(rs.getLong(1));
        }
        return prmUser;
      }

      return null;

    } catch (SQLException e) {
      throw new RuntimeException("Error saving user", e);
    }
  }

  @Override
  public boolean opDelete(long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'opDelete'");
  }

  @Override
  public boolean opUpdate(ClsUser user) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'opUpdate'");
  }

  @Override
  public ClsUser opGet(long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'opGet'");
  }

  /**
   * Verifies user credentials and returns the associated role.
   * <p>
   * This method performs authentication by validating the username
   * and password against the database.
   * </p>
   *
   * <p>
   * If the user exists and is active, the corresponding {@link Role}
   * is returned. Otherwise, a runtime exception is thrown.
   * </p>
   *
   * @param username user's username
   * @param password user's password
   * @return the role associated with the authenticated user
   * @throws RuntimeException if credentials are invalid, user is blocked,
   *                          or a database error occurs
   */
  @Override
  public Role opVerifyUser(String username, String password) throws RuntimeException {

    String sql = """
        SELECT R.ROLE_TYPE, S.STATE_TYPE
        FROM USER U
        JOIN ROLE R ON U.USER_ROLE = R.ROLE_ID
        JOIN STATE S ON U.USER_STATE = S.STATE_ID
        WHERE U.USER_USERNAME = ? AND U.USER_PASSWORD = ?
        """;

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, username);
      pstmt.setString(2, password);

      ResultSet rs = pstmt.executeQuery();

      if (!rs.next()) {
        throw new RuntimeException(
            "Usuario o contraseña incorrectos. El usuario no existe en la base de datos.");
      }

      String roleType = rs.getString("ROLE_TYPE");
      String stateType = rs.getString("STATE_TYPE");

      if ("ACTIVE".equals(stateType)) {
        switch (roleType) {
          case "ADMINISTRATOR":
            return Role.ADMINISTRATOR;
          case "PATIENT":
            return Role.PATIENT;
          case "CLINICALSTAFF":
            return Role.CLINICALSTAFF;
          case "APPOINTMENTMANAGER":
            return Role.APPOINTMENTMANAGER;
        }
      } else {
        // La excepción debe ser para un caso especifico (MODIFICAR)
        throw new RuntimeException(
            "Acceso denegado - Usuario BLOQUEADO");
      }

      return null;

    } catch (SQLException e) {
      throw new RuntimeException("Error verificando el usuario. 500", e);
    }
  }

  /**
   * Retrieves all users from the database.
   *
   * @return list of users
   * @throws RuntimeException if a database error occurs
   */
  @Override
  public List<ClsUser> opFindAll() {

    String sql = """
        SELECT  R.ROLE_TYPE, U.USER_USERNAME, U.USER_FULLNAME, S.STATE_TYPE
        FROM USER U
        JOIN ROLE R ON U.USER_ROLE = R.ROLE_ID
        JOIN STATE S ON U.USER_STATE = S.STATE_ID
        """;

    List<ClsUser> users = new ArrayList<>();

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {

      while (rs.next()) {
        ClsUser user = new ClsUser();

        user.setAttRole(Role.valueOf(rs.getString("ROLE_TYPE")));
        user.setAttUsername(rs.getString("USER_USERNAME"));
        user.setAttFullname(rs.getString("USER_FULLNAME"));
        user.setAttState(State.valueOf(rs.getString("STATE_TYPE")));

        users.add(user);
      }

      return users;

    } catch (SQLException e) {
      throw new RuntimeException("Error obteniendo los usuarios", e);
    }
  }
}
