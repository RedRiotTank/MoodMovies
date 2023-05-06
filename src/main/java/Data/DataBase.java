package Data;

import backend.Movie;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends Connector {

    // constructor para conectar la bd
    public DataBase(){
        super();
    }

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
