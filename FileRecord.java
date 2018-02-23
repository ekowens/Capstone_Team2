///////////////////////////////////////////////////////////////////////////////
//  Course:   CSC289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     FileRecord.java
////////////////////////////////////////////////////////////////////////////////
public class FileRecord {
	
	
	private static final FileRecord String = null;
	private String name = "";
	private String path = "";
	private int size = 0;
	private String extension = "";
	
	
	public FileRecord( String Name, String newPath, int newSize, String newExtension){
		
		name = Name;
		path = newPath;
		size = newSize;
		extension = newExtension;
		
	}


	public boolean update(FileRecord file){
		//TODO add record when fileRecord is finished
		
		name = file.getName();
		path = file.getPath();
		size = file.getSize();
		extension = file.getExtension();
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
	    return name +"=="+path+"=="+size+"=="+ extension  ;
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
	
	
	}
		
