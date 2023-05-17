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

    public void insertMovieGenre(int movieId, int genreId) {
        // Insertar la tupla en la tabla Movies_Genres
        String insertQuery = "INSERT INTO Movies_Genres (movie_id, genre_id) VALUES (?, ?)";
        try {
            try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
                statement.setInt(1, movieId);
                statement.setInt(2, genreId);

                statement.executeUpdate();
            }
            System.out.println("La tupla ha sido insertada en la tabla Movies_Genres.");
        } catch (SQLException e) {
            System.out.println("No se pudo insertar la tupla en la tabla Movies_Genres: " + e.getMessage());
        }
    }



    public void insertMovie(int id, String title, String year, double popularity, double score) throws SQLException {
        // Verificar si la ID ya existe en la base de datos
        String checkQuery = "SELECT COUNT(*) FROM Movies WHERE id = ?";
        try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, id);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    System.out.println("La ID ya existe en la base de datos. No se insertará la tupla.");
                    return; // Salir del método
                }
            }
        }

        // Insertar la tupla en la base de datos
        String insertQuery = "INSERT INTO Movies (id, title, year, popularity, score) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
            statement.setInt(1, id);
            statement.setString(2, title);
            statement.setString(3, year);
            statement.setDouble(4, popularity);
            statement.setDouble(5, score);

            statement.executeUpdate();
        }
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
    // Método que saca el id de un género a partir de un nombre
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
