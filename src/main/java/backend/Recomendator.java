package backend;

import java.util.ArrayList;

public class Recomendator {
    private boolean quality;
    private int minYear;
    private int maxYear;
    private int mood;

    private ArrayList<Movie> recomList;


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

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public ArrayList<Movie> getMovies(){

        return null;
    }

    public void setMovies(ArrayList<Movie> movies){

    }

    public void makeList(){

    }

    public Recomendator(boolean quality, int minYear, int maxYear, int mood){

    }

}
