package cpuid;
import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.TreeMap;

class CPUFeatureDecoder{
	private TreeMap<Integer,FeatureFlag> featureFlagsECX;
	private TreeMap<Integer,FeatureFlag> featureFlagsEDX;
	private TreeMap<Integer,FeatureFlag> featureFlagsExtendedECX;
	private TreeMap<Integer,FeatureFlag> featureFlagsExtendedEDX;	
	public CPUFeatureDecoder(){
		featureFlagsECX = loadFeatureFlags(new File("intel_level2_flags_ecx.csv"));
		featureFlagsEDX = loadFeatureFlags(new File("intel_level2_flags_edx.csv"));
		featureFlagsExtendedECX = loadFeatureFlags(new File("intel_extended_level_ecx_flags.csv"));
		featureFlagsExtendedEDX = loadFeatureFlags(new File("intel_extended_level_edx_flags.csv"));
	}
	
	public List<FeatureFlag> getFeatures(CPUIDLevel level1){
		List<FeatureFlag> supportedFeatures = new ArrayList<FeatureFlag>();	
		BitSet ecxBitSet = Helper.bitSetFromInt(level1.ecx);
		BitSet edxBitSet = Helper.bitSetFromInt(level1.edx);
		
		int nextSetBit = ecxBitSet.nextSetBit(0);
		while(nextSetBit!=-1){
			FeatureFlag setFlag = this.featureFlagsECX.get(nextSetBit);
			supportedFeatures.add(setFlag);
			nextSetBit = ecxBitSet.nextSetBit(nextSetBit+1);
		}
		
		nextSetBit = edxBitSet.nextSetBit(0);
		while(nextSetBit!=-1){
			FeatureFlag setFlag = this.featureFlagsEDX.get(nextSetBit);
			supportedFeatures.add(setFlag);
			nextSetBit = edxBitSet.nextSetBit(nextSetBit+1);
		}	
		return supportedFeatures;
	}
	
	public List<FeatureFlag> getExtendedFeatures(CPUIDLevel level81){
		List<FeatureFlag> supportedFeatures = new ArrayList<FeatureFlag>();
		BitSet ecx = Helper.bitSetFromInt(level81.ecx);
		BitSet edx = Helper.bitSetFromInt(level81.edx);
		int nextSetBit = ecx.nextSetBit(0);
		while(nextSetBit!=-1){
			FeatureFlag setFlag = this.featureFlagsExtendedECX.get(nextSetBit);
			supportedFeatures.add(setFlag);
			nextSetBit = ecx.nextSetBit(nextSetBit+1);
		}		
		nextSetBit = edx.nextSetBit(0);
		while(nextSetBit!=-1){
			FeatureFlag setFlag = this.featureFlagsExtendedEDX.get(nextSetBit);
			supportedFeatures.add(setFlag);
			nextSetBit = edx.nextSetBit(nextSetBit+1);
		}	
		return supportedFeatures;
	}
	
	private TreeMap<Integer,FeatureFlag> loadFeatureFlags(File flagFile){	
		TreeMap<Integer,FeatureFlag> featureFlags = new TreeMap<Integer,FeatureFlag>();
		List<Object> decodedTable = DecodeTableLoader.loadFeatureFlags(flagFile, 
			new TableDecoder<FeatureFlag>() {	
				public FeatureFlag decodeLine(String line) {				
					return parseFeatureFlag(line);
				}
			});
		for(Object decodedFlag : decodedTable){
			FeatureFlag flag = (FeatureFlag)decodedFlag;
			featureFlags.put(flag.bit,flag);
		}
		return featureFlags;
	}
	
	private FeatureFlag parseFeatureFlag(String line){
		String[] raw = line.split(";");	
		FeatureFlag flag = null;
		try{
			int bit = Helper.readInt(raw[0]);
			String name = raw[1];
			String description = raw[2];
			flag = new FeatureFlag(bit,name,description);
		}catch(Exception e){
			e.printStackTrace();
		}		
		return flag;
	}	
}