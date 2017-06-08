$(document).ready(function() {
	$('#ListUsers').submit(function(event) {
		event.preventDefault();
		ListUsers();
		return false;
	});
	
	$('#CreateUser').submit(function(event) {
		event.preventDefault();
		var user = {};
		user.Username = ("[id*=username]").val();
		user.Password = ("[id*=password]").val();
		CreateUser(user);
		return false;
	});
	
	$('#EditUser').submit(function(event) {
		event.preventDefault();
		EditUser();
		return false;
	});
});

function ListUsers() {
	$.ajax({
		type: 'GET',
		url: 'http://localhost:8080/CDIOFinal/HTML/User',
		dataType: 'json',
		success: renderList
	});
}

function CreateUser() {
	$.ajax({
		type: 'POST',
		url: 'http://localhost:8080/CDIOFinal/HTML/User',
		dataType: "json",
		data: 'user: ' + JSON.stringify(user) + '}',
		contentType: "application/json; charset=utf-8",
		success: function (response) {
			alert("User created");
		},
		error: function (errorThrown){
			alert("Unsuccesful");
		}
	});
}

function EditUser() {
	$.ajax({
		type: 'PUT',
		url: 'http://localhost:8080/CDIOFinal',
		dataType: "json",
		data: ""
	});
}


function renderList(data) {
	var list = data == null ? [] : (data instanceof Array ? data : [data]);

	$.each(list, function(index, user) {
		$('#UserList').append('<li><a href="#" data-identity="' + user.id + '">'+user.name+'</a></li>');
	});
}