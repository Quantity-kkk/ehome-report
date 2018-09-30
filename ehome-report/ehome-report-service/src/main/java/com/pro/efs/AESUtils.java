package com.pro.efs;

import java.io.PrintStream;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils
{
  public static byte[] genKeyPair()
    throws Exception
  {
    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    keygen.init(128);
    SecretKey secretKey = keygen.generateKey();
    byte[] key = secretKey.getEncoded();
    return key;
  }

  public static byte[] encryptByKey(byte[] data, byte[] key)
    throws Exception
  {
    SecretKey secretKey = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(1, secretKey);
    byte[] cipherByte = cipher.doFinal(data);
    return cipherByte;
  }

  public static byte[] decryptByKey(byte[] data, byte[] key)
    throws Exception
  {
    SecretKey secretKey = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(2, secretKey);
    byte[] cipherByte = cipher.doFinal(data);
    return cipherByte;
  }

  public static void main(String[] args) throws Exception {
    byte[] key = genKeyPair();
    System.out.println("\r密钥：\r\n" + new String(key));
    String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
    System.out.println("\r加密前文字：\r\n" + source);
    byte[] data = source.getBytes();
    byte[] encodedData = encryptByKey(data, key);
    System.out.println("加密后文字：\r\n" + new String(encodedData));
    byte[] decodedData = decryptByKey(encodedData, key);
    String target = new String(decodedData);
    System.out.println("解密后文字: \r\n" + target);
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.efs.AESUtils
*
 */