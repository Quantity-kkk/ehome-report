package report.java.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import report.java.entity.json.BgColor;
import report.java.entity.json.Col;
import report.java.entity.json.DataParms;
import report.java.entity.json.DataSets;
import report.java.entity.json.Field;
import report.java.entity.json.Image;
import report.java.entity.json.JsonReportEntity;
import report.java.entity.json.MergeCells;
import report.java.entity.json.Nodes;
import report.java.entity.json.Overlapping;
import report.java.entity.json.Parms;
import report.java.entity.json.Row;
import report.java.entity.json.Style;
import report.java.entity.xml.ColEntity;
import report.java.entity.xml.DataSetEntity;
import report.java.entity.xml.FieldEntity;
import report.java.entity.xml.ParmEntity;
import report.java.entity.xml.ReportEntity;
import report.java.entity.xml.RowEntity;

public class JsonToXMLUtil
{
  public static ReportEntity JsonToXml(JsonReportEntity jsonRep)
  {
    ReportEntity xmlRep = new ReportEntity();
    if (jsonRep != null) {
      try {
        xmlRep.setUuid(jsonRep.getUuid());
        xmlRep.setReportStyle(jsonRep.getReportStyle());

        xmlRep.setReportDescription(jsonRep.getReportDescription());
        xmlRep.setReportVersion(jsonRep.getReportVersion());
        xmlRep.setReportMemo(jsonRep.getReportMemo());
        xmlRep.setMainUuid(jsonRep.getMainUuid());

        xmlRep.setBodyPageorder(Integer.valueOf(jsonRep.getPageorder()));

        xmlRep.setBodyPagesize(Integer.valueOf(jsonRep.getPage()));
        xmlRep.setMaxR(Integer.valueOf(jsonRep.getMaxR()));
        xmlRep.setMaxC(Integer.valueOf(jsonRep.getMaxC()));
        xmlRep.setToolbar(jsonRep.getToolbar());

        List repDateSetList = new ArrayList();
        DataSetEntity dataSetEntity = null;
        List<DataSets> dataSets = jsonRep.getDataSets();
        FieldEntity fieldEntity;
        List parameters;
        for (DataSets dataSet : dataSets) {
          dataSetEntity = new DataSetEntity();
          dataSetEntity.setCommandtext(dataSet.getCommandText());
          dataSetEntity.setDatasourcename(dataSet.getDataSourceName());
          dataSetEntity.setIsdic(Boolean.valueOf(dataSet.getDic()));
          dataSetEntity.setName(dataSet.getDataSetName());
          dataSetEntity.setKeyName(dataSet.getKeyName());
          dataSetEntity.setOptCode(dataSet.getOptCode());
          dataSetEntity.setOptName(dataSet.getOptName());
          List fields = new ArrayList();
          for (Field field : dataSet.getField()) {
            fieldEntity = new FieldEntity();
            fieldEntity.setDataField(field.getColumnName());
            fieldEntity.setDatatype(field.getColumnType());
            if (dataSet.getKeyName() != null) {
              if (dataSet.getKeyName().equals(field.getColumnName()))
                fieldEntity.setDicType("type");
              else if (dataSet.getOptCode().equals(field.getColumnName()))
                fieldEntity.setDicType("code");
              else if (dataSet.getOptName().equals(field.getColumnName())) {
                fieldEntity.setDicType("text");
              }
            }
            fieldEntity.setName(field.getColumnName());
            fields.add(fieldEntity);
          }
          dataSetEntity.setFileds(fields);
          parameters = new ArrayList();
          for (Parms param : dataSet.getParms()) {
            parameters.add(param.getParmName());
          }
          dataSetEntity.setParameters(parameters);
          repDateSetList.add(dataSetEntity);
        }
        xmlRep.setDataSets(repDateSetList);

        List parmsList = new ArrayList();
        ParmEntity parmEntity = null;
        for (DataParms dp : jsonRep.getDataParms()) {
          parmEntity = new ParmEntity();
          parmEntity.setCname(dp.getParmCName());
          parmEntity.setDbtype(dp.getDbType());
          parmEntity.setIsnull("1");
          parmEntity.setName(dp.getParmName());
          parmEntity.setShowtype(dp.getShowType());
          parmEntity.setType(dp.getParmType());
          parmEntity.setVl(dp.getParmvl());
          parmsList.add(parmEntity);
        }
        xmlRep.setParmsList(parmsList);

        List rows = new ArrayList();
        RowEntity row = null;
        for (Row rw : jsonRep.getRow()) {
          row = new RowEntity();
          row.setCategory(rw.getRowCategory());
          List cols = new ArrayList();
          ColEntity col = null;
          for (Col cl : rw.getCol()) {
            col = new ColEntity();
            col.setBackground_color(cl.getStyle().getBackground_color());

            col.setColspan(Integer.valueOf(cl.getColspan()));
            col.setDic(cl.getDic());
            col.setExpText(cl.getExptext());
            col.setFont_color(cl.getStyle().getColor());
            col.setFont_family(cl.getStyle().getFont_family());
            col.setFont_size(cl.getStyle().getFont_size());
            col.setFont_style(cl.getStyle().getFont_style());
            col.setFont_weight(cl.getStyle().getFont_weight());
            col.setFrameid(cl.getFrameid());
            col.setHeight(cl.getHeight() + "px");
            col.setLink(cl.getLink());
            col.setRowspan(Integer.valueOf(cl.getRowspan()));
            col.setText_align(cl.getStyle().getText_align());
            col.setWhite_space(cl.getStyle().getWhite_space());
            StringBuilder border = new StringBuilder();
            border.append("{");
            if (cl.getStyle().getBorder_left() != null)
              border.append("leftColor:\"rgb(0,0,0)\",");
            else {
              border.append("leftColor:\"rgb(204,204,204)\",");
            }
            if (cl.getStyle().getBorder_top() != null)
              border.append("topColor:\"rgb(0,0,0)\",");
            else {
              border.append("topColor:\"rgb(204,204,204)\",");
            }
            if (cl.getStyle().getBorder_right() != null)
              border.append("rightColor:\"rgb(0,0,0)\",");
            else {
              border.append("rightColor:\"rgb(204,204,204)\",");
            }
            if (cl.getStyle().getBorder_bottom() != null)
              border.append("bottomColor:\"rgb(0,0,0)\"");
            else {
              border.append("bottomColor:\"rgb(204,204,204)\"");
            }
            border.append("}");
            col.setBorder(border.toString());
            List listtxtdList = cl.getStyle().getText_decoration();
            if (listtxtdList != null) {
              if (listtxtdList.indexOf("strike") != -1) {
                col.setText_decoration("line-through solid rgb(0, 0, 0)");
              }
              if (listtxtdList.indexOf("underline") != -1)
                col.setText_decoration("underline solid rgb(0, 0, 0)");
            }
            else {
              col.setText_decoration("none solid rgb(0, 0, 0)");
            }
            col.setVertical_align(cl.getStyle().getVertical_align());
            col.setValue(cl.getValue());
            col.setWidth(cl.getWidth() + "px");
            col.setColwidth(cl.getColwidth() + "px");
            if ((cl.getOverlapping() != null) && (cl.getOverlapping().getBase64() != null) && (cl.getOverlapping().getNodes() != null)) {
              col.setBase64(cl.getOverlapping().getBase64());
              col.setBase64nodes(JSONObject.toJSON(cl.getOverlapping().getNodes()).toString().replace("\"", "&quot;"));
            }
            if ((cl.getImage() != null) && (cl.getImage().size() > 0)) {
              col.setImages(JSON.toJSONString(cl.getImage()));
            }
            if ((cl.getBgColors() != null) && (cl.getBgColors().size() > 0)) {
              col.setBgcolors(JSON.toJSONString(cl.getBgColors()));
            }
            cols.add(col);
          }
          row.setCols(cols);
          rows.add(row);
        }
        xmlRep.setRows(rows);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return xmlRep;
  }

  public static JsonReportEntity XmlToJson(ReportEntity xmlRep)
  {
    JsonReportEntity jsonRep = new JsonReportEntity();
    if (xmlRep != null) {
      try {
        jsonRep.setUuid(xmlRep.getUuid());
        jsonRep.setReportStyle(xmlRep.getReportStyle());

        jsonRep.setReportDescription(xmlRep.getReportDescription());
        jsonRep.setReportVersion(xmlRep.getReportVersion());
        jsonRep.setReportMemo(xmlRep.getReportMemo());
        jsonRep.setMainUuid(xmlRep.getMainUuid());

        jsonRep.setPageorder(xmlRep.getBodyPageorder().intValue());

        jsonRep.setPage(xmlRep.getBodyPagesize().intValue());
        jsonRep.setMaxR(xmlRep.getMaxR().intValue());
        jsonRep.setMaxC(xmlRep.getMaxC().intValue());
        jsonRep.setToolbar(xmlRep.getToolbar());

        List repDateSetList = new ArrayList();
        DataSets dataSetEntity = null;
        List parameters;
        for (DataSetEntity dataSet : xmlRep.getDataSets()) {
          dataSetEntity = new DataSets();
          dataSetEntity.setCommandText(dataSet.getCommandtext());
          dataSetEntity.setDataSourceName(dataSet.getDatasourcename());
          dataSetEntity.setDic(dataSet.getIsdic().booleanValue());
          dataSetEntity.setDataSetName(dataSet.getName());
          dataSetEntity.setKeyName(dataSet.getKeyName());
          dataSetEntity.setOptCode(dataSet.getOptCode());
          dataSetEntity.setOptName(dataSet.getOptName());
          List fields = new ArrayList();
          for (FieldEntity field : dataSet.getFileds()) {
            Field fieldEntity = new Field();
            fieldEntity.setColumnName(field.getName());
            fieldEntity.setColumnType(field.getDatatype());
            fieldEntity.setColumnComments(field.getName());
            fields.add(fieldEntity);
          }
          dataSetEntity.setField(fields);
          parameters = new ArrayList();
          Parms parameter = null;
          for (String param : dataSet.getParameters()) {
            parameter = new Parms();
            parameter.setParmName(param);
            parameters.add(parameter);
          }
          dataSetEntity.setParms(parameters);
          repDateSetList.add(dataSetEntity);
        }
        jsonRep.setDataSets(repDateSetList);

        List parmsList = new ArrayList();
        DataParms parmEntity = null;
        for (ParmEntity dp : xmlRep.getParmsList()) {
          parmEntity = new DataParms();
          parmEntity.setParmCName(dp.getCname());
          parmEntity.setDbType(dp.getDbtype());
          parmEntity.setParmName(dp.getName());
          parmEntity.setShowType(dp.getShowtype());
          parmEntity.setParmType(dp.getType());
          parmEntity.setParmvl(dp.getVl());
          parmsList.add(parmEntity);
        }
        jsonRep.setDataParms(parmsList);

        List rows = new ArrayList();
        Row row = null;
        Object mergeCells = new ArrayList();
        MergeCells mergec = null;
        Object colWidths = new ArrayList();
        List rowHeights = new ArrayList();
        int maxR = xmlRep.getRows().size() - 1;
        int maxC = 0;
        int rowindex = 0;
        for (RowEntity rw : xmlRep.getRows()) {
          row = new Row();
          row.setRowCategory(rw.getCategory());
          List cols = new ArrayList();
          Col col = null;
          int colindex = 0;
          if (rw.getCols().size() - 1 > maxC) {
            maxC = rw.getCols().size() - 1;
            colWidths = new ArrayList();
          }
          for (ColEntity cl : rw.getCols()) {
            col = new Col();
            if (colindex == 0) {
              rowHeights.add(Integer.valueOf(new Double(Double.parseDouble(cl.getHeight().replace("px", ""))).intValue()));
            }
            ((List)colWidths).add(Integer.valueOf(new Double(Double.parseDouble(cl.getWidth().replace("px", ""))).intValue()));
            Style style = new Style();
            style.setBackground_color(cl.getBackground_color());
            style.setColor(cl.getFont_color());
            style.setFont_family(cl.getFont_family());
            style.setFont_size(cl.getFont_size());
            style.setFont_style(cl.getFont_style());
            style.setFont_weight(cl.getFont_weight());
            style.setText_align(cl.getText_align());
            style.setWhite_space(cl.getWhite_space());

            String border = cl.getBorder().replace(", ", ",");
            String[] arrbr = border.split("\",");
            for (String br : arrbr) {
              if (br.indexOf("rgb(0,0,0") != -1) {
                if (br.indexOf("leftColor") != -1)
                  style.setBorder_left("1px solid rgb(0,0,0)");
                else if (br.indexOf("topColor") != -1)
                  style.setBorder_top("1px solid rgb(0,0,0)");
                else if (br.indexOf("rightColor") != -1)
                  style.setBorder_right("1px solid rgb(0,0,0)");
                else if (br.indexOf("bottomColor") != -1) {
                  style.setBorder_bottom("1px solid rgb(0,0,0)");
                }
              }
            }

            String txtd = cl.getText_decoration();
            Object listtxtd = new ArrayList();
            if ((txtd != null) && 
              (txtd.length() > 0) && (!txtd.equals("none"))) {
              if (txtd.indexOf("line-through") != -1) {
                ((List)listtxtd).add("strike");
              }
              if (txtd.indexOf("underline") != -1) {
                ((List)listtxtd).add("underline");
              }
            }

            style.setText_decoration((List)listtxtd);
            style.setVertical_align(cl.getVertical_align());
            col.setStyle(style);
            col.setColspan(cl.getColspan().intValue());
            col.setDic(cl.getDic());
            col.setExptext(cl.getExpText());
            col.setFrameid(cl.getFrameid());
            col.setHeight(new Double(Double.parseDouble(cl.getHeight().replace("px", ""))).intValue());
            col.setLink(cl.getLink());
            col.setRowspan(cl.getRowspan().intValue());
            col.setValue(cl.getValue());
            col.setWidth(new Double(Double.parseDouble(cl.getWidth().replace("px", ""))).intValue());
            col.setColwidth(new Double(Double.parseDouble(cl.getColwidth().replace("px", ""))).intValue());
            if ((cl.getRowspan() != null) || (cl.getColspan() != null)) {
              mergec = new MergeCells();
              mergec.setCol(colindex);
              mergec.setRow(rowindex);
              mergec.setColspan(cl.getColspan() != null ? cl.getColspan().intValue() : 1);
              mergec.setRowspan(cl.getRowspan() != null ? cl.getRowspan().intValue() : 1);
              ((List)mergeCells).add(mergec);
            }
            if ((cl.getBase64() != null) && (cl.getBase64nodes() != null) && (cl.getBase64().length() > 0) && (cl.getBase64nodes().length() > 0)) {
              Overlapping ol = new Overlapping();
              ol.setBase64(cl.getBase64());
              List nodes = JSONObject.parseArray(cl.getBase64nodes().replace("&quot;", "\""), Nodes.class);
              ol.setNodes(nodes);
              col.setOverlapping(ol);
            }
            if ((cl.getImages() != null) && (cl.getImages().length() > 0))
              try {
                List list = JSON.parseArray(cl.getImages(), Image.class);
                col.setImage(list);
              }
              catch (Exception localException3) {
              }
            if ((cl.getBgcolors() != null) && (cl.getBgcolors().length() > 0))
              try {
                List list = JSON.parseArray(cl.getBgcolors(), BgColor.class);
                col.setBgColors(list);
              }
              catch (Exception localException4) {
              }
            cols.add(col);
            colindex++;
          }
          row.setCol(cols);
          rows.add(row);
          rowindex++;
        }
        jsonRep.setMergeCells((List)mergeCells);
        jsonRep.setColWidths((List)colWidths);
        jsonRep.setRowHeights(rowHeights);
        jsonRep.setRow(rows);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return (JsonReportEntity)(JsonReportEntity)(JsonReportEntity)jsonRep;
  }
}

/* 
 * Qualified Name:     report.java.entity.JsonToXMLUtil
*
 */