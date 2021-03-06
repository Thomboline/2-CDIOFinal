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
import dao.ReceptDAO;
import dao.ReceptKompDAO;
import daointerfaces.IReceptDAO;
import daointerfaces.IReceptKompDAO;
import dto.ReceptDTO;
import dto.ReceptKompDTO;
 

@Path("/ReceptService")
public class ReceptAdmin_Jersey {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String showMessage() {
        return "it works!";
    }

    IReceptDAO dao = new ReceptDAO();
    IReceptKompDAO daoKomp = new ReceptKompDAO();

    @GET
    @Path("/receptlist")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List <ReceptDTO> ListRecepts() throws Exception {

        return dao.getReceptList();
    }
    
    @GET
    @Path("/recept/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ReceptDTO ListRecept(@PathParam("id")int index) throws Exception {
    	
    	return dao.getRecept(index);
    }
    
    @GET
    @Path("/komponenter/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List <ReceptKompDTO> ListReceptKomp(@PathParam("id")int receptId) throws Exception {
    	
    	return daoKomp.getReceptKompList(receptId);
    }

    @PUT
    @Path("/recept")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ReceptDTO updateRecept(ReceptDTO recept) throws Exception {
 
        dao.updateRecept(recept);
 
        return recept;
    }
    
    @PUT
    @Path("/komponent/{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ReceptKompDTO updateKomp(ReceptKompDTO receptKomp) throws Exception {
 
        daoKomp.updateReceptKomp(receptKomp);
 
        return receptKomp;
    }
    
    @POST
    @Path("/createRecept")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ReceptDTO createRecept(ReceptDTO recept) throws Exception {

        dao.createRecept(recept);

        return recept;
    }
    
    @POST
    @Path("/createReceptKomp")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ReceptKompDTO createKomp(ReceptKompDTO receptKomp) throws Exception {
       
        daoKomp.createReceptKomp(receptKomp);

        return receptKomp;
    }

    @POST
    @Path("/recept/{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ReceptDTO delete(ReceptDTO recept) throws DALException {
        System.out.println("Updating user: " + recept.getReceptNavn());
//		dao.DeleteUser(user, 1);
        return recept;
    }
}