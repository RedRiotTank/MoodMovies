<section class="fullDisplay">
        <section class="moviesDisplay">
        <h3>Aquí están sus películas</h3>
        <?php

            $jsonData = file_get_contents('/home/albertoplazamontesdm/MoodMovies/movies.json');
            $movies = json_decode($jsonData, true);

            foreach ($movies as $movie) {
                $id = $movie['id'];
                $name = $movie['name'];
                $year = $movie['year'];
                $description = $movie['description'];
                $score_mc = $movie['score_mc'];
                $score_rt = $movie['score_rt'];

                echo '<article>';
                echo '<a href="peli.php?id=' . $id . '"><img class="moviesDisplay" src="images/' . $id . '.jpg" alt="ERROR" width="265" height="377"></a>';
                echo '<p><i class="material-icons">info</i>' . $name . ' </p>';
                echo '</article>';
            }
        ?>


        </section>
</section>