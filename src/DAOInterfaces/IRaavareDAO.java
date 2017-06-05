package DAOInterfaces;

import java.util.List;

import Connector.DALException;
import DTO.RaavareDTO;

public interface IRaavareDAO 
{
	RaavareDTO getRaavare(int raavareId) throws DALException, Exception;
	List<RaavareDTO> getRaavareList() throws DALException, Exception;
	void createRaavare(RaavareDTO raavare) throws DALException, Exception;
	void updateRaavare(RaavareDTO raavare) throws DALException, Exception;
}