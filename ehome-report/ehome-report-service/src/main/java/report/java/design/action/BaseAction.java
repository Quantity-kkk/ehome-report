package report.java.design.action;

import com.opensymphony.xwork2.ActionSupport;
import java.net.URL;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import report.java.jrreport.util.JRUtilNew;

public class BaseAction extends ActionSupport
{
  public static String realPath;
  public static String reportfilesPath;
  protected String message;
  protected String SUCCESS = "success";

  static
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    realPath = ServletActionContext.getServletContext().getRealPath("");
    if (realPath == null)
    {
      try {
        realPath = request.getSession().getServletContext().getResource("/").getFile() + "WEB-INF/";
        reportfilesPath = request.getSession().getServletContext().getResource("/").getFile() + "reportfiles";
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      realPath = realPath + System.getProperty("file.separator") + "WEB-INF" + System.getProperty("file.separator");
      reportfilesPath = request.getSession().getServletContext().getRealPath("reportfiles");
    }
  }

  protected String filePath() {
    String _filePath = JRUtilNew.getBaseFilePath();
    return _filePath;
  }

  public String getMessage()
  {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

/*
 * Qualified Name:     report.java.design.action.BaseAction
*
 */