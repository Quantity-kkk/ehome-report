package com.pro.encryption.entity;

public class ProjLockFile
{
  private String proNo;
  private String authDateTime;
  private String authEndDate;
  private String lockDate;
  private String checkDate;
  private String lockSts;

  public String getProNo()
  {
    return this.proNo;
  }

  public void setProNo(String proNo) {
    this.proNo = proNo;
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

  public String getLockDate() {
    return this.lockDate;
  }

  public void setLockDate(String lockDate) {
    this.lockDate = lockDate;
  }

  public String getLockSts() {
    return this.lockSts;
  }

  public void setLockSts(String lockSts) {
    this.lockSts = lockSts;
  }

  public String getCheckDate() {
    return this.checkDate;
  }

  public void setCheckDate(String checkDate) {
    this.checkDate = checkDate;
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.entity.ProjLockFile
*
 */