$(document).ready(function() {
	$('#ListUsers').submit(function(event) {
		event.preventDefault();
		console.log("1");
		ListUsers();
		return false;
	});
	
	$('#CreateUser').submit(function(event) {
		event.preventDefault();
		var user = {};
		user.UserID = ("[id*=BrugerId]").val();
		user.Navn = ("[id*=Navn]").val();
		user.Ini = ("[id*=Initialer]").val();
		user.Cpr = ("[id*=cpr]").val();
		CreateUser(user);
		return false;
	});
	
	$('#EditUser').submit(function(event) {
		event.preventDefault();
		var user = {};
		user.UserID = ("[id*=BrugerId]").val();
		user.Navn = ("[id*=Navn]").val();
		user.Ini = ("[id*=Initialer]").val();
		user.Cpr = ("[id*=cpr]").val();
		EditUser();
		return false;
	});
	
	$('#PasswordUser').submit(function(event) {
		event.preventDefault();
		var user = {};
		user.UserID = ("[id*=userid]").val();
		PasswordUser(user);
		return false;
	});
	
	$('#DeleteUser').submit(function(event) {
		event.preventDefault();
		var user = {};
		user.UserID = ("[id*=userid]").val();
		DeleteUser(user);
		return false;
	});
});

function ListUsers() {
	$.ajax({
		type: 'GET',
		url: '/UserService/users',
		dataType: 'json',
		converters: {
			'text json': true
		},
		success: function() {
			$("#wrapper").html(renderList());
		}
	});
}

function CreateUser() {
	$.ajax({
		type: 'POST',
		url: 'http://localhost:8080/CDIOFinal/HTML/HomePage.html',
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
		url: 'http://localhost:8080/CDIOFinal/HTML/HomePage.html',
		dataType: "json",
		data: 'user: ' + JSON.stringify(user) + '}',
		contentType: "application/json; charset=utf-8",
		success: function (response) {
			alert("User edited");
		},
		error: function (errorThrown) {
			alert("Unsuccessful");
		}
	});
} 

function PasswordUser() {
	$.ajax({
		
	});
}

function DeleteUser() {
	$.ajax({
		type: 'PUT',
		url: 'http://localhost:8080/CDIOFinal/HTML/HomePage.html',
		dataType: "json",
		data: 'user: ' + JSON.stringify(user) + '}',		
	});
}


function renderList(data) {
	var list = data == null ? [] : (data instanceof Array ? data : [data]);

	$.each(list, function(index, user) {
		$('#UserList').append('<li><a href="#" data-identity="' + user.id + '">'+user.name+'</a></li>');
	});
}