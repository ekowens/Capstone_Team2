import java.text.DateFormat;
import java.util.Date;
import java.sql.*;

////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     File.java
////////////////////////////////////////////////////////////////////////////////
public class FAFile implements Comparable<FAFile> {
	
	private int id = 0;
	private String name = "";
	private String path = "";
	private int size = 0;
	private String extension = "";
	private Boolean active = true;
	private String memo = "";
	private Timestamp modDate;
	private FileHistory history = new FileHistory();
	
	public FAFile(){}
	
	public FAFile(int newID, String newName, String newPath, int newSize, 
			String newExtension, Timestamp newModDate){
		id = newID;
		name = newName;
		path = newPath;
		size = newSize;
		extension = newExtension;
		modDate = newModDate;
	}
	
	public FAFile(int newID, String newName, String newPath, int newSize, 
			String newExtension, String newMemo, Timestamp newModDate){
		id = newID;
		name = newName;
		path = newPath;
		size = newSize;
		extension = newExtension;
		memo = newMemo;
		modDate = newModDate;
	}
	
	
	public FAFile(int newID, String newName, String newPath, int newSize, 
			String newExtension, Boolean newActive, String newMemo, Timestamp newModDate ){
		id = newID;
		name = newName;
		path = newPath;
		size = newSize;
		extension = newExtension;
		active = newActive;
		memo = newMemo;
		modDate = newModDate;
	}
	
	public boolean update(FAFile file){
		//TODO add record when fileRecord is finished
		id = file.getID();
		name = file.getName();
		path = file.getPath();
		size = file.getSize();
		extension = file.getExtension();
		active = file.isActive();
		memo = file.getMemo();
		return true;
	}
		
	public int getID(){
		return id;
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
	
	public Boolean isActive(){
		return active;
	}
	
	public Timestamp getModDate()
	{
		return modDate;
	}
	
	public String getMemo(){
		return memo;
	}
	
	public int compareTo(FAFile faFile)
	{
		if (this.modDate.compareTo(faFile.modDate) >= 0)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}


	public String toString()
	{
		String status = "Active";
		if (!active)
			{
			 status = "Inactive"; 
			}
		String fileString = "ID: " + id + " " + path + name + "." + extension + " Size: " + size + " " + status
				+ " \nMod Date: " + modDate ;
		
		if (memo != "")
		{
			fileString = fileString + "\nMemo: " + memo;
		}
		return fileString;
	}

}
