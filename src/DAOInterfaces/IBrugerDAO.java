package DAOInterfaces;

import java.util.List;

import Connector.DALException;
import DTO.BrugerDTO;

public interface IBrugerDAO 
{
		BrugerDTO getBruger(int oprId) throws  DALException, Exception;
		List<BrugerDTO> getBrugerList() throws DALException, Exception;
		void createBruger(BrugerDTO opr) throws DALException, Exception;
		void updateBruger(BrugerDTO opr) throws DALException, Exception;
}