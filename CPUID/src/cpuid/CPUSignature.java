package cpuid;

import java.util.BitSet;

public class CPUSignature{
	public BitSet extendedFamily;
	public BitSet extendedModel;
	public BitSet type;
	public BitSet familyCode;
	public BitSet modelNo;
	public BitSet steppingID;
	public String description;
	public CPUSignature(int extendedFamily,int extendedModel,int type,
						int familyCode,int modelNo,int steppingID,String description){
		this.extendedFamily = Helper.bitSetFromInt(extendedFamily);
		this.extendedModel = Helper.bitSetFromInt(extendedModel);
		this.type = Helper.bitSetFromInt(type);
		this.familyCode = Helper.bitSetFromInt(familyCode);
		this.modelNo = Helper.bitSetFromInt(modelNo);
		this.steppingID = Helper.bitSetFromInt(steppingID);
		this.description = description;		
	}
	
	public boolean isCPU(BitSet testExtendedFamily,BitSet testExtendedModel,
						 BitSet testProcessType,BitSet testFamily,BitSet testModel){
		return testExtendedFamily.equals(extendedFamily)&&
			   testExtendedModel.equals(extendedModel)&&
			   testProcessType.equals(type)&&
			   testFamily.equals(familyCode)&&
			   testModel.equals(modelNo);
	}
	
	
	public String toString(){
		return String.format("Extended Family: %s\nExtended Model: %s\n"+
						     "Type: %s\nFamily Code: %s\nModel No.: %s\nStepping ID: %s\nDescription: %s\n############\n",
						     extendedFamily,extendedModel,type,familyCode,modelNo,
						     steppingID,description);
	}
}