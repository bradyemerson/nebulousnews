package com.nebulousnews.users;

public class User extends UserTags{

	private String firstName, lastName, UID, password;
	
	public User(){
		super("blank",0.0);
		this.firstName = "blank";
		this.lastName = "blank";
		this.UID = "blank";
		this.password = "blank";
	}
	
	public User(String firstName, String lastName, String UID, String password){
		this.firstName = firstName;
		this.lastName = lastName;
		this.UID = UID;
		this.password = password;
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
		
		return sb.toString();
	}
}
