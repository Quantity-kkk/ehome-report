package report.java.jrreport.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import report.java.common.utils.PropertyUtil;
import report.java.common.utils.Tools;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.design.action.BaseAction;
import report.java.entity.json.BgColor;
import report.java.entity.json.Image;
import report.java.jrreport.action.PreviewReportAction;
import report.java.jrreport.service.PreviewReportService;
import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pro.encryption.entrance.report.ReportEntrance;

public class JRUtilNew
{
  public static Map<String, String> baseMap = getBaseMap();

  public static Map<String, Object> getJRPrintDNew(String tableModel, String dataSetsModel, String parmsModel, String fieldModel, int currentPage, int pageType, int pageSize, String uid, JSONArray jsonArray, Boolean issubreport, String toolbar, Boolean iscustom)
  {
    PreviewReportService previewReportService = new PreviewReportService();
    Connection conn = null;
    JexlContext jc = new MapContext();
    jc.set("expFuntionChange", new JRFunExpImpl());
    Map dataMap = new HashMap();
    Document tableDesign = null;
    Boolean dtSign = Boolean.valueOf(false);
    Boolean subreport = Boolean.valueOf(false);
    try {
      tableDesign = DocumentHelper.parseText(tableModel);
    } catch (DocumentException ed) {
      ed.printStackTrace();
    }

    Map dataSetMap = previewReportService.compileReportNew(dataSetsModel);

    LinkedHashMap docMap = previewReportService.getDocMap(tableDesign, dataSetMap);

    Map dsMap = getDsMap(docMap);
    List dstfList = (List)dsMap.get("a");
    List dsdList = (List)dsMap.get("b");
    Map dicWhereMap = (Map)dsMap.get("dicWhere");
    Map sumList = (Map)dsMap.get("sum");
    Map dtlMap = (Map)dsMap.get("dtl");
    dtSign = (Boolean)dsMap.get("isdtl");
    subreport = (Boolean)dsMap.get("subreport");
    previewReportService.dicMap(dataSetMap, dicWhereMap);

    if (DBUtil.isJndi().booleanValue()) {
      conn = DBUtil.getConn(new DataSourceBean());
    }
    Map mmdtl = new HashMap();

    Object objMtf = ServletActionContext.getRequest().getSession().getAttribute(uid);
    Map mtfMap = new HashMap();
    if ((currentPage == 1) || (objMtf == null)) {
      mtfMap = previewReportService.queryExecuterNew(conn, dstfList, dataSetMap, parmsModel, jsonArray, mmdtl, currentPage, pageSize, Boolean.valueOf(false), sumList, iscustom);
      ServletActionContext.getRequest().getSession().setAttribute(uid, mtfMap);
    } else {
      mtfMap = (Map)objMtf;
    }
    List<Map<String,Object>> listtf = (List)mtfMap.get("list");
    List listtfDtl = new ArrayList();

    if (dtSign.booleanValue()) {
      int di = 0;
      Map dhMap = new HashMap();
      Map loopMap = new CaseInsensitiveMap();
      for (Map<String,Object> hashMap : listtf) {
        for (String key : hashMap.keySet()) {
          String value = hashMap.get(key) != null ? hashMap.get(key).toString() : "";
          if ((mmdtl.get(key) != null) && (!mmdtl.get(key).equals("")))
            mmdtl.put(key, mmdtl.get(key) + "," + value);
          else {
            mmdtl.put(key, value);
          }
          if (di == 0)
            dhMap.put(key, value);
          else {
            dhMap.put(key + "_" + di, value);
          }
          if (loopMap.get(key) != null)
            loopMap.put(key, Integer.valueOf(((Integer)loopMap.get(key)).intValue() + 1));
          else {
            loopMap.put(key, Integer.valueOf(1));
          }
        }
        di++;
      }
      listtfDtl.add(dhMap);

      docMap = previewReportService.resetDocMap(docMap, dtlMap, loopMap);

      Map rsdsMap = getDsMap(docMap);
      sumList = (Map)rsdsMap.get("sum");
    }

    Map mdMap = previewReportService.queryExecuterNew(conn, dsdList, dataSetMap, parmsModel, jsonArray, mmdtl, currentPage, pageSize, Boolean.valueOf(true), sumList, iscustom);
    List listd = (List)mdMap.get("list");

    Map sumMap = (Map)mdMap.get("sum");

    int totalRecord = ((Integer)mdMap.get("totalRecord")).intValue();
    if (DBUtil.isJndi().booleanValue()) {
      DBUtil.closeConn(conn);
    }
    Object bodyMap = new HashMap();
    Map<String,Object> cssMap = new HashMap();
    StringBuilder bodyStr = new StringBuilder();
    bodyStr.append("<table class='dynamic' cellspacing='0' cellpadding='0'>");
    Boolean issub = subreport;
    if (!issub.booleanValue()) {
      issub = issubreport;
    }
    int docSize = docMap.size();
    Iterator it = docMap.entrySet().iterator();
    int is = 1;
    LinkedHashMap dlr = new LinkedHashMap();
    while (it.hasNext()) {
      Map.Entry e = (Map.Entry)it.next();
      LinkedHashMap lr = new LinkedHashMap();
      lr.put(e.getKey().toString(), (Map)e.getValue());
      String category = ((Map)lr.get(e.getKey())).get("category").toString();
      String str = "";
      if ((category.equals("dataarea")) && (is < docSize)) {
        dlr.put(e.getKey().toString(), (Map)e.getValue());
      }
      else if (((category.equals("dataarea")) && (is == docSize)) || ((!category.equals("dataarea")) && (dlr.size() > 0))) {
        if ((category.equals("dataarea")) && (is == docSize)) {
          dlr.put(e.getKey().toString(), (Map)e.getValue());
        }
        if (dlr.size() > 0) {
          bodyMap = xmlMapToDataStr(dlr, "dataarea", listd, sumMap, currentPage, pageSize, totalRecord, parmsModel, issub, jc);
          dlr.clear();
        }
        str = ((Map)bodyMap).get("body").toString();
        cssMap = (Map)((Map)bodyMap).get("css");
        if ((lr.size() > 0) && (!category.equals("dataarea"))) {
          bodyMap = xmlMapToDataStr(lr, category, listtf, sumMap, currentPage, pageSize, totalRecord, parmsModel, issub, jc);
          str = str + ((Map)bodyMap).get("body").toString();
          lr.clear();
        }
      } else {
        if (dtSign.booleanValue()) {
          bodyMap = xmlMapToDataStr(lr, category, listtfDtl, sumMap, currentPage, pageSize, totalRecord, parmsModel, issub, jc);
          str = ((Map)bodyMap).get("body").toString();
        } else {
          bodyMap = xmlMapToDataStr(lr, category, listtf, sumMap, currentPage, pageSize, totalRecord, parmsModel, issub, jc);
          str = ((Map)bodyMap).get("body").toString();
        }
        lr.clear();
      }

      if ((category.equals("headtitle")) || (category.equals("reporthead"))) {
        bodyStr.append("<thead class='rtitle'>");
        bodyStr.append(str);
        bodyStr.append("</thead>");
      }
      if (((category.equals("dataarea")) && (is == docSize)) || ((!category.equals("dataarea")) && (dlr.size() > 0))) {
        bodyStr.append("<tbody>");
        bodyStr.append(str);
        bodyStr.append("</tbody>");
      }
      if ((category.equals("reportfoot")) || (category.equals("foottitle"))) {
        bodyStr.append("<tfoot>");
        bodyStr.append(str);
        bodyStr.append("</tfoot>");
      }
      is++;
    }
    bodyStr.append("</table>");

    int totalPage = 0;

    if (pageSize == -1) {
      pageSize = -1;
      totalPage = 1;
      currentPage = 1;
    } else {
      if (pageSize == 0) {
        pageSize = 10;
      }
      totalPage = getPageCount(totalRecord, pageSize);
    }
    bodyStr = license(bodyStr.toString(), dataMap);

    dataMap.put("totalPage", Integer.valueOf(totalPage));

    dataMap.put("pageSize", Integer.valueOf(pageSize));

    dataMap.put("totalRecord", Integer.valueOf(totalRecord));

    dataMap.put("currentPage", Integer.valueOf(currentPage));

    dataMap.put("pageType", Integer.valueOf(pageType));

    dataMap.put("toolbar", toolbar);
    dataMap.put("body", bodyStr.toString().replace("</thead><thead class='rtitle'>", "").replace("</tfoot><tfoot>", ""));

    dataMap.put("subreport", subreport);
    StringBuilder css = new StringBuilder();
    int kk = 0;
    for (String skey : cssMap.keySet()) {
      kk++;
      css.append("." + skey + "{" + cssMap.get(skey) + "}");
    }

    dataMap.put("css", css.toString());
    return (Map<String, Object>)dataMap;
  }

