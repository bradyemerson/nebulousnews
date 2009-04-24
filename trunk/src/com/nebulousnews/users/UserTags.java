package com.nebulousnews.users;

import java.util.HashMap;
import java.util.Map;

public class UserTags {

	private Map<String, Double> tags = new HashMap<String, Double>();
	private Map<String, Double> norm_tags;
	private Double increment = 1.0;
	private Double decrement = -1.0;
	
	public UserTags(){
		this.tags.put("blank",0.0);
	}
	public UserTags(String tag, Double rating){
		this.tags.put(tag,rating);
	}
	
	public void setNormalTags(Map<String, Double> new_tags){
		this.norm_tags = new_tags;
	}
	
	public Map<String, Double> getNormalTags(){
		return this.norm_tags;
	}
	
	public void addUserTags(String tag, Double rating){
		if(this.tags.get(tag)==null){
			this.tags.put(tag, rating);
		} else {
			this.tags.put(tag, this.tags.get(tag) + rating);
		}
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
	
	public Map<String, Double> getUserTags(){
		return this.tags;
	}
	
	public String toString(){
		return "User Tags: " + this.tags.toString() + " ";
	}
}
