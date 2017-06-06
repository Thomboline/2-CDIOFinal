package DAOInterfaces;

import java.util.List;

import Connector.DALException;
import DTO.RaavareBatchDTO;

public interface IRaavareBatchDAO 
{
	RaavareBatchDTO getRaavareBatch(int rbId) throws DALException, Exception;
	List<RaavareBatchDTO> getRaavareBatchList() throws DALException, Exception;
	List<RaavareBatchDTO> getRaavareBatchList(int raavareId) throws DALException, Exception;
	void createRaavareBatch(RaavareBatchDTO raavarebatch) throws DALException, Exception;
	void updateRaavareBatch(RaavareBatchDTO raavarebatch) throws DALException, Exception;
}
