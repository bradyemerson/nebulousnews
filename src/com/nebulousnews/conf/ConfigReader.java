package com.nebulousnews.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class controls how configuration is read and passed on to other classes in Nebulous News.
 * <a href="http://code.google.com/p/nebulousnews/wiki/Configuration">The wiki on this class is available here</a>
 * @author Jason Schlesinger
 */
public class ConfigReader {
	private HashMap<String, String> configOptions;
	
	/**
	 * Reads in nebulous-default.conf and the site-config file
	 */
	public ConfigReader(){
		Scanner in = new Scanner("nebulous-default.conf");
		while(in.hasNextLine()){
			readConfiguration(in.nextLine());
		}
		in = new Scanner(this.getOption("site-config"));
		while(in.hasNextLine()){
			readConfiguration(in.nextLine());
		}
	}
	/**
	 * Private method that reads in a line in, ignoring whitespace, comments (defined by '#'),
	 * and lines without '=' 
	 * 
	 * @param line The line of the configuration file being read.
	 */
	private void readConfiguration(String line){
		line.replaceAll("[\t ]", "");  // ignore whitespace
		if(line.charAt(0) != '#'){     // ignore comments
			line = line.split("#")[0]; // ignore comments
			String[] keyVal = line.split("="); //ignore 
			if(keyVal.length == 2){
				configOptions.put(keyVal[0],keyVal[1]);
			}
		}
	}
	
	/**
	 * Read in a 
	 * @param path
	 * @throws FileNotFoundException 
	 */
	public void readConfigFile(File path) throws FileNotFoundException{
		Scanner in = new Scanner(path);
		while(in.hasNextLine()){
			readConfiguration(in.nextLine());
		}
	}
	
	public String getOption(String key){
		return configOptions.get(key);
	}
	
	public boolean hasOption(String key){
		return configOptions.containsKey(key);
	}
}
