package com.nebulousnews.io;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

/**
 * @author Jason Schlesinger
 *
 */
public class StandardInputFormat extends FileInputFormat<LongWritable, StandardSourceData>{

	/**
	 * Get the Record Reader for this Writable Type
	 * 
	 * @return An instance of StandardRecordReader 
	 */
	public RecordReader<LongWritable, StandardSourceData> getRecordReader(
			InputSplit genericSplit, JobConf conf, Reporter reporter) throws IOException {
		
		reporter.setStatus(genericSplit.toString());
		return new StandardRecordReader(conf, (FileSplit) genericSplit);
	}
	
	/**
	 * See if the record is splitable (it isn't) 
	 * 
	 * @return false
	 */
	protected boolean isSplitable(FileSystem fs, Path file){
		return false;
	}
	

}
