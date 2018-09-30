package report.java.db.util;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import report.java.db.bean.DataSourceBean;

public class XmlUtil
{
  public static Document loadInit(String filePath)
  {
    Document document = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.parse(new File(filePath));
      document.normalize();
      return document;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }return null;
  }

  public static boolean deleteXML(String dataSourceName, String filePath)
  {
    Document document = loadInit(filePath);
    try
    {
      NodeList nodeList = document.getElementsByTagName("db-config");
      for (int i = 0; i < nodeList.getLength(); i++) {
        Element element = (Element)nodeList.item(i);

        String dbConfigName = element.getAttribute("name");

        if (dbConfigName.equals(dataSourceName)) {
          Node node = nodeList.item(i);
          node.getParentNode().removeChild(node);
          saveXML(document, filePath);
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }return false;
  }

  public static boolean updateXML(DataSourceBean dataSourceBean, String filePath)
  {
    Document document = loadInit(filePath);
    try
    {
      NodeList nodeList = document.getElementsByTagName("db-config");

      for (int i = 0; i < nodeList.getLength(); i++) {
        Element element = (Element)nodeList.item(i);

        String dbConfigName = element.getAttribute("name");

        if (dbConfigName.equals(dataSourceBean.getDataSourceName())) {
          String dbDriver = dataSourceBean.getDriver();
          String dbUrl = dataSourceBean.getDataBaseUrl();
          String dbUserName = dataSourceBean.getUserName();
          String dbPassword = dataSourceBean.getPassword();
          String dbType = dataSourceBean.getType();
          String dbState = dataSourceBean.getState();
          document.getElementsByTagName("db-driver").item(i).getFirstChild().setNodeValue(dbDriver);
          document.getElementsByTagName("db-url").item(i).getFirstChild().setNodeValue(dbUrl);
          document.getElementsByTagName("db-username").item(i).getFirstChild().setNodeValue(dbUserName);
          document.getElementsByTagName("db-password").item(i).getFirstChild().setNodeValue(dbPassword);
          document.getElementsByTagName("db-type").item(i).getFirstChild().setNodeValue(dbType);
          document.getElementsByTagName("db-state").item(i).getFirstChild().setNodeValue(dbState);
        }
      }
      saveXML(document, filePath);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }return false;
  }

  public static boolean addXML(DataSourceBean dataSourceBean, String filePath)
  {
    try
    {
      Document document = loadInit(filePath);

      Element dbConfig = document.createElement("db-config");

      dbConfig.setAttribute("name", dataSourceBean.getDataSourceName());

      Element dbDriver = document.createElement("db-driver");
      Element dbUrl = document.createElement("db-url");
      Element dbUsername = document.createElement("db-username");
      Element dbPassword = document.createElement("db-password");
      Element dbType = document.createElement("db-type");
      Element dbState = document.createElement("db-state");
      Text driver = document.createTextNode(dataSourceBean.getDriver());
      dbDriver.appendChild(driver);
      Text url = document.createTextNode(dataSourceBean.getDataBaseUrl());
      dbUrl.appendChild(url);
      Text userName = document.createTextNode(dataSourceBean.getUserName());
      dbUsername.appendChild(userName);
      Text password = document.createTextNode(dataSourceBean.getPassword());
      dbPassword.appendChild(password);
      Text type = document.createTextNode(dataSourceBean.getType());
      dbType.appendChild(type);
      Text state = document.createTextNode(dataSourceBean.getState());
      dbState.appendChild(state);

      dbConfig.appendChild(dbDriver);
      dbConfig.appendChild(dbUrl);
      dbConfig.appendChild(dbUsername);
      dbConfig.appendChild(dbPassword);
      dbConfig.appendChild(dbType);
      dbConfig.appendChild(dbState);

      Element db = document.getDocumentElement();

      db.appendChild(dbConfig);

      saveXML(document, filePath);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }return false;
  }

  public static boolean saveXML(Document document, String filePath)
  {
    try
    {
      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer();

      transformer.setOutputProperty("indent", "yes");
      transformer.setOutputProperty("encoding", "UTF-8");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(new File(filePath));
      transformer.transform(source, result);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }return false;
  }

  public static DataSourceBean selectXML(DataSourceBean dataSourceBean, String filePath)
  {
    try
    {
      Document document = loadInit(filePath);

      NodeList nodeList = document.getElementsByTagName("db-config");

      for (int i = 0; i < nodeList.getLength(); i++) {
        Element element = (Element)nodeList.item(i);

        String dbConfigName = element.getAttribute("name");

        if (dbConfigName.equals(dataSourceBean.getDataSourceName())) {
          String dbDriver = document.getElementsByTagName("db-driver").item(i).getFirstChild().getNodeValue();
          String dbUrl = document.getElementsByTagName("db-url").item(i).getFirstChild().getNodeValue();
          String dbUserName = document.getElementsByTagName("db-username").item(i).getFirstChild().getNodeValue();
          String dbType = document.getElementsByTagName("db-type").item(i).getFirstChild().getNodeValue();
          String dbPassword = "";
          try {
            dbPassword = document.getElementsByTagName("db-password").item(i).getFirstChild().getNodeValue();
          } catch (Exception localException1) {
          }
          dataSourceBean.setDriver(dbDriver);
          dataSourceBean.setDataBaseUrl(dbUrl);
          dataSourceBean.setUserName(dbUserName);
          dataSourceBean.setPassword(dbPassword);
          dataSourceBean.setType(dbType);
        }
      }
      return dataSourceBean;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }return null;
  }

  public static List<DataSourceBean> selectXMLAll(String filePath)
  {
    List dataSourceBeanList = new ArrayList();
    try
    {
      Document document = loadInit(filePath);

      NodeList nodeList = document.getElementsByTagName("db-config");

      DataSourceBean dataSourceBean = null;
      for (int i = 0; i < nodeList.getLength(); i++) {
        Element element = (Element)nodeList.item(i);

        String dbConfigName = element.getAttribute("name");

        String dbDriver = document.getElementsByTagName("db-driver").item(i).getFirstChild().getNodeValue();
        String dbUrl = document.getElementsByTagName("db-url").item(i).getFirstChild().getNodeValue();
        String dbUserName = document.getElementsByTagName("db-username").item(i).getFirstChild().getNodeValue();
        String dbPassword = "";
        try {
          dbPassword = document.getElementsByTagName("db-password").item(i).getFirstChild().getNodeValue();
        } catch (Exception localException1) {
        }
        String dbType = document.getElementsByTagName("db-type").item(i).getFirstChild().getNodeValue();
        String dbState = document.getElementsByTagName("db-state").item(i).getFirstChild().getNodeValue();
        dataSourceBean = new DataSourceBean();
        dataSourceBean.setDataSourceName(dbConfigName);
        dataSourceBean.setDriver(dbDriver);
        dataSourceBean.setDataBaseUrl(dbUrl);
        dataSourceBean.setUserName(dbUserName);
        dataSourceBean.setPassword(dbPassword);
        dataSourceBean.setType(dbType);
        dataSourceBean.setState(dbState);

        dataSourceBeanList.add(dataSourceBean);
      }
      return dataSourceBeanList;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }return null;
  }
}

/* 
 * Qualified Name:     report.java.db.util.XmlUtil
*
 */