package cpuid;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.TreeMap;

class FeatureInformationDecoder{
	private List<CPUSignature> signatures;
	private TreeMap<Integer,BrandID> brandIDs;
	
	public FeatureInformationDecoder(){
		brandIDs = loadBrandIDs(new File("intel_brand_ids.csv"));
		signatures = loadSignatures();
	}
	
	public CPUSignature getCPUSignature(CPUIDLevel level1){
		BitSet eaxSet = Helper.bitSetFromInt(level1.eax);
		BitSet extendedFamilySet = eaxSet.get(20, 27);
		BitSet extendedModelSet = eaxSet.get(16,19);
		BitSet typeSet = eaxSet.get(12,13);
		BitSet familyCodeSet = eaxSet.get(8, 11);
		BitSet modelNumberSet = eaxSet.get(4, 7);
		
		for(CPUSignature sig : signatures){
			if(sig.isCPU(extendedFamilySet,extendedModelSet,typeSet,familyCodeSet,modelNumberSet)){
				return sig;
			}
		}		
		return null;	
	}	
	
	public BrandID getBrandID(CPUIDLevel level1){	
		BitSet ebxSet = Helper.bitSetFromInt(level1.ebx);
		int brandIDValue = Helper.bitSetToInt(ebxSet.get(0, 7));	
		return this.brandIDs.get(brandIDValue);
	}
	
	public String getVendorID(CPUIDLevel level0){
		ByteBuffer b = ByteBuffer.allocate(4*3);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(level0.ebx);		
		b.putInt(level0.edx);
		b.putInt(level0.ecx);
		return new String(b.array());
	}
	
	public String getProcessorBrandString(CPUIDLevel level82,CPUIDLevel level83,CPUIDLevel level84){
		ByteBuffer b = ByteBuffer.allocate(4*4*3);		
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(level82.eax);		
		b.putInt(level82.ebx);
		b.putInt(level82.ecx);
		b.putInt(level82.edx);
		b.putInt(level83.eax);		
		b.putInt(level83.ebx);
		b.putInt(level83.ecx);
		b.putInt(level83.edx);
		b.putInt(level84.eax);		
		b.putInt(level84.ebx);
		b.putInt(level84.ecx);
		b.putInt(level84.edx);
		return new String(b.array());
	}
	
	private List<CPUSignature> loadSignatures(){		
		List<CPUSignature> signatures = new ArrayList<CPUSignature>();
		File signatureFile = new File("intel_cpu_signatures.csv");
		List<Object> decodedTable = DecodeTableLoader.loadFeatureFlags(signatureFile, 
				new TableDecoder<CPUSignature>() {	
			public CPUSignature decodeLine(String line) {				
				return parseSignature(line);
			}
		});
		for(Object decodedSignature : decodedTable){
			CPUSignature signature = (CPUSignature)decodedSignature;
			signatures.add(signature);
		}
		return signatures;
	}
	private CPUSignature parseSignature(String line){
		String[] raw = line.split(";");	
		CPUSignature sig = null;
		try{
			int extendedFamily = Helper.readBinary(raw[0]);
			int extendedModel = Helper.readBinary(raw[1]);
			int type = Helper.readBinary(raw[2]);
			int familyCode = Helper.readBinary(raw[3]);
			int modelNo = Helper.readBinary(raw[4]);
			int steppingID = Helper.readBinary(raw[5]);
			String description = raw[6];
			sig = new CPUSignature(extendedFamily, extendedModel, type, familyCode, modelNo, steppingID, description);
		}catch(Exception e){
			e.printStackTrace();
		}		
		return sig;
	}
	
	private TreeMap<Integer,BrandID> loadBrandIDs(File brandIDFile){		
		TreeMap<Integer,BrandID> brandIDs = new TreeMap<Integer,BrandID>();
		List<Object> decodedTable = DecodeTableLoader.loadFeatureFlags(brandIDFile, 
				new TableDecoder<BrandID>() {	
			public BrandID decodeLine(String line) {				
				return parseBrandID(line);
			}
		});
		for(Object decodedID : decodedTable){
			BrandID id = (BrandID)decodedID;
			brandIDs.put(id.value,id);
		}		
		return brandIDs;
	}
	
	private BrandID parseBrandID(String line){
		String[] raw = line.split(";");	
		BrandID id = null;
		try{
			int value = Helper.readByteValue(raw[0]);
			String description = raw[1];
			id = new BrandID(value,description);
		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}
}