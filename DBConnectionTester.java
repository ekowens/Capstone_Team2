import java.sql.Timestamp;
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
					+ "\n1 \tShow Records" + "\n2 \tAdd a Record"
					+ "\n3 \tDelete Records"
					+ "\n4 \tExit" + "\nChoice: ");
			int response = scan.nextInt();
			switch (response)
			{
			case 1:
				dbConnection.selectRecords();
				break;
			case 2:
				FAFile newFile = createFile();
				dbConnection.insertFile(newFile);
				break;
			case 3:
				dbConnection.clearDB();
				break;
			case 4:
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

	
}
