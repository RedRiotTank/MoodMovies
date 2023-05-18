package backend;

import Data.MovieLoader;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

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
        mvloader.discoverMoviesWith(popularity, 1, Integer.toString(this.minYear), Integer.toString(this.maxYear), this.getSearch_by(), this.getDiscard());
        mvloader.discoverMoviesWith(popularity, 2, Integer.toString(this.minYear), Integer.toString(this.maxYear), this.getSearch_by(), this.getDiscard());

        /*consulta SQL

            Si obtengo + de n resultados, creamos el array de películas con ellas y lo devolvemos.
            Si obtengo - de n resulsultados hacemos una solicitud a la API que busque peliculas con determinados géneros y sin determinados géneros.
         */



        return null;
    }

}
