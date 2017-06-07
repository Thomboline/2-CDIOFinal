/**
 * Created by jonathanlarsen on 07/06/2017.
 */
$(document).ready(function() {
    $('#ListUsers').submit(function(event) {
        event.preventDefault();
        ListUsers();
        return false;
    });

    $('#CreateUser').submit(function(event) {
        event.preventDefault();
        CreateUser();
        return false;
    });
});

function MenuBar() {
    if(user.hasRole('Administrator')) {
        $("div.Admin").show();
    }

    if(user.hasRole('Farmaceut')) {
        $("div.Farmaceut").show();
    }

    if(user.hasRole('Vaerkfoerer')) {
        $("div.Vaerkfoereren").show();
    }

    if(user.hasRole('Laborant')) {
        $("div.Laborant").show();
    }
}

function ListUsers() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/CDIOFinal/',
        dataType: 'json',
        success: renderList
    });
}

function CreateUser() {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/CDIOFinal/',
        dataType: "json",
        data:
    });
}

function renderList(data) {
    var list = data == null ? [] : (data instanceof Array ? data : [data]);

    $.each(list, function(index, user) {
        $('#UserList').append('<li><a href="#" data-identity="' + user.id + '">'+user.name+'</a></li>');
    });
}
