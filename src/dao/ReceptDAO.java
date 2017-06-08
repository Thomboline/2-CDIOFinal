package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connector.Connector;
import connector.DALException;
import daointerfaces.IReceptDAO;
import dto.ReceptDTO;

public class ReceptDAO implements IReceptDAO
{
	Connector c = new Connector();
	
	@Override
	public ReceptDTO getRecept(int receptId) throws Exception 
	{
		ResultSet rs = Connector.doQuery("SELECT * FROM recept WHERE receptId = " + receptId);

		try 
		{
			if (!rs.first()) throw new DALException("Recept with receptId="+receptId+" does not exist.");

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

		ResultSet rs = Connector.doQuery("SELECT * FROM recept");

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
				String.format("CALL createRecept('%d', '%s');", recept.getReceptId(), recept.getReceptNavn())
		);
	}

	@Override
	public void updateRecept(ReceptDTO recept) throws Exception 
	{
		Connector.doUpdate
		(
				String.format("CALL updateRecept('%d', '%s');", recept.getReceptId(), recept.getReceptNavn())
		);
	}
}
