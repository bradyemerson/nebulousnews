package com.nebulousnews.io;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.nebulousnews.feed.Article;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.core.UnsupportedFormatException;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.parsers.FeedParser;

public class RSSImporter implements Importer {
	Article[] articles;
	public RSSImporter(String url) {
		ArrayList<Article> articlesList = new ArrayList<Article>();
		try {
			URL feed = new URL(url);
			ChannelIF channel = FeedParser.parse(new ChannelBuilder(), feed);
			
			for (Iterator<ItemIF> iter = channel.getItems().iterator(); iter.hasNext();) {
				ItemIF item = iter.next();
				Article temp = new Article(item.getTitle(), item.getLink().toString(), item.getDescription(), item.getDate());
				if (item.getGuid() != null && !"".equals(item.getGuid().toString())) {
					temp.setGUID(item.getGuid().toString());
				}
				
				articlesList.add(temp);
			}
			
			this.articles = articlesList.toArray(new Article[articlesList.size()]);
		} catch (MalformedURLException ex) {
			System.err.println(ex.getMessage());
		} catch (UnsupportedFormatException ex) {
			System.err.println(ex.getMessage());
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	@Override
	public Article[] getArticles() {
		return articles;
	}
	
}
