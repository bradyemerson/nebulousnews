/**
 * 
 */
package com.nebulousnews;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.*;

import com.clearforest.calais.full.Entity;
import com.nebulousnews.feed.Article;
import com.nebulousnews.io.ObjectSerializableWritable;
import com.nebulousnews.users.User;

/**
 * @author Brady Emerson
 *
 */
public class CmdLine {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Article[] articles = null;
		User[] users = null;
		try {
			FileInputStream underlyingStream = new FileInputStream("articles.big.news");
			ObjectInputStream serializer = new ObjectInputStream(underlyingStream);
			articles = (Article[]) serializer.readObject();
			serializer.close();
			underlyingStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*TreeSet<String> tags = new TreeSet<String>();
		
		for(Article article : articles) {
			Iterator<ArrayList<Entity>> tagsItr = article.getTags().getEntitiesList().iterator();
			double ranking = 0;
			while (tagsItr.hasNext()) {
				ArrayList<Entity> entity = tagsItr.next();
				if (entity.size() == 0) {
					continue;
				} else {
					tags.add(entity.get(0).getName().toUpperCase());
				}
			}
		}
		
		try {
			FileWriter fw = new FileWriter("tags.list");
			BufferedWriter out = new BufferedWriter(fw);
			for(String tag : tags) {
				out.write(tag+"\n");
			}
			out.close();
			System.exit(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		try {
			//Scanner user_profiles = new Scanner(new File("users.profiles"));
			//users.profiles is just a space delimited file that gives USER IDs, and all the tags they will automatically vote up on
			//UID|tagid|tagid|tagid|...
			//nothing fancy
			//users_tags (tagid,(userid,userid,...))
			//HashMap<String,ArrayList<String>> users_tags = new HashMap<String,ArrayList<String>>(); 
			ArrayList<User> usersHolder = new ArrayList<User>();
			try {
				FileInputStream underlyingStream = new FileInputStream("user_normalized_data");
				ObjectInputStream serializer = new ObjectInputStream(underlyingStream);
				while(true){
					usersHolder.add((User)serializer.readObject());
				}
			} catch (Exception e) { 
				//this is an awful way to do this
			}
			/*while (user_profiles.hasNextLine()){
				Scanner user_line = new Scanner(user_profiles.nextLine());
				user_line.useDelimiter("\\|");
				User user = new User();
				user.setUID(user_line.next());
				while (user_line.hasNext()){
					String next = user_line.next();
					user.addUserTags(next, 1.0);
				}
				usersHolder.add(user);
			}*/
			users = usersHolder.toArray(new User[usersHolder.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int userNumber = 72;
		System.out.println(users[userNumber].getNormalTags());
		int articleNumber = 0;
		
		TreeSet<UserArticle> userArticles = new TreeSet<UserArticle>();
		
		while (userNumber >= 0) {
			Article article = articles[articleNumber];
			Double ranking = users[userNumber].getArticleRanking(article);
			
			userArticles.add(new UserArticle(article.getTitle(), ranking.doubleValue()));
			articleNumber++;
			if (articleNumber == articles.length) {
				break;
			}
		}
		
		int i = 0;
		System.out.println(userArticles.size());
		for(UserArticle article : userArticles) {
			System.out.println(article.getTitle()+": "+article.getRanking());
			i++;
			if (i > 20) {
				break;
			}
		}
		
		System.out.println("Process took: "+(System.currentTimeMillis()-start)+"ms");
	}
}

class UserArticle implements Comparable {
	private String title;
	private double ranking;
	public UserArticle(String title, double ranking) {
		this.title = title;
		this.ranking = ranking;
	}
	
	public int compareTo(Object arg0) {
		UserArticle compare = (UserArticle)arg0;
		if (this.ranking > compare.getRanking()) {
			return -1;
		} else if (this.ranking < compare.getRanking()) {
			return 1;
		} else if (this.ranking == compare.getRanking()) {
			return this.title.compareTo(compare.getTitle());
		}
		return 0;
	}
	
	public double getRanking() {
		return ranking;
	}
	
	public String getTitle() {
		return title;
	}
}