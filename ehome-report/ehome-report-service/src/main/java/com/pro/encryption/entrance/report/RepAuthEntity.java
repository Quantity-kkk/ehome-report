package com.pro.encryption.entrance.report;

public class RepAuthEntity
{
  private boolean authFlag;
  private String authType;
  private String authMsg;
  private String startTime;
  private String endTime;
  private String lockType;

  public String getLockType()
  {
    return this.lockType;
  }

  public void setLockType(String lockType) {
    this.lockType = lockType;
  }

  public boolean isAuthFlag()
  {
    return this.authFlag;
  }

  public void setAuthFlag(boolean authFlag) {
    this.authFlag = authFlag;
  }

  public String getAuthType() {
    return this.authType;
  }

  public void setAuthType(String authType) {
    this.authType = authType;
  }

  public String getAuthMsg() {
    return this.authMsg;
  }

  public void setAuthMsg(String authMsg) {
    this.authMsg = authMsg;
  }

  public String getStartTime() {
    return this.startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return this.endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String toString()
  {
    return "RepAuthEntity [authFlag=" + this.authFlag + ", authType=" + this.authType + 
      ", authMsg=" + this.authMsg + ", lockType=" + this.lockType + "]";
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.entrance.report.RepAuthEntity
*
 */