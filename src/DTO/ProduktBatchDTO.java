package DTO;

public class ProduktBatchDTO 
{
	/** produkt batch id i omr�det 1-99999999. V�lges af brugerne */
	int pbId;
	/** status 0: ikke p�begyndt, 1: under produktion, 2: afsluttet */
	int status;
	/** recept id i omr�det 1-99999999. V�lges af brugerne */
	int receptId;
}
