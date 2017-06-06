package DTO;

import java.util.List;

public class BrugerDTO
{
	
	int oprId;						/** Bruger id i området 1-99999999. Vælges af brugerne */
	String oprNavn;					/** Bruger navn (opr_navn) min. 2 max. 20 karakterer */
	String ini;						/** Bruger initialer min. 2 max. 4 karakterer */
	String cpr;						/** Bruger cpr-nr 10 karakterer */
	String password;				/** Bruger password min. 5 max. 8 karakterer */
	List<String> roller;			/** Liste over roller */
	
	public BrugerDTO(int oprId, String oprNavn, String ini, String cpr, String password) 
	{
		this.oprId = oprId;
		this.oprNavn = oprNavn;
		this.ini = ini;
		this.cpr = cpr;
		this.password = password;
	}

	public BrugerDTO(BrugerDTO opr) 
	{
		this.oprId = opr.getId();
		this.oprNavn = opr.getOperatorNavn();
		this.ini = opr.getIni();
		this.cpr = opr.getCpr();
		this.password = opr.getPassword();
	}

	public int getId() 
	{
		return oprId;
	}

	public void setId(int oprId) 
	{

		this.oprId = oprId;
	}

	public String getOperatorNavn()
	{
		return oprNavn;
	}

	public void setOperatorNavn(String oprNavn) 
	{
		this.oprNavn = oprNavn;
	}

	public String getIni() 
	{
		return ini;
	}

	public void setIni(String ini) 
	{
		this.ini = ini;
	}

	public String getCpr() 
	{
		return cpr;
	}

	public void setCpr(String cpr) 
	{
		this.cpr = cpr;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public List<String> getRoles() 
	{
		return roller;
	}

	public void setRoles(List<String> roller) 
	{
		this.roller = roller;
	}

	public void addRole(String role) 
	{
		this.roller.add(role);
	}

	public String toString() 
	{
		return oprId + "\t" + oprNavn + "\t" + ini + "\t" + cpr + "\t" + password;
	}

}