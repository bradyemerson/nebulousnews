package com.nebulousnews.test;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.io.WritableUtils;

import com.nebulousnews.io.StandardSourceData;

public class SourceDataBreaker {
	public static void main(String[] args){
		FileInputStream streamer;
		StandardSourceData g = new StandardSourceData();
		int length = 0;
		try {
			streamer = new FileInputStream("sourceDataSample");
			DataInputStream in = new DataInputStream(streamer);
			g.readFields((DataInput) in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}
		byte[] foo = null;
		try {
			foo = "Hello, World!".getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(g.getUID() + " length: " + length);
		try {
			System.out.println(foo.toString() + new String(foo,"UTF8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
