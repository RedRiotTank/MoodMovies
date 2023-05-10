package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    private static final String URL = "jdbc:mariadb://34.175.218.19:3306/moodmovies";
    private static final String USERNAME = "moodMovies";
    private static final String PASSWORD = "moodPassword";

    protected Connection conn;

    // el constructor directamente la conecta
    public Connector() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            this.conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Conexión exitosa de la bd");
        }catch(SQLException e){
            System.out.println(e);
        }catch(Exception e){
            System.out.println(e);
        }

    }

    public void disconnect(){
        try{
            if(conn != null && !conn.isClosed()){
                conn.close();
                System.out.println("Desconexión exitosa de la bd");
            }
        } catch (SQLException e){
            System.out.println(e);
        }
    }
}
