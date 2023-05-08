package backend;

import java.util.ArrayList;

public class Movie {

    private String name;
    private int score;
    private int year;
    private String director;
    private int id;

    private ArrayList<Tag> genres;


    public Movie(int id, String name, int score, int year, String director){
        this.id = id;
        this.name = name;
        this.score = score;
        this.year = year;
        this.director = director;
        this.genres = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Tag> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Tag> genres) {
        this.genres = genres;
    }
}
