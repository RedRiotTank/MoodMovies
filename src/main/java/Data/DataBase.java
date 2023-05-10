package Data;

import backend.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends Connector {

    // constructor para conectar la bd
    public DataBase(){
        super();
    }

    private ResultSet resultSet;
    private boolean hasResultSet;

    public void insertMovie() throws SQLException {
        //INSERCIÓN DE PRUEBA.
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Genres (id,name) VALUES(2,'TEST2JAVA')");
        stmt.execute();
    }

    public String getMovieInfo(){
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    return null;
    }

    public List<Movie> getMovieList(){

        return new ArrayList<>();
    }
}
