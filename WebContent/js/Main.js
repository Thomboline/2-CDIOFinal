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
        LoginValidation(tempUser);
        ClearLogin();
        return false;
    });
});

function ClearLogin() 
{
    document.getElementById("Login").reset();
}

function LoginValidation(tempUser) 
{
    $.ajax({
        type: 'POST',
        url: 'rest/LoginService/verify',
        dataType: "json",
        data: JSON.stringify(tempUser),
        contentType: "application/json",
        success: function (response)
        {
        	UserRights(tempUser[Object.keys(tempUser)[0]], response);
        },
        error: function (jqXHR, textStatus, errorThrown){

            alert("Login unsuccesful: " + textStatus);
        }
    });
}
function UserRights(id, verify)
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
            	RolleVerify(response);
                ShowHide();
            }
            else
            {
                alert("Login unsuccesful, wrong id or password!");
            }
        },
        error: function (jqXHR, textStatus, errorThrown)
        {
            alert("Could not receive the role: " + textStatus);
        }
    });
}


function ShowHide() 
{
    $("#Login").hide();
}

function RolleVerify(Rolle) 
{
            if (Rolle == "farmaceut") {
                $(document).ready(function () {
                    $(".container").show();
                    $(".Farmaceut").load("Administration.html #RaavareAdmin, #RaavareBatchAdmin, #ProduktBatchAdmin, #ReceptAdmin", function () {
                        $("#LR").on("click", function () {
                            $("#wrapper").load("Raavare/ListRaavare.html");
                        });

                        $("#CR").on("click", function () {
                            $("#wrapper").load("Raavare/CreateRaavare.html");
                        });

                        $("#ER").on("click", function () {
                            $("#wrapper").load("Raavare/EditRaavare.html");
                        });

                        $("#DR").on("click", function () {
                            $("#wrapper").load("Raavare/DeleteRaavare.html");
                        });

                        $("#LRB").on("click", function () {
                            $("#wrapper").load("RaavareBatch/ListRaavareBatch.html");
                        });

                        $("#CRB").on("click", function () {
                            $("#wrapper").load("RaavareBatch/CreateRaavareBatch.html");
                        });

                        $("#ERB").on("click", function () {
                            $("#wrapper").load("RaavareBatch/EditRaavareBatch.html");
                        });

                        $("#DRB").on("click", function () {
                            $("#wrapper").load("RaavareBatch/DeleteRaavareBatch.html");
                        });

                        $("#LPB").on("click", function () {
                            $("#wrapper").load("ProduktBatch/ListProduktBatch.html");
                        });

                        $("#CPB").on("click", function () {
                            $("#wrapper").load("ProduktBatch/CreateProduktBatch.html");
                        });

                        $("#LRE").on("click", function () {
                            $("#wrapper").load("Recept/ListRecept.html");
                        });

                        $("#CRE").on("click", function () {
                            $("#wrapper").load("Recept/CreateRecept.html");
                        });

                        $("#ERE").on("click", function () {
                            $("#wrapper").load("Recept/EditRecept.html");
                        });

                        $("#DRE").on("click", function () {
                            $("#wrapper").load("Recept/DeleteRecept.html");
                        });
                    });
                })
                }

            else if (Rolle == "vaerkfoerer") {
                $(document).ready(function () {
                    $(".container").show();
                    $(".Vaerkfoerer").load("Administration.html #RaavareBatchAdmin, #ProduktBatchAdmin", function () {
                        $("#LRB").on("click", function () {
                            $("#wrapper").load("RaavareBatch/ListRaavareBatch.html");
                        });

                        $("#CRB").on("click", function () {
                            $("#wrapper").load("RaavareBatch/CreateRaavareBatch.html");
                        });

                        $("#ERB").on("click", function () {
                            $("#wrapper").load("RaavareBatch/EditRaavareBatch.html");
                        });

                        $("#DRB").on("click", function () {
                            $("#wrapper").load("RaavareBatch/DeleteRaavareBatch.html");
                        });

                        $("#LPB").on("click", function () {
                            $("#wrapper").load("ProduktBatch/ListProduktBatch.html");
                        });

                        $("#CPB").on("click", function () {
                            $("#wrapper").load("ProduktBatch/CreateProduktBatch.html");
                        });

                    });
                })
                }

            else if (Rolle == "laborant") {
                $(document).ready(function () {
                    $(".container").show();
                    $(".Laborant").load("Administration.html #RaavareBatchAdmin, #ProduktBatchAdmin", function () {
                        $("#LRB").on("click", function () {
                            $("#wrapper").load("RaavareBatch/ListRaavareBatch.html");
                        });
                        $("#LPB").on("click", function () {
                            $("#wrapper").load("ProduktBatch/LisrProduktBatch.html");
                        });
                    });
                })
                }

            else if (Rolle == "admin") {
                $(document).ready(function () {
                    $(".container").show();
                    $(".Administrator").load("Administration.html #UserAdmin", function () {
                        $("#LU").on("click", function () {
                            $("#wrapper").load("User/ListUser.html");
                        });

                        $("#CU").on("click", function () {
                            $("#wrapper").load("User/CreateUser.html");
                        });

                        $("#EU").on("click", function () {
                            $("#wrapper").load("User/EditUser.html");
                        });

                        $("#DU").on("click", function () {
                            $("#wrapper").load("User/DeleteUser.html");
                        });

                        $("#RP").on("click", function () {
                            $("#wrapper").load("User/ResetPassword.html");
                        });
                    });
                })
                }
            $(document).ready(function () {
                $("#ProfilIndstillinger").on("click", function () {
                    $("#wrapper").load("User/ProfileSettings.html")
            })
            });
}


$("#LogUd").on("click", function () { 
    $("#wrapper").empty(); 
    $(".container").hide(); 
    $(".Administrator").empty(); 
    $(".Farmaceut").empty(); 
    $("#Login").show() }); 


