package com.nebulousnews;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
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
public class UserTextGenerator {
	public static void main(String[] abs){
		try {
			FileInputStream underlyingStream = new FileInputStream("articles.big.news");
			ObjectInputStream serializer = new ObjectInputStream(underlyingStream);
			Article[] articles1 = (Article[]) serializer.readObject();
			FileInputStream underlyingStream2 = new FileInputStream("articles.news");
			ObjectInputStream serializer2 = new ObjectInputStream(underlyingStream2);
			Article[] articles2 = (Article[]) serializer2.readObject();
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
				user.setUID(Integer.parseInt(user_line.next())+"");
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
				System.out.print(user.getUID());
				user = loadArticles(articles1,user,users_tags);
				user = loadArticles(articles2,user,users_tags);
				System.out.println(": " + user.getUserTags().toString());
			}
			FileOutputStream fileout = new FileOutputStream("user_text");
			//ObjectOutputStream userout = new ObjectOutputStream(fileout);
			PrintStream userout = new PrintStream(fileout);
			for(User user: users){
				userout.print(user.getUID()+user.getUserTags()+"\n");
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
	public static User loadArticles(Article[] articles,User user,HashMap<String,ArrayList<String>> users_tags){
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
					for(String test_tag:users_tags.get(user.getUID())){
						if(next.getName().toUpperCase().contains(test_tag.toUpperCase())){
								//users_tags.get(user.getUID().toUpperCase()).contains(next.getName())
								//|| users_tags.get(user.getUID().toUpperCase()).equals(next.getName())){
							//print User Info
							//System.out.println(" added article: " + article.getTitle());
							//add all tags to user
							//System.out.println("		Top Tags added:");
							for (ArrayList<Entity> tagart: tags){
							//ArrayList<Entity> tagart = tags.get(0);
								Iterator<Entity> taggoth2 = tagart.iterator();
								while (taggoth2.hasNext()){
									Entity next_tag = taggoth2.next();
									Double rax = new Double(next_tag.getRelevance());
									String pid = next_tag.getName();
									user.addUserTags(pid, rax);
									//System.out.print(" " + next_tag.getName() + ":" + next_tag.getRelevance());
								}	
							}
							breaker=true;
							//System.out.println();
							break;
						}
						if( breaker){
							break;
						}
					}if( breaker){
						break;
					}
				}
				
			}
		}
		return user;
	}
}
