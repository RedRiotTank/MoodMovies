<section class="pelidisplay">
<div class="center-button">
      <a href="http://recomendator.moodmovies.net/recomendation/movies.php" target="_self">Volver</a>
</div>
<h2 class="titulo"><?php echo "$name"?></h2>
    <a href="a.html">
        <img class="cartelera" src="images/<?php echo $id; ?>.jpg" alt="ERROR" width="265" height="377.5" />
    </a>

        

        <ul class="paragraph">
                <li><strong>Año:</strong> <?php echo "$year"?>.</li>
        </ul>
        <p><img class="iconos" src="images/metacritic.png" alt="ERROR" width="30" height="30" />: <?php echo $score_mc?></p>
        <p><img class="iconos" src="images/tomato.png" alt="ERROR" width="30" height="30" />: <?php echo $score_rt?></p>
        <h2 class="sinopsis">SINOPSIS</h2>

        <p class="paragraph"> <?php echo "$description"?></p>

        <div class="center-button">
              <a href="http://recomendator.moodmovies.net/recomendation/movies.php" target="_self">Volver</a>
        </div>
</section>