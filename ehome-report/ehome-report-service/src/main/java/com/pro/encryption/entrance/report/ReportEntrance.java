package com.pro.encryption.entrance.report;

import com.pro.encryption.entity.LockFile;
import com.pro.util.DateUtil;
import com.pro.util.JarUtils;
import com.pro.util.SequenceUtil;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class ReportEntrance
{
  public static RepAuthEntity auth()
  {
    RepAuthEntity repAuth = new RepAuthEntity();
    String SNCode = SequenceUtil.getSN();

    if (SNCode != null) {
      LockFile lockFile = AuthUtil.getLockFileInfo();
      if (lockFile == null) {
        repAuth.setAuthFlag(false);
        repAuth.setLockType("0");
        repAuth.setAuthMsg("未找到授权信息");
        repAuth.setAuthType("03");

        AuthUtil.newInterimLockFile();
      } else if ("1".equals(lockFile.getLockSts())) {
        repAuth.setAuthFlag(false);
        repAuth.setLockType("2");
        repAuth.setAuthType(lockFile.getAuthType());
        repAuth.setAuthMsg("授权过期");
      }
      else if (SNCode.equals(lockFile.getLockMacCode())) {
        if (DateUtil.isBetween(lockFile.getAuthDateTime(), lockFile.getAuthEndDate())) {
          repAuth.setAuthFlag(true);
          repAuth.setLockType("1");
          repAuth.setAuthType(lockFile.getAuthType());
          if ("03".equals(lockFile.getAuthType()))
            repAuth.setAuthMsg("试用授权正常");
          else
            repAuth.setAuthMsg("授权正常");
        }
        else {
          repAuth.setAuthFlag(false);
          repAuth.setLockType("2");
          repAuth.setAuthMsg("授权过期");
          repAuth.setAuthType(lockFile.getAuthType());
          AuthUtil.newLockFile(lockFile.getLockMacCode(), lockFile.getAuthType(), DateUtil.getDateTimeNoFormat(), "1");
        }
      } else {
        repAuth.setAuthFlag(false);
        repAuth.setLockType("2");
        repAuth.setAuthMsg("授权机器码不匹配");
        repAuth.setAuthType(lockFile.getAuthType());
        AuthUtil.newLockFile(lockFile.getLockMacCode(), lockFile.getAuthType(), DateUtil.getDateTimeNoFormat(), "1");
      }
    }
    else
    {
      System.out.println("机器码获取失败！");
    }
    return repAuth;
  }

  public static Map<String, String> checkAuth() {
    Map returnMap = new HashMap();
    RepAuthEntity repAuth = auth();

    int expires = 1;
    if ("2".equals(repAuth.getLockType())) {
      expires = -1;
      if ("03".equals(repAuth.getAuthType()))
      {
        returnMap.put("show", "试用结束");
      }
      else
        returnMap.put("show", "授权过期");
    }
    else if ("1".equals(repAuth.getLockType())) {
      if ("03".equals(repAuth.getAuthType()))
        expires = 1;
      else {
        expires = 0;
      }
    }
    returnMap.put("expires", expires);
    return returnMap;
  }

  public static Map<String, String> getAuthMessage() {
    Map returnMap = new HashMap();
    String SNCode = SequenceUtil.getSN();
    returnMap.put("name", "报表工具");
    returnMap.put("version", JarUtils.getJarImplementationVersion());
    returnMap.put("systemType", SequenceUtil.getOsType());
    returnMap.put("sn", SequenceUtil.getSN());

    LockFile lockFile = AuthUtil.getLockFileInfo();
    returnMap.put("authSts", "0");
    if (lockFile == null) {
      returnMap.put("authType", "未授权");
      returnMap.put("authMsg", "未激活");
    }
    else if ("03".equals(lockFile.getAuthType())) {
      returnMap.put("authSts", "0");
      returnMap.put("authType", "未授权");
      returnMap.put("authMsg", "未激活");
    }
    else if (SNCode.equals(lockFile.getLockMacCode())) {
      if (DateUtil.isBetween(lockFile.getAuthDateTime(), lockFile.getAuthEndDate())) {
        returnMap.put("authSts", "1");
        returnMap.put("authType", "永久授权");
        returnMap.put("authMsg", "已激活");
      } else {
        returnMap.put("authType", "永久授权");
        returnMap.put("authMsg", "");
      }
    } else {
      returnMap.put("authType", "永久授权");
      returnMap.put("authMsg", "授权机器码不一致");
    }

    return returnMap;
  }

  public static void main(String[] args) {
    System.out.println(auth().toString());
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.entrance.report.ReportEntrance
*
 */