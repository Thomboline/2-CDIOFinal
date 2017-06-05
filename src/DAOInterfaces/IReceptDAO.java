package DAOInterfaces;

import java.util.List;

import Connector.DALException;
import DTO.ReceptDTO;

public interface IReceptDAO 
{
	ReceptDTO getRecept(int receptId) throws DALException;
	List<ReceptDTO> getReceptList() throws DALException;
	void createRecept(ReceptDTO recept) throws DALException;
	void updateRecept(ReceptDTO recept) throws DALException;
}