package com.ListDecoding.telecom;

import org.apache.hadoop.fs.FileStatus;
/**
 * 存放文件源路径,目的路径,是否覆盖
 * @author Administrator
 *
 */
public class FilePair {

	public FileStatus sour;
	public String dest;
	public int Cover; 
	FilePair(FileStatus s, String d,int c) {
		sour = s;
		dest = d;
		Cover = c;
	}
	
}
