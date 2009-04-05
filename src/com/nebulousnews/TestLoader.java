package com.nebulousnews;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

import com.nebulousnews.feed.Article;

public class TestLoader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileInputStream underlyingStream = new FileInputStream("articles.news");
			ObjectInputStream serializer = new ObjectInputStream(underlyingStream);
			Article[] articles = (Article[]) serializer.readObject();
			System.out.println(Arrays.toString(articles));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
