package com.nebulousnews.io;

import com.clearforest.calais.full.CalaisJSONIf;
import com.clearforest.calais.full.Entities;
import com.nebulousnews.feed.Article;

public class Tagger {
	private static final String API_KEY = "2qtmad4vfhtzb2qmbmqymy3j";
	private static CalaisJSONIf jsonIf = new CalaisJSONIf(Tagger.API_KEY, "60", "false", "", "true");
	public static void tagArticles(Article[] articles) {
		for(Article article : articles) {
			Tagger.tagArticle(article);
		}
	}
	
	public static void tagArticle(Article article) {
		//System.out.println("sending "+article.getCalaisTest());
		jsonIf.getFullJSON(article.getCalaisTest());
		Entities entities = jsonIf.getEntities();
		article.setTags(entities);
	}
}
