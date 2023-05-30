<section class="pelidisplay">
    <a href="a.html">
        <img class="cartelera" src="images/<?php echo $id; ?>.jpg" alt="ERROR" width="265" height="377.5" />
    </a>

        <h1 class="titulo"><?php echo "$name"?></h1>

        <ul class="paragraph">
                <li><strong>Año:</strong> <?php echo "$year"?>.</li>
        </ul>
        <p><img class="iconos" src="images/metacritic.png" alt="ERROR" width="30" height="30" />: <?php echo $score_mc?></p>
        <p><img class="iconos" src="images/tomato.png" alt="ERROR" width="30" height="30" />: <?php echo $score_rt?></p>
        <h2 class="sinopsis">SINOPSIS</h2>

        <p class="paragraph"> <?php echo "$description"?></p>

</section>