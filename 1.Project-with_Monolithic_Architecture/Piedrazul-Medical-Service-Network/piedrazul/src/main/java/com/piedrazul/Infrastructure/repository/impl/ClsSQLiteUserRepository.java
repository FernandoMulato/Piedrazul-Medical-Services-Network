package com.piedrazul.Infrastructure.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.piedrazul.Domain.entities.ClsClinicalStaff;
import com.piedrazul.Domain.entities.ClsPatient;
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
        String sqlUser = """
                INSERT INTO USER(USER_USERNAME, USER_FULLNAME, USER_PASSWORD, USER_ROLE, USER_STATE)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConnection.connect()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, prmUser.getAttUsername());
                pstmt.setString(2, prmUser.getAttFullname());
                pstmt.setString(3, prmUser.getAttPassword());
                pstmt.setInt(4, prmUser.getAttRole().getId());
                pstmt.setInt(5, prmUser.getAttState().getId());

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected <= 0) {
                    conn.rollback();
                    return null;
                }

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    prmUser.setAttId(rs.getLong(1));
                }

                if (prmUser instanceof ClsPatient patient) {
                    savePatient(conn, patient);
                } else if (prmUser instanceof ClsClinicalStaff staff) {
                    saveMedicalStaff(conn, staff);
                }

                conn.commit();
                return prmUser;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error saving user", e);
        }
    }

    private void savePatient(Connection conn, ClsPatient patient) throws SQLException {
        String sql = """
                INSERT INTO PATIENT(PAT_ID, PAT_CITIZENSHIPCARD, PAT_PHONENUMBER)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, patient.getAttId());
            pstmt.setLong(2, patient.getAttCitizenshipCard());
            pstmt.setString(3, patient.getAttPhoneNumber());

            pstmt.executeUpdate();
        }
    }

    private void saveMedicalStaff(Connection conn, ClsClinicalStaff staff) throws SQLException {
        String sql = """
                INSERT INTO MEDICALSTAFF(MED_ID, MED_PROFESSION, MED_SPECIALITY)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, staff.getAttId());
            pstmt.setString(2, staff.getAttProfession().name());
            pstmt.setString(3, staff.getAttSpecialty().name());

            pstmt.executeUpdate();
        }
    }

    private void updatePatient(Connection conn, ClsPatient patient) throws SQLException {

        String sql = """
                INSERT OR REPLACE INTO PATIENT(PAT_ID, PAT_CITIZENSHIPCARD, PAT_PHONENUMBER)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, patient.getAttId());
            pstmt.setLong(2, patient.getAttCitizenshipCard());
            pstmt.setString(3, patient.getAttPhoneNumber());

            pstmt.executeUpdate();
        }
    }

    private void updateMedicalStaff(Connection conn, ClsClinicalStaff staff) throws SQLException {

        String sql = """
                INSERT OR REPLACE INTO MEDICALSTAFF(MED_ID, MED_PROFESSION, MED_SPECIALITY)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, staff.getAttId());
            pstmt.setString(2, staff.getAttProfession().name());
            pstmt.setString(3, staff.getAttSpecialty().name());

            pstmt.executeUpdate();
        }
    }

    @Override
    public boolean opDeactivate(long id) {
        return opUpdateState(id, State.INACTIVE.getId());
    }

    @Override
    public boolean opExistsByUsername(String username) {
        String sql = """
                SELECT 1
                FROM USER
                WHERE USER_USERNAME = ?
                LIMIT 1
                """;

        try (Connection conn = databaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error verificando si el nombre de usuario existe", e);
        }
    }

    @Override
    public boolean opUpdate(ClsUser user) {

        String sqlUser = """
                UPDATE USER
                SET USER_USERNAME = ?, USER_FULLNAME = ?, USER_PASSWORD = ?, USER_ROLE = ?, USER_STATE = ?
                WHERE USER_ID = ?
                """;

        try (Connection conn = databaseConnection.connect()) {

            conn.setAutoCommit(false); // 🔥 INICIO TRANSACCIÓN

            try (PreparedStatement pstmt = conn.prepareStatement(sqlUser)) {

                // --- actualizar tabla USER ---
                pstmt.setString(1, user.getAttUsername());
                pstmt.setString(2, user.getAttFullname());
                pstmt.setString(3, user.getAttPassword());
                pstmt.setInt(4, user.getAttRole().getId());
                pstmt.setInt(5, user.getAttState().getId());
                pstmt.setLong(6, user.getAttId());

                int rows = pstmt.executeUpdate();

                if (rows <= 0) {
                    conn.rollback();
                    return false;
                }

                // --- si es PACIENTE ---
                if (user instanceof ClsPatient patient) {
                    updatePatient(conn, patient);
                }

                // --- si es PERSONAL MEDICO ---
                else if (user instanceof ClsClinicalStaff staff) {
                    updateMedicalStaff(conn, staff);
                }

                conn.commit(); // 🔥 TODO OK
                return true;

            } catch (Exception e) {
                conn.rollback(); // 🔥 ERROR → REVERSA TODO
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error actualizando usuario", e);
        }
    }

    @Override
    public boolean opUpdateState(long id, int stateId) {
        String sql = """
                UPDATE USER
                SET USER_STATE = ?
                WHERE USER_ID = ?
                """;

        try (Connection conn = databaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stateId);
            pstmt.setLong(2, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando el estado del usuario", e);
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
    public Role opVerifyUser(String username, String password) {

        String sql = """
                SELECT R.ROLE_TYPE, S.STATE_TYPE
                FROM USER U
                JOIN ROLE R ON U.USER_ROLE = R.ROLE_ID
                JOIN STATE S ON U.USER_STATE = S.STATE_ID
                WHERE U.USER_USERNAME = ?
                  AND U.USER_PASSWORD = ?
                  AND S.STATE_TYPE = 'ACTIVE'
                """;

        try (Connection conn = databaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Usuario o contraseña incorrectos. El usuario no existe en la base de datos.");
            }

            String roleType = rs.getString("ROLE_TYPE");

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

    @Override
    public boolean opExistsByUsernameExcludingId(String username, long id) {
        String sql = """
                SELECT 1
                FROM USER
                WHERE USER_USERNAME = ?
                  AND USER_ID != ?
                LIMIT 1
                """;

        try (Connection conn = databaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setLong(2, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error verificando si el nombre de usuario existe", e);
        }
    }

    @Override
    public boolean opDelete(long id) {
    throw new UnsupportedOperationException("La eliminación no está soportada. Use desactivación.");
    }
}