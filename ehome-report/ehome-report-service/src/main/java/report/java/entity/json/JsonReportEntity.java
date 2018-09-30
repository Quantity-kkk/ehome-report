package report.java.entity.json;

import java.util.List;

public class JsonReportEntity
{
  private List<MergeCells> mergeCells;
  private List<Integer> colWidths;
  private List<Integer> rowHeights;
  private RowCategory rowCategory;
  private int page;
  private int pageorder;
  private int maxR;
  private int maxC;
  private String toolbar;
  private String uuid;
  private String reportStyle;
  private String reportDescription;
  private String reportVersion;
  private String reportMemo;
  private String mainUuid;
  private List<Row> row;
  private List<DataSets> dataSets;
  private List<DataParms> dataParms;
  private String fileName;

  public void setMergeCells(List<MergeCells> mergeCells)
  {
    this.mergeCells = mergeCells;
  }

  public List<MergeCells> getMergeCells() {
    return this.mergeCells;
  }

  public void setColWidths(List<Integer> colWidths) {
    this.colWidths = colWidths;
  }

  public List<Integer> getColWidths() {
    return this.colWidths;
  }

  public void setRowHeights(List<Integer> rowHeights) {
    this.rowHeights = rowHeights;
  }

  public List<Integer> getRowHeights() {
    return this.rowHeights;
  }

  public void setRowCategory(RowCategory rowCategory) {
    this.rowCategory = rowCategory;
  }

  public RowCategory getRowCategory() {
    return this.rowCategory;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPage() {
    return this.page;
  }

  public void setPageorder(int pageorder) {
    this.pageorder = pageorder;
  }

  public int getPageorder() {
    return this.pageorder;
  }

  public void setMaxR(int maxR) {
    this.maxR = maxR;
  }

  public int getMaxR() {
    return this.maxR;
  }

  public void setMaxC(int maxC) {
    this.maxC = maxC;
  }

  public String getToolbar() {
    return this.toolbar;
  }

  public void setToolbar(String toolbar) {
    this.toolbar = toolbar;
  }

  public int getMaxC() {
    return this.maxC;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getUuid() {
    return this.uuid;
  }

  public void setReportStyle(String reportStyle) {
    this.reportStyle = reportStyle;
  }

  public String getReportStyle() {
    return this.reportStyle;
  }

  public void setReportDescription(String reportDescription) {
    this.reportDescription = reportDescription;
  }

  public String getReportDescription() {
    return this.reportDescription;
  }

  public void setReportVersion(String reportVersion) {
    this.reportVersion = reportVersion;
  }

  public String getReportVersion() {
    return this.reportVersion;
  }

  public void setReportMemo(String reportMemo) {
    this.reportMemo = reportMemo;
  }

  public String getReportMemo() {
    return this.reportMemo;
  }

  public void setMainUuid(String mainUuid) {
    this.mainUuid = mainUuid;
  }

  public String getMainUuid() {
    return this.mainUuid;
  }

  public void setRow(List<Row> row) {
    this.row = row;
  }

  public List<Row> getRow() {
    return this.row;
  }

  public void setDataSets(List<DataSets> dataSets) {
    this.dataSets = dataSets;
  }

  public List<DataSets> getDataSets() {
    return this.dataSets;
  }

  public void setDataParms(List<DataParms> dataParms) {
    this.dataParms = dataParms;
  }

  public List<DataParms> getDataParms() {
    return this.dataParms;
  }

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}

/* 
 * Qualified Name:     report.java.entity.json.JsonReportEntity
*
 */