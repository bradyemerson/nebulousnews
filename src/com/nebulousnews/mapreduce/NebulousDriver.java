package com.nebulousnews.mapreduce;

import java.io.IOException;
import java.util.Iterator;

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
import org.apache.hadoop.mapred.TextOutputFormat;

import com.nebulousnews.io.StandardInputFormat;
import com.nebulousnews.io.StandardSourceData;

/**
 * 
 * @author Jason Schlesinger
 *
 */
public class NebulousDriver {
	
	public static class Reduce extends MapReduceBase implements 
	Reducer<LongWritable, Text, LongWritable, Text>{

		public void reduce(LongWritable key,
				Iterator<Text> value,
				OutputCollector<LongWritable, Text> output,
				Reporter reporter) throws IOException {
			//SINGLE VALUE IDENTITY
			output.collect(key, value.next());
		}
		
	}
	
	/**
	 * 
	 * @param args unused
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(NebulousDriver.class);
		conf.setJobName("NebulousNews");       
		conf.setOutputKeyClass(LongWritable.class);
		conf.setOutputValueClass(Text.class);   
		conf.setMapperClass(TagMapper.class);
		//conf.setCombinerClass(Reduce.class);
		//FIXME: DEBUG START
		//conf.set("mapred.job.tracker","local");
		//conf.set("fs.default.name","local");
		//FIXME: DEBUG END
		conf.setReducerClass(Reduce.class); 
		conf.setInputFormat(StandardInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);    
		FileInputFormat.setInputPaths(conf, new Path("/user/training/sourceDataSample"));
		FileSystem dfs = DistributedFileSystem.get(conf);
		String filename = "output_test";
		Path output_file = new Path(filename);
		if(dfs.exists(output_file)){
			dfs.delete(output_file,true);
		}
		FileOutputFormat.setOutputPath(conf, output_file);    
		JobClient.runJob(conf);
	 }
}
