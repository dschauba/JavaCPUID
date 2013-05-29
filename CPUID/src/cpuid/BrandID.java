package cpuid;

class BrandID{
	public int value;
	public String description;
	public BrandID(int value,String description){
		this.value = value;
		this.description = description;
	}
	public String toString(){
		return String.format("[%d] %s",this.value,this.description);
	}
}