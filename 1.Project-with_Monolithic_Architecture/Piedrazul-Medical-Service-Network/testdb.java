import java.sql.*;
import java.text.*;
import java.util.Date;

public class testdb {
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
                 String status = rs.getString("APP_STATUS");
                 System.out.println("Status: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
