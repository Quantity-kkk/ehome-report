package report.java.login.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import report.java.base.BaseAction;
import report.java.base.LoginSessionListener;
import report.java.common.utils.Encryption;
import report.java.login.bean.User;

public class LoginAction extends BaseAction
{
  private static final long serialVersionUID = 3139595843532262240L;
  private String username;
  private String password;
  private String code;
  private Map<String, Object> dataMap;

  public String login()
    throws Exception
  {
    return "login";
  }

  public String loginAjax()
    throws Exception
  {
    this.dataMap = new HashMap();
    this.dataMap.put("flag", Boolean.valueOf(false));
    if (("".equals(this.username)) || (this.username == null)) {
      this.dataMap.put("msg", "用户名不能为空！");
      return "success";
    }
    if (("".equals(this.password)) || (this.password == null)) {
      this.dataMap.put("msg", "密码不能为空！");
      return "success";
    }
    User user = new User();
    if (!this.username.equals(user.getUsername())) {
      this.dataMap.put("msg", "用户名不正确！");
      return "success";
    }

    String passwordhash = null;
    passwordhash = Encryption.md5(this.password);
    if (passwordhash.equals(user.getPassword()))
    {
      if (LoginSessionListener.isOnline(this.username)) {
        LoginSessionListener.kickFirstOper(getHttpRequest().getSession(), this.username);
      }

      LoginSessionListener.putSessionMap(getHttpRequest().getSession(), this.username);
      this.dataMap.put("flag", Boolean.valueOf(true));
      return "success";
    }

    this.dataMap.put("msg", "密码不正确！");
    return "success";
  }

  public String logout()
    throws Exception
  {
    try
    {
      getHttpRequest().getSession().invalidate();
    } catch (Exception e) {
      return "login";
    }
    return "login";
  }

  public String sessionFailure()
  {
    this.dataMap = new HashMap();
    System.out.println(getActionMessages());
    this.dataMap.put("flag", "error");
    this.dataMap.put("msg", "登陆超时,请重新登陆！");
    return "success";
  }

  public String otherSysSituation()
  {
    this.dataMap = new HashMap();
    HttpServletRequest request = ServletActionContext.getRequest();
    if (request != null) {
      HttpSession session = request.getSession();
      if (session != null) {
        Map msgMap = new HashMap();
        msgMap = (Map)session.getAttribute("msgMap");
        session.removeAttribute("msgMap");
        String actionMsg = "";
        String state = "";
        if (msgMap != null) {
          actionMsg = (String)msgMap.get("actionMsg");
          state = (String)msgMap.get("state");
        }
        this.dataMap.put("flag", "error");
        this.dataMap.put("state", state);
        this.dataMap.put("msg", actionMsg);
        return "success";
      }
    }
    this.dataMap.put("flag", "error");
    this.dataMap.put("msg", "系统访问失败！");
    return "success";
  }

  public String getUsername() {
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

  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Map<String, Object> getDataMap() {
    return this.dataMap;
  }

  public void setDataMap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }
}

/*
 * Qualified Name:     report.java.login.action.LoginAction
*
 */