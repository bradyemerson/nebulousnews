package com.nebulousnews.mapreduce;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobConfigurable;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

import com.nebulousnews.io.ObjectSerializableWritable;

public class ObjectInputFormat extends FileInputFormat<LongWritable, ObjectSerializableWritable> implements JobConfigurable{

	@Override
	public RecordReader<LongWritable, ObjectSerializableWritable> getRecordReader(
			InputSplit genericSplit, JobConf job, Reporter reporter) throws IOException {
		reporter.setStatus( genericSplit.toString() );
		return new ObjectRecordReader(job, (FileSplit) genericSplit);
	}

	@Override
	public void configure(JobConf arg0) {
		// TODO Auto-generated method stub
		
	}
	protected boolean isSplitable(FileSystem fs, Path file){
		return false;
	}
}
