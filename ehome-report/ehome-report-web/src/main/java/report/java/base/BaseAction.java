package report.java.base;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseAction extends ActionSupport
  implements ServletRequestAware, SessionAware, ServletContextAware, ServletResponseAware
{
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private static final long serialVersionUID = 331256994500361617L;
  private HttpServletRequest httpRequest;
  private HttpServletResponse httpResponse;
  private ServletContext servletContext;
  private Map<String, Object> session;

  public Map<String, Object> getSession()
  {
    return this.session;
  }

  public Logger getLogger() {
    return this.logger;
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  public HttpServletRequest getHttpRequest() {
    return this.httpRequest;
  }

  public void setHttpRequest(HttpServletRequest httpRequest) {
    this.httpRequest = httpRequest;
  }

  public HttpServletResponse getHttpResponse() {
    return this.httpResponse;
  }

  public void setHttpResponse(HttpServletResponse httpResponse) {
    this.httpResponse = httpResponse;
  }

  public ServletContext getServletContext() {
    return this.servletContext;
  }

  public void setServletResponse(HttpServletResponse arg0)
  {
    this.httpResponse = arg0;
  }

  public void setServletRequest(HttpServletRequest arg0)
  {
    this.httpRequest = arg0;
  }

  public void setServletContext(ServletContext sc)
  {
    this.servletContext = sc;
  }

  public void setSession(Map<String, Object> arg0)
  {
    this.session = arg0;
  }
}

/*
 * Qualified Name:     report.java.base.BaseAction
*
 */