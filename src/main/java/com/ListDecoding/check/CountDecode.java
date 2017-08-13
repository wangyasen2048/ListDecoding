package com.ListDecoding.check;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * 清除重复解码文件
 * @author asus
 *
 */
public class CountDecode {
	public static void main(String[] args) throws IOException {
		Set<String> source_files=new HashSet<>();
		Set<String> upload_files=new HashSet<>();
		Path source = new Path(args[0]);
        Configuration conf =new Configuration();
        FileSystem fs =FileSystem.get(conf);
        FileStatus[] source_Pair = fs.listStatus(source);
        for(FileStatus fssp:source_Pair){
        	if(fssp.isFile()){
        	source_files.add(fssp.getPath().getName());
        	}else{
        		FileStatus[] upload_Pair = fs.listStatus(fssp.getPath());
        		for(FileStatus fsup:upload_Pair){
                	upload_files.add(fsup.getPath().getName());        			
        		}
        	}
        	
        }
        for(String source_file:source_files){
        	if(upload_files.contains(source_file)){
        		System.out.println(source_file);
        	}
        }
        
	}
	
	

}
