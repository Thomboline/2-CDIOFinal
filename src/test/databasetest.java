package test;
import connector.DALException;
import dao.BrugerDAO;
import dao.ProduktBatchDAO;
import dao.ProduktBatchKompDAO;
import dao.RaavareBatchDAO;
import dao.RaavareDAO;
import dao.ReceptDAO;
import dao.ReceptKompDAO;
import daointerfaces.IBrugerDAO;
import daointerfaces.IProduktBatchDAO;
import daointerfaces.IProduktBatchKompDAO;
import daointerfaces.IRaavareBatchDAO;
import daointerfaces.IRaavareDAO;
import daointerfaces.IReceptDAO;
import daointerfaces.IReceptKompDAO;
import dto.BrugerDTO;
import dto.ProduktBatchDTO;
import dto.ProduktBatchKompDTO;
import dto.RaavareBatchDTO;
import dto.RaavareDTO;
import dto.ReceptDTO;
import dto.ReceptKompDTO;

public class databasetest 
{
	public static void main(String[] args) throws DALException, Exception 
	{
		BrugerDAOTest(6);
//		RaavareDAOTest(1);
//		RaavareBatchDAOTest(1);
//		ReceptDAOTest(1);
//		ReceptKompDAOTest(1);
//		ProduktBatchDAOTest(1);
//		ProduktBatchKompDAOTest(1);
	}
	
	public static void BrugerDAOTest(int testtype) throws DALException, Exception
	{
		 IBrugerDAO BDAO = new BrugerDAO();
		
		if(testtype == 1)
		{
			BrugerDTO BDTO = new BrugerDTO(2, "Svend Kurtsen","SVK","password123","280486-1232", "Admin", 1);
			BDAO.createBruger(BDTO);
		}
		else if(testtype == 2)
		{
			BrugerDTO BDTO = new BrugerDTO(2, "Svend Kurtsen","SVK","PAsswordtest","280486-1232", "Admin", 1);
			BDAO.updateBruger(BDTO);
		}
		else if(testtype == 3)
		{
			System.out.println(BDAO.getBrugerList());
		}
		else if(testtype == 4)
		{
			System.out.println(BDAO.getBruger(1));
		}
		else if(testtype == 5)
		{
			BrugerDTO BDTO = new BrugerDTO(2, null, null, null, null, null, 1);
			BDAO.resetPassword(BDTO);
		}
		else if(testtype == 6)
		{
			BrugerDTO BDTO = new BrugerDTO(1, null, null, "1", null, null, 0);
			BDAO.login(BDTO);
		}


	}
	public static void ProduktBatchDAOTest(int testtype) throws DALException, Exception
	{
		IProduktBatchDAO PBDAO = new ProduktBatchDAO();
		
		if(testtype == 1)
		{
			ProduktBatchDTO PBDTO = new ProduktBatchDTO(1, 1, 2);
			PBDAO.createProduktBatch(PBDTO);
		}
		else if(testtype == 2)
		{
			ProduktBatchDTO PBDTO = new ProduktBatchDTO(1, 1, 2);
			PBDAO.updateProduktBatch(PBDTO); 
		}
		else if(testtype == 3)
		{
			System.out.println(PBDAO.getProduktBatchList());
		}
		else if(testtype == 4)
		{
			System.out.println(PBDAO.getProduktBatch(1));
		}
	}
	public static void ProduktBatchKompDAOTest(int testtype) throws DALException, Exception
	{
		IProduktBatchKompDAO PBKDAO = new ProduktBatchKompDAO();
		
		if(testtype == 1)
		{
			ProduktBatchKompDTO PBDTO = new ProduktBatchKompDTO(1, 1, 2, 45.5, 15.5, 2);
			PBKDAO.createProduktBatchKomp(PBDTO);
		}
		else if(testtype == 2)
		{
			ProduktBatchKompDTO PBDTO = new ProduktBatchKompDTO(1, 1, 2, 45.5, 15.5, 2);
			PBKDAO.updateProduktBatchKomp(PBDTO);
		}
		else if(testtype == 3)
		{
			System.out.println(PBKDAO.getProduktBatchKompList());
		}
		else if(testtype == 4)
		{
			System.out.println(PBKDAO.getProduktBatchKompList(1));
		}
		else if(testtype == 5)
		{
			System.out.println(PBKDAO.getProduktBatchKomp(1, 2));
		}

	}
	public static void ReceptDAOTest(int testtype) throws DALException, Exception
	{
		IReceptDAO RD = new ReceptDAO();
		
		if(testtype == 1)
		{
			ReceptDTO RDT = new ReceptDTO(1, "Crab");
			RD.createRecept(RDT);
		}
		else if(testtype == 2)
		{
			ReceptDTO RDT = new ReceptDTO(1, "tis");
			RD.updateRecept(RDT);
			
		}
		else if(testtype == 3)
		{
			System.out.println(RD.getRecept(1));
		}
		else if(testtype == 4)
		{
			System.out.println(RD.getReceptList());
		}
		
	}
	public static void ReceptKompDAOTest(int testtype) throws DALException, Exception
	{
		IReceptKompDAO RKDAO = new ReceptKompDAO();
		
		if(testtype == 1)
		{
			ReceptKompDTO RKDTO = new ReceptKompDTO(1, 2, 7.5, 7.5);
			RKDAO.createReceptKomp(RKDTO);
		
		}
		else if(testtype == 2)
		{
			ReceptKompDTO RKDTO = new ReceptKompDTO(1, 2, 7.5, 7.5);
			RKDAO.updateReceptKomp(RKDTO);
			
		}
		else if(testtype == 3)
		{
			System.out.println(RKDAO.getReceptKompList());
		}
		else if(testtype == 4)
		{
			System.out.println(RKDAO.getReceptKompList(1));
		}
		else if(testtype == 5)
		{
			System.out.println(RKDAO.getReceptKomp(1, 2));
		}

	}
	public static void RaavareBatchDAOTest(int testtype) throws DALException, Exception
	{
		IRaavareBatchDAO RBDAO = new RaavareBatchDAO();
		
		if(testtype == 1)
		{
			RaavareBatchDTO RBDTO = new RaavareBatchDTO(1, 2, 5.5);
			RBDAO.createRaavareBatch(RBDTO);
		}
		else if(testtype == 2)
		{
			RaavareBatchDTO RBDTO = new RaavareBatchDTO(1, 2, 5.5);
			RBDAO.updateRaavareBatch(RBDTO);
		}
		else if(testtype == 3)
		{
			System.out.println(RBDAO.getRaavareBatch(1));
		}
		else if(testtype == 4)
		{
			System.out.println(RBDAO.getRaavareBatchList(1));
		}
		else if(testtype == 5)
		{
			System.out.println(RBDAO.getRaavareBatchList());
		}
		
	}
	public static void RaavareDAOTest(int testtype) throws DALException, Exception
	{
		IRaavareDAO RDAO = new RaavareDAO();
		
		if(testtype == 1)
		{
			RaavareDTO RDTO = new RaavareDTO(2, "Grisetarm", "Holger");
			RDAO.createRaavare(RDTO);
		}
		else if(testtype == 2)
		{
			RaavareDTO RDTO = new RaavareDTO(2, "Grisetarm", "Holger");
			RDAO.updateRaavare(RDTO);
		}
		else if(testtype == 3)
		{
			System.out.println(RDAO.getRaavareList());
		}
		else if(testtype == 4)
		{
			System.out.println(RDAO.getRaavare(1));
		}		
	}
}
