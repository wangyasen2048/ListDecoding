package com.ListDecoding.telecom;
/**
 * 存放字段内容,每个字段的类型,大小等
 * @author Administrator
 *
 */
public class Value {
	public static enum Type {
		hide, integer, string
	}
	
	public Type type = Type.hide;
	
	//每个字段的标志
	public boolean flag = false;
	
	public int  tag = 0;      //tag值
	public long count = 0;//字段数?
	
	
	//存储String类型时的字段.
	public byte data[] = new byte[1024];
	
	//对于integer类型,就是转换后的值
	//对于String类型,则是本字段长度
	public long size = 0;
	
}

