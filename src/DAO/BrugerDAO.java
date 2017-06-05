package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Connector.Connector;
import DAOInterfaces.DALException;
import DAOInterfaces.IBrugerDAO;
import DTO.BrugerDTO;

public class BrugerDAO implements IBrugerDAO
{

	@Override
	public BrugerDTO getBruger(int oprId) throws DALException 
	{
		ResultSet rs = Connector.doQuery("SELECT oprId, name, ini, cpr, password FROM bruger WHERE oprId = " + oprId);
		try 
		{
			if (!rs.first()) throw new Exception("Bruger with oprId="+oprId+" does not exist.");
			return new BrugerDTO
					(
					rs.getInt("oprId"),
					rs.getString("oprNavn"),
					rs.getString("ini"),
					rs.getString("cpr"),
					rs.getString("password")
					);
		} 
		catch (SQLException e) 
		{
			throw new Exception(e);
		}
	}

	@Override
	public List<BrugerDTO> getBrugerList() throws DALException 
	{
		List<OperatoerDTO> list = new ArrayList<OperatoerDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM operatoer");
		try
		{
			while (rs.next()) 
			{
				list.add(new OperatoerDTO(rs.getInt("opr_id"), rs.getString("opr_navn"), rs.getString("ini"), rs.getString("cpr"), rs.getString("password")));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}
	}

	@Override
	public void createBruger(BrugerDTO opr) throws DALException 
	{
		Connector.doUpdate
		(
				String.format
				("CALL createBruger(%d,'%s','%s','%s','%s');",

						opr.getId(),
						opr.getName(),
						opr.getIni(),
						opr.getCpr(),
						opr.getPassword()
				)
		);
	}

	@Override
	public void updateBruger(BrugerDTO opr) throws DALException 
	{
		Connector.doUpdate(
				"UPDATE operatoer SET  opr_navn = '" + opr.getOprNavn() + "', ini =  '" + opr.getIni() + 
				"', cpr = '" + opr.getCpr() + "', password = '" + opr.getPassword() + "' WHERE opr_id = " +
				opr.getOprId()
		);
	}
	

}
