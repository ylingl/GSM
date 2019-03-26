package utils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GTime
  implements Serializable
{
  private static final long serialVersionUID = -6404250600725574186L;
  public static final int YYYY = 1;
  public static final int YYYYMM = 2;
  public static final int YYYYMMDD = 3;
  public static final int YYYYMMDDhh = 4;
  public static final int YYYYMMDDhhmm = 5;
  public static final int YYYYMMDDhhmmss = 6;
  public static final int YYYYMMDDhhmmssxxx = 7;
  public static final int YY = 11;
  public static final int YYMM = 12;
  public static final int YYMMDD = 13;
  public static final int YYMMDDhh = 14;
  public static final int YYMMDDhhmm = 15;
  public static final int YYMMDDhhmmss = 16;
  public static final int YYMMDDhhmmssxxx = 17;
  public static final int hh = 24;
  public static final int hhmm = 25;
  public static final int hhmmss = 26;
  public static final int hhmmssxxx = 27;
  public static final int log = 0;
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  public static synchronized String getTime(int format)
  {
    Calendar time = Calendar.getInstance();
    return getTime(format, time);
  }
  
  public static synchronized String getLogTime()
  {
    return sdf.format(new Date()) + " ";
  }
  
  public static synchronized String getNormalTime(Date date)
  {
    if (date == null) {
      return sdf.format(new Date());
    }
    return sdf.format(date);
  }
  
  public static synchronized String getAfterTime(int format, int m)
  {
    Calendar time = Calendar.getInstance();
    time.add(12, m);
    return getTime(format, time);
  }
  
  public static synchronized String getTimes(String date1, String date2)
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    Date now = null;
    Date date = null;
    try
    {
      now = df.parse(date1);
      date = df.parse(date2);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    long l = now.getTime() - date.getTime();
    long day = l / 86400000L;
    long hour = l / 3600000L - day * 24L;
    long min = l / 60000L - day * 24L * 60L - hour * 60L;
    long s = l / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L;
    long mmin = l - day * 24L * 60L * 60L * 1000L - hour * 60L * 60L * 1000L - min * 60L * 1000L - s * 1000L;
    return day + "å¤?" + hour + "å°æ—¶" + min + "åˆ?" + s + "ç§?" + mmin + "æ¯«ç§’";
  }
  
  public static synchronized String getBeforTime(int format, int m)
  {
    Calendar time = Calendar.getInstance();
    time.add(12, -m);
    return getTime(format, time);
  }
  
  private static synchronized String getTime(int format, Calendar time)
  {
    StringBuffer cTime = new StringBuffer(10);
    int miltime = time.get(14);
    int second = time.get(13);
    int minute = time.get(12);
    int hour = time.get(11);
    int day = time.get(5);
    int month = time.get(2) + 1;
    int year = time.get(1);
    if (format > 10) {
      year -= 2000;
    }
    if (format < 20) {
      if (format < 10) {
        cTime.append(year);
      } else if (format < 20) {
        cTime.append(getFormatTime(year, 2));
      }
    }
    if (((format < 20) && (format > 11)) || ((format > 1) && (format < 10))) {
      cTime.append(getFormatTime(month, 2));
    }
    if (((format < 20) && (format > 12)) || ((format > 2) && (format < 10))) {
      cTime.append(getFormatTime(day, 2));
    }
    if (((format > 13) && (format < 20)) || ((format > 3) && (format < 10)) || ((format > 23) && (format < 30))) {
      cTime.append(getFormatTime(hour, 2));
    }
    if (((format > 14) && (format < 20)) || ((format > 4) && (format < 10)) || ((format > 24) && (format < 30))) {
      cTime.append(getFormatTime(minute, 2));
    }
    if (((format > 15) && (format < 20)) || ((format > 5) && (format < 10)) || ((format > 25) && (format < 30))) {
      cTime.append(getFormatTime(second, 2));
    }
    if (((format > 16) && (format < 20)) || ((format > 6) && (format < 10)) || ((format > 26) && (format < 30))) {
      cTime.append(getFormatTime(miltime, 3));
    }
    return cTime.toString();
  }
  
  private static synchronized String getFormatTime(int time, int format)
  {
    StringBuffer numm = new StringBuffer();
    int length = String.valueOf(time).length();
    if (format < length) {
      return null;
    }
    for (int i = 0; i < format - length; i++) {
      numm.append("0");
    }
    numm.append(time);
    return numm.toString().trim();
  }
  
  public static synchronized String FormatTime(String ftime, String ttime, String time)
  {
    if ("".equals(time)) {
      return "";
    }
    if (ftime.equals("")) {
      ftime = "yyyyMMddhhmmss";
    }
    if (ttime.equals("")) {
      ttime = "yyyy-MM-dd HH:mm:ss";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(ftime);
    SimpleDateFormat tosdf = new SimpleDateFormat(ttime);
    String value = time;
    try
    {
      Date date = sdf.parse(time);
      value = tosdf.format(date);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return value;
  }
}
