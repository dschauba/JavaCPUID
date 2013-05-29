package cpuid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

public class Tests {

	private void test(){
		System.out.println("max cpuid: "+CPUIDJNIWrapper.getMaxCPUID(0, 0));
		int level = 0;
		int[] retval = CPUIDJNIWrapper.getCPUID(level);
		getMaskedValue(retval[2],0,4);
		System.out.println("level "+level+" reg: "+Arrays.toString(retval));	
		System.out.println("+++++++++++++");
		getProcessorInfoAndFeatures();
		System.out.println("+++++++++++++");
		decodeCacheDescriptors();
		System.out.println("+++++++++++++");
	}
	
	private static enum FEATURE_FLAGS_ECX_LEVEL2{
		SSE3,PCLMULDQ,DTES64,MONITOR,DS_CPL,VMX,SMX,EIST,TM2,SSSE3,CNXT_ID,RESERVED1,FMA,
		CX16,xTPR,PDCM,RESERVED2,PCID,DCA,SSE41,SSE42,x2APIC,MOVBE,POPCNT,TSC_DEADLINE,
		AES,XSAVE,OSXSAVE,AVX,F16C,RDRAND,NOTUSED
	};
	private static enum FEATURE_FLAGS_EDX_LEVEL2{
		FPU,VME,DE,PSE,TSC,MSR,PAE,MCE,CX8,APIC,RESERVED1,SEP,MTRR,PGE,MCA,CMOV,PAT,
		PSE_36,PSN,CLFSH,RESERVED2,DS,ACPI,MMX,FXSR,SSE,SSE2,SS,HTT,TM,RESERVED3,PBE
	};
	
	private static enum EAX_LEVEL6{
		DTS,ITBT,ARAT,PLN,ECMD,PTM
	};
	
	public static int getMaskedValue(int register, int start,int length){
		BitSet b = Helper.bitSetFromInt(register);
		System.out.println(String.format("%32s",Integer.toBinaryString(register).replace(' ','0')));
		
		BitSet extendedFamily = b.get(20,27);
		BitSet extendedModel = b.get(16,19);
		
		return 0;
	}
	
	public static void getProcessorInfoAndFeatures(){
		int[] retval = CPUIDJNIWrapper.getCPUID(1);
		ByteBuffer eaxBuffer = ByteBuffer.allocate(4);
		eaxBuffer.order(ByteOrder.LITTLE_ENDIAN);
		eaxBuffer.putInt(retval[1]);
		byte[] eaxBytes = eaxBuffer.array();
				
		ByteBuffer ecxBuffer = ByteBuffer.allocate(4);
		ecxBuffer.putInt(retval[3]);
		byte[] ecxBytes = ecxBuffer.array();
		
		
		
	
		int stepping_mask = 0b1111;
		int model_number_mask = 0b1111<<4;
		int ext_family_mask = 0b1111111<<20; 
		int family_mask = 0b1111<<8;
		int type_mask = 0b11<<12;
		int extended_model_mask = 0b1111<<16;
		int extended_family_mask = 0b11111111<<20;
			
		System.out.println(String.format("%32s eax",Integer.toBinaryString(retval[1]).replace(' ','0')));
		System.out.println(String.format("%32s stepping",Integer.toBinaryString(stepping_mask).replace(' ','0')));
		System.out.println(String.format("%32s model number",Integer.toBinaryString(model_number_mask).replace(' ','0')));
		System.out.println(String.format("%32s family",Integer.toBinaryString(family_mask).replace(' ','0')));
		System.out.println(String.format("%32s type",Integer.toBinaryString(type_mask).replace(' ','0')));
		System.out.println(String.format("%32s extended model",Integer.toBinaryString(extended_model_mask).replace(' ','0')));
		System.out.println(String.format("%32s extended family",Integer.toBinaryString(extended_family_mask).replace(' ','0')));
		
		int model_shift = retval[1]>>4;
		int model_val = model_number_mask&model_shift;
		System.out.println(String.format("%32s model",Integer.toBinaryString(model_shift).replace(' ','0')));
		
		System.out.println();		
		
		int ecx=retval[3];
		System.out.println(String.format("%32s ecx",Integer.toBinaryString(ecx).replace(' ','0')));
		
		for(FEATURE_FLAGS_ECX_LEVEL2 bit : FEATURE_FLAGS_ECX_LEVEL2.values()){
			int bit_mask = 1<<bit.ordinal();
			boolean available = (ecx&bit_mask)!=0;
			
			System.out.println(String.format("%32s %15s [%c]",Integer.toBinaryString(bit_mask).replace(' ','0'),bit,
														  (available? 'x':' ')));
		}
		
		int edx=retval[4];
		System.out.println(String.format("%32s edx",Integer.toBinaryString(edx).replace(' ','0')));
		for(FEATURE_FLAGS_EDX_LEVEL2 bit : FEATURE_FLAGS_EDX_LEVEL2.values()){
			int bit_mask = 1<<bit.ordinal();
			boolean available = (edx&bit_mask)!=0;
			
			System.out.println(String.format("%32s %15s [%c]",Integer.toBinaryString(bit_mask).replace(' ','0'),bit,
														  (available? 'x':' ')));
		}		
	}
	
