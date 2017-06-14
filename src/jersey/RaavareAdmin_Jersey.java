package jersey;

import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import connector.DALException;
import dao.RaavareBatchDAO;
import dao.RaavareDAO;
import daointerfaces.IRaavareBatchDAO;
import daointerfaces.IRaavareDAO;
import dto.BrugerDTO;
import dto.RaavareBatchDTO;
import dto.RaavareDTO;


@Path("/RaavareService")
public class RaavareAdmin_Jersey {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String showMessage() {
        return "it works!";
    }



    IRaavareDAO raavare_dao = new RaavareDAO();
    IRaavareBatchDAO raavareBatch_dao = new RaavareBatchDAO();

    @GET
    @Path("/raavarelist")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<RaavareDTO> ListRaavarer() throws Exception {

        return raavare_dao.getRaavareList();
    }
    @GET
    @Path("/raavare/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public RaavareDTO ListRaavare(@PathParam("id")int index) throws Exception {

        return raavare_dao.getRaavare(index);
    }
    @PUT
    @Path("/update")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RaavareDTO update(RaavareDTO raavare) throws Exception {
        System.out.println("Updating raavare: " + (raavare.getRaavareId()));

        raavare_dao.updateRaavare(raavare);
        return raavare;
    }

    @POST
    @Path("/createRaavare")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RaavareDTO create(RaavareDTO raavare) throws Exception {
        System.out.println("Creating raavare: " + raavare.getRaavareNavn());

        raavare_dao.createRaavare(raavare);

        return raavare;
    }    
    
    @POST
    @Path("/createRaavareBatch")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RaavareBatchDTO create(RaavareBatchDTO raavareBatch) throws Exception {
    	
    	raavareBatch_dao.createRaavareBatch(raavareBatch);

        return raavareBatch;
    }
    @GET
	@Path("/raavareBatchList/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public RaavareBatchDTO ListUser(@PathParam("id")int index) throws Exception {
			
		return raavareBatch_dao.getRaavareBatch(index);	
	}
    
    @GET
	@Path("/RaavareBatchList")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<RaavareBatchDTO> ListUsers() throws Exception {	

		return raavareBatch_dao.getRaavareBatchList();
	}
    @PUT
    @Path("/updateRaavareBatch")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RaavareBatchDTO update(RaavareBatchDTO raavarebatch) throws Exception {
        System.out.println("Updating raavare: " + (raavarebatch.getRbId()));

        raavareBatch_dao.updateRaavareBatch(raavarebatch);
        return raavarebatch;
    }
}