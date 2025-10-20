import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class GrantPrivileges {

    public static void main(String[] args) {
        String url = "jdbc:mysql://root:fiaUVTtqFHekuNDcKJgyEHhdoLEMHhtM@interchange.proxy.rlwy.net:53530/railway";
        String user = "root";
        String password = "fiaUVTtqFHekuNDcKJgyEHhdoLEMHhtM";

        String grantSQL = "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'manager' WITH GRANT OPTION;";
        String flushSQL = "FLUSH PRIVILEGES;";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            System.out.println("Connected to Railway internal MySQL");

            stmt.executeUpdate(grantSQL);
            stmt.executeUpdate(flushSQL);

            System.out.println("Privileges granted successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
