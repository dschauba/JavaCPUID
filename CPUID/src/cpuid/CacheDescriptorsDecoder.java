package cpuid;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.TreeMap;

import cpuid.CacheType.Type;

class CacheDescriptorsDecoder{
	private TreeMap<Integer,CacheType> cacheTypes;
	public CacheDescriptorsDecoder(){
		cacheTypes = loadCacheDescriptors(new File("intel_cache_descriptors.csv"));
	}
	
	public List<CacheType> getCacheInformation(CPUIDLevel level2){
		List<CacheType> supportedCacheTypes = new ArrayList<CacheType>();			
		BitSet eax = Helper.bitSetFromInt(level2.eax);
		BitSet ebx = Helper.bitSetFromInt(level2.ebx);
		BitSet ecx = Helper.bitSetFromInt(level2.ecx);
		BitSet edx = Helper.bitSetFromInt(level2.edx);		
		decodeCacheInformation(eax,supportedCacheTypes);
		decodeCacheInformation(ebx,supportedCacheTypes);
		decodeCacheInformation(ecx,supportedCacheTypes);
		decodeCacheInformation(edx,supportedCacheTypes);
		return supportedCacheTypes;
	}
	
	private void decodeCacheInformation(BitSet register,List<CacheType> supported){
		byte[] bytes = register.toByteArray();
		for(int b : bytes){
			CacheType type = this.cacheTypes.get(b&0xff);
			if(type!=null){
				supported.add(type);
			}
		}
	}

	private TreeMap<Integer,CacheType> loadCacheDescriptors(File cacheFile){		
		TreeMap<Integer,CacheType> cacheTypes = new TreeMap<Integer,CacheType>();
		List<Object> decodedTable = DecodeTableLoader.loadFeatureFlags(cacheFile, 
				new TableDecoder<CacheType>() {	
			public CacheType decodeLine(String line) {				
				return parseCacheType(line);
			}
		});
		for(Object decodedType : decodedTable){
			CacheType type = (CacheType)decodedType;
			cacheTypes.put(type.value,type);
		}	
		return cacheTypes;
	}
	
	private CacheType parseCacheType(String line){
		String[] raw = line.split(";");	
		CacheType type = null;
		try{
			int value = Helper.readByteValue(raw[0]);
			Type cacheType = Helper.readCacheType(raw[1]);
			String description = raw[2];
			type = new CacheType(value, cacheType, description);
		}catch(Exception e){
			e.printStackTrace();
		}
		return type;		
	}
}