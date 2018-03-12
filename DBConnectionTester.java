
////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 251 Spring 2017
//  Section:  0001
// 
//  Project:  FileAidPilot
//  File:     DBConnectionTester.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class DBConnectionTester
{
	private static String dbURL = "jdbc:derby:C:/FileAid/jdbcFileAidPilotDB;create=true";
	private static String tableName = "FILE";
	// jdbc Connection
	private static Connection conn = null;
	private static Statement stmt = null;
	private static boolean dbExists = false;

	public static void main(String[] args)
	{
		createConnection();
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
					selectRecords();
					break;
				case 2:
					FAFile newFile = createFile();
					insertFile(newFile);
					break;
				case 3:
					DBConnection.clearDB(conn);
					break;
				case 4:
					exit = true;
					break;
				default:
					System.out.println("Invalid response; please try again.");
				}
			}
			scan.close();
	}

	private static void createConnection()
	{
		File directory = new File("c:/FileAid/jdbcFileAidPilotDB");
		if (!directory.exists())
		{
			System.out.println("FileAid has not been installed.");
			System.out.println("...Creating Database");
			DBConnection.createDB();
		}
		else
		{
			try
			{
				Class.forName("org.apache.derby.jdbc.EmbeddedDriver")
						.newInstance();
				// Get a connection
				conn = DriverManager.getConnection(dbURL);

				if (DBConnection.wwdChk4Table(conn))
				{
					dbExists = true;
					int numRows = DBConnection.getNumTableRows(conn, tableName);
					System.out.println("Table " + tableName + " Exists with "
							+ numRows + " rows.");
				} // end if

			}
			catch (Exception except)
			{
				except.printStackTrace();
			}
			if (!dbExists)
			{
				System.out.println("Database does not exist.");
				shutdown();
			}
		}// end else
	}

	private static FAFile createFile()
	{
		GregorianCalendar date1 = new GregorianCalendar(2018, 01, 19);
		Timestamp sqlDate1 = new Timestamp(date1.getTimeInMillis());
		FAFile test1 = new FAFile(4, "test4", "c:\\testfiles\\", 3, "docx",
				sqlDate1);
		return test1;
	} // end createFile

	private static void insertFile(FAFile file)
	{
		try
		{
			stmt = conn.createStatement();

			String update = String.format(
					"INSERT INTO FILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					file.getID(), file.getPath(), file.getName(),
					file.getSize(), file.getExtension(),
					DBConnection.getActiveStatus(file.isActive()),
					file.getModDate(), file.getMemo());
			stmt.execute(update);
			stmt.close();
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
		}
	}

	private static void selectRecords()
	{
		try
		{
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from " + tableName);
			int numRows = DBConnection.getNumTableRows(conn, tableName);

			System.out.println(
					"\n-------------------------------------------------");

			if (numRows == 0)
			{
				System.out
						.println("There are no records in " + tableName + ".");
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
				stmt.close();
			}
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
		}
	}

	private static void shutdown()
	{
		try
		{
			if (stmt != null)
			{
				stmt.close();
			}
			if (conn != null)
			{
				DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
			}
		}
		catch (SQLException sqlExcept)
		{

		}
		System.out.println("\n\nFileAid DB shut down succcessfully");

	} // end shutdown
}