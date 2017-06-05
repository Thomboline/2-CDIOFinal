package DTO;

public class ProduktBatchDTO 
{
	/** produkt batch id i området 1-99999999. Vælges af brugerne */
	int pbId;
	/** status 0: ikke påbegyndt, 1: under produktion, 2: afsluttet */
	int status;
	/** recept id i området 1-99999999. Vælges af brugerne */
	int receptId;
}