	private static void decodeCacheDescriptors(){
		HashMap<Integer,String> chacheDescriptors = new HashMap<Integer,String>();
		chacheDescriptors.put(0x00, "null desc");
		chacheDescriptors.put(0x00, "null desc");
		chacheDescriptors.put(0x00, "null desc");
		chacheDescriptors.put(0x00, "null desc");
		chacheDescriptors.put(0x00, "null desc");
		int[] retval = CPUIDJNIWrapper.getCPUID(2);
		int eax = retval[1];
		int ebx = retval[2];
		int ecx = retval[3];
		int edx = retval[4];
		int repeats = eax&0xFF;
		System.out.println(String.format("%32s eax",Integer.toBinaryString(eax).replace(' ','0')));
		System.out.println("repeats: "+repeats);
		
		ByteBuffer chacheByteBuffer = ByteBuffer.allocate(4*4);
		chacheByteBuffer.putInt(eax);	
		if((ebx&(1<<32))!=0){
			chacheByteBuffer.putInt(ebx);
		}
		if((ecx&(1<<32))!=0){
			chacheByteBuffer.putInt(ecx);
		}
		if((edx&(1<<32))!=0){
			chacheByteBuffer.putInt(edx);
		}					
		byte[] chacheBytes = chacheByteBuffer.array();
		for(int i=0;i<chacheBytes.length;i++){
			System.out.print(String.format("%h ",chacheBytes[i]&0xff));
		}
		System.out.println();
		
		retval = CPUIDJNIWrapper.getCPUID(6);
		System.out.println(Arrays.toString(retval));
		chacheByteBuffer.clear();
		chacheByteBuffer.putInt(retval[1]);
		chacheByteBuffer.putInt(retval[2]);
		chacheByteBuffer.putInt(retval[3]);
		chacheByteBuffer.putInt(retval[4]);
		chacheBytes = chacheByteBuffer.array();
		for(int i=0;i<chacheBytes.length;i++){
			System.out.print(String.format("%h ",chacheBytes[i]&0xff));
		}
		System.out.println();
		
		eax = retval[1];
		System.out.println(String.format("%32s eax",Integer.toBinaryString(eax).replace(' ','0')));
		for(EAX_LEVEL6 bit : EAX_LEVEL6.values()){
			int bit_mask = 1<<bit.ordinal();
			boolean available = (eax&bit_mask)!=0;
			
			System.out.println(String.format("%32s %15s [%c]",Integer.toBinaryString(bit_mask).replace(' ','0'),bit,
														  (available? 'x':' ')));
		}		
		System.out.println();
		
		retval = CPUIDJNIWrapper.getCPUID(5);
		System.out.println("level 5: "+Arrays.toString(retval));		
	}
}
