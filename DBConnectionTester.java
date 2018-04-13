package fileAid;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAidPilot
//  File:     DBConnectionTester.java
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
public class DBConnectionTester
{

	/**
	 * (Insert a brief description that describes the purpose of this method) 
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		DBConnection dbConnection = null;
		Scanner scan = new Scanner(System.in);
		boolean success = false;
		System.out.println("\nWelcome to FileAid");
		while (!success)
		{
			System.out.print("\nPlease enter your username: ");
			String userName = scan.next();
			System.out.print("\nPlease enter your password: ");
			String password = scan.next();
			dbConnection = new DBConnection(userName, password);
			String status = dbConnection.createConnection();
			System.out.println(status);
			if (status.equals("INCORRECT_PASSWORD"))
			{
				System.out.println(
						"\nIncorrect username or password, please try again.");
			}
			else
				if(status.equals("LOCKED_OUT"))
				{
					System.out.println(
							"\nYou are locked out of the program.");
					System.exit(0);
				}
				else
					if(status.equals("LOGGED_IN_FIRST_LOGIN"))
					{
						System.out.println(
								"\nYou must change your password.");
						success = true;
					}
					else
			{
				success = true;
			}
		}		
		
		boolean exit = false;
		while (!exit)
		{
			System.out.print("\nChoose from the following options: "
					+ "\n1 \tShow all FAFile Records" 
					+ "\n2 \tShow all FAFile Records in FAFileDisplay Format" 
					+ "\n3 \tAdd a Record"
					+ "\n4 \tDelete All Records"
					+ "\n5 \tDelete a Record"
					+ "\n6 \tReturn Record with ID = 1"
					+ "\n7 \tInsert Test Data  (Don't do this twice without clearing the DB)"
					+ "\n8 \tReturn FileRecords for a particular FAFile"
					+ "\n9 \tAdd a FileRecord to FAFile ID = 2"
					+ "\n10 \tAdd a SummaryRecord"
					+ "\n11 \tShow all SummaryRecord Records" 
					+ "\n12 \tAdd an ActionLog Record" 
					+ "\n13 \tShow all ActionLog Records" 
					+ "\n14 \tClear all ActionLog Records" 
					+ "\n15 \tAdd a Memo to FAFile 1" 
					+ "\n16 \tAdd a Link to FAFile ID = 2 in the FAFile ID = 1 record" 
					+ "\n17 \tToggle the Active field for FAFile ID = 1" 
					+ "\n18 \tReturn the next available FAFile ID" 
					+ "\n19 \tChange the password for \"user\" account" 
					+ "\n20 \tShow all Logins" 
					+ "\n21 \tUnlock a locked user account" 
					+ "\n22 \tDelete a memo" 
					+ "\n23 \tBack up FileAid to c:/FileAidBackups/yyyy-MM-dd/" 
					+ "\n24 \tRestore a backup" 
					+ "\n25 \tExit" 
					+ "\nChoice: ");
			int response = scan.nextInt();
			switch (response)
			{
			case 1:
				ArrayList<FAFile> allFAFiles = dbConnection.getAllFAFileRecords();
				if (allFAFiles == null)
				{
					System.out.println("No FAFile records in database");
				}
				else
				{
					for (FAFile faFile : allFAFiles)
					{
						System.out.println("\n" + faFile.toString());
					}
				}
				break;
			case 2:
				ArrayList<FAFileDisplay> allFAFileDisplayRecords = dbConnection.getFAFileDisplayRecords();
				if (allFAFileDisplayRecords == null)
				{
					System.out.println("No FAFile records in database");
				}
				else
				{
					for (FAFileDisplay faFileDisplay : allFAFileDisplayRecords)
					{
						System.out.println("\n" + faFileDisplay.toString());
					}
				}
				break;
			case 3:
				FAFile newFile = createFile();
				success = dbConnection.insertFile(newFile);
				if (success)
				{
					System.out.println("\nNew FAFile successfully added.");
				}
				else
				{
					System.out.println("\nNew FAFile was already in the database.");
				}
				break;
			case 4:
				dbConnection.clearDB();
				break;
			case 5:
				System.out.print("Enter a record ID to delete: ");
				int delRecordID = scan.nextInt();
				success = dbConnection.deleteFAFile(delRecordID);
				if (success)
				{
					System.out.println("\nDeletion Successful");
				}
				else System.out.println("\nRecord does not exist");
				break;
			case 6:
				FAFile faFile = dbConnection.findFAFile(1);
				if (faFile == null)
				{
					System.out.println("\nThere is no record with ID = 1");
				}
				else
				{
					System.out.println("\n" + faFile.toString());
				}
				break;

			case 7:
				dbConnection.insertTestData();
				break;

			case 8:
				System.out.print("Enter a FAFile ID: ");
				int faFileID = scan.nextInt();				
				ArrayList<FileRecord> fileRecords = dbConnection.getFileRecords(faFileID);
				if (fileRecords == null)
				{
					System.out.println("\nThis FAFile does not exist or has no FileRecords");
				}
				else
				{					
					for ( FileRecord fileRecord : fileRecords)
					{
						System.out.println("\n" + fileRecord.toString());
					}				
				}
				break;

			case 9:
				FileRecord newFileRecord = createFileRecord(2);
				success = dbConnection.insertFileRecord(newFileRecord);
				if (success)
				{
					System.out.println("\nNew FileRecord successfully added.");
				}
				else
				{
					System.out.println("\nAssociated FAFile was not in the database.");
				}
				break;
				
			case 10:
				SummaryRecord newSummaryRecord = createSummaryRecord();
				dbConnection.insertSummaryRecord(newSummaryRecord);
				break;

			case 11:
				ArrayList<SummaryRecord> summaryRecords= new ArrayList<>();
				summaryRecords = dbConnection.getSummaryRecords();
				if (summaryRecords == null)
				{
					System.out.println("\nThere are no SummaryRecord records.");
				}
				else
				{
					for (SummaryRecord sr : summaryRecords)
					{
						System.out.println("\n" + sr.toString());
					}
				}
				break;

			case 12:
				ActionLog newActionLog = createActionLogRecord();
				dbConnection.insertActionLogItem(newActionLog);
				break;

			case 13:
				ArrayList<ActionLog> actionLogRecords= new ArrayList<>();
				actionLogRecords = dbConnection.getActionLogRecords();
				if (actionLogRecords == null)
				{
					System.out.println("\nThere are no ActionLog records.");
				}
				else
				{
					for (ActionLog sr : actionLogRecords)
					{
						System.out.println(sr.toString());
					}
				}
				break;

			case 14:
				dbConnection.clearActionLog();
				break;
				
			case 15:
				Memo memo = createMemo();
				success = dbConnection.insertMemo(memo, 1);
				if(success)
				{
					System.out.println("Memo Added");
				}
				else
				{
					System.out.println("No FAFile with ID=1 exists.");					
				}
				break;
				
			case 16:
				success = dbConnection.insertLink(2, 1);
				if(success)
				{
					System.out.println("Link added");
				}
				else
				{
					System.out.println("FAFile not found");
				}
				break;
				
			case 17:
				boolean currentActiveStatus = dbConnection.getActive(1);
				if (currentActiveStatus)
				{
					dbConnection.updateActive(1, false);
				}
				else
				{
					dbConnection.updateActive(1, true);
				}
				break;
				
			case 18:
				System.out.println("\nThe next available FAFile ID is: " + dbConnection.getNextFAFileID());
				break;

			case 19:
				success = dbConnection.updatePassword("user", "shazam");
				if(success)
				{
					System.out.println("Password updated to \"shazam\".");
				}
				else
				{
					System.out.println("You don't have the authority to do that.");
				}
				break;

			case 20:
				ArrayList<Login> allLogins = dbConnection.getAllLogins();
				for ( Login login : allLogins)
				{
					System.out.println(login.toString());
				}
				break;

			case 21:
				System.out.print("\nPlease enter the username of the account to unlock: ");
				String lockedAccountuserName = scan.next();
				success = dbConnection.unlockAccount(lockedAccountuserName);
				 if (success)
				 {
					 System.out.println("Account Unlocked");
				 }
				 else
				 {
					 System.out.println("You don't have the authority to unlock accounts.");
				 }
				break;

			case 22:
				System.out.print("\nPlease enter the ID of the FAFile of the memo: ");
				faFileID = scan.nextInt();
				System.out.print("\nPlease enter the Timestamp of the memo: ");
				String timeString = scan.nextLine();
				Timestamp dateTime = Timestamp.valueOf(timeString);
				
				success = dbConnection.deleteMemo(faFileID, dateTime);
				 if (success)
				 {
					 System.out.println("Memo deleted");
				 }
				 else
				 {
					 System.out.println("faFileID not found.");
				 }
				break;

			case 23:
				try
				{
					dbConnection.backUpDatabase();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case 24:
				System.out.print("Enter the name of the backup directory: ");
				String backupDirectory = scan.next();
				success = dbConnection.restoreDatase(backupDirectory);
				if (success)
				{
					System.out.println("\nDatabase Restored");
				    dbConnection.createConnection();
				}
				else
				{
					System.out.println("\nBackup Directory does not exist.");
				}
				break;

			case 25:
				exit = true;
				break;
			default:
				System.out.println("Invalid response; please try again.");
			}
		}
		scan.close();
		dbConnection.shutdown();

	}// end Main

	private static FAFile createFile()
	{
		FileRecord fileRecord = createFileRecord(4);
		FileHistory history = new FileHistory();
		history.addRecord(fileRecord);
		ArrayList<Integer> links = new ArrayList<>();
		links.add(2);
		links.add(3);
		Memo memo = new Memo("This is testfile # 4");
		ArrayList<Memo> memos = new ArrayList<>();
		memos.add(memo);
		Tickler tickler = createTickler(4);
		ArrayList<Tickler> ticklers = new ArrayList<>();
		ticklers.add(tickler);
		FAFile test4 = new FAFile(4, "test4", "docx", true, history, memos, links, ticklers);
		return test4;
	} // end createFile
		
	private static FileRecord createFileRecord(int faFileID)
	{
		GregorianCalendar date7 = new GregorianCalendar();
		Timestamp sqlDate7 = new Timestamp(date7.getTimeInMillis());
		FileRecord test7 = new FileRecord(faFileID, "c:\\testfiles\\", 1, sqlDate7);
		return test7;
	}
	
	private static SummaryRecord createSummaryRecord()
	{
		GregorianCalendar date = new GregorianCalendar();
		Timestamp sqlDate = new Timestamp(date.getTimeInMillis());
		SummaryRecord newSummaryRecord = new SummaryRecord(sqlDate, 5, 10, 1, 1, 2);
		return newSummaryRecord;
	}
	
	private static ActionLog createActionLogRecord()
	{
		Timestamp sqlDate = DBConnection.getDateTimeNow();
		ActionLog al = new ActionLog(sqlDate, ActionLogType.ADD_FAFILE, 3);
		return al;
	}
	
	private static Memo createMemo()
	{
		String text = "This is a memo for file # 1.";
		Memo memo = new Memo(text);
		return memo;
	}

	private static Tickler createTickler(int faFileID)
	{
		String text = "This is a tickler for file # 4.";
	    GregorianCalendar date = new GregorianCalendar(2018, 8, 20);
	    Timestamp sqlDate = new Timestamp(date.getTimeInMillis());
		Tickler tickler = new Tickler(faFileID, sqlDate, text);
		return tickler;
	}	
}
