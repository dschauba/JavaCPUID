package cpuid;

public class CacheType{
	public static enum Type{
		TLB,GENERAL,CACHE,PREFETCH,STLB
	}
	public String description;
	public int value;
	public Type type;
	public CacheType(int value,Type type,String description){
		this.value = value;
		this.type = type;
		this.description = description;
	}
	public String toString(){
		return String.format("[%d:%s]: %s",this.value,this.type,this.description);
	}
}