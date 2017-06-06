package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Connector.Connector;
import Connector.DALException;
import DAOInterfaces.IReceptDAO;
import DTO.ReceptDTO;

public class ReceptDAO implements IReceptDAO
{

	@Override
	public ReceptDTO getRecept(int receptId) throws Exception 
	{
		ResultSet rs = Connector.doQuery("SELECT receptId, name FROM receipt WHERE receptId = " + receptId);

		try 
		{
			if (!rs.first()) throw new DALException("Receipt with receptId="+receptId+" does not exist.");

			return new ReceptDTO
			(
					rs.getInt("receptId"),
					rs.getString("receptNavn")
			);

		} catch (SQLException e) 
		{
			throw new DALException(e);
		}
	}

	@Override
	public List<ReceptDTO> getReceptList() throws Exception 
	{
		List<ReceptDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT id, name FROM receipt");

		try 
		{
			while (rs.next()) 
			{
				list.add
				(
						new ReceptDTO
						(
								rs.getInt("receptId"),
								rs.getString("receptNavn")
						)
				);
			}

		} catch (SQLException e) 
		{
			throw new DALException(e);
		}

		return list;
	}
	
	@Override
	public void createRecept(ReceptDTO recept) throws Exception 
	{
		Connector.doUpdate
		(
				String.format("CALL createRecept(&d, '%s');", recept.getId(), recept.getName())
		);
	}

	@Override
	public void updateRecept(ReceptDTO recept) throws Exception 
	{
		Connector.doUpdate
		(
				String.format("CALL updateRecept(&d, '%s');", recept.getId(), recept.getName())
		);
	}
}
