package DTO;

public class RaavareBatchDTO
{
	int rbId;			/** raavare batch id i området 1-99999999. Vælges af brugerne */
	int raavareId;		/** raavare id i området 1-99999999 vælges af brugerne */
	double maengde;		/** mængde på lager */
	
	public RaavareBatchDTO(int rbId, int raavareId, double maengde) 
	{
		this.rbId = rbId;
		this.raavareId = raavareId;
		this.maengde = maengde;
	}

	public int getId() 
	{
		return rbId;
	}

	public void setId(int rbId) 
	{
		this.rbId = rbId;
	}

	public int getraavareId() 
	{
		return raavareId;
	}

	public void setraavareId(int raavareId) 
	{
		this.raavareId = raavareId;
	}

	public double getmaengde() 
	{
		return maengde;
	}

	public void setmaengde(double maengde) 
	{
		this.maengde = maengde;
	}

	public String toString() 
	{
		return rbId + "\t" + raavareId + "\t" + maengde;
	}
	
	
}