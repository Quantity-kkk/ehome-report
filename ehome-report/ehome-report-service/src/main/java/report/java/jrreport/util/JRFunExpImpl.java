package report.java.jrreport.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JRFunExpImpl
  implements JRFunExp
{
  static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

  public String select(String select_exp) {
    return select_exp;
  }

  public String select(String select_exp, boolean filter_exp) {
    if (filter_exp) {
      return select_exp;
    }
    return "";
  }

  public String select(String select_exp, String expression) {
    List arrayList = new ArrayList();
    String resultString = "<table style='width:100%;'>";
    String[] str1 = expression.split("AND");
    int k = 0;
    for (int i = 0; i < str1.length; i++) {
      String[] str2 = str1[i].split("OR");
      for (int j = 0; j < str2.length; j++) {
        String string = str2[j].replace("(", "").replace(")", "").trim();
        arrayList.add(string);
        k++;
      }
    }
    int listsize = 0;
    List list = new ArrayList();
    for (int i = 0; i < arrayList.size(); i++) {
      String ds2 = "";
      if (((String)arrayList.get(i)).indexOf("<=") != -1)
        ds2 = ((String)arrayList.get(i)).split("<=")[1].trim();
      else if (((String)arrayList.get(i)).indexOf(">=") != -1)
        ds2 = ((String)arrayList.get(i)).split(">=")[1].trim();
      else if (((String)arrayList.get(i)).indexOf("!=") != -1)
        ds2 = ((String)arrayList.get(i)).split("!=")[1].trim();
      else if (((String)arrayList.get(i)).indexOf(">") != -1)
        ds2 = ((String)arrayList.get(i)).split(">")[1].trim();
      else if (((String)arrayList.get(i)).indexOf("<") != -1)
        ds2 = ((String)arrayList.get(i)).split("<")[1].trim();
      else if (((String)arrayList.get(i)).indexOf("=") != -1) {
        ds2 = ((String)arrayList.get(i)).split("=")[1].trim();
      }
      list = (List)SelectUtil.dataSetMap.get(ds2.split("\\.")[0]);
      if (list.size() > listsize) {
        listsize = list.size();
      }
    }

    String[][] arrayList1 = new String[listsize][arrayList.size()];
    for (int i = 0; i < arrayList.size(); i++) {
      String ds1 = "";
      String ds2 = "";
      String relationalOperators = "";
      if (((String)arrayList.get(i)).indexOf("<=") != -1) {
        ds1 = ((String)arrayList.get(i)).split("<=")[0].trim();
        ds2 = ((String)arrayList.get(i)).split("<=")[1].trim();
        relationalOperators = "<=";
      } else if (((String)arrayList.get(i)).indexOf(">=") != -1) {
        ds1 = ((String)arrayList.get(i)).split(">=")[0].trim();
        ds2 = ((String)arrayList.get(i)).split(">=")[1].trim();
        relationalOperators = ">=";
      } else if (((String)arrayList.get(i)).indexOf("!=") != -1) {
        ds1 = ((String)arrayList.get(i)).split("!=")[0].trim();
        ds2 = ((String)arrayList.get(i)).split("!=")[1].trim();
        relationalOperators = "!=";
      } else if (((String)arrayList.get(i)).indexOf(">") != -1) {
        ds1 = ((String)arrayList.get(i)).split(">")[0].trim();
        ds2 = ((String)arrayList.get(i)).split(">")[1].trim();
        relationalOperators = ">";
      } else if (((String)arrayList.get(i)).indexOf("<") != -1) {
        ds1 = ((String)arrayList.get(i)).split("<")[0].trim();
        ds2 = ((String)arrayList.get(i)).split("<")[1].trim();
        relationalOperators = "<";
      } else if (((String)arrayList.get(i)).indexOf("=") != -1) {
        ds1 = ((String)arrayList.get(i)).split("=")[0].trim();
        ds2 = ((String)arrayList.get(i)).split("=")[1].trim();
        relationalOperators = "=";
      }
      String val1 = SelectUtil.getValue(ds1);
      List list1 = (List)SelectUtil.dataSetMap.get(ds2.split("\\.")[0]);
      for (int j = 0; j < list1.size(); j++) {
        String val2 = ((HashMap)list1.get(j)).get(ds2).toString();
        if (relationalOperators.indexOf("<=") != -1) {
          if (Double.parseDouble(val1) <= Double.parseDouble(val2))
            arrayList1[j][i] = "true";
          else
            arrayList1[j][i] = "false";
        }
        else if (relationalOperators.indexOf(">=") != -1) {
          if (Double.parseDouble(val1) >= Double.parseDouble(val2))
            arrayList1[j][i] = "true";
          else
            arrayList1[j][i] = "false";
        }
        else if (relationalOperators.indexOf("!=") != -1) {
          if (Double.parseDouble(val1) != Double.parseDouble(val2))
            arrayList1[j][i] = "true";
          else
            arrayList1[j][i] = "false";
        }
        else if (relationalOperators.indexOf(">") != -1) {
          if (Double.parseDouble(val1) > Double.parseDouble(val2))
            arrayList1[j][i] = "true";
          else
            arrayList1[j][i] = "false";
        }
        else if (relationalOperators.indexOf("<") != -1) {
          if (Double.parseDouble(val1) < Double.parseDouble(val2))
            arrayList1[j][i] = "true";
          else
            arrayList1[j][i] = "false";
        }
        else if (relationalOperators.indexOf("=") != -1) {
          if (val1.equals(val2))
            arrayList1[j][i] = "true";
          else {
            arrayList1[j][i] = "false";
          }
        }
      }
    }

    boolean isStyle = false;
    for (int i = 0; i < arrayList1.length; i++) {
      String strnewString = expression;
      for (int j = 0; j < arrayList1[i].length; j++) {
        strnewString = strnewString.replace((CharSequence)arrayList.get(j), arrayList1[i][j]);
      }
      strnewString = strnewString.replace("AND", "&&").replace("OR", "||");
      try {
        String result = jse.eval(strnewString).toString();
        String styleStr = SelectUtil.styleString;
        if (result.equals("true")) {
          if (isStyle) {
            String styleStrNew = "style='" + styleStr + "border-top:1px solid rgb(0, 0, 0);'";
            resultString = resultString + "<tr><td " + styleStrNew + ">" + ((HashMap)list.get(i)).get(select_exp) + "</td></tr>";
          } else {
            resultString = resultString + "<tr><td style='" + styleStr + "'>" + ((HashMap)list.get(i)).get(select_exp) + "</td></tr>";
          }
          isStyle = true;
        }
      }
      catch (ScriptException e) {
        e.printStackTrace();
      }
    }
    resultString = resultString + "</table>";
    return resultString;
  }

  public String gpc(Object[] args) {
    String str = "";
    if (args.length == 1) {
      str = args[0].toString();
    }
    else if (args[0].toString().indexOf("</td><td>") != -1) {
      String[] arr = args[0].toString().split("</td><td>");
      for (int i = 0; i < arr.length; i++) {
        str = str + args[1];
        if (i < arr.length - 1)
          str = str + "</td><td>";
      }
    }
    else {
      str = args[0].toString();
    }

    return str;
  }

  public int count() {
    return 1;
  }

  public int count(boolean filterExp) {
    if (filterExp) {
      return 1;
    }
    return 0;
  }

  public int count(String nullCheckExp) {
    if ((nullCheckExp != null) && (!nullCheckExp.equals(""))) {
      return 1;
    }
    return 0;
  }

  public String dateTime(String dataStr) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat nsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date = sdf.parse(dataStr);
      return nsdf.format(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }return "";
  }

  public String dateTime(Long dataStr)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      //long timeStart = sdf.parse(dataStr).getTime();
      Date date = new Date(dataStr);
      return sdf.format(date);
    } catch (Exception e) {
      e.printStackTrace();
    }return "";
  }

  public String dateTime2(String dataStr, String format)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    SimpleDateFormat nsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date = sdf.parse(dataStr);
      return nsdf.format(date); } catch (ParseException e) {
    }
    return "";
  }

  private Calendar getCalendar(Date date)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }

  public String day(String dateStr) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try
    {
      Date date = sdf.parse(dateStr);
      Calendar cal = getCalendar(date);
      return cal.get(5)+"";
    } catch (ParseException e) {
      e.printStackTrace();
    }return "";
  }

  public String month(String dateStr)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try
    {
      Date date = sdf.parse(dateStr);
      Calendar cal = getCalendar(date);
      return cal.get(2)+"";
    } catch (ParseException e) {
      e.printStackTrace();
    }return "";
  }

  public String year(String dateStr)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try
    {
      Date date = sdf.parse(dateStr);
      Calendar cal = getCalendar(date);
      return cal.get(1)+"";
    } catch (ParseException e) {
      e.printStackTrace();
    }return "";
  }

  public String now()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    return sdf.format(date);
  }

  public String time() {
    DateFormat sdf = DateFormat.getTimeInstance();
    Date date = new Date();
    return sdf.format(date);
  }

  public String date(String dataStr) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat nsdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = sdf.parse(dataStr);
      return nsdf.format(date) + " 00:00:00"; } catch (ParseException e) {
    }
    return "";
  }

  public String chn(String str)
  {
    return new JRMoneyFormat().formatM(str);
  }

  public String chn(String str, boolean flag1) {
    JRMoneyFormat jrm = new JRMoneyFormat();
    if (flag1) {
      return jrm.transition(str);
    }
    return jrm.formatM(str);
  }

  public String chn(String str, boolean flag1, boolean flag2)
  {
    JRMoneyFormat jrm = new JRMoneyFormat();
    if (flag2) {
      return jrm.format(str, false);
    }
    return jrm.formatM(str);
  }

  public String left(String str, int n)
  {
    if ((n > 0) && (str.length() > n)) {
      return str.substring(0, n);
    }
    return str;
  }

  public String right(String str, int n)
  {
    if ((n > 0) && (str.length() > n)) {
      return str.substring(str.length() - n);
    }
    return str;
  }

  public String ltrim(String str)
  {
    return str.substring(str.lastIndexOf(str.trim()));
  }

  public String rtrim(String str) {
    return str.substring(0, str.lastIndexOf(str.trim()) + str.trim().length());
  }

  public String trim(String str) {
    str = str.substring(str.lastIndexOf(str.trim()));
    str = str.substring(0, str.lastIndexOf(str.trim()) + str.trim().length());
    return str;
  }

  public int len(String str) {
    return str.length();
  }

  public String lower(String str) {
    return str.toLowerCase();
  }

  public String upper(String str) {
    return str.toUpperCase();
  }

  public String mid(String str, int start) {
    return str.substring(start);
  }

  public String mid(String str, int start, int end) {
    return str.substring(start, end);
  }

  public int pos(String str1, String str2) {
    return str1.indexOf(str2);
  }

  public int pos(String str1, String str2, int index) {
    return str1.indexOf(str2, index);
  }

  public String rmb(Number num) {
    JRMoneyFormat jrm = new JRMoneyFormat();
    return jrm.format(num.toString(), true);
  }

  public String rmb(String num) {
    JRMoneyFormat jrm = new JRMoneyFormat();
    return jrm.format(num, true);
  }

  public String rplc(String str, String strz, String strt) {
    return str.replaceAll(strz, strt);
  }

  public String space(int n) {
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < n; i++) {
      str.append(" ");
    }
    return str.toString();
  }

  public String[] split(String str, String flag) {
    return str.split(flag);
  }

  public String wordCap(String str) {
    String[] b = str.split("\\s+");
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < b.length; i++) {
      sb.append(String.valueOf(b[i].charAt(0)).toUpperCase() + b[i].substring(1) + " ");
    }
    return sb.toString();
  }

  public String rmQuote(String str) {
    return str.replaceAll("\"", "").replaceAll("'", "");
  }

  public double toDouble(Object str) {
    if ("".equals(str)) {
      return 0.0D;
    }
    return Double.parseDouble(str.toString());
  }

  public float toFloat(Object str)
  {
    if ("".equals(str)) {
      return 0.0F;
    }
    return Float.parseFloat(str.toString());
  }

  public int toInt(Object str)
  {
    if ("".equals(str)) {
      return 0;
    }
    return Integer.parseInt(str.toString());
  }

  public long toLong(Object str)
  {
    return Long.parseLong(str.toString());
  }

  public Number toNumber(Object str) {
    char[] s = str.toString().toCharArray();

    int n = 0;
    int start = 0;
    int end = 0;
    boolean flag = true;
    for (int i = 0; i < s.length; i++)
      if ((s[i] >= '0') && (s[i] <= '9')) {
        n = 10 * n + (s[i] - '0');
        if (flag)
          start = n;
        else {
          end = n;
        }
      }
      else if (s[i] == '.') {
        n = 0;
        flag = false;
      }
    Number num;
    //Number num;
    if (flag)
      num = Integer.valueOf(start);
    else {
      num = Float.valueOf(start + Float.parseFloat("0." + end));
    }
    return num;
  }

  public String toStr(Object obj) {
    return obj.toString();
  }

  public String toDef(Object str, String pattern)
  {
    if (pattern.equals(""))
      pattern = "#,###0.00";
    DecimalFormat df = new DecimalFormat(pattern);
    if (str.equals(""))
      str = "0";
    try {
      return df.format(toDouble(str.toString())); } catch (Exception e) {
    }
    return str.toString();
  }

  public String toDwf(Object str, String pattern)
  {
    if (pattern.equals(""))
      pattern = "#,###0.00";
    DecimalFormat df = new DecimalFormat(pattern);
    if (str.equals(""))
      str = "0";
    try {
      return df.format(toDouble(str.toString()) / 10000.0D); } catch (Exception e) {
    }
    return "0";
  }

  public String toDtf(Object str, String frompattern, String topattern)
  {
    if (frompattern.equals(""))
      frompattern = "yyyy-MM-dd HH:mm:ss";
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(frompattern);
      Date dt = sdf.parse(str.toString());
      return new SimpleDateFormat(topattern).format(dt); } catch (Exception e) {
    }
    return str.toString();
  }

  public String sum(String[] args)
  {
    double expText = 0.0D;

    for (String str : args) {
      expText += Double.parseDouble(nvl(str, "0"));
    }
    return expText+"";
  }

  public String nvl(Object value1, String value2) {
    if ((value1 == null) || (value1.toString().equals(""))) {
      return value2;
    }
    return value1.toString();
  }

  public String img(Object[] url)
  {
    Object[] arr = url;
    if (url.length == 1)
      return "<img src=\"" + arr[0] + "\" width=\"100%\" height=\"100%\">";
    if (url.length == 2)
      return "<img src=\"" + arr[0] + "\">";
    if (url.length == 3) {
      return "<img src=\"" + arr[0] + "\" width=\"" + arr[1] + "\" height=\"" + arr[2] + "\">";
    }
    return "";
  }

  public String avg(String[] args)
  {
    double expint = 0.0D;
    for (String str : args) {
      expint += Double.parseDouble(nvl(str, "0"));
    }
    expint /= Double.parseDouble(args.length+"");
    expint = (int)(expint * 100.0D) / 100.0D;
    return expint+"";
  }

  public String max(String[] args) {
    double expint = 0.0D;
    for (String str : args) {
      double exp = Double.valueOf(nvl(str, "0")).doubleValue();
      if (expint < exp) {
        expint = exp;
      }
    }
    DecimalFormat df = new DecimalFormat("########0.00");
    return df.format(expint);
  }

  public String min(String[] args) {
    double expint = Double.parseDouble(args[0]);
    for (String str : args) {
      double exp = Double.parseDouble(nvl(str, "0"));
      if (expint > exp) {
        expint = exp;
      }
    }
    return expint+"";
  }

  public String group(String str) {
    return str;
  }
}
