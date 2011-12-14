package com.edu.pennbook;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.gargoylesoftware.htmlunit.javascript.host.Text;

public class PennbookDriver {
	public static void main(String[] args) throws Exception{
		if(args.length != 2) {
			System.err.println("Usage: PennbookDriver <input path> <output path>");
			System.exit(-1);
		}
		//Creates job
		Job job = new Job();
		job.setJarByClass(PennbookDriver.class);
		
		//Sets the Input and output paths for files
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		//Sets Mapper adn reducer classes
		
		//Sets Mapper and reducer classes
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
	}
}
