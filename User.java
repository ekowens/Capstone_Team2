////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 251 Spring 2017
//  Section:  0001
// 
//  Project:  FileAid
//  File:     User.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////
package fileAid;

import java.util.ArrayList;

/**
 * (Insert a comment that briefly describes the purpose of this class definition.)
 *
 * <p/> Bugs: (List any known issues or unimplemented features here)
 * 
 * @author (Insert your first and last name)
 *
 */
public class User
{
	private int userID;
	private String userName;
	private ArrayList<Login> logins;
	/**
	 * Constructs a new User object. (Insert any further description that is needed)
	 * @param userID
	 * @param userName
	 * @param logins
	 */
	public User(int userID, String userName, ArrayList<Login> logins)
	{
		this.userID = userID;
		this.userName = userName;
		this.logins = logins;
	}
	/**
	 * @return the userID
	 */
	public int getUserID()
	{
		return userID;
	}
	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}
	/**
	 * @return the logins
	 */
	public ArrayList<Login> getLogins()
	{
		return logins;
	}
	
	public String toString()
	{
		String user = userName;
		for ( Login login : logins)
		{
			user = user + "\n" + login.toString();
		}
		return user;
	}

}
