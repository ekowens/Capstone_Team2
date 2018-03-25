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
	private Timestamp modDate;
	private FileHistory history = new FileHistory();
	private List<Memo> memos;
	private List<String> links;
	
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
			String newExtension, Timestamp newModDate, FileHistory newHistory){
		id = newID;
		name = newName;
		path = newPath;
		size = newSize;
		extension = newExtension;
		modDate = newModDate;
		history = newHistory;
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
			String newExtension, String newMemo, Timestamp newModDate,
			FileHistory newHistory){
		id = newID;
		name = newName;
		path = newPath;
		size = newSize;
		extension = newExtension;
		memo = newMemo;
		modDate = newModDate;
		history = newHistory;
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
	
	public void addMemo(String memoAdd) {
		Memo add = new Memo(memoAdd);
		memos.add(add);
	}
	
	//Added so the return to the driver/UI is cleaner.
	//Calls toString from Memo.
	//returns date on one line then
	//escapes to the next line where the Memo text is displayed
	public List<String> getMemosAsString(){
		List<String> returnList = new ArrayList<>();
		for(Memo m : memos) {
			returnList.add(m.toString());
		}
	}
	
	public List<Memo> getMemos(){
		return memos;
	}
	
	public void addLink(String id) {
		links.add(id);
	}
	
	public List<String> getLinks(){
		return links;
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

	//Needs 
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
		
		if (history != null)
		{
			for( FileRecord fileRecord : history)
			{
				fileString = fileString + "\n" + fileRecord.toString();
			}
		}
		return fileString;
	}

}
