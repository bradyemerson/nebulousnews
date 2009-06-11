package com.nebulousnews.io;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;

public class StandardRecordReader implements RecordReader<LongWritable, StandardSourceData>{
	private long start;
	private long end;
	private long pos;
	private DataInputStream in;
	
	
	public StandardRecordReader(JobConf conf, FileSplit split){
		pos = 0;
		start = split.getStart();
		end = start + split.getLength();
		
		try {
			FileSystem fs = FileSystem.get(conf);
			FSDataInputStream fileIn = fs.open(split.getPath());
			in = new DataInputStream(fileIn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the DataInputStream, keeping things neat and tity
	 */
	public void close() throws IOException {
		if (in != null){
			in.close();
		}
	}

	/**
	 * Creates a new LongWritable instance
	 * 
	 * @return new LongWritable()
	 */
	public LongWritable createKey() {
		return new LongWritable();
	}

	/**
	 * Returns null - StandardSourceData cannot be used on its own
	 * 
	 * @return null
	 */
	public StandardSourceData createValue() {
		return new StandardSourceData();
	}

	/**
	 * Since this record isn't splitable, it's always zero
	 * 
	 * @return zero
	 */
	public long getPos() throws IOException {
		return pos;
	}
	
	/**
	 * Since this record isn't splitable, it's always zero
	 * 
	 * @return zero
	 */
	public float getProgress() throws IOException {
		return 0;
	}

	/**
	 * Reads in the buffered section of the file
	 * 
	 * @return false if no more records, true if record advanced
	 */
	public boolean next(LongWritable key, StandardSourceData value)
			throws IOException {
		key.set(pos);
		int verify;
		byte[] input;
		BufferedInputStream inputStream;
		try {
			int length = WritableUtils.readVInt(in);
			input = new byte[length];
			pos += 4 + length; // it's 4 bytes/int... right?
			//TODO: Verify that pos is advanced this much for every value
			inputStream = new BufferedInputStream(in, length);
			/*verify = in.read(input, 0, length);
			if (verify != length){
				return false;
			}*/
			value.readFields((DataInput)inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
