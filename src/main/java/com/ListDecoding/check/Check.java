package com.ListDecoding.check;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * 过滤详单未解码文件
 * @author asus
 *
 */
public class Check {
	public static void main(String[] args) throws IOException {
//		args=new String[]{"/itf/qixin/cdr/mbl/811/20160313/","/output/fixcdr/201603/13/"};
		
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
		Set<String> nmset=new HashSet<>();
		Set<String> nmset1=new HashSet<>();
		Configuration conf =new Configuration();
		
		FileSystem fs =FileSystem.get(conf);
		FileStatus[] listsource = fs.listStatus(new Path(args[0]));
		
		FileStatus[] listgual = fs.listStatus(new Path(args[1]));
		for(FileStatus lsl :listgual){	
			if(lsl.isFile()){
			String fileName=lsl.getPath().getName();
			if(fileName.endsWith(".Decode.lzo")){
				nmset1.add(fileName.substring(0, lsl.getPath().getName().length()-11));	
			}else if(fileName.endsWith(".Decode.Error.lzo")){
				nmset1.add(fileName.substring(0, lsl.getPath().getName().length()-17));	
			}
			}else{
				FileStatus[] listStatus = fs.listStatus(lsl.getPath());
				for(FileStatus flst:listStatus){
					String fileName=flst.getPath().getName();
					if(fileName.endsWith(".Decode.lzo")){
						nmset1.add(fileName.substring(0, flst.getPath().getName().length()-11));	
					}else if(fileName.endsWith(".Decode.Error.lzo")){
						nmset1.add(fileName.substring(0, flst.getPath().getName().length()-17));	
					}
				}
			}
		}
		
		for(FileStatus lse :listsource){
			if(!nmset1.contains(lse.getPath().getName())){
				System.out.println(lse.getPath().toString()+"+--+"+bartDateFormat.format(new Date(lse.getModificationTime())));
			}
		}

	}

}
