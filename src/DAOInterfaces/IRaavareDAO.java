package DAOInterfaces;

import java.util.List;

import Connector.DALException;
import DTO.RaavareDTO;

public interface IRaavareDAO 
{
	RaavareDTO getRaavare(int raavareId) throws DALException;
	List<RaavareDTO> getRaavareList() throws DALException;
	void createRaavare(RaavareDTO raavare) throws DALException;
	void updateRaavare(RaavareDTO raavare) throws DALException;
}