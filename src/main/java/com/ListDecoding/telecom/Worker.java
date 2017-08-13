package com.ListDecoding.telecom;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import com.ListDecoding.tools.asn1ber_model.Asn1BerParser_ALL_MODEL;
import com.ListDecoding.tools.asn1ber_model.Asn1BerParser_FIX_CDR;
import com.ListDecoding.tools.asn1ber_model.Asn1BerParser_FIX_DDR;
import com.ListDecoding.tools.asn1ber_model.Asn1BerParser_FIX_MDR;
import com.ListDecoding.tools.asn1ber_model.Asn1BerParser_FIX_VDR;
import com.ListDecoding.tools.asn1ber_model.Asn1BerParser_OTH_CDR;
import com.ListDecoding.tools.asn1ber_model.Asn1BerParser_OTH_DDR;
import com.ListDecoding.tools.asn1ber_model.Asn1BerParser_OTH_MDR;
import com.ListDecoding.tools.asn1ber_model.Asn1BerParser_OTH_VDR;
import com.hadoop.compression.lzo.LzopCodec;

/**
 * 
 * @author Administrator
 *
 */
public class Worker implements Runnable {

	private static Queue<FilePair> items = null;// 用来存放每个文件的信息源路径,目的路径,是否覆盖,缓存队列,在initMulti中实例化

	private static Integer count = null; // 线程数

	private Configuration conf = new Configuration();
	private static int check = 0; // 文件个数
	private static long size = 0; // 文件大小
	public static Logger log = Logger.getLogger(Parser.class);

	/**
	 * 判断线程是否运行
	 * 
	 * @return
	 */
	public static boolean isRunning() {
		return items != null;
	}

	/**
	 * 判断文件可用?
	 * 
	 * @return
	 */
	public static boolean isAvaible() {
		if (isRunning()) {
			synchronized (items) {
				return items.size() > 0;
			}
		}
		return false;
	}

	/**
	 * 封装文件信息,
	 * 
	 * @param sour
	 * @param dest
	 * @param Cover
	 * @return
	 */
	public static boolean push(FileStatus sour, String dest, int Cover) {
		if (items == null)
			return false;
		if (sour == null)
			return false;
		if (dest == null)
			return false;
		synchronized (items) {
			// 文件个数
			check++;
			// 文件大小
			size += sour.getLen();

			// run()方法开始继续执行
			return items.add(new FilePair(sour, dest, Cover));
		}
	}

	/**
	 * 移除items中的文件信息,并返回new FilePair(sour, dest,Cover)
	 * 
	 * @return
	 */
	public static FilePair take() {
		if (items == null)
			return null;
		synchronized (items) {
			return items.poll();
		}
	}

	/**
	 * 初始化并启动多线程,读取文件逻辑
	 * 
	 * @param owner
	 * @param thread
	 */
	public static void initMulti(Worker owner, int thread) {
		if (thread > 0) {
			items = new LinkedList<FilePair>();
			count = thread;
			for (int i = 0; i < count; i++)
				// 启动多线程,每个线程逻辑一样
				new Thread(new Worker(owner)).start();
		}
	}

