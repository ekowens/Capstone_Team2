
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
//  File:     DBConnectionTester2.java
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
		DBConnection dbConnection = new DBConnection();
		dbConnection.createConnection();
		
		System.out.println("\nWelcome to FileAid");

		Scanner scan = new Scanner(System.in);
		boolean exit = false;
		while (!exit)
		{
			System.out.print("\nChoose from the following options: "
					+ "\n1 \tShow all FAFile Records" 
					+ "\n2 \tAdd a Record"
					+ "\n3 \tDelete All Records"
					+ "\n4 \tDelete a Record"
					+ "\n5 \tReturn Record with ID = 1"
					+ "\n6 \tInsert Test Data  (Don't do this twice without clearing the DB)"
					+ "\n7 \tReturn FileRecords for a particular FAFile"
					+ "\n8 \tAdd a FileRecord to FAFile ID = 2"
					+ "\n9 \tAdd a SummaryRecord"
					+ "\n10 \tShow all SummaryRecord Records" 
					+ "\n11 \tAdd an ActionLog Record" 
					+ "\n12 \tShow all ActionLog Records" 
					+ "\n13 \tClear all ActionLog Records" 
					+ "\n14 \tAdd a Memo to FAFile 1" 
					+ "\n15 \tBack up FileAid to c:/FileAidBackups/yyyy-MM-dd/" 
					+ "\n16 \tRestore a backup" 
					+ "\n17 \tExit" 
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
				FAFile newFile = createFile();
				boolean success = dbConnection.insertFile(newFile);
				if (success)
				{
					System.out.println("\nNew FAFile successfully added.");
				}
				else
				{
					System.out.println("\nNew FAFile was already in the database.");
				}
				break;
			case 3:
				dbConnection.clearDB();
				break;
			case 4:
				System.out.print("Enter a record ID to delete: ");
				int delRecordID = scan.nextInt();
				success = dbConnection.deleteFAFile(delRecordID);
				if (success)
				{
					System.out.println("\nDeletion Successful");
				}
				else System.out.println("\nRecord does not exist");
				break;
			case 5:
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

			case 6:
				dbConnection.insertTestData();
				break;

			case 7:
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

			case 8:
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
				
			case 9:
				SummaryRecord newSummaryRecord = createSummaryRecord();
				dbConnection.insertSummaryRecord(newSummaryRecord);
				break;

			case 10:
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

			case 11:
				ActionLog newActionLog = createActionLogRecord();
				dbConnection.insertActionLogItem(newActionLog);
				break;

			case 12:
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

			case 13:
				dbConnection.clearActionLog();
				break;
				
			case 14:
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
				

			case 15:
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

			case 16:
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

			case 17:
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
		FAFile test1 = new FAFile(4, "test4", "docx");
		test1.addToHistory(createFileRecord(4));
		return test1;
	} // end createFile
	
	/*private static FAFile modifyFile(FAFile faFile)
	{
		faFile.setMemo("This is the file that was modified");
		return faFile;
	}*/
	
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
		GregorianCalendar date = new GregorianCalendar();
		Timestamp sqlDate = new Timestamp(date.getTimeInMillis());
		ActionLog al = new ActionLog(sqlDate, ActionLogType.ADD_FAFILE, 3);
		return al;
	}
	
	private static Memo createMemo()
	{
		String text = "This is a memo for file # 1.";
		Memo memo = new Memo(text);
		return memo;
	}


	
}
