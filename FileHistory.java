import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     FileHistory.java
////////////////////////////////////////////////////////////////////////////////
public class FileHistory extends ArrayList<FileRecord> {
	
	private int updateCount = 0;
	
	public FileHistory(){}
		
	public FileRecord getRecord(int x){
		return this.get(x);
	}
	
	public Boolean addRecord(FileRecord newRecord){
		this.add(newRecord);
		updateCount = this.size();
		return true;
	}

	/**
	 * @return the updateCount
	 */
	public int getUpdateCount()
	{
		return updateCount;
	}

}
