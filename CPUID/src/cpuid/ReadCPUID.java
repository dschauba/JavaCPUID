package cpuid;

import java.util.List;


public class ReadCPUID {
	public ReadCPUID(){	
		updateCPUInformation();
	}	
	
	public void updateCPUInformation(){
		FeatureInformationDecoder featureInfoDecoder = new FeatureInformationDecoder();
		CPUIDLevel level0 = CPUIDJNIWrapper.readLevel(0);
		CPUIDLevel level1 = CPUIDJNIWrapper.readLevel(1);
		CPUIDLevel level2 = CPUIDJNIWrapper.readLevel(2);
		CPUIDLevel level4 = CPUIDJNIWrapper.readLevel(4);
		CPUIDLevel level5 = CPUIDJNIWrapper.readLevel(5);
		CPUIDLevel level6 = CPUIDJNIWrapper.readLevel(6);
		CPUIDLevel level81 = CPUIDJNIWrapper.readLevel(0x80000001);
		CPUIDLevel level82 = CPUIDJNIWrapper.readLevel(0x80000002);
		CPUIDLevel level83 = CPUIDJNIWrapper.readLevel(0x80000003);
		CPUIDLevel level84 = CPUIDJNIWrapper.readLevel(0x80000004);
		System.out.println(level0);
		System.out.println(level1);
		System.out.println(level2);
		System.out.println(level4);
		System.out.println(level5);
		System.out.println(level6);
		System.out.println(level81);
		System.out.println(level82);
		System.out.println(level83);
		System.out.println(level84);
		
		
		System.out.println("signature: "+featureInfoDecoder.getCPUSignature(level1));
		System.out.println("vendor id: "+featureInfoDecoder.getVendorID(level0));
		System.out.println("processor brand: "+featureInfoDecoder.getProcessorBrandString(level82,level83,level84));
		
		CPUFeatureDecoder featureDecoder = new CPUFeatureDecoder();
		System.out.println("features: ");
		List<FeatureFlag> flags = featureDecoder.getFeatures(level1);
		for(FeatureFlag flag : flags){
			System.out.println("    "+flag);
		}
		System.out.println("extended features: ");
		flags = featureDecoder.getExtendedFeatures(level81);
		for(FeatureFlag flag : flags){
			System.out.println("    "+flag);
		}
		
		CacheDescriptorsDecoder cacheDecoder = new CacheDescriptorsDecoder();
		List<CacheType> cacheFeatures = cacheDecoder.getCacheInformation(level2);
		System.out.println("cache: ");
		for(CacheType cache : cacheFeatures){
			System.out.println("    "+cache);
		}
		
		MonitorParameterDecoder monitorDecoder = new MonitorParameterDecoder();
		System.out.println(monitorDecoder.getMonitorParameters(level5));
		
		ThermalSensorPowerManagementDecoder thermalPowerDecoder = new ThermalSensorPowerManagementDecoder();
		System.out.println(thermalPowerDecoder.getThermalSensorsData(level6));
	}		
	
	public static void main(String[] args){		
		ReadCPUID c = new ReadCPUID();		
	}
}
