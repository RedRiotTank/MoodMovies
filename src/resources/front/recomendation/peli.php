<!doctype html>

<html lang="es">
        <head>
                <meta charset="utf-8">
                <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
                <link rel="stylesheet" type="text/css" href="css/pelicss.css">
                <title>Movie information</title>
        </head>
        <body>
                <main>
                        <?php
                                $id = $_GET['id'];

                                $jsonData = file_get_contents('/home/albertoplazamontesdm/MoodMovies/movies.json');

                                $movies = json_decode($jsonData, true);
                                // Buscar la película con el ID deseado
                                $desiredMovie = null;
                                foreach ($movies as $movie) {
                                    if ($movie['id'] == $id) {
                                        $desiredMovie = $movie;
                                break;

                                    }
                                }
                                // Mostrar los detalles de la película si se encuentra
                                if ($desiredMovie) {

                                    $name = $desiredMovie['name'];
                                    $year = $desiredMovie['year'];
                                    $description = $desiredMovie['description'];
                                    $score_mc = $desiredMovie['scores']['score_mc'];
                                    $score_rt = $desiredMovie['scores']['score_rt'];

                                    include('includes/pelicontent.inc.php');
                                }

                        ?>

                </main>


            <footer id="pie">

            </footer>

        </body>
</html>