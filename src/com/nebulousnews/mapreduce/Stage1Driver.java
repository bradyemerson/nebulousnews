package com.nebulousnews.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.OutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.filecache.DistributedCache;

public class Stage1Driver extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		JobConf conf = new JobConf(getConf(), Stage1Driver.class);
		conf.setJobName("stage1");
		
		conf.setOutputKeyClass(ObjectWritable.class);
		conf.setOutputValueClass(ObjectWritable.class);
		
		conf.setMapperClass(Stage1Mapper.class);
		conf.setCombinerClass(Stage1Reducer.class);
		conf.setReducerClass(Stage1Reducer.class);
		
		conf.setInputFormat(InputFormat.class);
		conf.setOutputFormat(OutputFormat.class);
		
		FileInputFormat.setInputPaths(conf, "/user/mjcervan/mike.ser");
		FileOutputFormat.setOutputPath(conf, new Path("/user/mjcervan/neboutput"));
		
		JobClient.runJob(conf);
		return 0;
	}
	
	public static void main(String [] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new Stage1Driver(), args);
		System.exit(res);
	}

}
