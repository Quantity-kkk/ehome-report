package report.java.jrreport.util;

import java.util.Map;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.Script;

public class DyMethodUtil
{
  private static JexlEngine jexl = new JexlEngine();
  private static JexlContext jcc = new MapContext();

  public static Object invokeMethod(String jexlExp, Map<String, Object> map) {
    Expression e = jexl.createExpression(jexlExp);
    JexlContext jc = new MapContext();
    for (String key : map.keySet()) {
      jc.set(key, map.get(key));
    }
    if (e.evaluate(jc) == null) {
      return "";
    }
    return e.evaluate(jc);
  }

  public static Object invokeMethodNew(String jexlExp, JexlContext jc) {
    jexl.setCache(256);

    Object o = null;
    if (jexlExp.contains("expFuntionChange.")) {
      Expression e = jexl.createExpression(jexlExp);
      o = e.evaluate(jc);
    } else {
      Script e = jexl.createScript(jexlExp);
      o = e.execute(jcc);
    }

    if (o == null) {
      return "";
    }
    return o;
  }
}

/*
 * Qualified Name:     report.java.jrreport.util.DyMethodUtil
*
 */