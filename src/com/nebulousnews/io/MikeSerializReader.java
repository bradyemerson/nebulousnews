package com.nebulousnews.io;

import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import com.nebulousnews.users.*;
import java.util.List;
import java.util.ArrayList;

public class MikeSerializReader {
	public static void main(String [] args){
	String filename = "mike.ser";
		if(args.length > 0){
			filename = args[0];
		}
		List tempUser = new ArrayList();
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try{
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			tempUser = (ArrayList)in.readObject();
			in.close();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}
		User temp = (User)(tempUser.get(1));
		System.out.println("Flattened user: " + temp.getFirstName());
	}
}
