package com.pro.encryption.entrance.report;

import com.pro.log.UserLog;

public class AuthRepConstant
{
  public static final String HOME_DATA_REP_PATH = UserLog.getHomeDataRepPath();

  public static String LOCK_REP_PATH = HOME_DATA_REP_PATH + "\\dlok.dat";

  public static String LOCK_KEY_REP_PATH = HOME_DATA_REP_PATH + "\\dloky.dat";
  public static final String REPORT_IS_AUTH = "1";
  public static final String REPORT_AUTH = "2";
  public static final String REPORT_LOCK_FILE_NULL = "0";
  public static final String REPORT_LOCK_FILE_NOLOCKED = "1";
  public static final String REPORT_LOCK_FILE_LOCKED = "2";
  public static final String REPORT_AUTH_MSG_00 = "授权正常";
  public static final String REPORT_AUTH_MSG_04 = "试用授权正常";
  public static final String REPORT_AUTH_MSG_01 = "未找到授权信息";
  public static final String REPORT_AUTH_MSG_02 = "授权过期";
  public static final String REPORT_AUTH_MSG_03 = "授权机器码不匹配";
  public static final String REPORT_AUTH_TYPE_FOREVER = "01";
  public static final String REPORT_AUTH_TYPE_TIME = "02";
  public static final String REPORT_AUTH_TYPE_TEST = "03";
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.entrance.report.AuthRepConstant
*
 */