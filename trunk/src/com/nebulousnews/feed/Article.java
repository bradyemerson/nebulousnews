/**
 * 
 */
package com.nebulousnews.feed;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.clearforest.calais.full.Entities;

/**
 * @author Brady
 *
 */
public class Article implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title, link, description, guid;
	private Date pubdate;
	private Entities tags;
	private Map<String, String> enclosures;
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

	public String getCalaisTest() {
		StringBuilder sb = new StringBuilder();
		sb.append(title+" ");
		if (enclosures.containsKey("html")) {
			sb.append(enclosures.get("html"));
		} else {
			sb.append(description);
		}
		return sb.toString();
	}
	
	public Entities getTags() {
		return this.tags;
	}
	
	public void setTags(Entities tags) {
		this.tags = tags;
	}

	public void setEnclosures(Map<String, String> enclosures) {
		this.enclosures = enclosures;
	}

	public Map<String, String> getEnclosures() {
		return enclosures;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Title: "+this.title);
		if (this.tags != null) {
			sb.append(" | Tags: "+this.tags.toString());
		}
		return sb.toString();
	}
}
