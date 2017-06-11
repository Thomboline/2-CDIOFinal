package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connector.Connector;
import connector.DALException;
import daointerfaces.IReceptKompDAO;
import dto.ReceptKompDTO;
import dtointerfaces.IReceptKompDTO;

public class ReceptKompDAO implements IReceptKompDAO
{
	Connector c = new Connector();
	
	@Override
	public IReceptKompDTO getReceptKomp(int receptId, int raavareId) throws DALException, Exception 
	{
		ResultSet rs = Connector.doQuery("SELECT * FROM receptkomponent WHERE receptId = " + receptId + " AND raavareId = "+ raavareId);

		try 
		{
			if (!rs.first())

				throw new DALException("Recept Komponen tmed recept id="+receptId+" og raavare id="+raavareId+" eksistere ikke.");

			return new ReceptKompDTO
			(
					rs.getInt("receptId"),
					rs.getInt("raavareId"),
					rs.getDouble("nomNetto"),
					rs.getDouble("tolerance")
			);
			
		} catch (SQLException e) 
		{
			throw new DALException(e);
		}
	}

	@Override
	public List<IReceptKompDTO> getReceptKompList(int receptId) throws DALException, Exception 
	{
		List<IReceptKompDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT * FROM receptkomponent WHERE receptId="+receptId);

		try 
		{
			while (rs.next()) 
			{
				list.add
				(
						new ReceptKompDTO
						(
								rs.getInt("receptId"),
								rs.getInt("raavareId"),
								rs.getDouble("nomNetto"),
								rs.getDouble("tolerance")
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
	public List<IReceptKompDTO> getReceptKompList() throws DALException, Exception 
	{
		List<IReceptKompDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT * FROM receptkomponent");

		try 
		{
			while (rs.next()) 
			{
				list.add(

						new ReceptKompDTO
						(
								rs.getInt("receptId"),
								rs.getInt("raavareId"),
								rs.getDouble("nomNetto"),
								rs.getDouble("tolerance")
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
	public void createReceptKomp(IReceptKompDTO receptkomponent) throws DALException, Exception 
	{
		Connector.doUpdate
		(
				String.format
				("CALL createReceptkomponent('%d', '%d', '%s', '%s');",

						receptkomponent.getReceptId(),
						receptkomponent.getRaavareId(),
						receptkomponent.getNomNetto(),
						receptkomponent.getTolerance()
				)
		);	
	}

	@Override
	public void updateReceptKomp(IReceptKompDTO receptkomponent) throws DALException, Exception 
	{
		Connector.doUpdate
		(
				String.format
				("CALL updateReceptkomponent('%d', '%d', '%s', '%s');",

						receptkomponent.getReceptId(),
						receptkomponent.getRaavareId(),
						receptkomponent.getNomNetto(),
						receptkomponent.getTolerance()
				)
		);	
		
	}

}
