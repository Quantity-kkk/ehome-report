package report.java.login.bean;

import java.util.Map;
import report.java.common.utils.PropertyUtil;

public class User
{
  private String username;
  private String password;
  public static Map<String, String> userMap;

  public User()
  {
    if (userMap == null) {
      userMap = PropertyUtil.getPropertyMap("user.properties");
    }
    this.username = ((String)userMap.get("user.username"));
    this.password = ((String)userMap.get("user.password"));
  }

  public String getUsername()
  {
    return this.username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
}

/*
 * Qualified Name:     report.java.login.bean.User
*
 */