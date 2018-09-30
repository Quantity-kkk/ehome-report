package report.java.jrreport.util;

public abstract interface JRFunExp
{
  public abstract String select(String paramString);

  public abstract String select(String paramString, boolean paramBoolean);

  public abstract String gpc(Object[] paramArrayOfObject);

  public abstract String sum(String[] paramArrayOfString);

  public abstract String avg(String[] paramArrayOfString);

  public abstract String max(String[] paramArrayOfString);

  public abstract String min(String[] paramArrayOfString);

  public abstract int count();

  public abstract int count(boolean paramBoolean);

  public abstract int count(String paramString);

  public abstract String nvl(Object paramObject, String paramString);

  public abstract String img(Object[] paramArrayOfObject);

  public abstract String day(String paramString);

  public abstract String month(String paramString);

  public abstract String year(String paramString);

  public abstract String now();

  public abstract String time();

  public abstract String chn(String paramString);

  public abstract String chn(String paramString, boolean paramBoolean1, boolean paramBoolean2);

  public abstract String chn(String paramString, boolean paramBoolean);

  public abstract String left(String paramString, int paramInt);

  public abstract String right(String paramString, int paramInt);

  public abstract String mid(String paramString, int paramInt);

  public abstract String mid(String paramString, int paramInt1, int paramInt2);

  public abstract String ltrim(String paramString);

  public abstract String rtrim(String paramString);

  public abstract String trim(String paramString);

  public abstract int len(String paramString);

  public abstract String lower(String paramString);

  public abstract String upper(String paramString);

  public abstract int pos(String paramString1, String paramString2);

  public abstract int pos(String paramString1, String paramString2, int paramInt);

  public abstract String rmb(Number paramNumber);

  public abstract String rmb(String paramString);

  public abstract String rplc(String paramString1, String paramString2, String paramString3);

  public abstract String space(int paramInt);

  public abstract String[] split(String paramString1, String paramString2);

  public abstract String wordCap(String paramString);

  public abstract String rmQuote(String paramString);

  public abstract String date(String paramString);

  public abstract String dateTime(String paramString);

  public abstract String dateTime(Long paramLong);

  public abstract String dateTime2(String paramString1, String paramString2);

  public abstract double toDouble(Object paramObject);

  public abstract float toFloat(Object paramObject);

  public abstract int toInt(Object paramObject);

  public abstract long toLong(Object paramObject);

  public abstract Number toNumber(Object paramObject);

  public abstract String toStr(Object paramObject);

  public abstract String toDef(Object paramObject, String paramString);

  public abstract String toDtf(Object paramObject, String paramString1, String paramString2);

  public abstract String toDwf(Object paramObject, String paramString);

  public abstract String group(String paramString);
}

/*
 * Qualified Name:     report.java.jrreport.util.JRFunExp
*
 */