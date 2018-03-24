
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
					+ "\n6 \tModify a Record" 
					+ "\n7 \tInsert Test Data  (Don't do this twice without clearing the DB)"
					+ "\n8 \tReturn FileRecords for a particular FAFile"
					+ "\n9 \tAdd a FileRecord to FAFile ID = 3"
					+ "\n10 \tAdd a SummaryRecord"
					+ "\n11 \tShow all SummaryRecord Records" 
					+ "\n12 \tBack up FileAid to c:/FileAidBackups/yyyy-MM-dd/" 
					+ "\n13 \tRestore a backup" 
					+ "\n14 \tExit" 
					+ "\nChoice: ");
			int response = scan.nextInt();
			switch (response)
			{
			case 1:
				ArrayList<FAFile> allFAFiles = dbConnection.selectAllRecords();
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
				FAFile oldFAFile = dbConnection.findFAFile(2);
				FAFile modifiedFAFile = modifyFile(oldFAFile);
				success = dbConnection.upDateFAFile(modifiedFAFile);
				if (success)
				{
					ArrayList<FAFile> allFAFiles2 = dbConnection
							.selectAllRecords();
					for (FAFile faFile2 : allFAFiles2)
					{
						System.out.println("\n" + faFile2.toString());
					}
				} // end if
				else
				{
					System.out.println("FAFile not found");
				}
				break;
				
			case 7:
				dbConnection.insertTestData();
				break;

			case 8:
				System.out.print("Enter a FAFile ID: ");
				int faFileID = scan.nextInt();				
				ArrayList<FileRecord> fileRecords = dbConnection.selectFileRecords(faFileID);
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
				FileRecord newFileRecord = createFileRecord();
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

			case 13:
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

			case 14:
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
		GregorianCalendar date1 = new GregorianCalendar(2018, 01, 19);
		Timestamp sqlDate1 = new Timestamp(date1.getTimeInMillis());
		FAFile test1 = new FAFile(4, "test4", "c:\\testfiles\\", 3, "docx",
				sqlDate1);
		return test1;
	} // end createFile
	
	private static FAFile modifyFile(FAFile faFile)
	{
		faFile.setMemo("This is the file that was modified");
		return faFile;
	}
	
	private static FileRecord createFileRecord()
	{
		GregorianCalendar date7 = new GregorianCalendar(2018, 00, 20);
		Timestamp sqlDate7 = new Timestamp(date7.getTimeInMillis());
		FileRecord test7 = new FileRecord(3, "test3", "c:\\testfiles\\", 1,
				"docx", sqlDate7);
		return test7;
	}
	
	private static SummaryRecord createSummaryRecord()
	{
		GregorianCalendar date = new GregorianCalendar();
		Timestamp sqlDate = new Timestamp(date.getTimeInMillis());
		SummaryRecord newSummaryRecord = new SummaryRecord(sqlDate, 5, 10, 1, 1, 2);
		return newSummaryRecord;
	}


	
}
