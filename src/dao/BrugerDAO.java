
package dao;
/*gfdg*/
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import PasswordGenerator.PasswordGenerator;
import connector.Connector;
import connector.DALException;
import daointerfaces.IBrugerDAO;
import dto.BrugerDTO;

public class BrugerDAO implements IBrugerDAO
{
	Connector c = new Connector();

	@Override
	public BrugerDTO getBruger(int brugerId) throws Exception 
	{

		ResultSet rs = Connector.doQuery("SELECT * FROM sql11178303.brugere natural join sql11178303.brugerinfo WHERE brugerId = " + brugerId);

		try 
		{
			if (!rs.first()) throw new Exception("Brugeren med brugerId="+brugerId+" eksistere ikke.");
			return new BrugerDTO
					(
							rs.getInt("brugerId"),
							rs.getString("brugerNavn"),
							rs.getString("initialer"),
							rs.getString("password"),
							rs.getString("cpr"),
							rs.getString("rolle"),
							rs.getInt("brugerStatus")
					);
		} 
		catch (SQLException e) {throw new DALException(e); }
	}

	@Override
	public List<BrugerDTO> getBrugerList() throws Exception 
	{
		List<BrugerDTO> list = new ArrayList<BrugerDTO>();
		ResultSet rs = Connector.doQuery("SELECT * FROM sql11178303.brugere natural join sql11178303.brugerinfo");

		try
		{
			while (rs.next()) 
			{
				list.add(new BrugerDTO(rs.getInt("brugerId"), rs.getString("brugerNavn"), rs.getString("initialer"), rs.getString("password"), rs.getString("cpr"), rs.getString("rolle"), rs.getInt("brugerstatus")));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createBruger(BrugerDTO bruger) throws Exception
	{
		Connector.doUpdate
		(
				String.format
				("CALL createBruger('%d','%s','%s','%s','%s','%s','%d');",

						bruger.getBrugerId(),
						bruger.getBrugerNavn(),
						bruger.getIni(),
						bruger.getPassword(),
						bruger.getCpr(),
						bruger.getRolle(),
						bruger.getStatus()
						)
				);
	}

	@Override
	public void updateBruger(BrugerDTO bruger) throws Exception 
	{
		Connector.doUpdate
		(
				String.format
				("CALL updateBruger(%d,'%s','%s','%s','%d','%s','%s');",

						bruger.getBrugerId(),
						bruger.getBrugerNavn(),
						bruger.getIni(),
						bruger.getPassword(),
						bruger.getStatus(),
						bruger.getCpr(),
						bruger.getRolle()
				)
		);
	}
	@Override
	public void resetPassword(int id) throws Exception 
	{
		PasswordGenerator PG = new PasswordGenerator();

		Connector.doUpdate
		(
				String.format
				("CALL resetPassword('%d','%s');",

						id,
						PG.PasswordGen(PG.PasswordLength())
						)
				);
	}
	public void deleteBruger(int id) throws Exception 
	{
		Connector.doUpdate
		(
				String.format
				("CALL deleteBruger('%d');",
						id
				)
		);
	}
	public boolean login(BrugerDTO bruger) throws Exception
	{
		boolean Request;
		String Verification;
		
		Verification = "select Login(?,?)";
		java.sql.PreparedStatement pst = c.getPreparedStatement(Verification);
		
		pst.setInt(1, bruger.getBrugerId());
		pst.setString(2, bruger.getPassword());
	
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		Request = rs.getBoolean(1);

		return Request;
	}
	public String getRolle(int brugerId) throws Exception
	{
		String Query;
		String DBRolle;
		
		Query = "select getRolle(?)";
		java.sql.PreparedStatement pst = c.getPreparedStatement(Query);
		
		pst.setInt(1, brugerId);
	
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		DBRolle = rs.getString(1);

		return DBRolle;
	}
}
