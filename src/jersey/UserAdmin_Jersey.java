/**
 * 
 */
/**
 * @author Thomas-PC
 *
 */
package jersey;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import connector.DALException;
import dto.BrugerDTO;
import dtointerfaces.IBrugerDTO;

@Path("/User") 
public class UserAdmin_Jersey {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String showMessage() {
		return "it works!";
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<IBrugerDTO> ListUsers() throws DALException {
		System.out.println("findAll");
		
		List<IBrugerDTO> list = new ArrayList<>();
		
		list.add(new BrugerDTO(1, "hej", "med", "dig", "det", "virker", 1));
		
		return list;
		
		//return dao.getUserList();
	}
	
	@PUT @Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IBrugerDTO update(IBrugerDTO user) throws DALException {
		System.out.println("Updating user: " + user.getBrugerNavn());
		
		return user;

//		dao.updateUser(user, 1);
//		return user;
	}
	
	@PUT @Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IBrugerDTO delete(IBrugerDTO user) throws DALException 
	{
		System.out.println("Updating user: " + user.getBrugerNavn());
		dao.DeleteUser(user, 1);
		return user;
	}
}