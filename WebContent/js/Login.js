/**
 * Created by Bij og Stu on 07/06/2017.
 */
$(document).ready(function(event) {
	
	$('#Login').submit(function(event) {
		data = $('#Login').serializeArray();
		event.preventDefault();
		var tempUser = {
		brugerId : data[0]['value'],
		password : data[1]['value']
		};
		window.alert(JSON.stringify(tempUser));
		Login(tempUser);
		return false;
	});
});

function Login(tempUser) {
$.ajax({
	type: 'GET',
	url: 'rest/LoginService',
	dataType: "json",
	data: JSON.stringify(tempUser),
	contentType: "application/json",
	success: function (response) {
		alert("Login succesful: " + response );
	},
	error: function (jqXHR, textStatus, errorThrown){
		
		alert("Login unsuccesful: " + textStatus);
	}
});
}
