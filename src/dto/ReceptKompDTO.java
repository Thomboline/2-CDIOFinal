package dto;

import dtointerfaces.IReceptKompDTO;

public class ReceptKompDTO implements IReceptKompDTO
{
	
	int receptId;				/** recept id i området 1-99999999 */
	int raavareId;				/** raavare id i området 1-99999999 vælges af brugerne */
	double nomNetto;			/** nominel nettomængde i området 0,05 - 20,0 kg */
	double tolerance;			/** tolerance i området 0,1 - 10,0 % */
	
	
	public ReceptKompDTO(int receptId, int raavareId, double nomNetto, double tolerance) 
	{
		this.receptId = receptId;
		this.raavareId = raavareId;
		this.nomNetto = nomNetto;
		this.tolerance = tolerance;
	}
	
	public int getReceptId() 
	{
		return receptId;
	}

	public void setReceptId(int receptId) 
	{
		this.receptId = receptId;
	}

	public int getRaavareId() 
	{
		return raavareId;
	}

	public void setRaavareId(int raavareId) 
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
