package report.java.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption
{
  public static String md5(String msg)
    throws NoSuchAlgorithmException, UnsupportedEncodingException
  {
    MessageDigest md = MessageDigest.getInstance("MD5");

    byte[] input = msg.getBytes();

    byte[] output = md.digest(input);

    String result = Base64.encoder(output);
    return result;
  }
}

/* 
 * Qualified Name:     report.java.common.utils.Encryption
*
 */