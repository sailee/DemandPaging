package demandPaging;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;


/**
 * This class simulates demand paging using FIFO, LRU and Random page replacement methods
 * @author Sailee Latkar
 */
public class Paging {

	private static int machineSize, pageSize, processSize, jobMix, referenceCount, debugLevel;
	private static PagingAlgorithm algorithm = null;
	private static LinkedList<Page> pageFrames;
	private static int countOfProcesses;
	private static ArrayList<Process> processes;

	/**
	 * @param args Should contain values for Machine Size, Page Size, Process Size, Job Mix, Count of references per process in the given sequence
	 * Can optionally contain the additional last parameter for Debugging level, with 0 indicating silent and 1 for more verbose output
	 */
	public static void main(String[] args) {
		PrintStream out = processInput(args);

		initializePager();

		startPager();

		System.setOut(out);

		displayOutput();
	}

	/**
	 * Scans and displays the input and initializes parameters to start the pager.
	 * @param args parameters for the pager
	 * @return PrintStream object, indicating the value that System.out should take, based on debugging level
	 */
	private static PrintStream processInput(String[] args) {
		PrintStream out=System.out;		

		if(args.length < 6)
		{
			System.out.println("Invalid Input.");
			return null;
		}		

		machineSize = Integer.parseInt(args[0]);		
		pageSize = Integer.parseInt(args[1]);		
		processSize = Integer.parseInt(args[2]);		
		jobMix = Integer.parseInt(args[3]);		
		referenceCount = Integer.parseInt(args[4]);		
		debugLevel = 0;

		for(PagingAlgorithm algo : EnumSet.allOf(PagingAlgorithm.class))
		{
			if(algo.name().equalsIgnoreCase(args[5]))
			{
				algorithm = algo;
				break;
			}
		}

		displayInputs();

		if(args.length == 7)
		{			
			debugLevel = Integer.parseInt(args[6]);

		}
		return out;
	}


	/**
	 * Initializes the processes and page frames array lists
	 */
	private static void initializePager() {
		int framecount = machineSize/pageSize;
		pageFrames = new LinkedList<Page>();

		for(int i=0;i<framecount;i++)
		{
			pageFrames.add(new Page());
		}

		processes = getProcessList(jobMix);
		countOfProcesses = processes.size();

		if(debugLevel == 0)
		{
			System.setOut(new PrintStream(new OutputStream(){
				public void write(int b) {}
			}));

		}
	}

	/**
	 * Simulates given page replacement algorithm, by generating three references per process at a time
	 */
	private static void startPager() {
		for(int clock=0; clock < countOfProcesses*referenceCount; )
		{
			for(int i=0;i<processes.size();i++)
			{
				Process p = processes.get(i);		

				for(int q = 0; q < 3; q++)
				{					
					if(p.hasPendingReferences())	//if process has not depleted its references
					{
						int w = p.getNextReference();	
						System.out.print("\n"+p.processID + " references word " + w + " (page " + w/pageSize +") at time " + (clock+1));

						int pageIndex = inMemory(w, p);		//verify if the page is in memory

						if(pageIndex == -1)			//page not in memory
						{							
							p.Fault();				//Page Fault
							pageIndex = algorithm.fetchPage(pageFrames, clock);		//Make the page resident							
							pageFrames.get(pageIndex).Assign(p, clock, w/pageSize);	//Assign the page to the given process
						}
						else
						{
							System.out.print(": Hit in frame " + (pageIndex+1) + "\n");	//Hit
							pageFrames.get(pageIndex).Hit(clock);	//Update the time the page was last used
						}

						p.generateNextReference();	//Generate next reference for the process
						clock++;
					}
					else
					{
						q=3;
					}				
				}
			}
		}
	}

	/**
	 * Displays the average page residence and count of faults per process
	 */
	private static void displayOutput()
	{
		int countOfFaults = 0, countOfEvictions = 0;
		double residence = 0;
		String str;

		System.out.println();

		for (Process  p : processes) {
			System.out.println(p);
			countOfFaults += p.getFaultCount();
			residence+=p.getTotalResidence();
			countOfEvictions += p.getCountofEvictions();
		}	

		if (PagingAlgorithm.getCountOfEvictions() == 0)
			str = ".\n\tWith no evictions, the overall average residence is undefined.";
		else
		{
			double avgResidence = residence/countOfEvictions;
			str = " and the overall average residency is " + avgResidence;
		}

		System.out.println("\nThe total number of faults is "+ countOfFaults + str);
	}

	/**
	 * Checks if the page containing given reference is in memory for the given process
	 * @param w	word referenced by the process
	 * @param p the process itself
	 * @return location of the page in the pageframe, if present. -1 otherwise.
	 */
	private static int inMemory(int w, Process p) {
		int pageID = w/pageSize;

		for(int i = 0; i < pageFrames.size(); i++)
		{
			if(pageFrames.get(i).Equals(pageID,p.processID))
				return i;
		}

		return -1;
	}

	/**
	 * Initialize the processes based on the given job mix
	 * @param jobMix given job mix
	 * @return array list of processes
	 */
	public static ArrayList<Process> getProcessList(int jobMix)
	{
		ArrayList<Process> processes = new ArrayList<Process>();
		int pageCount = processSize/pageSize;

		switch(jobMix)
		{
		case 1:	//fully sequential
			processes.add(new Process(1,0,0,1,pageCount,referenceCount,processSize));
			break;
		case 2:
			for(int i=0;i<4;i++)
			{
				processes.add(new Process(1,0,0,i+1,pageCount,referenceCount,processSize));
			}
			break;			
		case 3: //fully random
			for(int i=0;i<4;i++)
			{
				processes.add(new Process(0,0,0,i+1,pageCount,referenceCount,processSize));
			}
			break;
		case 4:
			processes.add(new Process(0.75,0.25,0,1,pageCount,referenceCount,processSize));
			processes.add(new Process(0.75,0,0.25,2,pageCount,referenceCount,processSize));
			processes.add(new Process(0.75,0.125,0.125,3,pageCount,referenceCount,processSize));
			processes.add(new Process(0.5,0.125,0.125,4,pageCount,referenceCount,processSize));
			break;
		}

		return processes;
	}

	/**
	 * This method displays the inputs received by command line arguments
	 */
	public static void displayInputs()
	{
		System.out.println("The machine size is "+machineSize);
		System.out.println("The page size is "+pageSize);
		System.out.println("The process size is "+processSize);
		System.out.println("The job mix number is "+jobMix);
		System.out.println("The number of references per process is "+referenceCount);
		System.out.println("The replacement algorithm is "+algorithm.name().toUpperCase());		
		System.out.println("The level of debugging output is: " + debugLevel);
	}
}
