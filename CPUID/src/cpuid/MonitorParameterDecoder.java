package cpuid;

import java.util.BitSet;
public class MonitorParameterDecoder{
	public MonitorParameters getMonitorParameters(CPUIDLevel level5){		
		BitSet eax = Helper.bitSetFromInt(level5.eax);
		BitSet ebx = Helper.bitSetFromInt(level5.ebx);
		BitSet ecx = Helper.bitSetFromInt(level5.ecx);
		BitSet edx = Helper.bitSetFromInt(level5.edx);
		
		int smallestMonitorSize = Helper.bitSetToInt(eax.get(0, 15));
		int largestMonitorSize = Helper.bitSetToInt(ebx.get(0, 15));
		boolean treatingSupport = ecx.get(1);
		boolean monitorWaitExtension = ecx.get(0);
		int c4states = Helper.bitSetToInt(edx.get(16,19));
		int c3states = Helper.bitSetToInt(edx.get(12,15));
		int c2states = Helper.bitSetToInt(edx.get(8,11));
		int c1states = Helper.bitSetToInt(edx.get(4,7));
		int c0states = Helper.bitSetToInt(edx.get(0,3));	
		
		return new MonitorParameters(smallestMonitorSize, largestMonitorSize, treatingSupport, monitorWaitExtension, c4states, c3states, c2states, c1states, c0states);
	}
	
}