package cpuid;

class MonitorParameters{
	public int smallesMonitorSize;
	public int largestMonitorSize;
	public boolean treatingSupport;
	public boolean monitorWaitExtension;
	public int c4StateCount;
	public int c3StateCount;
	public int c2StateCount;
	public int c1StateCount;
	public int c0StateCount;	
	public MonitorParameters(int smallestMonitorSize,int largestMonitorSize,
							 boolean treatingSupport,boolean monitorWaitExtension,
							 int c4states,int c3states,int c2states,int c1states,int c0states){
		this.smallesMonitorSize = smallestMonitorSize;
		this.largestMonitorSize = largestMonitorSize;
		this.treatingSupport = treatingSupport;
		this.monitorWaitExtension = monitorWaitExtension;
		this.c4StateCount = c4states;
		this.c3StateCount = c3states;
		this.c2StateCount = c2states;
		this.c1StateCount = c1states;
		this.c0StateCount = c0states;
	}
	public String toString(){
		return String.format("Monitor Parameters:\n" +
							 "    smallest monitor size: %d\n" +
							 "    largest monitor size: %d\n"+
							 "    treating enabled: %s\n" +
							 "    monitor wait extension: %s\n"+
							 "    c4 states: %d\n" +
							 "    c3 states: %d\n" +
							 "    c2 states: %d\n" +
							 "    c1 states: %d\n" +
							 "    c0 states: %d",
							 this.smallesMonitorSize,this.largestMonitorSize,this.treatingSupport,this.monitorWaitExtension,
							 this.c4StateCount,this.c3StateCount,this.c2StateCount,this.c1StateCount,this.c0StateCount);
	}
}