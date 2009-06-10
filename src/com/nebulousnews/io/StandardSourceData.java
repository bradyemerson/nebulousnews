package com.nebulousnews.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;


abstract public class StandardSourceData implements Writable{
	private String uID;
	private String dataType;
	private HashMap<String,String> metadata;
	private byte[] data;
	
	/**
	 * Sets the UID to a random 10 character string.
	 * <p>
	 * This method is recommended if your data has no intuitive
	 * Identifier
	 */
	public void UID(){
		//should be random String
		this.uID = "";
	}
	
	/**
	 * Sets the UID to a given String value.
	 * <p>
	 * It is imperative that the programmer take care to not create a UID that
	 * already exists, or the old record may be over-written.
	 * Changes '|' to ASCII char FS (1C), so that it won't mess up the writing procedure.
	 * 
	 * @param uid The UID that will be given to the object.
	 */
	public void UID(String uid){
		char[] uidTemp = uid.toCharArray();
		for(char a : uidTemp){
			if (a=='|')	a = 28; 
		}
		uid = new String(uidTemp);
		this.uID = uid;
	}
	
	/**
	 * Get the UID that the object has, returns empty String when no UID is present.
	 * Changes ASCII char FS (1C) to '|' so that it will print correctly.
	 * 
	 * @return The object's UID.
	 */
	public String getUID() {
		char[] uidTemp = uID.toCharArray();
		for(char a : uidTemp){
			if (a==28)	a = '|'; 
		}
		return new String(uidTemp);
		
	}
	
	/**
	 * Set the type of an object.  Only available as a string.
	 * The standard is to use MIMEtype to describe the content of the data.
	 * 
	 * The purpose of this is to help instruct which Classifier to use.
	 * 
	 * @param type The type of an object (i.e. "text/html")
	 */
	public void setDataType(String type){
		dataType = type;
	}
	
	/**
	 * Return the type of an object
	 * 
	 * @return The type of an object, or if the type is not set, returns an empty String.
	 */
	public String getDataType(){
		return dataType;
	}
	
	/**
	 * Writes out the object in the format of STD[UID(String)|Type(String)|Length(int)|data(compressed byte array)|metadata(compressed String)]
	 * 
	 * @see WritableUtils
	 */
	public void write(DataOutput out) throws IOException{
		WritableUtils.writeString(out,"STD[|"+uID+"|"+dataType+"|"+data.length+"|");
		WritableUtils.writeCompressedByteArray(out,data);
		WritableUtils.writeCompressedString(out,"|" + metadata.toString());
		WritableUtils.writeString(out,"]\n");
		
	}
	
	/**
	 * Reads in the object that was written by write().
	 * Throws IOException if header or data size is incorrect.
	 */
	public void readFields(DataInput in) throws IOException {
		// read in all the data
		String intro = WritableUtils.readString(in);
		byte[] dataTentative = WritableUtils.readCompressedByteArray(in);
		String metaData = WritableUtils.readCompressedString(in);
		// verify that it's a STD class element
		if (!intro.substring(0, 3).equals("STD")){
			throw new IOException("Incorrect type read");
		}
		// break up the values in the intro
		String[] introArray = intro.split("|");
		uID = introArray[1];
		dataType = introArray[2];
		int length = Integer.parseInt(introArray[3]);
		// verify that the data compressed and uncompressed correctly (or at least the same size)
		if (dataTentative.length != length){
			throw new IOException("Data length does not match check, suspect corruption.");
		} else { 
			data = dataTentative;
		}
		// remove the braces from the metadata, and return each key-value pair to the global variable 
		for(String elementData : metaData.substring(1,metaData.length()-1).split("\" , \"")){
			String[] dataElement = elementData.split("\"=\"");
			metadata.put(dataElement[0], dataElement[1]);
		}
	}
	
	/**
	 * Puts metadata values into the metadata variable.
	 * 
	 * @param key The String that is the key of the variable.
	 * @param value The String that is the value of the variable.
	 */
	public void putMetaDataValue(String key, String value){
		metadata.put(key, value);
	}
	
	/**
	 * Returns the value in metadata that is under key.
	 * Returns null if the value does not exist.
	 * 
	 * @param key The key of the value you want to access.
	 */
	public String getMetaDataValue(String key){
		return metadata.get(key);
	}
	
	/**
	 * Get the metadata available.
	 * 
	 * @return The metadata as a HashMap<String, String>. 
	 */
	public HashMap<String,String> getMetaData(){
		return metadata;
	}
	
	/**
	 * Get the data available.  For classes that inherit this class, it would probably be best to include a 
	 * "getData&lt;Type&gt;()" for whatever type the data is logically.  
	 * @return the data as byte[] 
	 */
	public byte[] getData(){
		return data;
	}
	
	/**
	 * Set the data that was collected.  Once the class has collected data 
	 * from its source, it should be saved here.
	 * 
	 * @param dataIn The data that was collected, after it has been cleaned and sanitized.
	 */
	abstract public void setData(byte[] dataIn);
	
	/**
	 * Set the metadata about the data that was collected.  Suggestions are 
	 * sourceURL, Author, sourceTitle, pageLinks, DateCollected, sourceIP, etc.
	 * This is the data that will be used in determining the best classifier, as 
	 * well as enriching the output later on, so any data that can be collected at this point  
	 * @param metaData
	 */
	abstract public void setMetaData(HashMap<String,String> metaData);
	
	
}
