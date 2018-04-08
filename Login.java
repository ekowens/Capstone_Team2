////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     Login.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////
package fileAid;

import java.sql.Timestamp;

/**
 * Represents a user login
 * 
 * @author David Matthews
 *
 */
public class Login
{
	private int userID;
	private Timestamp date;
	private boolean success;
	
	public Login(int newUserID, Timestamp newDate, boolean newSuccess)
	{
		userID = newUserID;
		date = newDate;
		success = newSuccess;
	}

	/**
	 * @return the userID
	 */
	public int getUserID()
	{
		return userID;
	}

	/**
	 * @return the date
	 */
	public Timestamp getDate()
	{
		return date;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess()
	{
		return success;
	}
	
	public String toString()
	{
		return "UserID: " + userID + " Date: " + date + " Success: " + success;
	}

}
