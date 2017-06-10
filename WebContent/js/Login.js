/**
 * Created by jonathanlarsen on 07/06/2017.
 */
$(document).ready(function () {
    $('#login').submit(function(event) {
        event.preventDefault();
        $(function() {

            /* Login */
            $.ajax({
                type: "POST",
                url: 'http://localhost:8080/CDIOFinal/',
                dataType: 'json',
                data: JSON.stringify({
                    "username": username,
                    "password": password
                }),
                error: function(errorThrown) {
                    console.log(errorThrown);
                },
                success: function() {
                    console.log('Succesfully');
                }
            })
        });
        return false;
    });
});

