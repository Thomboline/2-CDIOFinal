package dto;

public class ProduktBatchDTO
{
	int pbId;				/** produkt batch id i området 1-99999999. Vælges af brugerne */
	int pb_status;			/** status 0: ikke påbegyndt, 1: under produktion, 2: afsluttet */
	int receptId;			/** recept id i området 1-99999999. Vælges af brugerne */
	
	public ProduktBatchDTO()
	{
	}
	
	public ProduktBatchDTO(int pbId, int receptId, int pb_status ) 
	{
		this.pbId = pbId;
		this.pb_status = pb_status;
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
		return pb_status;
	}

	public void setStatus(int pb_status) 
	{
		this.pb_status = pb_status;
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
		return pbId + "\t" + receptId + "\t" + pb_status;
	}
	
}
