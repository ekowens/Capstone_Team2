////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     ActionLogType.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////

/**
 * Enum class for defining ActionLog types.
 * 
 * @author David Matthews
 *
 */
public enum ActionLogType
{
	ADD_MEMO,
	DELETE_MEMO,
	MODIFY_MEMO,
	ADD_LINKAGE,
	DELETE_LINKAGE,
	MODIFY_LINKAGE,
	ADD_FAFILE,
	DELETE_FAFILE,
	MODIFY_FAFILE,
	RUN_TIME_ERROR,
	PERIODIC_FILE_SCAN,
	MANUAL_FILE_SCAN;
}
