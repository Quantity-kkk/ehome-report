package com.pro.encryption;

import com.pro.log.UserLog;
import com.pro.util.EnProperty;

public class AuthConstant
{
  public static final String HOME_DATA_PATH = UserLog.getHomeDataPath();

  public static final String HOME_DATA_DEVP_PATH = UserLog.getHomeDataDevpPath();

  public static final String PROJ_PROP_PATH = EnProperty.getPropValue("configPath").trim();

  public static final String LICENSE_PATH = PROJ_PROP_PATH + "\\data\\listener\\license.dat";

  public static final String LICENSE_KEY_PATH = PROJ_PROP_PATH + "\\data\\listener\\key.dat";

  public static final String PROJ_NO_PATH = HOME_DATA_PATH + "\\proj.dat";

  public static final String LOCK_PATH = HOME_DATA_PATH + "\\lok.dat";

  public static final String LOCK_KEY_PATH = HOME_DATA_PATH + "\\loky.dat";

  public static final String LOCK_DEVP_PATH = HOME_DATA_DEVP_PATH + "\\dlok.dat";

  public static final String LOCK_KEY_DEVP_PATH = HOME_DATA_DEVP_PATH + "\\dloky.dat";
  public static final String AES_KEY = "weYCTqZCqwEJa1ipLBIsEg==";
  public static final String AES_PROJ_KEY = "icQSsXNTSpoX8Hc3kIV6XA==";
  public static final String AES_MACHINE_KEY = "YrEfF0nVbwxWtSUfz2ZqWg==";
  public static final String AES_SERVER_KEY = "RqPR4DyiUM6Uyxk7Y94F8A==";
  public static final String RSA_COM_PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKLKNffC6L5/GlEOBtpoL51e5eXniw6TA4cEMQcBbAdEt8wAhsx5edUeWWi3M686vkgb6Q7rzLbZLXr+Ix+o3YWmNsCRIVa2rWf0L0qD5lkTyLCObqNtls/VhEC6GF0M9fFsIFv/ZTaEshjoHCZPD6oYCO3k1dtjujHCuBRpA/2wIDAQAB";
  public static final String TYPE_DELP = "0";
  public static final String TYPE_PROD = "1";
  public static final String MODE_OFFLINE = "0";
  public static final String MODE_ONLINE = "1";
  public static final String LOCKSTS_NOLOCK = "0";
  public static final String LOCKSTS_LOCKED = "1";
  public static final String LOCKSTS_DELP = "9";
  public static final String HANDLER_TYPE_AUTH = "1";
  public static final String HANDLER_TYPE_LOCK = "0";
  public static final String HANDLER_TYPE_CONNECT = "2";
  public static final String HANDLER_TYPE_GET = "3";
  public static final String NET_AUTH_SUCCESS = "1";
  public static final String NET_AUTH_FIELD = "0";
  public static final String AUTH_TYPE_FORM = "form";
  public static final String AUTH_TYPE_APP = "app";
  public static final String APP_LOCK_FILE_NULL = "0";
  public static final String APP_LOCK_FILE_NOLOCKED = "1";
  public static final String APP_LOCK_FILE_LOCKED = "2";
  public static final String APP_CHECK_CONNECT = "0";
  public static final String APP_IS_AUTH = "1";
  public static final String APP_AUTH = "2";
  public static final String APP_CREATE_ORDER = "3";
  public static final String APP_QUERY_AUTH = "4";
  public static final String APP_QUERY_PRODUCT = "5";
  public static final String APP_AUTH_TYPE_PERSONAL = "0";
  public static final String APP_AUTH_TYPE_BUS_V1 = "1";
  public static final String REPORT_AUTH_TYPE_FOREVER = "01";
  public static final String REPORT_AUTH_TYPE_TIME = "02";
  public static final String ERRERCODE_AUTH_LOCKED = "锁定文件被锁定授权无法通过.";
  public static final String ERRERCODE_PROJ_LOCKED = "项目已被锁定授权无法通过.";
  public static final String ERRERCODE_MACCODE_NULL = "获取机器码失败.";
  public static final String ERRERCODE_PRO_NO_NULL = "获取本地项目编号失败.";
  public static final String ERRERCODE_MACCODE_ERRER = "机器与授权文件不匹配.";
  public static final String ERRERCODE_PRONO_ERRER = "本地项目与授权文件不匹配.";
  public static final String ERRERCODE_PRONO_UNFIND = "未找到本地项目的项目编号.";
  public static final String ERRERCODE_PROJ_NOTFOUND = "本地项目不存在锁文件中.";
  public static final String ERRERCODE_AUTH_OVER = "授权已过期.";
  public static final String ERRERCODE_AUTH_FILE = "获取授权文件失败.";
  public static final String ERRERCODE_LOCK_FILE = "获取锁文件失败.";
  public static final String ERRERCODE_USE_NATIVE = "无法联网，请使用激活码.";
  public static final String UNLOCKCODE_NULL = "解锁码不能为空.";
  public static final String UNACTIVECODE_NULL = "激活码不能为空.";
  public static final String UNACTIVECODE_SUCCESS = "激活成功.";
  public static final String UNACTIVECODE_INVALID = "激活码失效.";
  public static final String UNACTIVECODE_UNSNCODE = "激活失败,机器码不匹配.";
  public static final String UNACTIVECODE_FAILED = "激活失败.";
  public static final String UNLOCKCODE_SUCCESS = "解锁成功.";
  public static final String UNLOCKCODE_FAILED = "解锁失败.";
  public static final String UNLOCKCODE_UNEQ = "解锁失败,项目不匹配";
  public static final String UNLOCKCODE_UNSNCODE = "解锁失败,机器码不匹配";
  public static final String AUTH_TYPE_REPORT = "report";
  public static final String REPORT_IS_AUTH = "1";
  public static final String REPORT_AUTH = "2";
  public static final String REPORT_LOCK_FILE_NULL = "0";
  public static final String REPORT_LOCK_FILE_NOLOCKED = "1";
  public static final String REPORT_LOCK_FILE_LOCKED = "2";
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.encryption.AuthConstant
*
 */