  public static void exportExcel(String tableModel, String dataSetsModel, String parmsModel, String fieldModel, String uid, JSONArray jsonArray, String defaultFilename, HttpServletRequest request, HttpServletResponse response, Boolean iscustom)
  {
    long starttime = System.currentTimeMillis();
    String expWhere = "";
    JexlContext jc = new MapContext();
    jc.set("expFuntionChange", new JRFunExpImpl());
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Set-Cookie", "fileDownload=true; path=/");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    String defaultname = null;
    if ((defaultFilename.trim() != null) && (defaultFilename != null))
      defaultname = defaultFilename + ".xlsx";
    else {
      defaultname = "export.xlsx";
    }
    String userAgent = request.getHeader("USER-AGENT");
    String fileName = defaultname;
    if (((userAgent != null) && (userAgent.indexOf("Firefox") >= 0)) || (userAgent.indexOf("Chrome") >= 0) || (userAgent.indexOf("Safari") >= 0))
      try {
        fileName = new String(fileName.getBytes(), "ISO8859-1");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    else {
      try {
        fileName = URLEncoder.encode(fileName, "UTF8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

    int currentPage = 1; int pageType = 1; int pageSize = -1;
    PreviewReportService previewReportService = new PreviewReportService();
    Connection conn = null;
    Map dataMap = new HashMap();
    Document tableDesign = null;
    Boolean dtSign = Boolean.valueOf(false);
    try {
      tableDesign = DocumentHelper.parseText(tableModel);
    } catch (DocumentException ed) {
      response.setHeader("Set-Cookie", "fileDownload=false; path=/");
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      ed.printStackTrace();
    }

    Map dataSetMap = previewReportService.compileReportNew(dataSetsModel);

    LinkedHashMap docMap = previewReportService.getDocMap(tableDesign, dataSetMap);

    Map dsMap = getDsMap(docMap);
    List dstfList = (List)dsMap.get("a");
    List dsdList = (List)dsMap.get("b");
    Map dicMap = (Map)dsMap.get("dic");
    Map sumList = (Map)dsMap.get("sum");
    Map dtlMap = (Map)dsMap.get("dtl");
    dtSign = (Boolean)dsMap.get("isdtl");

    long starttime1 = System.currentTimeMillis();
    System.out.println("模板处理时间：" + (starttime1 - starttime));
    if (DBUtil.isJndi().booleanValue()) {
      conn = DBUtil.getConn(new DataSourceBean());
    }
    Map mmdtl = new HashMap();

    Map mtfMap = previewReportService.queryExecuterNew(conn, dstfList, dataSetMap, parmsModel, jsonArray, mmdtl, currentPage, pageSize, Boolean.valueOf(false), sumList, iscustom);
    List<Map<String,Object>> listtf = (List)mtfMap.get("list");
    List listtfDtl = new ArrayList();

    if (dtSign.booleanValue()) {
      int di = 0;
      Map dhMap = new HashMap();
      Map loopMap = new CaseInsensitiveMap();
      for (Map<String,Object> hashMap : listtf) {
        for (String key : hashMap.keySet()) {
          String value = hashMap.get(key) != null ? hashMap.get(key).toString() : "";
          if ((mmdtl.get(key) != null) && (!mmdtl.get(key).equals("")))
            mmdtl.put(key, mmdtl.get(key) + "," + value);
          else {
            mmdtl.put(key, value);
          }
          if (di == 0)
            dhMap.put(key, value);
          else {
            dhMap.put(key + "_" + di, value);
          }
          if (loopMap.get(key) != null)
            loopMap.put(key, Integer.valueOf(((Integer)loopMap.get(key)).intValue() + 1));
          else {
            loopMap.put(key, Integer.valueOf(1));
          }
        }
        di++;
      }
      listtfDtl.add(dhMap);

      docMap = previewReportService.resetDocMap(docMap, dtlMap, loopMap);

      Map rsdsMap = getDsMap(docMap);
      sumList = (Map)rsdsMap.get("sum");
    }

    Map mdMap = previewReportService.queryExecuterNew(conn, dsdList, dataSetMap, parmsModel, jsonArray, mmdtl, currentPage, pageSize, Boolean.valueOf(true), sumList, iscustom);
    List listd = (List)mdMap.get("list");

    Map sumMap = (Map)mdMap.get("sum");
    int totalRecord = ((Integer)mdMap.get("totalRecord")).intValue();
    if (DBUtil.isJndi().booleanValue()) {
      DBUtil.closeConn(conn);
    }
    long endtime1 = System.currentTimeMillis();
    System.out.println("数据处理时间：" + (endtime1 - starttime1));
    Object os = null;
    try {
      os = response.getOutputStream();

      SXSSFWorkbook wb = new SXSSFWorkbook(1000);
      wb.setCompressTempFiles(true);

      Sheet sheet1 = wb.createSheet("sheet1");
      int rownum = 0;
      int docSize = docMap.size();
      Map cn = new HashMap();
      int i = 0;
      LinkedHashMap dlr = new LinkedHashMap();
      Iterator it = docMap.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry e = (Map.Entry)it.next();
        LinkedHashMap lr = new LinkedHashMap();
        lr.put(e.getKey().toString(), (Map)e.getValue());
        String category = ((Map)lr.get(e.getKey())).get("category").toString();
        if ((category.equals("dataarea")) && (i + 1 < docSize)) {
          dlr.put(e.getKey().toString(), (Map)e.getValue());
        }
        else if (((category.equals("dataarea")) && (i + 1 == docSize)) || ((!category.equals("dataarea")) && (dlr.size() > 0))) {
          if ((category.equals("dataarea")) && (i + 1 == docSize)) {
            dlr.put(e.getKey().toString(), (Map)e.getValue());
          }
          if (cn.get("row") == null) {
            cn.put("row", Integer.valueOf(0));
          }
          if (dlr.size() > 0) {
            cn = setExcelRow(wb, sheet1, cn, dlr, "dataarea", listd, sumMap, currentPage, pageSize, totalRecord, parmsModel, jc);
            dlr.clear();
          }
          if ((lr.size() > 0) && (!category.equals("dataarea"))) {
            cn = setExcelRow(wb, sheet1, cn, lr, category, listtf, sumMap, currentPage, pageSize, totalRecord, parmsModel, jc);
            lr.clear();
          }
        }
        else if (dtSign.booleanValue()) {
          if (i == 0) {
            cn.put("row", Integer.valueOf(0));
            cn = setExcelRow(wb, sheet1, cn, lr, category, listtfDtl, sumMap, currentPage, pageSize, totalRecord, parmsModel, jc);
          } else {
            cn = setExcelRow(wb, sheet1, cn, lr, category, listtfDtl, sumMap, currentPage, pageSize, totalRecord, parmsModel, jc);
          }
        }
        else if (i == 0) {
          cn.put("row", Integer.valueOf(0));
          cn = setExcelRow(wb, sheet1, cn, lr, category, listtf, sumMap, currentPage, pageSize, totalRecord, parmsModel, jc);
        } else {
          cn = setExcelRow(wb, sheet1, cn, lr, category, listtf, sumMap, currentPage, pageSize, totalRecord, parmsModel, jc);
        }

        i++;
      }
      wb.write((OutputStream)os);
      wb.dispose();
    } catch (Exception e) {
      response.setHeader("Set-Cookie", "fileDownload=false; path=/");
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      e.printStackTrace();
      try
      {
        if (os != null)
          ((OutputStream)os).close();
      }
      catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (os != null)
          ((OutputStream)os).close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    long endtime2 = System.currentTimeMillis();
    System.out.println("导出处理时间：" + (endtime2 - endtime1));
  }

  public static Map<String, Object> getJRPrintD(String tableModel, String dataSetsModel, String parmsModel, String fieldModel, int currentPage, int pageType, int pageSize, String uid, JSONArray jsonArray)
  {
    PreviewReportService previewReportService = new PreviewReportService();
    Connection conn = null;
    StringBuilder sb = new StringBuilder();
    Map dataMap = new HashMap();

    int[] colindexn = new int[0];
    int[] colindex = new int[0];
    int[] dtcount = new int[0];
    int[] dtcol = new int[0];
    int _dtindex = 0;
    int totalRecord = 0;
    HashMap maph = new HashMap();
    Boolean dtbb = Boolean.valueOf(false);
    try {
      if (DBUtil.isJndi().booleanValue()) {
        conn = DBUtil.getConn(new DataSourceBean());
      }
      Document tableDesign = DocumentHelper.parseText(tableModel);

      Map dicMap = new HashMap();
      if ((baseMap.get("dicdb") != null) && ((""+baseMap.get("dicdb")).equals("1"))) {
        previewReportService.getdicList(tableDesign.getRootElement(), dicMap);
      }

      int tempHeight = 0;
      Map DataSetMap = previewReportService.compileReport(dataSetsModel);

      String[] tempArray = { "", "" };
      String[] tabTitle = previewReportService.listTableNode("headtitle", tableDesign.getRootElement(), tempArray, false);

      String trueTabTitle = tabTitle[0].replace("<tr>", "<tr rel=\"headtitle\">");

      if (!"".equals(tabTitle[1])) {
        tempHeight += Integer.parseInt(tabTitle[1]);
      }

      String trueTabHead = "";
      String[] aindex;
      if (currentPage == 1) {
        String[] tempArray1 = { "", "" };
        String[] tabHead = previewReportService.listTableNode("reporthead", tableDesign.getRootElement(), tempArray1, false);
        List<Map<String,Object>> listh = new ArrayList();
        trueTabHead = tabHead[0];
        if (!"".equals(trueTabHead))
        {
          Map mph = previewReportService.queryExecuter(conn, getDataSet(trueTabHead), DataSetMap, parmsModel, jsonArray, new HashMap(), 0, 0, Boolean.valueOf(false), dicMap);
          listh = (List)mph.get("list");

          if (listh.size() > 0) {
            if (trueTabHead.indexOf("{{new report.java.jrreport.util.JRFunExpImpl().gpc(") != -1)
              dtbb = Boolean.valueOf(true);
            else {
              dtbb = Boolean.valueOf(false);
            }
            HashMap<String,Object> mmh = new HashMap();
            String[] striii;
            if (dtbb.booleanValue()) {
              for (Map<String,Object> hashMap : listh) {
                for (String key : hashMap.keySet()) {
                  String value = hashMap.get(key) != null ? hashMap.get(key).toString() : "";
                  if ((mmh.get(key) != null) && (!mmh.get(key).equals("")))
                    mmh.put(key, mmh.get(key) + "," + value);
                  else {
                    mmh.put(key, value);
                  }
                }
              }
              maph = mmh;

              int arrsize = mmh.size();
              colindexn = new int[arrsize];
              colindex = new int[arrsize];
              dtcount = new int[arrsize];
              dtcol = new int[arrsize];
              striii = null;
              for (String key : mmh.keySet()) {
                String value = mmh.get(key) != null ? mmh.get(key).toString() : "";
                String newv = "";
                if ((value != "") && ((value.indexOf(",") != -1) || (dtbb.booleanValue()))) {
                  String[] arr = Tools.array_unique(value.split(","));
                  trueTabHead = trueTabHead.replace("{{new report.java.jrreport.util.JRFunExpImpl().gpc(\"<=" + key + ">\")}}", "<=" + key + ">");
                  int td_start = trueTabHead.split("<=" + key + ">")[0].lastIndexOf("<td");
                  String stri = trueTabHead.substring(td_start, trueTabHead.indexOf("</td>", td_start) + 5);

                  aindex = trueTabHead.split("</tr>");
                  if (aindex[0].indexOf(stri) != -1) {
                    String[] strii = aindex[0].split("<td");
                    for (int j = 1; j < strii.length; j++) {
                      int _cla = 1; int _clb = 1;
                      if (strii[j].indexOf("colspan='") != -1) {
                        _cla = Integer.parseInt(strii[j].split("colspan='")[1].split("'")[0]);
                      }
                      if (strii[j].indexOf("rowspan='") != -1) {
                        _clb = Integer.parseInt(strii[j].split("rowspan='")[1].split("'")[0]);
                      }
                      colindexn[_dtindex] += _cla;
                      colindex[_dtindex] += _cla - _clb;
                    }
                  }

                  String newString = "";
                  dtcount[_dtindex] = arr.length;
                  try {
                    dtcol[_dtindex] = Integer.parseInt(stri.substring(stri.indexOf("colspan='") + 9).split("'")[0]);
                  }
                  catch (Exception e) {
                    dtcol[_dtindex] = 1;
                  }
                  if (aindex.length > 1) {
                    int sindex = 0;
                    if (_dtindex == 0) {
                      striii = aindex[1].split("<td");
                    }
                    for (int j = 1; j < striii.length; j++) {
                      if (sindex >= colindex[_dtindex] - dtcol[_dtindex]) {
                        break;
                      }
                      if (striii[j].indexOf("colspan='") != -1) {
                        sindex += Integer.parseInt(striii[j].split("colspan='")[1].split("'")[0]);
                      }
                    }
                    String sk = "";
                    if (_dtindex == 0) {
                      for (int l = sindex; l < sindex + dtcol[_dtindex]; l++)
                        sk = sk + "<td" + striii[(l + 1)];
                    }
                    else {
                      int startsindex = 0;
                      for (int _si = 0; _si < _dtindex; _si++) {
                        startsindex += dtcol[_si];
                      }
                      for (int l = sindex + startsindex; l < sindex + startsindex + dtcol[_dtindex]; l++) {
                        sk = sk + "<td" + striii[(l + 1)];
                      }
                    }
                    String newsk = "";

                    for (int i = 0; i < arr.length; i++) {
                      newString = newString + stri.replace(new StringBuilder("<=").append(key).append(">").toString(), arr[i]);
                      newsk = newsk + sk;
                    }
                    trueTabHead = trueTabHead.replace(stri, newString).replace(sk, newsk);
                  }
                  else {
                    for (int i = 0; i < arr.length; i++) {
                      newString = newString + stri.replace(new StringBuilder("<=").append(key).append(">").toString(), arr[i]);
                    }
                    trueTabHead = trueTabHead.replace(stri, newString);
                  }
                }

                if ((dtcount[_dtindex] > 0) && (dtcol[_dtindex] > 0)) {
                  trueTabTitle = trueTabTitle.replace("colspan='" + trueTabTitle.split("colspan='")[1].split("'")[0] + "'", "colspan='" + (Integer.parseInt(trueTabTitle.split("colspan='")[1].split("'")[0]) + (dtcount[_dtindex] - 1) * dtcol[_dtindex]) + "'");
                }

                _dtindex++;
              }
            } else {
              mmh = (HashMap)listh.get(0);
              for (String key : mmh.keySet() ) { 
            	//String key = (String)((Iterator)striii).next();
                String value = mmh.get(key) != null ? mmh.get(key).toString() : "";
                trueTabHead = trueTabHead.replace("<=" + key + ">", value);
              }

            }

            boolean tempHeadFlag = true;
            while (tempHeadFlag)
            {
              if ((trueTabHead.indexOf("{{") != -1) && (trueTabHead.indexOf("}}") != -1)) {
                int sindex = 0;
                String strindex = trueTabHead.substring(trueTabHead.indexOf("}}") + 2);
                for (int is = 0; is < 10; is++) {
                  if ((strindex.length() > 0) && (strindex.startsWith("}"))) {
                    strindex = strindex.substring(1);
                    sindex++;
                  }
                }
                strindex = null;
                String vStr = trueTabHead.substring(trueTabHead.indexOf("{{") + 2, trueTabHead.indexOf("}}") + sindex);

                Map mm = new HashMap();
                mm.put("ttttttt", new JRFunExpImpl());

                String expression = vStr.replace("new report.java.jrreport.util.JRFunExpImpl()", "ttttttt");
                trueTabHead = trueTabHead.replace("{{" + vStr + "}}", DyMethodUtil.invokeMethod(expression, mm).toString());
              }
              else
              {
                tempHeadFlag = false;
              }
            }
          }

        }

        if (!"".equals(tabHead[1]))
          tempHeight += Integer.parseInt(tabHead[1]);
      } else {
        trueTabHead = "<tr><td></td></tr>";
      }

      String[] tempArray2 = { "", "" };

      String[] tabData = previewReportService.listTableNode("dataarea", tableDesign.getRootElement(), tempArray2, false);
      String styleData = "";
      if (!"".equals(tabData[0]))
        styleData = tabData[0].replaceAll("<tr></tr>", "");
      String[] striii = null;
      if ((dtbb.booleanValue()) && (!"".equals(styleData))) {
        for (int _dt = 0; _dt < _dtindex; _dt++) {
          int bindex = 0;
          if (_dt == 0) {
            striii = styleData.split("</tr>")[0].replace("<tr>", "").split("</td>");
            for (int j = 1; j < striii.length; j++) {
              if (bindex >= colindex[_dt] - dtcol[_dt]) {
                break;
              }
              if (striii[j].indexOf("colspan='") != -1)
                bindex += Integer.parseInt(striii[j].split("colspan='")[1].split("'")[0]);
              else
                bindex++;
            }
          }
          else {
            for (int j = 1; j < striii.length; j++) {
              if (bindex <= colindex[_dt] - dtcol[_dt]) {
                if (striii[j].indexOf("colspan='") != -1)
                  bindex += Integer.parseInt(striii[j].split("colspan='")[1].split("'")[0]);
                else
                  bindex++;
              }
              else {
                bindex = 1;
              }
            }
          }
          if (bindex > 1) {
            bindex = 0;
          }
          String sk = "";
          if (_dt == 0) {
            for (int l = bindex; l < bindex + dtcol[_dt]; l++)
              sk = sk + striii[(l + 1)] + "</td>";
          }
          else {
            int startsindexd = 0;
            for (int _si = 0; _si < _dtindex - 1; _si++) {
              startsindexd += dtcol[_si];
            }
            for (int l = bindex + startsindexd; l < bindex + startsindexd + dtcol[_dt]; l++) {
              sk = sk + striii[(l + 1)] + "</td>";
            }
          }

          String newbd = "";
          for (int i = 0; i < dtcount[_dt]; i++) {
            if (i == 0)
              newbd = newbd + sk;
            else {
              newbd = newbd + sk.replaceAll("(\\<\\=)([\\w\\_\\.]+)(\\>)", new StringBuilder("$1$2_").append(i).append("$3").toString());
            }
          }
          styleData = styleData.replace(sk, newbd);
        }

      }

      boolean isGroup = true;
      List groupNameList = new ArrayList();

      while (isGroup) {
        if (styleData.indexOf("new report.java.jrreport.util.JRFunExpImpl().group") != -1) {
          String[] str = styleData.split("new report.java.jrreport.util.JRFunExpImpl\\(\\).group");
          String vStr = "";
          if ("{".equals(str[0].substring(str[0].length() - 1))) {
            vStr = str[1].substring(2, str[1].indexOf("}}") - 2);
            if (vStr.indexOf("<=") != -1) {
              groupNameList.add(vStr.substring(2, vStr.length() - 1));
              styleData = styleData.replace("{{new report.java.jrreport.util.JRFunExpImpl().group(\"" + vStr + "\")}}", vStr);
            }
          } else {
            vStr = styleData.substring(styleData.indexOf("group") + 7, styleData.indexOf(">\")") + 1);
            if (vStr.indexOf("<=") != -1) {
              groupNameList.add(vStr.substring(2, vStr.length() - 1));
              styleData = styleData.replace("new report.java.jrreport.util.JRFunExpImpl().group(\"" + vStr + "\")", vStr);
            }
          }
        } else {
          isGroup = false;
        }
      }

      int dataHeight = 0;
      if (!"".equals(tabData[1])) {
        dataHeight = Integer.parseInt(tabData[1]);
      }

      String trueTabFoot = "";
      if (currentPage == 1) {
        String[] tempArray3 = { "", "" };
        String[] tabFoot = previewReportService.listTableNode("reportfoot", tableDesign.getRootElement(), tempArray3, false);
        List listf = new ArrayList();
        trueTabFoot = tabFoot[0];
        if (!"".equals(trueTabFoot))
        {
          Map mpf = previewReportService.queryExecuter(conn, getDataSet(trueTabFoot), DataSetMap, parmsModel, jsonArray, new HashMap(), 0, 0, Boolean.valueOf(false), dicMap);
          listf = (List)mpf.get("list");

          if (listf.size() > 0) {
            HashMap<String,Object> mapf = (HashMap)listf.get(0);

            for (String key : mapf.keySet()) {
              String value = mapf.get(key) != null ? mapf.get(key).toString() : "";
              trueTabFoot = trueTabFoot.replace("<=" + key + ">", value);
            }
          }

          if (dtbb.booleanValue()) {
            String[] striiit = null;
            for (int _dt = 0; _dt < _dtindex; _dt++) {
              int findex = 0;
              if (trueTabFoot.split("</tr>").length == 1) {
                if (_dt == 0) {
                  striiit = trueTabFoot.split("</tr>")[0].replace("<tr>", "").split("</td>");
                  for (int j = 1; j < striiit.length; j++) {
                    if (findex >= colindex[_dt] - dtcol[_dt]) {
                      break;
                    }
                    if (striiit[j].indexOf("colspan='") != -1)
                      findex += Integer.parseInt(striiit[j].split("colspan='")[1].split("'")[0]);
                  }
                }
                else {
                  for (int j = 1; j < striiit.length; j++) {
                    if (findex <= colindex[_dt] - dtcol[_dt]) {
                      if (striiit[j].indexOf("colspan='") != -1)
                        findex += Integer.parseInt(striiit[j].split("colspan='")[1].split("'")[0]);
                      else
                        findex++;
                    }
                    else {
                      findex = 1;
                    }
                  }
                }
                if (findex > 1) {
                  findex = 0;
                }
                String skt = "";
                if (_dt == 0) {
                  for (int l = findex; l < findex + dtcol[_dt]; l++)
                    skt = skt + striiit[(l + 1)] + "</td>";
                }
                else {
                  int _strf = 0;
                  for (int _si = 0; _si < _dtindex - 1; _si++) {
                    _strf += dtcol[_dt];
                  }
                  for (int l = findex + _strf; l < findex + _strf + dtcol[_dt]; l++) {
                    skt = skt + striiit[(l + 1)] + "</td>";
                  }
                }

                String newft = "";
                for (int i = 0; i < dtcount[_dt]; i++) {
                  if (trueTabFoot.split("</tr>")[0].indexOf("$V{") != -1) {
                    if (i == 0)
                      newft = newft + skt;
                    else
                      newft = newft + skt.replace("}</td>", new StringBuilder("_").append(i).append("}</td>").toString());
                  }
                  else {
                    newft = newft + skt;
                  }
                }
                trueTabFoot = trueTabFoot.replace(skt, newft);
              }

            }

          }

        }

        if (!"".equals(tabFoot[1]))
          tempHeight += Integer.parseInt(tabFoot[1]);
      } else {
        trueTabFoot = "<tr><td></td></tr>";
      }

      Object list = new ArrayList();

      if (dtbb.booleanValue()) {
        Map mplist = previewReportService.queryExecuter(conn, getDataSet(styleData), DataSetMap, parmsModel, jsonArray, maph, currentPage, pageSize, Boolean.valueOf(true), dicMap);
        list = (List)mplist.get("list");
        totalRecord = ((Integer)mplist.get("totalRecord")).intValue();
      } else {
        Map mplist = previewReportService.queryExecuter(conn, getDataSet(styleData), DataSetMap, parmsModel, jsonArray, new HashMap(), currentPage, pageSize, Boolean.valueOf(true), dicMap);
        list = (List)mplist.get("list");
        totalRecord = ((Integer)mplist.get("totalRecord")).intValue();
      }

      int totalPage = 0;

      if (pageSize == -1) {
        pageSize = -1;
        totalPage = 1;
        currentPage = 1;
      } else if (pageSize == -2) {
        totalPage = getPageCount(totalRecord, 40);
      } else {
        if (pageSize == 0)
        {
          double trueHeight = 0.0D;
          if (pageType == 1)
            trueHeight = 262.0D;
          else {
            trueHeight = 175.0D;
          }

          trueHeight -= (tempHeight + 1) / 96.0D * 25.399999999999999D;
          pageSize = (int)Math.floor(trueHeight / ((dataHeight + 1) / 96.0D * 25.399999999999999D));
        }

        totalPage = getPageCount(totalRecord, pageSize);
      }

      dataMap.put("totalPage", Integer.valueOf(totalPage));

      dataMap.put("pageSize", Integer.valueOf(pageSize));

      dataMap.put("totalRecord", Integer.valueOf(totalRecord));

      dataMap.put("currentPage", Integer.valueOf(currentPage));

      dataMap.put("pageType", Integer.valueOf(pageType));

      Map<String,Object> summaryMap = new HashMap();

      List sumCol = getSumCol(trueTabFoot);

      int currentSumPage = currentPage * pageSize;
      if (((List)list).size() < currentSumPage) {
        currentSumPage = ((List)list).size();
      }
      String trueData = "";
      boolean flag = true;

      int rownum = ((List)list).size();
      int colnum = groupNameList.size();
      String[][] a = new String[rownum][colnum];
      int rn = 0;
      for (HashMap hashMap : (List<HashMap>)list) {
        int cn = 0;
        for (int gn = 0; gn <= colnum - 1; gn++) {
          a[rn][cn] = (hashMap.get(groupNameList.get(gn)) == null ? "" : hashMap.get(groupNameList.get(gn)).toString());
          cn++;
        }
        rn++;
      }

      String[][] b = new String[rownum][colnum];
      int count = 0;
      String temp = "";
      for (int col = 0; col < colnum; col++)
      {
        for (int row = 0; row < rownum; row++) {
          if ((row == 0) || ((temp != null) && (!temp.equals(a[row][col])))) {
            count = 1;
            temp = a[row][col];
          } else {
            boolean flagGroup = true;
            if ((col > 0) && (row > 0)) {
              for (int g = col - 1; g >= 0; g--) {
                if (!a[(row - 1)][g].equals(a[row][g])) {
                  flagGroup = false;
                  break;
                }
              }
            }
            if (flagGroup) {
              count++;
            } else {
              count = 1;
              temp = a[row][col];
            }
          }
          for (int k = row; (k >= row + 1 - count) && (k >= 0); k--) {
            b[k][col] = count+"";
          }
        }
      }

      String[][] c = new String[rownum][colnum];
      for (int i = 0; i < rownum; i++)
        for (int j = 0; j < colnum; j++)
          if (b[i][j].equals("1")) {
            c[i][j] = "1";
          }
          else if (i == 0) {
            c[i][j] = "1";
          }
          else if (!a[i][j].equals(a[(i - 1)][j])) {
            c[i][j] = "1";
          }
          else if (j == 0) {
            c[i][j] = "0";
          }
          else if (!"0".equals(c[i][(j - 1)]))
            c[i][j] = "1";
          else
            c[i][j] = "0";
      String tempData;
      for (int i = 0; i < ((List)list).size(); i++) {
        tempData = styleData;
        SelectUtil.clearMap();
        HashMap<String,Object> map = (HashMap)((List)list).get(i);
        SelectUtil.putAll(map);

        for (String key : map.keySet()) {
          Object vl = map.get(key);

          String tp = "format:string;";
          if ((vl instanceof Integer))
          {
            tp = "format:string;";
          } else if ((vl instanceof Double))
          {
            tp = "format:number;";
          } else if ((vl instanceof BigDecimal))
          {
            tp = "format:number;";
          }
          String value = vl != null ? vl.toString() : "";

          tempData = tempData.replace(">ROWNUM<", ">" + String.valueOf(i + 1) + "<");
          if (!groupNameList.contains(key)) {
            tempData = tempData.replace("(#", "<").replace("#)", ">");

            String[] str = tempData.split("</td>");
            for (int s = 0; s < str.length - 1; s++) {
              if (str[s].indexOf("<=" + key + ">") != -1) {
                tempData = tempData.replace(str[s], str[s].replace("<=" + key + ">", value).replace("style=\"", "style=\"" + tp));
              }

            }

          }
          else
          {
            for (int j = 0; j < groupNameList.size(); j++) {
              if (key.equals(groupNameList.get(j))) {
                if ("0".equals(c[i][j])) {
                  if (i == (currentPage - 1) * pageSize) {
                    int k = 0;
                    for (int j2 = (currentPage - 1) * pageSize; j2 < c.length; j2++) {
                      if (!"0".equals(c[j2][j])) break;
                      k++;
                    }

                    if (tempData.indexOf("><=" + key + ">") != -1) {
                      tempData = tempData.replace("><=" + key + ">", " rowspan=" + k + ">" + value);
                    } else {
                      String[] str = tempData.split("<=" + key + ">");
                      String aa = str[0].substring(str[0].lastIndexOf(">{{"), str[0].length());
                      String bb = aa + "<=" + key + ">";
                      tempData = tempData.replace(bb, " rowspan=" + k + aa + "\"" + value + "\"");
                    }
                  }
                  else if (tempData.indexOf("><=" + key + ">") != -1) {
                    tempData = tempData.replaceAll("<td((?!</td>).)*?><=" + key + "></td>", "");
                  } else {
                    String[] str = tempData.split("<=" + key + ">");
                    String aa = str[0].substring(str[0].lastIndexOf("<td"), str[0].length());
                    String bb = str[1].substring(0, str[1].indexOf(">"));
                    tempData = tempData.replace(aa + "<=" + key + ">" + bb + ">", "");
                  }

                }
                else if (tempData.indexOf("><=" + key + ">") != -1) {
                  tempData = tempData.replace("><=" + key + ">", " rowspan=" + b[i][j] + ">" + value);
                } else {
                  String[] str = tempData.split("<=" + key + ">");
                  String aa = str[0].substring(str[0].lastIndexOf(">{{"), str[0].length());
                  String bb = aa + "<=" + key + ">";
                  tempData = tempData.replace(bb, " rowspan=" + b[i][j] + aa + "\"" + value + "\"");
                }
              }

            }

            tempData = tempData.replace("(#", "<").replace("#)", ">");
            tempData = tempData.replace("<=" + key + ">", value);
          }
          String curKey = key.substring(key.indexOf(".") + 1, key.length());
          if ((sumCol.indexOf(curKey) != -1) || (sumCol.indexOf(key) != -1)) {
            if (!"".equals(value)) {
              value = value.trim().replace(",", "");
            }
            if ((isNumeric(value)) && (sumCol.size() != 0))
            {
              Double singleValue = Double.valueOf(0.0D);
              if ("".equals(value))
                value = "0";
              else if (value.indexOf(".") == 0) {
                value = "0" + value;
              }
              value = getPrettyNumber(value);
              try {
                singleValue = Double.valueOf(Double.parseDouble(value));
              }
              catch (Exception localException1)
              {
              }
              if (flag)
              {
                for (int j = 0; j < sumCol.size(); j++) {
                  if (((String)sumCol.get(j)).equals(curKey)) {
                    summaryMap.put((String)sumCol.get(j), singleValue);
                  }
                }
              }
              else if (summaryMap.containsKey(curKey))
                summaryMap.put(curKey, Double.valueOf(((Double)summaryMap.get(curKey)).doubleValue() + singleValue.doubleValue()));
            }
            else
            {
              summaryMap.put(curKey, Double.valueOf(0.0D));
            }

          }

        }

        boolean tempFlag = true;
        while (tempFlag)
        {
          if ((tempData.indexOf("{{") != -1) && (tempData.indexOf("}}") != -1)) {
            int sindex = 0;
            String strindex = tempData.substring(tempData.indexOf("}}") + 2);
            for (int is = 0; is < 10; is++) {
              if ((strindex.length() > 0) && (strindex.startsWith("}"))) {
                strindex = strindex.substring(1);
                sindex++;
              }
            }
            strindex = null;
            String vStr = tempData.substring(tempData.indexOf("{{") + 2, tempData.indexOf("}}") + sindex);
            if (vStr.indexOf("<=") == -1)
            {
              Map mm = new HashMap();
              mm.put("ttttttt", new JRFunExpImpl());

              String expression = vStr.replace("new report.java.jrreport.util.JRFunExpImpl()", "ttttttt");
              tempData = SelectUtil.replaceStr(expression, vStr, mm, tempData);
            }
            else
            {
              tempData = tempData.replace("{{" + vStr + "}}", "");
            }
          } else {
            tempFlag = false;
          }
        }

        if (!summaryMap.isEmpty()) {
          flag = false;
        }
        trueData = trueData + tempData;
      }

      if (trueTabTitle.length() > 1) {
        sb.append("<table class='dynamich' cellspacing='0' cellpadding='0'>" + trueTabTitle + "</table>");
      }
      sb.append("<table class='dynamic' cellspacing='0' cellpadding='0'>");
      sb.append("<thead class='rtitle'>" + trueTabHead + "</thead>");
      sb.append("<tbody>" + trueData + "</tbody>");
      if (currentPage == 1) {
        for (String key : summaryMap.keySet()) {
          String value = summaryMap.get(key) != null ? ((Double)summaryMap.get(key)).toString() : "";
          String oldStra = "$V{" + key + "}＋";
          String oldStrb = "$V{" + key + "}</td>";
          String oldStrc = "$V{" + key + "}";

          trueTabFoot = trueTabFoot.replace(oldStra, value).replace(oldStrb, getPrettyNumber(value, "#,##0.00") + "</td>").replace(oldStrc, value);
        }
        trueTabFoot = trueTabFoot.replace("{TotalRecord}", String.valueOf(totalRecord));

        boolean tempFootFlag = true;
        while (tempFootFlag)
        {
          if ((trueTabFoot.indexOf("{{") != -1) && (trueTabFoot.indexOf("}}") != -1)) {
            int sindex = 0;
            String strindex = trueTabFoot.substring(trueTabFoot.indexOf("}}") + 2);
            for (int is = 0; is < 10; is++) {
              if ((strindex.length() > 0) && (strindex.startsWith("}"))) {
                strindex = strindex.substring(1);
                sindex++;
              }
            }
            strindex = null;
            String vStr = trueTabFoot.substring(trueTabFoot.indexOf("{{") + 2, trueTabFoot.indexOf("}}") + sindex);

            Object mm = new HashMap();
            ((Map)mm).put("ttttttt", new JRFunExpImpl());

            String expression = vStr.replace("new report.java.jrreport.util.JRFunExpImpl()", "ttttttt");
            try {
              trueTabFoot = trueTabFoot.replace("{{" + vStr + "}}", DyMethodUtil.invokeMethod(expression, (Map)mm).toString());
            } catch (Exception e) {
              trueTabFoot = trueTabFoot.replace("{{" + vStr + "}}", "--");
              e.printStackTrace();
            }

          }
          else
          {
            tempFootFlag = false;
          }
        }
      }
      sb.append("<tfoot>" + trueTabFoot + "</tfoot></table>");
      String bdStr = sb.toString().replace(">< ><", "> <").replaceAll("<=\\S*?>", " ").replaceAll("\\$V\\{\\S*?\\}", " ");

      if ((!bdStr.isEmpty()) && (bdStr.indexOf("id=\"frm_") != -1)) {
        String[] arr = bdStr.split("id=\"frm_");
        for (int i = 1; i < arr.length; i++) {
          try {
            String uuid = arr[i].substring(0, 32);
            bdStr = bdStr.replace("id=\"frm_" + uuid + "\"", "id='" + uuid + "' ");
            String ddd = PreviewReportAction.getSubReport(uuid, parmsModel);
            String[] arr2 = bdStr.split("id='" + uuid + "'");
            if (arr2.length == 2) {
              String colspan = "";
              if (arr2[1].toLowerCase().indexOf("colspan='") != -1) {
                colspan = " colspan='" + arr2[1].split("colspan='")[1].split("'")[0] + "'";
              }
              bdStr = arr2[0] + colspan + arr2[1].substring(arr2[1].indexOf(">"), arr2[1].indexOf("subreport:")) + ddd + arr2[1].substring(arr2[1].indexOf("</td>"));
            }
          } catch (Exception e) {
            e.printStackTrace();
          }

        }

      }

      Map authMap = ReportEntrance.checkAuth();
      String expires = (String)authMap.get("expires");
      if (authMap.get("show") != null) {
        bdStr = (String)authMap.get("show");
      }
      dataMap.put("expires", (String)authMap.get(expires));
      if (!"0".equals(expires)) {
        StringBuffer html = new StringBuffer();
        html.append("<a href=\"#\" style=\"float: left;\" onclick=\"activation();\">报表应用激活</a> ");
        html.append("<script type=\"text/javascript\"> ");
        html.append("function activation(){");
        html.append("  $(\"body\").append('<div id=\"activation_post\" style=\"overflow: hidden;float: left; position: fixed;width: 580px;height: 300px;top: 5%;left: 15%;background: #ddd;\"><span style=\"float: left;position: relative;left: 0px;padding-top: 8px; padding-left: 8px;\">激活处理页面</span><span style=\"float: right;position: relative;right: 0px;\"><img style=\"margin: 7px 7px 0px 0px;width:15px;height:15px;\" onclick=\"exit_activation();\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDIxIDc5LjE1NTc3MiwgMjAxNC8wMS8xMy0xOTo0NDowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkY0QzI3MDJGNDkyRjExRTg5ODI3ODk1Mjk5RTMyQUQ4IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkY0QzI3MDMwNDkyRjExRTg5ODI3ODk1Mjk5RTMyQUQ4Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RjRDMjcwMkQ0OTJGMTFFODk4Mjc4OTUyOTlFMzJBRDgiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RjRDMjcwMkU0OTJGMTFFODk4Mjc4OTUyOTlFMzJBRDgiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4bOS8IAAAAtElEQVR42qRRAQrCMAw0vsftA+5dKxtM2WST+i5foA+qd5KFMsOUGbgWkrtwvUpKabel9jzKohIgfCOTQ64JUTUwotmsiDgblWvCCHTABELviNiblBPfTb5xRnE49kDivdYjZBmObj8BIXMzPJ73c84TL1WIa7OEBRDd3FQ3f4djNapVInqB/RcONnJHo5F/BJEF1gJXzM0qLV04WIpY2muVE8wqbAgQciseyCHX/cdf6yXAAPdFnXsLA5z1AAAAAElFTkSuQmCC\"/></span><iframe frameborder=\"0\" src=\"../../ActivationServletReport\" style=\"width: 100%; height: 100%;\"></iframe><div>');");

        html.append(" ");
        html.append("};");
        html.append("function exit_activation(){");
        html.append(" $('#activation_post').remove();");
        html.append("}");
        html.append("</script>");
        String exprisemsg = html.toString();
        if ("1".equals(expires)) {
          exprisemsg = exprisemsg + "<script type=\"text/javascript\">$.getScript(\"./watermark.js\",function(){window.onload();});</script>";
        }

        dataMap.put("exprisemsg", exprisemsg);
      }
      dataMap.put("body", bdStr);
    } catch (DocumentException e) {
      e.printStackTrace();
    } finally {
      if (DBUtil.isJndi().booleanValue()) {
        DBUtil.closeConn(conn);
      }
    }
    return (Map<String, Object>)(Map<String, Object>)(Map<String, Object>)dataMap;
  }

  public static StringBuilder license(String bdStr, Map<String, Object> dataMap)
  {
    Map authMap = ReportEntrance.checkAuth();
    String expires =  authMap.get("expires")+"";
    if (authMap.get("show") != null) {
      bdStr = (String)authMap.get("show");
    }
    dataMap.put("expires",  authMap.get(expires)+"");
    if (!"0".equals(expires)) {
      StringBuffer html = new StringBuffer();
      html.append("<a href=\"#\" style=\"float: left;\" onclick=\"activation();\">报表应用激活</a> ");
      html.append("<style> #activation_post span img:hover{cursor: pointer;}</style>");
      html.append("<script type=\"text/javascript\"> ");
      html.append("function activation(){");
      html.append("  $(\"body\").append('<div id=\"activation_post\" style=\"overflow: hidden;float: left; position: fixed;width: 580px;height: 300px;top: 5%;left: 15%;background: #ddd;\"><span style=\"float: left;position: relative;left: 0px;padding-top: 8px; padding-left: 8px;\">激活处理页面</span><span style=\"float: right;position: relative;right: 0px;\"><img style=\"margin: 7px 7px 0px 0px;width:15px;height:15px;\" onclick=\"exit_activation();\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDIxIDc5LjE1NTc3MiwgMjAxNC8wMS8xMy0xOTo0NDowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkY0QzI3MDJGNDkyRjExRTg5ODI3ODk1Mjk5RTMyQUQ4IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkY0QzI3MDMwNDkyRjExRTg5ODI3ODk1Mjk5RTMyQUQ4Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RjRDMjcwMkQ0OTJGMTFFODk4Mjc4OTUyOTlFMzJBRDgiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RjRDMjcwMkU0OTJGMTFFODk4Mjc4OTUyOTlFMzJBRDgiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4bOS8IAAAAtElEQVR42qRRAQrCMAw0vsftA+5dKxtM2WST+i5foA+qd5KFMsOUGbgWkrtwvUpKabel9jzKohIgfCOTQ64JUTUwotmsiDgblWvCCHTABELviNiblBPfTb5xRnE49kDivdYjZBmObj8BIXMzPJ73c84TL1WIa7OEBRDd3FQ3f4djNapVInqB/RcONnJHo5F/BJEF1gJXzM0qLV04WIpY2muVE8wqbAgQciseyCHX/cdf6yXAAPdFnXsLA5z1AAAAAElFTkSuQmCC\"/></span><iframe frameborder=\"0\" src=\"../../ActivationServletReport\" style=\"width: 100%; height: 100%;\"></iframe><div>');");

      html.append(" ");
      html.append("};");
      html.append("function exit_activation(){");
      html.append(" $('#activation_post').remove();");
      html.append("}");
      html.append("</script>");
      String exprisemsg = html.toString();
      if ("1".equals(expires)) {
        exprisemsg = exprisemsg + "<script type=\"text/javascript\">$.getScript(\"./watermark.js\",function(){window.onload();});</script>";
      }
      dataMap.put("exprisemsg", exprisemsg);
    }
    return new StringBuilder(bdStr);
  }

  private static String getPrettyNumber(String number) {
    String plainString = BigDecimal.valueOf(Double.parseDouble(number)).stripTrailingZeros().toPlainString();
    if (plainString.equals("0.0")) {
      plainString = "0";
    }
    return plainString;
  }

  private static String getPrettyNumber(String number, String pattern) {
    return new JRFunExpImpl().toDef(getPrettyNumber(number), pattern).replace(".00", "");
  }

  private static int getPageCount(int recordCount, int pageSize)
  {
    int size = recordCount / pageSize;

    int mod = recordCount % pageSize;
    if (mod != 0)
      size++;
    return recordCount == 0 ? 1 : size;
  }

  public static List<String> getDataSet(String str)
  {
    List dataList = new ArrayList();
    if ((!"".equals(str)) && 
      (str.indexOf("<=") != -1)) {
      String[] arrTemp = str.split("<=");

      for (int i = 1; i < arrTemp.length; i++) {
        if ((arrTemp[i].contains(".")) && (arrTemp[i].contains(">"))) {
          String fullFiled = arrTemp[i].split(">")[0];
          dataList.add(fullFiled.substring(0, fullFiled.indexOf(".")));
        }
      }
      dataList = new ArrayList(new HashSet(dataList));
    }

    return dataList;
  }

  public static List<String> getSumCol(String str)
  {
    List sumDataList = new ArrayList();
    if (!"".equals(str)) {
      String[] arrTemp = str.split("</td>");

      for (int i = 0; i < arrTemp.length; i++) {
        if (!arrTemp[i].contains("$V{"))
          continue;
        try
        {
          String kk = arrTemp[i].substring(arrTemp[i].indexOf("$V{") + 3);
          sumDataList.add(kk.substring(0, kk.indexOf("}")));
        } catch (Exception e) {
          System.out.println(e.toString());
        }
      }

    }

    return sumDataList;
  }

  public static boolean isNumeric(String str)
  {
    if ("".equals(str))
      str = "0";
    else if (str.indexOf(".") == 0) {
      str = "0" + str;
    }
    Boolean v = Boolean.valueOf(true);
    try {
      getPrettyNumber(str);
    } catch (Exception e) {
      v = Boolean.valueOf(false);
    }

    return v.booleanValue();
  }

  public Number covNumber(String str) {
    if ((str != null) && (!"".equals(str))) {
      return Integer.valueOf(Integer.parseInt(str));
    }
    return Integer.valueOf(0);
  }

  public static Map<String, String> getBaseMap()
  {
    if (baseMap == null) {
      return PropertyUtil.getPropertyMap("base.properties");
    }
    return baseMap;
  }

  public static String getBaseFilePath()
  {
    String filePath = "";

    if (((String)baseMap.get("ismyreportpath")).equals("0"))
      filePath = BaseAction.reportfilesPath;
    else if (((String)baseMap.get("ismyreportpath")).equals("1"))
      filePath = ServletActionContext.getRequest().getSession().getServletContext().getRealPath("") + System.getProperty("file.separator") + (String)baseMap.get("myreportpath");
    else {
      filePath = (String)baseMap.get("myreportpath");
    }

    return filePath;
  }

  public static String xmlMapToStr(Map map)
  {
    StringBuilder str = new StringBuilder();
    Iterator tr = map.entrySet().iterator();
    str.append("<tr>");
    while (tr.hasNext()) {
      Map.Entry trm = (Map.Entry)tr.next();
      if (trm.getKey().toString().startsWith("td-")) {
        Map tdm = (Map)trm.getValue();
        Iterator td = tdm.entrySet().iterator();
        str.append("<td");
        while (td.hasNext()) {
          Map.Entry m = (Map.Entry)td.next();
          if ((!m.getKey().equals("text")) && (!m.getKey().equals("dic")) && (!m.getKey().equals("expText")) && (!m.getKey().equals("dynamic"))) {
            str.append(" " + m.getKey() + "='" + m.getValue() + "'");
          }
        }
        str.append(">" + tdm.get("text"));
        str.append("</td>");
      }
    }
    str.append("</tr>");
    return str.toString();
  }

  public static Map<String, Object> xmlMapToDataStr(LinkedHashMap<String, Map<String, Object>> docMap, String category, List<Map<String, Object>> list, Map<String, Object> sum, int currentPage, int pageSize, int totalRecord, String parmsModel, Boolean subreport, JexlContext jc)
  {
    StringBuilder str = new StringBuilder();
    Map bodyMap = new HashMap();
    Map cssMap = new HashMap();
    Map grp = null;
    if (!category.equals("dataarea")) {
      if ((list == null) || (list.size() == 0)) {
        list = new ArrayList();
        Map mInit = new HashMap();
        mInit.put("0", "0");
        list.add(mInit);
      }
      else if (list.size() > 1) {
        Map maplist = (Map)list.get(0);
        list = new ArrayList();
        list.add(maplist);
      }
    }
    else {
      grp = getGroupMap(docMap, list);
    }
    int r = 0;
    for (Map mapp : list) {
      Map<String,Object> map = new CaseInsensitiveMap(mapp);
      for (String kkk : docMap.keySet()) {
        Iterator tr = ((Map)docMap.get(kkk)).entrySet().iterator();
        str.append("<tr>");
        r++;
        int c = 0;
        while (tr.hasNext()) {
          Map.Entry trm = (Map.Entry)tr.next();
          if (trm.getKey().toString().startsWith("td-")) {
            Map tdm = (Map)trm.getValue();
            Iterator td = tdm.entrySet().iterator();
            String subReport = null;
            Boolean isGroup = Boolean.valueOf(false);
            Boolean isShowCell = Boolean.valueOf(true);
            if ((grp != null) && (grp.get("isgroup") != null)) {
              isGroup = (Boolean)grp.get("isgroup");
              if ((isGroup.booleanValue()) && (((Group[][])grp.get("group"))[0].length >= c + 1)) {
                isShowCell = ((Group[][])grp.get("group"))[(r - 1)][c].getShowCell();
              }
            }
            if ((grp == null) || (!isGroup.booleanValue()) || ((isGroup.booleanValue()) && (isShowCell.booleanValue()))) {
              str.append("<td");
              if ((isGroup.booleanValue()) && (((Group[][])grp.get("group"))[0].length >= c + 1) && (isShowCell.booleanValue()) && (((Group[][])grp.get("group"))[(r - 1)].length >= c + 1)) {
                str.append(" rowspan='" + ((Group[][])grp.get("group"))[(r - 1)][c].getRow() + "'");
              }
              StringBuilder ext = new StringBuilder();
              Boolean isWarning = Boolean.valueOf(false);
              String warnStr = "";
              List listcl;
              BgColor bc;
              while (td.hasNext()) {
                Map.Entry m = (Map.Entry)td.next();

                if ((m.getKey().equals("bgcolors")) && 
                  (tdm.get("isnum") != null) && (tdm.get("isnum").equals("1")) && (tdm.get("dynamic").equals("1"))) {
                  double v = Double.parseDouble(map.get(tdm.get("text")).toString());
                  listcl = JSON.parseArray(m.getValue().toString(), BgColor.class);
                  for (Iterator localIterator3 = listcl.iterator(); localIterator3.hasNext(); ) { bc = (BgColor)localIterator3.next();
                    if ((bc.getFrom() <= v) && ((bc.getTo() <= 0) || (bc.getTo() > v))) {
                      warnStr = bc.getColor();
                      isWarning = Boolean.valueOf(true);
                    }
                  }

                }

                if ((!m.getKey().equals("text")) && (!m.getKey().equals("dic")) && (!m.getKey().equals("expText")) && (!m.getKey().equals("dynamic")) && (!m.getKey().equals("group")) && (!m.getKey().equals("isnum")) && (!m.getKey().equals("image")) && (!m.getKey().equals("bgcolors"))) {
                  if (m.getKey().equals("style")) {
                    if ((!isWarning.booleanValue()) && (category.equals("dataarea")) && (r == 1) && (subReport == null) && (!subreport.booleanValue()) && (docMap.size() == 1)) {
                      cssMap.put("d_" + c, m.getValue());
                      ext.append(" class='d_" + c + "'");
                    }
                    else if ((!isWarning.booleanValue()) && (category.equals("dataarea")) && (r > 1) && (!subreport.booleanValue()) && (docMap.size() == 1)) {
                      ext.append(" class='d_" + c + "'");
                    }
                    else if (isWarning.booleanValue()) {
                      if (m.getValue().toString().indexOf("background-color") != -1) {
                        String style = m.getValue().toString();
                        ext.append(" " + m.getKey() + "='" + style.split("background-color")[0] + "background-color:" + warnStr + style.split("background-color")[1] + "'");
                      } else {
                        ext.append(" " + m.getKey() + "='" + m.getValue() + "background-color:" + warnStr + ";'");
                      }
                    } else {
                      ext.append(" " + m.getKey() + "='" + m.getValue() + "'");
                    }

                  }
                  else if ((m.getKey().equals("onclick")) && (tdm.get("dynamic").equals("1"))) {
                    ext.append(" " + m.getKey() + "='" + m.getValue().toString().replace(new StringBuilder("(#=").append(tdm.get("text")).append("#)").toString(), map.get(tdm.get("text")) == null ? "" : map.get(tdm.get("text")).toString()) + "'");
                  }
                  else if ((m.getKey().equals("class")) && (category.equals("dataarea")) && (!subreport.booleanValue()) && (docMap.size() == 1))
                    ext.append(" class='d_" + c + " " + m.getValue() + "'");
                  else {
                    ext.append(" " + m.getKey() + "='" + m.getValue() + "'");
                  }
                }

                if (!m.getKey().equals("frameid"))
                  continue;
                try {
                  String uuid = m.getValue().toString();
                  subReport = PreviewReportAction.getSubReport(uuid, parmsModel);
                } catch (Exception e) {
                  e.printStackTrace();
                }

              }

              if (subReport == null) {
                str.append(ext.toString());
              }
              str.append(">");
              if (subReport == null)
              {
                List<Image> listimg;
                if (tdm.get("dynamic").equals("1")) {
                  Object dbtext = map.get(tdm.get("text")) == null ? "" : map.get(tdm.get("text"));
                  if ((tdm.get("text").toString().startsWith("$V{")) && (tdm.get("text").toString().endsWith("}"))) {
                    String skey = tdm.get("text").toString().substring(3, tdm.get("text").toString().length() - 1);
                    dbtext = sum.get(skey);
                  }
                  if (tdm.get("dic") != null) {
                    String dic = StringUtils.indexOf(tdm.get("dic").toString(), "=") != -1 ? StringUtils.split(tdm.get("dic").toString(), "=")[1] : tdm.get("dic").toString();
                    if ((dbtext != null) && (dbtext.toString().length() > 0)) {
                      dbtext = ((Map)DBUtil.DicMap.get(dic)).get(dbtext.toString());
                    }
                  }
                  if (tdm.get("expText") != null) {
                    String exp = tdm.get("expText").toString();
                    if ((tdm.get("text").toString().startsWith("$V{")) && (tdm.get("text").toString().endsWith("}"))) {
                      exp = exp.substring(0, exp.indexOf("$V{")) + dbtext.toString() + exp.substring(exp.indexOf(tdm.get("text").toString().replace("$V{", "")) + tdm.get("text").toString().replace("$V{", "").length());
                    }
                    exp = exp.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");

                    if ((tdm.get("isnum") == null) || (!tdm.get("isnum").equals("1")))
                      exp = exp.replace("$F{" + tdm.get("text").toString().toUpperCase() + "}", "\"" + dbtext + "\"");
                    else {
                      exp = exp.replace("$F{" + tdm.get("text").toString().toUpperCase() + "}", dbtext.equals("") ? "0" : dbtext.toString());
                    }

                    if (exp.indexOf("$F") != -1) {
                      for (String rskey : map.keySet()) {
                        exp = exp.replace("$F{" + rskey.toUpperCase() + "}", map.get(rskey).equals("") ? "0" : map.get(rskey).toString());
                      }
                    }
                    dbtext = DyMethodUtil.invokeMethodNew(exp, jc);
                  }
                  if (category.equals("dataarea"))
                    str.append(dbtext == null ? "" : dbtext.toString());
                  else if ((category.equals("reporthead")) || (category.equals("reportfoot"))) {
                    if (tdm.get("text").toString().indexOf("$F{") != -1) {
                      String ntext = tdm.get("text").toString();
                      for (String textkey : map.keySet()) {
                        ntext = ntext.replace("$F{" + textkey.toUpperCase() + "}", map.get(textkey).toString());
                      }
                      if (tdm.get("base64") != null) {
                        str.append("<img src='" + tdm.get("base64").toString() + "'>");
                      } else if (tdm.get("image") != null) {
                        listimg = JSON.parseArray(tdm.get("image").toString(), Image.class);
                        for (Image img : listimg)
                          str.append("<img src='" + img.getValue().toString() + "' width='" + img.getWidth() + "' height='" + img.getHeight() + "'>");
                      }
                      else {
                        str.append(ntext);
                      }
                    } else {
                      str.append(dbtext == null ? "" : dbtext.toString());
                    }

                  }

                }
                else if (category.equals("dataarea")) {
                  str.append(tdm.get("text").toString().replace("ROWNUM", String.valueOf(pageSize * (currentPage - 1) + r)));
                }
                else if (category.equals("reportfoot")) {
                  str.append(tdm.get("text").toString().replace("{TotalRecord}", String.valueOf(totalRecord)));
                }
                else if (tdm.get("base64") != null) {
                  str.append("<img src='" + tdm.get("base64").toString() + "'>");
                } else if (tdm.get("image") != null) {
                    listimg = JSON.parseArray(tdm.get("image").toString(), Image.class);
                  for (Image img : listimg)
                    str.append("<img src='" + img.getValue().toString() + "' width='" + img.getWidth() + "' height='" + img.getHeight() + "'>");
                }
                else {
                  str.append(tdm.get("text"));
                }

              }
              else
              {
                str.append(subReport);
              }
              str.append("</td>");
            }
            c++;
          }
        }
        str.append("</tr>");
      }
    }
    String htmlStr = str.toString();
    bodyMap.put("body", htmlStr);
    bodyMap.put("css", cssMap);
    return bodyMap;
  }

  public static Map<String, Integer> setExcelRow(SXSSFWorkbook wb, Sheet sheet1, Map<String, Integer> cn, LinkedHashMap<String, Map<String, Object>> docMap, String category, List<Map<String, Object>> list, Map<String, Object> sum, int currentPage, int pageSize, int totalRecord, String parmsModel, JexlContext jc)
  {
    StringBuilder str = new StringBuilder();
    Map grp = null;
    Map ncn = new HashMap();
    XSSFCellStyle[] cellStyles = null;
    short[] rowheight = null;
    if (!category.equals("dataarea")) {
      if ((list == null) || (list.size() == 0)) {
        list = new ArrayList();
        Map mInit = new HashMap();
        mInit.put("0", "0");
        list.add(mInit);
      }
      else if (list.size() > 1) {
        Map maplist = (Map)list.get(0);
        list = new ArrayList();
        list.add(maplist);
      }
    }
    else {
      grp = getGroupMap(docMap, list);
      int xcs = 0;
      if (list.size() > 0) {
        xcs = ((Map)list.get(0)).size();
        for (String kkk : docMap.keySet()) {
          if (((Map)docMap.get(kkk)).size() - 1 > xcs) {
            xcs = ((Map)docMap.get(kkk)).size() - 1;
          }
        }
      }
      cellStyles = new XSSFCellStyle[xcs];
      rowheight = new short[xcs];
    }
    SXSSFCell cell = null;
    SXSSFRow rw = null;
    int r = ((Integer)cn.get("row")).intValue();
    int n = 0;
    Boolean loopWaring = Boolean.valueOf(false);
    for (Map mapp : list) {
      Map<String,Object> map = new CaseInsensitiveMap(mapp);
      for (String kkk : docMap.keySet()) {
        Iterator tr = ((Map)docMap.get(kkk)).entrySet().iterator();
        rw = (SXSSFRow)sheet1.createRow(r);
        n++;
        int c = 0;
        int nc = 0;

        while (tr.hasNext()) {
          Map.Entry trm = (Map.Entry)tr.next();
          if (trm.getKey().toString().startsWith("td-")) {
            Map tdm = (Map)trm.getValue();
            Iterator td = tdm.entrySet().iterator();
            if ((cn.get(r + "-" + c) != null) && (((Integer)cn.get(r + "-" + c)).intValue() > 0)) {
              for (int nnn = 0; nnn < cn.size(); nnn++) {
                if ((cn.get(r + "-" + nnn) != null) && (((Integer)cn.get(r + "-" + nnn)).intValue() > 0)) {
                  nc += ((Integer)cn.get(r + "-" + nnn)).intValue();
                  c += ((Integer)cn.get(r + "-" + nnn)).intValue();
                }
              }
            }
            cell = rw.createCell(nc);
            String subReport = null;
            Boolean isGroup = Boolean.valueOf(false);
            Boolean isShowCell = Boolean.valueOf(true);
            if ((grp != null) && (grp.get("isgroup") != null)) {
              isGroup = (Boolean)grp.get("isgroup");
              if ((isGroup.booleanValue()) && (((Group[][])grp.get("group"))[0].length >= c + 1)) {
                isShowCell = ((Group[][])grp.get("group"))[(n - 1)][c].getShowCell();
              }
            }
            if ((grp == null) || (!isGroup.booleanValue()) || ((isGroup.booleanValue()) && (isShowCell.booleanValue()))) {
              int rowspan = 1;
              int colspan = 1;
              if ((isGroup.booleanValue()) && (((Group[][])grp.get("group"))[0].length >= c + 1) && (isShowCell.booleanValue()) && (((Group[][])grp.get("group"))[(n - 1)].length >= c + 1)) {
                rowspan = ((Group[][])grp.get("group"))[(n - 1)][c].getRow().intValue();
              }
              Boolean isWarning = Boolean.valueOf(false);
              String warnStr = "";
              String[] styleArr;
              if ((!category.equals("dataarea")) || (n == 1) || (docMap.size() > 1) || (loopWaring.booleanValue())) {
                XSSFCellStyle cellStyle = (XSSFCellStyle)wb.createCellStyle();
                XSSFFont cellFont = (XSSFFont)wb.createFont();
                short rheight = 200;
                while (td.hasNext()) {
                  Map.Entry m = (Map.Entry)td.next();
                  if ((m.getKey().equals("bgcolors")) && 
                    (tdm.get("isnum") != null) && (tdm.get("isnum").equals("1")) && (tdm.get("dynamic").equals("1"))) {
                    double v = Double.parseDouble(map.get(tdm.get("text")).toString());
                    List<BgColor> listcl = JSON.parseArray(m.getValue().toString(), BgColor.class);
                    for (BgColor bc : listcl) {
                      if ((bc.getFrom() <= v) && ((bc.getTo() <= 0) || (bc.getTo() > v))) {
                        warnStr = bc.getColor();
                        isWarning = Boolean.valueOf(true);
                        loopWaring = Boolean.valueOf(true);
                      }
                    }
                  }

                  if (m.getKey().equals("style")) {
                    styleArr = StringUtils.split(m.getValue().toString(), ";");
                    for (int s = 0; s < styleArr.length; s++) {
                      String[] sty = StringUtils.split(styleArr[s], ":");
                      if ((sty[0].equals("height")) && (sty.length == 2)) {
                        rheight = (short)(new Short(StringUtils.replace(sty[1], "px", "")).shortValue() * 16);
                        rw.setHeight(rheight);
                      }
                      if ((sty[0].equals("width")) && (sty.length == 2)) {
                        int w = (int)(Integer.parseInt(StringUtils.replace(sty[1], "px", "")) * 36.0F);
                        sheet1.setColumnWidth(c, w > 65000 ? 65000 : w);
                      }
                      if ((sty[0].equals("word-break")) && (sty.length == 2)) {
                        cellStyle.setWrapText(true);
                      }
                      if ((sty[0].equals("text-align")) && (sty.length == 2)) {
                        if (sty[1].equals("center"))
                          cellStyle.setAlignment(HorizontalAlignment.CENTER);
                        else if (sty[1].equals("right")) {
                          cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                        }

                      }

                      if ((sty[0].equals("vertical-align")) && (sty.length == 2)) {
                        if (sty[1].equals("middle"))
                          cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        else if (sty[1].equals("top"))
                          cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
                        else if (sty[1].equals("bottom")) {
                          cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
                        }
                      }
                      if ((sty[0].equals("font-family")) && (sty.length == 2)) {
                        cellFont.setFontName(sty[1]);
                      }
                      if ((sty[0].equals("text-decoration")) && (sty.length == 2)) {
                        if (sty[1].indexOf("underline") != -1) {
                          cellFont.setUnderline(FontUnderline.SINGLE);
                        }
                        if (sty[1].indexOf("line-throug") != -1) {
                          cellFont.setStrikeout(true);
                        }
                      }
                      if ((sty[0].equals("font-style")) && (sty.length == 2) && (sty[1].equals("italic"))) {
                        cellFont.setItalic(true);
                      }
                      if ((sty[0].equals("background-color")) && (sty.length == 2) && 
                        (!"rgba(0, 0, 0, 0)".equals(sty[1]))) { String rgb = StringUtils.replace(StringUtils.replace(StringUtils.replace(sty[1], "rgba(", ""), ")", ""), " ", "");
                        String[] rarr = StringUtils.split(rgb, ",");
                        Color color;
                        try { color = new Color(Integer.parseInt(rarr[0]), Integer.parseInt(rarr[1]), Integer.parseInt(rarr[2]));
                        }
                        catch (Exception e)
                        {
                          //Color color;
                          color = Color.WHITE;
                        }
                        cellStyle.setFillForegroundColor(new XSSFColor(color));
                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                      }

                      if ((sty[0].equals("font-size")) && (sty.length == 2)) {
                        cellFont.setFontHeightInPoints((short)(Integer.parseInt(StringUtils.replace(sty[1], "px", "")) - 3));
                      }
                      if ((sty[0].equals("color")) && (sty.length == 2))
                      {
                        if (!"rgb(0, 0, 0)".equals(sty[1])) { String rgb = StringUtils.replace(StringUtils.replace(StringUtils.replace(sty[1], "rgb(", ""), ")", ""), " ", "");
                          String[] rarr = StringUtils.split(rgb, ",");
                          Color color;
                          try { color = new Color(Integer.parseInt(rarr[0]), Integer.parseInt(rarr[1]), Integer.parseInt(rarr[2]));
                          }
                          catch (Exception e)
                          {
                           // Color color;
                            color = Color.BLACK;
                          }
                          cellFont.setColor(new XSSFColor(color));
                        }
                      }
                      if ((sty[0].equals("font-weight")) && (sty.length == 2) && (sty[1].equals("700"))) {
                        cellFont.setBold(true);
                      }
                    }

                    if ((isWarning.booleanValue()) && (warnStr.length() > 0)) { String rgb = StringUtils.replace(StringUtils.replace(StringUtils.replace(warnStr, "rgba(", ""), ")", ""), " ", "");
                      String[] rarr = StringUtils.split(rgb, ",");
                      Color color;
                      try { color = new Color(Integer.parseInt(rarr[0]), Integer.parseInt(rarr[1]), Integer.parseInt(rarr[2]));
                      }
                      catch (Exception e)
                      {
                       // Color color;
                        color = Color.WHITE;
                      }
                      cellStyle.setFillForegroundColor(new XSSFColor(color));
                      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    }
                  }
                }
                if ((tdm.get("isnum") != null) && (tdm.get("isnum").equals("1")))
                {
                  if (tdm.get("expText") != null)
                    cellStyle.setDataFormat(4);
                  else {
                    cellStyle.setDataFormat(0);
                  }

                }
                else
                {
                  cellStyle.setDataFormat(49);
                }
                if (tdm.get("style").toString().indexOf("border-bottom:1px solid rgb(0,0,0)") != -1) {
                  cellStyle.setBorderBottom(BorderStyle.THIN);
                }
                if (tdm.get("style").toString().indexOf("border-left:1px solid rgb(0,0,0)") != -1) {
                  cellStyle.setBorderLeft(BorderStyle.THIN);
                }
                if (tdm.get("style").toString().indexOf("border-right:1px solid rgb(0,0,0)") != -1) {
                  cellStyle.setBorderRight(BorderStyle.THIN);
                }
                if (tdm.get("style").toString().indexOf("border-top:1px solid rgb(0,0,0)") != -1) {
                  cellStyle.setBorderTop(BorderStyle.THIN);
                }

                cellStyle.setFont(cellFont);
                cell.setCellStyle(cellStyle);

                if ((category.equals("dataarea")) && (n == 1)) {
                  cellStyle.setFont(cellFont);
                  cellStyles[nc] = ((XSSFCellStyle)wb.createCellStyle());
                  cellStyles[nc] = cellStyle;
                  rowheight[nc] = rheight;
                }
              } else {
                cell.setCellStyle(cellStyles[nc]);
                rw.setHeight(rowheight[nc]);
              }
              if (tdm.get("rowspan") != null) {
                rowspan = Integer.parseInt(tdm.get("rowspan").toString());
              }
              if (tdm.get("colspan") != null) {
                colspan = Integer.parseInt(tdm.get("colspan").toString());
              }

              if ((rowspan > 1) || (colspan > 1)) {
                CellRangeAddress cra = new CellRangeAddress(r, r + rowspan - 1, c, c + colspan - 1);
                sheet1.addMergedRegion(cra);
                if (tdm.get("style").toString().indexOf("border-bottom:1px solid rgb(0,0,0)") != -1) {
                  RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet1);
                }
                if (tdm.get("style").toString().indexOf("border-left:1px solid rgb(0,0,0)") != -1) {
                  RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet1);
                }
                if (tdm.get("style").toString().indexOf("border-right:1px solid rgb(0,0,0)") != -1) {
                  RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet1);
                }
                if (tdm.get("style").toString().indexOf("border-top:1px solid rgb(0,0,0)") != -1) {
                  RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet1);
                }
                if (rowspan > 1) {
                  for (int rn = rowspan - 1; rn > 0; rn--) {
                    ncn.put(r + rn + "-" + c, Integer.valueOf(rowspan - 1));
                    if (colspan > 1) {
                      for (int ccn = 1; ccn < colspan; ccn++) {
                        ncn.put(r + rn + "-" + (c + ccn), Integer.valueOf(rowspan - 1));
                      }
                    }
                  }
                }
                if (colspan > 1) {
                  c = c + colspan - 1;
                }
              }

              String valueStr = "";
              if (subReport == null) {
                if (tdm.get("dynamic").equals("1")) {
                  Object dbtext = map.get(tdm.get("text")) == null ? "" : map.get(tdm.get("text"));
                  if ((tdm.get("text").toString().startsWith("$V{")) && (tdm.get("text").toString().endsWith("}"))) {
                    String skey = tdm.get("text").toString().substring(3, tdm.get("text").toString().length() - 1);
                    dbtext = sum.get(skey);
                  }
                  if (tdm.get("dic") != null) {
                    String dic = StringUtils.indexOf(tdm.get("dic").toString(), "=") != -1 ? StringUtils.split(tdm.get("dic").toString(), "=")[1] : tdm.get("dic").toString();
                    if ((dbtext != null) && (dbtext.toString().length() > 0)) {
                      dbtext = ((Map)DBUtil.DicMap.get(dic)).get(dbtext.toString());
                    }
                  }
                  if (((tdm.get("isnum") == null) || (!tdm.get("isnum").equals("1"))) && 
                    (tdm.get("expText") != null)) {
                    String exp = tdm.get("expText").toString();
                    if ((tdm.get("text").toString().startsWith("$V{")) && (tdm.get("text").toString().endsWith("}"))) {
                      exp = exp.substring(0, exp.indexOf("$V{")) + dbtext.toString() + exp.substring(exp.indexOf(tdm.get("text").toString().replace("$V{", "")) + tdm.get("text").toString().replace("$V{", "").length());
                    }
                    exp = exp.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");

                    if ((tdm.get("isnum") == null) || (!tdm.get("isnum").equals("1")))
                      exp = exp.replace("$F{" + tdm.get("text").toString().toUpperCase() + "}", "\"" + dbtext + "\"");
                    else {
                      exp = exp.replace("$F{" + tdm.get("text").toString().toUpperCase() + "}", dbtext.equals("") ? "0" : dbtext.toString());
                    }

                    if (exp.indexOf("$F") != -1) {
                      for (String rskey : map.keySet()) {
                        exp = exp.replace("$F{" + rskey.toUpperCase() + "}", map.get(rskey).equals("") ? "0" : map.get(rskey).toString());
                      }
                    }
                    dbtext = DyMethodUtil.invokeMethodNew(exp, jc);
                  }

                  if (category.equals("dataarea"))
                    valueStr = dbtext == null ? "" : dbtext.toString();
                  else if ((category.equals("reporthead")) || (category.equals("reportfoot"))) {
                    if (tdm.get("text").toString().indexOf("$F{") != -1) {
                      String ntext = tdm.get("text").toString();
                      for (String textkey : map.keySet()) {
                        ntext = ntext.replace("$F{" + textkey.toUpperCase() + "}", map.get(textkey).toString());
                      }
                      valueStr = ntext;
                    } else {
                      valueStr = dbtext == null ? "" : dbtext.toString();
                    }
                  }
                }
                else if (category.equals("dataarea")) {
                  valueStr = tdm.get("text").toString().replace("ROWNUM", String.valueOf(pageSize * (currentPage - 1) + n));
                }
                else if (category.equals("reportfoot")) {
                  valueStr = tdm.get("text").toString().replace("{TotalRecord}", String.valueOf(totalRecord));
                } else {
                  valueStr = tdm.get("text").toString();
                }

              }

              if ((valueStr.length() > 0) && (valueStr.indexOf("<img") != -1)) {
                String nvalueStr = valueStr.replaceAll("<img\\s[^>]+>", "");
                cell.setCellValue(nvalueStr);
                drawPictureInfoExcel(wb, sheet1, nvalueStr, valueStr, r, r + rowspan - 1, nc, c);
              } else {
                if (tdm.get("base64") != null) {
                  drawPictureInfoExcel(wb, sheet1, "", tdm.get("base64").toString(), r, r + rowspan - 1, nc, c);
                }
                if (tdm.get("image") != null) {
                  drawPictureInfoExcel(wb, sheet1, tdm.get("text").toString(), tdm.get("image").toString(), r, r + rowspan - 1, nc, c);
                }
                else if ((tdm.get("isnum") == null) || (!tdm.get("isnum").equals("1")))
                  cell.setCellValue(valueStr);
                else {
                  cell.setCellValue((valueStr == null) || (valueStr.equals("")) ? 0.0D : Double.parseDouble(valueStr));
                }
              }

            }

            c++;
            nc = c;
          }
        }
        r++;
      }
    }
    ncn.put("row", Integer.valueOf(r));
    return ncn;
  }

