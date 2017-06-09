package dtointerfaces;

public interface IBrugerDTO 
{
	int getId();
	void setId(int brugerId); 
	String getBrugerNavn();
	void setBrugerNavn(String brugerNavn);
	String getIni();
	void setIni(String ini);
	String getCpr();
	void setCpr(String cpr);
	String getPassword();
	void setPassword(String password);
	String getRolle();
	void setRolle(String rolle);
	String toString();
	int getStatus();
	
}
