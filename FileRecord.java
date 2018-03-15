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
	private int id = 0;
	private int faFileID = 0;
	private String name = "";
	private String path = "";
	private int size = 0;
	private String extension = "";
	private Timestamp modDate;
	
	
	public FileRecord(int newID, int newfaFileID, String Name, String newPath, int newSize, String newExtension, Timestamp newModDate){
		
		id = newID;
		faFileID = newfaFileID;
		name = Name;
		path = newPath;
		size = newSize;
		extension = newExtension;
		modDate = newModDate;
		
	}


	public boolean update(FileRecord file){
		//TODO add record when fileRecord is finished
		
		name = file.getName();
		path = file.getPath();
		size = file.getSize();
		extension = file.getExtension();
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
	    return name +"=="+path+"=="+size+"=="+ extension + "==" + modDate;
	}
	
	public int getID(){
		return id;
	}
		
	public int getFAFileID(){
		return faFileID;
	}
		
	public String getName(){
		return name;
	}
	
	public String getPath(){
		return path;
	}
	
	public int getSize(){
		return size;
	}
	
	public String getExtension(){
		return extension;
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
		
