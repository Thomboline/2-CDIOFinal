package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import DAOInterfaces.DALException;
import DAOInterfaces.IBrugerDAO;
import DTO.BrugerDTO;

public class BrugerDAO implements IBrugerDAO
{

	@Override
	public BrugerDTO getBruger(int oprId) throws DALException 
	{
		ResultSet rs = Connector.doQuery("SELECT * FROM operatoer WHERE opr_id = " + oprId);
	    try 
	    {
	    	if (!rs.first()) throw new DALException("Operatoeren " + oprId + " findes ikke");
	    	return new BrugerDTO ();
	    }
	    catch (SQLException e) {throw new DALException(e);}
	}

	@Override
	public List<BrugerDTO> getBrugerList() throws DALException 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createBruger(BrugerDTO opr) throws DALException 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBruger(BrugerDTO opr) throws DALException 
	{
		// TODO Auto-generated method stub
		
	}
	

}
