package demandPaging;

import java.util.LinkedList;

/**
 * Abstracts FIFO, LRU and Random page replacement algorithms, and their page replacement techniques
 * @author Sailee Latkar
 */
public enum PagingAlgorithm {
	fifo {

		@Override
		public int Evict(LinkedList<Page> pageFrames, int clock) {
			Page lastPage = pageFrames.getLast();
			System.out.print(": Fault, evicting page "+lastPage.getID()+" of "+ lastPage.getProcessID()+" from frame " + pageFrames.size());
			System.out.println();
			pageFrames.remove(lastPage);
			lastPage.Evict(clock);
			
			pageFrames.addFirst(lastPage);
			
			return 0;			
		}
	},
	lru {

		@Override
		public int Evict(LinkedList<Page> pageFrames, int clock) {
			int min = Integer.MAX_VALUE;
			int i,index = 0;

			for(i=0;i<pageFrames.size();i++)
			{
				if(min > pageFrames.get(i).getLastUsed())
				{
					min = pageFrames.get(i).getLastUsed();
					index = i;
				}
			}
			System.out.print(": Fault, evicting page "+pageFrames.get(index).getID()+" of "+ pageFrames.get(0).getProcessID()+" from frame " + index);
			System.out.println();
			pageFrames.get(index).Evict(clock);
			return index;
		}		
	},
	random {

		@Override
		public int Evict(LinkedList<Page> pageFrames, int clock) {
			int index = Process.getRandom()%pageFrames.size();
			System.out.print(": Fault, evicting page "+pageFrames.get(index).getID()+" of "+ pageFrames.get(0).getProcessID()+" from frame " + index);
			System.out.println();
			pageFrames.get(index).Evict(clock);
			
			return 	index;
		}
	};

	/**
	 * Chooses the page to evict based on the algorithm
	 * @param pageFrames linked list of pages representing the used page frames
	 * @param clock time of eviction
	 * @return index of the evicted page
	 */
	public abstract int Evict(LinkedList<Page> pageFrames, int clock);

	/**
	 * Fetches a page into a pageframe, based on the page replacement algorithm chosen 
	 * @param pageFrames linked list of pages representing page frames
	 * @param clock time at which page is to be made resident 
	 * @return index of the free page
	 */
	public int fetchPage(LinkedList<Page> pageFrames, int clock) {		
		for(int i=pageFrames.size(); i >0; i--)
		{
			if(pageFrames.get(i-1).isFree())
			{
				System.out.print(": Fault, using free frame " + i + "\n");
				return (i-1);
			}
		}
		countOfEvictions++;
		return this.Evict(pageFrames, clock);
	}
	
	/**
	 * Returns the total number of evictions
	 * @return total number of evictions
	 */
	public static int getCountOfEvictions()
	{
		return countOfEvictions;
	}
	
	private static int countOfEvictions = 0; 
}