	/**
	 * 结束线程
	 */
	public static void stopMulti() {
		if (items == null)
			return;
		while (isAvaible())
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		items = null;

		if (count == null)
			return;

		while (count > 0)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	public static void stopItems() {
		if (items != null) {
			items.clear();
		}
		items = null;
	}

	private Worker owner = null;
	private byte buffer[];// 文件流数组
	private int offset = 0;// 全局buffer偏移量
	private int length = 0;// 单个文件大小
	private volatile int total = 0; // 文件个数
	private volatile int error = 0;// 跳过头记录失败记录数
	private volatile long bytes = 0;// 文件总大小?
	private volatile long traffic = 0;//
	private volatile long load = 0;//
	private volatile long perf = 0; // //数据解码时间?

	HashMap<String, Asn1BerParser_ALL_MODEL> models = new HashMap<String, Asn1BerParser_ALL_MODEL>();

	// getModel(paths.getName());在processFile中实例化
	Asn1BerParser_ALL_MODEL model;

	/**
	 * 从offset下标开始获取buffer值进行0xFF&运算,
	 * 
	 * @param value
	 * @return
	 */
	public int ubyte(byte value) {
		// -124还原回132
		return 0xFF & (int) value;
	}

	/**
	 * 从offset下标开始获取buffer值进行0xFF&运算,offset会+1
	 * 
	 * @param value
	 * @return
	 */
	public int next() {
		return ubyte(buffer[offset++]);
	}

	/**
	 * 获取当前字段长度信息,长度信息只有一位返回这一位,长度信息不止一位,则返回计算后的长度信息
	 * 
	 * @return
	 */
	public int pickSize() {
		int value = next();
		if (value < 0x80) // 长度信息只有一位返回这一位
			return value;
		return (int) pickInteger(value & 0x07);// &111得到长度信息的长度
	}

	//
	public String pickString(int length) {
		String result = new String(buffer, offset, length);
		offset += length;
		return result;
	}

	//
	public String pickValue(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(Integer.toHexString(next()));
		}
		return sb.toString();
	}

	/**
	 * 根据传入信息,计算出对应的值(长度值,字段信息) 包含处理文件信息中integer类型的字段的逻辑
	 * 
	 * @param size
	 *            文件长度信息的长度,几位组成 84 12 34 56 78 则是4位
	 * @return
	 */
	public long pickInteger(int size) {
		long result = 0;
		for (int i = 0; i < size; i++)
			result = result * 256 + next();
		return result;
	}

	/**
	 * 根据下标获取当前字段tag值,包含tag值16进制有两位时的处理逻辑
	 * 
	 * @return
	 */
	public int pickAns1Integer() {
		int result = 0;
		while (offset < buffer.length) {
			int value = next();
			// result * 128 + (value & 0x7F)01111111
			result = result * 0x80 + (value & 0x7F);
			if (value < 0x80)// 128 01111111
				break;
		}
		return result;
	}

	/**
	 * 根据文件名前7位获得对应 Asn1BerParser_*的实例化对象
	 * 
	 * @param s
	 * @return
	 */
	public Asn1BerParser_ALL_MODEL getModel(String s) {
		// String newname[] = s.split("_");
		// return models.get(newname[0].substring(0,3)+"_"+newname[1]);

		// FIX_CDR
		return models.get(s.substring(0, 7).toUpperCase());
	}

	/**
	 * 读取每条记录内容,,最主要的
	 * 
	 * @param limit
	 *            每条记录最后下标+1
	 * @param ps
	 * @throws IOException
	 */
	public void processRecord(int limit, OutputStream ps) throws IOException {
		while (offset < limit) {
			int value = next();

			int tag = pickAns1Integer();
			int size = pickSize();

			switch (value) {
			case 0x5F:
				Value v = model.get(tag);
				if (v != null) {
					v.count++;
					switch (v.type) {
					case integer:
						v.flag = true;
						v.size = pickInteger(size);
						continue;
					case string:
						v.flag = true;
						v.size = size;
						// buffer的offset开始,长度为size复制到v.data的0开始,长度为size
						System.arraycopy(buffer, offset, v.data, 0, size);
						offset += size;
						continue;
					default:
						break;
					}
				}
				break;
			}
		}
		model.export(ps);
	}

	/**
	 * 
	 * 读取文件内容
	 * 
	 * @param limit
	 *            文件内容最后下标
	 * @param ps
	 * @throws IOException
	 */
	public void processBody(int limit, OutputStream ps) throws IOException {
		while (offset < limit) {
			int value = next();//

			int old_offset = offset;//
			int tag = pickAns1Integer();//
			int size = pickSize();

			switch (value) {
			case 0x7F:
				switch (tag) {
				case 0x20:
					processRecord(offset + size, ps);
					continue;
				}
				continue;
			}
		}
	}

