package com.piedrazul.Infrastructure.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Infrastructure.config.IDatabaseConnection;
import com.piedrazul.Infrastructure.repository.IUserRepository;
import com.piedrazul.Domain.enums.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClsSQLiteUserRepository implements IUserRepository {
  private final IDatabaseConnection databaseConnection;

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

      if ("ACTIVE".equals(stateType) && "ADMINISTRATOR".equals(roleType)) {
        return Role.Administrator;
      }

      return null;

    } catch (SQLException e) {
      throw new RuntimeException("Error verificando el usuario", e);
    }
  }
}
