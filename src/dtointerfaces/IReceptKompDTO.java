package dtointerfaces;

public interface IReceptKompDTO 
{
	int getReceptId();
	void setReceptId(int receptId);
	int getRaavareId();
	void setRaavareId(int raavareId);
	double getNomNetto();
	void setNomNetto(double nomNetto);
	double getTolerance();
	void setTolerance(double tolerance);
	String toString();
}
