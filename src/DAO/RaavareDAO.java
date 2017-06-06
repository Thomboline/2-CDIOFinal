package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Connector.Connector;
import Connector.DALException;
import DAOInterfaces.IRaavareDAO;
import DTO.RaavareDTO;

public class RaavareDAO implements IRaavareDAO
{

	@Override
	public RaavareDTO getRaavare(int raavareId) throws Exception 
	{
		ResultSet rs = Connector.doQuery("SELECT * FROM raavare WHERE raavareId = " + raavareId);
		try 
		{
			if (!rs.first()) throw new DALException("Raavare with raavareId="+raavareId+" does not exist.");

			return new RaavareDTO
			(
					rs.getInt("raavareId"),
					rs.getString("name"),
					rs.getString("supplier")
			);

		} catch (SQLException e) 
		{
			throw new DALException(e);
		}
	}

	@Override
	public List<RaavareDTO> getRaavareList() throws Exception 
	{
		List<RaavareDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT raavareId, raavareNavn, leverandoer FROM raavare");

		try 
		{
			while (rs.next()) 
			{
				list.add
				(
						new RaavareDTO
						(
								rs.getInt("raavareId"),
								rs.getString("raavareNavn"),
								rs.getString("leverandoer")
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
	public void createRaavare(RaavareDTO raavare) throws Exception 
	{
		Connector.doUpdate
		(

				String.format
				("CALL createRaavare(%d,'%s','%s');",

						raavare.getRaavareId(),
						raavare.getRaavareNavn(),
						raavare.getLeverandoer()
				)
		);
	}

	@Override
	public void updateRaavare(RaavareDTO raavare) throws Exception 
	{
		Connector.doUpdate
		(

				String.format
				("CALL  updateRaavare(%d,'%s','%s');",

						raavare.getRaavareId(),
						raavare.getRaavareNavn(),
						raavare.getLeverandoer()
				)
		);
	}

}
