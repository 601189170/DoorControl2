package com.yyide.doorcontrol.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Base64;
public class Tool {
	/**
	 * 十进制数据格式字节数组转换成十六进制格式字符串
	 * @param src
	 * @return
	 * @throws Exception
	 * [85, 85, 11, -34, 7, 3, 4, 0, 0, -126, 0, 1, 52, 1] —————————— 55550BDE0703040000820001
	 */
	public static String BytesToHexString(byte[] src) throws Exception {
		StringBuilder stringBuilder = new StringBuilder("");
		if ((src == null) || (src.length <= 0)) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static  String getMyUUID(){
 		UUID uuid = UUID.randomUUID();

String uniqueId = uuid.toString();



return uniqueId;

 }


	public static Bitmap stringtoBitmap(String string) {
		//将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	/**
	 * 十六进制字符串数据格式 转字节组十进制
	 * @param hexString
	 * @return
	 * @throws Exception
	 * 55550BDE0703040000820001 —————————— [85, 85, 11, -34, 7, 3, 4, 0, 0, -126, 0, 1, 52, 1]
	 */
	public static byte[] HexStringToBytes(String hexString) throws Exception {
		if ((hexString == null) || (hexString.equals(""))) {
			return null;
		}
		hexString = hexString.toUpperCase(Locale.getDefault());
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = ((byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[(pos + 1)])));
		}
		return d;
	}

