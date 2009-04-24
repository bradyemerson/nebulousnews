package com.nebulousnews;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
			//UID|tagid|tagid|tagid|...
			//nothing fancy
			//users_tags (tagid,(userid,userid,...))
			HashMap<String,ArrayList<String>> users_tags = new HashMap<String,ArrayList<String>>(); 
			ArrayList<User> users = new ArrayList<User>();
			while (user_profiles.hasNextLine()){
				Scanner user_line = new Scanner(user_profiles.nextLine());
				user_line.useDelimiter("\\|");
				User user = new User();
				user.setUID(user_line.next());
				users.add(user);
				while (user_line.hasNext()){
					String next = user_line.next();
					if(users_tags.containsKey(user.getUID())){
						users_tags.get(user.getUID()).add(next);
					} else {
						users_tags.put(user.getUID(), new ArrayList<String>());
						users_tags.get(user.getUID()).add(next);
					}
				}
				//System.out.println(user.getUID() + " : " + users_tags.get(user.getUID()).toString() );
			}
			for (User user: users){
				for (Article article: articles){
					boolean breaker = false;
					ArrayList<ArrayList<Entity>> tags = article.getTags().getEntitiesList();
					for (ArrayList<Entity> tag: tags){
						Iterator<Entity> taggoth = tag.iterator();
						while (taggoth.hasNext()){
							Entity next = taggoth.next();
							//System.out.println(next.getName());
							//see if a tag is in the user's list
							//NOTE: Mabye we should have a cutoff on if the tag is added?
							if(users_tags.get(user.getUID()).contains(next.getName())){
								//print User Info
								System.out.println(user.getUID() + " : " + users_tags.get(user.getUID()).toString() + " added article: " + article.getTitle());
								//add all tags to user
								System.out.println("Top Tags added:");
								for (ArrayList<Entity> tagart: tags){
									Iterator<Entity> taggoth2 = tagart.iterator();
									while (taggoth2.hasNext()){
										Entity next_tag = taggoth2.next();
										user.addUserTags(next_tag.getId(), new Double(next_tag.getRelevance()));
										System.out.print(" " + next_tag.getName() + ":" + next_tag.getRelevance());
									}	
								}
								System.out.println();
								break;
							}
							if( breaker){
								break;
							}
						}
						
					}
				}
			}
			FileOutputStream fileout = new FileOutputStream("user_data");
			ObjectOutputStream userout = new ObjectOutputStream(fileout);
			for(User user: users){
				userout.writeObject(user);	
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
