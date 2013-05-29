package cpuid;

import java.util.BitSet;
class ThermalSensorPowerManagementFlags{
	public boolean ptmCabable;
	public boolean ecmdCapable;
	public boolean plnCapable;
	public boolean aratCapable;
	public boolean turboBoostCapabl;
	public boolean dtsCapable;
	public int interruptThresholds;
	public boolean performanceEnergyBiasCapable;
	public boolean acnt2Capable;
	public boolean hardwareCondFeedbackAvailable;
	public ThermalSensorPowerManagementFlags(boolean ptm, boolean ecmd,
											 boolean pln,boolean arat,
											 boolean turboBost,boolean dts,
											 boolean performEnergyBias,
											 boolean acnt2,boolean hardwareCondFeedback,
											 int interruptThresholds){
		this.ptmCabable = ptm;
		this.ecmdCapable = ecmd;
		this.plnCapable = pln;
		this.aratCapable = arat;
		this.turboBoostCapabl = turboBost;
		this.dtsCapable = dts;
		this.performanceEnergyBiasCapable = performEnergyBias;
		this.acnt2Capable = acnt2;
		this.hardwareCondFeedbackAvailable = hardwareCondFeedback;
		this.interruptThresholds = interruptThresholds;	
	}
	public String toString(){
		return String.format("Thermal Sensor & Power Management Flags:\n" +
							 "    ptm: %s\n" +
							 "    ecmd: %s\n" +
							 "    pln: %s\n" +
							 "    arat: %s\n" +
							 "    turbo bost: %s\n" +
							 "    dts: %s\n" +
							 "    interrupt thresholds: %d\n" +
							 "    performance energy bias: %s\n" +
							 "    acnt2: %s\n" +
							 "    hardware condition feedback: %s",this.ptmCabable,
							 this.ecmdCapable,this.plnCapable,this.aratCapable,
							 this.turboBoostCapabl,this.dtsCapable,this.interruptThresholds,
							 this.performanceEnergyBiasCapable,this.acnt2Capable,this.hardwareCondFeedbackAvailable);
	}
	
}

class ThermalSensorPowerManagementDecoder{
	public ThermalSensorPowerManagementFlags getThermalSensorsData(CPUIDLevel level6){	
		BitSet eax = Helper.bitSetFromInt(level6.eax);
		BitSet ebx = Helper.bitSetFromInt(level6.ebx);
		BitSet ecx = Helper.bitSetFromInt(level6.ecx);
		boolean ptm = eax.get(6);
		boolean ecmd = eax.get(5);
		boolean pln = eax.get(4);
		boolean arat = eax.get(2);
		boolean turboBoost = eax.get(1);
		boolean dts = eax.get(0);
		int interruptThresholds = Helper.bitSetToInt(ebx.get(0, 3));
		boolean performanceEnergyBias = ecx.get(3);
		boolean acnt2= ecx.get(1);
		boolean hardwareCondFeedback = ecx.get(0);
		return new ThermalSensorPowerManagementFlags(ptm, ecmd, pln, arat, turboBoost, dts, 
													 performanceEnergyBias, acnt2, hardwareCondFeedback, interruptThresholds);	
	}
}