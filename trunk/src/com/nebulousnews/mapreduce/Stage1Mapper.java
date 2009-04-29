package com.nebulousnews.mapreduce;

import java.io.*;
import java.io.BufferedReader;

import org.apache.hadoop.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapred.*;
import java.util.ArrayList;
import java.util.List;

import com.nebulousnews.users.*;

public class Stage1Mapper extends MapReduceBase 
	implements Mapper<ObjectWritable, ObjectWritable, ObjectWritable, ObjectWritable> {

	public void map(ObjectWritable key, ObjectWritable values,
			OutputCollector<ObjectWritable, ObjectWritable> output, Reporter reporter) throws IOException {
		List tempArray = new ArrayList();
		tempArray = (ArrayList)values.get();
		User tempUser = new User();
		tempUser = ((User)tempArray.get(1));
		
		System.out.println("The Temp User Record is: " + tempUser);
	}
}
