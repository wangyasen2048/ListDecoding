package com.ListDecoding.tools.asn1ber_model;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.hadoop.fs.FSDataOutputStream;

import com.ListDecoding.telecom.Value;
import com.ListDecoding.telecom.Value.Type;



public abstract class Asn1BerParser_ALL_MODEL {
	
	//
	private Value fields[];//value-->value.type,value.tag
	//tags[i], value-->value.type,value.tag
	private HashMap<Integer, Value> map = new HashMap<Integer, Value>();
	
	public abstract boolean isString(int s);

	protected int fix(int key) { return key ; }
	
	//初始化,根据tag值判断当前字段类型
	public void init (Integer tags[]) {
		fields = new Value[tags.length];
		for (int i = 0; i < fields.length; i++) {
			int tag = tags[i];
			Value value = new Value();
			value.type = isString(tag) ? Type.string : Type.integer;
			value.tag = tags[i];
			fields[i] = value;
			map.put(tags[i], value);
		}
	}
	
	/**
	 * 根据tag值获得value对象,文件内容
	 * @param tag
	 * @return
	 */
	public Value get(int tag) {
		int k = fix(tag);
		if (k < 0) return null;
		return map.get(k);
	}
	/**
	 * 每条记录解码后导入输出流?
	 * @param ps
	 * @throws IOException
	 */
	public void export(OutputStream ps) throws IOException {
		for (int i = 0; i < fields.length; i++) {
			Value value = fields[i];
			if (value.flag) {
				switch(value.type) {
					case integer: {
						if (i > 0)
							ps.write("|".getBytes());
						ps.write(String.valueOf(value.size).getBytes());
						break;
					}
					case string: {
						if (i > 0)
							ps.write("|".getBytes());
						ps.write(value.data, 0, (int) value.size);
						break;
					}
					default:
						continue;
				}
				//每个字段的标志
				value.flag = false;
			} else
				if (i > 0)
					//???
					ps.write(0x7c);
		}

		ps.write("\n".getBytes());
	}
	
	/**
	 * 没调用?
	 */
	public void report() {
	//	System.out.printf("%s:\n", this.getClass().getSimpleName());
		for (int i = 0; i < fields.length; i++) {
			System.out.printf("%d-%d\n", fields[i].tag, fields[i].count);
		}
	}
	
}
