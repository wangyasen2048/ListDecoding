package com.ListDecoding.telecom;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;



/**
 * 主类
 * @author Administrator
 *
 */
public class Parser extends Worker {
	
	private Configuration conf = new Configuration();
	private FileSystem fs = null;
	private File sour;
	private File dest;
	private int thread;
	private int count;
	private boolean add = true;
	public static Logger log = Logger.getLogger(Parser.class);
	/**
	 * 判断文件时间是否符合提供参数要求
	 * @param StartTime
	 * @param EndTime
	 * @param time
	 * @return
	 */
	public boolean inTime(long StartTime, long EndTime, long time) {
	
		return ((StartTime == 0) || (time >= StartTime)) && ((EndTime == 0) || (time < EndTime));
		
	}
	/**
	 * 对文件目录进行递归遍历
	 * @param sour
	 * @param dest
	 * @param StartTime
	 * @param EndTime
	 * @param Cover
	 * @throws IOException
	 */
	public void processPath(FileStatus sour, String dest,long StartTime,long EndTime,int Cover,String file_data) throws Exception {
	
		
		//如果是文件夹,遍历后执行下面那个
		if(sour.isDirectory())
		{
			fs = sour.getPath().getFileSystem(conf);
			FileStatus[] fileStatus = fs.listStatus(sour.getPath());
			for(int i = 0 ;i< fileStatus.length ;i++)
			{
				//遍历文件夹时判断 文件夹内容名字是时间文件夹还是文件
					Pattern pattern = Pattern.compile("\\d{8}|\\d{4}-{1}\\d{2}-{1}\\d{2}");
					Matcher matcher = pattern.matcher(fileStatus[i].getPath().getName());
					boolean b= matcher.matches();
					if(file_data != null && fileStatus[i].isFile())
					{
						processPath(fileStatus[i], dest+"/"+fileStatus[i].getPath().getName(),StartTime,EndTime,Cover,file_data);
					}
					if(!b && file_data == null && fileStatus[i].isFile())
					{
						 processPath(fileStatus[i], dest+"/"+fileStatus[i].getPath().getName(),StartTime,EndTime,Cover,file_data);
					}
			}
			return;
		}
		//如果是文件,判断是否符合条件
		if (sour.isFile()) {
			SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
			Date date = new Date(sour.getModificationTime());
			long time = Long.valueOf(bartDateFormat.format(date));
			
			//判断时间是否符合
			if(inTime(StartTime,EndTime,time))
			{
				//文件名
				String name = new Path(dest).getName();
				//保留到 /hdl/cdr/fix/cls/data/tm/$province/
				String patha = dest.substring(0, dest.length()-name.length());
//				if(StartTime != 0)
//				{
//					String s = time+"";	
//					dest = patha+s.substring(0, 6)+"/"+s.substring(6, 8)+"/"+name;
//				}
				//file_data 20151212
				
				//以输入文件路径时间为先,为空的话判断文件最后修改时间,,,用来确定输出路径
				if(file_data != null)
				{
//					String s [] = file_data.split("-");
//					dest = patha+s[0]+s[1]+"/"+s[2]+"/"+name;
					//输出   年月/日/原名
					dest = patha +file_data.substring(0, 6)+"/"+file_data.substring(6, 8)+"/"+name;
				}else
				{
					String s = time+"";	
					dest = patha+s.substring(0, 6)+"/"+s.substring(6, 8)+"/"+name;
				}
				
				if (push(sour, dest,Cover)){
					return;
				}
				processFile(sour, dest,Cover);
			}
			return;
		}

	}
	/**
	 * 初始化并分配线程,file_date为空,则读取指定时间段的文件,
	 * @param s
	 * @param ss
	 * @param StartTime
	 * @param EndTime
	 * @param Cover
	 * @throws IOException
	 */
	public void process(String sour ,String dest,long StartTime,long EndTime,int Cover,String file_data){
		Properties props =ReadProperties.ReadJarProperties();
		
		//单机跑需要加的参数
		if("false".equals(props.getProperty("JobMode"))){
		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
		}
		
		long b = System.currentTimeMillis();
		
		//启动多线程,每个线程在Asn1BerParser_ALL_MODEL中将fields和map赋值,读取文件逻辑
		initMulti(this, thread);
		
		Path path = new Path(sour);
		try {
			fs=path.getFileSystem(conf);

		if(file_data !=null)
		{
				String x[] = file_data.split(":");
				if(x.length>1)
				{
						for(int i = Integer.valueOf(x[0]) ; i <=Integer.valueOf(x[1]);i++)
						{
							file_data = i+"";
							Path path_new = new Path(path.toString()+"/"+file_data.toString());
							if(fs.exists(path_new))
							{
								FileStatus files =	fs.getFileStatus(path_new);
								processPath(files, dest,StartTime,EndTime,Cover,file_data);
							}else
							{
								System.out.println("[ERROR]Folder does not exist!");
								long starttime = Long.valueOf(i+"000000");
								long endtime = Long.valueOf(i+"235959");
								file_data = null;
								process(sour, dest, starttime, endtime, Cover, file_data);
								return;
							}
							
						}
					
				}else
				{
					
					/**
					 * 新加
					 */
					if(fs.getFileStatus(path).isFile()){	
						FileStatus file = fs.getFileStatus(path);
						FileSystem fsFile = path.getFileSystem(conf);
						String fileName = path.getName();
						processPath(file, dest+"/"+fileName,StartTime,EndTime,Cover,file_data);

					}else{
						
						Path path_new = new Path(path.toString()+"/"+file_data.toString());
						if(fs.exists(path_new))
						{
							FileStatus files =	fs.getFileStatus(path_new);
							processPath(files, dest,StartTime,EndTime,Cover,file_data);
						}else
						{
							System.out.println("[ERROR]Folder does not exist!");
							long starttime = Long.valueOf(file_data+"000000");
							long endtime = Long.valueOf(file_data+"235959");
							file_data = null;
							process(sour, dest, starttime, endtime, Cover, file_data);
							return;
						}
					}

				}
			
		}else
		{
				FileStatus files =	fs.getFileStatus(path);
				processPath(files,dest,StartTime,EndTime,Cover,file_data);
		
		}
		} catch (Exception e1) {
			//log.error("Exception :",e1);
			e1.printStackTrace();
			stopItems();
			return;
		}
		stopMulti();
		long e = System.currentTimeMillis();
		report(e-b);
	}
	/**
	 * 将starttime转换为long类型
	 * @param StartTime
	 * @return
	 */
	public long StartTime(Date StartTime)
	{
		
		long a = 0;
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
		if(StartTime ==null)
		{
			//log.info("StartTime is null");
			System.out.println("[INFO]StartTime is null");
		}else
		{
			//log.info("StartTime is "+StartTime);
			System.out.println("[INFO]StartTime is "+StartTime);
			a = Long.valueOf(bartDateFormat.format(StartTime));
		}
		return a;
	}
	/**
	 * 将endtime转换为long类型
	 * @param EndTime
	 * @return
	 */
	public long EndTime(Date EndTime)
	{
		long a = 0;
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
		if(EndTime == null)
		{
			SimpleDateFormat bartDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Date date = new Date();
			//log.info("EndTime is "+bartDateFormat1.format(date));
			System.out.println("EndTime is "+bartDateFormat1.format(date));
			a = Long.valueOf(bartDateFormat.format(date));
		}else
		{
			//log.info("EndTime is "+EndTime);
			System.out.println("EndTime is "+EndTime);
			a = Long.valueOf(bartDateFormat.format(EndTime));
		}
		return a;
	}
	/**
	 * 判断输入目录，输出目录是否相同
	 * @param sour
	 * @param dest
	 * @return
	 */
	public boolean Dir(String sour,String dest)
	{
		if (sour == null) {
			//log.error("Please enter the inputdir !");
			System.out.println("[ERROR]Please enter the inputdir !");
			return false;
		}
		if(dest == null)
		{
			//log.error("Please enter the outputdir !");
			System.out.println("[ERROR]Please enter the outputdir !");
			return false;
		}
		if (dest.equals(sour.toString())) {
			//log.error("The inputdir outputdir can't be the same !");
			System.out.println("[ERROR]The inputdir outputdir can't be the same !");
			return false;
		}
		return true;
	}
	public Parser(String sour, String dest, int thread, int count) {
		super(null);

		this.sour = new File(sour);
		this.dest = new File(dest);
		this.thread = thread;
		this.count = count;
	}
	
