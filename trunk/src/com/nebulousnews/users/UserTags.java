package com.nebulousnews.users;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.clearforest.calais.full.Entity;
import com.nebulousnews.feed.Article;

public class UserTags implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Double> tags;
	private Map<String, Double> norm_tags;
	private Double increment = 1.0;
	private Double decrement = -1.0;
	
	public UserTags(){
		tags = new HashMap<String, Double>();
	}
	public UserTags(String tag, Double rating){
		addUserTags(tag, rating);
	}
	
	public void setNormalTags(Map<String, Double> new_tags){
		this.norm_tags = new_tags;
	}
	
	public Map<String, Double> getNormalTags(){
		return this.norm_tags;
	}
	
	public void addUserTags(String tag, Double rating){
		tag = tag.toUpperCase();
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
	
	public double getArticleRanking(Article article) {
		Iterator<ArrayList<Entity>> tagsItr = article.getTags().getEntitiesList().iterator();
		double ranking = 0;
		while (tagsItr.hasNext()) {
			ArrayList<Entity> entity = tagsItr.next();
			if (entity.size() == 0) {
				continue;
			} else {
				Entity temp = entity.get(0);
				String name = temp.getName().toUpperCase();
				if (this.norm_tags.containsKey(name)) {
					ranking += this.norm_tags.get(name)*temp.getRelevance();
				}
			}
		}
		return ranking;
	}
	
	public String toString(){
		return "User Tags: " + this.tags.toString() + " ";
	}

}
