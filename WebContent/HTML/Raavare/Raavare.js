/**
 * Created by Thomasjakobsen on 07/06/2017.
 */

function Search(input) {
	if (input == "")
		ListRaavare();
	else 
		SearchById(input);
}

function SearchById(input) {
	$.ajax({
		type: 'GET',
		url: 'http://localhost:8080/CDIOFinal/HTML/Raavare/Search' + input,
		dataType: "json",
		data: "hej",
		success: function (event) {
			
		}
	});
}