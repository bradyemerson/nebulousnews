package com.nebulousnews;

import java.io.DataInput;
import java.io.IOException;


abstract public class StandardSourceData implements Writable{
	private String uID;
	private String dataType;
	private HashMap metadata;
	private byte[] data;
	
	public String UID(){
		this.uID = "";
		this.dataType = 0;
	}
	
	public String UID(String uid) {
		this.uID = uid;
		this.dataType = 0;
	}
	
	public int setDataType(int datatype){
		this.dataType = datatype;
	}

	public void write(DataOutput out){
		out.write("STD|"+uID+"|"+dataType+"|");
		out.write(data.length+"|");
		out.write(data);
		out.write("|");
		out.write(metadata.toString()+"\n");
		
	}
	
	public void readFields(DataInput in) throws IOException {
		String in.readLine()
	}
	
	abstract public setData(byte[] dataIn){
		data = dataIn;
	}
	
	abstract public byte[] getData(){
		return data;
	}
	
	abstract public setMetaData(HashMap metaData){
		metaData = metaData;
	}
	
	abstract public HashMap getMetaData(){
		return metadata
	}
	
	
}
