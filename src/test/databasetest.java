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
		/*
		IBrugerDAO BDAO = new BrugerDAO();
		BrugerDTO BDTO = new BrugerDTO(2, "Svend Kurtsen","SVK","280656-1323","sddgfdDFGfd", "Admin", 1);
		
		BDAO.createBruger(BDTO); //Laver en bruger - Det virker! //
		
		IBrugerDAO BDAO = new BrugerDAO();
		BrugerDTO BDTO = new BrugerDTO(1, "Børge Kurtsen","BK","343545-1323","sdfdsQQQ", "Laborant", 1);
		
		BDAO.updateBruger(BDTO); //Opdatere en bruger - Det virker!!//
		
		IBrugerDAO BDAO = new BrugerDAO();
		
		System.out.println(BDAO.getBrugerList());
		*/

	
		/*Test af Raavare klassen */
		//RaavareDTO RDTO = new RaavareDTO(2, "Grisetarm", "Holger");
		//IRaavareDAO RDAO = new RaavareDAO();
		//RDAO.createRaavare(RDTO);
		//RDAO.updateRaavare(RDTO);
		//System.out.println(RDAO.getRaavareList());
		
		
		
		/*Test af RaavareBatch*/
		/*
		IRaavareBatchDAO RBDAO = new RaavareBatchDAO();
		RaavareBatchDTO RBDTO = new RaavareBatchDTO(1, 2, 5.5);
		
		//RBDAO.createRaavareBatch(RBDTO);
		//RBDAO.updateRaavareBatch(RBDTO);
		
		//System.out.println(RBDAO.getRaavareBatchList());
		
		*/
		
		 
		/* Test af ReceptDAO*/
		/*
		IReceptDAO RD = new ReceptDAO();
		ReceptDTO RDT = new ReceptDTO(1, "Crab");
		
		//RD.createRecept(RDT);
		//RD.updateRecept(RDT);

		System.out.println(RD.getReceptList());
		*/
		
		
		
		
		/*
		IReceptKompDAO RKDAO = new ReceptKompDAO();
		ReceptKompDTO RKDTO = new ReceptKompDTO(1, 2, 7.5, 7.5);
		
		//RKDAO.createReceptKomp(RKDTO);
		//RKDAO.updateReceptKomp(RKDTO);
		
		System.out.println(RKDAO.getReceptKompList(1));
		*/
		
		
		IProduktBatchDAO PBDAO = new ProduktBatchDAO();
		ProduktBatchDTO PBDTO = new ProduktBatchDTO(1, 1, 2);
		
		//PBDAO.createProduktBatch(PBDTO);
		PBDAO.updateProduktBatch(PBDTO);
		
		//System.out.println(PBDAO.getProduktBatch(1));
		
		
		
		
		
		
		
		
		
		/*
		//Test af CreatePBK//
		ProduktBatchKompDTO PBDTO = new ProduktBatchKompDTO(1, 2, 50, 43, 1, 1);
		IProduktBatchKompDAO PBKDAO = new ProduktBatchKompDAO();
		
		PBKDAO.createProduktBatchKomp(PBDTO);
		//PBKDAO.updateProduktBatchKomp(PBDTO);
		
		
		*/
		
		
		
	}

	

	
}
