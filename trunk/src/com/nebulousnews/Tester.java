package com.nebulousnews;

import java.util.Arrays;

import com.nebulousnews.feed.Article;
import com.nebulousnews.io.RSSImporter;
import com.nebulousnews.io.Tagger;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = "file:///C:/Documents%20and%20Settings/Brady/eclipse_workspace/Nebulous%20News/topnews%20sample.xml";
		RSSImporter importer = new RSSImporter(path);
		importer.loadArticles();
		Article[] articles = importer.getArticles();
		if (articles != null && articles.length > 0) {
			Tagger.tagArticles(articles);
			System.out.println(Arrays.toString(articles));
		} else {
			System.err.println("Failed to load articles");
		}
	}

}
