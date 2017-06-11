package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.ProduktBatchKompDTO;
import dtointerfaces.IProduktBatchKompDTO;

public interface IProduktBatchKompDAO 
{
	IProduktBatchKompDTO getProduktBatchKomp(int pbId, int rbId) throws DALException, Exception;
	List<IProduktBatchKompDTO> getProduktBatchKompList(int pbId) throws DALException, Exception;
	List<IProduktBatchKompDTO> getProduktBatchKompList() throws DALException, Exception;
	void createProduktBatchKomp(IProduktBatchKompDTO produktbatchkomponent) throws DALException, Exception;
	void updateProduktBatchKomp(IProduktBatchKompDTO produktbatchkomponent) throws DALException, Exception;
}
