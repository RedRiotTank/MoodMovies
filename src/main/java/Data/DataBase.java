package Data;

import backend.Movie;
import backend.Tag;
import java.sql.*;
import java.util.ArrayList;

public class DataBase extends Connector {

    // constructor para conectar la bd
    public DataBase(){
        super();
    }
    // se obtiene el nombre de un tag segun su id
    private static String stringToTag(String str) {
        String result = null;
        switch (str) {
            case "12":
                result = Tag.ADVENTURE.name().toUpperCase();
                break;
            case "14":
                result = Tag.FANTASY.name().toUpperCase();
                break;
            case "16":
                result = Tag.ANIMATION.name().toUpperCase();
                break;
            case "18":
                result = Tag.DRAMA.name().toUpperCase();
                break;
            case "27":
                result = Tag.HORROR.name().toUpperCase();
                break;
            case "28":
                result = Tag.ACTION.name().toUpperCase();
                break;
            case "35":
                result = Tag.COMEDY.name().toUpperCase();
                break;
            case "36":
                result = Tag.HISTORY.name().toUpperCase();
                break;
            case "37":
                result = Tag.WESTERN.name().toUpperCase();
                break;
            case "53":
                result = Tag.THRILLER.name().toUpperCase();
                break;
            case "80":
                result = Tag.CRIME.name().toUpperCase();
                break;
            case "9648":
                result = Tag.MYSTERY.name().toUpperCase();
                break;
            case "10749":
                result = Tag.ROMANCE.name().toUpperCase();
                break;
            case "878":
                result = Tag.SCIENCE_FICTION.name().toUpperCase();
                break;
            default:
                // Opción por defecto si ninguna de las anteriores coincide
                // Puedes asignar un valor apropiado o lanzar una excepción, según sea necesario
                break;
        }

        return result;
    }

    public ArrayList<Movie> getDataBaseRecommendedList(int minYear, int maxYear,ArrayList<String> search_by,ArrayList<String> discard, boolean popularity_order){
        ArrayList<Movie> recommendedList = new ArrayList<>();
        if(search_by.isEmpty())
            return null;

        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT DISTINCT m.id, m.title, m.year, m.popularity, m.score_mc, m.score_rt, m.description ")
                    .append("FROM Movies AS m ")
                    .append("INNER JOIN Movies_Genres AS mg ON m.id = mg.movie_id ")
                    .append("INNER JOIN Genres AS g ON mg.genre_id = g.id ")
                    .append("WHERE g.name IN (");

            for (int i = 0; i < search_by.size(); i++) {
                sqlBuilder.append("?");
                if (i < search_by.size() - 1) {
                    sqlBuilder.append(", ");
                }
            }
            sqlBuilder.append(")  AND m.year >= '"+minYear+"' AND m.year <= '"+maxYear+"'");

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
            }

            if(popularity_order){
                sqlBuilder.append("ORDER BY m.popularity DESC");
            } else {
                sqlBuilder.append("ORDER BY RAND()");
            }
            sqlBuilder.append(" LIMIT 20");

            System.out.println("consulta lista recomendada: " + sqlBuilder);

            PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString());

            for (int i = 0; i < search_by.size(); i++) {
                pstmt.setString(i + 1, stringToTag(search_by.get(i)));
            }

            int paramIndex = search_by.size() + 1;
            for (int i = 0; i < discard.size(); i++) {
                pstmt.setString(paramIndex + i, stringToTag(discard.get(i)));
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int year = rs.getInt("year");
                double popularity = rs.getDouble("popularity");
                String score_mc = rs.getString("score_mc");
                String score_rt = rs.getString("score_rt");
                String description = rs.getString("description");
                Movie movie = new Movie(id, title, score_mc, score_rt, year, popularity, "", description);
                recommendedList.add(movie);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recommendedList;
    }

    public int getNumDataBaseMovies(int minYear, int maxYear,ArrayList<String> search_by,ArrayList<String> discard, boolean popularity_order){
        if(search_by.isEmpty())
            return -1;

        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT COUNT(DISTINCT m.id) AS total_movies ")
                    .append("FROM Movies AS m ")
                    .append("INNER JOIN Movies_Genres AS mg ON m.id = mg.movie_id ")
                    .append("INNER JOIN Genres AS g ON mg.genre_id = g.id ")
                    .append("WHERE g.name IN (");

            for (int i = 0; i < search_by.size(); i++) {
                sqlBuilder.append("?");
                if (i < search_by.size() - 1) {
                    sqlBuilder.append(", ");
                }
            }
            sqlBuilder.append(") AND m.year >= '"+minYear+"' AND m.year <= '"+maxYear+"' ");

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
            }
            System.out.println(sqlBuilder);

            PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString());

            for (int i = 0; i < search_by.size(); i++) {
                pstmt.setString(i + 1, stringToTag(search_by.get(i)));
            }

            int paramIndex = search_by.size() + 1;
            for (int i = 0; i < discard.size(); i++) {
                pstmt.setString(paramIndex + i, stringToTag(discard.get(i)));
            }

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total_movies");
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public void insertMovieGenre(int movieId, int genreId) {
        String insertQuery = "INSERT INTO Movies_Genres (movie_id, genre_id) VALUES (?, ?)";
        try {
            try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
                statement.setInt(1, movieId);
                statement.setInt(2, genreId);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("-----No se pudo insertar la tupla en la tabla Movies_Genres: " + e.getMessage() + "-----");
        }
    }

    public void insertMovie(int id, String title, String year, double popularity, String description, String score_mc, String score_rt) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM Movies WHERE id = ?";
        try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, id);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    System.out.println("-----La ID ya existe en la base de datos. No se insertará la tupla.-----");
                    return;
                }
            }
        }

        String insertQuery = "INSERT INTO Movies (id, title, year, popularity, description, score_mc, score_rt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
            statement.setInt(1, id);
            statement.setString(2, title);
            statement.setString(3, year);
            statement.setDouble(4, popularity);
            statement.setString(5, description);
            statement.setString(6, score_mc);
            statement.setString(7, score_rt);

            System.out.println("consulta insercion: " + insertQuery);
            statement.executeUpdate();
            System.out.println("----Se ha insertado la pelicula " + title + "------");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
