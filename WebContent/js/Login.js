/**
 * Created by Bij og Stu on 07/06/2017.
 */
$(document).ready(function(event) {
	$('#Login').submit(function(event) {
		data = $('#Login').serializeArray();
		event.preventDefault();
		var tempUser = {
		brugerId : data[0]['value'],
		password : data[1]['value'],
		ini : data[2]["test"],
		password : data[3]["test"],
		cpr : data[4]["test"],
		rolle : data[5]["admin"],
		status : data[6]["1"]
		};
		window.alert("So far so good");
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
