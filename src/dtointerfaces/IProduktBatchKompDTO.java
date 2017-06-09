package dtointerfaces;

public interface IProduktBatchKompDTO 
{
	int getProduktBatchId();
	void setProduktBatchId(int pbId);
	int getRaavareBatchId();
	void setRaavareBatchId(int rbId);
	double getTara();
	void setTara(double tara);
	double getNetto();
	void setNetto(double netto);
	int getBrugerId();
	void setBrugerId(int brugerId);
	void setTerminal(int terminal);
	int getTerminal();
	String toString();
}
