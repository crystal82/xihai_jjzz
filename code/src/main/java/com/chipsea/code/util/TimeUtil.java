package com.chipsea.code.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeUtil {

	public static final String TIME_FORMAT0 = "yyyy/MM/dd  HH:mm:ss";
	public static final String TIME_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT2 = "yyyy-MM-dd";
	public static final String TIME_FORMAT_MM_DD_1 = "MM月dd日";
	public static final String TIME_FORMAT3 = "yyyy/MM/dd";

	/**
	 * 日期格式转换
	 *
	 * @param src
	 *            + 原日期格式
	 * @param des
	 *            目标日期格式
	 * @return
	 */
	public static String dateFormatChange(String date, String src, String des) {
		SimpleDateFormat format = new SimpleDateFormat(src, Locale.getDefault());
		SimpleDateFormat format1 = new SimpleDateFormat(des,
				Locale.getDefault());
		try {
			return format1.format(format.parse(date));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static int dateDiffMinte(String StartTime){
		long nm = 1000*60;//一分钟的毫秒数
		int ret=0;
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate=sdf.parse(StartTime);
			long iTmp=(startDate.getTime()-System.currentTimeMillis());
			if(iTmp>0){
				iTmp=iTmp / nm;
				ret=(int)iTmp;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 获取当前系统时间
	 *
	 * @return
	 */
	public static String getCurrentTime(String dateFormat) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(dateFormat,
				Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());
		return myFormatter.format(date);
	}



	/**
	 * 根据年 月 获取对应的月份 天数
	 * */
	public static int getDaysByYearMonth(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 获取一年中一周从星期一到星期日的日期列表
	 *
	 * @return
	 */
	public static ArrayList<String> getWeekDateInYear(String year) {

		// 一周从星期一到星期日的日期列表
		ArrayList<String> weekdate = new ArrayList<String>();
		// 开始日期
		String startYear = year + "-01" + "-01";
		// 开始日期是星期几
		int weekday = getWeek(startYear);
		String startTime = TimeUtil.getDateForSubDay(Calendar.DAY_OF_MONTH,
				-(weekday - 1), startYear, TIME_FORMAT2);
		String endTime = TimeUtil.getDateForSubDay(Calendar.DAY_OF_YEAR,
				7 - weekday, startYear, TIME_FORMAT2);
		weekdate.add(TimeUtil.dateFormatChange(startTime, TIME_FORMAT2,
				"MM月dd日")
				+ "~"
				+ TimeUtil.dateFormatChange(endTime, TIME_FORMAT2, "MM月dd日"));

		String tmpTime = endTime;
		String tmpStartTime = "";
		for (int i = 0; i < 365; i++) {
			if ((i % 7) == 0) {
				tmpStartTime = TimeUtil.getDateForSubDay(Calendar.DAY_OF_YEAR,
						1, tmpTime, TIME_FORMAT2);
			}
			if ((i % 7) == 6) {
				tmpTime = TimeUtil.getDateForSubDay(Calendar.DAY_OF_YEAR, 7,
						tmpTime, TIME_FORMAT2);
				weekdate.add(TimeUtil.dateFormatChange(tmpStartTime,
						TIME_FORMAT2, "MM月dd日")
						+ "~"
						+ TimeUtil.dateFormatChange(tmpTime, TIME_FORMAT2,
						"MM月dd日"));
			}
		}
		return weekdate;
	}

	/**
	 * 获取一年中季度的日期列表
	 *
	 * @return
	 */
	public static ArrayList<String> getQuarterDateInYear(String year) {
		ArrayList<String> quarterdate = new ArrayList<String>();
		String quarterStart = year + "-01" + "-01";
		String tmpDate = quarterStart;
		for (int i = 0; i < 4; i++) {
			tmpDate = TimeUtil.getDateForSubDay(Calendar.MONTH, 3, tmpDate,
					TIME_FORMAT2);
			String quarterEnd = TimeUtil.getDateForSubDay(Calendar.DATE, -1,
					tmpDate, TIME_FORMAT2);
			quarterdate.add(TimeUtil.dateFormatChange(quarterStart,
					TIME_FORMAT2, "MM月dd日")
					+ "~"
					+ TimeUtil.dateFormatChange(quarterEnd, TIME_FORMAT2,
					"MM月dd日"));
			quarterStart = tmpDate;
		}
		return quarterdate;
	}


	/**
	 * 根据日期获取季度的日期范围
	 *
	 * @param time
	 * @return
	 */
	public static String[] getQuarterDateRange(String time) {
		String[] rang = new String[2];
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
				Locale.getDefault());
		Calendar a = Calendar.getInstance();
		try {
			a.setTime(format.parse(time));
			int year = a.get(Calendar.YEAR);
			int mon = a.get(Calendar.MONTH) + 1;
			if (mon == 1 || mon == 2 || mon == 3) {
				rang[0] = year + "-01" + "-01";
				int days = TimeUtil.getDaysByYearMonth(year, 3);
				rang[1] = year + "-03" + "-"
						+ (days < 10 ? ("0" + days) : days);
			} else if (mon == 4 || mon == 5 || mon == 6) {
				rang[0] = year + "-04" + "-01";
				int days = TimeUtil.getDaysByYearMonth(year, 6);
				rang[1] = year + "-06" + "-"
						+ (days < 10 ? ("0" + days) : days);
			} else if (mon == 7 || mon == 8 || mon == 9) {
				rang[0] = year + "-07" + "-01";
				int days = TimeUtil.getDaysByYearMonth(year, 9);
				rang[1] = year + "-09" + "-"
						+ (days < 10 ? ("0" + days) : days);
			} else if (mon == 10 || mon == 11 || mon == 12) {
				rang[0] = year + "-10" + "-01";
				int days = TimeUtil.getDaysByYearMonth(year, 12);
				rang[1] = year + "-12" + "-"
						+ (days < 10 ? ("0" + days) : days);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rang;
	}


	/**
	 * 根据日期获取一周的日期范围
	 *
	 * @param time
	 * @return
	 */
	public static String[] getWeekDateRange(String time) {
		String[] rang = new String[2];
		int weekday = getWeek(time);
		rang[0] = TimeUtil.getDateForSubDay(Calendar.DAY_OF_MONTH,
				-(weekday - 1), time, TIME_FORMAT2);
		rang[1] = TimeUtil.getDateForSubDay(Calendar.DAY_OF_YEAR, 7 - weekday,
				time, TIME_FORMAT2);
		return rang;
	}


	/**
	 * 减一天的日期
<<<<<<< .working
	 *
=======
	 *
	 * @param time
	 * @return
	 */
	public static String getSyncMonth(String time) {
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT1,
				Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.MONTH, -2);
		Date date = calendar.getTime();
		SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT1,
				Locale.getDefault());
		return sf.format(date);
	}

	/**
	 * 减一天的日期
	 * 
	 * @param time
	 * @return
	 */
	public static String minusOneMonth(String time) {
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT1,
				Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.MONTH, -1);
		Date date = calendar.getTime();
		SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT1,
				Locale.getDefault());
		return sf.format(date);
	}

	/**
	 * 减一天的日期
	 *
	 * @param time
	 * @return
	 */
	public static String minusOneDay(String time) {
		return TimeUtil.getDateForSubDay(Calendar.DAY_OF_MONTH, -1, time,
				TIME_FORMAT2);
	}

	/**
	 * 减数天的日期
	 *
	 * @param time
	 * @param day
	 * @return
	 */
	public static String minusDay(String time, int day) {
		return TimeUtil.getDateForSubDay(Calendar.DAY_OF_MONTH, -day, time,
				TIME_FORMAT2);
	}


	public static String getMinusDayString(long time, int day) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
		Date date = new Date(time + day * Constant.ONE_DAY_MS);
		return sdf.format(date);
	}



	public static  int getTwoDay(long startTs, String standardDate) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date thisDay = myFormatter.parse(standardDate);
			return (int) ((thisDay.getTime() - startTs) / (Constant.ONE_DAY_MS));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	public static long periodFor(int ptype){
		int nDays;
		if(ptype==1){
			nDays = 7;
		}else if (ptype==2){
			nDays = 28;
		}else if (ptype==3){
			nDays = 3*28;
		}else {
			throw new RuntimeException();
		}

		return 3600L*1000*24*nDays;

	}

	public static List<Long> rotateStartEndTimes(long lastStart, int ptype){
		List<Long> times = new ArrayList<Long>();
		times.add(lastStart); // 下次结束时间
		times.add(lastStart- periodFor(ptype));
		return times;
	}

	/**
	 * 加一天的日期
	 *
	 * @param time
	 * @return
	 */
	public static String addDay(String time ,int day) {
		return TimeUtil.getDateForSubDay(Calendar.DAY_OF_MONTH, day, time,
				TIME_FORMAT2);
	}

	/**
	 * 获取周表下标，用于查询当前时间所在周的时间段
	 *
	 * @return
	 */
	public static int getCurWeekIndex() {

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.WEEK_OF_YEAR) - 1;
	}

	/**
	 * 获取月表下标，用于查询当前时间所在月的时间段
	 *
	 * @return
	 */
	public static int getCurMonthIndex() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.MONTH);
	}

	/**
	 * 获取季度表下标，用于查询当前时间所在月季度的时间段
	 *
	 * @return
	 */
	public static int getCurQuarterIndex() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.MONTH) / 3;
	}

	/**
	 * 判断当前日期是星期几
	 *
	 * @param pTime
	 *            设置的需要判断的时间 //格式如2012-09-08
	 *
	 *
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */

	public static int getWeek(String pTime) {

		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取年月日
	 *
	 * @param pTime
	 * @return
	 */
	public static int[] getDate(String pTime) {

		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
				Locale.getDefault());
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int[] date = new int[3];
		date[0] = c.get(Calendar.DATE);
		date[1] = c.get(Calendar.MONTH);
		date[2] = c.get(Calendar.YEAR);
		return date;
	}

	/**
	 * 获取当月的 天数
	 * */
	public static int getCurrentMonthDay() {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 通过当前日期获取距离当前日期数的日期
	 *
	 * @param
	 *
	 * @param field
	 *            距离当前日期的天数
	 * @return
	 */
	public static String getDateForSubDay(int field, int value, String tmpDate,
										  String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
				Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(tmpDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.add(field, value);
		Date date = calendar.getTime();
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat,
				Locale.getDefault());
		return sf.format(date);
	}

	/**
	 * 判断两个日期是在同一周
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static boolean isSameWeek(String date1, String date2) {
		if (date1.equals("") || date2.equals("")) {
			return false;
		}
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
				Locale.getDefault());
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(date1);
			d2 = format.parse(date2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		// subYear==0,说明是同一年
		if (subYear == 0) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * 判断两个日期是在同一月
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameMonth(String date1, String date2) {
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
				Locale.getDefault());
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(date1);
			d2 = format.parse(date2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		// subYear==0,说明是同一年
		if (subYear == 0) {
			if (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))
				return true;
		}
		return false;
	}

	/**
	 * 判断两个日期是在同一月
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameQuarter(String date1, String date2) {
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT2,
				Locale.getDefault());
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(date1);
			d2 = format.parse(date2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		// subYear==0,说明是同一年
		if (subYear == 0) {

			int mon1 = cal1.get(Calendar.MONTH);
			int mon2 = cal2.get(Calendar.MONTH);
			if ((mon2 - mon1) <= 0)
				return true;
		}
		return false;
	}

	/**
	 * 获得当前日期和时间
	 *
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurDateAndTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				TIME_FORMAT1, Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * 将时间戳转化成时间格式
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String parseTimes(long times) {
		return parseTimes(times,TIME_FORMAT1);
	}

	/**
	 * 将时间戳转化成时间格式
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String parseTimes(long times,String fotmat) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				fotmat, Locale.getDefault());
		Date curDate = new Date(times);// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * 获得当前日期和时间
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getCurDate() {
		SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT2,
				Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * 获得年龄
	 *
	 * @param birthday
	 * @return
	 */
	public static int getAgeThroughBirthday(String birthday) {
		int year = Integer.valueOf(birthday.substring(0, 4));
		int nowYear = Integer.valueOf(TimeUtil.getCurDate().substring(0, 4));
		return nowYear - year;
	}

	/**
	 * 转化时间戳
	 *
	 * @param time
	 * @return
	 */
	public static long getTimestamp(String time) {
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT1,Locale.getDefault());
		Date date = new Date();
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
	/**
	 * 转化时间戳
	 *
	 * @param time
	 * @return
	 */
	public static long getTimestamp(String time , String FROMAT) {
		SimpleDateFormat format = new SimpleDateFormat(FROMAT,Locale.getDefault());
		Date date = new Date();
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
	public static List<String> getOneYearDateString(){
		String day = getCurDate() ;
		List<String> result = new ArrayList<String>() ;
		long befor = System.currentTimeMillis() ;
		for (int i = 365; i > 0; i--) {
			String dateStr = minusDay(day,i) ;
			result.add(dateStr);
		}
		long after = System.currentTimeMillis() ;
		Log.e("TestActivity",after - befor + "") ;
		result.add("今天");
		return result ;
	}

	/**
	 * 格式转换
	 * @param time
	 * @return 2016-06-01 星期三
     */
	public static String formatTime(String time,String formt){
		Date date=new Date(TimeUtil.getTimestamp(time));
		SimpleDateFormat dateFm = new SimpleDateFormat(formt);
		return dateFm.format(date);
	}
	public static String getWeekForString(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String str = formatter.format(curDate);
		if (str.equals(dateStr)) {
			return "今天";
		} else {
			Date date = stringToDate(dateStr);
			try {
				String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六", ""};
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
				if (w < 0)
					w = 0;
				return weekDays[w];
			} catch (Exception e) {
				// TODO: handle exception
				return "";
			}
		}
	}
	public static Date stringToDate(String dateString){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		return convertedDate;
	}

	/**
	 * 获得一段时间内的周数
	 * @param startTs 开始时间戳
	 * @return
     */
	public static List<Integer> getWeeks(long startTs) {
		List<Integer> result = new ArrayList<Integer>() ;
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.setTime(new Date(startTs));
		for(int i=0; i<12 ; i++){
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			result.add(week);
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		}
		return result ;
	}
	public static int getWeekNumberFormDate(String date){
		long timestamp = getTimestamp(date,TIME_FORMAT2) ;
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.setTime(new Date(timestamp));
		int week = cal.get(Calendar.WEEK_OF_YEAR);
		return week ;
	}
	public static String getWeekFirst(int year, int week, int dayOfWeek) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, 0, 1);

		int day = calendar.get(Calendar.DAY_OF_WEEK);
		System.out.println("day->"+day);


		calendar.add(Calendar.DATE, (week-1)*7+(dayOfWeek-day));

		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");

		return sf2.format(calendar.getTime());
	}
}
