package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.ProduktBatchDTO;
import dtointerfaces.IProduktBatchDTO;

public interface IProduktBatchDAO 
{
	IProduktBatchDTO getProduktBatch(int pbId) throws DALException, Exception;
	List<IProduktBatchDTO> getProduktBatchList() throws DALException, Exception;
	void createProduktBatch(IProduktBatchDTO produktbatch) throws DALException, Exception;
	void updateProduktBatch(IProduktBatchDTO produktbatch) throws DALException, Exception;
}
