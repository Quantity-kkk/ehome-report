package com.pro.encryption.entrance.report;

import com.pro.efs.Base64Utils;
import com.pro.efs.RSAUtils;
import com.pro.encryption.entity.AuthFile;
import com.pro.log.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import net.sf.json.JSONObject;

public class SocketClientUtil
{
  public static final String IP = "";

  public static JSONObject sendSocketMsg(JSONObject authObj)
  {
    Socket socket = null;
    JSONObject obj = null;
    try
    {
      if ("" != null) {
        String ip = "".split(":")[0];
        int port = Integer.parseInt("".split(":")[1]);
        socket = new Socket(ip, port);
        socket.setSoTimeout(20000);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        DataInputStream input = new DataInputStream(socket.getInputStream());

        String oet = authObj.toString();
        byte[] encryptedData = RSAUtils.encryptByPublicKey(oet.getBytes(), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKLKNffC6L5/GlEOBtpoL51e5eXniw6TA4cEMQcBbAdEt8wAhsx5edUeWWi3M686vkgb6Q7rzLbZLXr+Ix+o3YWmNsCRIVa2rWf0L0qD5lkTyLCObqNtls/VhEC6GF0M9fFsIFv/ZTaEshjoHCZPD6oYCO3k1dtjujHCuBRpA/2wIDAQAB");
        out.writeUTF(Base64Utils.encode(encryptedData));

        String ret = input.readUTF();
        byte[] decodedData = RSAUtils.decryptByPublicKey(Base64Utils.decode(ret), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKLKNffC6L5/GlEOBtpoL51e5eXniw6TA4cEMQcBbAdEt8wAhsx5edUeWWi3M686vkgb6Q7rzLbZLXr+Ix+o3YWmNsCRIVa2rWf0L0qD5lkTyLCObqNtls/VhEC6GF0M9fFsIFv/ZTaEshjoHCZPD6oYCO3k1dtjujHCuBRpA/2wIDAQAB");
        ret = new String(decodedData);
        obj = JSONObject.fromObject(ret);

        out.close();
        input.close();
      }
    } catch (Exception e) {
      Logger.error(e.getMessage());

      if (socket != null)
        try {
          socket.close();
        } catch (IOException e1) {
          Logger.error(e1.toString());
          socket = null;
        }
    }
    finally
    {
      if (socket != null) {
        try {
          socket.close();
        } catch (IOException e) {
          Logger.error(e.toString());
          socket = null;
        }
      }
    }

    return obj;
  }

  public static void main(String[] args) throws Exception {
    System.out.println("客户端测试开始...");
    AuthFile authFile = new AuthFile();
    authFile.setAuthMacCode("hhhhhhhhh");
    authFile.setProNo("10");
    authFile.setAuthDateTime("2018-11-11 11:11:11");
    authFile.setAuthType("1");
    JSONObject obj = JSONObject.fromObject(authFile);
    obj.put("handlerType", "0");

    System.out.println("客户端测试结束...");
    try {
      String a = null;
      a.split(",");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.entrance.report.SocketClientUtil
*
 */