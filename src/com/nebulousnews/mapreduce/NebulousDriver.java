package com.nebulousnews.mapreduce;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.tools.Tool;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.mapred.TableMap;
import org.apache.hadoop.hbase.mapred.TableMapReduceUtil;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;

import com.nebulousnews.conf.ConfigReader;
import com.nebulousnews.io.StandardInputFormat;

/**
 * 
 * @author Jason Schlesinger
 *
 */
public class NebulousDriver implements Tool{
	
	/**
	 * 
	 * @param args unused
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ConfigReader config = new ConfigReader();
		String filesS = config.getOption("uses-files");
		String hbaseS = config.getOption("uses-hbase");
		boolean files = ( filesS.equals("true") || filesS.equals("yes"));
		boolean hbase = ( hbaseS.equals("true") || hbaseS.equals("yes"));
		JobConf conf = new JobConf(NebulousDriver.class);
		conf.setJobName("NebulousTagger");       
		conf.setOutputKeyClass(Text.class);  //Tags
		conf.setOutputValueClass(Text.class);//UID   
		conf.setMapperClass(TagMapper.UseFile.class);
		conf.setCombinerClass(Combiner.class);
		conf.setReducerClass(Indexer.class); 
		conf.setOutputFormat(TextOutputFormat.class);
		FileSystem dfs = DistributedFileSystem.get(conf);
		String filename = "output_test";
		Path output_file = new Path(filename);
		if(dfs.exists(output_file)){
			dfs.delete(output_file,true);
		}
		FileOutputFormat.setOutputPath(conf, output_file);
		if (files){
			conf.setInputFormat(StandardInputFormat.class);
			FileInputFormat.setInputPaths(conf, new Path(config.getOption("data-folder")));
			JobClient.runJob(conf);
		}
		if (hbase){
			int res = ToolRunner.run(new Configuration(), (org.apache.hadoop.util.Tool) new NebulousDriver(), args);
			System.exit(res);
		}
		
	 }

	@Override
	public Set<SourceVersion> getSourceVersions() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method is called when a HBase job is run.  
	 * 
	 * @param args Strings passed from main()
	 */
	@SuppressWarnings("unchecked")
	public int run(String[] args) throws Exception {
		ConfigReader config = new ConfigReader();
		JobConf jobConf = new JobConf();
		jobConf.setJobName("NebulousTaggerHbase");
		TableMapReduceUtil.initTableMapJob( config.getOption("hbase-table"),
				"data metadata", (Class<? extends TableMap>) TagMapper.UseHbase.class, Text.class, Text.class, jobConf);
		jobConf.setMapOutputValueClass(Text.class);
		jobConf.setReducerClass(Indexer.class);
		FileSystem dfs = DistributedFileSystem.get(jobConf);
		String filename = "output_test";
		Path output_file = new Path(filename);
		if(dfs.exists(output_file)){
			dfs.delete(output_file,true);
		}
		FileOutputFormat.setOutputPath(jobConf, output_file);
		JobClient.runJob(jobConf);
		return 0;
	}

	/**
	 * Inherited from tool, just a wrapper method for the other run
	 */
	public int run(InputStream in, OutputStream out, OutputStream err,
			String... args) {
		try {
			return this.run(args);
		} catch (Exception e) {
			return 1;
		}
	}
}
