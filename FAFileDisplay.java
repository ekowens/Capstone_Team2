package fileAid;
import java.sql.Timestamp;
import java.util.List;

////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAid
//  File:     FAFileDisplay.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////

/**
 * Joins FAFile information to the most recent FileRecord information 
 * to create an easily displayed and searched record
 * 
 * @author (Insert your first and last name)
 *
 */
public class FAFileDisplay implements Comparable<FAFileDisplay>
{
	// FAFile Instance Variables
	private int id = 0;
	private String name = "";
	private String extension = "";
	private Boolean active = true;
	private List<Memo> memos;
	private List<Integer> links;
	private List<Tickler> ticklers;
	// FileRecord Instance Variables
	private String path = "";
	private int size = 0;
	private Timestamp modDate;
	
	public FAFileDisplay()
	{}

	/**
	 * Constructs a new FAFileDisplay object. (Insert any further description that is needed)
	 * @param id
	 * @param name
	 * @param extension
	 * @param active
	 * @param memos
	 * @param links
	 * @param ticklers
	 * @param path
	 * @param size
	 * @param modDate
	 */
	public FAFileDisplay(int id, String name, String extension, Boolean active,
			List<Memo> memos, List<Integer> links, List<Tickler>ticklers,
			String path, int size, Timestamp modDate)
	{
		this.id = id;
		this.name = name;
		this.extension = extension;
		this.active = active;
		this.memos = memos;
		this.links = links;
		this.ticklers = ticklers;
		this.path = path;
		this.size = size;
		this.modDate = modDate;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the extension
	 */
	public String getExtension()
	{
		return extension;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive()
	{
		return active;
	}

	/**
	 * @return the memos
	 */
	public List<Memo> getMemos()
	{
		return memos;
	}

	/**
	 * @return the links
	 */
	public List<Integer> getLinks()
	{
		return links;
	}

	/**
	 * @return the ticklers
	 */
	public List<Tickler> getTicklers()
	{
		return ticklers;
	}

	/**
	 * @return the path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * @return the size
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * @return the modDate
	 */
	public Timestamp getModDate()
	{
		return modDate;
	}
	
	public int compareTo(FAFileDisplay faFileDisplay)
	{
		return this.name.compareTo(faFileDisplay.getName());
	}
	
	public String toString()
	{
		String displayFileString = id + "==" + path + name + "." + extension +
				"==" + modDate + "==" + size + "==" + active;
		return displayFileString;
	}

}
