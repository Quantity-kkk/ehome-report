package report.java.entity.json;

import java.util.List;

public class Overlapping
{
  private String base64;
  private List<Nodes> nodes;

  public void setBase64(String base64)
  {
    this.base64 = base64;
  }
  public String getBase64() {
    return this.base64;
  }

  public void setNodes(List<Nodes> nodes) {
    this.nodes = nodes;
  }
  public List<Nodes> getNodes() {
    return this.nodes;
  }
}

/* 
 * Qualified Name:     report.java.entity.json.Overlapping
*
 */