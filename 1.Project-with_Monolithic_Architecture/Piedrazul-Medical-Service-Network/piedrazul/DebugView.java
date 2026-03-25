import java.sql.Connection;
import java.sql.DriverManager;
import com.piedrazul.Infrastructure.config.impl.SQLiteConnection;
import com.piedrazul.Infrastructure.repository.impl.ClsAppointmentRepository;
import com.piedrazul.Domain.entities.ClsAppointment;

public class DebugView {
    public static void main(String[] args) {
        try {
            SQLiteConnection conn = new SQLiteConnection();
            ClsAppointmentRepository repo = new ClsAppointmentRepository(conn);
            ClsAppointment a = repo.opView(1);
            System.out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
