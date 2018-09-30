package report.java.echarts.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import report.java.echarts.Option;

public class OptionUtil
{
  public static String exportToHtml(Option option)
  {
    String folderPath = System.getProperty("java.io.tmpdir");
    return exportToHtml(option, folderPath);
  }

  public static String exportToHtml(Option option, String folderPath)
  {
    String fileName = "ECharts-" + System.currentTimeMillis() + ".html";
    return exportToHtml(option, folderPath, fileName);
  }

  private static String getFolderPath(String folderPath)
  {
    File folder = new File(folderPath);
    if ((folder.exists()) && (folder.isFile())) {
      String tempPath = folder.getParent();
      folder = new File(tempPath);
    }
    if (!folder.exists()) {
      folder.mkdirs();
    }
    return folder.getPath();
  }

  private static List<String> readLines(Option option)
  {
    String optionStr = GsonUtil.format(option);
    InputStream is = null;
    InputStreamReader iReader = null;
    BufferedReader bufferedReader = null;
    List lines = new ArrayList();
    try
    {
      is = OptionUtil.class.getResourceAsStream("/template");
      iReader = new InputStreamReader(is);
      bufferedReader = new BufferedReader(iReader);
      String line;
      while ((line = bufferedReader.readLine()) != null)
      {
        //String line;
        if (line.contains("##option##")) {
          line = line.replace("##option##", optionStr);
        }
        lines.add(line);
      }
    }
    catch (Exception localException) {
      if (is != null)
        try {
          is.close();
        }
        catch (IOException localIOException)
        {
        }
    }
    finally
    {
      if (is != null)
        try {
          is.close();
        }
        catch (IOException localIOException1)
        {
        }
    }
    return lines;
  }

  public static String exportToHtml(Option option, String folderPath, String fileName)
  {
    if ((fileName == null) || (fileName.length() == 0)) {
      return exportToHtml(option, folderPath);
    }
    FileWriter writer = null;
    List<String> lines = readLines(option);

    File html = new File(getFolderPath(folderPath) + "/" + fileName);
    try {
      writer = new FileWriter(html);
      for (String l : lines)
        writer.write(l + "\n");
    }
    catch (Exception localException1)
    {
      if (writer != null)
        try {
          writer.close();
        }
        catch (IOException localIOException)
        {
        }
    }
    finally
    {
      if (writer != null)
        try {
          writer.close();
        }
        catch (IOException localIOException1)
        {
        }
    }
    try
    {
      return html.getAbsolutePath(); } catch (Exception e) {
    }
    return null;
  }

  public static String browse(Option option)
  {
    String htmlPath = exportToHtml(option);
    try
    {
      browse(htmlPath);
    } catch (Exception localException) {
    }
    return htmlPath;
  }

  public static void browse(String url)
    throws Exception
  {
    String osName = System.getProperty("os.name", "");
    if (osName.startsWith("Mac OS"))
    {
      Class fileMgr = Class.forName("com.apple.eio.FileManager");
      Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
      openURL.invoke(null, new Object[] { url });
    } else if (osName.startsWith("Windows"))
    {
      Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
    }
    else {
      String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
      String browser = null;
      for (int count = 0; (count < browsers.length) && (browser == null); count++)
      {
        if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) {
          browser = browsers[count];
        }
      }
      if (browser == null) {
        throw new Exception("Could not find web browser");
      }

      Runtime.getRuntime().exec(new String[] { browser, url });
    }
  }
}

/*
 * Qualified Name:     report.java.echarts.json.OptionUtil
*
 */