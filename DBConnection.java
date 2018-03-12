import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAidPilot
//  File:     DBConnection.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////

/**
 * (Insert a comment that briefly describes the purpose of this class definition.)
 *
 * <p/> Bugs: (List any known issues or unimplemented features here)
 * 
 * @author (Insert your first and last name)
 *
 */
public class DBConnection
{
	public static void createDB()
	{
		// define the driver to use
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		// the database name
		String dbName = "jdbcFileAidPilotDB";
		// define the Derby connection URL to use
		String connectionURL = "jdbc:derby:C:/FileAid/" + dbName + ";create=true";
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
			// PreparedStatement psInsert;
			String createString = "CREATE TABLE FILE  "
					+ "(fiID INT NOT NULL CONSTRAINT FILE_PK PRIMARY KEY, "
					+ " fiPATH VARCHAR(210), fiNAME VARCHAR(260), fiSIZE INT NOT NULL, "
					+ " fiEXTENSION VARCHAR(5) NOT NULL, fiACTIVE SMALLINT, "
					+ " fiMOD_DATE TIMESTAMP NOT NULL, fiMEMO VARCHAR(120)  ) ";

			// Create a statement to issue simple commands.
			// Call utility method to check if table exists.
			// Create the table if needed
			if (!FileAidPilotUtils.wwdChk4Table(conn))
			{
				System.out.println(" . . . . creating table FILE");
				s.execute(createString);
			}

			// Create File data for test
			// Create dates for records
		    GregorianCalendar date1 = new GregorianCalendar(2018, 01, 20);
		    Timestamp sqlDate1 = new Timestamp(date1.getTimeInMillis());
		    GregorianCalendar date2 = new GregorianCalendar(2018, 01, 21);
		    Timestamp sqlDate2 = new Timestamp(date2.getTimeInMillis());
		    GregorianCalendar date3 = new GregorianCalendar(2018, 01, 22);
		    Timestamp sqlDate3 = new Timestamp(date3.getTimeInMillis());
		    
		    
			FAFile Test1 = new FAFile(1, "test1", "c:\\testfiles\\", 1, "docx",
					sqlDate1);
			FAFile Test2 = new FAFile(2, "test2", "c:\\testfiles\\", 1, "xlsx",
					sqlDate2);
			FAFile Test3 = new FAFile(3, "test3", "c:\\testfiles\\", 1, "pptx",
					sqlDate3);

			System.out.println("File data to be inserted");
			System.out.println(Test1);
			System.out.println(Test2);
			System.out.println(Test3);


			String update = String.format(
					"INSERT INTO FILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					Test1.getID(), Test1.getPath(), Test1.getName(),
					Test1.getSize(), Test1.getExtension(),
					getActiveStatus(Test1.isActive()), Test1.getModDate(), Test1.getMemo());
			s.executeUpdate(update);

			update = String.format(
					"INSERT INTO FILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					Test2.getID(), Test2.getPath(), Test2.getName(),
					Test2.getSize(), Test2.getExtension(),
					getActiveStatus(Test2.isActive()), Test2.getModDate(), Test2.getMemo());
			s.executeUpdate(update);

			update = String.format(
					"INSERT INTO FILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					Test3.getID(), Test3.getPath(), Test3.getName(),
					Test3.getSize(), Test3.getExtension(),
					getActiveStatus(Test3.isActive()), Test3.getModDate(), Test3.getMemo());
			s.executeUpdate(update);
			// Release the resources (clean up )
			s.close();
			//System.out.println("Closed connection");

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
		System.out.println("FileAid Installed.");

	}
	
	public static void clearDB(Connection conn)
	{

		try
		{
			Statement s = conn.createStatement();

			// Call utility method to check if table exists.
			// Create the table if needed
			if (FileAidPilotUtils.wwdChk4Table(conn))
			{
				System.out.println(" . . . . clearing table FILE");
				s.executeUpdate("DELETE FROM FILE WHERE 1=1");
			}

			// Release the resources (clean up )
			s.close();
			System.out.println("Closed connection");

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
	} // end clearDB

	public static int getActiveStatus(boolean active)
	{
		if (active)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}//end getActiveStatus
	
	/***      Check for  FILE table    ****/
	   public static boolean wwdChk4Table (Connection conTst ) throws SQLException {
	      try {
	         Statement s = conTst.createStatement();
	         s.execute("update FILE set fiID = 999, fiPATH = 'C:\', fiNAME = 'TEST ENTRY', fiSIZE = 1, fiEXTENSION = '.DOCX',"
	         		+ " fiACTIVE = 1, fiMEMO = 'Test' where 1=3");
	      }  catch (SQLException sqle) {
	         String theError = (sqle).getSQLState();
	         /** If table exists will get -  WARNING 02000: No row was found **/
	         if (theError.equals("42X05"))   // Table does not exist
	         {  return false;
	          }  else if (theError.equals("42X14") || theError.equals("42821"))  {
	             System.out.println("WwdChk4Table: Incorrect table definition. Drop table FILE and rerun this program");
	             throw sqle;   
	          } else { 
	             System.out.println("WwdChk4Table: Unhandled SQLException" );
	             throw sqle; 
	          }
	      }
	      //System.out.println("Just got the warning - table exists OK ");
	      return true;
	   }  /*** END wwdInitTable  **/
	   
	   public static int getNumTableRows(Connection conn, String tableName) throws SQLException
	   {
           int numRows = 0;
		try
		{
			Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select count(*) from " + tableName);
           while(results.next())
           	{
           	numRows = results.getInt(1);
           	}
			stmt.close();
			
		}

			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		   return numRows;
	   }



} // end class


