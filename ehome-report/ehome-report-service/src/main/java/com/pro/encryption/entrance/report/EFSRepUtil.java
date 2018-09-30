package com.pro.encryption.entrance.report;

import com.pro.efs.AESUtils;
import com.pro.efs.Base64Utils;
import com.pro.encryption.entity.LockFile;
import com.pro.log.Logger;
import net.sf.json.JSONObject;

public class EFSRepUtil
{
  public static void newRepLockFile(LockFile lockFile)
  {
    try
    {
      String data = JSONObject.fromObject(lockFile).toString();

      byte[] key = AESUtils.genKeyPair();
      String AESKey = Base64Utils.encode(key);
      byte[] encodedData = AESUtils.encryptByKey(data.getBytes(), Base64Utils.decode(AESKey));
      Base64Utils.byteArrayToFile(encodedData, AuthRepConstant.LOCK_REP_PATH);

      Base64Utils.byteArrayToFile(AESKey.getBytes(), AuthRepConstant.LOCK_KEY_REP_PATH);
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
  }

  public static String getRepLockFileContent()
  {
    String target = null;
    try {
      byte[] data = Base64Utils.fileToByte(AuthRepConstant.LOCK_REP_PATH);
      byte[] key = Base64Utils.fileToByte(AuthRepConstant.LOCK_KEY_REP_PATH);
      byte[] decodedData = AESUtils.decryptByKey(data, Base64Utils.decode(new String(key)));
      target = new String(decodedData);
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
    return target;
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.entrance.report.EFSRepUtil
*
 */