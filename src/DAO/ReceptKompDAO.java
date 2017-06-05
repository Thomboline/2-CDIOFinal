package DAO;

import java.util.List;

import Connector.DALException;
import DAOInterfaces.IReceptKompDAO;
import DTO.ReceptKompDTO;

public class ReceptKompDAO implements IReceptKompDAO
{

	@Override
	public ReceptKompDTO getReceptKomp(int receptId, int raavareId) throws DALException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReceptKompDTO> getReceptKompList(int receptId) throws DALException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReceptKompDTO> getReceptKompList() throws DALException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createReceptKomp(ReceptKompDTO receptkomponent) throws DALException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateReceptKomp(ReceptKompDTO receptkomponent) throws DALException, Exception {
		// TODO Auto-generated method stub
		
	}

}
