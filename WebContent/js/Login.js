/**
 * Created by Bij og Stu on 07/06/2017.
 */
$(document).ready(function(event) 
{
	$('#Login').submit(function(event) 
	{
		data = $('#Login').serializeArray();
		event.preventDefault();
		var tempUser = {
		brugerId : data[0]['value'],
		password : data[1]['value']
		};
		Login(tempUser);
		return false;
	});
});

function Login(tempUser) {
$.ajax({
	type: 'POST',
	url: 'rest/LoginService/verify',
	dataType: "json",
	data: JSON.stringify(tempUser),
	contentType: "application/json",
	success: function (response) 
	{
		getRolle(tempUser[Object.keys(tempUser)[0]], response);

	},
	error: function (jqXHR, textStatus, errorThrown){
		
		alert("Login unsuccesful: " + textStatus);
	}
});
}
function getRolle(id, verify) 
{
		$.ajax({
		type: 'GET',
		url: 'rest/LoginService/rolle/' + id,
		dataType: 'json',
		async: false,
		converters: {
			'text json': true
		},
		success: function(response) 
		{
			if(verify == true)
			{
				if(response == admin)
				{
//					Et kald til main.js
//					alert("id er: " + id);
//					alert("Rolle er: " + response);
					window.location.replace("http://localhost:8080/CDIOFinal/HomePage.html");
				}
				else if(response == farmaceut)
				{
					
				}
				else if(response == vaerkfoerer)
				{
					
				}
				else if(response == laborant)
				{
					
				}
				else if(response == root)
				{
					
				}
				
			}
			else
			{
				alert("Login unsuccesful, wrong id or password!");
			}
		},
		error: function (jqXHR, textStatus, errorThrown)
		{
			alert("Dette er id " + searchKey);
			alert("Could not receive the role: " + textStatus);
		}
	});	
}