	/**
	 * 处理尾记录
	 * 
	 * @param limit
	 * @param ps
	 * @param path
	 * @throws IOException
	 */
	public void processTail(int limit, Path path) throws IOException {
		while (offset < limit) {
			int value = ubyte(buffer[offset++]);
			int old_offset = offset;
			int tag = pickAns1Integer();
			int size = pickSize();

			switch (value) {
			case 0x5F:
				switch (tag) {
				case 0x39:
					long s = pickInteger(size);
					// log.info(path+","+s);
					System.out.println("[LOG]" + path + "," + s);
					break;
				default:
					offset += size;
					continue;
				}
			}
		}

	}

	/**
	 * 
	 * 读取整个文件文件放进buffer
	 * 
	 * @param path
	 *            a/b/c.txt
	 * @param len
	 *            文件大小
	 * @return
	 */
	public boolean loadFile(Path path, long len) {
		offset = 0;
		length = (int) len;
		buffer = new byte[length];
		try {
			FileSystem fs = path.getFileSystem(conf);
			FSDataInputStream in = null;
			in = fs.open(path);
			IOUtils.readFully(in, buffer, 0, buffer.length);
			traffic += bytes;

			return bytes == length;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 跳过头文件的逻辑,文件大小等信息,不是头记录
	 * 
	 * @return
	 */
	public boolean skipHead() {
		if (offset != 0)
			return false;
		int value = next();
		if (value != 0x61)
			return false;
		value = next();
		// 说明value大于0x80
		if ((value & 0x80) == 0x80) {
			// 把0x80去掉,0x84余4
			value = (int) pickInteger(value & 0x7F);
		}
		value += offset;
		if (value != length)
			return false;
		return true;
	}

	/**
	 * 跳过头记录,读取文件内容,主要转换方法,数据解码流程
	 * 
	 * @param ps
	 *            .tmp输出流
	 * @param path
	 *            输出文件路径 +原名
	 * @return
	 * @throws IOException
	 */
	public boolean processData(OutputStream ps, Path path) {
		try {

			long b = System.currentTimeMillis();

			// 如果没有跳过文件信息记录,则记录错误
			if (!skipHead()) {
				error++;
				System.out.println("[ERROR]" + path.getName() + "," + new Date());
				return false;
			}
			bytes += length;
			while (offset < length) {

				// int value = ubyte(buffer[offset++]);
				int value = next();

				int old_offset = offset;
				int tag = pickAns1Integer();
				int size = pickSize();
				switch (value) {
				case 0x7F:
					switch (tag) {
					// 文件记录
					case 0x20:
						processBody(offset + size, ps);
						continue;
						// 头记录,跳过头记录
					case 0x21:
						offset += size;
						continue;
						// 尾记录
					case 0x22:
						processTail(offset + size, path);
						continue;
					}
					break;
				}
			}
			long e = System.currentTimeMillis();
			// 数据解码时间?
			perf += e - b;
		} catch (Exception e) {
			System.out.println("[ERROR]" + path.getName() + "," + new Date());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据文件名,确定输出状态,开始处理文件内容
	 * 
	 * @param sour
	 * @param dest
	 * @param Cover
	 * @throws IOException
	 */
	public void processFile(FileStatus sour, String dest, int Cover) throws Exception {

		Properties props = ReadProperties.ReadJarProperties();
		//
		Path paths = sour.getPath();
		long len = sour.getLen();
		// 根据文件名前7位获得对应 Asn1BerParser_*的实例化对象
		model = getModel(paths.getName());
		if (model == null) {
			// log.error("File name exception! "+paths.getName()+","+new
			// Date());
			System.out.println("[ERROR]File name exception! " + paths.getName() + "," + new Date());
			return;
		}
		Path pathout = null;
		Path patherror = null;
		if ("true".equals(props.getProperty("lzo"))) {
			pathout = new Path(dest + ".Decode.lzo");
			patherror = new Path(dest + ".Decode.Error.lzo");
		} else {
			pathout = new Path(dest + ".Decode");
			patherror = new Path(dest + ".Decode.Error");
		}
		total++;

		// 判断输出文件是否存在,如果存在----
		FileSystem fs = pathout.getFileSystem(conf);
		//

		if (fs.exists(pathout)) {
			// log.warn(dest);
			System.out.println("[WARN]" + pathout + " already exists!");
			if (Cover == 1) {
				fs.delete(pathout, false);
			} else
				return;
		}
		if (fs.exists(patherror)) {
			System.out.println("[WARN]" + patherror + " already exists!");
			return;
		}

		// 读取整个文件文件放进buffer
		loadFile(paths, len);// 此处有返回值,

		FileSystem fs1 = FileSystem.get(URI.create(dest + ".Decode.tmp"), conf);
		Path path = new Path(dest + ".Decode.tmp");

		PrintStream out;

		if ("true".equals(props.getProperty("lzo"))) {
			LzopCodec lz = new LzopCodec();
			lz.setConf(this.conf);
			out = new PrintStream(new BufferedOutputStream(lz.createOutputStream(fs1.create(path))));
		} else {
			out = new PrintStream(new BufferedOutputStream(fs1.create(path)));
		}

		// 数据读取,调用读取数据流程,
		if (!processData(out, paths)) {
			fs1.rename(path, new Path(pathout + ".Error"));
			// log.error("File content exception! "+paths.getName()+","+new
			// Date());
			out.close();
			return;
		}

		// 数据读取正确
		fs1 = path.getFileSystem(conf);
		fs1.rename(path, pathout);

		out.close();
	}

	/**
	 * 打印处理信息,结束
	 * 
	 * @param tick
	 */
	public void report(long tick) {
		DecimalFormat df = new DecimalFormat("#.###");
		// System.out.println("[INFO]Total "+check+" files
		// "+df.format(1e-6*size)+"MB in "+df.format(1e-3*tick)+"s, average
		// "+df.format(1e-3*size/tick)+"MBps\n");
		System.out.printf("[INFO]Total %d files %.3fMB in %.3fs, average %.3fMBps\n", check, 1e-6 * size, 1e-3 * tick,
				1e-3 * size / tick);
		System.out.println("[INFO]END!");
		// log.info("Total "+check+" files "+df.format(1e-6*size)+"MB in
		// "+df.format(1e-3*tick)+"s, average
		// "+df.format(1e-3*size/tick)+"MBps\n");
		// log.info("END!");
	}

	/**
	 * 在Asn1BerParser_ALL_MODEL中将fields和map赋值
	 * 
	 */
	public void initModels() {
		models.put("FIX_CDR", new Asn1BerParser_FIX_CDR());
		models.put("FIX_DDR", new Asn1BerParser_FIX_DDR());
		models.put("FIX_MDR", new Asn1BerParser_FIX_MDR());
		models.put("FIX_VDR", new Asn1BerParser_FIX_VDR());
		models.put("OTH_CDR", new Asn1BerParser_OTH_CDR());
		models.put("OTH_DDR", new Asn1BerParser_OTH_DDR());
		models.put("OTH_MDR", new Asn1BerParser_OTH_MDR());
		models.put("OTH_VDR", new Asn1BerParser_OTH_VDR());
	}

	public Worker(Worker owner) {
		this.owner = owner;
		initModels();
	}

	/**
	 * 
	 * 当pair不等于null时,才会继续
	 */
	public void run() {
		// 只要item不为空,就一直运行,
		while (isRunning()) {
			// 移除items中的文件信息,并返回new FilePair(sour, dest,Cover)
			FilePair pair = take();

			if (pair != null) {
				try {
					//
					processFile(pair.sour, pair.dest, pair.Cover);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		if (owner == null)
			return;
		owner.merge(this);
		synchronized (count) {
			count--;
		}
	}

	/**
	 * 合并全局文件属性
	 * 
	 * @param worker
	 */
	public void merge(Worker worker) {
		total += worker.total;
		error += worker.error;
		bytes += worker.bytes;
		traffic += worker.traffic;
		load += worker.load;
		perf += worker.perf;
	}

}
