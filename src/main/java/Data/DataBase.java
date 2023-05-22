package Data;

import backend.Movie;
import backend.Tag;
import net.officefloor.compile.section.SectionOutputType;

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

    // se obtiene el nombre de un tag segun su id
    private static String stringToTag(String str) {
        String result = null;
        switch (str) {
            case "12" -> result = Tag.ADVENTURE.name().toUpperCase();
            case "14" -> result = Tag.FANTASY.name().toUpperCase();
            case "16" -> result = Tag.ANIMATION.name().toUpperCase();
            case "18" -> result = Tag.DRAMA.name().toUpperCase();
            case "27" -> result = Tag.HORROR.name().toUpperCase();
            case "28" -> result = Tag.ACTION.name().toUpperCase();
            case "35" -> result = Tag.COMEDY.name().toUpperCase();
            case "36" -> result = Tag.HISTORY.name().toUpperCase();
            case "37" -> result = Tag.WESTERN.name().toUpperCase();
            case "53" -> result = Tag.THRILLER.name().toUpperCase();
            case "80" -> result = Tag.CRIME.name().toUpperCase();
            case "9648" -> result = Tag.MYSTERY.name().toUpperCase();
            case "10749" -> result = Tag.ROMANCE.name().toUpperCase();
            case "878" -> result = Tag.SCIENCE_FICTION.name().toUpperCase();
        }
        return result;
    }

    // obtiene el número de peliculas con los generos de la lista <generos>
    public int getNumMovies(ArrayList<String> generos) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) AS total_movies FROM Movies movies ")
                .append("JOIN Movies_Genres movies_genres ON movies.id = movies_genres.movie_id ")
                .append("JOIN Genres genres ON movies_genres.genre_id = genres.id ");

        // Construir cláusula WHERE con parámetros individuales
        if (!generos.isEmpty()) {
            queryBuilder.append("WHERE genres.name IN (");
            for (int i = 0; i < generos.size(); i++) {
                queryBuilder.append("?");
                if (i < generos.size() - 1) {
                    queryBuilder.append(", ");
                }
            }
            queryBuilder.append(")");
        }

        String query = queryBuilder.toString();
        System.out.println(query);
        PreparedStatement psmt = conn.prepareStatement(query);

        // Establecer los parámetros individuales
        for (int i = 0; i < generos.size(); i++) {
            psmt.setString(i + 1, stringToTag(generos.get(i)));
        }
        System.out.println(psmt.toString());
        ResultSet rs = psmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("total_movies");
        }

        return 0;
    }

    public ArrayList<Movie> getDataBaseRecommendedList(ArrayList<String> search_by,ArrayList<String> discard, boolean popularity_order){
        ArrayList<Movie> recommendedList = new ArrayList<>();

        try {
            // Crear la consulta SQL dinámicamente
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT DISTINCT m.id, m.title, m.year, m.popularity, m.score ")
                    .append("FROM Movies AS m ")
                    .append("INNER JOIN Movies_Genres AS mg ON m.id = mg.movie_id ")
                    .append("INNER JOIN Genres AS g ON mg.genre_id = g.id ")
                    .append("WHERE g.name IN (");

            // Agregar los géneros incluidos a la consulta
            for (int i = 0; i < search_by.size(); i++) {
                sqlBuilder.append("?");
                if (i < search_by.size() - 1) {
                    sqlBuilder.append(", ");
                }
            }
            sqlBuilder.append(") ");

            // Agregar los géneros excluidos a la consulta
            if (!discard.isEmpty()) {
                sqlBuilder.append("AND m.id NOT IN (")
                        .append("SELECT DISTINCT m2.id ")
                        .append("FROM Movies AS m2 ")
                        .append("INNER JOIN Movies_Genres AS mg2 ON m2.id = mg2.movie_id ")
                        .append("INNER JOIN Genres AS g2 ON mg2.genre_id = g2.id ")
                        .append("WHERE g2.name IN (");
                for (int i = 0; i < discard.size(); i++) {
                    sqlBuilder.append("?");
                    if (i < discard.size() - 1) {
                        sqlBuilder.append(", ");
                    }
                }

                sqlBuilder.append("))");
                if(popularity_order){
                    sqlBuilder.append("ORDER BY m.popularity DESC");
                }
                //sqlBuilder.append(" LIMIT 40");
            }

            // Crear el objeto PreparedStatement
            PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString());

            // Establecer los valores de los parámetros de los géneros incluidos
            for (int i = 0; i < search_by.size(); i++) {
                pstmt.setString(i + 1, stringToTag(search_by.get(i)));
            }

            // Establecer los valores de los parámetros de los géneros excluidos
            int paramIndex = search_by.size() + 1;
            for (int i = 0; i < discard.size(); i++) {
                pstmt.setString(paramIndex + i, stringToTag(discard.get(i)));
            }

            System.out.println(sqlBuilder);
            // Ejecutar la consulta
            ResultSet rs = pstmt.executeQuery();

            // Iterar sobre los resultados y construir las instancias de Movie
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int year = rs.getInt("year");
                double popularity = rs.getDouble("popularity");
                String score = rs.getString("score");

                // Crear una instancia de Movie y agregarla a la lista
                Movie movie = new Movie(id, title, score, year, popularity, "");
                recommendedList.add(movie);
            }

            // Cerrar la conexión y liberar recursos
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recommendedList;
    }

    private static String convertirArrayListAString(ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arrayList.size(); i++) {
            String elemento = stringToTag(arrayList.get(i));
            sb.append("'").append(elemento).append("'");

            if (i != arrayList.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

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



    public void insertMovie(int id, String title, String year, double popularity, String score) throws SQLException {
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
            statement.setString(5, score);

            statement.executeUpdate();
            System.out.println("Se ha insertado la pelicula" + title);
        } catch (SQLException e){
            e.printStackTrace();
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
