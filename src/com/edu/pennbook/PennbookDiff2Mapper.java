package com.edu.pennbook;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PennbookDiff2Mapper extends Mapper<LongWritable, Text, Text, Text>{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		String[] vals = value.toString().split("\t");
		String enc = vals[3] + "\t" + vals[2];
		context.write(new Text(vals[0]), new Text(enc));
	}
}
