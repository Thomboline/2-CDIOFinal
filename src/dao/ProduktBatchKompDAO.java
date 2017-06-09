package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connector.Connector;
import connector.DALException;
import daointerfaces.IProduktBatchKompDAO;
import dto.ProduktBatchKompDTO;

public class ProduktBatchKompDAO implements IProduktBatchKompDAO
{
	Connector c = new Connector();
	
	@Override
	public ProduktBatchKompDTO getProduktBatchKomp(int pbId, int rbId) throws Exception 
	{
		ResultSet rs = Connector.doQuery("SELECT * FROM produktbatchkomponent WHERE pbId = " + pbId + " AND rbId = " + rbId);

			try {

				if (!rs.first()) throw new Exception("Produkt batch komponent med pbId="+pbId+" og rbId="+rbId+" eksistere ikke.");

				return new ProduktBatchKompDTO
				(
						rs.getInt("pbId"),
						rs.getInt("rbId"),
						rs.getInt("brugerId"),
						rs.getDouble("tara"),
						rs.getDouble("netto"),
						rs.getInt("terminal")
				);

			} catch (SQLException e) 
			{
				throw new DALException(e);
			}
	}

	@Override
	public List<ProduktBatchKompDTO> getProduktBatchKompList(int pbId) throws Exception 
	{
		List<ProduktBatchKompDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT * FROM produktbatchkomponent WHERE pbId = " + pbId);

		try 
		{
			while (rs.next()) 
			{
				list.add
				(			new ProduktBatchKompDTO
							(
							rs.getInt("pbId"),
							rs.getInt("rbId"),
							rs.getInt("brugerId"),
							rs.getDouble("tara"),
							rs.getDouble("netto"),
							rs.getInt("terminal")
							)
				);
			}

		} catch (SQLException e) 
		{
			throw new DALException(e);
		}
		return list;
	}

	@Override
	public List<ProduktBatchKompDTO> getProduktBatchKompList() throws Exception 
	{
		List<ProduktBatchKompDTO> list = new ArrayList<>();

		ResultSet rs = Connector.doQuery("SELECT * FROM produktbatchkomponent");

		try 
		{

			while (rs.next()) 
			{

				list.add(new ProduktBatchKompDTO
						(
								rs.getInt("pbId"),
								rs.getInt("rbId"),
								rs.getInt("brugerId"),
								rs.getDouble("tara"),
								rs.getDouble("netto"),
								rs.getInt("terminal")
						)
				);
			}

		} catch (SQLException e) 
		{
			throw new DALException(e);
		}

		return list;
	}

	@Override
	public void createProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException, Exception 
	{
		Connector.doUpdate
		(
				String.format
				("CALL createProduktBatchKomponent('%d', '%d', '%d', '%s', '%s', '%d');",

						produktbatchkomponent.getProduktBatchId(),
						produktbatchkomponent.getRaavareBatchId(),
						produktbatchkomponent.getBrugerId(),
						produktbatchkomponent.getTara(),
						produktbatchkomponent.getNetto(),
						produktbatchkomponent.getTerminal()
				)
		);
		
	}

	@Override
	public void updateProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException, Exception 
	{
		Connector.doUpdate
		(
				String.format
				("CALL updateProduktBatchKomponent('%d', '%d', '%d', '%s', '%s', '%d');",

						produktbatchkomponent.getProduktBatchId(),
						produktbatchkomponent.getRaavareBatchId(),
						produktbatchkomponent.getBrugerId(),
						produktbatchkomponent.getTara(),
						produktbatchkomponent.getNetto(),
						produktbatchkomponent.getTerminal()
						
				)
		);
		
	}

}
