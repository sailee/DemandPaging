package demandPaging;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Process {
	private double A,B,C;
	int processID, pageCount, referenceCount, currentReference, w, processSize;
	private static FileReader fread = null; 
	private static Scanner scan = null; 

	public Process(double a, double b, double c, int id, int pgCount, int refCount, int size)
	{
		A=a;
		B=b;
		C=c;
		processID = id;
		pageCount = pgCount;
		referenceCount = refCount;
		currentReference = 0;
		w = 111*id % size;
		processSize = size;
	}	

	public int getNextReference()
	{
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
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

		if(currentReference == 0)
		{
			currentReference++;
			return w;
		}

		return getRandomReference();
	}

	private int getRandomReference()
	{
		currentReference++;
		double y = scan.nextInt();
		System.out.println("Fetched Random Number: " + y);
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
			w = scan.nextInt();
			System.out.println("Fetched Random Number: " + w);
			w=w%processSize;
		}
			
		return w;
	}
	
	public String toString()
	{
		String str = "\nID: " + processID + "\nW: " + w +"\n";
		return str;
	}
	
	public boolean hasPendingReferences()
	{
		return referenceCount > currentReference;
	}

}
