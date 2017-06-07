package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.ProduktBatchKompDTO;

public interface IProduktBatchKompDAO 
{
	ProduktBatchKompDTO getProduktBatchKomp(int pbId, int rbId) throws DALException, Exception;
	List<ProduktBatchKompDTO> getProduktBatchKompList(int pbId) throws DALException, Exception;
	List<ProduktBatchKompDTO> getProduktBatchKompList() throws DALException, Exception;
	void createProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException, Exception;
	void updateProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException, Exception;
}
