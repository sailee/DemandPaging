package demandPaging;

/**
 * This class abstracts the Page 
 * @author Sailee Latkar
 *
 */
public class Page {
	private int arrivalTime, lastUsed, id;
	private Process p;	
	
	public Page()
	{
		id = 0;
		arrivalTime = 0;
		p=null;
		lastUsed = Integer.MAX_VALUE;		
	}
	
	/**
	 * Evicts the given page at given clock cycle, and frees the page frame to accomodate a new page
	 * @param clock
	 */
	public void Evict(int clock)
	{		
		p.Evict(id, clock-arrivalTime);
		id =0;		
		p=null;
	}
		
	/**
	 * indicates if the page is assigned to any process
	 * @return true if it is not, else false
	 */
	public boolean isFree()
	{
		return null==p;
	}
	
	/**
	 * Updates the lastUsed time of the page
	 * @param clock time at which the page was last used
	 */
	public void Hit(int clock)
	{
		lastUsed = clock;		
	}

	/**
	 * Gets the value of the time the page was last used
	 * @return the time at which the page was last used
	 */
	public int getLastUsed() {		
		return lastUsed;
	}

	/**
	 * Assigns the page to a given process
	 * @param p2 process which refers to this page
	 * @param clock time at which the page was assigned
	 * @param id Page number within the process
	 */
	public void Assign(Process p2, int clock, int id) {
		lastUsed = clock;
		arrivalTime = clock;
		p = p2;		
		this.id =id;
	}
	
	/**
	 * Checks if two pages are the same
	 * @param pageID the page number within the process
	 * @param processID the ID of the process in which the page belongs
	 * @return true, if the two pages are equal, else false.
	 */
	public boolean Equals(int pageID, int processID)
	{
		if (id == pageID)
			if(p==null)
				return false;
			else
				return (p.processID == processID);
		return false;
	}
	
	/**
	 * Returns the page number within the process
	 * @return page number within the process
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * returns the identifier for the process
	 * @return returns the identifier for the process
	 */
	public int getProcessID()
	{
		return p.processID;
	}
	
	public String toString()
	{
		return "Process: " + p.processID + "\tPage: " + id + "\tLast Used: " + lastUsed;
	}
}
