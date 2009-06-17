package com.nebulousnews.plugins;

import java.util.HashMap;

public abstract class TaggerPlugin {
	private byte[] data;
	private HashMap<String, String> metadata;
	private String type;
	
	public TaggerPlugin (String typeIn, HashMap<String, String> metaData){
		metadata = metaData;
		type = typeIn;
	}
}
