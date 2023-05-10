package backend;

import java.util.ArrayList;

public class Recomendator {

    private mood mood;
    private int minYear;
    private int maxYear;
    private ArrayList<stream> plataforms;
    private ArrayList<Tag> yes_genres;
    private ArrayList<Tag> no_genres;

    private boolean quality;

    private ArrayList<Movie> recomList;


    public Recomendator(String mood, int minYear, int maxYear,  ArrayList<stream> plataforms,ArrayList<Tag> yes_genres,ArrayList<Tag> no_genres){

        switch (mood){
            case "good":
                this.mood = backend.mood.GOOD;
                break;
            default:
            case "normal":
                this.mood = backend.mood.NORMAL;
                break;
            case "bad":
                this.mood = backend.mood.BAD;
                break;
        }

        this.minYear = minYear;
        this.maxYear = maxYear;

        this.plataforms = plataforms;

        this.yes_genres = yes_genres;
        this.no_genres = no_genres;


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

    public mood getMood() {
        return mood;
    }

    public void setMood(mood mood) {
        this.mood = mood;
    }

    public ArrayList<Movie> getMovies(){

        return null;
    }

    public void setMovies(ArrayList<Movie> movies){

    }

    public void makeList(){
        System.out.printf(Integer.toString(minYear));

    }


}
