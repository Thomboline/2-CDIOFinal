package dto;

public class ProduktBatchDTO 
{
	int pbId;				/** produkt batch id i omr�det 1-99999999. V�lges af brugerne */
	int status;				/** status 0: ikke p�begyndt, 1: under produktion, 2: afsluttet */
	int receptId;			/** recept id i omr�det 1-99999999. V�lges af brugerne */
	
	
	public ProduktBatchDTO(int pbId, int status, int receptId) 
	{
		this.pbId = pbId;
		this.status = status;
		this.receptId = receptId;
	}

	public int getProduktBatchId() 
	{
		return pbId;
	}
	
	public void setProduktBatchId(int pbId) 
	{
		this.pbId = pbId;
	}

	public int getStatus() 
	{
		return status;
	}

	public void setStatus(int status) 
	{
		this.status = status;
	}

	public int getReceptId() 
	{
		return receptId;
	}

	public void setReceptId(int receptId) 
	{
		this.receptId = receptId;
	}

	public String toString() 
	{
		return pbId + "\t" + status + "\t" + receptId;
	}
	
}
