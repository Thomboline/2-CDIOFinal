package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.ReceptDTO;
import dtointerfaces.IReceptDTO;

public interface IReceptDAO 
{
	IReceptDTO getRecept(int receptId) throws DALException, Exception;
	List<IReceptDTO> getReceptList() throws DALException, Exception;
	void createRecept(IReceptDTO recept) throws DALException, Exception;
	void updateRecept(IReceptDTO recept) throws DALException, Exception;
}