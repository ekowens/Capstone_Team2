import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 259 Spring 2018
//  Section:  0001
// 
//  Project:  FileAidPilot
//  File:     FileAidPilotReset.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////

/**
 * Crude class to reset database
 * 
 * @author David Matthews
 *
 */
public class FileAidPilotReset
{

	/**
	 * Resets the FileAidPilot database
	 **/
	public static void main(String[] args)
	{
		// ## DEFINE VARIABLES SECTION ##
		// define the driver to use
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		// the database name
		String dbName = "jdbcFileAidPilotDB";
		// define the Derby connection URL to use
		String connectionURL = "jdbc:derby:" + dbName;
		Connection conn = null;

		try
		{
			Class.forName(driver);
		}
		catch (java.lang.ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		try
		{
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("Connected to database " + dbName);

			Statement s = conn.createStatement();

			// Call utility method to check if table exists.
			// Create the table if needed
			if (FileAidPilotUtils.wwdChk4Table(conn))
			{
				System.out.println(" . . . . dropping table FILE");
				s.executeUpdate("DROP TABLE FILE");
			}

			// Release the resources (clean up )
			s.close();
			System.out.println("Closed connection");

			// ## DATABASE SHUTDOWN SECTION ##
			/***
			 * In embedded mode, an application should shut down Derby. Shutdown
			 * throws the XJ015 exception to confirm success.
			 ***/
			if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver"))
			{
				boolean gotSQLExc = false;
				try
				{
					DriverManager.getConnection("jdbc:derby:;shutdown=true");
				}
				catch (SQLException se)
				{
					if (se.getSQLState().equals("XJ015"))
					{
						gotSQLExc = true;
					}
				}
				if (!gotSQLExc)
				{
					System.out.println("Database did not shut down normally");
				}
				else
				{
					System.out.println("Database shut down normally");
				}
			}

			// Beginning of the primary catch block: prints stack trace
		}
		catch (Throwable e)
		{
			/*
			 * Catch all exceptions and pass them to the
			 * Throwable.printStackTrace method
			 */
			System.out.println(" . . . exception thrown:");
			e.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("FileAid Reset program ending.");
	}
}
