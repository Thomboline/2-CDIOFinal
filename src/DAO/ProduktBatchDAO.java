package DAO;

import java.sql.ResultSet;
import java.util.List;

import Connector.Connector;
import DAOInterfaces.DALException;
import DAOInterfaces.IProduktBatchDAO;
import DTO.ProduktBatchDTO;

public class ProduktBatchDAO implements IProduktBatchDAO
{

	@Override
	public ProduktBatchDTO getProduktBatch(int pbId) throws DALException 
	{
		ResultSet rs = Connector.doQuery("SELECT id, status, receiptId FROM productBatch WHERE id = " + id);

		try {
			if (!rs.first()) throw new DALException("Product batch with id="+id+" does not exist.");

			return new ProduktBatchDTO(
					rs.getInt("oprId"),
					rs.getInt("status"),
					rs.getInt("receiptId")
			);
		} catch (SQLException e) 
		{
			throw new DALException(e);
		}
	}

	@Override
	public List<ProduktBatchDTO> getProduktBatchList() throws DALException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createProduktBatch(ProduktBatchDTO produktbatch) throws DALException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProduktBatch(ProduktBatchDTO produktbatch) throws DALException {
		// TODO Auto-generated method stub
		
	}

}
