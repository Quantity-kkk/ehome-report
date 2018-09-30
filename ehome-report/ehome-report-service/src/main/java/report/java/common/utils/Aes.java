package report.java.common.utils;

import java.io.PrintStream;
import java.math.BigInteger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

public class Aes
{
  private static final String KEY = "abcdefgabcdefg12";
  private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

  public static String aesDecrypt(String encrypt)
  {
    try
    {
      return aesDecrypt(encrypt, "abcdefgabcdefg12");
    } catch (Exception e) {
      e.printStackTrace();
    }return "";
  }

  public static String aesEncrypt(String content)
  {
    try
    {
      return aesEncrypt(content, "abcdefgabcdefg12");
    } catch (Exception e) {
      e.printStackTrace();
    }return "";
  }

  public static String binary(byte[] bytes, int radix)
  {
    return new BigInteger(1, bytes).toString(radix);
  }

  public static String base64Encode(byte[] bytes)
  {
    return Base64.encodeBase64String(bytes);
  }

  public static byte[] base64Decode(String base64Code)
    throws Exception
  {
    return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
  }

  public static byte[] aesEncryptToBytes(String content, String encryptKey)
    throws Exception
  {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128);
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(1, new SecretKeySpec(encryptKey.getBytes(), "AES"));

    return cipher.doFinal(content.getBytes("utf-8"));
  }

  public static String aesEncrypt(String content, String encryptKey)
    throws Exception
  {
    return base64Encode(aesEncryptToBytes(content, encryptKey));
  }

  public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey)
    throws Exception
  {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128);

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(2, new SecretKeySpec(decryptKey.getBytes(), "AES"));
    byte[] decryptBytes = cipher.doFinal(encryptBytes);
    return new String(decryptBytes);
  }

  public static String aesDecrypt(String encryptStr, String decryptKey)
    throws Exception
  {
    return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
  }

  public static void main(String[] args)
    throws Exception
  {
    String content = "123";
    System.out.println("加密前：" + content);
    System.out.println("加密密钥和解密密钥：abcdefgabcdefg12");
    String encrypt = aesEncrypt(content, "abcdefgabcdefg12");
    System.out.println("加密后：" + encrypt);
    String decrypt = aesDecrypt(encrypt, "abcdefgabcdefg12");
    System.out.println("解密后：" + decrypt);
  }
}

/* 
 * Qualified Name:     report.java.common.utils.Aes
*
 */