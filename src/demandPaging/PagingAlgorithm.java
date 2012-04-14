package demandPaging;

import java.util.ArrayList;

public enum PagingAlgorithm {
	fifo {
		@Override
		public void execute(int machineSize, int pageSize, int processSize,
				ArrayList<Process> processes, int referenceCount, int framecount) {

			ArrayList<Integer> pageFrames = new ArrayList<Integer>(framecount);
			
			for(int i=0;i<framecount;i++)
			{
				pageFrames.add(-1);
			}			
		}
	},
	lru {
		@Override
		public void execute(int machineSize, int pageSize, int processSize,
				ArrayList<Process> processes, int referenceCount, int framecount) {
			// TODO Auto-generated method stub

		}
	},
	random {
		@Override
		public void execute(int machineSize, int pageSize, int processSize,
				ArrayList<Process> processes, int referenceCount, int framecount) {
			// TODO Auto-generated method stub

		}
	};

	public abstract void execute(int machineSize, int pageSize, int processSize, ArrayList<Process> processes, int referenceCount, int framecount);
}
