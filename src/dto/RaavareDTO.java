package dto;

public class RaavareDTO
{
	int raavareId;						/** raavare id i området 1-99999999 vælges af brugerne */
	String raavareNavn;					/** min. 2 max. 20 karakterer */
	String leverandoer;					/** min. 2 max. 20 karakterer */
	
	public RaavareDTO(int raavareId, String raavareNavn, String leverandoer) 
	{
		this.raavareId = raavareId;
		this.raavareNavn = raavareNavn;
		this.leverandoer = leverandoer;
	}

	public int getRaavareId() 
	{
		return raavareId;
	}

	public void setRaavareId(int raavareId) 
	{
		this.raavareId = raavareId;
	}

	public String getRaavareNavn() 
	{
		return raavareNavn;
	}

	public void setRaavareNavn(String raavareNavn) 
	{
		this.raavareNavn = raavareNavn;
	}
	
	public String getLeverandoer() 
	{
		return leverandoer;
	}

	public void setLeverandoer(String leverandoer) 
	{
		this.leverandoer = leverandoer;
	}

	public String toString() 
	{
		return raavareId + "\t" + raavareNavn + "\t" + leverandoer;
	}
}