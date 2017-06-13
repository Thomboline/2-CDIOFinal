function RolleVerify(Rolle)
{
	$(document).ready( function() 
	{
		if (Rolle == "farmaceut")
		{
			$(".Farmaceut").load("Administration.html #RaavareAdmin, #RaavareBatchAdmin, #ProduktBatchAdmin, #ReceptAdmin", function () {
		        $("#LR").on("click", function(){
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
		}
		else if(Rolle == "vaerkfoerer")
		{
			$(".Vaerkfoerer").load("Administration.html #RaavareBatchAdmin, #ProduktBatchAdmin", function () {
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
		    });
		}
		else if(Rolle == "laborant")
		{
			$(".Laborant").load("Administration.html #RaavareBatchAdmin, #ProduktBatchAdmin", function () {
		        $("#LRB").on("click", function() {
		            $("#wrapper").load("RaavareBatch/ListRaavareBatch.html");
		        });
		        $("#LPB").on("click", function() {
		            $("#wrapper").load("ProduktBatch/LisrProduktBatch.html");
		        });
		    });
		}
		else if(Rolle == "admin")
		{
			$(".Administrator").load("Administration.html #UserAdmin", function () {
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
		        
		        $("#RP").on("click", function() {
		        	$("#wrapper").load("User/ResetPassword.html");
		        });
		    });	
		}
	    $("#ProfilIndstillinger").on("click", function () 
	    {
	        $("#wrapper").load("User/ProfileSettings.html")
	    });
	});
}


