////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAidPilot
//  File:     Memo.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////

/**
 * (Insert a comment that briefly describes the purpose of this class definition.)
 *
 * <p/> Bugs: (List any known issues or unimplemented features here)
 * 
 * @author (Insert your first and last name)
 *
 */
import java.sql.Timestamp;

public class Memo
{
	private Timestamp date;
	private String memoText;

	public Memo(String text)
	{
		date = new Timestamp(System.currentTimeMillis());
		memoText = text;
	}

	public String toString()
	{
		return date.toString() + "\n" + memoText;
	}

}