  public static Map<String, Object> getGroupMap(LinkedHashMap<String, Map<String, Object>> trMap, List<Map<String, Object>> list)
  {
    Map map = new HashMap();
    Group[][] grp = null;
    if (list.size() > 0) {
      for (String kkk : trMap.keySet())
      {
        Map<String,Object> groupColMap = new LinkedHashMap();
        Iterator it = ((Map)trMap.get(kkk)).entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry trm = (Map.Entry)it.next();
          if (trm.getKey().toString().startsWith("td-")) {
            Map tdm = (Map)trm.getValue();
            Iterator td = tdm.entrySet().iterator();
            if ((tdm.get("group") != null) && (tdm.get("dynamic").equals("1"))) {
              groupColMap.put(tdm.get("text").toString(), "");
            }
          }
        }
        if (groupColMap.size() > 0) {
          String[] groupArr = new String[groupColMap.size()];
          int g = 0;
          for (Map.Entry mtd : groupColMap.entrySet()) {
            groupArr[g] = mtd.getKey().toString();
            g++;
          }
          map.put("isgroup", Boolean.valueOf(true));
          grp = new Group[list.size()][groupColMap.size()];
          int r = 0;
          for (Map mm : list) {
            int c = 0;
            for (String mtd : groupArr) {
              grp[r][c] = new Group();
              if ((c == 0) || ((c > 0) && (groupColMap.get(groupArr[(c - 1)]).equals(mm.get(groupArr[(c - 1)]))))) {
                if (groupColMap.get(mtd).equals("")) {
                  grp[r][c].setRow(Integer.valueOf(1));
                  grp[r][c].setShowCell(Boolean.valueOf(true));
                  groupColMap.put(mtd, mm.get(mtd));
                }
                else if ((groupColMap.get(mtd).equals(mm.get(mtd))) && ((c == 0) || ((c > 0) && (!grp[r][(c - 1)].getShowCell().booleanValue())))) {
                  for (int j = r - 1; j >= 0; j--) {
                    if (grp[j][c].getShowCell().booleanValue()) {
                      grp[j][c].setRow(Integer.valueOf(r - j + 1));
                      break;
                    }
                  }
                  grp[r][c].setRow(Integer.valueOf(1));
                  grp[r][c].setShowCell(Boolean.valueOf(false));
                } else {
                  for (int j = r - 1; j >= 0; j--) {
                    if (grp[j][c].getShowCell().booleanValue()) {
                      grp[j][c].setRow(Integer.valueOf(r - j));
                      break;
                    }
                  }
                  groupColMap.put(mtd, mm.get(mtd));
                  grp[r][c].setRow(Integer.valueOf(1));
                  grp[r][c].setShowCell(Boolean.valueOf(true));
                }
              }
              else {
                for (int j = r - 1; j >= 0; j--) {
                  if (grp[j][c].getShowCell().booleanValue()) {
                    grp[j][c].setRow(Integer.valueOf(r - j));
                    break;
                  }
                }
                groupColMap.put(mtd, mm.get(mtd));
                grp[r][c].setRow(Integer.valueOf(1));
                grp[r][c].setShowCell(Boolean.valueOf(true));
              }
              c++;
            }
            r++;
          }

          map.put("group", grp);
        } else {
          map.put("isgroup", Boolean.valueOf(false));
        }
      }
    }
    return map;
  }

  public static Map getDsMap(Map dmap)
  {
    Map ds = new HashMap();
    List a = new ArrayList();
    List b = new ArrayList();
    Map dicWhereMap = new CaseInsensitiveMap();
    Map sum = new CaseInsensitiveMap();

    Map dtlMap = new HashMap();
    Boolean subreport = Boolean.valueOf(false);
    Boolean isdtl = Boolean.valueOf(false);
    Iterator it = dmap.entrySet().iterator();
    int rowindex = 0;
    while (it.hasNext()) {
      Map.Entry e = (Map.Entry)it.next();
      Map lr = (Map)e.getValue();
      String category = lr.get("category").toString();
      Iterator itr = lr.entrySet().iterator();
      int colindex = 0;
      while (itr.hasNext()) {
        Map.Entry trm = (Map.Entry)itr.next();
        if (trm.getKey().toString().startsWith("td-")) {
          Map tdm = (Map)trm.getValue();

          if (tdm.get("colspan") != null)
            colindex += Integer.parseInt(tdm.get("colspan").toString());
          else {
            colindex++;
          }

          if ((tdm.get("frameid") != null) && (tdm.get("frameid").toString().length() > 0)) {
            subreport = Boolean.valueOf(true);
          }

          if (tdm.get("dynamic").toString().equals("1")) {
            String d = "";
            if (tdm.get("text").toString().indexOf("$F") != -1) {
              Pattern r = Pattern.compile("(\\$F\\{[\\w\\-\\.]+})");
              Matcher m = r.matcher(tdm.get("text").toString());
              while (m.find()) {
                String rt = tdm.get("text").toString().substring(m.start(), m.end()).substring(3);
                if (rt.indexOf(".") != -1)
                  d = rt.split("\\.")[0].toLowerCase();
              }
            }
            else {
              d = tdm.get("text").toString().split("\\.")[0].toLowerCase();
            }
            if ((tdm.get("dic") != null) && 
              (StringUtils.indexOf(tdm.get("dic").toString(), "=") != -1) && (StringUtils.indexOf(tdm.get("dic").toString(), ".") != -1)) {
              String[] dts = StringUtils.split(tdm.get("dic").toString(), ".");
              List ls = new ArrayList();
              String dt1 = StringUtils.split(dts[1], "=")[1];
              if ((dicWhereMap.size() > 0) && (dicWhereMap.get(dts[0]) != null)) {
                ls = (List)dicWhereMap.get(dts[0]);
                if (!ls.contains(dt1)) {
                  ls.add(dt1);
                  dicWhereMap.put(dts[0], ls);
                }
              } else {
                ls.add(dt1);
                dicWhereMap.put(dts[0], ls);
              }
            }

            if ((category.equals("reporthead")) || (category.equals("reportfoot"))) {
              if ((!d.startsWith("$V{")) && (!d.endsWith("}"))) {
                if (a.size() == 0) {
                  a.add(d);
                }
                else if (!a.contains(d)) {
                  a.add(d);
                }
              }

              if ((category.equals("reporthead")) && 
                (tdm.get("expText") != null) && (tdm.get("expText").toString().startsWith("expFuntionChange.gpc("))) {
                isdtl = Boolean.valueOf(true);
                Map mdtl = new HashMap();
                int colspan = tdm.get("colspan") != null ? Integer.parseInt(tdm.get("colspan").toString()) : 1;
                mdtl.put("rowindex", Integer.valueOf(rowindex));
                mdtl.put("colindex", Integer.valueOf(colindex - colspan));
                mdtl.put("colspan", Integer.valueOf(colspan));
                dtlMap.put(tdm.get("text").toString(), mdtl);
              }

              if ((category.equals("reportfoot")) && 
                (tdm.get("text").toString().startsWith("$V{")) && (tdm.get("text").toString().endsWith("}"))) {
                String s = StringUtils.substring(tdm.get("text").toString(), 3, tdm.get("text").toString().length() - 1);
                if (sum.size() == 0) {
                  sum.put(s, Double.valueOf(0.0D));
                }
                else if (sum.get(s) == null) {
                  sum.put(s, Double.valueOf(0.0D));
                }
              }

            }

            if (category.equals("dataarea")) {
              if (b.size() == 0) {
                b.add(d);
              }
              else if (!b.contains(d)) {
                b.add(d);
              }
            }
          }
        }
      }

      rowindex++;
    }
    ds.put("a", a);
    ds.put("b", b);
    ds.put("dicWhere", dicWhereMap);
    ds.put("sum", sum);
    ds.put("isdtl", isdtl);
    ds.put("dtl", dtlMap);
    ds.put("subreport", subreport);

    return ds;
  }

  public static String getKeyFromDatasetMap(Map dataSetMap, String ds, String filedname, String param)
  {
    String str = null;
    Map mds = (Map)dataSetMap.get(ds);
    if (mds != null) {
      Map mfds = (Map)mds.get("fields");
      if (mfds != null) {
        Map mf = (Map)mfds.get(filedname);
        if (mf.get(param) != null) {
          str = mf.get(param).toString();
        }
      }
    }
    return str;
  }

  private static void drawPictureInfoExcel(SXSSFWorkbook wb, Sheet sheet, String txt, String img, int firstRow, int endRow, int firtCol, int endCol)
  {
    try
    {
      String pictureUrl = null;
      if ((!"".equals(txt)) && 
        (img.length() > 0) && (img.indexOf("src=\"") != -1)) {
        pictureUrl = img.split("src=\"")[1].split("\"")[0];
      }

      if ((StringUtils.isNotBlank(pictureUrl)) || (("".equals(txt)) && (img.length() > 0))) {
        int pictureIdx = 0;
        Drawing drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = wb.getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
        anchor.setRow1(firstRow);
        anchor.setRow2(endRow);
        anchor.setCol1(firtCol);
        anchor.setCol2(endCol);
        anchor.setDx1(0);
        anchor.setDy1(0);
        anchor.setDx2(0);
        anchor.setDy2(0);

        if ("".equals(txt)) {
          BASE64Decoder decoder = new BASE64Decoder();
          if (img.startsWith("data:image/png;base64,")) {
            byte[] data = decoder.decodeBuffer(img.replace("data:image/png;base64,", ""));

            for (int i = 0; i < data.length; i++) {
              if (data[i] < 0)
              {
                int tmp239_237 = i;
                byte[] tmp239_235 = data; tmp239_235[tmp239_237] = (byte)(tmp239_235[tmp239_237] + 256);
              }
            }
            pictureIdx = wb.addPicture(data, 6);
            Picture pict = drawing.createPicture(anchor, pictureIdx);

            pict.resize();
          } else {
            List<Image> listimg = JSON.parseArray(img, Image.class);
            for (Image image : listimg) {
              byte[] data = decoder.decodeBuffer(image.getValue().replace("data:image/png;base64,", ""));

              for (int i = 0; i < data.length; i++) {
                if (data[i] < 0)
                {
                  int tmp362_360 = i;
                  byte[] tmp362_358 = data; tmp362_358[tmp362_360] = (byte)(tmp362_358[tmp362_360] + 256);
                }
              }
              pictureIdx = wb.addPicture(data, 6);
              Picture pict = drawing.createPicture(anchor, pictureIdx);

              pict.resize();
            }
          }
        } else {
          URL url = new URL(pictureUrl);

          HttpURLConnection conn = (HttpURLConnection)url.openConnection();

          conn.setRequestMethod("GET");

          conn.setConnectTimeout(5000);

          InputStream inStream = conn.getInputStream();
          byte[] data = readInputStream(inStream);
          String fileExt = getExtensionName(pictureUrl);
          if ((fileExt.toLowerCase().equals("jpg")) || (fileExt.toLowerCase().equals("jpeg")))
            pictureIdx = wb.addPicture(data, 5);
          else if (fileExt.toLowerCase().equals("png")) {
            pictureIdx = wb.addPicture(data, 6);
          }
          Picture pict = drawing.createPicture(anchor, pictureIdx);

          pict.resize();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static byte[] readInputStream(InputStream inStream) throws Exception {
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    byte[] buffer = new byte[1024];

    int len = 0;

    while ((len = inStream.read(buffer)) != -1)
    {
      outStream.write(buffer, 0, len);
    }

    inStream.close();

    return outStream.toByteArray();
  }

  private static String getExtensionName(String filename)
  {
    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < filename.length() - 1)) {
        return filename.substring(dot + 1);
      }
    }
    return filename;
  }
}

/* 
 * Qualified Name:     report.java.jrreport.util.JRUtilNew
*
 */