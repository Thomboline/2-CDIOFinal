package DTO;

public class ReceptDTO
{
	int receptId;				/** recept id i området 1-99999999 */
	String receptNavn;			/** Receptnavn min. 2 max. 20 karakterer */
	
	public ReceptDTO(int receptId, String receptNavn) 
	{
		this.receptId = receptId;
		this.receptNavn = receptNavn;
	}

	public int getId() 
	{
		return receptId;
	}

	public void setId(int receptId) 
	{
		this.receptId = receptId;
	}

	public String getName() 
	{
		return receptNavn;
	}

	public void setName(String receptNavn) 
	{
		this.receptNavn = receptNavn;
	}

	public String toString() 
	{
		return receptId + "\t" + receptNavn;
	}
	
}