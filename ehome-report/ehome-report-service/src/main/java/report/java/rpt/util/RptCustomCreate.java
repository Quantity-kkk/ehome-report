package report.java.rpt.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import report.java.common.utils.Aes;
import report.java.jrreport.util.JRUtilNew;
import report.java.rpt.bean.ColConfigBean;
import report.java.rpt.bean.CusReportBean;
import report.java.rpt.service.RptConfigService;

public class RptCustomCreate
{
  public void reportCreate(CusReportBean bean)
    throws Exception
  {
    try
    {
      Document doc = DocumentHelper.createDocument();
      Element root = doc.addElement("dhccReport");

      root.addAttribute("uuid", bean.getRep_id());
      root.addAttribute("reportStyle", "D");
      root.addAttribute("iscustom", "1");
      root.addAttribute("reportDescription", bean.getRep_name());

      Element dataSets = root.addElement("datasets");

      String reportSql = bean.getReportSql();
      if ((reportSql.length() > 0) && (reportSql.toLowerCase().indexOf("select ") == -1)) {
        reportSql = Aes.aesDecrypt(reportSql);
      }
      if (!"".equals(reportSql))
      {
        Element dataSet = dataSets.addElement("dataset");
        dataSet.addAttribute("name", "cust");
        dataSet.addAttribute("isdic", "false");

        Element query = dataSet.addElement("query");

        Element dataSourceName = query.addElement("datasourcename");

        dataSourceName.setText(bean.getRep_dsname());

        Element commandtext = query.addElement("commandtext");

        commandtext.setText(reportSql);

        Element parameters = query.addElement("parameters");
        Element parmsList = root.addElement("parmsList");
        String parms = bean.getParms();
        if (!"".equals(parms)) {
          String[] subParm = parms.split("@");
          for (int i = 0; i < subParm.length; i++) {
            Element parameter = parameters.addElement("parameter");
            parameter.addAttribute("name", subParm[i].split("#")[0]);

            Element parameterForList = parmsList.addElement("parm");
            parameterForList.addAttribute("name", subParm[i].split("#")[0]);
            parameterForList.addAttribute("dbtype", "string");
            parameterForList.addAttribute("isnull", "0");
            parameterForList.addAttribute("type", "normal");
            parameterForList.addAttribute("vl", subParm[i].split("#")[0]);
          }

        }

        Element body = root.addElement("body");
        body.addAttribute("pagesize", "10");
        body.addAttribute("pageorder", "1");
        String[] rowCategory = { "headtitle", "reporthead", "dataarea" };
        String[] rowHeight = { "41px", "39px", "23px" };

        String betSql = reportSql.substring(reportSql.indexOf("SELECT") + 6, reportSql.indexOf("FROM"));
        if ((!"".equals(betSql)) && (betSql != null)) {
          String[] colName = betSql.split(",");

          Element fields = dataSet.addElement("fields");
          for (int i = 0; i < colName.length; i++) {
            colName[i] = colName[i].trim();
            String tab_name = bean.getTab_name();

            ColConfigBean colConfigBean = new RptConfigService().getColByTabidAndColname(tab_name, colName[i].trim());
            if (colConfigBean != null) {
              Element field = fields.addElement("field");
              field.addAttribute("name", colName[i]);
              Element datafield = field.addElement("datafield");
              datafield.setText(colName[i]);

              if ("2".equals(colConfigBean.getCol_type()))
              {
                datafield.addAttribute("formattype", "0");
                if ((!"".equals(colConfigBean.getShow_format())) && (colConfigBean.getShow_format() != null))
                  datafield.addAttribute("formatvalue", colConfigBean.getShow_format());
              }
              else if ("3".equals(colConfigBean.getCol_type()))
              {
                datafield.addAttribute("formattype", "2");
                if ((!"".equals(colConfigBean.getShow_format())) && (colConfigBean.getShow_format() != null)) {
                  datafield.addAttribute("formatvalue", colConfigBean.getShow_format());
                }
              }

              Element datatype = field.addElement("datatype");
              datatype.setText(colConfigBean.getField_type());
              String colDic = colConfigBean.getCol_dic();
              if ((!"".equals(colDic)) && (colDic != null)) {
                Element datadic = field.addElement("datadic");
                if ((colDic.indexOf("-") != -1) && (colDic.indexOf("|") != -1)) {
                  String tempStr = "";
                  String[] dicArray = colDic.split("\\|");
                  for (int j = 0; j < dicArray.length; j++) {
                    if (j != dicArray.length - 1)
                      tempStr = tempStr + "\"" + dicArray[j].split("\\-")[0] + "\":\"" + dicArray[j].split("\\-")[1] + "\",";
                    else
                      tempStr = tempStr + "\"" + dicArray[j].split("\\-")[0] + "\":\"" + dicArray[j].split("\\-")[1] + "\"";
                  }
                  tempStr = "{" + tempStr + "}";
                  datadic.setText(tempStr);
                } else {
                  datadic.setText(colDic);
                }
              }
            }
          }

          for (int i = 0; i < 3; i++)
          {
            Element row = body.addElement("row");
            row.addAttribute("category", rowCategory[i]);
            row.addAttribute("isselect", "");
            row.addAttribute("THheight", rowHeight[i]);

            String znName = bean.getCn_name();

            for (int j = 0; j < colName.length; j++)
            {
              Element col = row.addElement("col");
              col = setColProperties(col);
              col.addAttribute("height", rowHeight[i]);
              col.addAttribute("border", "{leftColor:\"rgb(0,0,0)\",topColor:\"rgb(0,0,0)\",rightColor:\"rgb(0,0,0)\",bottomColor:\"rgb(0,0,0)\"}");
              if (i == 0) {
                col.addAttribute("font-weight", "700");
                col.addAttribute("text-align", "center");
                if (j == 0) {
                  col.addAttribute("colspan", colName.length+"");
                  col.addAttribute("background-color", "rgba(207,226,243,1)");
                  col.addAttribute("colwidth", 150 * colName.length + "px");
                  col.addAttribute("font-size", "16px");
                  col.setText(bean.getRep_name());
                  col.addAttribute("offsetTop", "27");
                  col.addAttribute("offsetLeft", "36");
                } else {
                  col.addAttribute("ishidden", "true");
                  col.addAttribute("offsetTop", "");
                  col.addAttribute("offsetLeft", "");
                }
              } else if (i == 1) {
                col.addAttribute("text-align", "center");
                col.addAttribute("font-size", "14px");
                col.setText(znName.split(",")[j]);
                col.addAttribute("background-color", "rgba(235,235,235,1)");
                col.addAttribute("offsetTop", "68");
                col.addAttribute("offsetLeft", (36 + j * 100)+"");
                col.addAttribute("font-weight", "700");
              } else if (i == 2) {
                col.setText("=" + dataSet.attributeValue("name") + "." + colName[j].trim());
                col.addAttribute("offsetTop", "107");
                col.addAttribute("offsetLeft", (36 + j * 100)+"");
                col.addAttribute("font-weight", "400");
              }
            }
          }
        }
      }
      root.addElement("fillreports");
      root.addElement("expression");

      String filePath = JRUtilNew.getBaseFilePath();

      String xml = root.getDocument().asXML();
      File f = new File(filePath + System.getProperty("file.separator") + bean.getRep_id() + ".xml");

      PrintWriter pw = new PrintWriter(
        new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
      pw.write(xml);
      pw.close();
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private Element setColProperties(Element col)
  {
    col.addAttribute("colwidth", "100px");
    col.addAttribute("THwidth", "100px");
    col.addAttribute("width", "100px");
    col.addAttribute("font-family", "SimHei");
    col.addAttribute("font-size", "13px");
    col.addAttribute("font-style", "normal");
    col.addAttribute("text-decoration", "none");
    col.addAttribute("white-space", "nowrap");
    col.addAttribute("expText", "");
    col.addAttribute("dic", "");
    col.addAttribute("format", "");
    col.addAttribute("fieldFormat", "");
    col.addAttribute("expType", "");
    col.addAttribute("expData", "");
    col.addAttribute("link", "");
    col.addAttribute("font-color", "rgb(0, 0, 0)");
    col.addAttribute("text-align", "left");
    col.addAttribute("vertical-align", "middle");
    col.addAttribute("expText", "");
    col.addAttribute("dic", "");
    col.addAttribute("format", "");
    col.addAttribute("fieldFormat", "");
    col.addAttribute("expType", "");
    col.addAttribute("expData", "");
    col.addAttribute("link", "");
    col.addAttribute("background-color", "rgba(0, 0, 0, 0)");
    return col;
  }
}

/* 
 * Qualified Name:     report.java.rpt.util.RptCustomCreate
*
 */