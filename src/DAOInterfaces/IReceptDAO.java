package DAOInterfaces;

import java.util.List;

import Connector.DALException;
import DTO.ReceptDTO;

public interface IReceptDAO 
{
	ReceptDTO getRecept(int receptId) throws DALException, Exception;
	List<ReceptDTO> getReceptList() throws DALException, Exception;
	void createRecept(ReceptDTO recept) throws DALException, Exception;
	void updateRecept(ReceptDTO recept) throws DALException, Exception;
}