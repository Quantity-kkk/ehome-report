package com.pro.util;

import com.pro.encryption.entity.LockFile;
import com.pro.encryption.entrance.report.AuthUtil;
import java.io.PrintStream;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WatchLockFileUtil
{
  public static void main(String[] args)
  {
    LockFile lockFile = AuthUtil.getLockFileInfo();

    System.out.println(">>>>>>>>>>>>>>>>>>>>锁文件信息开始<<<<<<<<<<<<<<<<<<<<");

    System.out.println("授权开始日期：             " + parseDate(lockFile.getAuthDateTime()));
    System.out.println("授权结束日期：             " + parseDate(lockFile.getAuthEndDate()));
    String mode = "0".equals(lockFile.getAuthMode()) ? "内网" : "外网";
    System.out.println("授权模式：                       " + mode);
    String type = "0".equals(lockFile.getAuthType()) ? "开发" : "生产";
    System.out.println("授权类型：                       " + lockFile.getAuthType());
    System.out.println("授权类型：                       " + type);
    System.out.println("验证日期：                       " + lockFile.getCheckDate());
    System.out.println("最大连接次数：             " + lockFile.getCheckMax() + "(次)");
    System.out.println("单次连接网络次数：   " + lockFile.getConnectMax() + "(次)");
    String sts = "0".equals(lockFile.getLockSts()) ? "未锁定" : "锁定";
    System.out.println("锁定状态：                       " + lockFile.getLockSts());
    System.out.println("锁定状态：                       " + sts);
    System.out.println("锁定日期：                       " + lockFile.getLockDate());
    System.out.println("锁定项目：                       ");

    JSONArray list = JSONArray.fromObject(lockFile.getProjLockFileList());
    for (int i = 0; i < list.size(); i++) {
      System.out.println("         |-项目编号：              " + list.getJSONObject(i).get("proNo"));
      System.out.println("         |-授权日期：              " + parseDate(list.getJSONObject(i).getString("authDateTime")));
      System.out.println("         |-授权到期日：         " + parseDate(list.getJSONObject(i).getString("authEndDate")));
      System.out.println("         |-锁定日期：              " + list.getJSONObject(i).getString("lockDate"));
      System.out.println("         |-验证日期：              " + list.getJSONObject(i).getString("checkDate"));
      String sts2 = "0".equals(list.getJSONObject(i).getString("lockSts")) ? "未锁定" : "锁定";
      System.out.println("         |-锁定状态：              " + sts2);
      System.out.println("");
    }
    System.out.println(">>>>>>>>>>>>>>>>>>>>锁文件信息结束<<<<<<<<<<<<<<<<<<<<");
  }

  public static String parseDate(String time)
  {
    return DateUtil.getDataByStr(time, DateUtil.nodfTime, DateUtil.dfTime);
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.util.WatchLockFileUtil
*
 */