package jersey;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import connector.DALException;
import dao.RaavareDAO;
import daointerfaces.IRaavareDAO;
import dto.RaavareDTO;


@Path("/RaavareService")
public class RaavareAdmin_Jersey {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String showMessage() {
        return "it works!";
    }



    IRaavareDAO dao = new RaavareDAO();

    @GET
    @Path("/raavare")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<RaavareDTO> ListRaavare() throws Exception {

        return dao.getRaavareList();
    }

    @PUT
    @Path("/raavare/{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RaavareDTO update(RaavareDTO raavare) throws Exception {
        System.out.println("Updating raavare: " + (raavare.getRaavareId()));

        dao.updateRaavare(raavare);
        return raavare;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RaavareDTO create(RaavareDTO raavare) throws Exception {
        System.out.println("Creating raavare: " + raavare.getRaavareNavn());

        dao.createRaavare(raavare);

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
}