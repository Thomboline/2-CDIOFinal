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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import connector.DALException;
import dao.BrugerDAO;
import daointerfaces.IBrugerDAO;
import dto.BrugerDTO;
import dtointerfaces.IBrugerDTO;


@Path("/UserService")
public class UserAdmin_Jersey {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String showMessage() {
		return "it works!";
	}
	
	IBrugerDAO dao = new BrugerDAO();
	
	@GET
	@Path("/users")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<IBrugerDTO> ListUsers() throws Exception {
		
		return dao.getBrugerList();
	}
	
	@PUT
	@Path("/users/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IBrugerDTO update(BrugerDTO user) throws Exception {
		System.out.println("Updating user: " + user.getBrugerNavn());

		dao.updateBruger(user);
		return user;
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IBrugerDTO create(BrugerDTO user) throws Exception {
		System.out.println("Creating user: " + user.getId());		
		
		dao.createBruger(user);
		
		return user;
	}
	
	@POST
	@Path("/users/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IBrugerDTO delete(BrugerDTO user) throws DALException {
		System.out.println("Updating user: " + user.getBrugerNavn());
//		dao.DeleteUser(user, 1);
		return user;
	}
}