package report.java.base.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;

public class AuthenticationInterceptorAjax extends MethodFilterInterceptor
{
  private static final long serialVersionUID = -2642239372165491134L;
  public String actionMsg;

  private boolean sessionIsValid(ActionInvocation arg0)
    throws Exception
  {
    return true;
  }

  public String getActionMsg()
  {
    return this.actionMsg;
  }
  public void setActionMsg(String actionMsg) {
    this.actionMsg = actionMsg;
  }

  protected String doIntercept(ActionInvocation ai) throws Exception
  {
    Map msgMap = new HashMap();
    HttpSession session = ServletActionContext.getRequest().getSession();
    if (session == null)
      return "noLogin";
    try
    {
      if (sessionIsValid(ai))
      {
        String result = ai.invoke();
        return result;
      }
      msgMap.put("actionMsg", "会话失效,请重新登录系统!");
      return "noLogin";
    }
    catch (Exception e) {
      e.printStackTrace();
      if ((e instanceof NullPointerException))
        this.actionMsg = "空指针异常";
      else {
        this.actionMsg = e.getMessage();
      }
      msgMap.put("state", "exception");
      msgMap.put("actionMsg", this.actionMsg);
      session.setAttribute("msgMap", msgMap);
    }return "commonProcessing";
  }
}

/*
 * Qualified Name:     report.java.base.interceptor.AuthenticationInterceptorAjax
*
 */