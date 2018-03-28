///////////////////////////////////////////////////////////////////////////////
//  Course:   CSC289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     FileRecord.java
////////////////////////////////////////////////////////////////////////////////
import java.sql.*;
public class FileRecord implements Comparable<FileRecord> {
	
	
	private static final FileRecord String = null;
	private int faFileID = 0;
	private String path = "";
	private int size = 0;
	private Timestamp modDate;
	
	
	public FileRecord(int newfaFileID, String newPath, int newSize, Timestamp newModDate)
	{	
		faFileID = newfaFileID;
		path = newPath;
		size = newSize;
		modDate = newModDate;		
	}


	public boolean update(FileRecord file)
	{
		//TODO add record when fileRecord is finished
				path = file.getPath();
		size = file.getSize();
		modDate = file.getModDate();
		return true;
	}
	public FileRecord getRecord(){
		if(String.isEmpty()){
			return null;
		} else
		{
			return String;
		}
	}
		
	private boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
		
	public String toString(){
	    return faFileID +"=="+path+"=="+size+"==" + modDate;
	}
			
	public int getFAFileID(){
		return faFileID;
	}
		
	public String getPath(){
		return path;
	}
	
	public int getSize(){
		return size;
	}
	
	public Timestamp getModDate(){
		return modDate;
	}
	
	public int compareTo(FileRecord fileRecord)
	{
		if (this.modDate.compareTo(fileRecord.modDate) >= 0)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}

	}
		
