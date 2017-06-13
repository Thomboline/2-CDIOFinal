package dto;

public class RaavareBatchDTO
{
	private int rbId;			/** raavare batch id i området 1-99999999. Vælges af brugerne */
	private int raavareId;		/** raavare id i området 1-99999999 vælges af brugerne */
	private double maengde;		/** mængde på lager */
	
	public RaavareBatchDTO()
	{
	}
	
	public RaavareBatchDTO(int rbId, int raavareId, double maengde) 
	{
		this.rbId = rbId;
		this.raavareId = raavareId;
		this.maengde = maengde;
	}

	public int getRaavareBatchId() 
	{
		return rbId;
	}

	public void setRaavareBatchId(int rbId) 
	{
		this.rbId = rbId;
	}

	public int getRaavareId() 
	{
		return raavareId;
	}

	public void setRaavareId(int raavareId) 
	{
		this.raavareId = raavareId;
	}

	public double getMaengde() 
	{
		return maengde;
	}

	public void setMaengde(double maengde) 
	{
		this.maengde = maengde;
	}

	public String toString() 
	{
		return rbId + "\t" + raavareId + "\t" + maengde;
	}
	
	
}