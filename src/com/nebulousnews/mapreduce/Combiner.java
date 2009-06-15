package com.nebulousnews.mapreduce;


import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.io.Text;

public class Combiner implements Reducer<Text, Text, Text, Text>{

	public void reduce(Text key, Iterator<Text> values,
			OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		StringBuffer buffer = new StringBuffer();
		while(values.hasNext() ){
			buffer.append(values.next()+", ");
		}
		output.collect(key,new Text(buffer.substring(0, -2)));
	}

	@Override
	public void configure(JobConf arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
