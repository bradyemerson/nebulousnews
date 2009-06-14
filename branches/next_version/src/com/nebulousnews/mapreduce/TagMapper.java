package com.nebulousnews.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import com.nebulousnews.io.StandardSourceData;

public class TagMapper extends MapReduceBase implements 
Mapper<LongWritable, StandardSourceData, LongWritable, Text>{

	public void map(LongWritable key, StandardSourceData value,
			OutputCollector<LongWritable, Text> output, Reporter reporter)
			throws IOException {
		
		
	}

}
