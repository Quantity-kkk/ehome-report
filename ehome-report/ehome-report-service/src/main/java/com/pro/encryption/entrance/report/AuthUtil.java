package com.pro.encryption.entrance.report;

import com.pro.encryption.entity.AuthFile;
import com.pro.encryption.entity.LockFile;
import com.pro.util.DateUtil;
import com.pro.util.SequenceUtil;
import java.io.File;
import java.util.Date;
import net.sf.json.JSONObject;

public class AuthUtil
{
  public static LockFile getLockFileInfo()
  {
    LockFile lockFile = null;
    String target = null;
    try
    {
      String LockRepPath = AuthRepConstant.LOCK_REP_PATH;
      if (SequenceUtil.getOsType().equals("LIUNX")) {
        LockRepPath = LockRepPath.replaceAll("\\\\", "/");
      }
      if (new File(LockRepPath).exists()) {
        target = EFSRepUtil.getRepLockFileContent();
        lockFile = (LockFile)JSONObject.toBean(JSONObject.fromObject(target), LockFile.class);
      } else {
        newInterimLockFile();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return lockFile;
  }

  public static void newAuthLockFile(AuthFile authFile)
  {
    LockFile lockFile = new LockFile();
    lockFile.setLockMacCode(authFile.getAuthMacCode());
    lockFile.setAuthMode(authFile.getAuthMode());
    lockFile.setLockDate(DateUtil.getDateTimeNoFormat());
    lockFile.setLockSts("0");
    lockFile.setAuthDateTime(authFile.getAuthDateTime());
    lockFile.setAuthEndDate(authFile.getAuthEndDate());
    EFSRepUtil.newRepLockFile(lockFile);
  }

  public static void newLockFile(String macCode, String authMode, String lockDate, String lockSts)
  {
    LockFile lockFile = new LockFile();
    lockFile.setLockMacCode(macCode);
    lockFile.setAuthMode(authMode);
    lockFile.setLockDate(lockDate);
    lockFile.setLockSts(lockSts);
    EFSRepUtil.newRepLockFile(lockFile);
  }

  public static void newInterimLockFile()
  {
    Date date = new Date();
    LockFile lockFile = new LockFile();
    lockFile.setLockMacCode(SequenceUtil.getSN());
    lockFile.setAuthMode("03");
    lockFile.setAuthType("03");
    lockFile.setLockDate("");
    lockFile.setLockSts("0");
    lockFile.setCheckDate(DateUtil.getDateTimeNoFormat());
    lockFile.setAuthDateTime(DateUtil.getDateTimeNoFormat());
    lockFile.setAuthEndDate(DateUtil.addByDay(DateUtil.getDateTimeNoFormat(), 30));
    EFSRepUtil.newRepLockFile(lockFile);
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.entrance.report.AuthUtil
*
 */