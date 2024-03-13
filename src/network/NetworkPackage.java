package network;

public class NetworkPackage<DataType> {

	private Class<?> dataClass = null;
	private DataType data = null;
	
	public NetworkPackage(Class<?> dataClass, DataType data){
		this.dataClass = dataClass;
		this.data = data;
	}
	
	public Class<?> getDataClass(){
		return dataClass;
	}
	
	public DataType getData(){
		return data;
	}
}
