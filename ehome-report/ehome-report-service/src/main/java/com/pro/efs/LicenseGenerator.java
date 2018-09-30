package com.pro.efs;

import com.pro.encryption.AuthConstant;
import java.io.PrintStream;

public class LicenseGenerator
{
  private static String licensestatic = "{\"authMode\":\"1\",\"proNo\":\"100001&169\",\"connects\":0,\"authEndDate\":\"20180210000000000\",\"authMacCode\":\"Y1049J810QLYFP3E605000FFBFBEFB\",\"authDateTime\":\"20180201000000000\",\"authType\":\"0\",\"authCus\":\"socket通讯\"}";

  private static String licensestatic2 = "{\"authCus\":\"guohanchen\",\"proNo\":\"1111\",\"authMacCode\":\"123123123\",\"authDateTime\":\"20170101\",\"authEndDate\":\"20180101\",\"authType\":\"0\",\"authMode\":\"0\"}";
  private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaa8y9g+ioR2OSs8njT5uCGWm0YmOA1elSu/P5\nD3XYPCHsUPab74V5Og+NEZeTk9/LtG+jPSpKekTWm67gS2lQYWoygTwnoWsr4woaqNXmWw7L8Ty0\nLQFTojZgyynvu2RSIJp4c76z6SV/khiP/ireGtt8uzXPswPO5uaPU38tQQIDAQAB";
  private static final String AESKey = "weYCTqZCqwEJa1ipLBIsEg==";
  private static final String AESKey2 = "icQSsXNTSpoX8Hc3kIV6XA==";
  public static final String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJprzL2D6KhHY5KzyeNPm4IZabRi\nY4DV6VK78/kPddg8IexQ9pvvhXk6D40Rl5OT38u0b6M9Kkp6RNabruBLaVBhajKBPCehayvjChqo\n1eZbDsvxPLQtAVOiNmDLKe+7ZFIgmnhzvrPpJX+SGI/+Kt4a23y7Nc+zA87m5o9Tfy1BAgMBAAEC\ngYAVnlfohEoTHQN0q1TtTNzRhutEhK23gLsMiSGr0Z1G64w4QFF2HT9LbHR25GqbD426QAWNDegY\nyytN/DesUQJqNXx8vuEuqs7+MQDgKgJqpAx+Fg3Iwsk/SVjq7meaSVGCgPKhtWHJk5oXoRMpsrlT\nAwUjpdpAZXIIKW3mrqkW0QJBANq4INw6lZlqRFtxT4uzYQYtzmB/nxMnCbL2SQ4ZQ/4CWlQpOnR/\nmH2JxIBCVtTADFlPM0DWF4aoqykYs9tu2X0CQQC0vgEk8DpkQbh1kgIyBGWCqYSKISTSXia0rbYo\nFPnzdldgtZVirNGNmiJGL8RPz0YKpZNOg9FLHq/oYXSNFI4VAkAJ4OcbC0pWc4ZC2wtMs/1d2hPI\nJ/t3UfwOKTGDgYCgqFqMEpChUmIAyYgmgtiJI2NrZThbZVAKtPOGF6eH8anBAkAbxkL4wS3H8E1/\nS7OoqgJLZO9oJpW4+hzqkPM4D5klb58Xzm+pXTNKllAEBx0cwpZZ1n3fh+Qmrg2MIUW+1FTNAkBt\nWECowLUqW014M96WsFpiof7kjteOBNOjFyxhIbx2eT7//bnrADfq2Xu1/mSedUKrjGr/O+FRi7PO\nu7WhF6C9";

