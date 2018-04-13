package fileAid;
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
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
	private String userName;
	private String password;
	private int userID = 0;
	private String userNamePasswordURL;
	private Connection conn = null;
	public final static long LOCKOUT_PERIOD = 60;
	public final static int MAX_FAILED_LOGINS = 3;
	
	public DBConnection()
	{}
	
	public DBConnection(String newUserName, String newPassword)
	{
		userName = newUserName;
		password = newPassword;
		userNamePasswordURL = ";user=" + userName + ";password=" + password;
		if(newUserName.equals("admin"))
			userID = 1;
		else if(newUserName.equals("user"))
			userID = 2;
		else
			userID = 3;
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
				System.out.println(" . . . . creating FileAid Database");
				for (String createString : createStrings)
					{
					//System.out.println(createString);
					statement.execute(createString);
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
	
	// Checks for existence of DB, creates it if necessary, and establishes
	// connection
	public String createConnection()
	{
		// Check for existence of database
		File directory = new File("c:/FileAid/" + dbName);
		if (!directory.exists())
		{
			System.out.println("FileAid has not been installed.");
			System.out.println("...Creating Database");
			createDB();
		}

		// Determine the status of the user
		String status = getUserStatus();
		System.out.println(status);

		if (status.equals("LOGGED_IN_FIRST_LOGIN") | status.equals("LOGGED_IN"))
		{
			try
			{
				Class.forName(driver).newInstance();
			}
			catch (java.lang.ClassNotFoundException | InstantiationException
					| IllegalAccessException e)
			{
				e.printStackTrace();
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);			}
			try
			{
				conn = DriverManager
						.getConnection(connectionURL + userNamePasswordURL);
			}
			catch (Exception except)
			{
				// except.printStackTrace();
			}
			try
			{
				Statement statement = conn.createStatement();
				// Set the SCHEMA to the default "APP" for everyone
				statement.execute("SET SCHEMA APP");

				int numRows = DBConnection.getNumTableRows(conn, "FAFILE");
				System.out.println(
						"Database Exists with " + numRows + " file records.");
			}
			catch (Exception except)
			{
				except.printStackTrace();
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
		} // end if
		return status;
	}// end createConnection
	
		// Shutdown the database
	   public void shutdown()
		{
			try
			{
				if (conn != null)
				{
					DriverManager.getConnection(connectionURL + ";shutdown=true" + userNamePasswordURL);
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
				statement.executeUpdate("DELETE FROM MEMO WHERE 1=1");
				statement.executeUpdate("DELETE FROM LINK WHERE 1=1");
				statement.executeUpdate("DELETE FROM TICKLER WHERE 1=1");
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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}
	} // end clearDB
	
	// Given a FAFile ID, deletes that record, returns true if successful, false if record not found
	public boolean deleteFAFile(int ID)
	{
		if (this.findFAFile(ID) != null)
		{
			String updateFileRecord = "DELETE FROM FILERECORD WHERE frfiID=" + ID;
			String updateMemoRecord = "DELETE FROM MEMO WHERE mefiID=" + ID;
			String updateLinkRecord = "DELETE FROM LINK WHERE lifiID=" + ID;
			String updateLinkedRecord = "DELETE FROM LINK WHERE liLINKEDFILEID=" + ID;
			String updateTicklerRecord = "DELETE FROM TICKLER WHERE tifiID=" + ID;			
			String updateFAFile = "DELETE FROM FAFILE WHERE fiID=" + ID;
			try
			{
				Statement statement = conn.createStatement();
				statement.executeUpdate(updateFileRecord);
				statement.executeUpdate(updateMemoRecord);
				statement.executeUpdate(updateLinkRecord);
				statement.executeUpdate(updateLinkedRecord);
				statement.executeUpdate(updateTicklerRecord);
				statement.executeUpdate(updateFAFile);
				statement.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.DELETE_FAFILE, userID);
			insertActionLogItem(alItem);
			return true;
		} // end if
		return false;
	}
	
		// Returns an ArrayList of all FAFile records in DB sorted by file name; 
		// returns null if there are none
		public ArrayList<FAFile> getAllFAFileRecords()
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
					    FileHistory fileHistory = getFileRecords(results.getInt(1));
					    ArrayList<Memo> memos = getMemoRecords(results.getInt(1));
					    ArrayList<Integer> links = getLinkRecords(results.getInt(1));
					    ArrayList<Tickler> ticklers = getTicklerRecords(results.getInt(1));

						FAFile newFAFile = new FAFile(results.getInt(1),
								results.getString(2), results.getString(3),
								convertBooleanDB(results.getInt(4)), fileHistory, 
								memos, links, ticklers);
						allFAFileRecords.add(newFAFile);
					} // end while
					results.close();
					statement.close();
				}
			}
			catch (SQLException sqlExcept)
			{
				sqlExcept.printStackTrace();
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			Collections.sort(allFAFileRecords);
			return allFAFileRecords;
		}// end selectAllRecords

		// Converts the ArrayList of all FAFile Records into an ArrayList of FAFileDisplay Records
		// Intended to facilitate UI display of FAFile Record information as well as searching records
		// Uses most recent FileRecord in FileHistory
		public ArrayList<FAFileDisplay> getFAFileDisplayRecords()
		{
			ArrayList<FAFile> allFAFiles = this.getAllFAFileRecords();
			ArrayList<FAFileDisplay> allFAFileDisplayRecords = new ArrayList<>();
			
			for(FAFile faFile : allFAFiles)
			{
				FAFileDisplay newFAFileDisplay = new FAFileDisplay(faFile.getID(),
						faFile.getName(), faFile.getExtension(),
						faFile.isActive(), faFile.getMemos(), faFile.getLinks(),
						faFile.getTicklers(), faFile.getHistory().get(0).getPath(), 
						faFile.getHistory().get(0).getSize(),
						faFile.getHistory().get(0).getModDate());
				allFAFileDisplayRecords.add(newFAFileDisplay);
			}
			Collections.sort(allFAFileDisplayRecords);
			return allFAFileDisplayRecords;		}
		
		// Select FileRecords for a particular FAFile, Returns null if FAFile does not exist or has no records
		// Records are returned in ascending order by date
		public FileHistory getFileRecords(int faFileID)
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
								results.getString(3), results.getInt(4),
								results.getTimestamp(5));
						fileRecords.add(newFileRecord);
					} // end while
					results.close();
					statement.close();
				}
			}
			catch (SQLException sqlExcept)
			{
				sqlExcept.printStackTrace();
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			Collections.sort(fileRecords, Collections.reverseOrder());
			return fileRecords;
		}// end selectRecords
		
		// Updates Active field for a particular record
		// Returns true if successful, false if unable to locate faFile
		public boolean updateActive(int faFileID, boolean active)
	{
		if (this.findFAFile(faFileID) == null)
		{
			return false;
		}
		else
		{
			int activeStatus = 0;
			if (active)
			{
				activeStatus = 1;
			}
			String update = "update FAFile set fiActive =" + activeStatus +
					"where fiID=" + faFileID;
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
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.MODIFY_FAFILE, userID);
			insertActionLogItem(alItem);
			return true;
		} // end else
	}
		
	public boolean getActive(int faFAFileID)
	{
		String query =	"select fiActive from FAFile where fiID="+ faFAFileID;
		boolean status = true;
		
		try
		{
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(query);
				while (results.next())
				{
					status = convertBooleanDB(results.getInt(1));
				} // end while
				results.close();
				statement.close();
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}
		return status;
	}
		
	public boolean insertFile(FAFile file)
	{
		String update = ""; 
		int ID = file.getID();
		if (findFAFile(ID) == null)
		{
			update = String.format(
					"INSERT INTO FAFILE (fiID, fiNAME, fiEXTENSION, fiACTIVE) "
							+ "VALUES(%d, '%s', '%s', %d)",
					file.getID(), file.getName(),file.getExtension(),
					convertBooleanObject(file.isActive()));
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
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			
			//Insert FileHistory Records
			FileHistory fileHistory = file.getHistory();
			for (int i = 0; i < fileHistory.getUpdateCount(); i++)
			{
				insertFileRecord(fileHistory.getRecord(i));
			}
			
			//Insert Memo Records
			for(Memo memo : file.getMemos())
			{
				insertMemo(memo, ID);
			}

			//Insert Link Records
			for(Integer link : file.getLinks())
			{
				insertLink(link, ID);
			}

			//Insert Tickler Records
			for(Tickler tickler : file.getTicklers())
			{
				insertTickler(tickler, ID);
			}	
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.ADD_FAFILE, userID);
			insertActionLogItem(alItem);
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
					"INSERT INTO FILERECORD (frfiID, frPATH, frSIZE, frMOD_DATE) "
							+ "VALUES(%d, '%s', %d, '%s')",
							fileRecord.getFAFileID(), fileRecord.getPath(),
							fileRecord.getSize(), fileRecord.getModDate());
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
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.ADD_FILERECORD, userID);
			insertActionLogItem(alItem);

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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}		
		return summaryRecords;
	} // end getSummaryRecords

	//Inserts an ActionLogItem
	public void insertActionLogItem(ActionLog alItem)
	{
		String update = String.format(
				"INSERT INTO ACTIONLOG (alDATE, alTYPE, alusID) "
				+ "VALUES('%s', '%s', %d)",
				alItem.getDate(), (alItem.getType().toString()), alItem.getUser());
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
		
	}// end insertActionLogItem

	// Returns all ActionLog records; return null if there are none
	public ArrayList<ActionLog> getActionLogRecords()
	{
		ArrayList<ActionLog> actionLogRecords = new ArrayList<>();
		String query = "select * from ActionLog";
		try
		{
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(query);
			int numRows = DBConnection.getNumTableRows(conn, "ACTIONLOG");

			if (numRows == 0)
			{
				return null;
			}
			else
			{
				while (results.next())
				{
					ActionLog actionLog = new ActionLog(results.getTimestamp(2),
							ActionLogType.valueOf(results.getString(3)), results.getInt(4));
					actionLogRecords.add(actionLog);
				} // end while
				results.close();
				statement.close();
			}
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}		
		return actionLogRecords;
	} // end getActionLogRecords

	// Deletes all ActionLog records from database
	public void clearActionLog()
	{
		try
		{
			Statement statement = conn.createStatement();

				System.out.println(" . . . . clearing ActionLog");
				statement.executeUpdate("DELETE FROM ACTIONLOG WHERE 1=1");

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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}
	} // end clearActionLog
	
	// Inserts a Memo, returns true if successful, false if FAFile not
	// found
	public boolean insertMemo(Memo memo, int faFileID)
	{
		String update = String.format(
				"INSERT INTO MEMO (meDATE, meTEXT, mefiID) "
						+ "VALUES('%s', '%s', %d)",
				memo.getDate(), memo.getMemoText(), faFileID);
		Statement statement = null;

		if (findFAFile(faFileID) == null)
		{
			return false;
		}
		else
		{
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
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			//Create an ActionLog Item and record to database
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.ADD_MEMO, userID);
			insertActionLogItem(alItem);
			return true;
		}
	}// end insertMemo

	// Returns all Memo records; return null if there are none
	public ArrayList<Memo> getMemoRecords(int FAFileID)
	{
		ArrayList<Memo> memoRecords = new ArrayList<>();
		String query = "select * from MEMO where mefiID = " + FAFileID;
		try
		{
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(query);
			int numRows = DBConnection.getNumTableRows(conn, "MEMO");

			if (numRows == 0)
			{
				return null;
			}
			else
			{
				while (results.next())
				{
					Memo memo = new Memo(results.getTimestamp(2), results.getString(3));
					memoRecords.add(memo);
				} // end while
				results.close();
				statement.close();
			}
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}		
		return memoRecords;
	} // end getMemoRecords

	// Deletes a memo given FAFile ID and date of memo
	public boolean deleteMemo(int faFileID, Timestamp date)
	{
		if(findFAFile(faFileID) == null)
		{
			return false;
		}
		else
		{
			String update = "DELETE FROM MEMO WHERE mefiID=" + faFileID +
					" AND meDATE=" + date;
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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.DELETE_MEMO, userID);
			insertActionLogItem(alItem);
			return true;
		} // end else

	}
	
	// Inserts a Link, returns true if successful, false if FAFile not
	// found
	public boolean insertLink(Integer link, int faFileID)
	{
		String update = String.format(
				"INSERT INTO LINK (lifiID, liLINKEDFILEID) "
						+ "VALUES(%d, %d)",
				faFileID, link);
		Statement statement = null;

		if (findFAFile(faFileID) == null)
		{
			return false;
		}
		else
		{
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
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.ADD_LINKAGE, userID);
			insertActionLogItem(alItem);
			return true;
		}
	}// end insertLink

	// Returns all Link records; return null if there are none
	public ArrayList<Integer> getLinkRecords(int FAFileID)
	{
		ArrayList<Integer> linkRecords = new ArrayList<>();
		String query = "select * from LINK where lifiID = " + FAFileID;
		try
		{
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(query);
			int numRows = DBConnection.getNumTableRows(conn, "LINK");

			if (numRows == 0)
			{
				return null;
			}
			else
			{
				while (results.next())
				{
					linkRecords.add(results.getInt(3));
				} // end while
				results.close();
				statement.close();
			}
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}		
		return linkRecords;
	} // end getLinkRecords

	// Deletes a link given FAFile ID and linked FAFile ID
	public void deleteLink(int faFileID, int linkedFAFileID)
	{
			String update = "DELETE FROM MEMO WHERE lifiID=" + faFileID +
					" AND liLINKEDFILEID=" + linkedFAFileID;
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
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.DELETE_LINKAGE, userID);
			insertActionLogItem(alItem);
	} // end deleteLink

	// Inserts a Tickler, returns true if successful, false if FAFile not
	// found
	public boolean insertTickler(Tickler tickler, int faFileID)
	{
		String update = String.format(
				"INSERT INTO TICKLER (tifiID, tiDATE, tiACTION, tiRESOLVED) "
						+ "VALUES(%d, '%s', '%s', %d)",
						faFileID, tickler.getDate(), tickler.getAction(), 
						convertBooleanObject(tickler.isResolved()));
		Statement statement = null;

		if (findFAFile(faFileID) == null)
		{
			return false;
		}
		else
		{
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
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.ADD_TICKLER, userID);
			insertActionLogItem(alItem);
			return true;
		}
	}// end insertTickler

	// Returns Tickler records; return null if there are none
	public ArrayList<Tickler> getTicklerRecords(int FAFileID)
	{
		ArrayList<Tickler> ticklerRecords = new ArrayList<>();
		String query = "select * from TICKLER where tifiID = " + FAFileID;
		try
		{
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(query);
			int numRows = DBConnection.getNumTableRows(conn, "TICKLER");

			if (numRows == 0)
			{
				return null;
			}
			else
			{
				while (results.next())
				{
					Tickler tickler = new Tickler(results.getInt(2), results.getTimestamp(3),
							results.getString(4), convertBooleanDB(results.getInt(5)));
					ticklerRecords.add(tickler);
				} // end while
				results.close();
				statement.close();
			}
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}		
		return ticklerRecords;
	} // end getTicklerRecords

	// Deletes a Tickler given FAFile ID and date of memo
	public void deleteTickler(int faFileID, Timestamp date)
	{
			String update = "DELETE FROM TICKLER WHERE tifiID=" + faFileID +
					" AND tiDATE=" + date;
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
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.DELETE_TICKLER, userID);
			insertActionLogItem(alItem);
	}
	
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
		String restoreConnectionURL = "jdbc:derby:C:/FileAid/" + dbName + "; restoreFrom=" + 
		backupDirectory + userNamePasswordURL;
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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
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
	
	// Returns a password for the specified user; returns null if user does not
	// exist
	public String getPassword(String user)
	{
		String password = "";
		String query = "select usPassword from FA_USER where usName='" + user + "'";
		try
		{
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(query);

			if (results == null)
			{
				return null;
			}
			else
			{
				while(results.next())
				{
				password = results.getString(1);
				}
				results.close();
				statement.close();
			}
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}
		return password;
	}// end getPassword
	
	// Returns a password for the specified user; returns null if user does not
	// exist
	public int getNextFAFileID()
	{
		int nextID = 0;
		String query = "select fiID from FAFILE";
		try
		{
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(query);

				while(results.next())
				{
				nextID = results.getInt(1);
				}
				results.close();
				statement.close();
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}
		return ++nextID;
	}// end getPassword
	
	// Update password (also updates FIRSTLOGIN to 0 aka false)
	public boolean updatePassword(String user, String newPassword)
	{
		if (userName.equals("admin") | userName.equals(user))
		{
			String update = "update FA_USER set usPASSWORD ='" + newPassword + 
					"', usFIRSTLOGIN= 0 where usNAME='" + user + "'";
		try
		{
			Statement statement = conn.createStatement();
			statement.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY("
					+ "    'derby.user." + user + "', '" + newPassword + "')");
			statement.executeUpdate(update);
			statement.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}
		return true;
		} // end if
		else
		{
			return false;
		}
	}
	
	public ArrayList<Login> getAllLogins()
	{
		ArrayList<Login> allLogins = new ArrayList<Login>();
		String query = "select * from LOGIN";
		try
		{
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(query);
			int numRows = DBConnection.getNumTableRows(conn, "LOGIN");

			if (numRows == 0)
			{
				return null;
			}
			else
			{
				while (results.next())
				{
					Login login = new Login(results.getInt(2), results.getTimestamp(3),
							convertBooleanDB(results.getInt(4)));
					allLogins.add(login);
				} // end while
				results.close();
				statement.close();
			}
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}			
		return allLogins;
	}
	
	
	public boolean unlockAccount(String lockedAccountUserName)
	{
		if(!userName.equals("admin"))
				{
			return false;
				}
		else
		{
			String update = "update FA_USER set usLOCKOUT = null, usCONSECFAILEDLOGINS = 0 "
				+ "where usName='" + lockedAccountUserName + "'";
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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}
		return true;
		} //end else		
	} //end unLockAccount


