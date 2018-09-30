package report.java.entity.json;

import java.util.List;

public class Col
{
  private String value;
  private int colwidth;
  private int width;
  private int height;
  private int col;
  private int row;
  private Style style;
  private String className;
  private String link;
  private int rowspan;
  private int colspan;
  private String frameid;
  private String dic;
  private String exptext;
  private String ds;
  private Overlapping overlapping;
  private String bgImage;
  private List<Image> image;
  private List<BgColor> bgColors;

  public void setValue(String value)
  {
    this.value = value;
  }

  public int getColwidth() {
    return this.colwidth;
  }

  public void setColwidth(int colwidth) {
    this.colwidth = colwidth;
  }

  public String getValue() {
    return this.value;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getWidth() {
    return this.width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getHeight() {
    return this.height;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public int getCol() {
    return this.col;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getRow() {
    return this.row;
  }

  public void setStyle(Style style) {
    this.style = style;
  }

  public Style getStyle() {
    return this.style;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getClassName() {
    return this.className;
  }

  public void setRowspan(int rowspan) {
    this.rowspan = rowspan;
  }

  public int getRowspan() {
    return this.rowspan;
  }

  public void setColspan(int colspan) {
    this.colspan = colspan;
  }

  public int getColspan() {
    return this.colspan;
  }

  public String getLink() {
    return this.link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getFrameid() {
    return this.frameid;
  }

  public void setFrameid(String frameid) {
    this.frameid = frameid;
  }

  public String getDic() {
    return this.dic;
  }

  public void setDic(String dic) {
    this.dic = dic;
  }

  public String getExptext() {
    return this.exptext;
  }

  public void setExptext(String exptext) {
    this.exptext = exptext;
  }

  public String getDs() {
    return this.ds;
  }

  public void setDs(String ds) {
    this.ds = ds;
  }

  public Overlapping getOverlapping() {
    return this.overlapping;
  }

  public void setOverlapping(Overlapping overlapping) {
    this.overlapping = overlapping;
  }

  public void setImage(List<Image> image)
  {
    this.image = image;
  }
  public List<Image> getImage() {
    return this.image;
  }

  public String getBgImage() {
    return this.bgImage;
  }

  public void setBgImage(String bgImage) {
    this.bgImage = bgImage;
  }

  public List<BgColor> getBgColors() {
    return this.bgColors;
  }

  public void setBgColors(List<BgColor> bgColors) {
    this.bgColors = bgColors;
  }
}

/* 
 * Qualified Name:     report.java.entity.json.Col
*
 */