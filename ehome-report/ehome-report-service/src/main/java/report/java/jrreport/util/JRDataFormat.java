package report.java.jrreport.util;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JRDataFormat
{
  public static void main(String[] args)
  {
    JRDataFormat jf = new JRDataFormat();
    System.out.println(convertData(0, "", "111111", "#,###.00"));
  }

  public static String convertData(int Class, String type, String data, String newType)
  {
    if ((data == null) || (data.equals(""))) return "";
    String str = null;
    switch (Class) {
    case 0:
      str = numerical(type, data, newType);
      break;
    case 1:
      str = money(type, data, newType);
      break;
    case 2:
      str = date(type, data, newType);
      break;
    case 3:
      str = time(type, data, newType);
      break;
    case 4:
      str = date(type, data, newType);
      break;
    case 5:
      str = fraction(type, data, newType);
      break;
    case 6:
      str = scientificCount(type, data, newType);
    }

    if (str == null) {
      str = "不支持该数据格式";
    }
    return str;
  }

  private static String numerical(String type, String data, String newType)
  {
    if (newType.equals("")) newType = "#";
    DecimalFormat df = new DecimalFormat(type);
    DecimalFormat ndf = new DecimalFormat(newType);
    try {
      Number n = df.parse(data);
      return ndf.format(n); } catch (Exception e) {
    }
    return null;
  }

  private static String money(String type, String data, String newType)
  {
    String flag;
    if (type.equals("")) {
        flag = String.valueOf(newType.charAt(0));
      newType = newType.substring(1);
    } else {
      flag = String.valueOf(type.charAt(0));
      type = type.substring(1);
    }
    DecimalFormat df = new DecimalFormat(type);
    DecimalFormat ndf = new DecimalFormat(newType);
    try {
      Number n = df.parse(data);
      return flag + ndf.format(n); } catch (Exception e) {
    }
    return null;
  }

  private static String time(String type, String data, String newType)
  {
    if (type.equals("")) {
      type = "HH:mm:ss";
    }
    else if (newType.equals("")) {
      newType = "HH:mm:ss";
    }

    SimpleDateFormat sdf = new SimpleDateFormat(type);
    SimpleDateFormat nsdf = new SimpleDateFormat(newType);
    try {
      Date date = sdf.parse(data);
      return nsdf.format(date); } catch (ParseException e) {
    }
    return null;
  }

  private static String date(String type, String data, String newType)
  {
    if (type.equals("")) {
      if (data.indexOf("-") > 0) {
        if (data.length() > 8)
          type = "yyyy-MM-dd";
        else
          type = "yy-MM-dd";
      } else if (data.indexOf("/") > 0) {
        if (data.length() > 8)
          type = "yyyy/MM/dd";
        else
          type = "yy/MM/dd";
      } else if (data.length() == 8) {
        type = "yyyyMMdd";
      }
    }
    else if (newType.equals("")) {
      newType = "yyMMdd";
    }

    SimpleDateFormat sdf = new SimpleDateFormat(type);
    SimpleDateFormat nsdf = new SimpleDateFormat(newType);
    try {
      Date date = sdf.parse(data);
      return nsdf.format(date); } catch (ParseException e) {
    }
    return null;
  }

  private static String dateTime(String type, String data, String newType)
  {
    if (type.equals("")) {
      if (data.indexOf("-") > 0) {
        if (data.length() > 17)
          type = "yyyy-MM-dd HH:mm:ss";
        else
          type = "yy-MM-dd HH:mm:ss";
      } else if (data.indexOf("/") > 0) {
        if (data.length() > 17)
          type = "yyyy/MM/dd HH:mm:ss";
        else
          type = "yy/MM/dd HH:mm:ss";
      }
    }
    else if (newType.equals("")) {
      newType = "yyyy-MM-dd HH:mm:ss";
    }

    SimpleDateFormat sdf = new SimpleDateFormat(type);
    SimpleDateFormat nsdf = new SimpleDateFormat(newType);
    try {
      Date date = sdf.parse(data);
      return nsdf.format(date); } catch (ParseException e) {
    }
    return null;
  }

  private static String fraction(String type, String data, String newType)
  {
    String flag;
    if (type.equals("")) {
        flag = String.valueOf(newType.charAt(newType.length() - 1));
      newType = newType.substring(0, newType.length() - 1);
    } else {
      flag = String.valueOf(type.charAt(type.length() - 1));
      type = type.substring(0, type.length() - 1);
      data = data.substring(0, data.length() - 1);
      if (!newType.equals("")) {
        newType = newType.substring(0, newType.length() - 1);
      }
    }
    DecimalFormat df = new DecimalFormat(type);
    DecimalFormat ndf = new DecimalFormat(newType);
    try {
      Number n = df.parse(data);
      return ndf.format(n) + flag; } catch (Exception e) {
    }
    return null;
  }

  private static String scientificCount(String type, String data, String newType)
  {
    if (type.equals("")) {
      double d = Double.valueOf(data).doubleValue();
      DecimalFormat df = new DecimalFormat(newType);
      return df.format(d);
    }
    if (!newType.equals("")) {
      double d = Double.valueOf(data).doubleValue();
      DecimalFormat df = new DecimalFormat(newType);
      return df.format(d);
    }
    BigDecimal db = new BigDecimal(data);
    data = db.toPlainString();
    return data;
  }
}

/*
 * Qualified Name:     report.java.jrreport.util.JRDataFormat
*
 */