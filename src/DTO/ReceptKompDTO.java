package DTO;

public class ReceptKompDTO
{
	
	int receptId;				/** recept id i omr�det 1-99999999 */
	int raavareId;				/** raavare id i omr�det 1-99999999 v�lges af brugerne */
	double nomNetto;			/** nominel nettom�ngde i omr�det 0,05 - 20,0 kg */
	double tolerance;			/** tolerance i omr�det 0,1 - 10,0 % */
	
	public int getReceiptId() 
	{
		return receptId;
	}

	public void setReceiptId(int receptId) 
	{
		this.receptId = receptId;
	}

	public int getMaterialId() 
	{
		return raavareId;
	}

	public void setMaterialId(int raavareId) 
	{
		this.raavareId = raavareId;
	}

	public double getNomNetto() 
	{
		return nomNetto;
	}

	public void setNomNetto(double nomNetto) 
	{
		this.nomNetto = nomNetto;
	}

	public double getTolerance() 
	{
		return tolerance;
	}

	public void setTolerance(double tolerance) 
	{
		this.tolerance = tolerance;
	}

	public String toString() 
	{
		return receptId + "\t" + raavareId + "\t" + nomNetto + "\t" + tolerance;
	}
}
