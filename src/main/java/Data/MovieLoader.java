package Data;



import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import backend.mood;

public class MovieLoader {

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
    public void discoverMoviesWith(String mood,String first_year, String second_year) throws URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        /* propuesta dependiendo del estado de animo que tengamos, se añaden unos generos u otros.
        Falta ver como procesar esa lista de generos
        if(mood.equals(mood.GOOD)){
            generos = {...}
        }
        */

        HttpRequest request = HttpRequest.newBuilder(new URI("https://api.themoviedb.org/3/discover/movie?"+API_KEY+"&language=es-ES&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_date.gte="+first_year+"&primary_release_date.lte="+second_year+"&with_watch_monetization_types=flatrate"))
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

    public void loadMoviesScrap(){

    }

}
