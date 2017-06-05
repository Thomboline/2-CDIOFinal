package DAOInterfaces;

import java.sql.SQLException;

public class DALException extends Exception 
{
	
	private static final long serialVersionUID = 1L;

	public DALException(String string) 
	{
	super(string);
	}
}