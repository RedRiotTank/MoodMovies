package Data;



import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class MovieLoader {

    private static String API_KEY = "?api_key=e37359bf96dd4a19ed90f8d427112595";
    public void loadMoviesAPI() throws URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("https://api.themoviedb.org/3/movie/550" + API_KEY))
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
