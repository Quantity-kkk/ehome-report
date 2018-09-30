package com.pro.encryption.entity;

import java.util.List;

public class LockFile
{
  private String lockCus;
  private String lockMacCode;
  private String authDateTime;
  private String authEndDate;
  private String authType;
  private String authMode;
  private String checkDate;
  private int connectMax;
  private int checkMax;
  private String lockDate;
  private String lockSts;
  private String version;
  private List<ProjLockFile> projLockFileList;

  public String getLockCus()
  {
    return this.lockCus;
  }

  public void setLockCus(String lockCus) {
    this.lockCus = lockCus;
  }

  public String getLockMacCode() {
    return this.lockMacCode;
  }

  public void setLockMacCode(String lockMacCode) {
    this.lockMacCode = lockMacCode;
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

  public String getCheckDate() {
    return this.checkDate;
  }

  public void setCheckDate(String checkDate) {
    this.checkDate = checkDate;
  }

  public int getConnectMax() {
    return this.connectMax;
  }

  public void setConnectMax(int connectMax) {
    this.connectMax = connectMax;
  }

  public int getCheckMax() {
    return this.checkMax;
  }

  public void setCheckMax(int checkMax) {
    this.checkMax = checkMax;
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

  public List<ProjLockFile> getProjLockFileList() {
    return this.projLockFileList;
  }

  public void setProjLockFileList(List<ProjLockFile> projLockFileList) {
    this.projLockFileList = projLockFileList;
  }

  public String getVersion() {
    return this.version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.entity.LockFile
*
 */