package com.piedrazul.Infrastructure.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.piedrazul.Domain.entities.ClsAppointment;
import com.piedrazul.Domain.enums.AppointmentStatus;
import com.piedrazul.Domain.enums.AttentionType;
import com.piedrazul.Infrastructure.repository.IAppointmentRepository;
import com.piedrazul.Infrastructure.repository.IDatabaseConnection;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClsAppointmentRepository implements IAppointmentRepository {
  private final IDatabaseConnection databaseConnection;

  @Override
  public ClsAppointment opCreate(ClsAppointment appointment) {
    String sql = """
        INSERT INTO APPOINTMENT(APP_PAT_CITIZENSHIPCARD, APP_PAT_PHONENUMBER, APP_MED_ID, APP_DATETIME, APP_TYPEOFCARE, APP_STATUS, APP_REASON)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setLong(1, appointment.getAttCitizenshipCardPatient());
      pstmt.setString(2, appointment.getAttPhoneNumber());
      pstmt.setLong(3, appointment.getAttMedicalStaffId());
      pstmt.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(appointment.getAttDateAndTime()));
      pstmt.setString(5, appointment.getAttAttentionType().name());
      pstmt.setString(6, appointment.getAttStatus().name());
      pstmt.setString(7, appointment.getAttReason());

      int rowsAffected = pstmt.executeUpdate();

      if (rowsAffected > 0) {
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
          appointment.setAttId(rs.getLong(1));
        }
        return appointment;
      }

      return null;

    } catch (SQLException e) {
      throw new RuntimeException("Error saving appointment", e);
    }
  }

  @Override
  public boolean opUpdate(ClsAppointment appointment) {
    String sql = """
        UPDATE APPOINTMENT
        SET APP_PAT_PHONENUMBER = ?, APP_MED_ID = ?, APP_DATETIME = ?, APP_TYPEOFCARE = ?, APP_STATUS = ?, APP_REASON = ?
        WHERE APP_ID = ?
        """;

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, appointment.getAttPhoneNumber());
      pstmt.setLong(2, appointment.getAttMedicalStaffId());
      pstmt.setString(3, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(appointment.getAttDateAndTime()));
      pstmt.setString(4, appointment.getAttAttentionType().name());
      pstmt.setString(5, appointment.getAttStatus().name());
      pstmt.setString(6, appointment.getAttReason());
      pstmt.setLong(7, appointment.getAttId());

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new RuntimeException("Error updating appointment", e);
    }
  }

  @Override
  public ClsAppointment opView(long id) {
    String sql = "SELECT * FROM APPOINTMENT WHERE APP_ID = ?";

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setLong(1, id);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return mapResultSetToAppointment(rs);
      }

      return null;

    } catch (SQLException e) {
      throw new RuntimeException("Error fetching appointment", e);
    }
  }

  @Override
  public boolean opCheckScheduleConflict(long medId, String dateTime) {
      String sql = "SELECT COUNT(*) FROM APPOINTMENT WHERE APP_MED_ID = ? AND APP_DATETIME = ? AND APP_STATUS != 'CANCELLED'";

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setLong(1, medId);
      pstmt.setString(2, dateTime);

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) > 0;
      }

      return false;

    } catch (SQLException e) {
      throw new RuntimeException("Error checking schedule conflict", e);
    }
  }

  @Override
  public List<ClsAppointment> opListAll() {
    String sql = "SELECT * FROM APPOINTMENT ORDER BY APP_DATETIME DESC";
    List<ClsAppointment> appointments = new ArrayList<>();

    try (Connection conn = databaseConnection.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        appointments.add(mapResultSetToAppointment(rs));
      }

      return appointments;

    } catch (SQLException e) {
      throw new RuntimeException("Error listing appointments", e);
    }
  }

  private ClsAppointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
    Date parsedDate;
    String dateStr = rs.getString("APP_DATETIME");
    try {
        parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr);
    } catch (ParseException e) {
        try {
            parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e2) {
            throw new SQLException("Error parsing appointment date", e2);
        }
    }
    
    ClsAppointment appointment = new ClsAppointment(
        rs.getLong("APP_PAT_CITIZENSHIPCARD"),
        rs.getString("APP_PAT_PHONENUMBER"),
        rs.getLong("APP_MED_ID"),
        parsedDate,
        AttentionType.valueOf(rs.getString("APP_TYPEOFCARE")),
        rs.getString("APP_REASON")
    );
    appointment.setAttId(rs.getLong("APP_ID"));
    appointment.setAttStatus(AppointmentStatus.valueOf(rs.getString("APP_STATUS")));
    return appointment;
  }
}