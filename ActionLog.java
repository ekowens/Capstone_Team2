import java.sql.Timestamp;

////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     ActionLog.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////

/**
 * Logs major actions in FileAid
 * 
 * @author David Matthews
 *
 */
public class ActionLog
{
	private Timestamp date;
	private ActionLogType type;
	private int userID;
	
	public ActionLog()
	{}
	
	public ActionLog(Timestamp newDate, ActionLogType newType, int newUserID)
	{
		date = newDate;
		type = newType;
		userID = newUserID;
	}
	/**
	 * @return the date
	 */
	public Timestamp getDate()
	{
		return date;
	}

	/**
	 * @return the type
	 */
	public ActionLogType getType()
	{
		return type;
	}

	/**
	 * @return the user
	 */
	public int getUser()
	{
		return userID;
	}
	
	public String toString()
	{
		String actionLogString = "\nDate: " + date.toString()
				+ " Type: " + type.toString()
				+ " User: " + userID;
				return actionLogString;
	}


}
