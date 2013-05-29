package cpuid;

class CPUIDLevel{
	public int level;
	public int eax;
	public int ebx;
	public int ecx;
	public int edx;
	public CPUIDLevel(int level,int eax,int ebx,int ecx,int edx){
		this.level = level;
		this.eax = eax;
		this.ebx = ebx;
		this.ecx = ecx;
		this.edx = edx;
	}
	public int[] asArray(){
		return new int[]{this.eax,this.ebx,this.ecx,this.edx};
	}
	
	public static CPUIDLevel init(int level,int[] registers){
		return new CPUIDLevel(level,registers[1],registers[2],registers[3],registers[4]);
	}
	public String toString(){
		return String.format("CPUID Level %h\n" +
							 "    eax: %32s:%h\n" +
							 "    ebx: %32s:%h\n" +
							 "    ecx: %32s:%h\n" +
							 "    edx: %32s:%h\n",this.level,
									Integer.toBinaryString(this.eax),this.eax,
									Integer.toBinaryString(this.ebx),this.ebx,
									Integer.toBinaryString(this.ecx),this.ecx,
									Integer.toBinaryString(this.edx),this.edx);
	}
}

public class CPUIDJNIWrapper {
	public static final int LEVEL_INDEX=0;
	public static final int EAX_INDEX = 1;
	public static final int EBX_INDEX = 2;
	public static final int ECX_INDEX = 3;
	public static final int EDX_INDEX = 4;	
	public static native int[] getCPUID(int level);
	public static native int getMaxCPUID(int extended, int signature);
	static {
	    System.loadLibrary("jni/cpuidjni");
	}	
	
	public static CPUIDLevel readLevel(int level){
		return CPUIDLevel.init(level,CPUIDJNIWrapper.getCPUID(level));
	}
}
