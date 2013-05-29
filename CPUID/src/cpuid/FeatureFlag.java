package cpuid;

class FeatureFlag{
	public String description;
	public String name;
	public int bit;
	public FeatureFlag(int bit,String name,String description){
		this.bit = bit;
		this.name = name;
		this.description = description;
	}
	public String toString(){
		return String.format("Flag [%d:%s] : %s",this.bit, this.name,this.description);
	}
}