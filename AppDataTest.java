////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 249 Fall 2015
//  Section:  (your section number)
// 
//  Project:  FileAid
//  File:     AppDataTest.java
//  
//  Name:     (your first and last name)
//  Email:    (your Wake Tech Email Address)
////////////////////////////////////////////////////////////////////////////////
package fileAid;

import java.io.FileNotFoundException;

/**
 * (Insert a comment that briefly describes the purpose of this class definition.)
 *
 * <p/> Bugs: (List any known issues or unimplemented features here)
 * 
 * @author (Insert your first and last name)
 *
 */
public class AppDataTest {

	/**
	 * (Insert a brief description that describes the purpose of this method) 
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		AppData wat = new AppData();
		
		System.out.println("start");
		
		try {
			wat.addFAFile("F:\\capstone\\testing files\\wat.txt");//change path as needed
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wat.shutdown();
		
	}

}
