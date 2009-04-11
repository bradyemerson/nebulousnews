package com.nebulousnews.mapreduce;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.examples.WordCount;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import com.nebulousnews.users.User;

public class MapRed {
	//input: all user objects from file
	//output (top_tag),"UserID{tag1=.40, tag2=1.0, tag3=.008, ..."
	public static class Map extends MapReduceBase implements 
			Mapper<LongWritable, ObjectWritable, Text, Text> {
		public void map(LongWritable key, ObjectWritable value, OutputCollector<Text, Text> 
				output, Reporter reporter) throws IOException {
			User user = (User)value.get();
			String greatest_key = "";
			HashMap<String, Double> tags = (HashMap<String, Double>) user.getUserTags();
			HashMap<String, Double> normalized_tags = new HashMap<String,Double>();
			for(String map_key: tags.keySet() ){
				if(greatest_key.equals("") || tags.get(map_key) > tags.get(greatest_key)){
					greatest_key = map_key;
				}
			}
			Double greatest_tag = tags.get(greatest_key);
			for(String map_key: tags.keySet() ){
				normalized_tags.put(map_key, tags.get(map_key)/greatest_tag);
			}
			output.collect(new Text(user.getUID()), new Text( normalized_tags.toString()));
		}

	}
	public static class Reduce extends MapReduceBase implements 
			Reducer<Text, Text, Text, Text> {
		 public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> 
		 		output, Reporter reporter) throws IOException {
			 String all_tags = "";
			 while( values.hasNext()){
				 String tagset = values.next().toString();
				 all_tags += tagset.substring(tagset.length()-2);				 
			 }
			 all_tags += "}";
			output.collect(key, new Text(all_tags)); 
		 }
	}
	
	 public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(WordCount.class);
		conf.setJobName("nebulous news");       
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);   
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class); 
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);    
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileSystem dfs = DistributedFileSystem.get(conf);
		String filename = "normalized_users";
		Path output_file = new Path(filename);
		if(dfs.exists(output_file)){
			dfs.delete(output_file,true);
		}
		FileOutputFormat.setOutputPath(conf, output_file);    
		JobClient.runJob(conf);
	 }

}
