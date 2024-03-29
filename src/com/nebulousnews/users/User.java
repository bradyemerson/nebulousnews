package com.nebulousnews.users;

import java.io.Serializable;

public class User extends UserTags implements Serializable{//let's hope it doesn't come to that, Writable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstName, lastName, UID, password;
	
	public User(){
		this("", "", "", "");
	}
	
	public User(String firstName, String lastName, String UID, String password){
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.UID = UID;
		this.password = password;
	}
	
	public User(String firstName, String lastName, String UID, String password, String tag, Double rating){
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.UID = UID;
		this.password = password;
		super.addUserTags(tag, rating);
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	public void setUID(String UID){
		this.UID = UID;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public String getUID(){
		return this.UID;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		sb.append("User ID: " + this.UID + " ");
		sb.append("First Name: " + this.firstName + " ");
		sb.append("Last Name: " + this.lastName + " ");
		sb.append("Password: " + this.password + " ");
		sb.append(super.toString() + "\n");
		
		return sb.toString();
	}
	/*
	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}
	*/
}
