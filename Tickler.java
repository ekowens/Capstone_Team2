////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 251 Spring 2017
//  Section:  0001
// 
//  Project:  FileAid
//  File:     Tickler.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////
package fileAid;

import java.sql.Timestamp;

/**
 * (Insert a comment that briefly describes the purpose of this class definition.)
 *
 * <p/> Bugs: (List any known issues or unimplemented features here)
 * 
 * @author (Insert your first and last name)
 *
 */
public class Tickler
{	
	private int faFileID;
	private Timestamp date;
	String action;
	boolean resolved = false;
	/**
	 * Constructs a new Tickler object.
	 * @param faFileID
	 * @param date
	 * @param action
	 * @param resolved
	 */
	public Tickler(int faFileID, Timestamp date, String action,
			boolean resolved)
	{
		this.faFileID = faFileID;
		this.date = date;
		this.action = action;
		this.resolved = resolved;
	}
	/**
	 * Constructs a new Tickler object. Use for new Tickler objects (Resolved = False)
	 * @param faFileID
	 * @param date
	 * @param action
	 * @param resolved
	 */
	public Tickler(int faFileID, Timestamp date, String action)
	{
		this.faFileID = faFileID;
		this.date = date;
		this.action = action;
	}
	/**
	 * @return the faFileID
	 */
	public int getFaFileID()
	{
		return faFileID;
	}
	/**
	 * @return the date
	 */
	public Timestamp getDate()
	{
		return date;
	}
	/**
	 * @return the action
	 */
	public String getAction()
	{
		return action;
	}
	/**
	 * @return the resolved
	 */
	public boolean isResolved()
	{
		return resolved;
	}
	
	// String describing tickler
	public String toString()
	{
		String resolveString = "Not Resolved";
		if(resolved)
		{
			resolveString = "Resolved";
		}
		String ticklerString = "\nDate: " + date +
				"\nAction: " + action +
				"\nResolution: " + resolveString;
				
		return ticklerString;
	}
	
	

}
