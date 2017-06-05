package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Connector.Connector;
import Connector.DALException;
import DAOInterfaces.IProduktBatchKompDAO;
import DTO.ProduktBatchKompDTO;

public class ProduktBatchKompDAO implements IProduktBatchKompDAO
{

	@Override
	public ProduktBatchKompDTO getProduktBatchKomp(int pbId, int rbId) throws Exception 
	{
		ResultSet rs = Connector.doQuery("SELECT pbId, rbId, tara, netto, operatorId FROM productBatchComponent WHERE pbId = " + pbId + " AND rbId = " + rbId);

			try {

				if (!rs.first()) throw new Exception("Product batch component with pbId="+pbId+" and rbId="+rbId+" does not exist.");

				return new ProduktBatchKompDTO
				(
						rs.getInt("pbId"),
						rs.getInt("rbId"),
						rs.getDouble("tara"),
						rs.getDouble("netto"),
						rs.getInt("oprId")
				);

			} catch (SQLException e) 
			{
				throw new DALException(e);
			}
	}

	@Override
	public List<ProduktBatchKompDTO> getProduktBatchKompList(int pbId) throws Exception 
	{
		List<ProduktBatchKompDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT pbId, rbId, tara, netto, oprId FROM productBatchComponent WHERE productBatchId = " + pbId);

		try 
		{
			while (rs.next()) 
			{
				list.add
				(			new ProduktBatchKompDTO
							(
							rs.getInt("pbId"),
							rs.getInt("rbId"),
							rs.getDouble("tara"),
							rs.getDouble("netto"),
							rs.getInt("oprId")
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
	public List<ProduktBatchKompDTO> getProduktBatchKompList() throws Exception 
	{
		List<ProduktBatchKompDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT productBatchId, materialBatchId, tara, netto, operatorId FROM productBatchComponent");

		try 
		{

			while (rs.next()) 
			{

				list.add(

						new ProductBatchComponent(

								rs.getInt("productBatchId"),

								rs.getInt("materialBatchId"),

								rs.getDouble("tara"),

								rs.getDouble("netto"),

								rs.getInt("operatorId")

						)

				);

			}

		} catch (SQLException e) {

			throw new DALException(e);

		}

		return list;
	}

	@Override
	public void createProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException, Exception {
		// TODO Auto-generated method stub
		
	}

}
