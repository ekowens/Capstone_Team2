import java.io.File;
import java.io.FileNotFoundException;
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
					+ "\n1 \tShow Records" 
					+ "\n2 \tAdd a Record"
					+ "\n3 \tDelete All Records"
					+ "\n4 \tDelete a Record"
					+ "\n5 \tReturn Record with ID = 1"
					+ "\n6 \tModify a Record" 
					+ "\n7 \tExit" 
					+ "\nChoice: ");
			int response = scan.nextInt();
			switch (response)
			{
			case 1:
				ArrayList<FAFile> allFAFiles = dbConnection.selectAllRecords();
				for ( FAFile faFile : allFAFiles)
				{
					System.out.println("\n" + faFile.toString());
				}
				break;
			case 2:
				FAFile newFile = createFile();
				dbConnection.insertFile(newFile);
				break;
			case 3:
				dbConnection.clearDB();
				break;
			case 4:
				System.out.print("Enter a record ID to delete: ");
				int delRecordID = scan.nextInt();
				int result = dbConnection.deleteFAFile(delRecordID);
				if (result == 1)
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
				FAFile updatedFAFile = dbConnection.findFAFile(2);
				updatedFAFile = upDateFile(updatedFAFile);
				dbConnection.upDateFAFile(updatedFAFile);
				ArrayList<FAFile> allFAFiles2 = dbConnection.selectAllRecords();
				for ( FAFile faFile2 : allFAFiles2)
				{
					System.out.println("\n" + faFile2.toString());
				}				
				break;
				
			case 7:
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
	
	private static FAFile upDateFile(FAFile faFile)
	{
		faFile.setMemo("This is the file that was modified");
		return faFile;
	}
	


	
}
