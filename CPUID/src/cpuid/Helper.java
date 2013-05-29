package cpuid;

import java.util.BitSet;

import cpuid.CacheType.Type;

public class Helper {
	public static BitSet bitSetFromInt(int val){
		BitSet b = new BitSet();
		for(int i=0;i<32;i++){
			boolean isSet = ((1<<i)&val)!=0; 
			b.set(i,isSet);
		}
		return b;
	}
	public static int bitSetToInt(BitSet set){	
		byte[] bytes = set.toByteArray();
		int sum=0;
		for(int i=0;i<bytes.length;i++){
			sum|=bytes[i]<<(i*8);
		}
		return sum;		
	}
	
	public static int readByteValue(String field) {
		int ret=0;	
		try{
			ret = Integer.valueOf(field,16);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		return ret;
	}
	
	public static int readBinary(String field){
		int ret=0;
		if(field.contains("x")){
			return ret;
		}
		try{
			ret = Integer.valueOf(field,2);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		return ret;
	}
	
	public static int readInt(String field){
		int ret=0;		
		try{
			ret = Integer.valueOf(field);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		return ret;
	}
	
	public static Type readCacheType(String field){
		Type type = null;
		try{
			type = Type.valueOf(field);
		}catch(Exception e){
			e.printStackTrace();
		}
		return type;
	}
	
}
