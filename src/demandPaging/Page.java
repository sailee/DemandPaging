package demandPaging;

public class Page {
	int residencyTime;
	Process p;
	
	public Page()
	{
		residencyTime = 0;
		p=null;
	}
	
	public void Evict(int clock)
	{
		residencyTime = clock;
	}
	
	public int getPageResidency()
	{
		return residencyTime;
	}
}
