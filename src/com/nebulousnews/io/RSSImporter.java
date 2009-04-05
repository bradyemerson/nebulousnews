package com.nebulousnews.io;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.nebulousnews.feed.Article;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.core.UnsupportedFormatException;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.parsers.FeedParser;

public class RSSImporter implements Importer {
	ArrayList<Article> articles;
	String url;
	public RSSImporter(String url) {
		this.url = url;
		articles = new ArrayList<Article>();
	}

	@Override
	public Article[] getArticles() {
		return articles.toArray(new Article[articles.size()]);
	}
	
	public void loadArticles() {
		try {
			URL feed = new URL(this.url);
			ChannelIF channel = FeedParser.parse(new ChannelBuilder(), feed);
			
			for (Iterator<ItemIF> iter = channel.getItems().iterator(); iter.hasNext();) {
				ItemIF item = iter.next();
				Article temp = new Article(item.getTitle(), item.getLink().toString(), item.getDescription(), item.getDate());
				if (item.getGuid() != null && !"".equals(item.getGuid().toString())) {
					temp.setGUID(item.getGuid().toString());
				}
				Map<String, String> enclosures = new HashMap<String, String>();
				if (!("".equals(item.getElementValue("media:text")))) {
					enclosures.put("html", item.getElementValue("media:text"));
				}
				String[] mediaContentValues = item.getElementValues("media:content", new String[] {});
				String[] mediaContentAttributes = item.getAttributeValues("media:content", new String[] {});
				if (mediaContentValues != null && mediaContentValues.length > 0) {
					
				}
				temp.setEnclosures(enclosures);
				this.articles.add(temp);
			}
		} catch (MalformedURLException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		} catch (UnsupportedFormatException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
}
