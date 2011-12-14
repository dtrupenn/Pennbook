package com.edu.pennbook;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PennbookDiff1Reducer extends Reducer<Text, Text, Text, Text>{
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
		for(Text txt:values){
			String[] vals = txt.toString().split("\t");
			Double diff = Math.abs(Double.parseDouble(vals[0]) - Double.parseDouble(vals[1]));
			context.write(key, new Text(diff.toString()));
		}
	}
}
