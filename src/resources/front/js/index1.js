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


    /*
    function checkGenres() {
    			var yesGenres = document.querySelectorAll('#yes_genres_fieldset input.genre_selector:checked');
    			var noGenres = document.querySelectorAll('#no_genres_fieldset input.genre_selector:checked');

    			for (var i = 0; i < yesGenres.length; i++) {
    				for (var j = 0; j < noGenres.length; j++) {
    					if (yesGenres[i].name === noGenres[j].name) {
    						alert("No puedes seleccionar el mismo género en 'Sí' y 'No'.");
    						return false;
    					}
    				}
    			}

    			return true;
    		}
    */

    /*$( function() {
      $( ".genre_selector" ).checkboxradio({
        icon: false
      });
    } )*/

    /*$( ".genre_selector" ).checkboxradio({
            icon: false
          });*/

    function formsearchmovies(e){
        e.preventDefault();

        var enviar = true;

        //var yesGenres = document.querySelectorAll('#yes_genres_fieldset input.genre_selector:checked');
        //var noGenres = document.querySelectorAll('#no_genres_fieldset input.genre_selector:checked');

        var yesGenres = $('#yes_genres_fieldset').find('.genre_selector:checked');
        var noGenres = $('#no_genres_fieldset').find('.genre_selector:checked');

        for (var i = 0; i < yesGenres.length; i++) {
            for (var j = 0; j < noGenres.length; j++) {
                if (yesGenres[i].name === noGenres[j].name) {

                    enviar = false;
                }
            }
        }

        if(enviar){
            $('.formsearchmovies').submit();
        }else{
            alert("No puedes seleccionar el mismo género en 'Sí' y 'No'.");
        }


    }

    function selectgenre(){
        if($(this).find('input').prop('checked')){
            $(this).addClass('btnsele');
        }else{
             $(this).removeClass('btnsele');
        }
    }

    //Asignación de funciones al evento 'submit' del formulario formsearchmovies
    $(document).on('click', '.selectgenre', selectgenre);
    $(document).on('click', '#search_button', formsearchmovies);
 },);
