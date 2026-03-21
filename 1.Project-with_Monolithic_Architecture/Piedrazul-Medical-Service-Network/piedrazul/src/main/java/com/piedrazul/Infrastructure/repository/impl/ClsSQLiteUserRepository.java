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
        INSERT INTO USER(USER_USERNAME, USER_FULLNAME, USER_PASSWORD, USER_ROLE, USER_STATE)
        VALUES (?, ?, ?, ?, ?)
        """;

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, prmUser.getAttUsername());
      pstmt.setString(2, prmUser.getAttFullname());
      pstmt.setString(3, prmUser.getAttPassword());
      pstmt.setInt(4, prmUser.getAttRole().getId());   // Aquí se da el mapeo de enum con ID
      pstmt.setInt(5, prmUser.getAttState().getId());  // Igual aquí

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
    String sql = "DELETE FROM USER WHERE USER_ID = ?";

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setLong(1, id);
      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new RuntimeException("Error eliminando usuario", e);
    }
  }

  @Override
  public boolean opUpdate(ClsUser user) {
    String sql = """
        UPDATE USER
        SET USER_USERNAME = ?, USER_FULLNAME = ?, USER_PASSWORD = ?, USER_ROLE = ?, USER_STATE = ?
        WHERE USER_ID = ?
        """;

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, user.getAttUsername());
      pstmt.setString(2, user.getAttFullname());
      pstmt.setString(3, user.getAttPassword());
      pstmt.setInt(4, user.getAttRole().getId());   // / Aquí se da el mapeo de enum con ID
      pstmt.setInt(5, user.getAttState().getId());  // Igual aquí
      pstmt.setLong(6, user.getAttId());

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new RuntimeException("Error actualizando usuario", e);
    }
  }

  @Override
  public ClsUser opGet(long id) {
    String sql = """
        SELECT U.USER_ID, U.USER_USERNAME, U.USER_FULLNAME, U.USER_PASSWORD,
        R.ROLE_TYPE, S.STATE_TYPE
        FROM USER U
        JOIN ROLE R ON U.USER_ROLE = R.ROLE_ID
        JOIN STATE S ON U.USER_STATE = S.STATE_ID
        WHERE U.USER_ID = ?
        """;

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setLong(1, id);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        ClsUser user = new ClsUser();

        user.setAttId(rs.getLong("USER_ID"));
        user.setAttUsername(rs.getString("USER_USERNAME"));
        user.setAttFullname(rs.getString("USER_FULLNAME"));
        user.setAttPassword(rs.getString("USER_PASSWORD"));
        user.setAttRole(Role.valueOf(rs.getString("ROLE_TYPE")));
        user.setAttState(State.valueOf(rs.getString("STATE_TYPE")));

        return user;
      }

      return null;

    } catch (SQLException e) {
      throw new RuntimeException("Error obteniendo usuario", e);
    }
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
  public Role opVerifyUser(String username, String password) { // Verificamos el usuario con su contraseña 
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
        throw new RuntimeException("Usuario o contraseña incorrectos");
      }

      String roleType = rs.getString("ROLE_TYPE");
      String stateType = rs.getString("STATE_TYPE");

      if (!"ACTIVE".equals(stateType)) {
        throw new RuntimeException("Usuario bloqueado");
      }

      return Role.valueOf(roleType); 

    } catch (SQLException e) {
      throw new RuntimeException("Error verificando usuario", e);
    }
  }

  /**
   * Retrieves all users from the database.
   *
   * @return list of users
   * @throws RuntimeException if a database error occurs
   */
  @Override
  public List<ClsUser> opFindAll() { // Optenemos la lista completa de todos los usuarios

    String sql = """
        SELECT U.USER_ID, U.USER_USERNAME, U.USER_FULLNAME,
        R.ROLE_TYPE, S.STATE_TYPE
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

        user.setAttId(rs.getLong("USER_ID"));
        user.setAttUsername(rs.getString("USER_USERNAME"));
        user.setAttFullname(rs.getString("USER_FULLNAME"));
        user.setAttRole(Role.valueOf(rs.getString("ROLE_TYPE")));
        user.setAttState(State.valueOf(rs.getString("STATE_TYPE")));

        users.add(user);
      }

      return users;

    } catch (SQLException e) {
      throw new RuntimeException("Error obteniendo usuarios", e);
    }
  }
}