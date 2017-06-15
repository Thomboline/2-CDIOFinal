package connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Connector {
    private final String HOST     = "sql11.freemysqlhosting.net";
    private final int    PORT     = 3306;
    private final String DATABASE = "sql11178303";
    private final String USERNAME = "sql11178303"; 
    private final String PASSWORD = "BqqZTJ7cu7";
    
    //Til lokal db, udvikling:
    private final String HOSTLOCAL = "localhost";
    private final String USERNAME1 = "root";
    private final String PASSWORD1 = "root";
    private boolean test = false;
    private static Connection connection;
    
    public Connector() 
    {
        try 
        {	if(!test) {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
			connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
        }
        else if(test) {
        	Class.forName("com.mysql.jdbc.Driver");
        	String url = "jdbc:mysql://" + HOSTLOCAL + ":" + PORT + "/" + DATABASE;
        	connection = DriverManager.getConnection(url, USERNAME1, PASSWORD1);
        }
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
    }

    public Connection getConnection(){
    	return connection;
    }
    
    public static ResultSet doQuery(String query) throws SQLException
    {
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(query);
        return res;
    }
    
    public static void doUpdate(String query) throws SQLException
    {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
    }
    public PreparedStatement getPreparedStatement(String sql) throws SQLException 
	{
		return connection.prepareStatement(sql);
	}

}