package main;


import Data.DataBase;
import backend.Recomendator;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;



/**
 * @brief test brief
 * @note test note.
 * @author test auth.
 */
public class Main {
    public static void main(String[] args) throws IOException, SQLException, URISyntaxException {

        System.out.println("Iniciando Sistema de control MoodMovies:");

        // variables
        String year_desde, year_hasta;


        DataBase database = new DataBase();

        while(true){
            HTTPsocket http_request = new HTTPsocket();

            Recomendator recomendation = http_request.loadRecomedationInfo();

            recomendation.makeList();



            http_request.httpOUT();


        }

    }




}
