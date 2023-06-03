package Data;

import backend.Movie;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class MovieLoader {

    DataBase db;

    public MovieLoader(DataBase db) {
        this.db = db;
    }

    public DataBase getDb() {
        return db;
    }

    private static final String API_KEY = "api_key=e37359bf96dd4a19ed90f8d427112595";

    public void discoverMoviesWith(boolean popularity_mode, int page, String first_year, String second_year, ArrayList<String> searchBy, ArrayList<String> discard){
        String search_by_parameters = String.join("%20%7C%7C%20", searchBy);

        String discard_parameters = String.join("%2C", discard);

        boolean controlMovies = true;
        while (!hasMovies(popularity_mode, page, first_year, second_year, search_by_parameters, discard_parameters) && controlMovies) {
            System.out.println("No se encontraron películas en la página " + page + ". Buscando otra página...");
            controlMovies = false;
        }
    }

    private boolean hasMovies(boolean popularity_mode, int page, String first_year, String second_year, String search_by_parameters, String discard_parameters) {
        String peticion;
        boolean has_movies = false;
        try {
            if (popularity_mode) {
                peticion = "https://api.themoviedb.org/3/discover/movie?" + API_KEY + "&language=en-EN&sort_by=popularity.desc&include_adult=false&include_video=false&page=" + page + "&primary_release_date.gte=" + first_year + "&primary_release_date.lte=" + second_year + "&with_watch_monetization_types=flatrate&with_genres=" + search_by_parameters + "&without_genres=" + discard_parameters + "&with_runtime.gte=80&certification.lte=16&certification_country=ES";
            } else {
                int max;
                int total_pages = getTotalPages(first_year, second_year, search_by_parameters, discard_parameters);

                max = Math.min(total_pages, 198);

                page = (int) (Math.random() * max + 1);
                peticion = "https://api.themoviedb.org/3/discover/movie?" + API_KEY + "&language=en-EN&sort_by=popularity.desc&include_adult=false&include_video=false&page=" + page + "&primary_release_date.gte=" + first_year + "&primary_release_date.lte=" + second_year + "&with_watch_monetization_types=flatrate&with_genres=" + search_by_parameters + "&without_genres=" + discard_parameters + "&with_runtime.gte=80&certification.lte=16&certification_country=ES";
            }

            // Obtener el JSON desde el enlace
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new URL(peticion));

            if (rootNode.has("results")) {
                JsonNode results = rootNode.get("results");
                has_movies = results.size() > 0;
            }
            // Obtener el array de resultados
            JsonNode resultsNode = rootNode.get("results");

            // Recorrer todas las películas
            for (JsonNode movieNode : resultsNode) {
                int id = movieNode.get("id").asInt();

                String title = movieNode.get("title").asText();
                String year = movieNode.get("release_date").asText().substring(0, 4);
                double popularity = movieNode.get("popularity").asDouble();
                String description = movieNode.get("overview").asText();
                String path = movieNode.get("poster_path").asText();
                String score_mc = scrapRatingMC(title);
                String score_rt = scrapRatingRT(title);

                getImageAPI(path, id);

                String[] genres = objectMapper.convertValue(movieNode.get("genre_ids"), String[].class);

                db.insertMovie(id, title, year, popularity, description, score_mc, score_rt);

                for (String genre : genres) {
                    db.insertMovieGenre(id, Integer.parseInt(genre));
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return has_movies;
    }

    public void getImageAPI(String path, int id) throws IOException, URISyntaxException {
        if(path != null){
            String imageUrl = "https://image.tmdb.org/t/p/w342/" + path;
            URL urlObject = new URL(imageUrl);
            String fileName = id + ".jpg";
            String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String jarDirectory = new File(jarPath).getParent();
            String imagesDirectory = jarDirectory + File.separator + "images";
            File directory = new File(imagesDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Path targetPath = Paths.get(imagesDirectory, fileName);

            try (InputStream inputStream = urlObject.openStream()) {
                // si la imagen ya existe, la reemplaza
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Imagen guardada exitosamente.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public int getTotalPages(String first_year, String second_year, String search_by_parameters, String discard_parameters) throws IOException {
        String peticion = "https://api.themoviedb.org/3/discover/movie?" + API_KEY + "&language=en-EN&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_date.gte=" + first_year + "&primary_release_date.lte=" + second_year + "&with_watch_monetization_types=flatrate&with_genres=" + search_by_parameters + "&without_genres=" + discard_parameters + "&with_runtime.gte=80&certification.lte=16&certification_country=ES";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new URL(peticion));

        return rootNode.get("total_pages").asInt();
    }
    public static String scrapRatingMC(String tituloPelicula){
        String normalizedTitle = Normalizer.normalize(tituloPelicula, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String mcTitle = pattern.matcher(normalizedTitle)
                .replaceAll("")
                .replaceAll("[:,¿?'?.]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replace("&", "")
                .toLowerCase();

        mcTitle = mcTitle.replaceAll("-+", "-");

        String url = "https://www.metacritic.com/movie/" + mcTitle;
        String result;

        try {
            Document document = Jsoup.connect(url).get();
            Element element = document.selectFirst("span.metascore_w");
            result = element.text();

            if (result.equals(""))
                throw new IOException();

            if(result.equals("tbd"))
                throw new IOException();

        } catch (IOException e) {
            System.out.println("ERROR: No se pudo realizar el web scraping de RATING en Metacritic: " + e.getMessage());
            return "NF";
        }

        return result;
    }



    public static String scrapRatingRT(String tituloPelicula) {
        String normalizedTitle = Normalizer.normalize(tituloPelicula, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String rtTitle = pattern.matcher(normalizedTitle).replaceAll("").replaceAll("[:,¿?'?.]", "").replaceAll(" ", "_").replace("&", "and").toLowerCase();

        String url = "https://www.rottentomatoes.com/m/" + rtTitle;
        String result;

        try {
            Document document = Jsoup.connect(url).get();
            Element element = document.selectFirst("score-board");

            if (element == null)
                throw new IOException();

            result = element.attr("audiencescore");

            if (result.equals(""))
                throw new IOException();

        } catch (IOException e) {
            System.out.println("ERROR: No se pudo realizar el web scraping de RATING en Rottentomatoes: " + e.getMessage());
            return "NF";
        }
        return result;
    }

    public ArrayList<Movie> getRecommendedList(int minYear, int maxYear, ArrayList<String> search_by, ArrayList<String> discard, boolean popularity_order) {

        return db.getDataBaseRecommendedList(minYear,maxYear,search_by, discard, popularity_order);
    }

    public int getNumDataBaseMovies(int minYear, int maxYear,ArrayList<String> search_by, ArrayList<String> discard, boolean popularity_order){
        return db.getNumDataBaseMovies(minYear,maxYear,search_by, discard, popularity_order);
    }

}