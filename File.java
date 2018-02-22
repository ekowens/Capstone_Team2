////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     File.java
////////////////////////////////////////////////////////////////////////////////
public class File {
	
	private int id = 0;
	private String name = "";
	private String path = "";
	private int size = 0;
	private String extension = "";
	private Boolean active = false;
	private String memo = "";
	private FileHistory history = new FileHistory();
	
	public File(){}
	
	public File(int newID, String newName, String newPath, int newSize, String newExtension, Boolean newActive){
		id = newID;
		name = newName;
		path = newPath;
		size = newSize;
		extension = newExtension;
		active = newActive;
	}
	
	public File(int newID, String newName, String newPath, int newSize, String newExtension, Boolean newActive, String newMemo){
		id = newID;
		name = newName;
		path = newPath;
		size = newSize;
		extension = newExtension;
		active = newActive;
		memo = newMemo;
	}
	
	public File(int newID, String newName, String newPath, int newSize, String newExtension, Boolean newActive, FileHistory newHistory){
		id = newID;
		name = newName;
		path = newPath;
		size = newSize;
		extension = newExtension;
		active = newActive;
		history = newHistory;
	}
	
	public File(int newID, String newName, String newPath, int newSize, String newExtension, Boolean newActive, String newMemo, FileHistory newHistory){
		id = newID;
		name = newName;
		path = newPath;
		size = newSize;
		extension = newExtension;
		active = newActive;
		memo = newMemo;
		history = newHistory;
	}
	
	public boolean update(File file){
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
	
	public FileHistory getFileHistory(){
		if(history.isEmpty()){
			return null;
		} else{
			return history;
		}
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
	
	public String getMemo(){
		return memo;
	}

}
