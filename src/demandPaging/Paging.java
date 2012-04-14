package demandPaging;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.EnumSet;


public class Paging {
	
	private static int machineSize, pageSize, processSize, jobMix, referenceCount;
	private static PagingAlgorithm algorithm = null;


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PrintStream out=null;
		
		if(args.length < 6)
		{
			System.out.println("Invalid Input.");
			return;
		}		
		
		machineSize = Integer.parseInt(args[0]);		
		pageSize = Integer.parseInt(args[1]);		
		processSize = Integer.parseInt(args[2]);		
		jobMix = Integer.parseInt(args[3]);		
		referenceCount = Integer.parseInt(args[4]);		
		
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
			if(args[6] == "0")
			{
				out = System.out;
				System.setOut(new PrintStream(new OutputStream(){
					public void write(int b) {}
				}));
			}
		}
		
		int framecount = machineSize/pageSize;
		algorithm.execute(machineSize, pageSize, processSize, getProcessList(jobMix), referenceCount, framecount);
		
		if(args.length ==7)
			System.setOut(out);
	}
	
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
		System.out.println();
	}

}
