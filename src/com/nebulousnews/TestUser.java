package com.nebulousnews;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.nebulousnews.users.User;

public class TestUser {
	public static void main(String[] args) {
		try {
			FileInputStream underlyingStream = new FileInputStream("user_data");
			ObjectInputStream serializer = new ObjectInputStream(underlyingStream);
			while(true){
				User a = (User)serializer.readObject();
				System.out.println(a.toString());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
