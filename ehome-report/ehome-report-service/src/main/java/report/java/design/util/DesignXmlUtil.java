package report.java.design.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import report.java.design.bean.XMLbean;
import report.java.entity.xml.ColEntity;
import report.java.entity.xml.DataSetEntity;
import report.java.entity.xml.FieldEntity;
import report.java.entity.xml.ParmEntity;
import report.java.entity.xml.ReportEntity;
import report.java.entity.xml.RowEntity;
import report.java.jrreport.service.PreviewService;

public class DesignXmlUtil
{
  public static Map<String, Object> xmlMap = new HashMap();

  public static boolean saveXML(String uuid, String type, String desc, String tableData, String dataSetsModel, String expressionData, String parmsModel, String parmsExtModel, String fieldModel, String filePath, String reportVersion, String reportMemo, String mainUuid)
  {
    try
    {
      if (tableData.equals("")) {
        return false;
      }
      Document doc = DocumentHelper.createDocument();
      Element root = doc.addElement("dhccReport");
      root.addAttribute("uuid", uuid);

      root.addAttribute("reportStyle", type);
      root.addAttribute("reportDescription", desc);
      root.addAttribute("reportVersion", (reportVersion == null) || ("".equals(reportVersion)) ? "1.0" : reportVersion);
      root.addAttribute("reportMemo", reportMemo);
      root.addAttribute("mainUuid", mainUuid);
      if ((dataSetsModel != null) && (!dataSetsModel.equals(""))) {
        Document dataSets = DocumentHelper.parseText(dataSetsModel);
        root.add(dataSets.getRootElement());
      } else {
        root.addElement("datasets");
      }
      if ((parmsModel != null) && (!parmsModel.equals(""))) {
        Document parmsModels = DocumentHelper.parseText(parmsModel);
        root.add(parmsModels.getRootElement());
      } else {
        root.addElement("parmsList");
      }
      if ((parmsExtModel != null) && (!parmsExtModel.equals(""))) {
        Document parmsExtModels = DocumentHelper.parseText(parmsExtModel);
        root.add(parmsExtModels.getRootElement());
      } else {
        root.addElement("parmsExtList");
      }
      Document table = DocumentHelper.parseText(tableData);
      root.add(table.getRootElement());
      if ((expressionData != null) && (!expressionData.equals(""))) {
        Element exp = root.addElement("expression");
        exp.setText(expressionData);
      }

      if ((fieldModel != null) && (!fieldModel.equals(""))) {
        Document fieldModels = DocumentHelper.parseText(fieldModel);
        root.add(fieldModels.getRootElement());
      } else {
        root.addElement("fillreports");
      }
      String xml = root.getDocument().asXML();
      File f = new File(filePath);
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
      pw.write(xml);
      pw.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  public static Boolean reportToXML(ReportEntity report, String filePath)
  {
    try
    {
      String xml = reportToElement(report).getDocument().asXML();
      File f = new File(filePath);
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
      pw.write(xml);
      pw.close();
    } catch (Exception e) {
      e.printStackTrace();
      return Boolean.valueOf(false);
    }
    return Boolean.valueOf(true);
  }

  public static Element reportToElement(ReportEntity report)
  {
    Document doc = DocumentHelper.createDocument();
    Element root = doc.addElement("dhccReport");
    try {
      root.addAttribute("version", "2.0");
      root.addAttribute("uuid", report.getUuid());

      root.addAttribute("reportStyle", report.getReportStyle());
      root.addAttribute("reportDescription", report.getReportDescription());
      root.addAttribute("reportVersion", (report.getReportVersion() == null) || ("".equals(report.getReportVersion())) ? "1.0" : report.getReportVersion());
      root.addAttribute("reportMemo", report.getReportMemo());
      root.addAttribute("mainUuid", report.getMainUuid());
      Element datasets = root.addElement("datasets");
      Element dataset;
      Element parameters;
      if (report.getDataSets().size() > 0) {
        for (DataSetEntity ds : report.getDataSets()) {
          dataset = datasets.addElement("dataset");
          dataset.addAttribute("name", ds.getName());
          if (ds.getIsdic() != null) {
            dataset.addAttribute("isdic", ds.getIsdic().toString());
          }
          Element query = dataset.addElement("query");
          Element datasourcename = query.addElement("datasourcename");
          datasourcename.addText(ds.getDatasourcename());
          Element commandtext = query.addElement("commandtext");
          commandtext.addText(ds.getCommandtext());
          parameters = query.addElement("parameters");
          Element parameter;
          for (String par : ds.getParameters()) {
            parameter = parameters.addElement("parameter");
            parameter.addAttribute("name", par);
          }
          Element fields = dataset.addElement("fields");
          for (FieldEntity fd : ds.getFileds()) {
            Element field = fields.addElement("field");
            field.addAttribute("name", fd.getName());
            if (fd.getDicType() != null) {
              field.addAttribute("dicType", fd.getDicType());
            }
            Element datafield = field.addElement("datafield");
            datafield.addText(fd.getName());
            Element datatype = field.addElement("datatype");
            datatype.addText(fd.getDatatype());
          }
        }
      }
      Element parmsList = root.addElement("parmsList");
      Element parm;
      for (ParmEntity pm : report.getParmsList()) {
        parm = parmsList.addElement("parm");
        parm.addAttribute("name", pm.getName());
        parm.addAttribute("cname", pm.getCname());
        parm.addAttribute("dbtype", pm.getDbtype());
        parm.addAttribute("isnull", pm.getIsnull());
        parm.addAttribute("type", pm.getType());
        parm.addAttribute("vl", pm.getVl());
        parm.addAttribute("showtype", pm.getShowtype());
      }
      Element body = root.addElement("body");
      body.addAttribute("pageorder", report.getBodyPageorder().toString());
      body.addAttribute("pagesize", report.getBodyPagesize().toString());
      body.addAttribute("maxR", report.getMaxR().toString());
      body.addAttribute("maxC", report.getMaxC().toString());
      body.addAttribute("toolbar", report.getToolbar());
      for (RowEntity rows : report.getRows()) {
        Element row = body.addElement("row");
        row.addAttribute("category", rows.getCategory());
        for (ColEntity cols : rows.getCols()) {
          Element col = row.addElement("col");
          col.addText(cols.getValue());
          col.addAttribute("expText", cols.getExpText());
          col.addAttribute("dic", cols.getDic());
          col.addAttribute("link", cols.getLink());
          col.addAttribute("frameid", cols.getFrameid());
          col.addAttribute("width", cols.getWidth());
          col.addAttribute("colwidth", cols.getColwidth());
          col.addAttribute("height", cols.getHeight());
          col.addAttribute("font-family", cols.getFont_family());
          col.addAttribute("font-size", cols.getFont_size());
          col.addAttribute("font-color", cols.getFont_color());
          col.addAttribute("font-weight", cols.getFont_weight());
          col.addAttribute("font-style", cols.getFont_style());
          col.addAttribute("text-decoration", cols.getText_decoration());
          col.addAttribute("vertical-align", cols.getVertical_align());
          col.addAttribute("text-align", cols.getText_align());
          col.addAttribute("background-color", cols.getBackground_color());
          col.addAttribute("border", cols.getBorder());
          col.addAttribute("white-space", cols.getWhite_space());
          if ((cols.getRowspan().intValue() != 1) || (cols.getColspan().intValue() != 1))
          {
            if ((cols.getRowspan() != null) && (cols.getRowspan().intValue() > 0)) {
              col.addAttribute("rowspan", cols.getRowspan().toString());
            }
            if ((cols.getColspan() != null) && (cols.getColspan().intValue() > 0)) {
              col.addAttribute("colspan", cols.getColspan().toString());
            }
          }
          if ((cols.getBase64() != null) && (cols.getBase64nodes() != null)) {
            col.addAttribute("base64", cols.getBase64());
            col.addAttribute("base64nodes", cols.getBase64nodes());
          }
          if ((cols.getImages() != null) && (cols.getImages().length() > 0)) {
            col.addAttribute("image", cols.getImages());
          }
          if ((cols.getBgcolors() != null) && (cols.getBgcolors().length() > 0)) {
            col.addAttribute("bgcolors", cols.getBgcolors());
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return root;
  }

  public static ReportEntity openXMLNew(String filePath)
  {
    ReportEntity report = new ReportEntity();
    try {
      File f = new File(filePath);
      if ((!f.exists()) || (!f.isFile())) {
        return null;
      }
      Element dhccReport = getElementAsText(filePath, "dhccReport");
      report.setUuid(dhccReport.attributeValue("uuid"));
      report.setVersion(Double.valueOf(dhccReport.attributeValue("version") != null ? Double.parseDouble(dhccReport.attributeValue("version")) : 1.0D));
      report.setReportStyle(dhccReport.attributeValue("reportStyle"));
      report.setReportMemo(dhccReport.attributeValue("reportStyle"));
      report.setReportDescription(dhccReport.attributeValue("reportDescription"));
      report.setReportVersion(dhccReport.attributeValue("reportVersion"));
      report.setMainUuid(dhccReport.attributeValue("mainUuid"));
      if (dhccReport.attribute("iscustom") != null)
        report.setIscustom(dhccReport.attributeValue("iscustom"));
      else {
        report.setIscustom("0");
      }
      Element eledataSets = dhccReport.element("datasets");
      List dataSets = new ArrayList();
      List dslist = eledataSets.elements("dataset");
      for (Iterator itr = dslist.iterator(); itr.hasNext(); ) {
        Element elmr = (Element)itr.next();
        DataSetEntity ds = new DataSetEntity();
        ds.setName(elmr.attributeValue("name"));
        ds.setIsdic(Boolean.valueOf(elmr.attributeValue("isdic").equals("true")));
        Element ele = elmr.element("query");
        ds.setDatasourcename(ele.elementText("datasourcename"));
        ds.setCommandtext(ele.elementText("commandtext"));
        List parms = new ArrayList();
        if (ele.element("parameters") != null) {
          List parmList = ele.element("parameters").elements("parameter");
          for (Iterator itrp = parmList.iterator(); itrp.hasNext(); ) {
            Element elep = (Element)itrp.next();
            parms.add(elep.attributeValue("name"));
          }
        }
        ds.setParameters(parms);
        List filedslist = elmr.element("fields").elements("field");
        List fileds = new ArrayList();
        for (Iterator itrf = filedslist.iterator(); itrf.hasNext(); ) {
          Element elef = (Element)itrf.next();
          FieldEntity filed = new FieldEntity();
          filed.setName(elef.attributeValue("name"));
          if (elef.attributeValue("dicType") != null) {
            filed.setDicType(elef.attributeValue("dicType"));
            if (elef.attributeValue("dicType").equals("type"))
              ds.setKeyName(elef.attributeValue("name"));
            else if (elef.attributeValue("dicType").equals("code"))
              ds.setOptCode(elef.attributeValue("name"));
            else if (elef.attributeValue("dicType").equals("text")) {
              ds.setOptName(elef.attributeValue("name"));
            }
          }
          filed.setDataField(elef.attributeValue("name"));
          filed.setDatatype(elef.elementText("datatype"));
          fileds.add(filed);
        }
        ds.setFileds(fileds);
        dataSets.add(ds);
      }
      report.setDataSets(dataSets);
      Element body = dhccReport.element("body");

      List nodesrow = body.elements("row");
      for (Iterator itr = nodesrow.iterator(); itr.hasNext(); ) {
        Element elmr = (Element)itr.next();
        List nodescol = elmr.elements("col");
        String col = "";
        for (Iterator itc = nodescol.iterator(); itc.hasNext(); ) {
          Element elmc = (Element)itc.next();
          if (elmc.attributeValue("base64") == null) {
            if ((elmc.attributeValue("image") != null) && (elmc.getText().length() == 0))
              col = col + " ";
            else
              col = col + elmc.getText();
          }
          else {
            col = col + " ";
          }
        }
        if (col.length() == 0) {
          Element parent = elmr.getParent();
          parent.remove(elmr);
        }
      }
      List rowslist = new ArrayList();
      List rows = body.elements("row");
      for (Iterator itr = rows.iterator(); itr.hasNext(); ) {
        Element elmr = (Element)itr.next();
        List cols = elmr.elements("col");
        RowEntity row = new RowEntity();
        List colslist = new ArrayList();
        row.setCategory(elmr.attributeValue("category"));
        for (Iterator itc = cols.iterator(); itc.hasNext(); ) {
          Element elmc = (Element)itc.next();
          ColEntity col = new ColEntity();
          col.setBackground_color(elmc.attributeValue("background-color"));
          col.setBorder(elmc.attributeValue("border"));
          col.setDic(elmc.attributeValue("dic"));
          col.setExpText(elmc.attributeValue("expText"));
          col.setFont_color(elmc.attributeValue("font-color"));
          col.setFont_family(elmc.attributeValue("font-family"));
          col.setFont_size(elmc.attributeValue("font-size"));
          col.setFont_style(elmc.attributeValue("font-style"));
          col.setFont_weight(elmc.attributeValue("font-weight"));
          col.setFrameid(elmc.attributeValue("frameid"));
          col.setHeight(elmc.attributeValue("height"));
          col.setLink(elmc.attributeValue("link"));
          col.setText_align(elmc.attributeValue("text-align"));
          col.setText_decoration(elmc.attributeValue("text-decoration"));
          col.setValue(elmc.getText());
          col.setVertical_align(elmc.attributeValue("vertical-align"));
          col.setWhite_space(elmc.attributeValue("white-space"));
          col.setWidth(elmc.attributeValue("width"));
          if (elmc.attributeValue("colwidth") != null)
            col.setColwidth(elmc.attributeValue("colwidth"));
          else {
            col.setColwidth(elmc.attributeValue("width"));
          }
          if (elmc.attributeValue("rowspan") != null)
            col.setRowspan(Integer.valueOf(Integer.parseInt(elmc.attributeValue("rowspan"))));
          else {
            col.setRowspan(Integer.valueOf(1));
          }
          if (elmc.attributeValue("colspan") != null)
            col.setColspan(Integer.valueOf(Integer.parseInt(elmc.attributeValue("colspan"))));
          else {
            col.setColspan(Integer.valueOf(1));
          }
          if ((elmc.attributeValue("base64") != null) && (elmc.attributeValue("base64nodes") != null)) {
            col.setBase64(elmc.attributeValue("base64"));
            col.setBase64nodes(elmc.attributeValue("base64nodes"));
          }
          if ((elmc.attribute("image") != null) && (elmc.attributeValue("image") != null)) {
            col.setImages(elmc.attributeValue("image"));
          }
          if ((elmc.attribute("bgcolors") != null) && (elmc.attributeValue("bgcolors") != null)) {
            col.setBgcolors(elmc.attributeValue("bgcolors"));
          }
          colslist.add(col);
        }
        row.setCols(colslist);
        rowslist.add(row);
      }
      report.setRows(rowslist);
      if (body.attributeValue("pageorder") != null)
        report.setBodyPageorder(Integer.valueOf(Integer.parseInt(body.attributeValue("pageorder"))));
      else {
        report.setBodyPageorder(Integer.valueOf(1));
      }
      if (body.attributeValue("pagesize") != null)
        report.setBodyPagesize(Integer.valueOf(Integer.parseInt(body.attributeValue("pagesize"))));
      else {
        report.setBodyPagesize(Integer.valueOf(1));
      }
      if ((body.attributeValue("maxR") == null) || (body.attributeValue("maxC") == null)) {
        report.setMaxC(Integer.valueOf(0));
        report.setMaxR(Integer.valueOf(0));
      } else {
        report.setMaxC(Integer.valueOf(Integer.parseInt(body.attributeValue("maxC"))));
        report.setMaxR(Integer.valueOf(Integer.parseInt(body.attributeValue("maxR"))));
      }
      if (body.attributeValue("toolbar") != null)
        report.setToolbar(body.attributeValue("toolbar"));
      else {
        report.setToolbar("top");
      }

      Element eleparmsList = dhccReport.element("parmsList");
      List parmsList = new ArrayList();
      if (eleparmsList != null) {
        List parmList = eleparmsList.elements("parm");
        for (Iterator itr = parmList.iterator(); itr.hasNext(); ) {
          Element elmr = (Element)itr.next();
          ParmEntity parm = new ParmEntity();
          parm.setCname(elmr.attributeValue("cname"));
          parm.setDbtype(elmr.attributeValue("dbtype"));
          parm.setIsnull(elmr.attributeValue("isnull"));
          parm.setName(elmr.attributeValue("name"));
          parm.setShowtype(elmr.attributeValue("showtype"));
          parm.setType(elmr.attributeValue("type"));
          parm.setVl(elmr.attributeValue("vl"));
          parmsList.add(parm);
        }
        report.setParmsList(parmsList);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return report;
  }

  public static Map<String, Object> openXML(ReportEntity report) {
    Map map = new HashMap();
    Element dhccReport = reportToElement(report);
    String uuid = report.getUuid();
    String reportStyle = report.getReportStyle();
    String reportTitle = report.getReportDescription();
    String reportVersion = report.getReportVersion();
    String reportMemo = report.getReportMemo();
    String mainUuid = report.getMainUuid();
    Element dataSets = dhccReport.element("datasets");
    Element fillreports = dhccReport.element("fillreports");
    Element body = dhccReport.element("body");

    List nodesrow = body.elements("row");
    for (Iterator itr = nodesrow.iterator(); itr.hasNext(); ) {
      Element elmr = (Element)itr.next();
      List nodescol = elmr.elements("col");
      String col = "";
      for (Iterator itc = nodescol.iterator(); itc.hasNext(); ) {
        Element elmc = (Element)itc.next();
        if (elmc.attributeValue("base64") == null) {
          if ((elmc.attributeValue("image") != null) && (elmc.getText().length() == 0))
            col = col + " ";
          else
            col = col + elmc.getText();
        }
        else {
          col = col + " ";
        }
      }
      if (col.length() == 0) {
        Element parent = elmr.getParent();
        parent.remove(elmr);
      }

    }

    Element expression = dhccReport.element("expression");
    Element parmsList = dhccReport.element("parmsList");
    Element parmsExtList = dhccReport.element("parmsExtList");
    XMLbean lbean = new XMLbean();
    lbean.setUuid(uuid);

    if (body.attributeValue("pagesize") == null)
      lbean.setPagesize("10");
    else {
      lbean.setPagesize(body.attributeValue("pagesize"));
    }

    if (body.attributeValue("pageorder") == null)
      lbean.setPageorder("1");
    else {
      lbean.setPageorder(body.attributeValue("pageorder"));
    }
    if (body.attributeValue("toolbar") != null)
      lbean.setToolbar(body.attributeValue("toolbar"));
    else {
      lbean.setToolbar("top");
    }
    lbean.setType(reportStyle);
    lbean.setTitle(reportTitle);
    lbean.setReportVersion(reportVersion);
    lbean.setReportMemo(reportMemo);
    lbean.setMainUuid(mainUuid);
    if (dhccReport.attribute("iscustom") != null)
      lbean.setIscustom(dhccReport.attributeValue("iscustom"));
    else {
      lbean.setIscustom("0");
    }
    lbean.setBody(body.asXML().replaceAll("\n", "").replaceAll("\t", ""));
    lbean.setDataSets(dataSets.asXML().replaceAll("\n", "").replaceAll("\t", ""));
    if (fillreports != null)
      lbean.setFillreports(fillreports.asXML().replaceAll("\n", "").replaceAll("\t", ""));
    if (expression != null)
      lbean.setExpression(expression.getText());
    if (parmsList != null)
      lbean.setParmslist(parmsList.asXML());
    if (parmsExtList != null)
      lbean.setParmsExtlist(parmsExtList.asXML());
    else {
      lbean.setParmsExtlist("<parmsExtList/>");
    }
    map.put("xml", lbean);

    return map;
  }

  public static Map<String, Object> openXML(String filePath)
  {
    Map map = new HashMap();
    File f = new File(filePath);
    if ((!f.exists()) || (!f.isFile())) {
      return null;
    }
    Element dhccReport = getElementAsText(filePath, "dhccReport");
    String uuid = dhccReport.attributeValue("uuid");
    String reportStyle = dhccReport.attributeValue("reportStyle");
    String reportTitle = dhccReport.attributeValue("reportDescription");
    String reportVersion = dhccReport.attributeValue("reportVersion");
    String reportMemo = dhccReport.attributeValue("reportMemo");
    String mainUuid = dhccReport.attributeValue("mainUuid");
    Element dataSets = getElementAsText(filePath, "datasets");
    Element fillreports = getElementAsText(filePath, "fillreports");
    Element body = getElementAsText(filePath, "body");

    List nodesrow = body.elements("row");
    for (Iterator itr = nodesrow.iterator(); itr.hasNext(); ) {
      Element elmr = (Element)itr.next();
      List nodescol = elmr.elements("col");
      String col = "";
      for (Iterator itc = nodescol.iterator(); itc.hasNext(); ) {
        Element elmc = (Element)itc.next();
        if (elmc.attributeValue("base64") == null) {
          if ((elmc.attributeValue("image") != null) && (elmc.getText().length() == 0))
            col = col + " ";
          else
            col = col + elmc.getText();
        }
        else {
          col = col + " ";
        }
      }
      if (col.length() == 0) {
        Element parent = elmr.getParent();
        parent.remove(elmr);
      }

    }

    Element expression = getElementAsText(filePath, "expression");
    Element parmsList = getElementAsText(filePath, "parmsList");
    Element parmsExtList = getElementAsText(filePath, "parmsExtList");
    XMLbean lbean = new XMLbean();
    lbean.setUuid(uuid);

    if (body.attributeValue("pagesize") == null)
      lbean.setPagesize("10");
    else {
      lbean.setPagesize(body.attributeValue("pagesize"));
    }

    if (body.attributeValue("pageorder") == null)
      lbean.setPageorder("1");
    else {
      lbean.setPageorder(body.attributeValue("pageorder"));
    }
    if (body.attributeValue("toolbar") == null)
      lbean.setToolbar("top");
    else {
      lbean.setToolbar(body.attributeValue("toolbar"));
    }
    lbean.setType(reportStyle);
    lbean.setTitle(reportTitle);
    lbean.setReportVersion(reportVersion);
    lbean.setReportMemo(reportMemo);
    lbean.setMainUuid(mainUuid);
    if (dhccReport.attribute("iscustom") != null)
      lbean.setIscustom(dhccReport.attributeValue("iscustom"));
    else {
      lbean.setIscustom("0");
    }
    lbean.setBody(body.asXML().replaceAll("\n", "").replaceAll("\t", ""));
    lbean.setDataSets(dataSets.asXML().replaceAll("\n", "").replaceAll("\t", ""));
    if (fillreports != null)
      lbean.setFillreports(fillreports.asXML().replaceAll("\n", "").replaceAll("\t", ""));
    if (expression != null)
      lbean.setExpression(expression.getText());
    if (parmsList != null)
      lbean.setParmslist(parmsList.asXML());
    if (parmsExtList != null)
      lbean.setParmsExtlist(parmsExtList.asXML());
    else {
      lbean.setParmsExtlist("<parmsExtList/>");
    }
    map.put("xml", lbean);
    xmlMap.put(uuid, lbean);
    return map;
  }

  public static Map<String, Object> getParmsByUUID(String filePath) {
    File f = new File(filePath);
    if ((!f.exists()) || (!f.isFile())) {
      return null;
    }
    Map map = null;
    Element parmsList = getElementAsText(filePath, "parmsList");
    if (parmsList != null) {
      map = new PreviewService().compileParms(parmsList.asXML());
    }
    return map;
  }

  public static boolean updateXML(String dataSetsModel, String tableData, String expressionData, String parmsModel, String parmsExtModel, String fieldModel, String filePath)
  {
    try
    {
      SAXReader reader = new SAXReader();
      Document document = reader.read(new File(filePath));
      Element root = document.getRootElement();
      List<Node> dataSetsList = root.selectNodes("datasets");
      for (Node element : dataSetsList) {
        root.remove(element);
      }
      Document dataSetsdoc = DocumentHelper.parseText(dataSetsModel);
      root.add(dataSetsdoc.getRootElement());

      List<Node> parmsModels = root.selectNodes("parmsList");
      for (Node element : parmsModels)
        root.remove(element);
      Element element;
      if (!parmsModel.equals("")) {
        Document parmsModelsdoc = DocumentHelper.parseText(parmsModel);
        root.add(parmsModelsdoc.getRootElement());
        List rootList = root.selectNodes("body");
        for (Iterator localIterator3 = rootList.iterator(); localIterator3.hasNext(); ) 
        {
          element = (Element)localIterator3.next();
          root.remove(element);
        }
      }
      List<Node> parmsExtModels = root.selectNodes("parmsExtList");
      for (Node node : parmsExtModels) {
        root.remove(node);
      }
      if ((parmsExtModel != null) && (!parmsExtModel.equals(""))) {
        Document parmsExtModelsdoc = DocumentHelper.parseText(parmsExtModel);
        if (parmsExtModelsdoc != null) {
          root.add(parmsExtModelsdoc.getRootElement());
          List rootList = root.selectNodes("body");
          for (Iterator localIterator4 = rootList.iterator(); localIterator4.hasNext(); ) { element = (Element)localIterator4.next();
            root.remove((Element)element);
          }
        }
      }
      if ((expressionData != null) && (!expressionData.equals("")) && (root.selectNodes("expression").size() > 0)) {
        ((Element)root.selectNodes("expression").get(0)).setText(expressionData);
      }
      List fieldModelList = root.selectNodes("fillreports");
      for (Object element1 = fieldModelList.iterator(); ((Iterator)element1).hasNext(); ) 
      { 
    	  Element element2 = (Element)((Iterator)element1).next();
          root.remove(element2);
      }

      if ((!"".equals(fieldModel)) && (fieldModel != null))
      {
        Document fieldModels = DocumentHelper.parseText(fieldModel);
        root.add(fieldModels.getRootElement());
      }
      else {
        root.addElement("fillreports");
      }

      Document doc = DocumentHelper.parseText(tableData);
      root.add(doc.getRootElement());
      String xml = root.getDocument().asXML();
      File f = new File(filePath);
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
      pw.write(xml);
      pw.close();
      String uuid = "";
      try {
        uuid = filePath.substring(filePath.length() - 36).substring(0, 32);
        xmlMap.remove(uuid);
      } catch (Exception localException1) {
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  public static Map<String, String> getDataSets(String filePath)
  {
    Element root = getElementAsText(filePath, "datasets");
    listNodes(root);
    return null;
  }

  public static Element getElementAsText(String filePath, String elementName)
  {
	  List rootList = new ArrayList();
    try
    {
     
      SAXReader reader = new SAXReader();

      Document document = reader.read(new File(filePath));
      Element root = document.getRootElement();
      if (elementName.equals("dhccReport"))
        return root;
      
      if (root.element(elementName) != null)
        rootList = root.selectNodes(elementName);
      else
        rootList = null;
    }
    catch (DocumentException e)
    {
     // List rootList;
      e.printStackTrace();
      System.out.println(e.getMessage());
      return null;
    }
   // List rootList;
    if ((rootList == null) || (rootList.size() == 0))
      return null;
    return (Element)rootList.get(0);
  }

  public static void listNodes(Element node)
  {
    System.out.println("当前节点的名称：" + node.getName());

    List<Attribute> list = node.attributes();

    for (Attribute attribute : list) {
      System.out.println("属性" + attribute.getName() + ":" + attribute.getValue());
    }

    if (!node.getTextTrim().equals("")) {
      System.out.println(node.getName() + "：" + node.getText());
    }

    Iterator iterator = node.elementIterator();
    while (iterator.hasNext()) {
      Element e = (Element)iterator.next();
      listNodes(e);
    }
  }
}

/* 
 * Qualified Name:     report.java.design.util.DesignXmlUtil
*
 */