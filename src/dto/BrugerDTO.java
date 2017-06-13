package dto;



public class BrugerDTO
{	

	private int status;
	private int brugerId;					/** Bruger id i omr�det 1-99999999. V�lges af brugerne */
	String brugerNavn;				/** Bruger navn (bruger_navn) min. 2 max. 20 karakterer */
	String ini;						/** Bruger initialer min. 2 max. 4 karakterer */
	String cpr;						/** Bruger cpr-nr 10 karakterer */
	String password;				/** Bruger password min. 5 max. 8 karakterer */
	//List<String> roller;			/** Liste over roller */
	String rolle;
	
	public BrugerDTO(){
		
	}
	
	public BrugerDTO(int brugerId, String brugerNavn, String ini, String password, String cpr, String rolle, int status) 
	{
		this.brugerId = brugerId;
		this.brugerNavn = brugerNavn;
		this.ini = ini;
		this.password = password;
		this.cpr = cpr;
		this.rolle = rolle;
		this.status = status;
	}

	public BrugerDTO(BrugerDTO bruger) 
	{
		this.brugerId = bruger.getBrugerId();
		this.brugerNavn = bruger.getBrugerNavn();
		this.ini = bruger.getIni();
		this.password = bruger.getPassword();
		this.cpr = bruger.getCpr();
		this.rolle = bruger.getRolle();
		this.status = bruger.getStatus();
	}

	public int getBrugerId() 
	{
		return brugerId;
	}

	public void setBrugerId(int brugerId) 
	{

		this.brugerId = brugerId;
	}

	public String getBrugerNavn()
	{
		return brugerNavn;
	}

	public void setBrugerNavn(String brugerNavn) 
	{
		this.brugerNavn = brugerNavn;
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
	
	public String getRolle()
	{
		return rolle;
	}
	
	public void setRolle(String rolle)
	{
		 this.rolle = rolle;
	}
	
	/*
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
	 */
	public String toString() 
	{
		return brugerId + "\t" + brugerNavn + "\t" + ini + "\t" + password + "\t" + cpr + "\t" + rolle + "\t" + status;
	}
	
	public int getStatus()
	{
		return status;
	}

}