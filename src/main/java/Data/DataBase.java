package Data;

import backend.Movie;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private String url;
    private String username;
    private String password;
    private ResultSet resultSet;
    private boolean hasResultSet;

    public void insertMovie(Movie movie){

    }

    public String getMovieInfo(){
        return "";
    }

    public List<Movie> getMovieList(){

        return new ArrayList<>();
    }
}
