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

        DataBase database = new DataBase();
        MovieLoader mvloader = new MovieLoader(database);

        while(true){
            HTTPsocket http_request = new HTTPsocket();

            Recomendator recomendation = http_request.loadRecomedationInfo(mvloader);

            ArrayList<Movie> movieList = recomendation.makeList();

            /*Procesamiento de la lista de peliculas a código HTML*/

            http_request.httpOUT();


        }

    }




}
