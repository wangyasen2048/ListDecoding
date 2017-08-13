package com.ListDecoding.telecom;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

	
	public static Properties ReadJarProperties(){
		InputStream in=Worker.class.getResourceAsStream("/decode.properties");	
		Properties props = new Properties(); 
		try{
		props.load(in);
		}catch(IOException ioe){
			System.out.println("[ERROR] Profile error!");
		}
		return props;
	}

}
