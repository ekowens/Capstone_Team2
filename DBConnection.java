import java.io.File;
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
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String dbName = "jdbcFileAidPilotDB";
	// define the Derby connection URL to use
	private String connectionURL = "jdbc:derby:C:/FileAid/" + dbName + ";create=true";
	private Connection conn = null;
	private Statement statement = null;
	
	public DBConnection()
	{
		
	}

	public void createDB()
	{
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

			statement = conn.createStatement();
			// PreparedStatement psInsert;
			String createString1 = "CREATE TABLE FAFILE  "
					+ "(fiID INT NOT NULL CONSTRAINT FAFILE_PK PRIMARY KEY, "
					+ " fiPATH VARCHAR(210), fiNAME VARCHAR(260), fiSIZE INT NOT NULL, "
					+ " fiEXTENSION VARCHAR(5) NOT NULL, fiACTIVE SMALLINT, "
					+ " fiMOD_DATE TIMESTAMP NOT NULL, fiMEMO VARCHAR(120)  ) ";
			
			String createString2 = "CREATE TABLE FILERECORD "
					+ "(frID INT NOT NULL CONSTRAINT FILERECORD_PK PRIMARY KEY, "
					+ " frfiID INT NOT NULL, frPATH VARCHAR(210), frNAME VARCHAR(260), "
					+ " frSIZE INT NOT NULL, frEXTENSION VARCHAR(5) NOT NULL, "
					+ " frMOD_DATE TIMESTAMP NOT NULL ) ";

			// Create a statement to issue simple commands.
			// Call utility method to check if FAFile table exists.
			// Create the DB if needed
			if (!FileAidPilotUtils.wwdChk4Table(conn))
			{
				System.out.println(" . . . . creating FileAid Database");
				statement.execute(createString1);
				statement.execute(createString2);
			}

			// Create File data for test
			// Create dates for File records
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

			System.out.println("\nFile data to be inserted");
			System.out.println(Test1);
			System.out.println(Test2);
			System.out.println(Test3);


			String update = String.format(
					"INSERT INTO FAFILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					Test1.getID(), Test1.getPath(), Test1.getName(),
					Test1.getSize(), Test1.getExtension(),
					getActiveStatus(Test1.isActive()), Test1.getModDate(), Test1.getMemo());
			statement.executeUpdate(update);

			update = String.format(
					"INSERT INTO FAFILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					Test2.getID(), Test2.getPath(), Test2.getName(),
					Test2.getSize(), Test2.getExtension(),
					getActiveStatus(Test2.isActive()), Test2.getModDate(), Test2.getMemo());
			statement.executeUpdate(update);

			update = String.format(
					"INSERT INTO FAFILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					Test3.getID(), Test3.getPath(), Test3.getName(),
					Test3.getSize(), Test3.getExtension(),
					getActiveStatus(Test3.isActive()), Test3.getModDate(), Test3.getMemo());
			statement.executeUpdate(update);
			
			// Create Test Data for FILERECORD
		    GregorianCalendar date4 = new GregorianCalendar(2018, 00, 20);
		    Timestamp sqlDate4 = new Timestamp(date4.getTimeInMillis());
		    GregorianCalendar date5 = new GregorianCalendar(2018, 00, 21);
		    Timestamp sqlDate5 = new Timestamp(date5.getTimeInMillis());
		    GregorianCalendar date6 = new GregorianCalendar(2018, 00, 22);
		    Timestamp sqlDate6 = new Timestamp(date6.getTimeInMillis());

			FileRecord Test4 = new FileRecord(1, 1, "test1", "c:\\testfiles\\", 1, "docx",
					sqlDate4);
			FileRecord Test5 = new FileRecord(2, 1, "test1", "c:\\testfiles\\", 1, "docx",
					sqlDate5);
			FileRecord Test6 = new FileRecord(3, 1, "test1", "c:\\testfiles\\", 1, "docx",
					sqlDate6);

			System.out.println("\nFileRecord data to be inserted");
			System.out.println(Test4);
			System.out.println(Test5);
			System.out.println(Test6);
			
			update = String.format(
					"INSERT INTO FILERECORD (frID, frfiID, frPATH, frNAME, frSIZE, frEXTENSION, frMOD_DATE) "
							+ "VALUES(%d, %d, '%s', '%s', %d, '%s', '%s')",
					Test4.getID(), Test4.getFAFileID(), Test4.getPath(), Test4.getName(),
					Test4.getSize(), Test4.getExtension(),
					Test4.getModDate());
			statement.executeUpdate(update);

			update = String.format(
					"INSERT INTO FILERECORD (frID, frfiID, frPATH, frNAME, frSIZE, frEXTENSION, frMOD_DATE) "
							+ "VALUES(%d, %d, '%s', '%s', %d, '%s', '%s')",
					Test5.getID(), Test5.getFAFileID(), Test5.getPath(),
					Test5.getName(), Test5.getSize(), Test5.getExtension(),
					Test5.getModDate());
			statement.executeUpdate(update);

			update = String.format(
					"INSERT INTO FILERECORD (frID, frfiID, frPATH, frNAME, frSIZE, frEXTENSION, frMOD_DATE) "
							+ "VALUES(%d, %d, '%s', '%s', %d, '%s', '%s')",
					Test6.getID(), Test6.getFAFileID(), Test6.getPath(),
					Test6.getName(), Test6.getSize(), Test6.getExtension(),
					Test6.getModDate());
			statement.executeUpdate(update);

			
			// Release the resources (clean up )
			statement.close();
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
		System.out.println("\nFileAid Installed.");

	}
	
	public void clearDB()
	{

		try
		{
			Statement statement = conn.createStatement();

			// Call utility method to check if table exists.
			// Create the table if needed
			if (wwdChk4Table(conn))
			{
				System.out.println(" . . . . clearing table FAFILE");
				statement.executeUpdate("DELETE FROM FAFILE WHERE 1=1");
			}

			// Release the resources (clean up )
			statement.close();
			//System.out.println("Closed connection");

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
	         s.execute("update FAFILE set fiID = 999, fiPATH = 'C:\', fiNAME = 'TEST ENTRY', fiSIZE = 1, fiEXTENSION = '.DOCX',"
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
	   }//end wwdChk4Table

		public void createConnection()
		{
			File directory = new File("c:/FileAid/jdbcFileAidPilotDB");
			if (!directory.exists())
			{
				System.out.println("FileAid has not been installed.");
				System.out.println("...Creating Database");
				createDB();
			}
			try
			{
				Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
				// Get a connection
				conn = DriverManager.getConnection(connectionURL);

				if (wwdChk4Table(conn))
				{
					int numRows = DBConnection.getNumTableRows(conn, "FAFILE");
					System.out.println("Database Exists with "
							+ numRows + " file records.");
				} // end if

			}
			catch (Exception except)
			{
				except.printStackTrace();
			}
		}// end createConnection

		public void selectRecords()
		{
			try
			{
				statement = conn.createStatement();
				ResultSet results = statement.executeQuery("select * from FAFILE");
				int numRows = DBConnection.getNumTableRows(conn, "FAFILE");

				System.out.println(
						"\n-------------------------------------------------");

				if (numRows == 0)
				{
					System.out
							.println("There are no records in FAFILE.");
				}
				else
				{

					while (results.next())
					{
						FAFile printFile = new FAFile(results.getInt(1),
								results.getString(3), results.getString(2),
								results.getInt(4), results.getString(5),
								results.getString(8), results.getTimestamp(7));
						System.out.println("\n" + printFile.toString());
					} // end while
					results.close();
					statement.close();
				}
			}
			catch (SQLException sqlExcept)
			{
				sqlExcept.printStackTrace();
			}
		}// end selectRecords
		
		public void insertFile (FAFile file)
		{
			try
			{
				statement = conn.createStatement();

				String update = String.format(
						"INSERT INTO FAFILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
								+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
						file.getID(), file.getPath(), file.getName(),
						file.getSize(), file.getExtension(),
						getActiveStatus(file.isActive()),
						file.getModDate(), file.getMemo());
				statement.execute(update);
				statement.close();
			}
			catch (SQLException sqlExcept)
			{
				sqlExcept.printStackTrace();
			}
		}// end insertFile

		public void shutdown()
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
				if (conn != null)
				{
					DriverManager.getConnection(connectionURL + ";shutdown=true");
					conn.close();
				}
			}
			catch (SQLException sqlExcept)
			{

			}
			System.out.println("\n\nFileAid DB shut down succcessfully");

		} // end shutdown
	

} // end class