	/**
	 * 十六进制数据格式字符串 转字节组十六进制数据格式字符串
	 * @param hexString
	 * @return
	 * @throws Exception
	 * 55550BDE0703040000820001 —————————— 55 55 b de 7 3 4 0 0 82 0 1 34 1
	 */
	public static String HexStringToBytes1(String hexString) throws Exception {
		if ((hexString == null) || (hexString.equals(""))) {
			return null;
		}
		hexString = hexString.toUpperCase(Locale.getDefault());
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = ((byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[(pos + 1)])));
			str.append(Integer.toHexString(d[i] & 0xff));
			str.append(" ");
		}
		return str.toString();
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 *
	 * @param num
	 * @return  高位在前，低位在后
	 */
	public static byte[] IntToBytes(int num) {
		byte[] result = new byte[4];
		result[0] = (byte) ((num >> 24) & 0xff);
		result[1] = (byte) ((num >> 16) & 0xff);
		result[2] = (byte) ((num >> 8) & 0xff);
		result[3] = (byte) ((num >> 0) & 0xff);
		return result;
	}

	/**
	 *
	 * @param bytes
	 * @return
	 */
	public static int BytesToInt(byte[] bytes) {
		int result = 0;
		if(bytes.length == 4) {
			int a = (bytes[0] & 0xff) << 24;
			int b = (bytes[1] & 0xff) << 16;
			int c = (bytes[2] & 0xff) << 8;
			int d = (bytes[3] & 0xff);
			result = a | b | c | d;
		}
		return result;
	}

	/**
	 *
	 * @param bytes
	 */
	public static int BytesToInt2(byte[] bytes) {
		int result = 0;
		if(bytes.length == 2) {
			int a = (bytes[0] & 0xff);
			int b = (bytes[1] & 0xff) << 8;
			result = a|b;
		}
		return result;
	}
	//输入日期，转化为毫秒数，用DATE方法()
	/**
	 * 先用SimpleDateFormat.parse() 方法将日期字符串转化为Date格式
	 * 通过Date.getTime()方法，将其转化为毫秒数  String date = "2001-03-15 15-37-05";
	 */
	public static String stampToDate3(String s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
	public static long gettime(String str){





		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//24小时制
		//      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");//12小时制
		long time2 = 0;
		if (str!=null){
		try {
			time2 = simpleDateFormat.parse(str).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		}
		System.out.println(time2);
		return time2;

	}
	//将毫秒转换为小时：分钟：秒格式

	public static String ms2HMS(int _ms){
		String HMStime;
		_ms/=1000;
		int hour=_ms/3600;
		int mint=(_ms%3600)/60;
		int sed=_ms%60;
		String hourStr= String.valueOf(hour);
		if(hour<10){
			hourStr="0"+hourStr;
		}
		String mintStr= String.valueOf(mint);
		if(mint<10){
			mintStr="0"+mintStr;
		}
		String sedStr= String.valueOf(sed);
		if(sed<10){
			sedStr="0"+sedStr;
		}
		HMStime=hourStr+":"+mintStr+":"+sedStr;
		return HMStime;
	}
	public static String formatTime(Long ms) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
//	    Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		StringBuilder sb = new StringBuilder();
		if(day > 0) {
			sb.append(day+"天");
		}
		if(hour > 0) {
			sb.append(hour+"时");
		}
		if(minute > 0) {
			sb.append(minute+"分");
		}
//		if(second > 0) {
//			sb.append(second+"秒");
//		}
//	    if(milliSecond > 0) {
//	        sb.append(milliSecond+"毫秒");
//	    }
		return sb.toString();
	}
	public static String formattime(Long ms, int time) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		String tm=null;
	if (time==1){
		tm=day+"天";

	}else if (time==2){
		tm=hour+"时";
	}else if (time==3){
		tm=minute+"分";
	}
		return tm;
	}
	public static String formattime2(Long ms, int time) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		String tm=null;
		if (time==1){
			tm=day+"天";

		}else if (time==2){
			tm=hour+" : "+minute;
		}
		Log.e("TAG", "formattime2: "+tm );
		return tm;
	}
	public static Date transForDate(Integer ms) {
		if (ms == null) {
			ms = 0;
		}
		long msl = (long) ms * 1000;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-HH-mm");
		Date temp = null;
		if (ms != null) {
			try {
				String str = sdf.format(msl);
				temp = sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}
	/**
	 * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
	 *
	 * @param time
	 * @return
	 */
	public static String dataOne(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}
	public static String getnowtimenob(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
			//获取当前时间
		Date date = new Date(System.currentTimeMillis());
//		time1.setText("Date获取当前日期时间"+simpleDateFormat.format(date));

		return simpleDateFormat.format(date);

	}


	public static List<WeekDay> getWeekDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		// 获取本周的第一天
		int firstDayOfWeek = calendar.getFirstDayOfWeek();
		List<WeekDay> list = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
			WeekDay weekDay = new WeekDay();
			// 获取星期的显示名称，例如：周一、星期一、Monday等等
			weekDay.week = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.CHINESE);
				weekDay.day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

			list.add(weekDay);
		}

		return list;
	}


    public static class WeekDay {
		/** 星期的显示名称*/
		public String week;
		/** 对应的日期*/
		public String day;




		@Override
		public String toString() {
//			return "WeekDay{" +
//					"week='" + week + '\'' +
//					", day='" + day + '\'' +
//					'}';
			return day ;


		}
	}

	public static File File_dir(String filename){
		String dir = filename;
		File sdRoot = Environment.getExternalStorageDirectory();
		File mkDir = new File(sdRoot, dir);
		if (!mkDir.exists())
			mkDir.mkdirs();
		return mkDir;

	}
	/**
	 * 获取前置摄像头
	 * @return
	 *          当没有前置摄像头时，返回第一个摄像头
	 */
	public static int getFrontCameraID() {
		int size = Camera.getNumberOfCameras();
		L.d("===>"+size);
		Camera.CameraInfo info = new Camera.CameraInfo();
		for (int i = 0; i < size; i++) {
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
				return i;
		}
		return 0;
	}
	/*
    * 将时间转换为时间戳
    */
	public static String dateToStamp(String s) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	public static String dateToStamp3(String s) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}
	/*
    * 将时间转换为时间戳
    */
	public static String dateToStamp2(String s) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}
	/*
        * 将时间戳转换为时间
        */
	public static String stampToDate(String s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
	public static String stampToDate2(String s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
	//去掉小数凑整:不管小数是多少，都进一
	public static int ceilInt(double number){
		return (int) Math.ceil(number);
	}
	public static String getWeek(long time) {

		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date(time));

		int year  = cd.get(Calendar.YEAR); //获取年份
		int month = cd.get(Calendar.MONTH); //获取月份
		int day   = cd.get(Calendar.DAY_OF_MONTH); //获取日期
		int week  = cd.get(Calendar.DAY_OF_WEEK); //获取星期

		String weekString;
		switch (week) {
			case Calendar.SUNDAY:
				weekString = "星期日";
				break;
			case Calendar.MONDAY:
				weekString = "星期一";
				break;
			case Calendar.TUESDAY:
				weekString = "星期二";
				break;
			case Calendar.WEDNESDAY:
				weekString = "星期三";
				break;
			case Calendar.THURSDAY:
				weekString = "星期四";
				break;
			case Calendar.FRIDAY:
				weekString = "星期五";
				break;
			default:
				weekString = "星期六";
				break;

		}

		return weekString;
	}
	/**
	 * 字节数组转16进制
	 * @param bytes 需要转换的byte数组
	 * @return  转换后的Hex字符串
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if(hex.length() < 2){
				sb.append(0);
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 字节转十六进制
	 * @param b 需要进行转换的byte字节
	 * @return  转换后的Hex字符串
	 */
	public static String byteToHex(byte b){
		String hex = Integer.toHexString(b & 0xFF);
		if(hex.length() < 2){
			hex = "0" + hex;
		}
		return hex;
	}
	/**
	 * 自动关闭软键盘
	 * @param activity
	 */
	public static void closeKeybord(Activity activity) {
		InputMethodManager imm =  (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null) {
			imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
		}
	}
	//	获取io状态
//
	public static int ReadIO(int io) {

		try {
			FileInputStream fin=null;
			if(io==1) try {
				fin= new FileInputStream("/proc/yx_gpio/gpio_io1");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//  Log.d("rrr=","fin"+fin);
			BufferedReader reader= new BufferedReader(new InputStreamReader(fin));
			String config = null;
			try {
				config = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fin.close();
			Log.d("rrrr====","****************\n");

			Log.d("rrrr====","read===="+config);

			Log.d("rrrr====","****************\n");


			if(config.equals("1")) {return 1;}
			else if(config.equals("0")) {return 0;}

			return -1;
		} catch(IOException e) {
			e.printStackTrace();

		}

		return -1;
	}
	//读取文本文件中的内容
	public static String ReadTxtFile(String strFilePath)
	{
		String path = strFilePath;
		String content = ""; //文件内容字符串
		//打开文件
		File file = new File(path);
		//如果path是传递过来的参数，可以做一个非目录的判断
		if (file.isDirectory())
		{
			Log.d("TestFile", "The File doesn't not exist.");
		}
		else
		{
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null)
				{
					InputStreamReader inputreader = new InputStreamReader(instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					//分行读取
					while (( line = buffreader.readLine()) != null) {
						content += line + "\n";
					}
					instream.close();
				}
			}
			catch (FileNotFoundException e)
			{
				Log.d("TestFile", "The File doesn't not exist.");
			}
			catch (IOException e)
			{
				Log.d("TestFile", e.getMessage());
			}
		}
		return content;
	}

	//format 传进来的格式
	//seconds 秒
	public static String timeStamp2Date(String seconds,String format) {
		  if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
		         return "";
		    }
	     if(format == null || format.isEmpty()){
	            format = "yyyy-MM-dd";
		        }
	      SimpleDateFormat sdf = new SimpleDateFormat(format);
	      return sdf.format(new Date(Long.valueOf(seconds+"000")));
	    }

	public static String dateToStamp4(String s) throws ParseException {
		String res=null;
		Log.e("TAG", "dateToStamp4: "+s );
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
			Date date = simpleDateFormat.parse(s);
			long ts = date.getTime();

			res = String.valueOf(ts);


		return res;
	}

	/**
	 * 选择变换
	 *
	 * @param origin 原图
	 * @param alpha  旋转角度，可正可负
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmap(Bitmap origin, float alpha) {
		Log.e("TAG", "rotateBitmap: "+alpha );
		if (origin == null) {
			return null;
		}
		int width = origin.getWidth();
		int height = origin.getHeight();
		Matrix matrix = new Matrix();
		matrix.setRotate(alpha);
		// 围绕原地进行旋转
		Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
		if (newBM.equals(origin)) {
			return newBM;
		}
		origin.recycle();
		return newBM;
	}

	/**
	 * 判断身份证格式
	 *
	 * @param idNum
	 * @return
	 */
	public static boolean isIdNum(String idNum) {

		// 中国公民身份证格式：长度为15或18位，最后一位可以为字母
		Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");

		// 格式验证
		if (!idNumPattern.matcher(idNum).matches())
			return false;

		// 合法性验证

		int year = 0;
		int month = 0;
		int day = 0;

		if (idNum.length() == 15) {

			// 一代身份证

			System.out.println("一代身份证：" + idNum);

			// 提取身份证上的前6位以及出生年月日
			Pattern birthDatePattern = Pattern.compile("\\d{6}(\\d{2})(\\d{2})(\\d{2}).*");

			Matcher birthDateMather = birthDatePattern.matcher(idNum);

			if (birthDateMather.find()) {

				year = Integer.valueOf("19" + birthDateMather.group(1));
				month = Integer.valueOf(birthDateMather.group(2));
				day = Integer.valueOf(birthDateMather.group(3));

			}

		} else if (idNum.length() == 18) {

			// 二代身份证

			System.out.println("二代身份证：" + idNum);

			// 提取身份证上的前6位以及出生年月日
			Pattern birthDatePattern = Pattern.compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");

			Matcher birthDateMather = birthDatePattern.matcher(idNum);

			if (birthDateMather.find()) {

				year = Integer.valueOf(birthDateMather.group(1));
				month = Integer.valueOf(birthDateMather.group(2));
				day = Integer.valueOf(birthDateMather.group(3));
			}

		}

		// 年份判断，100年前至今

		Calendar cal = Calendar.getInstance();

		// 当前年份
		int currentYear = cal.get(Calendar.YEAR);

		if (year <= currentYear - 100 || year > currentYear)
			return false;

		// 月份判断
		if (month < 1 || month > 12)
			return false;

		// 日期判断

		// 计算月份天数

		int dayCount = 31;

		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				dayCount = 31;
				break;
			case 2:
				// 2月份判断是否为闰年
				if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
					dayCount = 29;
					break;
				} else {
					dayCount = 28;
					break;
				}
			case 4:
			case 6:
			case 9:
			case 11:
				dayCount = 30;
				break;
		}

		System.out.println(String.format("生日：%d年%d月%d日", year, month, day));

		System.out.println(month + "月份有：" + dayCount + "天");

		if (day < 1 || day > dayCount)
			return false;

		return true;
	}
	public static boolean isMobileNO(String mobileNums) {
		/**
		 * 判断字符串是否符合手机号码格式
		 * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
		 * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
		 * 电信号段: 133,149,153,170,173,177,180,181,189
		 * @param str
		 * @return 待检测的字符串
		 *
		 */

		String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else
			return mobileNums.matches(telRegex);
	}


	public static String  Stringbyorder(String str) {

		//function 1将字符串的每个字符存入数组toCharArray()
		StringBuffer sb = new StringBuffer();
		char[] ch = str.toCharArray();
		for(int i = ch.length-1;i>=0;i--){
			sb.append(ch[i]);
		}

		return sb.toString();

	}





	public static void showKeyboard(View view) {
		try {
			InputMethodManager imm = (InputMethodManager) view.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				view.requestFocus();
				imm.showSoftInput(view, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}



}
