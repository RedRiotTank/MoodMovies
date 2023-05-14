package backend;

import java.util.ArrayList;

public class Recomendator {

    private Mood mood;
    private int minYear;
    private int maxYear;
    private ArrayList<stream> plataforms;
    private ArrayList<Tag> yes_genres;
    private ArrayList<Tag> no_genres;

    private ArrayList<Tag> search_by;

    private ArrayList<Tag> discard;
    private boolean quality;


    public Recomendator(String mood, int minYear, int maxYear,  ArrayList<stream> plataforms,ArrayList<Tag> yes_genres,ArrayList<Tag> no_genres){
        search_by = new ArrayList<>();
        discard = new ArrayList<>();
        switch (mood){
            case "good":
                this.mood = backend.Mood.GOOD;
                search_by.add(Tag.COMEDY);
                search_by.add(Tag.ADVENTURE);
                search_by.add(Tag.ANIMATION);
                discard.add(Tag.HORROR);
                discard.add(Tag.DRAMA);
                discard.add(Tag.CRIME);
                break;
            default:
            case "normal":
                this.mood = backend.Mood.NORMAL;
                break;
            case "bad":
                this.mood = backend.Mood.BAD;
                search_by.add(Tag.HORROR);
                search_by.add(Tag.DRAMA);
                search_by.add(Tag.THRILLER);
                search_by.add(Tag.MYSTERY);
                discard.add(Tag.COMEDY);
                discard.add(Tag.WESTERN);
                discard.add(Tag.ANIMATION);
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

            search_by.add(genre);

        }

        for(Tag genre : no_genres){

            if(search_by.contains(genre))
                search_by.remove(genre);

            discard.add(genre);

        }


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

    public void setMovies(ArrayList<Movie> movies){

    }

    public ArrayList<Movie> makeList(){
        System.out.printf(Integer.toString(minYear));
        return null;
    }


}
