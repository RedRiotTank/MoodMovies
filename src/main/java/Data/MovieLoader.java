package Data;




import backend.Movie;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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




public class MovieLoader {

    DataBase db = null;

    public MovieLoader(DataBase db) {
        this.db = db;
    }

    public DataBase getDb() {
        return db;
    }

    private static String API_KEY = "api_key=e37359bf96dd4a19ed90f8d427112595";

    /* DOCUMENTACION API (método discover)
    --------------------------------------------------------------
     * language: indica en que idioma queremos que salga el json
     * sort_by: orden en el que vienen los datos (solo uno de los siguientes) {
     *   popularity.asc, popularity.desc, release_date.asc, release_date.desc,
     *   primary_release_date.asc, primary_release_date.desc, vote_average.asc, vote_average.desc,
     *   vote_count.asc, vote_count.desc
     * }
     *
     * certification: para restrictir las peliculas que salgan (p.e. que no sean violentas)
     * include_adult: false
     * include_video: false
     * page : Página que queremos que muestre, nos va a servir para sacar películas distintas cada vez
     * primary_release_date.gte : fecha DESDE (se pueden meter meses y dias)
     * primary_release_date.lte : fecha HASTA (se pueden meter meses y dias)
     * year : año exacto de la pelicula
     * vote_average.gte : valoracion DESDE
     * vote_average.lte : valoracion HASTA
     * vote_count.gte : número de votaciones DESDE
     * with_genres : Lista de generos que queremos
     * without_genres : lista de generos que NO queremos
     * with_keywords : palabras clave (EN PRINCIPIO NO SE USA)
     * with_original_language : idioma original (EN PRINCIPIO NO SE USA)
     ------------------------------------------------------------------
     *


     * */
    // Añadir los dos arratList generados.
    public void discoverMoviesWith(boolean popularity_mode, int page, String first_year, String second_year, ArrayList<String> searchBy, ArrayList<String> discard) throws URISyntaxException, SQLException, UnsupportedEncodingException {
        // Procesamiento de los arrays de géneros.
        String search_by_parameters = String.join("%20%7C%7C%20", searchBy);
        //String searchByParametroCodificado = URLEncoder.encode(search_by_parameters, "UTF-8");

        String discard_parameters = String.join("%2C", discard);
        //String discardParametroCodificado = URLEncoder.encode(discard_parameters, "UTF-8");

        // procesamiento de página aleatoria: max page = 500
        boolean controlMovies = true;
        while (!hasMovies(popularity_mode, page, first_year, second_year, search_by_parameters, discard_parameters) && controlMovies) {
            System.out.println("No se encontraron películas en la página " + page + ". Buscando otra página...");
            controlMovies = false;
        }

        //System.out.println("Se encontraron peliculas en la pagina: " + page);
    }

    private boolean hasMovies(boolean popularity_mode, int page, String first_year, String second_year, String search_by_parameters, String discard_parameters) {
        String peticion = " ";
        boolean has_movies = false;
        try {
            if (popularity_mode) {
                peticion = "https://api.themoviedb.org/3/discover/movie?" + API_KEY + "&language=en-EN&sort_by=popularity.desc&include_adult=false&include_video=false&page=" + page + "&primary_release_date.gte=" + first_year + "&primary_release_date.lte=" + second_year + "&with_watch_monetization_types=flatrate&with_genres=" + search_by_parameters + "&without_genres=" + discard_parameters + "&with_runtime.gte=80&certification.lte=16&certification_country=ES";
            } else {
                int max;
                int total_pages = getTotalPages(first_year, second_year, search_by_parameters, discard_parameters);

                if (total_pages > 198) {
                    max = 198;
                } else {
                    max = total_pages;
                }

                page = (int) (Math.random() * max + 1);
                peticion = "https://api.themoviedb.org/3/discover/movie?" + API_KEY + "&language=en-EN&sort_by=popularity.desc&include_adult=false&include_video=false&page=" + page + "&primary_release_date.gte=" + first_year + "&primary_release_date.lte=" + second_year + "&with_watch_monetization_types=flatrate&with_genres=" + search_by_parameters + "&without_genres=" + discard_parameters + "&with_runtime.gte=80&certification.lte=16&certification_country=ES";
            }

            // Obtener el JSON desde el enlace
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new URL(peticion));

            int totalPages = rootNode.get("total_pages").asInt();

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
                String score = scrapRating(title,id);

                getImageAPI(path, id);

                String[] genres = objectMapper.convertValue(movieNode.get("genre_ids"), String[].class);

                db.insertMovie(id, title, year, popularity, score, description);

                for (int i = 0; i < genres.length; i++) {
                    db.insertMovieGenre(id, Integer.parseInt(genres[i]));
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
        String imageUrl = "https://image.tmdb.org/t/p/original/" + path;
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
        Files.copy(urlObject.openStream(),targetPath);
    }

    public int getTotalPages(String first_year, String second_year, String search_by_parameters, String discard_parameters) throws IOException {
        String peticion = "https://api.themoviedb.org/3/discover/movie?" + API_KEY + "&language=en-EN&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_date.gte=" + first_year + "&primary_release_date.lte=" + second_year + "&with_watch_monetization_types=flatrate&with_genres=" + search_by_parameters + "&without_genres=" + discard_parameters + "&with_runtime.gte=80&certification.lte=16&certification_country=ES";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new URL(peticion));

        return rootNode.get("total_pages").asInt();
    }

    public void getMovieInfoAPI(String id) {

    }

