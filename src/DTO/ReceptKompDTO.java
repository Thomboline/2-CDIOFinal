package DTO;

public class ReceptKompDTO
{
	/** recept id i området 1-99999999 */
	int receptId;
	/** raavare id i området 1-99999999 vælges af brugerne */
	int raavareId;
	/** nominel nettomængde i området 0,05 - 20,0 kg */
	double nomNetto;
	/** tolerance i området 0,1 - 10,0 % */
	double tolerance;
	
}
