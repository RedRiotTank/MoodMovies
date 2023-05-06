$(document).ready(function(){
    // funcion que aumenta y dismuniye los años del range
    $( "#year_slider" ).slider({
        range: true,
        min: 1950,
        max: 2023,
        values: [ 2000, 2023 ],
        slide: function( event, ui ) {
          $( "#years_desde" ).val(ui.values[ 0 ]);
          $( "#years_hasta" ).val(ui.values[ 1 ] );
        }
      });
      $( "#years_desde" ).val( "" + $( "#year_slider" ).slider( "values", 0 ));
      $( "#years_hasta" ).val( "" + $( "#year_slider" ).slider( "values", 1 ));



        //" - " + $( "#year_slider" ).slider( "values", 1 ) );
    }, 

    $( function() {
      $( ".genre_selector" ).checkboxradio({
        icon: false
      });
    } )
 );
