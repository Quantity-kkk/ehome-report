package com.pro.efs;

import com.pro.encryption.AuthConstant;
import com.pro.encryption.entity.LockFile;
import com.pro.log.Logger;
import net.sf.json.JSONObject;

public class EFSUtil
{
  public static String getAuthFileContent()
  {
    String target = null;
    try {
      byte[] encodedData = Base64Utils.fileToByte(AuthConstant.LICENSE_PATH);
      String publicKey = getPublicKey(encodedData);
      if (publicKey != null) {
        byte[] decodedData = RSAUtils.decryptByPublicKey(encodedData, publicKey);
        target = new String(decodedData, "UTF-8");
      }
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
    return target;
  }

  public static String getPublicKey(byte[] encodedData)
  {
    try
    {
      String keyStr = getKeyFileContent();
      String publicKey = keyStr.split("dh")[0];
      String signStr = keyStr.split("dh")[1];
      boolean status = RSAUtils.verify(encodedData, publicKey, signStr);
      if (status) {
        return publicKey;
      }
      return null;
    }
    catch (Exception e) {
      Logger.error(e.getMessage());
    }return null;
  }

  public static String getKeySign()
  {
    String keyStr = getKeyFileContent();
    String signStr = keyStr.split("dh")[1];
    return signStr;
  }

  public static String getKeyFileContent()
  {
    try
    {
      byte[] encodedData = Base64Utils.fileToByte(AuthConstant.LICENSE_KEY_PATH);
      byte[] encodedDataKey = AESUtils.decryptByKey(encodedData, Base64Utils.decode("weYCTqZCqwEJa1ipLBIsEg=="));
      String keyStr = new String(encodedDataKey);
      return keyStr;
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
    return null;
  }

  public static String getProjFileContent()
  {
    try
    {
      byte[] encodedData = Base64Utils.fileToByte(AuthConstant.PROJ_NO_PATH);
      byte[] encodedDataKey = AESUtils.decryptByKey(encodedData, Base64Utils.decode("icQSsXNTSpoX8Hc3kIV6XA=="));
      String keyStr = new String(encodedDataKey);
      return keyStr;
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
    return null;
  }

  public static void newLockFile(LockFile lockFile) {
    try {
      String data = JSONObject.fromObject(lockFile).toString();

      byte[] key = AESUtils.genKeyPair();
      String AESKey = Base64Utils.encode(key);
      byte[] encodedData = AESUtils.encryptByKey(data.getBytes(), Base64Utils.decode(AESKey));
      Base64Utils.byteArrayToFile(encodedData, AuthConstant.LOCK_PATH);

      Base64Utils.byteArrayToFile(AESKey.getBytes(), AuthConstant.LOCK_KEY_PATH);
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
  }

  public static void newDevpLockFile(LockFile lockFile) {
    try {
      String data = JSONObject.fromObject(lockFile).toString();

      byte[] key = AESUtils.genKeyPair();
      String AESKey = Base64Utils.encode(key);
      byte[] encodedData = AESUtils.encryptByKey(data.getBytes(), Base64Utils.decode(AESKey));
      Base64Utils.byteArrayToFile(encodedData, AuthConstant.LOCK_DEVP_PATH);

      Base64Utils.byteArrayToFile(AESKey.getBytes(), AuthConstant.LOCK_KEY_DEVP_PATH);
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
  }

  public static void updateAuthFile(String AuthStr)
  {
    try
    {
      byte[] encodedData = Base64Utils.decode(AuthStr);
      Base64Utils.byteArrayToFile(encodedData, AuthConstant.LICENSE_PATH);
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
  }

  public static String getLockFileContent()
  {
    String target = null;
    try {
      byte[] data = Base64Utils.fileToByte(AuthConstant.LOCK_PATH);
      byte[] key = Base64Utils.fileToByte(AuthConstant.LOCK_KEY_PATH);
      byte[] decodedData = AESUtils.decryptByKey(data, Base64Utils.decode(new String(key)));
      target = new String(decodedData);
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
    return target;
  }

  public static String getDevpLockFileContent()
  {
    String target = null;
    try {
      byte[] data = Base64Utils.fileToByte(AuthConstant.LOCK_DEVP_PATH);
      byte[] key = Base64Utils.fileToByte(AuthConstant.LOCK_KEY_DEVP_PATH);
      byte[] decodedData = AESUtils.decryptByKey(data, Base64Utils.decode(new String(key)));
      target = new String(decodedData);
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
    return target;
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.efs.EFSUtil
*
 */