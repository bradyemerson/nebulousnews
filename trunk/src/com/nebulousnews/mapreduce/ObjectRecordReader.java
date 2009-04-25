package com.nebulousnews.mapreduce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;

public class ObjectRecordReader implements RecordReader<LongWritable, ObjectWritable>{
	private String			_filename;
	private ArrayList<ObjectWritable>   _objects;
	private long             _currentObject;
	@SuppressWarnings("unused")
	private JobConf			_conf;	
	public ObjectRecordReader(JobConf conf, FileSplit split) throws IOException{
		//set the configuration
		_conf = conf;
		
		//find the fliename
		_filename = split.getPath().toString();
		
		//this is the first object
		_currentObject = 0;
		
		//get the objects out
		Configuration config = new Configuration();
		FileSystem hdfs = FileSystem.get(config);
		Path path = new Path(_filename);
		FSDataInputStream input = hdfs.open(path);
		ObjectInputStream objectStream = new ObjectInputStream(input);
		try {
			ObjectWritable object = (ObjectWritable) objectStream.readObject();
			while(object!=null){
				_objects.add(object);
				object = (ObjectWritable) objectStream.readObject();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		// directory
		String directory = UsulFilePath.directory( _filename );
		
		// basename
		String basename = UsulFilePath.base( _filename );
		
		// create the directory to hold the sub files
		Path dirPath = new Path( directory + basename + "_output" );
		hdfs.mkdirs( dirPath );
		*/
	}
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LongWritable createKey() {
		// TODO Auto-generated method stub
		return new LongWritable();
	}

	@Override
	public ObjectWritable createValue() {
		// TODO Auto-generated method stub
		return new ObjectWritable();
	}

	@Override
	public long getPos() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getProgress() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean next(LongWritable position, ObjectWritable object)
			throws IOException {
		if (_objects.size()-1<=_currentObject){
			ObjectWritable obj = _objects.get((int) _currentObject);
			_currentObject ++;
			position.set(_currentObject);
			object.set(obj);
			return true;
		} else{
		// TODO Auto-generated method stub
		return false;
		}
	}

}
