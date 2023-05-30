package main;


import Data.DataBase;
import Data.MovieLoader;
import backend.Movie;
import backend.Recomendator;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * @brief test brief
 * @note test note.
 * @author test auth.
 */
public class Main {
    public static void main(String[] args) throws IOException, SQLException, URISyntaxException {

        System.out.println("Iniciando Sistema de control MoodMovies:");

        DataBase database = null;
        MovieLoader mvloader = null;

        while(true){
            HTTPsocket http_request = new HTTPsocket();

            database = new DataBase();
            mvloader = new MovieLoader(database);
            Recomendator recomendation = http_request.loadRecomedationInfo(mvloader);
            ArrayList<Movie> movieList = recomendation.makeList(mvloader);

            Boolean found_movies ;
            if(movieList == null){
                found_movies = false;
                System.out.println("no found movies");
            } else {
                found_movies = true;
                http_request.createJsonFile(movieList);
            }


            http_request.httpOUT(found_movies);
            mvloader.getDb().disconnect();
        }

    }




}
