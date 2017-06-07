package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.ReceptKompDTO;

public interface IReceptKompDAO 
{
	ReceptKompDTO getReceptKomp(int receptId, int raavareId) throws DALException, Exception;
	List<ReceptKompDTO> getReceptKompList(int receptId) throws DALException, Exception;
	List<ReceptKompDTO> getReceptKompList() throws DALException, Exception;
	void createReceptKomp(ReceptKompDTO receptkomponent) throws DALException, Exception;
	void updateReceptKomp(ReceptKompDTO receptkomponent) throws DALException, Exception;
}