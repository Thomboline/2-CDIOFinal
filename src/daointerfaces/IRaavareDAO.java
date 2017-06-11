package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.RaavareDTO;
import dtointerfaces.IRaavareDTO;

public interface IRaavareDAO 
{
	IRaavareDTO getRaavare(int raavareId) throws DALException, Exception;
	List<IRaavareDTO> getRaavareList() throws DALException, Exception;
	void createRaavare(IRaavareDTO raavare) throws DALException, Exception;
	void updateRaavare(IRaavareDTO raavare) throws DALException, Exception;
}