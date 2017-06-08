package dto;

public class ProduktBatchKompDTO 
{
	
	int terminal;
	int pbId;					/**produkt batch id i området 1-99999999. Vælges af brugerne */
	int rbId;					/** raavare batch id i området 1-99999999. Vælges af brugerne */
	double tara;				/** tara i kg */
	double netto;				/** afvejet nettomængde i kg */
	int brugerId;					/** Laborant-identifikationsnummer */
	
	public ProduktBatchKompDTO(int pbId, int rbId, double tara, double netto, int brugerId, int terminal) 
	{
		this.pbId = pbId;
		this.rbId = rbId;
		this.tara = tara;
		this.netto = netto;
		this.brugerId = brugerId;
		this.terminal = terminal;
	}

	public int getProduktBatchId() 
	{
		return pbId;
	}

	public void setProduktBatchId(int pbId) 
	{

		this.pbId = pbId;
	}

	public int getRaavareBatchId() 
	{
		return rbId;
	}

	public void setRaavareBatchId(int rbId) 
	{
		this.rbId = rbId;
	}

	public double getTara() 
	{
		return tara;
	}

	public void setTara(double tara) 
	{
		this.tara = tara;
	}

	public double getNetto() 
	{
		return netto;
	}

	public void setNetto(double netto) 
	{
		this.netto = netto;
	}

	public int getBrugerId() 
	{
		return brugerId;
	}

	public void setBrugerId(int brugerId) 
	{
		this.brugerId = brugerId;
	}

	public void setTerminal(int terminal)
	{
		this.terminal = terminal;
	}
	
	public int getTerminal()
	{
		return terminal;
	}
	
	public String toString() 
	{

		return pbId + "\t" + rbId + "\t" + tara + "\t" + netto + "\t" + brugerId + "\t" + terminal;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
