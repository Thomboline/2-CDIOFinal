/**
 * Created by jonathanlarsen on 07/06/2017.
 */
$(document).ready(function () {
    $('#login').submit(function(event) {
        event.preventDefault();
        $(function() {

            /* Validate
            $("#login").validate({
                rules:
                    {
                        username: {
                            required: true
                        },
                        password: {
                            required: "Please enter your password"
                        },
                        submitHandler: submitForm
                    })
            /* Validate */

            /* Login */
            $.ajax({
                type: "POST",
                url: 'http://localhost:????/login',
                dataType: 'json',
                data: JSON.stringify({
                    "userid": Username,
                    "password": Password
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

