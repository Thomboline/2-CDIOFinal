package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connector.Connector;
import daointerfaces.IProduktBatchDAO;
import dto.ProduktBatchDTO;

public class ProduktBatchDAO implements IProduktBatchDAO
{
	Connector c = new Connector();
	
	@Override
	public ProduktBatchDTO getProduktBatch(int pbId) throws Exception
	{
		ResultSet rs = Connector.doQuery("SELECT * FROM produktbatch WHERE pbId = " + pbId);

		try {
			if (!rs.first()) throw new Exception("Product batch with id="+pbId+" does not exist.");

			return new ProduktBatchDTO(
					rs.getInt("pbId"),
					rs.getInt("status"),
					rs.getInt("receptId")
			);
		} catch (SQLException e) 
		{
			throw new Exception(e);
		}
	}

	@Override
	public List<ProduktBatchDTO> getProduktBatchList() throws Exception 
	{
		List<ProduktBatchDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT * FROM produktbatch");

		try 
		{
			while (rs.next()) 
			{
				list.add
				(
						new ProduktBatchDTO
						(
								rs.getInt("pbId"),
								rs.getInt("status"),
								rs.getInt("receptId")
						)
				);
			}

		} catch (SQLException e) 
		{
			throw new Exception(e);
		}

		return list;
	}

	@Override
	public void createProduktBatch(ProduktBatchDTO produktbatch) throws Exception 
	{
		Connector.doUpdate
		(
				String.format
				("CALL createProduktBatch('%d', '%d', '%d');",

						produktbatch.getProduktBatchId(),
						produktbatch.getReceptId(),
						produktbatch.getStatus()
				)
		);
	}

	@Override
	public void updateProduktBatch(ProduktBatchDTO produktbatch) throws Exception 
	{
		Connector.doUpdate
		(
				String.format
				("CALL updateProduktBatch('%d', '%d', '%d');",

						produktbatch.getProduktBatchId(),
						produktbatch.getReceptId(),
						produktbatch.getStatus()
				)
		);
	}

}