  public static void generator()
    throws Exception
  {
    System.err.println("生成授权文件");

    byte[] data = licensestatic.getBytes();
    byte[] encodedData = RSAUtils.encryptByPrivateKey(data, "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJprzL2D6KhHY5KzyeNPm4IZabRi\nY4DV6VK78/kPddg8IexQ9pvvhXk6D40Rl5OT38u0b6M9Kkp6RNabruBLaVBhajKBPCehayvjChqo\n1eZbDsvxPLQtAVOiNmDLKe+7ZFIgmnhzvrPpJX+SGI/+Kt4a23y7Nc+zA87m5o9Tfy1BAgMBAAEC\ngYAVnlfohEoTHQN0q1TtTNzRhutEhK23gLsMiSGr0Z1G64w4QFF2HT9LbHR25GqbD426QAWNDegY\nyytN/DesUQJqNXx8vuEuqs7+MQDgKgJqpAx+Fg3Iwsk/SVjq7meaSVGCgPKhtWHJk5oXoRMpsrlT\nAwUjpdpAZXIIKW3mrqkW0QJBANq4INw6lZlqRFtxT4uzYQYtzmB/nxMnCbL2SQ4ZQ/4CWlQpOnR/\nmH2JxIBCVtTADFlPM0DWF4aoqykYs9tu2X0CQQC0vgEk8DpkQbh1kgIyBGWCqYSKISTSXia0rbYo\nFPnzdldgtZVirNGNmiJGL8RPz0YKpZNOg9FLHq/oYXSNFI4VAkAJ4OcbC0pWc4ZC2wtMs/1d2hPI\nJ/t3UfwOKTGDgYCgqFqMEpChUmIAyYgmgtiJI2NrZThbZVAKtPOGF6eH8anBAkAbxkL4wS3H8E1/\nS7OoqgJLZO9oJpW4+hzqkPM4D5klb58Xzm+pXTNKllAEBx0cwpZZ1n3fh+Qmrg2MIUW+1FTNAkBt\nWECowLUqW014M96WsFpiof7kjteOBNOjFyxhIbx2eT7//bnrADfq2Xu1/mSedUKrjGr/O+FRi7PO\nu7WhF6C9");
    Base64Utils.byteArrayToFile(encodedData, "C:\\Users\\Administrator\\Desktop\\ras\\license.dat");
    String encodedDataSSS = Base64Utils.encode(encodedData);
    System.out.println(encodedDataSSS);

    System.err.println("生成密钥文件(公钥，签名)");
    String sign = RSAUtils.sign(encodedData, "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJprzL2D6KhHY5KzyeNPm4IZabRi\nY4DV6VK78/kPddg8IexQ9pvvhXk6D40Rl5OT38u0b6M9Kkp6RNabruBLaVBhajKBPCehayvjChqo\n1eZbDsvxPLQtAVOiNmDLKe+7ZFIgmnhzvrPpJX+SGI/+Kt4a23y7Nc+zA87m5o9Tfy1BAgMBAAEC\ngYAVnlfohEoTHQN0q1TtTNzRhutEhK23gLsMiSGr0Z1G64w4QFF2HT9LbHR25GqbD426QAWNDegY\nyytN/DesUQJqNXx8vuEuqs7+MQDgKgJqpAx+Fg3Iwsk/SVjq7meaSVGCgPKhtWHJk5oXoRMpsrlT\nAwUjpdpAZXIIKW3mrqkW0QJBANq4INw6lZlqRFtxT4uzYQYtzmB/nxMnCbL2SQ4ZQ/4CWlQpOnR/\nmH2JxIBCVtTADFlPM0DWF4aoqykYs9tu2X0CQQC0vgEk8DpkQbh1kgIyBGWCqYSKISTSXia0rbYo\nFPnzdldgtZVirNGNmiJGL8RPz0YKpZNOg9FLHq/oYXSNFI4VAkAJ4OcbC0pWc4ZC2wtMs/1d2hPI\nJ/t3UfwOKTGDgYCgqFqMEpChUmIAyYgmgtiJI2NrZThbZVAKtPOGF6eH8anBAkAbxkL4wS3H8E1/\nS7OoqgJLZO9oJpW4+hzqkPM4D5klb58Xzm+pXTNKllAEBx0cwpZZ1n3fh+Qmrg2MIUW+1FTNAkBt\nWECowLUqW014M96WsFpiof7kjteOBNOjFyxhIbx2eT7//bnrADfq2Xu1/mSedUKrjGr/O+FRi7PO\nu7WhF6C9");
    String keyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaa8y9g+ioR2OSs8njT5uCGWm0YmOA1elSu/P5\nD3XYPCHsUPab74V5Og+NEZeTk9/LtG+jPSpKekTWm67gS2lQYWoygTwnoWsr4woaqNXmWw7L8Ty0\nLQFTojZgyynvu2RSIJp4c76z6SV/khiP/ireGtt8uzXPswPO5uaPU38tQQIDAQABdh" + sign;
    byte[] encodedDataKey = AESUtils.encryptByKey(keyStr.getBytes(), Base64Utils.decode("weYCTqZCqwEJa1ipLBIsEg=="));
    Base64Utils.byteArrayToFile(encodedDataKey, "C:\\Users\\Administrator\\Desktop\\ras\\key.dat");

    System.err.println("生成项目编号文件(产品线，项目)");
    String projStr = "100001&169";
    byte[] encodedProjNo = AESUtils.encryptByKey(projStr.getBytes(), Base64Utils.decode("icQSsXNTSpoX8Hc3kIV6XA=="));
    Base64Utils.byteArrayToFile(encodedProjNo, "C:\\Users\\Administrator\\Desktop\\ras\\proj.dat");

    byte[] encodedProjNo2 = Base64Utils.fileToByte("C:\\Users\\Administrator\\Desktop\\ras\\proj.dat");
    byte[] encodedDataKey2 = AESUtils.decryptByKey(encodedProjNo2, Base64Utils.decode("icQSsXNTSpoX8Hc3kIV6XA=="));
    String keyStr3 = new String(encodedDataKey2);
    System.out.println(keyStr3);
  }

  public static void main(String[] args)
    throws Exception
  {
    System.out.println(AuthConstant.LOCK_PATH);
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.efs.LicenseGenerator
*
 */