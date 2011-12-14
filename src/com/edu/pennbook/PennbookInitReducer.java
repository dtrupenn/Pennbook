package com.edu.pennbook;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PennbookInitReducer extends Reducer<Text, Text, Text, Text>{
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
		StringBuffer sb = new StringBuffer();
		for(Text txt:values){
			String temp = txt.toString();
			sb.append(temp + ",");
		}
		context.write(key, new Text(sb.toString().substring(0, sb.length() - 1) + "\t1"));
	}
}
