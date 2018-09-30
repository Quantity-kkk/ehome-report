package report.java.jrreport.util;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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

public class PoiUtil
{
  public static void exportExcel(String body, String defaultFilename, HttpServletRequest request, HttpServletResponse response)
  {
    body = StringUtils.replace(body, "<table class='dynamic' cellspacing='0' cellpadding='0'><thead class='rtitle'>", "");
    body = StringUtils.replace(body, "<tr><td class='frameid'><tr>", "<tr>");
    body = StringUtils.replace(body, "<thead>", "");
    body = StringUtils.replace(body, "</thead>", "");
    body = StringUtils.replace(body, "<tbody>", "");
    body = StringUtils.replace(body, "</tbody>", "");
    body = StringUtils.replace(body, "<tfoot>", "");
    body = StringUtils.replace(body, "</tfoot>", "");
    body = StringUtils.replace(body, "</table>", "");
    body = StringUtils.replace(body, "</td></tr></td></tr>", "</td></tr>");
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

    OutputStream os = null;
    try {
      os = response.getOutputStream();

      SXSSFWorkbook wb = new SXSSFWorkbook(1000);
      wb.setCompressTempFiles(true);

      Sheet sheet1 = wb.createSheet("sheet1");
      SXSSFCell cell = null;
      SXSSFRow rw = null;

      if ((body.length() > 0) && (body.indexOf("<tr>") != -1) && (body.indexOf("</tr>") != -1) && (body.indexOf("<td ") != -1) && (body.indexOf("</td>") != -1)) {
        String[] rowArr = body.split("<\\/tr>");
        body = null;
        int[][] rcArr = new int[rowArr.length][100];
        for (int i = 0; i < rowArr.length; i++) {
          if ((rowArr[i].indexOf("<td ") != -1) && (rowArr[i].indexOf("</td>") != -1)) {
            rw = (SXSSFRow)sheet1.createRow(i);
            String[] cellArr = rowArr[i].split("<td ");
            int cellIndex = 0;
            for (int j = 0; j < cellArr.length - 1; j++) {
              cellIndex = rcArr[i][j] + cellIndex;
              int[] map = setCell(cellArr[(j + 1)], rw, cell, wb, sheet1, i, cellIndex);
              cellIndex += map[1];
              if (map[0] > 1) {
                for (int r = 1; r < map[0]; r++) {
                  if (rcArr[(i + r)][(cellIndex - 1)] > 0)
                    rcArr[(i + r)][(cellIndex - 1)] += 1;
                  else {
                    rcArr[(i + r)][(cellIndex - 1)] = 1;
                  }
                }
              }
            }
          }
        }
      }
      wb.write(os);
      wb.dispose();
    } catch (Exception e) {
      e.printStackTrace();
      try
      {
        if (os != null)
          os.close();
      }
      catch (IOException e1) {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (os != null)
          os.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static int[] setCell(String str, SXSSFRow rw, SXSSFCell cell, SXSSFWorkbook wb, Sheet sheet1, int rowindex, int cellindex)
  {
    XSSFCellStyle cellStyle = (XSSFCellStyle)wb.createCellStyle();
    XSSFFont cellFont = (XSSFFont)wb.createFont();
    if (str.indexOf("style='") != -1) {
      String nstr = StringUtils.replace(str, "style='", "`");
      String style = StringUtils.split(StringUtils.split(nstr, "`")[1], "'")[0];
      String[] styleArr = StringUtils.split(style, ";");
      for (int s = 0; s < styleArr.length; s++) {
        String[] sty = StringUtils.split(styleArr[s], ":");
        if (sty[0].equals("height")) {
          rw.setHeight((short)(new Short(StringUtils.replace(sty[1], "px", "")).shortValue() * 20));
        }
        if (sty[0].equals("width")) {
          int w = Integer.parseInt(StringUtils.replace(sty[1], "px", "")) * 40;
          sheet1.setColumnWidth(cellindex, w > 6000 ? 6000 : w);
        }
        if (sty[0].equals("word-break")) {
          cellStyle.setWrapText(true);
        }
        if (sty[0].equals("text-align")) {
          if (sty[1].equals("center"))
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
          else if (sty[1].equals("right")) {
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
          }

        }

        if (sty[0].equals("vertical-align")) {
          if (sty[1].equals("middle"))
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
          else if (sty[1].equals("top"))
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
          else if (sty[1].equals("bottom")) {
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
          }
        }
        if (sty[0].equals("font-family")) {
          cellFont.setFontName(sty[1]);
        }
        if ((sty[0].equals("background-color")) && 
          (!"rgba(0, 0, 0, 0)".equals(sty[1]))) {
          String rgb = StringUtils.replace(StringUtils.replace(StringUtils.replace(sty[1], "rgb(", ""), ")", ""), " ", "");
          String[] r = StringUtils.split(rgb, ",");
          Color color = new Color(Integer.parseInt(r[0]), Integer.parseInt(r[1]), Integer.parseInt(r[2]));
          cellStyle.setFillForegroundColor(new XSSFColor(color));
          cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        if (sty[0].equals("font-size")) {
          cellFont.setFontHeightInPoints((short)(Integer.parseInt(StringUtils.replace(sty[1], "px", "")) - 3));
        }
        if ((sty[0].equals("color")) && 
          (!"rgb(0, 0, 0)".equals(sty[1]))) {
          String rgb = StringUtils.replace(StringUtils.replace(StringUtils.replace(sty[1], "rgb(", ""), ")", ""), " ", "");
          String[] r = StringUtils.split(rgb, ",");
          Color color = new Color(Integer.parseInt(r[0]), Integer.parseInt(r[1]), Integer.parseInt(r[2]));
          cellFont.setColor(new XSSFColor(color));
        }

        if ((sty[0].equals("font-weight")) && (sty[1].equals("700"))) {
          cellFont.setBold(true);
        }
      }
    }
    int rowspan = 1;
    int colspan = 1;
    if (str.indexOf("rowspan='") != -1) {
      String nstr = StringUtils.replace(str, "rowspan='", "^");
      String[] arr = StringUtils.split(nstr, "^");
      if (arr.length == 1)
        rowspan = Integer.parseInt(arr[0].split("'")[0]);
      else {
        rowspan = Integer.parseInt(arr[1].split("'")[0]);
      }
    }
    if (str.indexOf("colspan='") != -1) {
      String nstr = StringUtils.replace(str, "colspan='", "^");
      String[] arr = StringUtils.split(nstr, "^");
      if (arr.length == 1)
        colspan = Integer.parseInt(arr[0].split("'")[0]);
      else {
        colspan = Integer.parseInt(arr[1].split("'")[0]);
      }
    }
    if (str.indexOf("isnum='1'") != -1) {
      DataFormat format = wb.createDataFormat();
      cellStyle.setDataFormat(format.getFormat("#,##0.00"));
    } else {
      DataFormat format = wb.createDataFormat();
      cellStyle.setDataFormat(format.getFormat("@"));
    }
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    if ((rowspan > 1) || (colspan > 1)) {
      CellRangeAddress cra = new CellRangeAddress(rowindex, rowindex + rowspan - 1, cellindex, cellindex + colspan - 1);
      sheet1.addMergedRegion(cra);
      RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet1);
      RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet1);
      RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet1);
      RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet1);
    }

    String value = str.split("<\\/td>")[0];
    if (value.split(">").length == 1)
      value = "";
    else {
      value = value.split(">")[1];
    }
    cellStyle.setFont(cellFont);
    cell = rw.createCell(cellindex);
    cell.setCellStyle(cellStyle);
    cell.setCellValue(value);
    int[] map = new int[2];
    map[0] = rowspan;
    map[1] = colspan;
    return map;
  }
}

/*
 * Qualified Name:     report.java.jrreport.util.PoiUtil
*
 */