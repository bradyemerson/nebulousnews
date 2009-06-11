package com.nebulousnews.test;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.nebulousnews.io.StandardSourceData;

/**
 * Creates a single instance of StandardSourceData and writes it to a file several times
 * 
 * @author Jason Schlesinger
 *
 */
public class SourceDataGenerator {
	public static void main(String[] args){
		StandardSourceData data0 = new StandardSourceData();
		data0.setData("Hello, World!".getBytes());
		data0.setDataType("text/plain");
		HashMap<String,String> metadata = new HashMap<String,String>();
		metadata.put("source", "Afghanistan");
		metadata.put("foo","bar");
		data0.setMetaData(metadata);
		
		try {
			FileOutputStream streamer = new FileOutputStream("sourceDataSample");
			DataOutputStream out = new DataOutputStream(streamer);
			for(int i = 0;i<10;i++){
				data0.UID(i+"_test");
				data0.write((DataOutput) out);
			}
			out.close();
			streamer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
