package com.nebulousnews;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import com.nebulousnews.feed.Article;
import com.nebulousnews.io.RSSImporter;
import com.nebulousnews.io.Tagger;

public class Tester {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		String path = "file:///C:/Documents%20and%20Settings/Brady/eclipse_workspace/Nebulous%20News/articles.xml";
		RSSImporter importer = new RSSImporter(path);
		importer.loadArticles();
		Article[] articles = importer.getArticles();
		if (articles != null && articles.length > 0) {
			System.out.println("tagging "+articles.length+" articles");
			Tagger.tagArticles(articles);
			System.out.println(Arrays.toString(articles));
		} else {
			System.err.println("Failed to load articles");
		}
		
		try {
			//C:\\Documents%20and%20Settings\\Brady\\eclipse_workspace\\Nebulous%20News\\
			FileOutputStream underlyingStream = new FileOutputStream("articles.big.news");
			ObjectOutputStream serializer = new ObjectOutputStream(underlyingStream);
			serializer.writeObject(articles);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
