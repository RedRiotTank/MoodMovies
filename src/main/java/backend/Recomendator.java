package backend;

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


    private boolean quality;


    public Recomendator(String mood, int minYear, int maxYear,  ArrayList<stream> plataforms,ArrayList<Tag> yes_genres,ArrayList<Tag> no_genres){
        search_by = new ArrayList<>();
        discard = new ArrayList<>();
        switch (mood){
            case "good":
                this.mood = backend.Mood.GOOD;
                search_by.add("35");    //comedy
                search_by.add("12");    //adventure
                search_by.add("16");    //adventure
                discard.add("27");      //Horror
                discard.add("18"); //drama
                discard.add("80");  //crime
                break;
            default:
            case "normal":
                this.mood = backend.Mood.NORMAL;
                break;
            case "bad":
                this.mood = backend.Mood.BAD;
                search_by.add("27");    //horror
                search_by.add("18");    //drama
                search_by.add("53");    //Thriller
                search_by.add("9648");  //mistery
                discard.add("35");      //comedy
                discard.add("37");      //western
                discard.add("16"); //animation
                break;
        }

        this.minYear = minYear;
        this.maxYear = maxYear;

        this.plataforms = plataforms;

        this.yes_genres = yes_genres;
        this.no_genres = no_genres;

        for(Tag genre : yes_genres){

            if(discard.contains(genre))
                discard.remove(genre);

            search_by.add(tagtostring(genre));

        }

        for(Tag genre : no_genres){

            if(search_by.contains(genre))
                search_by.remove(genre);

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
        }

        return result;

    }



    public boolean getQuality() {
        return quality;
    }

    public void setQuality(boolean quality) {
        this.quality = quality;
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

    public ArrayList<Movie> makeList(){

        /*consulta SQL

            Si obtengo + de n resultados, creamos el array de películas con ellas y lo devolvemos.
            Si obtengo - de n resulsultados hacemos una solicitud a la API que busque peliculas con determinados géneros y sin determinados géneros.
         */



        return null;
    }

    @Override
    public String toString() {
        return "Recomendator{" +
                "mood=" + mood +
                ", minYear=" + minYear +
                ", maxYear=" + maxYear +
                ", plataforms=" + plataforms +
                ", yes_genres=" + yes_genres +
                ", no_genres=" + no_genres +
                ", search_by=" + search_by +
                ", discard=" + discard +
                ", quality=" + quality +
                '}';
    }
}
