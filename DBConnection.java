import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/crms",
                "root",
                "Harshini@2407"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

