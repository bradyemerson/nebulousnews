package com.nebulousnews.users;

import java.util.Map;

public class UserTags {

	private Map<String, Double> tags;
	private Double increment = 1.0;
	private Double decrement = -1.0;
	
	public UserTags(){
		this.tags.clear();
	}
	public UserTags(String tag, Double rating){
		this.tags.put(tag,rating);
	}
	
	public void addUserTags(String tag, Double rating){
		this.tags.put(tag, rating);
	}
	
	public void updateUserTagMeanRating(String tag, Double rating){
		Double tempRating;
		tempRating = (this.tags.get(tag) + rating)/2;
		this.tags.put(tag,tempRating);	
	}
	
	public void incrementUserTagRating(String tag){
		Double tempRating;
		tempRating = this.tags.get(tag) + this.increment;
		this.tags.put(tag,tempRating);
	}
	
	public void decrementUserTagRating(String tag){
		Double tempRating;
		tempRating = this.tags.get(tag) + this.decrement;
		this.tags.put(tag,tempRating);
	}
	
	public void setIncrement(Double increment){
		this.increment = increment;
	}
	
	public void setDecrement(Double decrement){
		this.decrement = decrement;
	}
	
	public String toString(){
		return "User Tags: " + this.tags.toString();
	}
}