    public void loadMoviesScrap() {

    }

    public static String scrapRating(String tituloPelicula, int id){
        String tituloPeliculaCambiado = tituloPelicula.replaceAll("[:,¿?'?]", "");

        // Reemplazar espacios por guiones
        tituloPeliculaCambiado = tituloPeliculaCambiado.replaceAll(" ", "-");

        // Convertir a minúsculas
        tituloPeliculaCambiado = tituloPeliculaCambiado.toLowerCase();
        String url = "https://www.metacritic.com/movie/" + tituloPeliculaCambiado;
        String ratingValue = " ";

        try {
            // Conectar con la página web y descargar el código fuente HTML
            Document document = Jsoup.connect(url).get();
            // Buscar la ecore en el código fuente HTML
            Element element = document.selectFirst("span.metascore_w");
            String result = element.text();
            if(result.equals("tbd")){
                throw new IOException();
            }

            return result;

        } catch (IOException e) {
            System.out.println("ERROR: No se pudo realizar el web scraping de RATING: " + e.getMessage());
            System.out.println("Se intentará en themoviedatabase:");

            String urlTMD = "https://www.themoviedb.org/movie/" + id;
            try {
                // Conectar con la página web y descargar el código fuente HTML
                Document document = Jsoup.connect(urlTMD).get();
                // Buscar la ecore en el código fuente HTML
                Element element = document.selectFirst("div.user_score_chart");
                String score = element.attr("data-percent");
                int puntoIndex = score.indexOf(".");

                if (puntoIndex != -1) {
                    score =  score.substring(0, puntoIndex);
                }

                return score;

            } catch (IOException exception) {
                System.out.println("Tampoco se pudo hacer scrap al rating en themoviedatabase" + exception.getMessage());
                ratingValue = "NF";
                System.out.println("se introdujo NF");
                return ratingValue;
            }


        }



    }

    // Se obtiene el numero de peliculas obtenidas de la bd con los generos de la lista <generos>
    public int getNumMoviesRecommended(ArrayList<String> generos) throws SQLException {
        return db.getNumMovies(generos);
    }

    public ArrayList<Movie> getRecommendedList(int minYear, int maxYear, ArrayList<String> search_by, ArrayList<String> discard, boolean popularity_order) {

        return db.getDataBaseRecommendedList(minYear,maxYear,search_by, discard, popularity_order);
    }

    public int getNumDataBaseMovies(int minYear, int maxYear,ArrayList<String> search_by, ArrayList<String> discard, boolean popularity_order){
        return db.getNumDataBaseMovies(minYear,maxYear,search_by, discard, popularity_order);
    }

}
/*
        // Método para construir la lista de películas
        public ArrayList<Movie> buildMovieList(List<String> includedGenres, List<String> excludedGenres) {
            ArrayList<Movie> movieList = new ArrayList<>();

            try {
                // Establecer conexión con la base de datos
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nombre_base_de_datos", "usuario", "contraseña");

                // Crear la consulta SQL dinámicamente
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("SELECT m.id, m.title, m.year, m.popularity, m.score ")
                        .append("FROM Movies AS m ")
                        .append("INNER JOIN Movies_Genres AS mg ON m.id = mg.movie_id ")
                        .append("INNER JOIN Genres AS g ON mg.genre_id = g.id ")
                        .append("WHERE g.name IN (");

                // Agregar los géneros incluidos a la consulta
                for (int i = 0; i < includedGenres.size(); i++) {
                    sqlBuilder.append("?");
                    if (i < includedGenres.size() - 1) {
                        sqlBuilder.append(", ");
                    }
                }
                sqlBuilder.append(") ");

                // Agregar los géneros excluidos a la consulta
                if (!excludedGenres.isEmpty()) {
                    sqlBuilder.append("AND m.id NOT IN (")
                            .append("SELECT DISTINCT m2.id ")
                            .append("FROM peliculas AS m2 ")
                            .append("INNER JOIN Movies_Genres AS mg2 ON m2.id = mg2.movie_id ")
                            .append("INNER JOIN generos AS g2 ON mg2.genre_id = g2.id ")
                            .append("WHERE g2.name IN (");
                    for (int i = 0; i < excludedGenres.size(); i++) {
                        sqlBuilder.append("?");
                        if (i < excludedGenres.size() - 1) {
                            sqlBuilder.append(", ");
                        }
                    }
                    sqlBuilder.append("))");
                }

                // Crear el objeto PreparedStatement
                PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString());

                // Establecer los valores de los parámetros de los géneros incluidos
                for (int i = 0; i < includedGenres.size(); i++) {
                    pstmt.setString(i + 1, includedGenres.get(i));
                }

                // Establecer los valores de los parámetros de los géneros excluidos
                int paramIndex = includedGenres.size() + 1;
                for (int i = 0; i < excludedGenres.size(); i++) {
                    pstmt.setString(paramIndex + i, excludedGenres.get(i));
                }

                // Ejecutar la consulta
                ResultSet rs = pstmt.executeQuery();

                // Iterar sobre los resultados y construir las instancias de Movie
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    int year = rs.getInt("year");
                    int popularity = rs.getInt("popularity");
                    int score = rs.getInt("score");

                    // Crear una instancia de Movie y agregarla a la lista
                    Movie movie = new Movie(title, score, year, "", id, new ArrayList<>());
                    movieList.add(movie);
                }

                // Cerrar la conexión y liberar recursos
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return movieList;
        }
    }

}
*/

