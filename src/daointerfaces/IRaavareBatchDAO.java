package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.RaavareBatchDTO;
import dtointerfaces.IRaavareBatchDTO;

public interface IRaavareBatchDAO 
{
	IRaavareBatchDTO getRaavareBatch(int rbId) throws DALException, Exception;
	List<IRaavareBatchDTO> getRaavareBatchList() throws DALException, Exception;
	List<IRaavareBatchDTO> getRaavareBatchList(int raavareId) throws DALException, Exception;
	void createRaavareBatch(IRaavareBatchDTO raavarebatch) throws DALException, Exception;
	void updateRaavareBatch(IRaavareBatchDTO raavarebatch) throws DALException, Exception;
}
