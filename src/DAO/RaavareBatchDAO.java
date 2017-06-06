package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Connector.Connector;
import Connector.DALException;
import DAOInterfaces.IRaavareBatchDAO;
import DTO.RaavareBatchDTO;

public class RaavareBatchDAO implements IRaavareBatchDAO
{

	@Override
	public RaavareBatchDTO getRaavareBatch(int rbId) throws DALException, Exception 
	{
		ResultSet rs = Connector.doQuery("SELECT rbId, raavareId, maengde FROM RaavareBatch WHERE rbId =" + rbId);

		try 
		{
			if (!rs.first()) throw new DALException("Raavare batch with id="+rbId+" does not exist.");

			return new RaavareBatchDTO
			(
					rs.getInt("rbId"),
					rs.getInt("raavareId"),
					rs.getDouble("maengde")
			);

		} catch (SQLException e) 
		{
			throw new DALException(e);
		}
	}

	@Override
	public List<RaavareBatchDTO> getRaavareBatchList() throws DALException, Exception 
	{
		List<RaavareBatchDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT rbId, raavareId, maengde FROM RaavareBatch");

		try 
		{
			while (rs.next()) 
			{
				list.add
				(
						new RaavareBatchDTO
						(
								rs.getInt("rbId"),
								rs.getInt("raavareId"),
								rs.getDouble("maengde")
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
	public List<RaavareBatchDTO> getRaavareBatchList(int raavareId) throws DALException, Exception 
	{
		List<RaavareBatchDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("CALL RaavareBatch By Raavare("+raavareId+");");

		try 
		{
			while (rs.next()) 
			{
				list.add
				(
						new RaavareBatchDTO
						(
								rs.getInt("rbId"),
								rs.getInt("raavareId"),
								rs.getDouble("maengde")
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
	public void createRaavareBatch(RaavareBatchDTO raavarebatch) throws DALException, Exception 
	{
		Connector.doUpdate(

				String.format
				("CALL createRaavareBatch(%d,'%d','%d');",

						raavarebatch.getRaavareBatchId(),
						raavarebatch.getRaavareId(),
						raavarebatch.getMaengde()
				)
		);
	}

	@Override
	public void updateRaavareBatch(RaavareBatchDTO raavarebatch) throws DALException, Exception 
	{
		Connector.doUpdate(

				String.format
				("CALL updateRaavareBatch(%d,'%d','%d');",

						raavarebatch.getRaavareBatchId(),
						raavarebatch.getRaavareId(),
						raavarebatch.getMaengde()
				)
		);
		
	}

}