//////////////////DBConnection Internal Helper Methods//////////////////////////////////////////


	
	// Return a specific FAFile object given its ID, returns null if ID does not exist
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
			    FileHistory fileHistory = getFileRecords(results.getInt(1));
			    ArrayList<Memo> memos = getMemoRecords(results.getInt(1));
			    ArrayList<Integer> links = getLinkRecords(results.getInt(1));
			    ArrayList<Tickler> ticklers = getTicklerRecords(results.getInt(1));

				faFile = new FAFile(results.getInt(1),
						results.getString(2), results.getString(3),
						convertBooleanDB(results.getInt(4)), fileHistory, 
						memos, links, ticklers);
			} // end while
			results.close();
			statement.close();

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}
		
		return faFile;
	} // end findFAFile
		
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
		private static int convertBooleanObject(boolean bool)
		{
			if (bool)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}//end convertBooleanObject

		// Convert Boolean Active to SmallInt for SQL table
		private static boolean convertBooleanDB(int bool)
		{
			if (bool == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}//end convertBooleanDB

		// Returns the user's status (LOGGED_IN, LOGGED_IN_FIRST_LOGIN, INCORRECT_PASSWORD, LOCKED_OUT)
		private String getUserStatus()
		{
			String status = "";
			String correctPassword = "";
			int firstLogin = 0;
			int consecFailedLogins = 0;
			Timestamp lockout = null;
			int userID = 0;
			
			try
			{
				Class.forName(driver).newInstance();
			}
			catch (java.lang.ClassNotFoundException | InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			try
			{
				conn = DriverManager
						.getConnection(connectionURL + ";user=superadmin;password=Sa123");
			}
			catch (Exception except)
			{
				except.printStackTrace();
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			try
			{
				Statement statement = conn.createStatement();
				// Set the SCHEMA to the default "APP" for everyone
				statement.execute("SET SCHEMA APP");
			}
			catch (Exception except)
			{
				except.printStackTrace();
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			
			String query = "select * from FA_USER where usName = '" + userName + "'";
			try
			{
				Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery(query);
					while (results.next())
					{
					    userID = results.getInt(1);
					    correctPassword = results.getString(3);
					    firstLogin = results.getInt(4);
					    consecFailedLogins = results.getInt(5);
					    lockout = results.getTimestamp(6);
					} // end while
					results.close();
					statement.close();
			}
			catch (SQLException sqlExcept)
			{
				sqlExcept.printStackTrace();
				ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
				insertActionLogItem(alItem);
			}
			
			// Evaluate lockout to determine current status
			if (lockout != null)
			{
				GregorianCalendar gcLockout = new GregorianCalendar();
				gcLockout.setTimeInMillis(lockout.getTime());
				GregorianCalendar now = new GregorianCalendar();
				long minutesDifference = minutesBetween(now, gcLockout);
				if (minutesDifference > LOCKOUT_PERIOD)
				{
					unlockAccount(userName);
					lockout = null;
				}
			}
			
			if (lockout != null)
			{
				status = "LOCKED_OUT";
				insertLogin(userID, 0);
			}
			else
			{
				if (!password.equals(correctPassword))
				{
					status = "INCORRECT_PASSWORD";
					insertLogin(userID, 0);
					consecFailedLogins = consecFailedLogins + 1;
					System.out.println("Failed Logins: " + consecFailedLogins);
					if(consecFailedLogins >= MAX_FAILED_LOGINS)
					{
						lockAccount(userName);
					}
					else
					{
						incrementFailedLogins(userName, consecFailedLogins);
					}
					//incrementLockOutCounter(userID);
					//increment consecFailedLogins, lockout if necessary 
				}
				else if (firstLogin == 1)
				{
					status = "LOGGED_IN_FIRST_LOGIN";
					insertLogin(userID, 1);
				}
				else
				{
					status = "LOGGED_IN";
					insertLogin(userID, 1);
					
				}
			}
			shutdown();
			return status;
		}// end getUserStatus


	private boolean insertLogin(int userID, int success)
	{
		GregorianCalendar date = new GregorianCalendar();
		Timestamp sqlDate = new Timestamp(date.getTimeInMillis());
		String update = String
				.format("INSERT INTO LOGIN (lousID, loDATE, loSUCCESS) "
						+ "VALUES(%d, '%s', %d)", userID, sqlDate, success);
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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}
		return true;
	}
	
	
	public void lockAccount(String userName)
	{
		GregorianCalendar date = new GregorianCalendar();
		Timestamp sqlDate = new Timestamp(date.getTimeInMillis());
		String update = "update FA_USER set usLOCKOUT = '" + sqlDate
				+ "' where usName='" + userName + "'";
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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}

	} //end lockAccount

	
	public void incrementFailedLogins(String userName, int consecFailedLogins)
	{
		System.out.println("incrementFailedLogins started.");
		String update = "update FA_USER set usCONSECFAILEDLOGINS = " + consecFailedLogins
				+ " where usName='" + userName + "'";
		System.out.println(update);
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
			ActionLog alItem = new ActionLog(getDateTimeNow(), ActionLogType.RUN_TIME_ERROR, userID);
			insertActionLogItem(alItem);
		}

	} //end incrementFailedLogins

	// Get today's date in Timestamp format
	public static Timestamp getDateTimeNow()
	{
		GregorianCalendar date = new GregorianCalendar();
		Timestamp dateTimeNow = new Timestamp(date.getTimeInMillis());
		return dateTimeNow;
	}
	// Get the minutes between two GregorianCalendar dates
	private static long minutesBetween(GregorianCalendar startDate, GregorianCalendar endDate) {
	    long end = endDate.getTimeInMillis();
	    long start = startDate.getTimeInMillis();
	    return TimeUnit.MILLISECONDS.toMinutes(Math.abs(end - start));
	}

		
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
			    
			    
				FAFile Test1 = new FAFile(1, "test1", "docx");
				FAFile Test2 = new FAFile(2, "test2", "xlsx");
				FAFile Test3 = new FAFile(3, "test3","pptx");

				System.out.println("\nFile data to be inserted");
				System.out.println(Test1);
				System.out.println(Test2);
				System.out.println(Test3);


				String update = String.format(
						"INSERT INTO FAFILE (fiID, fiNAME, fiEXTENSION, fiACTIVE) "
								+ "VALUES(%d, '%s', '%s', %d)",
						Test1.getID(), Test1.getName(), Test1.getExtension(),
						convertBooleanObject(Test1.isActive()));
				statement.executeUpdate(update);

				update = String.format(
						"INSERT INTO FAFILE (fiID, fiNAME, fiEXTENSION, fiACTIVE) "
								+ "VALUES(%d, '%s', '%s', %d)",
						Test2.getID(), Test2.getName(), Test2.getExtension(),
						convertBooleanObject(Test2.isActive()));
				statement.executeUpdate(update);

				update = String.format(
						"INSERT INTO FAFILE (fiID, fiNAME, fiEXTENSION, fiACTIVE) "
								+ "VALUES(%d, '%s', '%s', %d)",
						Test3.getID(), Test3.getName(), Test3.getExtension(),
						convertBooleanObject(Test3.isActive()));
				statement.executeUpdate(update);
				
				// Create Test Data for FILERECORD

				FileRecord test4 = new FileRecord(1, "c:\\testfiles\\", 1, sqlDate1);
				FileRecord test5 = new FileRecord(2, "c:\\testfiles\\", 1, sqlDate2);
				FileRecord test6 = new FileRecord(3, "c:\\testfiles\\", 1, sqlDate3);

				System.out.println("\nFileRecord data to be inserted");
				System.out.println(test4);
				System.out.println(test5);
				System.out.println(test6);
				
				update = String.format(
						"INSERT INTO FILERECORD (frfiID, frPATH, frSIZE, frMOD_DATE) "
								+ "VALUES(%d, '%s', %d, '%s')",
								test4.getFAFileID(), test4.getPath(),
								test4.getSize(), test4.getModDate());
				statement.executeUpdate(update);

				update = String.format(
						"INSERT INTO FILERECORD (frfiID, frPATH, frSIZE, frMOD_DATE) "
								+ "VALUES(%d, '%s', %d, '%s')",
								test5.getFAFileID(), test5.getPath(),
								test5.getSize(), test5.getModDate());
				statement.executeUpdate(update);

				update = String.format(
						"INSERT INTO FILERECORD (frfiID, frPATH, frSIZE, frMOD_DATE) "
								+ "VALUES(%d, '%s', %d, '%s')",
								test6.getFAFileID(), test6.getPath(),
								test6.getSize(), test6.getModDate());
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


