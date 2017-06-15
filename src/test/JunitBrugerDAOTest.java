package test;

import static org.junit.Assert.*;

import org.junit.Test;

import connector.DALException;
import dao.BrugerDAO;
import daointerfaces.IBrugerDAO;
import dto.BrugerDTO;

public class JunitBrugerDAOTest 
{
	@Test
	public void getBrugerTest() throws DALException, Exception
	{
		IBrugerDAO BrugerDAO = new BrugerDAO();
		
		BrugerDTO Expected = new BrugerDTO(1, "Admin", "A", "root", "root", "root", 1);
		BrugerDTO Result = BrugerDAO.getBruger(1);
	
	    assertEquals(Expected.toString(), Result.toString());
	}
	@Test
	public void updateBrugerTest() throws DALException, Exception
	{
		IBrugerDAO BrugerDAO = new BrugerDAO();
		
		BrugerDTO Expected = new BrugerDTO(6, "Ole Pedersen","OP","test1234","010470-1132", "vaerkfoerer", 0);
		BrugerDAO.updateBruger(Expected);
		
		BrugerDTO Result = BrugerDAO.getBruger(6);
		
		assertEquals(Expected.toString(), Result.toString());
	}
	
	@Test
	public void createBrugerTest() throws DALException, Exception
	{
		IBrugerDAO BrugerDAO = new BrugerDAO();
		
		BrugerDTO Expected = new BrugerDTO(7, "Sao Feng","SF","test123","010470-1132", "vaerkfoerer", 1);
		BrugerDAO.createBruger(Expected);
		
		BrugerDTO Result = BrugerDAO.getBruger(7);
			
		assertEquals(Expected.toString(), Result.toString());
	}
	@Test
	public void deleteBrugerTest() throws DALException, Exception
	{
		IBrugerDAO BrugerDAO = new BrugerDAO();
		
		int Expected = 0;
		BrugerDAO.deleteBruger(6);
		
		int Result = BrugerDAO.getBruger(6).getStatus();
			
		assertEquals(Expected, Result);
	}
	@Test
	public void loginTest() throws DALException, Exception
	{
		IBrugerDAO BrugerDAO = new BrugerDAO();
		
		BrugerDTO login = new BrugerDTO(1, null,null,"root",null, null, 1);
		
		boolean Expected = true;
		boolean Result = BrugerDAO.login(login);
			
		assertEquals(Expected, Result);
	}
	@Test
	public void getRolleTest() throws DALException, Exception
	{
		IBrugerDAO BrugerDAO = new BrugerDAO();
		
		String Expected = "root";
		String Result = BrugerDAO.getRolle(1);
			
		assertEquals(Expected, Result);
	}
	
}
