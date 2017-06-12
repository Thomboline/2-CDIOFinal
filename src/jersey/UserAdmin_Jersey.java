/**
 * 
 */
/**
 * @author Thomas-PC
 *
 */
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

import dao.BrugerDAO;
import daointerfaces.IBrugerDAO;
import dto.BrugerDTO;

@Path("/UserService")
public class UserAdmin_Jersey {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String showMessage() {
		return "it works!";
	}
	
	IBrugerDAO dao = new BrugerDAO();
	
	@GET
	@Path("/userlist")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<BrugerDTO> ListUsers() throws Exception {	

		return dao.getBrugerList();
	}
	
	@GET
	@Path("/users/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public BrugerDTO ListUser(@PathParam("id")int index) throws Exception {
			
		return dao.getBruger(index);	
	}
	
	@PUT
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public BrugerDTO update(BrugerDTO user) throws Exception {

		dao.updateBruger(user);

		return user;
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public BrugerDTO create(BrugerDTO user) throws Exception {		
		
		dao.createBruger(user);
		
		return user;
	}
	
	@POST
	@Path("/password/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void resetPassword(@PathParam("id")int id) throws Exception {
		
		dao.resetPassword(id);
		
	}
	
	@POST
	@Path("/delete/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void delete(@PathParam("id")int id) throws Exception {
		
		dao.deleteBruger(id, 0);
		
	}
}