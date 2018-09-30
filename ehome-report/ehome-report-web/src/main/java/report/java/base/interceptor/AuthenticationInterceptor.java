package report.java.base.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuthenticationInterceptor extends MethodFilterInterceptor
{
  private static final long serialVersionUID = 1440786165985641145L;
  private final Log log = LogFactory.getLog(getClass());

  private boolean sessionIsValid(ActionInvocation arg0)
    throws Exception
  {
    return true;
  }

  protected String doIntercept(ActionInvocation ai)
    throws Exception
  {
    ActionSupport as = (ActionSupport)ai.getAction();
    try {
      if (sessionIsValid(ai))
      {
        String result = ai.invoke();
        return result;
      }
      as.addActionMessage("会话失效,请重新登录系统!");
      return "noLogin";
    }
    catch (Exception e) {
      e.printStackTrace();
      if (this.log.isDebugEnabled()) {
        this.log.debug(e.getMessage());
      }
      if ((e instanceof NullPointerException)) {
        as.addActionMessage("空指针异常");
      }
      else
        as.addActionMessage(e.getMessage());
    }
    return "commonException";
  }
}

/*
 * Qualified Name:     report.java.base.interceptor.AuthenticationInterceptor
*
 */