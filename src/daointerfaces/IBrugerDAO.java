package daointerfaces;

import java.util.List;

import connector.DALException;
import dto.BrugerDTO;


public interface IBrugerDAO 
{
		BrugerDTO getBruger(int oprId) throws  DALException, Exception;
		List<BrugerDTO> getBrugerList() throws DALException, Exception;
		void createBruger(BrugerDTO opr) throws DALException, Exception;
		void updateBruger(BrugerDTO opr) throws DALException, Exception;
		void resetPassword(int id)throws DALException, Exception;
		void deleteBruger(int id) throws DALException, Exception;
		boolean login(BrugerDTO bruger) throws DALException, Exception;
		String getRolle(int id) throws DALException, Exception;
}