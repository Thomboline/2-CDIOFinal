package daointerfaces;

import java.util.List;

import connector.DALException;
import dtointerfaces.IBrugerDTO;

public interface IBrugerDAO 
{
		IBrugerDTO getBruger(int oprId) throws  DALException, Exception;
		List<IBrugerDTO> getBrugerList() throws DALException, Exception;
		void createBruger(IBrugerDTO opr) throws DALException, Exception;
		void updateBruger(IBrugerDTO opr) throws DALException, Exception;
		void resetPassword(IBrugerDTO bruger)throws DALException, Exception;


}