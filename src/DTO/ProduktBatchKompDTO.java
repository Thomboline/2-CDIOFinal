package DTO;

public class ProduktBatchKompDTO 
{
	
	int pbId;					/**produkt batch id i området 1-99999999. Vælges af brugerne */
	int rbId;					/** raavare batch id i området 1-99999999. Vælges af brugerne */
	double tara;				/** tara i kg */
	double netto;				/** afvejet nettomængde i kg */
	int oprId;					/** Laborant-identifikationsnummer */
	
	public ProduktBatchKompDTO(int pbId, int rbId, double tara, double netto, int oprId) 
	{
		this.pbId = pbId;
		this.rbId = rbId;
		this.tara = tara;
		this.netto = netto;
		this.oprId = oprId;
	}

	public int getProductBatchId() 
	{
		return pbId;
	}

	public void setProductBatchId(int productBatchId) 
	{

		this.pbId = pbId;
	}

	public int getMaterialBatchId() 
	{
		return rbId;
	}

	public void setMaterialBatchId(int rbId) 
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

	public int getOperatorId() 
	{
		return oprId;
	}

	public void setOperatorId(int oprId) 
	{
		this.oprId = oprId;
	}

	public String toString() 
	{

		return pbId + "\t" + rbId + "\t" + tara + "\t" + netto + "\t" + oprId;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
