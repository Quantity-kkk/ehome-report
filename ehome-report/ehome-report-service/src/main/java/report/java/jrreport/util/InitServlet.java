package report.java.jrreport.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.PropertyConfigurator;

public class InitServlet extends HttpServlet
{
  public void init()
    throws ServletException
  {
    super.init();
    String prefix = getServletContext().getRealPath("/");

    String log4jFile = getServletConfig().getInitParameter("log4j");
    String log4jConfigPath = prefix + log4jFile;
    PropertyConfigurator.configure(log4jConfigPath);
  }
}

/*
 * Qualified Name:     report.java.jrreport.util.InitServlet
*
 */