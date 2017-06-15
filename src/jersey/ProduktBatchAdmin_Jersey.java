package jersey;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import connector.DALException;
import dao.ProduktBatchDAO;
import dao.ProduktBatchKompDAO;
import daointerfaces.IProduktBatchDAO;
import daointerfaces.IProduktBatchKompDAO;
import dto.ProduktBatchDTO;
import dto.ProduktBatchKompDTO;

@Path("/ProduktBatchService")
public class ProduktBatchAdmin_Jersey {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String showMessage() {
        return "it works!";
    }

    IProduktBatchDAO dao = new ProduktBatchDAO();
    IProduktBatchKompDAO daoKomp = new ProduktBatchKompDAO();
    
    @GET
    @Path("/produktBatchList")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<ProduktBatchDTO> ListProduktBatches() throws Exception {

        return dao.getProduktBatchList();
    }
    
    @GET
    @Path("/produktBatch/{id}")
    @Produces ({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ProduktBatchDTO ListProduktBatch(@PathParam("id")int id) throws Exception {
    	
    	return dao.getProduktBatch(id);
    }
    
    @GET
    @Path("/komponenter/{id}")
    @Produces ({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List <ProduktBatchKompDTO> ListProduktKomp(@PathParam("id")int id) throws Exception {
    	
    	return daoKomp.getProduktBatchKompList(id);
    }
    
    @PUT
    @Path("/produktBatch/{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ProduktBatchDTO update(ProduktBatchDTO produktbatch) throws Exception {
       
    	return produktbatch;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ProduktBatchDTO create(ProduktBatchDTO produktbatch) throws Exception {
        System.out.println("Creating produktbatch: " + produktbatch.getProduktBatchId());

        dao.createProduktBatch(produktbatch);

        return produktbatch;
    }

    @POST
    @Path("/produktbatch/{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ProduktBatchDTO delete(ProduktBatchDTO produktbatch) throws DALException {
        System.out.println("Updating user: " + produktbatch.getProduktBatchId());
//		dao.DeleteUser(user, 1);
        return produktbatch;
    }
}