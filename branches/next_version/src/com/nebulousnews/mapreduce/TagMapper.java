package com.nebulousnews.mapreduce;

import java.io.IOException;

import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.io.RowResult;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import com.nebulousnews.io.StandardSourceData;

public class TagMapper {
	class UseFile extends MapReduceBase implements 
	Mapper<LongWritable, StandardSourceData, LongWritable, Text>{
		
		public void map(LongWritable key, StandardSourceData value,
				OutputCollector<LongWritable, Text> output, Reporter reporter)
				throws IOException {
			
			
		}
	}
	class UseHbase extends MapReduceBase implements 
	Mapper<ImmutableBytesWritable, RowResult, LongWritable, Text>{
		
		public void map(ImmutableBytesWritable key, RowResult value,
				OutputCollector<LongWritable, Text> output, Reporter reporter)
				throws IOException {
			
			
		}
		
	}

}
