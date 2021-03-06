package dto;

public class ProduktBatchKompDTO
{
	
	private int terminal;
	private int pbId;					/**produkt batch id i omr�det 1-99999999. V�lges af brugerne */
	private int rbId;					/** raavare batch id i omr�det 1-99999999. V�lges af brugerne */
	private double tara;				/** tara i kg */
	private double netto;				/** afvejet nettom�ngde i kg */
	private int brugerId;					/** Laborant-identifikationsnummer */
	
	public ProduktBatchKompDTO()
	{
	}
	
	public ProduktBatchKompDTO(int pbId, int rbId, int brugerId, double tara, double netto, int terminal) 
	{
		this.pbId = pbId;
		this.rbId = rbId;
		this.brugerId = brugerId;
		this.tara = tara;
		this.netto = netto;
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
