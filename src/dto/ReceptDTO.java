package dto;

import dtointerfaces.IReceptDTO;

public class ReceptDTO implements IReceptDTO
{
	int receptId;				/** recept id i området 1-99999999 */
	String receptNavn;			/** Receptnavn min. 2 max. 20 karakterer */
	
	public ReceptDTO(int receptId, String receptNavn) 
	{
		this.receptId = receptId;
		this.receptNavn = receptNavn;
	}

	public int getReceptId() 
	{
		return receptId;
	}

	public void setReceptId(int receptId) 
	{
		this.receptId = receptId;
	}

	public String getReceptNavn() 
	{
		return receptNavn;
	}

	public void setReceptNavn(String receptNavn) 
	{
		this.receptNavn = receptNavn;
	}

	public String toString() 
	{
		return receptId + "\t" + receptNavn;
	}
	
}