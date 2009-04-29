package com.nebulousnews.mapreduce;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class Stage1Reducer extends MapReduceBase implements 
			Reducer <ObjectWritable, ObjectWritable, ObjectWritable, ObjectWritable> {
				public void reduce(ObjectWritable key, Iterator<ObjectWritable> values,
						OutputCollector<ObjectWritable, ObjectWritable> output,
						Reporter reporter) throws IOException {
					
			output.collect(key, new ObjectWritable());
		}

	}


