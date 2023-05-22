package backend;

import Data.DataBase;
import Data.MovieLoader;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class Recomendator {

    private Mood mood;
    private int minYear;
    private int maxYear;
    private ArrayList<stream> plataforms;
    private ArrayList<Tag> yes_genres;
    private ArrayList<Tag> no_genres;

    private ArrayList<String> search_by;

    private ArrayList<String> discard;


    private boolean popularity;


    public Recomendator(String mood, int minYear, int maxYear,  ArrayList<stream> plataforms,ArrayList<Tag> yes_genres,ArrayList<Tag> no_genres,Boolean popularity, MovieLoader mvloader){
        this.popularity = popularity;
        search_by = new ArrayList<>();
        discard = new ArrayList<>();
        switch (mood){
            case "good":
                this.mood = backend.Mood.GOOD;
                search_by.add(tagtostring(Tag.COMEDY));
                search_by.add(tagtostring(Tag.ADVENTURE));
                search_by.add(tagtostring(Tag.ANIMATION));
                discard.add(tagtostring(Tag.HORROR));
                discard.add(tagtostring(Tag.DRAMA));
                discard.add(tagtostring(Tag.CRIME));
                break;
            default:
            case "normal":
                this.mood = backend.Mood.NORMAL;
                break;
            case "bad":
                this.mood = backend.Mood.BAD;
                search_by.add(tagtostring(Tag.HORROR));
                search_by.add(tagtostring(Tag.DRAMA));
                search_by.add(tagtostring(Tag.THRILLER));
                search_by.add(tagtostring(Tag.MYSTERY));
                discard.add(tagtostring(Tag.COMEDY));
                discard.add(tagtostring(Tag.WESTERN));
                discard.add(tagtostring(Tag.ANIMATION));
                break;
        }

        this.minYear = minYear;
        this.maxYear = maxYear;

        this.plataforms = plataforms;

        this.yes_genres = yes_genres;
        this.no_genres = no_genres;

        for(Tag genre : yes_genres){

            if(discard.contains(tagtostring(genre)))
                discard.remove(tagtostring(genre));

            if(!search_by.contains(tagtostring(genre)))
                search_by.add(tagtostring(genre));

        }

        for(Tag genre : no_genres){

            if(search_by.contains(tagtostring(genre)))
                search_by.remove(tagtostring(genre));

            if(!discard.contains(tagtostring(genre)))
                discard.add(tagtostring(genre));

        }
    }


    public String tagtostring(Tag tag){
        String result = null;
        switch (tag){
            case ADVENTURE -> result = "12";
            case FANTASY -> result = "14";
            case ANIMATION -> result = "16";
            case DRAMA -> result = "18";
            case HORROR -> result = "27";
            case ACTION -> result = "28";
            case COMEDY -> result = "35";
            case HISTORY -> result = "36";
            case WESTERN -> result = "37";
            case THRILLER -> result = "53";
            case CRIME -> result = "80";
            case MYSTERY -> result = "9648";
            case ROMANCE -> result = "10749";
            case SCIENCE_FICTION -> result = "878";
        }

        return result;

    }

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public ArrayList<Movie> getMovies(){

        return null;
    }

    public ArrayList<String> getSearch_by() {
        return search_by;
    }

    public ArrayList<String> getDiscard() {
        return discard;
    }

    public void setMovies(ArrayList<Movie> movies){

    }

    public ArrayList<Movie> makeList(MovieLoader mvloader) throws SQLException, UnsupportedEncodingException, URISyntaxException {
        int movies_search_by = 0, movies_to_discard = 0, total_movies = 0;
        ArrayList<Movie> recommended_list = new ArrayList<>();
        Movie aux_movie = new Movie();

        movies_search_by = mvloader.getNumMoviesRecommended(this.search_by);

        movies_to_discard = mvloader.getNumMoviesRecommended(this.discard);

        total_movies = movies_search_by - movies_to_discard;

        System.out.println("Hay un total de " + total_movies + " peliculas en la bd");

        // si no hay suficientes peliculas en bd, se buscan
        if(total_movies < 20){
            // discover()
            System.out.println("Buscando peliculas en nuestras fuentes de datos");
            mvloader.discoverMoviesWith(popularity, 1, Integer.toString(this.minYear), Integer.toString(this.maxYear), this.getSearch_by(), this.getDiscard());
            mvloader.discoverMoviesWith(popularity, 2, Integer.toString(this.minYear), Integer.toString(this.maxYear), this.getSearch_by(), this.getDiscard());
        }
        System.out.println("Se ha procesado la busqueda de peliculas");
        // se devuelve la lista de recomendación
        recommended_list = mvloader.getRecommendedList(this.search_by, this.discard, this.popularity);

        return recommended_list;
    }
    // combina los dos arrays, descartando los elementos del primero que estén en el segundo
    private static ArrayList<String> combinarArrayLists(ArrayList<String> arrayList1, ArrayList<String> arrayList2) {
        HashSet<String> elementosExcluidos = new HashSet<>(arrayList2);

        ArrayList<String> resultado = new ArrayList<>();

        for (String elemento : arrayList1) {
            if (!elementosExcluidos.contains(elemento)) {
                resultado.add(elemento);
            }
        }

        return resultado;
    }

}
