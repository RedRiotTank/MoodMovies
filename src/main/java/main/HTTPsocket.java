package main;

import Data.MovieLoader;
import backend.Recomendator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import backend.stream;
import backend.Tag;

public class HTTPsocket {
    private final int portNumber = 8080; // Puerto en el que se va a escuchar
    private Socket clientSocket;
    private String[] formData;

    public HTTPsocket() throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Waiting for incoming connections...");

            while(formData == null){
                clientSocket = serverSocket.accept();
                formData = this.getFormData();
            }


            System.out.println("Connection accepted");
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor en el puerto " + portNumber);
            e.printStackTrace();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
    public String[] getFormData() throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        if (in.readLine().startsWith("POST")) {
            System.out.println("Processing POST request...");
            StringBuilder requestBody = new StringBuilder();
            while (in.ready()) { // Leer todo el cuerpo de la solicitud
                requestBody.append((char) in.read());
            }
            return requestBody.substring(requestBody.indexOf("\r\n\r\n") + 4).toString().split("&");
        } else if (in.readLine().startsWith("GET")) {
            System.out.println("Processing GET request...");
            System.out.printf("THIS IS AN ERROR. Program should not be receiving GET requests. FormData IS NUL");
        }

        return null;
    }

    public Recomendator loadRecomedationInfo( MovieLoader mvloader){

        String mood = formData[0].substring(formData[0].indexOf("=") + 1);
        int min_year = Integer.parseInt(formData[1].substring(formData[1].indexOf("=") + 1));
        int max_year = Integer.parseInt(formData[2].substring(formData[2].indexOf("=") + 1));
        ArrayList<stream> stream_plataforms = new ArrayList<>();

        ArrayList<Tag> yes_genres = new ArrayList<>();
        ArrayList<Tag> no_genres = new ArrayList<>();
        boolean popularity = false;

        for(int i=3; i< formData.length; i++){
            String checkBoxInfo = formData[i].substring(0, formData[i].indexOf("="));

            String value = formData[i].substring(formData[i].indexOf("=") + 1);

            switch (checkBoxInfo){
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

                case "popularity":
                    if(value.equals("yes")) popularity = true;
                    else popularity = false;



                default:

                    break;
            }
        }

        return new Recomendator(mood,min_year,max_year, stream_plataforms,yes_genres,no_genres,popularity, mvloader);
    }

    public void httpOUT() throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("");
        out.println("<html><body><h1>Hello, World!</h1></body></html>");
        out.close();
        clientSocket.close();
    }


}
