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
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RaavareDTO create(RaavareDTO raavare) throws Exception {
        System.out.println("Creating raavare: " + raavare.getRaavareNavn());

        raavare_dao.createRaavare(raavare);

        return raavare;
    }

    @POST
    @Path("/raavare/{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RaavareDTO delete(RaavareDTO raavare) throws DALException {
        System.out.println("Updating raavare: " + raavare.getRaavareNavn());
//		dao.DeleteUser(user, 1);
        return raavare;
    }
    
    
    @POST
    @Path("/RaavareBatchService")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RaavareBatchDTO create(RaavareBatchDTO raavareBatch) throws Exception {
    	System.out.println("Creating raavarebatch: " + raavareBatch.getRaavareBatchId());
        raavareBatch_dao.createRaavareBatch(raavareBatch);

        return raavareBatch;
    }
}