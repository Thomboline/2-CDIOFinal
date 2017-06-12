package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.ReceptDTO;


public interface IReceptDAO 
{
	ReceptDTO getRecept(int receptId) throws DALException, Exception;
	List<ReceptDTO> getReceptList() throws DALException, Exception;
	void createRecept(ReceptDTO recept) throws DALException, Exception;
	void updateRecept(ReceptDTO recept) throws DALException, Exception;
}