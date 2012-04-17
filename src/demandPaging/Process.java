package demandPaging;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Abstracts a Process/Job
 * @author Sailee Latkar
 *
 */
public class Process {
	private double A,B,C;
	int processID, referenceCount, currentReference, w, processSize, faultCount;
	private static FileReader fread = null; 
	private static Scanner scan = null;
	private double evictionCount ;
	int[] residence, evictions; 

	public Process(double a, double b, double c, int id, int pgCount, int refCount, int size)
	{
		A=a;
		B=b;
		C=c;
		processID = id;		
		referenceCount = refCount;
		currentReference = 0;
		w = 111*id % size;
		processSize = size;
		faultCount = 0;		
		evictionCount = 0;
		
		residence = new int[pgCount];
		evictions = new int[pgCount];
		
		for(int i = 0; i < pgCount; i++)
		{
			residence[i] = -1;
			evictions[i] = 0;
		}
		
		try
		{
			if(scan == null)
			{
				fread = new FileReader("RandomNumbers");
				scan = new Scanner(fread);
			}
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("Could not locate the random numbers file. Please Place file with name 'RandomNumbers' in the src folder");
			System.exit(1);
		}
	}	

	/**
	 * Returns the next reference for the process
	 * @return next reference for the process
	 */
	public int getNextReference()
	{		
		currentReference++;
		return w;
	}
	
	/**
	 * Increments the fault count
	 */
	public void Fault()
	{
		faultCount++;
	}
	
	/**
	 * Returns the fault count
	 * @return returns the fault count
	 */
	public int getFaultCount()
	{
		return faultCount;
	}

	/**
	 * Generates the next reference
	 */
	public void generateNextReference()
	{		
		Double y = (double) getRandom();
		System.out.print(processID + " uses random number: " + y.intValue());
		y=y/(Integer.MAX_VALUE + 1D);

		if(y < A)
		{
			w = (w+1)%processSize;
		}
		else if(y < (A+B))
		{
			w = (w-5+processSize)%processSize;
		}
		else if(y < (A+B+C))
		{
			w = (w+4)%processSize;
		}
		else
		{
			w = getRandom();
			System.out.println(processID + " uses random number: "  + w);
			w=w%processSize;
		}	
	}
	
	public String toString()
	{
		String str = "Process " + processID+ " had "+ faultCount + " faults";
		if(evictionCount == 0)
		{
			str = str + ".\n\tWith no evictions, the average residence is undefined.";
		}
		else
			str = str + " and " + getAveragePageResidency() +" average residency.";		
		return str;
	}
	
	/**
	 * Returns true if the process has not exhausted its number of references, else false
	 * @return returns true if the process has not exhausted its number of references, else false
	 */
	public boolean hasPendingReferences()
	{
		return referenceCount > currentReference;
	}
	
	/**
	 * get a random number from file
	 * @return random number 
	 */
	static int getRandom()
	{	
		return scan.nextInt();
	}

	/**
	 * Adds up the residence time of the given page, on eviction & increments evictionCount
	 * @param pageID page evicted
	 * @param residence residence time
	 */
	public void Evict(int pageID, int residence) {
		evictions[pageID] += 1;
		
		if(this.residence[pageID] > 0)
			this.residence[pageID] += residence;
		else
			this.residence[pageID] = residence;
		
		evictionCount++;
	}
	
	/**
	 * Returns average page residency for the given process
	 * @return
	 */
	private double getAveragePageResidency()
	{
		double totalResidence = 0D, totalEvictions = 0D;
		for(int i=0; i< evictions.length; i++)
		{
			if(evictions[i] > 0 && residence[i] > 0)
			{
				totalResidence += residence[i];
				totalEvictions += evictions[i];
			}
		}
		
		return totalResidence/totalEvictions;
	}

	/**
	 * returns the count of evictions for this process
	 * @return returns the count of evictions for this process
	 */
	public int getCountofEvictions() {
		return (int) evictionCount;
	}
	
	/**
	 * returns the total residence for this process
	 * @return returns the total residence for this process
	 */
	public int getTotalResidence()
	{
		int count = 0;
		for (int i=0;i<residence.length;i++)
			if(residence[i] > 0)
				count+= residence[i];
		
		return count;
	}

}
