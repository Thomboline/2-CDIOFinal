package jersey;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.BrugerDAO;
import daointerfaces.IBrugerDAO;
import dto.BrugerDTO;

@Path("/LoginService")
public class LoginService {

	IBrugerDAO dao = new BrugerDAO();
	
	
	@GET
	@Path("users/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public BrugerDTO getBruger(BrugerDTO user) throws Exception {
		
		return dao.login(user);
	}
	
}
