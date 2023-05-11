package main;

import Data.Connector;
import Data.DataBase;
import backend.Mood;
import backend.Recomendator;
import backend.Tag;
import backend.stream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

import Data.MovieLoader;
import netscape.javascript.JSObject;


/**
 * @brief test brief
 * @note test note.
 * @author test auth.
 */
public class Main {
    public static void main(String[] args) throws SQLException, URISyntaxException {
        System.out.println("Iniciando Sistema de control MoodMovies:");

        // variables
        String year_desde, year_hasta;

        int portNumber = 8080; // Puerto en el que se va a escuchar

        DataBase database = new DataBase();
        //database.insertMovie();
        String genre_name = database.getGenreNameById(10749);
        System.out.println("Genero con id 14: " + genre_name);
        System.out.println("Genero con name ACTION: " + database.getGenreIdByName(Tag.ACTION));
        MovieLoader movieLoader = new MovieLoader();
        try{
            ArrayList<Integer> generos = new ArrayList<>();
            movieLoader.discoverMoviesWith(Mood.GOOD,"1999","2010", generos);
        } catch (URISyntaxException e) {
            throw new URISyntaxException("ee", "ee");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Servidor iniciado. Esperando conexiones entrantes...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;

                    if (in.readLine().startsWith("POST")) {
                        System.out.println("Processing POST request...");
                        StringBuilder requestBody = new StringBuilder();
                        while (in.ready()) { // Leer todo el cuerpo de la solicitud
                            requestBody.append((char) in.read());
                        }

                        String[] formData = requestBody.substring(requestBody.indexOf("\r\n\r\n") + 4).toString().split("&");

                        String mood = formData[0].substring(formData[0].indexOf("=") + 1);
                        int min_year = Integer.parseInt(formData[1].substring(formData[1].indexOf("=") + 1));
                        int max_year = Integer.parseInt(formData[2].substring(formData[2].indexOf("=") + 1));
                        ArrayList<stream> stream_plataforms = new ArrayList<>();

                        ArrayList<Tag> yes_genres = new ArrayList<>();
                        ArrayList<Tag> no_genres = new ArrayList<>();

                        for(int i=3; i< formData.length; i++){
                            String a = formData[i].substring(0, formData[i].indexOf("="));
                            String value = formData[i].substring(formData[i].indexOf("=") + 1);
                            System.out.println(a);
                            switch (a){
                                case "prime":
                                    stream_plataforms.add(stream.PRIME);
                                    break;

                                case "netflix":
                                    stream_plataforms.add(stream.NETFLIX);
                                    break;

                                case "itunes":
                                    stream_plataforms.add(stream.ITUNES);
                                    break;

                                case "hbo":
                                    stream_plataforms.add(stream.HBO);
                                    break;

                                case "disney":
                                    stream_plataforms.add(stream.DISNEY);
                                    break;

                                case "youtube":
                                    stream_plataforms.add(stream.YOUTUBE);
                                    break;

                                case "crunchyroll":
                                    stream_plataforms.add(stream.CRUNCHYROLL);
                                    break;

                                case "action":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.ACTION);
                                    else
                                        no_genres.add(Tag.ACTION);
                                    break;

                                case "animation":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.ANIMATION);
                                    else
                                        no_genres.add(Tag.ANIMATION);
                                    break;

                                case "adventures":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.ADVENTURE);
                                    else
                                        no_genres.add(Tag.ADVENTURE);
                                    break;

                                case "comedy":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.COMEDY);
                                    else
                                        no_genres.add(Tag.COMEDY);
                                    break;

                                case "drama":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.DRAMA);
                                    else
                                        no_genres.add(Tag.DRAMA);
                                    break;

                                case "fantasy":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.FANTASY);
                                    else
                                        no_genres.add(Tag.FANTASY);
                                    break;

                                case "horror":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.HORROR);
                                    else
                                        no_genres.add(Tag.HORROR);
                                    break;

                                case "mistery":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.MYSTERY);
                                    else
                                        no_genres.add(Tag.MYSTERY);
                                    break;

                                case "thriller":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.THRILLER);
                                    else
                                        no_genres.add(Tag.THRILLER);
                                    break;

                                case "western":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.WESTERN);
                                    else
                                        no_genres.add(Tag.WESTERN);
                                    break;

                                case "crime":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.CRIME);
                                    else
                                        no_genres.add(Tag.CRIME);
                                    break;

                                case "romance":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.ROMANCE);
                                    else
                                        no_genres.add(Tag.ROMANCE);
                                    break;

                                case "science_fiction":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.SCIENCE_FICTION);
                                    else
                                        no_genres.add(Tag.SCIENCE_FICTION);
                                    break;

                                case "history":
                                    if(value.equals("yes"))
                                        yes_genres.add(Tag.HISTORY);
                                    else
                                        no_genres.add(Tag.HISTORY);
                                    break;

                                default:
                                    //stream_plataforms.add(stream.HBO);
                                    break;
                            }
                        }



                        Recomendator recomendation = new Recomendator(mood,min_year,max_year, stream_plataforms,yes_genres,no_genres);
                        recomendation.makeList();

                    }
                    else if (in.readLine().startsWith("GET")) {
                        System.out.println("Processing GET request...");
                    }


                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println("");
                out.println("<html><body><h1>Hello, World!</h1></body></html>");

                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor en el puerto " + portNumber);
            e.printStackTrace();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}
