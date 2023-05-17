package Data;



import backend.Mood;
import backend.Tag;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import backend.Mood;

public class MovieLoader {

    DataBase db = null;

    public MovieLoader(DataBase db){
        this.db = db;

    }


    private static String API_KEY = "api_key=e37359bf96dd4a19ed90f8d427112595";
    /* DOCUMENTACION API (método discover)
    --------------------------------------------------------------
     * language: indica en que idioma queremos que salga el json
     * sort_by: orden en el que vienen los datos (solo uno de los siguientes) {
     *   popularity.asc, popularity.desc, release_date.asc, release_date.desc,
     *   primary_release_date.asc, primary_release_date.desc, vote_average.asc, vote_average.desc,
     *   vote_count.asc, vote_count.desc
     * }
     *
     * certification: para restrictir las peliculas que salgan (p.e. que no sean violentas)
     * include_adult: false
     * include_video: false
     * page : Página que queremos que muestre, nos va a servir para sacar películas distintas cada vez
     * primary_release_date.gte : fecha DESDE (se pueden meter meses y dias)
     * primary_release_date.lte : fecha HASTA (se pueden meter meses y dias)
     * year : año exacto de la pelicula
     * vote_average.gte : valoracion DESDE
     * vote_average.lte : valoracion HASTA
     * vote_count.gte : número de votaciones DESDE
     * with_genres : Lista de generos que queremos
     * without_genres : lista de generos que NO queremos
     * with_keywords : palabras clave (EN PRINCIPIO NO SE USA)
     * with_original_language : idioma original (EN PRINCIPIO NO SE USA)
     ------------------------------------------------------------------
     *


     * */
    // Añadir los dos arratList generados.
    public static void discoverMoviesWith(String first_year, String second_year, ArrayList<String> searchBy, ArrayList<String> discard) throws URISyntaxException, SQLException, UnsupportedEncodingException {
        HttpClient client = HttpClient.newHttpClient();
        String peticion = " ";

        // Procesamiento de los arrays de géneros.
        String search_by_parameters = String.join("%20%7C%7C%20", searchBy);
        String searchByParametroCodificado = URLEncoder.encode(search_by_parameters, "UTF-8");

        String discard_parameters = String.join("%2C", discard);
        String discardParametroCodificado = URLEncoder.encode(discard_parameters, "UTF-8");

        peticion = "https://api.themoviedb.org/3/discover/movie?"+API_KEY+"&language=en-EN&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_date.gte="+first_year+"&primary_release_date.lte="+second_year+"&with_watch_monetization_types=flatrate&with_genres="+ searchByParametroCodificado + "&without_genres="+discardParametroCodificado;

        HttpRequest request = HttpRequest.newBuilder(new URI(peticion))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try{
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = httpResponse.body();
            System.out.println(jsonResponse);
        } catch(InterruptedException e){
            System.out.println(e);
        } catch(IOException e){
            System.out.println(e);
        }
    }


    public void getMovieInfoAPI(String id){

    }
    public void loadMoviesScrap(){

    }

    public static void scrapIMG(String title) throws IOException {
        title = title.toLowerCase().replace(" ", "-");
        // Establecer la URL de la página web que se va a analizar
        String url = "https://www.metacritic.com/movie/";
        url = url + title;

        // Conectar con la página web y descargar el código fuente HTML
        Document document = Jsoup.connect(url).get();

        // Buscar la imagen con la clase "summary_img" en el código fuente HTML
        Element element = document.selectFirst("img.summary_img");

        // Obtener la URL de la imagen
        String imageUrl = element.attr("src");

        // Descargar la imagen y guardarla en el directorio "images"
        URL urlObject = new URL(imageUrl);
        String fileName = title + ".jpg"; // o el formato que desees
        Path targetPath = Paths.get("images", fileName);
        Files.copy(urlObject.openStream(), targetPath);
    }

    public static void scrapRating(String tituloPelicula){
        String tituloPeliculaCambiado = tituloPelicula.replace(" ", "_");
        String url = "https://www.rottentomatoes.com/m/" + tituloPeliculaCambiado;
        String valoracion = "";

        try {
            Document document = Jsoup.connect(url).get();
            String entrada = document.toString();

            String regex = "ratingValue\":\"(\\d+)\"";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(entrada);

            if (matcher.find()) {
                String ratingValue = matcher.group(1);
                System.out.println(ratingValue);
            } else {
                System.out.println("No se encontró ninguna coincidencia.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
