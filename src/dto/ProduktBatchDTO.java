package dto;

import dtointerfaces.IProduktBatchDTO;

public class ProduktBatchDTO implements IProduktBatchDTO
{
	int pbId;				/** produkt batch id i området 1-99999999. Vælges af brugerne */
	int status;				/** status 0: ikke påbegyndt, 1: under produktion, 2: afsluttet */
	int receptId;			/** recept id i området 1-99999999. Vælges af brugerne */
	
	
	public ProduktBatchDTO(int pbId, int receptId, int status ) 
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
