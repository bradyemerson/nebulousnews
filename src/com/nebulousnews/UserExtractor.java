package com.nebulousnews;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.nebulousnews.io.ObjectSerializableWritable;
import com.nebulousnews.users.User;

public class UserExtractor {
	public static void main(String[] args){
		ArrayList<User> users = new ArrayList<User>();
		try {
			FileInputStream underlyingStream = new FileInputStream("user_data");
			ObjectInputStream serializer = new ObjectInputStream(underlyingStream);
			while(true){
				User a = (User)serializer.readObject();
				users.add(a);
			}
		} catch (Exception e){
			
		}
		try{
			String intro = "0	OW[class=class java.lang.String,value=";
			FileInputStream partstream = new FileInputStream("part-00000");
			Scanner infile = new Scanner(partstream);
			for(User user:users){
				String line = infile.nextLine();
				//user.setUID(line.substring(0,line.indexOf('{')));
				String[] tag_line = line.substring(line.indexOf('{'),line.indexOf('}')).split(", ");
				HashMap<String, Double> normalized_tags = new HashMap<String,Double>();// = (HashMap<String, Double>) user.getUserTags();
				for(String tag: tag_line){
					String[] tag1 = tag.split("=");
					try{
						normalized_tags.put(tag1[0], Double.parseDouble(tag1[1]));
					}catch (ArrayIndexOutOfBoundsException e){
						normalized_tags.put(tag, 0.0);
						System.out.println("\nWarning: user " + user.getUID() + " tag " + tag + " did not have value\n");
					}
				}
				user.setNormalTags(normalized_tags);
			}
			FileOutputStream fileout = new FileOutputStream("user_normalized_data");
			ObjectOutputStream userout = new ObjectOutputStream(fileout);
			for(User user: users){
				userout.writeObject(user);	
			}
		} catch (Exception e){
			//don't care, it's late
			e.printStackTrace();
		}
	}
}
