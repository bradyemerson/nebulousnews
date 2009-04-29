package com.nebulousnews.io;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.nebulousnews.users.*;

public class MikeSerializWriter {
	public static void main(String [] args){
		String filename = "mike.ser";
		
		if(args.length > 0){
			filename = args[0];
		}
		
		List userList = new ArrayList();
		User test = new User();
		userList.add(test);
		userList.add(new User("Michael", "Cervantez", "123", "test"));
		
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(userList);
			out.close();
		} catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
