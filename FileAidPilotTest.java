
////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAidPilot
//  File:     FileAidPilotTest.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////
import java.util.Scanner;
import java.util.GregorianCalendar;
import java.sql.Timestamp;
import java.sql.*;

public class FileAidPilotTest
{
	public static void main(String[] args)
	{
		// ## DEFINE VARIABLES SECTION ##
		// define the driver to use
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		// the database name
		String dbName = "jdbcFileAidPilotDB";
		// define the Derby connection URL to use
		String connectionURL = "jdbc:derby:" + dbName + ";create=true";
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
			ResultSet faFiles;
			String createString = "CREATE TABLE FILE  "
					+ "(fiID INT NOT NULL CONSTRAINT FILE_PK PRIMARY KEY, "
					+ " fiPATH VARCHAR(210), fiNAME VARCHAR(260), fiSIZE INT NOT NULL, "
					+ " fiEXTENSION VARCHAR(5) NOT NULL, fiACTIVE SMALLINT, "
					+ " fiMOD_DATE TIMESTAMP NOT NULL, fiMEMO VARCHAR(120)  ) ";

			// JDBC code sections
			// Beginning of Primary DB access section
			// ## BOOT DATABASE SECTION ##

			// ## INITIAL SQL SECTION ##
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
		    
		    
			File Test1 = new File(1, "test1", "c:\\testfiles\\", 1, "docx",
					sqlDate1);
			File Test2 = new File(2, "test2", "c:\\testfiles\\", 1, "xlsx",
					sqlDate2);
			File Test3 = new File(3, "test3", "c:\\testfiles\\", 1, "pptx",
					sqlDate3);

			System.out.println("File data to be inserted");
			System.out.println(Test1);
			System.out.println(Test2);
			System.out.println(Test3);

			int result = 0;

			String update = String.format(
					"INSERT INTO FILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					Test1.getID(), Test1.getPath(), Test1.getName(),
					Test1.getSize(), Test1.getExtension(),
					getActiveStatus(Test1.isActive()), Test1.getModDate(), Test1.getMemo());
			result = s.executeUpdate(update);

			update = String.format(
					"INSERT INTO FILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					Test2.getID(), Test2.getPath(), Test2.getName(),
					Test2.getSize(), Test2.getExtension(),
					getActiveStatus(Test2.isActive()), Test2.getModDate(), Test2.getMemo());
			result = s.executeUpdate(update);

			update = String.format(
					"INSERT INTO FILE (fiID, fiPATH, fiNAME, fiSIZE, fiEXTENSION, fiACTIVE, fiMOD_DATE, fiMEMO) "
							+ "VALUES(%d, '%s', '%s', %d, '%s', %d, '%s', '%s')",
					Test3.getID(), Test3.getPath(), Test3.getName(),
					Test3.getSize(), Test3.getExtension(),
					getActiveStatus(Test3.isActive()), Test3.getModDate(), Test3.getMemo());
			result = s.executeUpdate(update);

			faFiles = s.executeQuery("SELECT * FROM FILE");

			while (faFiles.next())
			{
				int id = faFiles.getInt(1);
				String path = faFiles.getString(2);
				String name = faFiles.getString(3);
				int size = faFiles.getInt(4);
				String extension = faFiles.getString(5);
				boolean active = getActiveStatus(faFiles.getInt(6));
				Timestamp modDate = faFiles.getTimestamp(7);
				String memo = faFiles.getString(8);
				System.out.println("\n" + id + " " + path + name + " " + extension + " "
						+ size + " " + active + " " + modDate + " " + memo);
				
				File printFile = new File(faFiles.getInt(1), faFiles.getString(3), faFiles.getString(2),
						faFiles.getInt(4), faFiles.getString(5),
						faFiles.getString(8), faFiles.getTimestamp(7));
				System.out.println("\n" + printFile.toString());
			}

			boolean validAnswer = false;
			int selectedID = 0;
			Scanner scan1 = new Scanner(System.in);
			while (validAnswer == false)
			{
				System.out.print("\nEnter the ID of the File record to which "
						+ "you want to add a memo (1,2,3 or 9 to skip): ");
				selectedID = scan1.nextInt();
				switch (selectedID)
				{
				case 1:
				{
					validAnswer = true;
					break;
				}
				case 2:
				{
					validAnswer = true;
					break;
				}
				case 3:
				{
					validAnswer = true;
					break;
				}
				case 9:
				{
					validAnswer = true;
					break;
				}
				} // end switch
			} // end while

			if (selectedID != 9)
			{
				Scanner scan2 = new Scanner(System.in);
				System.out
						.print("\nEnter a memo of less than 120 characters: ");
				String insertString;
				insertString = scan2.nextLine();
				result = s.executeUpdate("UPDATE FILE " + "SET fiMEMO = '"
						+ insertString + "' WHERE fiID = " + selectedID);
				scan2.close();
			}

			scan1.close();

			faFiles = s.executeQuery("SELECT * FROM FILE");
			System.out.println();

			while (faFiles.next())
			{
				File printFile = new File(faFiles.getInt(1), faFiles.getString(3), faFiles.getString(2),
						faFiles.getInt(4), faFiles.getString(5),
						faFiles.getString(8), faFiles.getTimestamp(7));
				System.out.println("\n" + printFile.toString());
			}

			// Release the resources (clean up )
			s.close();
			System.out.println("Closed connection");

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
				else
				{
					System.out.println("Database shut down normally");
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
		System.out.println("FileAid Pilot program ending.");
	}// end main
	
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
	}
	
	public static boolean getActiveStatus(int active)
	{
		if (active == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


}
