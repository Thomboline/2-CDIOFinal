/**
 * Created by jonathanlarsen on 06/06/2017.
 */

$(document).ready(function () {
    $('#login').submit(function(event) {
        event.preventDefault();
        login();
        return false;
    });
});

function login() {
    $.ajax({
        type:'GET',
        url: rootURL,
        dataType: "json",
        data: "username=" + username + "&password=" + password,
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            $('div#loginResult').text("responseText: " + XMLHttpRequest.responseText
                + ", textStatus: " + textStatus
                + ", errorThrown: " + errorThrown);
            $('div#loginResult').addClass("error");
            console.log("Login failed")
        },
        success: function(data){
            if (data.error) {
                $('div#loginResult').text("data.error: " + data.error);
                $('div#loginResult').addClass("error");
            } else { // login was successful
                $('form#loginForm').hide();
                $('div#loginResult').text("data.success: " + data.success
                    + ", data.userid: " + data.userid);
                $('div#loginResult').addClass("success");
            }
        }
    });
    $('div#loginResult').fadeIn();
    return false;
}
