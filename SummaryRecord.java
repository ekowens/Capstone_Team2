import java.sql.Timestamp;

////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     SummaryRecord.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////

/**
 * Tracks and records statistics for a FileAid Session
 * 
 * @author David Matthews
 *
 */
public class SummaryRecord
{
	private Timestamp dateTimeCheck;
	private int numFiles;
	private int diskSpace;
	private int numFilesAdded;
	private int numFilesDeleted;
	private int numFilesModified;
	
	public SummaryRecord()
	{}
	
	public SummaryRecord(Timestamp newDateTimeCheck, int newNumFiles, int newDiskSpace,
			int newNumFilesAdded, int newNumFilesDeleted, int newNumFilesModified)
	{
		dateTimeCheck = newDateTimeCheck;
		numFiles = newNumFiles;
		diskSpace = newDiskSpace;
		numFilesAdded = newNumFilesAdded;
		numFilesDeleted = newNumFilesDeleted;
		numFilesModified = newNumFilesModified;
	}

	/**
	 * @return the dateTimeCheck
	 */
	public Timestamp getDateTimeCheck()
	{
		return dateTimeCheck;
	}

	/**
	 * @return the numFiles
	 */
	public int getNumFiles()
	{
		return numFiles;
	}

	/**
	 * @return the diskSpace
	 */
	public int getDiskSpace()
	{
		return diskSpace;
	}

	/**
	 * @return the numFilesAdded
	 */
	public int getNumFilesAdded()
	{
		return numFilesAdded;
	}

	/**
	 * @return the numFilesDeleted
	 */
	public int getNumFilesDeleted()
	{
		return numFilesDeleted;
	}

	/**
	 * @return the numFilesModified
	 */
	public int getNumFilesModified()
	{
		return numFilesModified;
	}
	
	/**
	 * toString method
	 */
	public String toString()
	{
		String summaryRecord = "Date: " + dateTimeCheck 
				+ "\nNumber of Files: " + numFiles
				+ "\nDiskSpace: " + diskSpace
				+ "\nFiles Added: " + numFilesAdded
				+ "\nFiles Deleted: " + numFilesDeleted
				+ "\nFiles Modified: " + numFilesModified;
		
		return summaryRecord;
	}
}
