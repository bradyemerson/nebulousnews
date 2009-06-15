package com.nebulousnews.conf;

import java.util.HashMap;
import java.util.Scanner;

public class ConfigReader {
	private HashMap<String, String> configOptions;
	
	public ConfigReader(){
		Scanner in = new Scanner("nebulous-default.conf");
		while(in.hasNextLine()){
			readConfiguration(in.nextLine());
		}
		in = new Scanner("nebulous-site.conf");
		while(in.hasNextLine()){
			readConfiguration(in.nextLine());
		}
	}
	
	private void readConfiguration(String line){
		if(line.charAt(0) != '#'){
			line.replaceAll("[\s]", "");
			String[] keyval = line.split("=");
			if(keyVal.size == 2){
				configOptions.put(keyVal[0],keyVal[1]);
			}
		}
	}
	
	public String getOption(String key){
		return configOptions.get(key);
	}
}
