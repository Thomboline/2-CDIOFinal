package jersey;
//fgf
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.BrugerDAO;
import daointerfaces.IBrugerDAO;
import dto.BrugerDTO;

@Path("/LoginService")
public class LoginService 
{
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String showMessage() {
		return "it works 2!";
	}
	
	IBrugerDAO dao = new BrugerDAO();
	

	@POST
	@Path("/verify/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public boolean verifyBruger(BrugerDTO tempUser) throws Exception 
	{
		return dao.login(tempUser);
	}
	
	@GET
	@Path("/rolle/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public String ListUser(@PathParam("id")int id) throws Exception 
	{

		return dao.getRolle(id);	
	}
	
}
