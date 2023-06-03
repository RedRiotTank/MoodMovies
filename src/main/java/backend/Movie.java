package backend;

import java.util.ArrayList;

public class Movie {

    private String name;
    private String score_mc;
    private String score_rt;
    private int year;
    private String director;
    private int id;
    private double popularity;
    private String description;

    private ArrayList<Tag> genres;

    public Movie(){
        this.id = 0;
        this.score_mc = "";
        this.score_rt = "";
        this.director = "";
        this.name = "";
        this.year = 0;
        this.popularity = 0.0;
        this.description = "";
        genres = new ArrayList<>();
    }

    public Movie(int id, String name, String score_mc, String score_rt, int year, double popularity, String director, String description){
        this.id = id;
        this.name = name;
        this.score_mc = score_mc;
        this.score_rt = score_rt;
        this.year = year;
        this.popularity = popularity;
        this.director = director;
        this.genres = new ArrayList<>();
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore_mc() {
        return score_mc;
    }

    public String getScore_rt() {
        return score_rt;
    }

    public String getYear() {
        return Integer.toString(year);
    }

    public String getId() {
        return Integer.toString(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

}
