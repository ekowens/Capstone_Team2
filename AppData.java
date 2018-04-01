package fileAid;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class AppData
{
	private DBConnection dbConnection;

	public AppData()
	{
		dbConnection = new DBConnection();
	}
	
	public void shutdown(){
		dbConnection.shutdown();
	}
	
	/*public ArrayList<FileRecord> getFAFileRecords()
	{
		dbConnection.createConnection();

		//ArrayList<FAFile> allFAFiles = dbConnection.getAllFAFileRecords();
		ArrayList<FileRecord> allFAFiles = dbConnection.getAllFAFileRecords();

		if (allFAFiles == null)
		{
			System.out.println("No FAFile records in database");
		}
		else
		{
			for (FileRecord faFile : allFAFiles)
			{
				System.out.println("\n" + faFile.toString());
			}
		}
		dbConnection.shutdown();
		return allFAFiles;		
	}*/

	public boolean deleteRecord(int delRecordID)
	{
		boolean success = dbConnection.deleteFAFile(delRecordID);
		if (success)
		{
			System.out.println("\nDeletion Successful");
			return true;
		}
		else
		{
			System.out.println("\nRecord does not exist");
			return false;
		}
	}

	public FileRecord findRecord(int faFileID)
	{
		ArrayList<FileRecord> fileRecords = dbConnection.getFileRecords(faFileID);
		FileRecord file=null;
		if (fileRecords == null)
		{
			System.out.println("\nThis FAFile does not exist or has no FileRecords");
		}
		else
		{					
			for ( FileRecord fileRecord : fileRecords)
			{
				System.out.println("\n" + fileRecord.toString());
				file = fileRecord;
			}				

		}
		return file;
	}
	
	//-------------------------------------------------------------------------------------------
	//added by coy
	
	public boolean addFAFile(String filePath) throws FileNotFoundException{
		
		int rand = ThreadLocalRandom.current().nextInt(1000, 1100 + 1);
		System.out.println(rand);
		File file = new File(filePath);
		if(file.exists() == false){
			return false;
		} else{
			String temp = filePath.substring(filePath.lastIndexOf("\\")+1);
			String name = temp.substring(0, temp.lastIndexOf("."));
			System.out.println(name);
			String ext = temp.substring(temp.lastIndexOf("."));
			System.out.println(ext);
			System.out.println(filePath);
			boolean feedback = dbConnection.insertFile(new FAFile(rand, name, ext, filePath));
			return feedback;
		}
		
	}
	
	public void removeFile(FAFile file){
		
		dbConnection.deleteFAFile(file.getID());
		
	}
	
	public boolean updateTracking(FAFile file){
		
		//TODO
		return false;
		
	}
	
	public int checkLogin(String user, String pass){
		//return 1 for pass
		//return 0 for fail
		//return 2 for old password
		
		switch(user){
		case "Admin":
			if(pass == "Ad123"){
				return 2;
			}
			break;
		case "User":
			if(pass == "Us123"){
				return 2;
			}
			break;
		case "Guest":
			if(pass == "Gu123"){
				return 2;
			}
			break;
		}
		
		return 0;
		
	}
	
	public  boolean editLogin(String user, String oldPass, String newPass){
		
		return false;
		
	}
	
}
