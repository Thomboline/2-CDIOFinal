package DTO;

public class ReceptKompDTO
{
	/** recept id i omr�det 1-99999999 */
	int receptId;
	/** raavare id i omr�det 1-99999999 v�lges af brugerne */
	int raavareId;
	/** nominel nettom�ngde i omr�det 0,05 - 20,0 kg */
	double nomNetto;
	/** tolerance i omr�det 0,1 - 10,0 % */
	double tolerance;
	
}
