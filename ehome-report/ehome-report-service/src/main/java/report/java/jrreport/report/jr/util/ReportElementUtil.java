package report.java.jrreport.report.jr.util;

public class ReportElementUtil
{
  public static String getRplcExp(String expText)
  {
    String bo = "expFuntionChange.";

    expText = expText.replaceAll("select\\(", bo + "select(")
      .replaceAll("group\\(", bo + "group(")
      .replaceAll("gpc\\(", bo + "gpc(")
      .replaceAll("value\\(", bo + "value(")
      .replaceAll("sum\\(", bo + "sum")
      .replaceAll("count\\(", bo + "count(")
      .replaceAll("nvl\\(", bo + "nvl(")
      .replaceAll("img\\(", bo + "img(")
      .replaceAll("avg\\(", bo + "avg(")
      .replaceAll("max\\(", bo + "max(")
      .replaceAll("min\\(", bo + "min(")
      .replaceAll("day\\(", bo + "day(")
      .replaceAll("month\\(", bo + "month(")
      .replaceAll("year\\(", bo + "year(")
      .replaceAll("now\\(", bo + "now(")
      .replaceAll("time\\(", bo + "time(")
      .replaceAll("chn\\(", bo + "chn(")
      .replaceAll("left\\(", bo + "left(")
      .replaceAll("right\\(", bo + "right(")
      .replaceAll("ltrim\\(", bo + "ltrim(")
      .replaceAll("rtrim\\(", bo + "rtrim(")
      .replaceAll("trim\\(", bo + "trim(")
      .replaceAll("len\\(", bo + "len(")
      .replaceAll("lower\\(", bo + "lower(")
      .replaceAll("upper\\(", bo + "upper(")
      .replaceAll("mid\\(", bo + "mid(")
      .replaceAll("pos\\(", bo + "pos(")
      .replaceAll("rmb\\(", bo + "rmb(")
      .replaceAll("rplc\\(", bo + "rplc(")
      .replaceAll("space\\(", bo + "space(")
      .replaceAll("split\\(", bo + "split(")
      .replaceAll("wordCap\\(", bo + "wordCap(")
      .replaceAll("rmQuote\\(", bo + "rmQuote(")
      .replaceAll("date\\(", bo + "date")
      .replaceAll("dateTime\\(", bo + "dateTime(")
      .replaceAll("dateTime2\\(", bo + "dateTime2(")
      .replaceAll("toDouble\\(", bo + "toDouble(")
      .replaceAll("toFloat\\(", bo + "toFloat(")
      .replaceAll("toInt\\(", bo + "toInt(")
      .replaceAll("toLong\\(", bo + "toLong(")
      .replaceAll("toNumber\\(", bo + "toNumber(")
      .replaceAll("toStr\\(", bo + "toStr(")
      .replaceAll("toDef\\(", bo + "toDef(")
      .replaceAll("toDtf\\(", bo + "toDtf(")
      .replaceAll("toDwf\\(", bo + "toDwf(");
    return expText;
  }
}

/*
 * Qualified Name:     report.java.jrreport.report.jr.util.ReportElementUtil
*
 */