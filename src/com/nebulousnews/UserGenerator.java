package com.nebulousnews;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import com.clearforest.calais.full.Entity;
import com.nebulousnews.feed.Article;
import com.nebulousnews.users.User;
/*
 * @author Jason
 * 
 */
public class UserGenerator {
	public static void main(String[] abs){
		try {
			FileInputStream underlyingStream = new FileInputStream("articles.news");
			ObjectInputStream serializer = new ObjectInputStream(underlyingStream);
			Article[] articles = (Article[]) serializer.readObject();
			Scanner user_profiles = new Scanner(new File("users.profiles"));
			//users.profiles is just a space delimited file that gives USER IDs, and all the tags they will automatically vote up on
			//UID tagid tagid tagid ...
			//nothing fancy
			//users_tags (tagid,(userid,userid,...))
			HashMap<String,ArrayList<String>> users_tags = new HashMap<String,ArrayList<String>>(); 
			ArrayList<User> users = new ArrayList<User>();
			while (user_profiles.hasNext()){
				Scanner user_line = new Scanner(user_profiles.nextLine());
				User user = new User();
				user.setUID(user_line.next());
				users.add(user);
				while (user_line.hasNext()){
					String next = user_line.next();
					if(users_tags.containsKey(next)){
						users_tags.get(next).add(user.getUID());
					} else {
						users_tags.put(next, new ArrayList<String>());
						users_tags.get(next).add(user.getUID());
					}
				}
			}
			for (User user: users){
				for (Article article: articles){
					ArrayList<ArrayList<Entity>> tags = article.getTags().getEntitiesList();
					for (ArrayList<Entity> tag: tags){
						Iterator<Entity> taggoth = tag.iterator();
						while (taggoth.hasNext()){
							Entity next = taggoth.next();
							System.out.println(next.getName());
							if(users_tags.get(next.getName()) != null &&
									users_tags.get(next.getName()).contains(user.getUID())){
								user.addUserTags(next.getName(), (double)next.getRelevance());
							}
						}
					}
				}
			}
			//then write the users to file
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		//import user framework
		//save users to file as ObjectWritable

	}
}
