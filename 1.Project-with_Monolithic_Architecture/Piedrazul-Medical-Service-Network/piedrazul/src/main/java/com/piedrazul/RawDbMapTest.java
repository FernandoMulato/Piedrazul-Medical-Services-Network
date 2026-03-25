package com.piedrazul;
import java.sql.*;
import java.text.*;
import java.util.Date;
import com.piedrazul.Domain.enums.AttentionType;
import com.piedrazul.Domain.enums.AppointmentStatus;
public class RawDbMapTest {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE APP_ID=1");
            if(rs.next()) {
                 String dateStr = rs.getString("APP_DATETIME");
                 Date parsedDate;
                 try {
                     parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr);
                 } catch (Exception e) {
                     try {
                         parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                     } catch (Exception e2) {
                         throw new SQLException("Error parsing appointment date", e2);
                     }
                 }
                 System.out.println("Parsed: " + parsedDate);
                 String type = rs.getString("APP_TYPEOFCARE");
                 System.out.println("Type: " + type);
                 System.out.println("Enum Type: " + AttentionType.valueOf(type));
                 
                 String status = rs.getString("APP_STATUS");
                 System.out.println("Status: " + status);
                 System.out.println("Enum Status: " + AppointmentStatus.valueOf(status));
                 
                 System.out.println("Card: " + rs.getLong("APP_PAT_CITIZENSHIPCARD"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
