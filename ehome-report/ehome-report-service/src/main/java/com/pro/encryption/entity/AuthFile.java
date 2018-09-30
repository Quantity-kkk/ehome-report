package com.pro.encryption.entity;

public class AuthFile
{
  private String authCus;
  private String proNo;
  private String authMacCode;
  private String authDateTime;
  private String authEndDate;
  private String authType;
  private String authMode;
  private int connects;
  private String ipAddress;
  private int maxConn;

  public String getAuthCus()
  {
    return this.authCus;
  }

  public void setAuthCus(String authCus) {
    this.authCus = authCus;
  }

  public String getProNo() {
    return this.proNo;
  }

  public void setProNo(String proNo) {
    this.proNo = proNo;
  }

  public String getAuthMacCode() {
    return this.authMacCode;
  }

  public void setAuthMacCode(String authMacCode) {
    this.authMacCode = authMacCode;
  }

  public String getAuthDateTime() {
    return this.authDateTime;
  }

  public void setAuthDateTime(String authDateTime) {
    this.authDateTime = authDateTime;
  }

  public String getAuthEndDate() {
    return this.authEndDate;
  }

  public void setAuthEndDate(String authEndDate) {
    this.authEndDate = authEndDate;
  }

  public String getAuthType() {
    return this.authType;
  }

  public void setAuthType(String authType) {
    this.authType = authType;
  }

  public String getAuthMode() {
    return this.authMode;
  }

  public void setAuthMode(String authMode) {
    this.authMode = authMode;
  }

  public int getConnects() {
    return this.connects;
  }

  public void setConnects(int connects) {
    this.connects = connects;
  }

  public String getIpAddress() {
    return this.ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public int getMaxConn() {
    return this.maxConn;
  }

  public void setMaxConn(int maxConn) {
    this.maxConn = maxConn;
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.entity.AuthFile
*
 */