package demandPaging;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Process {
	private double A,B,C;
	int processID, pageCount, referenceCount, currentReference, w, processSize;
	private static FileReader fread = null; //= new FileReader("RandomNumbers");
	private static Scanner scan = null; //= new Scanner(fread);	
	
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
			System.out.println(ex.setStackTrace(null));
		}
		
		if(currentReference == 0)
			return w;
		return w;
	}
	
}
