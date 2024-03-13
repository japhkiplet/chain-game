package nativeProtocol;

import java.io.Serializable;

public class NetworkPackage<T extends Serializable> implements Serializable{

	private static final long serialVersionUID = -7663133177578368238L;
	private Class<? extends Serializable> dataClass = null;
	private T data = null;
	
	/**
	 * A generic frame used to standardize serialization of objects
	 * @param dataClass - the class of an object being serialized
	 * @param data - the object to serialize
	 */
	public NetworkPackage(Class<? extends Serializable> dataClass, T data){
		this.dataClass = dataClass;
		this.data = data;
	}
	
	/**
	 * @return object class
	 */
	public Class<?> getDataClass(){
		return dataClass;
	}
	
	/**
	 * @return the object
	 */
	public T getData(){
		return data;
	}
	
	@Override
	public String toString(){
		return "Network Package {"
				+ "Contains " + dataClass.toString()
				+ "}";
	}
}
