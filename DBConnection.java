import java.io.File;
import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

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
 * Manages data traffic to and from  FileAid SQL Database
 * 
 * @author David Matthews
 *
 */

public class DBConnection

{
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String dbName = "FileAidDB";
	// define the Derby connection URL to use
	private String connectionURL = "jdbc:derby:C:/FileAid/" + dbName + ";create=true";
	private Connection conn = null;
	
	public DBConnection()
	{
		
	}

//////////////////DB Creation, Connection, and Shutdown Utilities////////////////////////
	
	// Creates database structure; should only be called by createConnection
	public void createDB()
	{
		// Read DDL into a String from an external text file
		File sourceFile = new File("DBCreate.txt");
		String ddl = "";
		try
		{
			ddl = readAFile(sourceFile);
		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Split the single string into an array of strings, 
		// each consisting of a single DDL statement
		String[] createStrings = ddl.split("\\;");

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
			Statement statement = conn.createStatement();

			// Create Database
			// Create a statement to issue simple commands.
			// Call utility method to check if FAFile table exists.
			// Create the DB if needed
			if (!DBConnection.wwdChk4Table(conn))
			{
				System.out.println(" . . . . creating FileAid Database");
				for (String createString : createStrings)
					{
					statement.execute(createString);
					}
			}
			
			// Release the resources (clean up )
			statement.close();
			//System.out.println("Closed connection");

			// Database Shutdown
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
	
	//Checks for existence of DB, creates it if necessary, and establishes connection
	   public void createConnection()
		{
			File directory = new File("c:/FileAid/" + dbName);
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

		// Shutdown the database
	   public void shutdown()
		{
			try
			{
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

//////////////////DB Management////////////////////////////////////////////
		
	// Deletes all records from database
	public void clearDB()
	{
		try
		{
			Statement statement = conn.createStatement();

				System.out.println(" . . . . clearing FileAid Database");
				statement.executeUpdate("DELETE FROM FILERECORD WHERE 1=1");
				statement.executeUpdate("DELETE FROM FAFILE WHERE 1=1");
				//statement.executeUpdate("DELETE FROM SUMMARYRECORD WHERE 1=1");

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
	
	// Given a FAFile ID, deletes that record, returns true if successful, false if record not found
	public boolean deleteFAFile(int ID)
	{
		if (this.findFAFile(ID) != null)
		{
			String updateFileRecord = "DELETE FROM FILERECORD WHERE frfiID=" + ID;
			String updateFAFile = "DELETE FROM FAFILE WHERE fiID=" + ID;
			try
			{
				Statement statement = conn.createStatement();
				statement.executeUpdate(updateFileRecord);
				statement.executeUpdate(updateFAFile);
				statement.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} // end if
		return false;
	}

	
	/***      Check for  FILE table    ****/
	   private static boolean wwdChk4Table (Connection conTst ) throws SQLException {
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
	      return true;
	   }  /*** END wwdInitTable  **/
	   
		// Returns an ArrayList of all FAFile records in DB; returns null if there are none
		public ArrayList<FAFile> selectAllRecords()
		{
			ArrayList<FAFile> allFAFileRecords = new ArrayList<>();
			String query = "select * from FAFILE";
			try
			{
				Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery(query);
				int numRows = DBConnection.getNumTableRows(conn, "FAFILE");


				if (numRows == 0)
				{
					return null;
				}
				else
				{

					while (results.next())
					{
					    FileHistory fileHistory = selectFileRecords(results.getInt(1));

						FAFile newFAFile = new FAFile(results.getInt(1),
								results.getString(3), results.getString(2),
								results.getInt(4), results.getString(5),
								results.getString(8), results.getTimestamp(7),
								fileHistory);
						allFAFileRecords.add(newFAFile);
					} // end while
					results.close();
					statement.close();
				}
			}
			catch (SQLException sqlExcept)
			{
				sqlExcept.printStackTrace();
			}
			return allFAFileRecords;
		}// end selectAllRecords
				
		// Find a specific FAFile object given its ID, returns null if ID does not exist
		public FAFile findFAFile(int ID)
		{
			FAFile faFile = null;
			String query = "select * from FAFILE where fiID = " + ID;
            try
			{
    			Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery(query);
				while (results.next())
				{
				    FileHistory fileHistory = selectFileRecords(results.getInt(1));

					faFile = new FAFile(results.getInt(1),
							results.getString(3), results.getString(2),
							results.getInt(4), results.getString(5),
							results.getString(8), results.getTimestamp(7),
							fileHistory);
				} // end while
				results.close();
				statement.close();

			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return faFile;
		}
		
		// Select FileRecords for a particular FAFile, Returns null if FAFile does not exist or has no records
		public FileHistory selectFileRecords(int faFileID)
		{
			FileHistory fileRecords = new FileHistory();
			String query = "select * from FILERECORD where frfiID = " + faFileID;
			try
			{
				Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery(query);
				int numRows = DBConnection.getNumTableRows(conn, "FILERECORD");

				if (numRows == 0)
				{
					return null;
				}
				else
				{
					while (results.next())
					{
						FileRecord newFileRecord = new FileRecord(results.getInt(2),
								results.getString(4), results.getString(3),
								results.getInt(5), results.getString(6),
								results.getTimestamp(7));
						fileRecords.add(newFileRecord);
					} // end while
					results.close();
					statement.close();
				}
			}
			catch (SQLException sqlExcept)
			{
				sqlExcept.printStackTrace();
			}
			return fileRecords;
		}// end selectRecords
		
		// Update an existing FAFile
		// Takes updated FAFile object and substitutes it for the existing FAFile record
		// Returns true if successful, false if FAFile does not exist.
		public boolean upDateFAFile(FAFile faFile)
		{
			int ID = faFile.getID();
			if(findFAFile(ID) != null)
			{
				String update = "DELETE FROM FILERECORD WHERE frfiID =" + ID;
				update = "DELETE FROM FAFILE WHERE fiID=" + ID;
				try
				{
					Statement statement = conn.createStatement();
					statement.executeUpdate(update);
					statement.close();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				insertFile(faFile);
				return true;
			}
			else
			{
				return false;
			}
		}
		
	public boolean insertFile(FAFile file)
	{
		String update = ""; 
		int ID = file.getID();
		if (findFAFile(ID) == null)
		{
			update = String.format(
					"INSERT INTO FAFILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					file.getID(), file.getPath(), file.getName(),
					file.getSize(), file.getExtension(),
					getActiveStatus(file.isActive()), file.getModDate(),
					file.getMemo());
			Statement statement = null;
			try
			{
				statement = conn.createStatement();
				statement.execute(update);
				statement.close();
			}
			catch (SQLException sqlExcept)
			{
				sqlExcept.printStackTrace();
			}
			return true;
		} // end if
		else
		{
			return false;
		}

	}// end insertFile
	
	// Adds a FileRecord, return false if associated FAFile does not exist
	public boolean insertFileRecord(FileRecord fileRecord)
	{
		int ID = fileRecord.getFAFileID();
		if (findFAFile(ID) != null)
		{
			String update = String.format(
					"INSERT INTO FILERECORD (frfiID, frPATH, frNAME, frSIZE, frEXTENSION, frMOD_DATE) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', '%s')",
							fileRecord.getFAFileID(), fileRecord.getPath(), fileRecord.getName(),
							fileRecord.getSize(), fileRecord.getExtension(), fileRecord.getModDate());
			Statement statement = null;
			try
			{
				statement = conn.createStatement();
				statement.execute(update);
				statement.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		else
		{
			return false;
		}
	} // end insertFileRecord
	
	public void insertSummaryRecord(SummaryRecord sr)
	{
		String update = String.format(
				"INSERT INTO SUMMARYRECORD (srDATETIMECHECK, srNUMFILES, srDISKSPACE,"
				+ "srNUMFILESADDED, srNUMFILESDELETED, srNUMFILESMODIFIED) "
				+ "VALUES('%s', %d, %d, %d, %d, %d)",
				sr.getDateTimeCheck(), sr.getNumFiles(), sr.getDiskSpace(),
				sr.getNumFilesAdded(), sr.getNumFilesDeleted(), sr.getNumFilesModified());
		Statement statement = null;
		try
		{
			statement = conn.createStatement();
			statement.execute(update);
			statement.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // end insertSummaryRecord
	
	// Returns all SummaryRecord records; return null if there are none
	public ArrayList<SummaryRecord> getSummaryRecords()
	{
		ArrayList<SummaryRecord> summaryRecords = new ArrayList<>();
		String query = "select * from SummaryRecord";
		try
		{
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(query);
			int numRows = DBConnection.getNumTableRows(conn, "SUMMARYRECORD");

			if (numRows == 0)
			{
				return null;
			}
			else
			{
				while (results.next())
				{
					SummaryRecord summaryRecord = new SummaryRecord(results.getTimestamp(2),
							results.getInt(3), results.getInt(4),
							results.getInt(5), results.getInt(6),
							results.getInt(7));
					summaryRecords.add(summaryRecord);
				} // end while
				results.close();
				statement.close();
			}
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
		}		
		return summaryRecords;
	} // end getSummaryRecords
	
	// Back up FileAid to a directory named today's date in format yyyy-MM-dd
	// Method directly from Derby Documentation
	public void backUpDatabase() throws SQLException
	{
		// Get today's date as a string:
		java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		String backupdirectory = "c:/FileAidBackups/" + todaysDate
				.format((java.util.Calendar.getInstance()).getTime());
		CallableStatement cs = conn
				.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
		cs.setString(1, backupdirectory);
		cs.execute();
		cs.close();
		System.out.println("Backed up database to " + backupdirectory);
	}
	
	//Restores database from c:/FileAidBackups/backupName
	//Assumes FileAid is currently running
	public boolean restoreDatase(String backupDirectory)
	{
		backupDirectory = "c:/FileAidBackups/" + backupDirectory + "/" + dbName;
		File backupDirectoryFile = new File(backupDirectory);
		if (!backupDirectoryFile.exists())
		{
			System.out.println("\nBackup Directory " + backupDirectory + " does not exist.");
			return false;
		}
		else
		{
		
		shutdown();
		String restoreConnectionURL = "jdbc:derby:C:/FileAid/" + dbName + "; restoreFrom=" + backupDirectory;
		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			// Get a connection
			conn = DriverManager.getConnection(restoreConnectionURL);
		}
		catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		return true;
		}// end else
	} // end restoreDatabase

//////////////////DBConnection Internal Helper Methods//////////////////////////////////////////
	
	// Using provided connection, returns number of rows in tablename   
	private static int getNumTableRows(Connection conn, String tableName) throws SQLException
	   {
        int numRows = 0;
        String query = "select count(*) from " + tableName;
		try
		{
			Statement stmt = conn.createStatement();
         ResultSet results = stmt.executeQuery(query);
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
	   }//end getNumTableRows

		private static String readAFile(File fileName) throws FileNotFoundException {

			Scanner scan = new Scanner(fileName);

			String charBuffer = scan.nextLine();
			while (scan.hasNextLine()) {
				charBuffer = charBuffer + scan.nextLine();
			}
			scan.close();
			return charBuffer;
		} // end readAFile
		
		// Convert Boolean Active to SmallInt for SQL table
		private static int getActiveStatus(boolean active)
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

		// Inserts data for testing purposes
		public void insertTestData()
		{
			Statement statement;
			try
			{
				statement = conn.createStatement();
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

				FileRecord Test4 = new FileRecord(1, "test1", "c:\\testfiles\\", 1, "docx",
						sqlDate4);
				FileRecord Test5 = new FileRecord(1, "test1", "c:\\testfiles\\", 1, "docx",
						sqlDate5);
				FileRecord Test6 = new FileRecord(1, "test1", "c:\\testfiles\\", 1, "docx",
						sqlDate6);

				System.out.println("\nFileRecord data to be inserted");
				System.out.println(Test4);
				System.out.println(Test5);
				System.out.println(Test6);
				
				update = String.format(
						"INSERT INTO FILERECORD (frfiID, frPATH, frNAME, frSIZE, frEXTENSION, frMOD_DATE) "
								+ "VALUES(%d, '%s', '%s', %d, '%s', '%s')",
						Test4.getFAFileID(), Test4.getPath(), Test4.getName(),
						Test4.getSize(), Test4.getExtension(),
						Test4.getModDate());
				statement.executeUpdate(update);

				update = String.format(
						"INSERT INTO FILERECORD (frfiID, frPATH, frNAME, frSIZE, frEXTENSION, frMOD_DATE) "
								+ "VALUES(%d, '%s', '%s', %d, '%s', '%s')",
						Test5.getFAFileID(), Test5.getPath(),
						Test5.getName(), Test5.getSize(), Test5.getExtension(),
						Test5.getModDate());
				statement.executeUpdate(update);

				update = String.format(
						"INSERT INTO FILERECORD (frfiID, frPATH, frNAME, frSIZE, frEXTENSION, frMOD_DATE) "
								+ "VALUES(%d, '%s', '%s', %d, '%s', '%s')",
						Test6.getFAFileID(), Test6.getPath(),
						Test6.getName(), Test6.getSize(), Test6.getExtension(),
						Test6.getModDate());
				statement.executeUpdate(update);
				statement.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

} // end class


