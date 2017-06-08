package test;

import connector.DALException;
import dao.BrugerDAO;
import dao.ProduktBatchKompDAO;
import daointerfaces.IBrugerDAO;
import dto.BrugerDTO;
import dto.ProduktBatchKompDTO;

public class databasetest 
{
	
	public static void main(String[] args) throws DALException, Exception 
	{
		/* 
		IBrugerDAO BDAO = new BrugerDAO();
		BrugerDTO BDTO = new BrugerDTO(1, "Svend Kurtsen","SVK","280656-1323","sddgfdDFGfd", "Admin");
		
		BDAO.createBruger(BDTO); //Laver en bruger - Det virker! //
		
		
		IBrugerDAO BDAO = new BrugerDAO();
		BrugerDTO BDTO = new BrugerDTO(1, "Børge Kurtsen","BK","343545-1323","sdfdsQQQ", "Laborant", 1);
		
		BDAO.updateBruger(BDTO); //Opdatere en bruger - Det virker!!//
		
		IBrugerDAO BDAO = new BrugerDAO();
		
		System.out.println(BDAO.getBrugerList());
		*/
		
		
		//Test af CreatePBK//
		ProduktBatchKompDTO PBDTO = new ProduktBatchKompDTO(1, 2, 500.0, 435.0, 3, 1);
		ProduktBatchKompDAO PBKDAO = new ProduktBatchKompDAO();
		
		PBKDAO.createProduktBatchKomp(PBDTO);
		
		//Test af CreatePBK//
		/*ProduktBatchKompDTO PBDTO1 = new ProduktBatchKompDTO(1, 2, 50, 43, 1, 1);
		ProduktBatchKompDAO PBKDAO1 = new ProduktBatchKompDAO();
		
		PBKDAO1.updateProduktBatchKomp(PBDTO1);
		*/
		
		
	}

	
}
