package jersey;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
	
	
	@GET
	@Path("/verify")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public boolean verifyBruger(BrugerDTO user) throws Exception 
	{
		System.out.println("EN LILLE TEST");
		return dao.login(user);
	}
	
}
