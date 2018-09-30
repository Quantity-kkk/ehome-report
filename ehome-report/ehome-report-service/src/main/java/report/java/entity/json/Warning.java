package report.java.entity.json;

public class Warning
{
  private int fontSize;
  private int fontWeight;
  private String color;
  private String bgColor;
  private String operator;
  private String andOr;
  private String value;

  public int getFontSize()
  {
    return this.fontSize;
  }

  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
  }

  public int getFontWeight() {
    return this.fontWeight;
  }

  public void setFontWeight(int fontWeight) {
    this.fontWeight = fontWeight;
  }

  public String getColor() {
    return this.color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getBgColor() {
    return this.bgColor;
  }

  public void setBgColor(String bgColor) {
    this.bgColor = bgColor;
  }

  public String getOperator() {
    return this.operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getAndOr() {
    return this.andOr;
  }

  public void setAndOr(String andOr) {
    this.andOr = andOr;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}

/* 
 * Qualified Name:     report.java.entity.json.Warning
*
 */