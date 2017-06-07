package dto;

public class RaavareBatchDTO
{
	int rbId;			/** raavare batch id i omr�det 1-99999999. V�lges af brugerne */
	int raavareId;		/** raavare id i omr�det 1-99999999 v�lges af brugerne */
	double maengde;		/** m�ngde p� lager */
	
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