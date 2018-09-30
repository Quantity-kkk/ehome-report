package report.java.common.utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;

public class IOUtil
{
  public static void closeQuietly(URLConnection conn)
  {
    if ((conn != null) && 
      ((conn instanceof HttpURLConnection)))
      ((HttpURLConnection)conn).disconnect();
  }

  public static void closeQuietly(Closeable closeable)
  {
    if (closeable != null)
      try {
        closeable.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
  }
}

/* 
 * Qualified Name:     report.java.common.utils.IOUtil
*
 */