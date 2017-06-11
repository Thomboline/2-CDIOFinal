package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.ReceptKompDTO;
import dtointerfaces.IReceptKompDTO;

public interface IReceptKompDAO 
{
	IReceptKompDTO getReceptKomp(int receptId, int raavareId) throws DALException, Exception;
	List<IReceptKompDTO> getReceptKompList(int receptId) throws DALException, Exception;
	List<IReceptKompDTO> getReceptKompList() throws DALException, Exception;
	void createReceptKomp(IReceptKompDTO receptkomponent) throws DALException, Exception;
	void updateReceptKomp(IReceptKompDTO receptkomponent) throws DALException, Exception;
}