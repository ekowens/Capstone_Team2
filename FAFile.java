package fileAid;

import java.util.ArrayList;
import java.util.List;
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
	private String extension = "";
	private String filePath = "";
	private Boolean active = true;
	private FileHistory history = new FileHistory();
	private List<Memo> memos;
	private List<Integer> links;
	private List<Tickler> ticklers;
	
	public FAFile(){}
	
	// **Constructor to be used only for inserting experimental data - remove before finalizing program
	public FAFile(int newID, String newName, String newExtension)
	{
		id = newID;
		name = newName;
		extension = newExtension;
		history = new FileHistory();
		memos = new ArrayList<>();
		links = new ArrayList<>();
	}
	
	public FAFile(int newID, String newName, String newExtension, String newFilePath)
	{
		id = newID;
		name = newName;
		extension = newExtension;
		filePath = newFilePath;
		history = new FileHistory();
		memos = new ArrayList<>();
		links = new ArrayList<>();
	}
	
	public FAFile(int newID, String newName, String newExtension, String newFilePath, FileHistory newHistory)
	{
		id = newID;
		name = newName;
		extension = newExtension;
		filePath = newFilePath;
		history = newHistory;
		memos = new ArrayList<>();
		links = new ArrayList<>();
	}
				
	// Constructor
	public FAFile(int newID, String newName, String newExtension, String newFilePath, 
			Boolean newActive, FileHistory newHistory, 
			ArrayList<Memo> newMemos, ArrayList<Integer> newLinks)
	{
		id = newID;
		name = newName;
		extension = newExtension;
		filePath = newFilePath;
		active = newActive;
		history = newHistory;
		memos = newMemos;
		links = newLinks;
	}
	
	// Constructor to be used for moving DB data into FAFile class objects
	// **Please don't modify this constructor without talking with David
	public FAFile(int newID, String newName, String newExtension, 
			Boolean newActive, FileHistory newHistory, 
			ArrayList<Memo> newMemos, ArrayList<Integer> newLinks, 
			ArrayList<Tickler> newTicklers)
	{
		id = newID;
		name = newName;
		extension = newExtension;
		active = newActive;
		history = newHistory;
		memos = newMemos;
		links = newLinks;
		ticklers = newTicklers;
	}
	
	public boolean update(FAFile file){
		//TODO add record when fileRecord is finished
		id = file.getID();
		name = file.getName();
		extension = file.getExtension();
		filePath = file.getPath();
		active = file.isActive();
		history = file.getHistory();
		memos = file.getMemos();
		links = file.getLinks();
		ticklers = file.getTicklers();
		return true;
	}
		
	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	
	public String getExtension(){
		return extension;
	}
	
	public String getPath(){
		return filePath;
	}
	
	public Boolean isActive(){
		return active;
	}
	
	
	//TODO add a method that can return the history in a usable/printable format
	
	public FileHistory getHistory(){
		return history;
	}
	
	//Turns the current file items into a FileRecord and adds to history.
	//This should probably only be access internally when a file is updated
	/*public void addHistory(){
		FileRecord toAdd = new FileRecord(id, name, path, size, extension, modDate);
		history.add(toAdd);
	}*/

	public void addToHistory(FileRecord fileRecord)
	{
	history.addRecord(fileRecord);
	}

	
	public void addMemo(String memoAdd) {
		Memo add = new Memo(memoAdd);
		memos.add(add);
	}
	
	//Added so the return to the driver/UI is cleaner.
	//Calls toString from Memo.
	//returns date on one line then
	//escapes to the next line where the Memo text is displayed
	//If memos is null, adds one string to the list that says "No Memos"
	public List<String> getMemosAsString(){
		List<String> returnList = new ArrayList<>();
		if(memos.equals(null)) {
			returnList.add("No Memos");
		}
		else {
			for(Memo m : memos) {
				returnList.add(m.toString());
			}
		}
		
		return returnList;
	}
	
	public List<Memo> getMemos(){
		return memos;
	}
	
	public void addLink(Integer link) {
		links.add(link);
	}
	
	public List<Integer> getLinks(){
		return links;
	}

	public void addTickler(Tickler tickler) {
		ticklers.add(tickler);
	}
	
	public List<Tickler> getTicklers(){
		return ticklers;
	}

	public Timestamp getCurrentModDate()
	{
		Timestamp currentModDate = history.getRecord(0).getModDate();
		return currentModDate;
	}
	
	public int compareTo(FAFile faFile)
	{
		return this.name.compareTo(faFile.name);
	}
	//Needs 
	public String toString()
	{
		String status = "Active";
		if (!active)
			{
			 status = "Inactive"; 
			}
		String fileString = "ID: " + id + " " + name + "." + extension + " "+ status;
		
		if (history != null)
		{
			fileString = fileString + "\nFile History";
			for( FileRecord fileRecord : history)
			{
				fileString = fileString + "\n" + fileRecord.toString();
			}
		}
		
		if (memos != null)
		{
			fileString = fileString + "\nMemos:";
			for( Memo memo : memos)
			{
				fileString = fileString + "\n" + memo.toString();
			}
		}

		if (links != null)
		{
			fileString = fileString + "\nLinks:";
			for( Integer link : links)
			{
				fileString = fileString + "\n" + link.toString();
			}
		}
		
		if (ticklers != null)
		{
			fileString = fileString + "\nTicklers:";
			for( Tickler tickler : ticklers)
			{
				fileString = fileString + "\n" + tickler.toString();
			}
		}

		
		return fileString;
	}
}
