/**
 * 
 */
package com.nebulousnews.feed;

import java.util.Date;

/**
 * @author Brady
 *
 */
public class Article {
	private String title, link, description, guid;
	private Date pubdate;
	public Article(String title, String link, String description, Date pubdate) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.pubdate = pubdate;
	}
	
	public String getGUID() {
		return guid;
	}
	
	public void setGUID(String guid) {
		this.guid = guid;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getLink() {
		return link;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Date getPubdate() {
		return pubdate;
	}
	
	
}