	/*
	 * 主方法,判断参数,赋值
	 * 
	 * 支持 动态输入日期   直接输入日期则读日期文件夹下的     输入 日期加starttime和endtime则读该路径下的时间段的.
	 * 
	 * 输入starttime和endtime则读全部文件 ,,遍历不递归该文件夹下时间段内的,再处理
	 * 
	 * file_data决定生成文件在哪个日期下，file_data为null则以文件最后修改时间确定位置
	 * 
	 * 
	 */
	public static void main(String[] args){
		

		args=new String[]{"-i","/itf/qixin/cdr/mbl/811/20160313/FIX_DDR_20160704.024.083.999.001.01_shenggongsi","-o","/output/fixcdr/"
				,"-d","20160313"
				};
//		args=new String[]{"-i","C:\\Users\\asus\\Desktop\\FIX_DDR_20160704.024.083.999.001.01","-o","C:/Users/asus/Desktop/asd"
//		,"-d","20160313"
//		};
		
		  //输入路径
		  String sour = null;
		  //输出路径
		  String dest = null;
		  //文件夹日期
		  String file_data = null;
		  
		  //
		  String logger = null;
		  //
		  int count = 0;
		  
		  //开始时间
		  Date StartTime = null;
		  //结束时间
		  Date EndTime = null;
		  //是否覆盖
		  int Cover = 0;
		  //转换后的结束时间
		  long EndTime1 = 0;
		  //转换后的开始时间
		  long StartTime1 = 0;
		  //线程数，默认为系统物理核心数
		  int thread = Runtime.getRuntime().availableProcessors();

		  
		  CommandLineParser parser = new BasicParser( );  
		  Options options = new Options( );  
		  options.addOption("i", "inputdir", true, "enter the inputdir ");
		  options.addOption("o", "outputdir", true, "enter the outputdir ");
		  options.addOption("r", "Cover", true, "enter the Cover ");
		  options.addOption("d", "file_data", true, "enter the filing ");
		  options.addOption("l", "filing", true, "enter the log ");
		  options.addOption("n", "thread", true, "Thread number");
		  options.addOption("f", "starttime", true, "Query from start time");  
		  options.addOption("t", "endtime", true, "Query to the end of time" );
		  CommandLine commandLine;
		try {
			commandLine = parser.parse( options, args );
			 //判断当为-i将起后面的参数保存到sour
			  if( commandLine.hasOption('i')) {
				  sour = commandLine.getOptionValue('i');
			  }
			  //判断当为-o将起后面的参数保存到dest
			  if( commandLine.hasOption('o')) {
				  dest = commandLine.getOptionValue('o');
			  }
			  if( commandLine.hasOption('d')) {
				  file_data = commandLine.getOptionValue('d');
			  }
			  //判断当为-r将起后面的参数保存到cover
			  if( commandLine.hasOption('r')) {
				  if(commandLine.getOptionValue('r').equals("y"))
				  {
					  Cover = 1;
				  }
			  }
			  
			  if( commandLine.hasOption('l')) {
				  logger = commandLine.getOptionValue('l');
			  }
			  //判断当为-n将起后面的参数保存到thread
			  if( commandLine.hasOption('n')) {
				  if(Integer.valueOf(commandLine.getOptionValue('n'))==0)
					  return;
				  thread = Integer.valueOf(commandLine.getOptionValue('n'));
			  }
			  //判断当为-f将起后面的参数保存到starttime
			  if( commandLine.hasOption('f') ) {  
				  Pattern pattern = Pattern.compile("\\d{4}-{1}\\d{2}-{1}\\d{2}\\s{1}\\d{2}:{1}\\d{2}:{1}\\d{2}");
				  Matcher matcher = pattern.matcher(commandLine.getOptionValue('f'));
				  boolean b= matcher.matches();
				  if(b)
				  {
					  StartTime = DateUtils.timeFromString(commandLine.getOptionValue('f'));
					  
				  }else
				  {
					  //log.error("Please enter the correct start time !");
					  System.out.println("[ERROR]Please enter the correct start time !");
					  System.exit(0);
				  }
			    
			  } 
			  //判断当为-t将起后面的参数保存到endtime
			  if( commandLine.hasOption('t'))
			  {
				  Pattern pattern = Pattern.compile("\\d{4}-{1}\\d{2}-{1}\\d{2}\\s{1}\\d{2}:{1}\\d{2}:{1}\\d{2}");
				  Matcher matcher = pattern.matcher(commandLine.getOptionValue('t'));
				  boolean b= matcher.matches();
				  if(b)
				  {
					  EndTime = DateUtils.timeFromString(commandLine.getOptionValue('t'));
					 
				  }else
				  {
					  //log.error("Please enter the correct end time !");
					  System.out.println("[ERROR]Please enter the correct end time !");
					  System.exit(0);
				  }
			  }
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}	
		//这一行
		Parser o = new Parser(sour, dest, thread, count);
		
		
		//判断输入输出目录是否相同
		if(!o.Dir(sour, dest))
		{
			System.exit(0);
		}
		
		System.out.println("[INFO]HDFS Connection success !");
		System.out.println("[INFO]version is TuoMingv1.0");
		System.out.println("[INFO]Inputdir:"+sour);
		System.out.println("[INFO]Outputdir:"+dest);
		System.out.println("[INFO]Thread:"+thread);
		System.out.println("[INFO]Cover:"+(Cover ==1 ?"YES":"NO"));
		System.out.println("[INFO]Date Folder:"+(file_data==null?"NO":file_data));
		//判断开始和结束时间,转化为long型
		StartTime1 = o.StartTime(StartTime);
		EndTime1 = o.EndTime(EndTime);
		
		//
	    o.process(sour,dest,StartTime1,EndTime1,Cover,file_data);
//	    t.stopOut();
	}

}
