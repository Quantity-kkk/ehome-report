package report.java.design.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.AUTOMATIC;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import report.java.entity.json.Col;
import report.java.entity.json.JsonReportEntity;
import report.java.entity.json.MergeCells;
import report.java.entity.json.Row;
import report.java.entity.json.Style;

public class ImportExcelAction
{
  private File filedata;
  private String filedataFileName;
  private String filedataContentType;
  private JSONArray dataArray;
  private Map<String, Object> dataMap;

  public String importExcel()
    throws Exception
  {
    InputStream intputstream = null;
    this.dataMap = new HashMap();
    try {
      intputstream = new FileInputStream(this.filedata);
      JsonReportEntity json = null;
      String extensionName = getExtensionName(this.filedataFileName);
      if (extensionName.equals("xlsx"))
        json = Excel07(intputstream);
      else if (extensionName.equals("xls")) {
        json = Excel03(intputstream);
      }
      this.dataMap.put("flag", "success");
      this.dataMap.put("data", json);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      this.dataMap.put("flag", "error");
    }
    return "success";
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

  private JsonReportEntity Excel03(InputStream intputstream)
  {
    JsonReportEntity json = null;
    HSSFWorkbook wb = null;
    try {
      wb = new HSSFWorkbook(intputstream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (wb != null) {
      json = new JsonReportEntity();
      HSSFSheet sheet = wb.getSheetAt(0);
      json.setPage(10);
      json.setPageorder(1);
      json.setToolbar("top");
      json.setReportStyle("D");

      int rowNum = sheet.getLastRowNum();
      json.setMaxR(rowNum);
      int columnNum = sheet.getRow(0).getPhysicalNumberOfCells();
      Map[] map = getRowSpanColSpanMap(sheet);
      json.setMaxC(columnNum);
      List mergeCells = new ArrayList();
      MergeCells mergec = null;
      List colWidths = new ArrayList();
      List rowHeights = new ArrayList();
      List rows = new ArrayList();
      HSSFCell pcell = null;
      Row row = null;
      Col col = null;
      List listcol = null;
      Style style = null;

      for (int i = 0; i <= rowNum; i++) {
        row = new Row();
        listcol = new ArrayList();
        row.setRowCategory("headtitle");
        float rowHeight = 0.0F;
        rowHeight = sheet.getRow(i).getHeightInPoints();

        int rHeight = (int)(rowHeight / 72.0F * 96.0F);
        rowHeights.add(Integer.valueOf(rHeight));

        for (int j = 0; j < columnNum; j++) {
          col = new Col();
          style = new Style();
          col.setCol(j);
          col.setRow(i);
          col.setDic("");
          col.setDs("");
          col.setExptext("");
          col.setFrameid("");
          col.setLink("");
          pcell = sheet.getRow(i).getCell(j);
          if (pcell == null) {
            col.setColspan(1);
            col.setColwidth(100);
            colWidths.add(Integer.valueOf(100));
            col.setHeight(23);
            col.setRowspan(1);
            col.setStyle(style);
            col.setValue("");
            col.setWidth(100);
          }
          else {
            if ((map[0].get(i + "," + j) != null) && (!"".equals(map[0].get(i + "," + j)))) {
              String[] mergeValue = ((String)map[0].get(i + "," + j)).split(",");
              col.setRowspan(Integer.parseInt(mergeValue[0]) + 1);
              col.setColspan(Integer.parseInt(mergeValue[1]) + 1);
              mergec = new MergeCells();
              mergec.setCol(j);
              mergec.setRow(i);
              mergec.setColspan(Integer.parseInt(mergeValue[1]) + 1);
              mergec.setRowspan(Integer.parseInt(mergeValue[0]) + 1);
              mergeCells.add(mergec);
            } else {
              col.setRowspan(1);
              col.setColspan(1);
            }
            col.setWidth((int)sheet.getColumnWidthInPixels(pcell.getColumnIndex()));
            colWidths.add(Integer.valueOf((int)sheet.getColumnWidthInPixels(pcell.getColumnIndex())));
            col.setHeight(rHeight);
            col.setValue(getCellValue(pcell));
            style = getStyle(wb, pcell, style);
            col.setStyle(style);
            listcol.add(col);
          }
        }
        row.setCol(listcol);
        rows.add(row);
      }
      json.setColWidths(colWidths);
      json.setRowHeights(rowHeights);
      json.setMergeCells(mergeCells);
      json.setRow(rows);
    }
    return json;
  }

  private JsonReportEntity Excel07(InputStream intputstream)
  {
    JsonReportEntity json = null;
    XSSFWorkbook wb = null;
    try {
      wb = new XSSFWorkbook(intputstream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (wb != null) {
      json = new JsonReportEntity();
      XSSFSheet sheet = wb.getSheetAt(0);
      json.setPage(10);
      json.setPageorder(1);
      json.setToolbar("top");
      json.setReportStyle("D");

      int rowNum = sheet.getLastRowNum();
      json.setMaxR(rowNum);
      int columnNum = sheet.getRow(0).getPhysicalNumberOfCells();
      Map[] map = getRowSpanColSpanMap(sheet);
      json.setMaxC(columnNum);
      List mergeCells = new ArrayList();
      MergeCells mergec = null;
      List colWidths = new ArrayList();
      List rowHeights = new ArrayList();
      List rows = new ArrayList();
      XSSFCell pcell = null;
      Row row = null;
      Col col = null;
      List listcol = null;
      Style style = null;

      for (int i = 0; i <= rowNum; i++) {
        row = new Row();
        listcol = new ArrayList();
        row.setRowCategory("headtitle");
        float rowHeight = 0.0F;
        rowHeight = sheet.getRow(i).getHeightInPoints();

        int rHeight = (int)(rowHeight / 72.0F * 96.0F);
        rowHeights.add(Integer.valueOf(rHeight));

        for (int j = 0; j < columnNum; j++) {
          col = new Col();
          style = new Style();
          col.setCol(j);
          col.setRow(i);
          col.setDic("");
          col.setDs("");
          col.setExptext("");
          col.setFrameid("");
          col.setLink("");
          pcell = sheet.getRow(i).getCell(j);
          if (pcell == null) {
            colWidths.add(Integer.valueOf(100));
            col.setColspan(1);
            col.setColwidth(100);
            col.setHeight(23);
            col.setRowspan(1);
            col.setStyle(style);
            col.setValue("");
            col.setWidth(100);
          }
          else {
            if ((map[0].get(i + "," + j) != null) && (!"".equals(map[0].get(i + "," + j)))) {
              String[] mergeValue = ((String)map[0].get(i + "," + j)).split(",");
              col.setRowspan(Integer.parseInt(mergeValue[0]) + 1);
              col.setColspan(Integer.parseInt(mergeValue[1]) + 1);
              mergec = new MergeCells();
              mergec.setCol(j);
              mergec.setRow(i);
              mergec.setColspan(Integer.parseInt(mergeValue[1]) + 1);
              mergec.setRowspan(Integer.parseInt(mergeValue[0]) + 1);
              mergeCells.add(mergec);
            } else {
              col.setRowspan(1);
              col.setColspan(1);
            }
            col.setWidth((int)sheet.getColumnWidthInPixels(pcell.getColumnIndex()));
            colWidths.add(Integer.valueOf((int)sheet.getColumnWidthInPixels(pcell.getColumnIndex())));
            col.setHeight(rHeight);
            col.setValue(getCellValue(pcell));
            style = getStyle(wb, pcell, style);
            col.setStyle(style);
            listcol.add(col);
          }
        }
        row.setCol(listcol);
        rows.add(row);
      }
      json.setColWidths(colWidths);
      json.setRowHeights(rowHeights);
      json.setMergeCells(mergeCells);
      json.setRow(rows);
    }
    return json;
  }

  private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {
    Map map0 = new HashMap();
    Map map1 = new HashMap();
    int mergedNum = sheet.getNumMergedRegions();
    CellRangeAddress range = null;
    for (int i = 0; i < mergedNum; i++) {
      range = sheet.getMergedRegion(i);
      int topRow = range.getFirstRow();
      int topCol = range.getFirstColumn();
      int bottomRow = range.getLastRow();
      int bottomCol = range.getLastColumn();
      map0.put(topRow + "," + topCol, bottomRow - topRow + "," + (bottomCol - topCol));

      int tempRow = topRow;
      while (tempRow <= bottomRow) {
        int tempCol = topCol;
        while (tempCol <= bottomCol) {
          map1.put(tempRow + "," + tempCol, "");
          tempCol++;
        }
        tempRow++;
      }
      map1.remove(topRow + "," + topCol);
    }
    Map[] map = { map0, map1 };
    return map;
  }

  private Style getStyle(Workbook wb, Cell cell, Style style)
  {
    CellStyle cellStyle = cell.getCellStyle();
    if (cellStyle.getWrapText())
      style.setWhite_space("normal");
    else {
      style.setWhite_space("nowrap");
    }
    String vertical_align = convertVerticalAlignToHtml(cellStyle.getVerticalAlignment());
    style.setVertical_align(vertical_align);
    String text_align = convertAlignToHtml(cellStyle.getAlignment());
    style.setText_align(text_align);
    if ((wb instanceof XSSFWorkbook)) {
      XSSFFont xf = ((XSSFCellStyle)cellStyle).getFont();
      if (xf.getBold())
        style.setFont_weight("700");
      else {
        style.setFont_weight("400");
      }
      if (xf.getItalic()) {
        style.setFont_style("italic");
      }
      style.setFont_size(xf.getFontHeight() / 20 + "px");
      style.setFont_family(getFontName(xf.getFontName()));
      List text_decoration = new ArrayList();
      if (FontUnderline.SINGLE.getByteValue() == xf.getUnderline()) {
        text_decoration.add("underline");
      }
      if (xf.getStrikeout()) {
        text_decoration.add("line-throug");
      }
      style.setText_decoration(text_decoration);
      XSSFColor xc = xf.getXSSFColor();
      if ((xc != null) && (!"".equals(xc)))
      {
        style.setColor("#" + xc.getARGBHex().substring(2));
      }
      XSSFColor bgColor = (XSSFColor)cellStyle.getFillForegroundColorColor();
      if ((bgColor != null) && (!"".equals(bgColor)))
      {
        style.setBackground_color("#" + bgColor.getARGBHex().substring(2));
      }
      style.setBorder_bottom(getBorderStyle(2, cellStyle.getBorderBottom(), ((XSSFCellStyle)cellStyle).getBottomBorderXSSFColor()));
      style.setBorder_left(getBorderStyle(3, cellStyle.getBorderLeft(), ((XSSFCellStyle)cellStyle).getLeftBorderXSSFColor()));
      style.setBorder_right(getBorderStyle(1, cellStyle.getBorderRight(), ((XSSFCellStyle)cellStyle).getRightBorderXSSFColor()));
      style.setBorder_top(getBorderStyle(0, cellStyle.getBorderTop(), ((XSSFCellStyle)cellStyle).getTopBorderXSSFColor()));
    } else if ((wb instanceof HSSFWorkbook)) {
      HSSFFont hf = ((HSSFCellStyle)cellStyle).getFont(wb);
      if (hf.getBold())
        style.setFont_weight("700");
      else {
        style.setFont_weight("400");
      }
      if (hf.getItalic()) {
        style.setFont_style("italic");
      }
      style.setFont_size(hf.getFontHeight() / 20 + "px");
      style.setFont_family(getFontName(hf.getFontName()));
      List text_decoration = new ArrayList();
      if (FontUnderline.SINGLE.getByteValue() == hf.getUnderline()) {
        text_decoration.add("underline");
      }
      if (hf.getStrikeout()) {
        text_decoration.add("line-throug");
      }
      style.setText_decoration(text_decoration);
      short fontColor = hf.getColor();
      HSSFPalette palette = ((HSSFWorkbook)wb).getCustomPalette();
      HSSFColor hc = palette.getColor(fontColor);
      String fontColorStr = convertToStardColor(hc);
      if ((fontColorStr != null) && (!"".equals(fontColorStr.trim()))) {
        style.setColor(fontColorStr);
      }
      short bgColor = cellStyle.getFillForegroundColor();
      hc = palette.getColor(bgColor);
      String bgColorStr = convertToStardColor(hc);
      if ((bgColorStr != null) && (!"".equals(bgColorStr.trim()))) {
        style.setBackground_color(bgColorStr);
      }
      style.setBorder_bottom(getBorderStyle(palette, 2, cellStyle.getBorderBottom(), cellStyle.getBottomBorderColor()));
      style.setBorder_left(getBorderStyle(palette, 3, cellStyle.getBorderLeft(), cellStyle.getLeftBorderColor()));
      style.setBorder_right(getBorderStyle(palette, 1, cellStyle.getBorderRight(), cellStyle.getRightBorderColor()));
      style.setBorder_top(getBorderStyle(palette, 0, cellStyle.getBorderTop(), cellStyle.getTopBorderColor()));
    }
    return style;
  }

  private static String convertAlignToHtml(short alignment)
  {
    String align = "left";
    if (alignment == HorizontalAlignment.LEFT.getCode())
      align = "left";
    if (alignment == HorizontalAlignment.CENTER.getCode())
      align = "center";
    if (alignment == HorizontalAlignment.RIGHT.getCode())
      align = "right";
    return align;
  }

  private static String convertVerticalAlignToHtml(short valignment)
  {
    String valign = "middle";
    if (valignment == VerticalAlignment.BOTTOM.getCode())
      valign = "bottom";
    if (valignment == VerticalAlignment.CENTER.getCode())
      valign = "middle";
    if (valignment == VerticalAlignment.TOP.getCode())
      valign = "top";
    return valign;
  }

  private static String convertToStardColor(HSSFColor hc) {
    StringBuffer sb = new StringBuffer("");
    if (hc != null) {
      if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
        return null;
      }
      sb.append("#");
      for (int i = 0; i < hc.getTriplet().length; i++) {
        sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
      }
    }
    return sb.toString();
  }

  private static String fillWithZero(String str) {
    if ((str != null) && (str.length() < 2)) {
      return "0" + str;
    }
    return str;
  }

  private static String getBorderStyle(HSSFPalette palette, int b, short s, short t) {
    if (s == 0) {
      return null;
    }
    return "1px solid #000000";
  }

  private static String getBorderStyle(int b, short s, XSSFColor xc)
  {
    if (s == 0) {
      return null;
    }
    return "1px solid #000000";
  }

  private String getFontName(String fontName)
  {
    if ("宋体".equals(fontName))
      fontName = "SimSun";
    else if ("楷体".equals(fontName))
      fontName = "KaiTi";
    else if ("黑体".equals(fontName))
      fontName = "SimHei";
    else if ("微软雅黑".equals(fontName))
      fontName = "Microsoft YaHei";
    else if ("仿宋".equals(fontName))
      fontName = "FangSong";
    else if ("新宋体".equals(fontName))
      fontName = "NSimSun";
    return fontName;
  }

  public static String getCellValue(Cell cell)
  {
    if (cell == null) {
      return "";
    }

    if (cell.getCellType() == 1) {
      return cell.getStringCellValue();
    }
    if (cell.getCellType() == 4) {
      return String.valueOf(cell.getBooleanCellValue());
    }
    if (cell.getCellType() == 2)
      try {
        return String.valueOf("=" + cell.getCellFormula());
      } catch (IllegalStateException e) {
        try {
          return String.valueOf(cell.getRichStringCellValue());
        } catch (IllegalStateException ex) {
          return "0.0";
        }
      }
    if (cell.getCellType() == 0) {
      if (HSSFDateUtil.isCellDateFormatted(cell)) {
        DateFormat formater = new SimpleDateFormat("yyyyMMdd");
        Date d = cell.getDateCellValue();

        return formater.format(d);
      }

      DecimalFormat df = new DecimalFormat("0.######");

      return String.valueOf(df.format(cell.getNumericCellValue()));
    }

    return "";
  }

  public File getFiledata() {
    return this.filedata;
  }

  public void setFiledata(File filedata) {
    this.filedata = filedata;
  }

  public String getFiledataFileName() {
    return this.filedataFileName;
  }

  public void setFiledataFileName(String filedataFileName) {
    this.filedataFileName = filedataFileName;
  }

  public String getFiledataContentType() {
    return this.filedataContentType;
  }

  public void setFiledataContentType(String filedataContentType) {
    this.filedataContentType = filedataContentType;
  }

  public JSONArray getDataArray() {
    return this.dataArray;
  }

  public void setDataArray(JSONArray dataArray) {
    this.dataArray = dataArray;
  }

  public Map<String, Object> getDataMap() {
    return this.dataMap;
  }

  public void setDataMap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }
}

/*
 * Qualified Name:     report.java.design.action.ImportExcelAction
*
 */