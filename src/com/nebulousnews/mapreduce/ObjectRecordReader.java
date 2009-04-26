package com.nebulousnews.mapreduce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;

import com.nebulousnews.io.ObjectSerializableWritable;

public class ObjectRecordReader implements RecordReader<LongWritable, ObjectSerializableWritable>{
	private String			_filename;
	private ArrayList<ObjectSerializableWritable>   _objects;
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
			ObjectSerializableWritable object = (ObjectSerializableWritable) objectStream.readObject();
			while(object!=null){
				_objects.add(object);
				object = (ObjectSerializableWritable) objectStream.readObject();
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
	public ObjectSerializableWritable createValue() {
		// TODO Auto-generated method stub
		return new ObjectSerializableWritable();
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
	public boolean next(LongWritable position, ObjectSerializableWritable object)
			throws IOException {
		if (_objects.size()-1<=_currentObject){
			ObjectSerializableWritable obj = _objects.get((int) _currentObject);
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
