package Data;

import backend.Movie;
import backend.Tag;

import javax.xml.transform.Result;
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

    // Método que saca el nombre de un género a partir de un id
    public String getGenreNameById(int id) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT Genres.name FROM Genres WHERE id=?");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        while(rs.next()){
            return rs.getString(1);
        }

        return null;
    }

    public int getGenreIdByName(Tag name) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT Genres.id FROM Genres WHERE name=?");
        pstmt.setString(1, name.toString());
        ResultSet rs = pstmt.executeQuery();

        while(rs.next()){
            return rs.getInt(1);
        }

        return 0;
    }

    public List<Movie> getMovieList(){

        return new ArrayList<>();
    }
}
