package test;

import java.text.DecimalFormat;

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
		
		ProduktBatchKompDTO PBDTO = new ProduktBatchKompDTO(1, 2, 3, "4.5", "4.5", 1);
		ProduktBatchKompDAO PBKDAO = new ProduktBatchKompDAO();
		
		
		
		//System.out.println(PBDTO.getNetto());
		PBKDAO.createProduktBatchKomp(PBDTO);
		
		//Test af CreatePBK//
		/*ProduktBatchKompDTO PBDTO1 = new ProduktBatchKompDTO(1, 2, 50, 43, 1, 1);
		ProduktBatchKompDAO PBKDAO1 = new ProduktBatchKompDAO();
		
		PBKDAO1.updateProduktBatchKomp(PBDTO1);
		*/
		
		
	}

	
}
