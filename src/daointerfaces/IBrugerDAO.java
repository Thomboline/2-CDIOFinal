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
		void resetPassword(BrugerDTO bruger)throws DALException, Exception;
		void deleteBruger(BrugerDTO bruger) throws DALException, Exception;
}