package com.nebulousnews.mapreduce;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.LongWritable;
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

import com.nebulousnews.io.ObjectSerializableWritable;
import com.nebulousnews.users.User;

/*
 * @author Jason
 * 
 */
public class TextMapRed {
	//input: all user objects from file
	//output (top_tag),"UserID{tag1=.40, tag2=1.0, tag3=.008, ..."
	public static JobConf _passer;
	public static class Map extends MapReduceBase implements 
			Mapper<LongWritable, Text, LongWritable, ObjectSerializableWritable> {
		public void map(LongWritable key, Text value, OutputCollector<LongWritable, ObjectSerializableWritable> 
				output, Reporter reporter) throws IOException {
			String line = value.toString();
			User user = new User();
			user.setUID(line.substring(0,line.indexOf('{')));
			String[] tag_line = line.substring(line.indexOf('{'),line.indexOf('}')).split(", ");
			String greatest_key = "";
			HashMap<String, Double> tags = new HashMap<String,Double>();// = (HashMap<String, Double>) user.getUserTags();
			HashMap<String, Double> normalized_tags = new HashMap<String,Double>();
			for(String tag: tag_line){
				String[] tag1 = tag.split("=");
				try{
					tags.put(tag1[0], Double.parseDouble(tag1[1]));
					user.addUserTags(tag1[0], Double.parseDouble(tag1[1]));
				}catch (ArrayIndexOutOfBoundsException e){
					tags.put(tag, 0.0);
					user.addUserTags(tag, 0.0);
					reporter.setStatus("\nWarning: user " + user.getUID() + " tag " + tag + " did not have value\n");
				}
			}
			for(String map_key: tags.keySet() ){
				if(greatest_key.equals("") || tags.get(map_key) > tags.get(greatest_key)){
					greatest_key = map_key;
				}
			}
			Double greatest_tag = tags.get(greatest_key);
			for(String map_key: tags.keySet() ){
				normalized_tags.put(map_key, tags.get(map_key)/greatest_tag);
			}
			user.setNormalTags(normalized_tags);
			output.collect(new LongWritable(0), new ObjectSerializableWritable(user));
		}


	}
	//I don't really need a reduce for this...
	//but I'll use one to write all my objects to file
	public static class Reduce extends MapReduceBase implements 
			Reducer<LongWritable, ObjectSerializableWritable, LongWritable, ObjectSerializableWritable> {
		 public void reduce(LongWritable key, Iterator<ObjectSerializableWritable > values, OutputCollector<LongWritable, ObjectSerializableWritable> 
		 		output, Reporter reporter) throws IOException {
				/*FileSystem hdfs = FileSystem.get(_passer);
			    
				Path path = new Path("/users/jschlesi/output_users");
				FSDataOutputStream oute = hdfs.append(path);
				ObjectOutputStream objectStream = new ObjectOutputStream(oute);
				while(values.hasNext()){
					User next = (User)values.next().get();
					objectStream.writeObject(next);
				}
				objectStream.close();
				oute.close();*/
			 while( values.hasNext()){
				 output.collect(key, values.next());
			 }
			//output.collect(new Text(user.getUID()),new ObjectSerializableWritable(user)); 
		 }
	}
	
	 public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(TextMapRed.class);
		conf.setJobName("nebulous news");       
		conf.setOutputKeyClass(LongWritable.class);
		conf.setOutputValueClass(ObjectSerializableWritable.class);   
		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Reduce.class);
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
		_passer = conf;
		JobClient.runJob(conf);
	 }

}
