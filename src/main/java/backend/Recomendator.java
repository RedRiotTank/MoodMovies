package backend;


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
                search_by.add(tagtostring(Tag.ACTION));
                search_by.add(tagtostring(Tag.ANIMATION));
                search_by.add(tagtostring(Tag.ADVENTURE));
                search_by.add(tagtostring(Tag.COMEDY));
                search_by.add(tagtostring(Tag.CRIME));
                search_by.add(tagtostring(Tag.DRAMA));
                search_by.add(tagtostring(Tag.FANTASY));
                search_by.add(tagtostring(Tag.HORROR));
                search_by.add(tagtostring(Tag.MYSTERY));
                search_by.add(tagtostring(Tag.ROMANCE));
                search_by.add(tagtostring(Tag.THRILLER));
                search_by.add(tagtostring(Tag.WESTERN));
                search_by.add(tagtostring(Tag.SCIENCE_FICTION));
                search_by.add(tagtostring(Tag.HISTORY));
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
        switch (tag) {
            case ADVENTURE:
                result = "12";
                break;
            case FANTASY:
                result = "14";
                break;
            case ANIMATION:
                result = "16";
                break;
            case DRAMA:
                result = "18";
                break;
            case HORROR:
                result = "27";
                break;
            case ACTION:
                result = "28";
                break;
            case COMEDY:
                result = "35";
                break;
            case HISTORY:
                result = "36";
                break;
            case WESTERN:
                result = "37";
                break;
            case THRILLER:
                result = "53";
                break;
            case CRIME:
                result = "80";
                break;
            case MYSTERY:
                result = "9648";
                break;
            case ROMANCE:
                result = "10749";
                break;
            case SCIENCE_FICTION:
                result = "878";
                break;
            default:
                break;
        }


        return result;

    }

    public ArrayList<String> getSearch_by() {
        return search_by;
    }

    public ArrayList<String> getDiscard() {
        return discard;
    }

    public ArrayList<Movie> makeList(MovieLoader mvloader){
        int total_movies;
        ArrayList<Movie> recommended_list;

        total_movies = mvloader.getNumDataBaseMovies(this.minYear, this.maxYear,this.search_by, this.discard, this.popularity);

        System.out.println("Hay un TOTAL de " + total_movies + " peliculas en la bd");

        // si no hay suficientes peliculas en bd, se buscan
        if(total_movies < 20 && total_movies >= 0){
            System.out.println("Buscando peliculas en nuestras fuentes de datos");
            mvloader.discoverMoviesWith(popularity, 1, Integer.toString(this.minYear), Integer.toString(this.maxYear+1), this.getSearch_by(), this.getDiscard());
        }
        System.out.println("Se ha procesado la busqueda de peliculas correctamente");
        recommended_list = mvloader.getRecommendedList(this.minYear, this.maxYear,this.search_by, this.discard, this.popularity);

        return recommended_list;
    }

}
