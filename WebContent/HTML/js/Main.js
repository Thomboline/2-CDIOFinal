$(document).ready( function() {
	$("#LU").on("click", function() {
		$("#wrapper").load("User/ListUser.html");
	});
	
	$("#CU").on("click", function() {
		$("#wrapper").load("User/CreateUser.html");
	});
	
	$("#EU").on("click", function() {
		$("#wrapper").load("User/EditUser.html");
	});
	
	$("#DU").on("click", function() {
		$("#wrapper").load("User/DeleteUser.html");
	});
	
	$("#LR").on("click", function() {
		$("#wrapper").load("Raavare/ListRaavare.html");
	});
	
	$("#CR").on("click", function() {
		$("#wrapper").load("Raavare/CreateRaavare.html");
	});
	
	$("#ER").on("click", function() {
		$("#wrapper").load("Raavare/EditRaavare.html");
	});
	
	$("#DR").on("click", function() {
		$("#wrapper").load("Raavare/DeleteRaavare.html");
	});	
	
	$("#LRB").on("click", function() {
		$("#wrapper").load("RaavareBatch/ListRaavareBatch.html");
	});
	
	$("#CRB").on("click", function() {
		$("#wrapper").load("RaavareBatch/CreateRaavareBatch.html");
	});
	
	$("#ERB").on("click", function() {
		$("#wrapper").load("RaavareBatch/EditRaavareBatch.html");
	});
	
	$("#DRB").on("click", function() {
		$("#wrapper").load("RaavareBatch/DeleteRaavareBatch.html");
	});
	
	$("#LPB").on("click", function() {
		$("#wrapper").load("ProduktBatch/LisrProduktBatch.html");
	});
	
	$("#CPB").on("click", function() {
		$("#wrapper").load("ProduktBatch/CreateProduktBatch.html");
	});
	
	$("#EPB").on("click", function() {
		$("#wrapper").load("ProduktBatch/EditProduktBatch.html");
	});
	
	$("#DPB").on("click", function() {
		$("#wrapper").load("ProduktBatch/DeleteProduktBatch.html");
	});
	
	$("#LRE").on("click", function() {
		$("#wrapper").load("Recept/ListRecept.html");
	});
	
	$("#CRE").on("click", function() {
		$("#wrapper").load("Recept/CreateRecept.html");
	});
	
	$("#ERE").on("click", function() {
		$("#wrapper").load("Recept/EditRecept.html");
	});
	
	$("#DRE").on("click", function() {
		$("#wrapper").load("Recept/DeleteRecept.html");
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