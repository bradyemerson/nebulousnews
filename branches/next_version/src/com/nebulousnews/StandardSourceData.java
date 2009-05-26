package com.nebulousnews;


abstract public class StandardSourceData {
	private String uID;
	private int dataType;
	
	public String UID(){
		this.uid = "";
		this.dataType = 0;
	}
	
	public String UID(String uID) {
		this.uID = uID;
		this.dataType = 0;
	}
	
	public int setDataType(int dataType){
		this.dataType = dataType;
	}

	abstract public <T> setData(T data){
		
	}
	
	abstract public <T> getData(){
		
	}
	
	abstract public <T> setMetaData(T metaData){
		
	}
	
	abstract public <T> getMetaData(){
		
	}
	
}
