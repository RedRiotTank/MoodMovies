<!doctype html>

<html lang="es">

<head>
        <meta charset="utf-8">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/pelicss.css">
        <link rel="icon" type="image/png" href="../images/favicon.png">
        <title>Movie information</title>
</head>
<header>
		<a href="http://recomendator.moodmovies.net" target="_self" alt="Nueva búsqueda">
            <img id="logo" src="../images/favicon.png" alt="Logo MoodMovies">
        </a>
		<h1>MoodMovies - El recomendador de películas que necesitas</h1>
</header>

<body>
        <main>
                <?php
                $id = $_GET['id'];

                $jsonData = file_get_contents('/home/albertoplazamontesdm/MoodMovies/movies.json');

                $movies = json_decode($jsonData, true);
                $desiredMovie = null;
                foreach ($movies as $movie) {
                        if ($movie['id'] == $id) {
                                $desiredMovie = $movie;
                                break;

                        }
                }
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


        <footer>
                Página desarrollada por alumnos de la Universidad de Granada&copy;
        </footer>

</body>

</html>