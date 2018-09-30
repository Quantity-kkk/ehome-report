package report.java.echarts.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.PrintStream;
import report.java.echarts.Option;
import report.java.echarts.axis.Axis;
import report.java.echarts.series.Series;

public class GsonUtil
{
  public static String format(Object object)
  {
    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    JsonParser jp = new JsonParser();
    JsonElement je = jp.parse(gson.toJson(object));
    String prettyJsonString = gson.toJson(je);

    String[] lines = prettyJsonString.split("\n");
    lines = replaceFunctionQuote(lines);
    StringBuilder stringBuilder = new StringBuilder();
    for (String line : lines) {
      stringBuilder.append(line);
    }
    return stringBuilder.toString();
  }

  public static String prettyFormat(Object object)
  {
    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    JsonParser jp = new JsonParser();
    JsonElement je = jp.parse(gson.toJson(object));
    String prettyJsonString = gson.toJson(je);

    String[] lines = prettyJsonString.split("\n");
    lines = replaceFunctionQuote(lines);
    StringBuilder stringBuilder = new StringBuilder();
    for (String line : lines) {
      stringBuilder.append(line + "\n");
    }
    return stringBuilder.toString();
  }

  public static String[] replaceFunctionQuote(String[] lines)
  {
    boolean function = false;
    boolean immediately = false;
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i].trim();
      if ((!function) && (line.contains("\"function"))) {
        function = true;
        line = line.replaceAll("\"function", "function");
      }
      if ((function) && (line.contains("}\""))) {
        function = false;
        line = line.replaceAll("\\}\"", "\\}");
      }

      if ((!immediately) && (line.contains("\"(function"))) {
        immediately = true;
        line = line.replaceAll("\"\\(function", "\\(function");
      }
      if ((immediately) && (line.contains("})()\""))) {
        immediately = false;
        line = line.replaceAll("\\}\\)\\(\\)\"", "\\}\\)\\(\\)");
      }
      lines[i] = line;
    }
    return lines;
  }

  public static <T extends Option> T fromJSON(String json, Class<T> type)
  {
    Gson gson = new GsonBuilder().setPrettyPrinting()
      .registerTypeAdapter(Series.class, new SeriesDeserializer())
      .registerTypeAdapter(Axis.class, new AxisDeserializer()).create();
    return (T)gson.fromJson(json, type);
  }

  public static Option fromJSON(String json)
  {
    Gson gson = new GsonBuilder().setPrettyPrinting()
      .registerTypeAdapter(Series.class, new SeriesDeserializer())
      .registerTypeAdapter(Axis.class, new AxisDeserializer()).create();
    Option option = (Option)gson.fromJson(json, Option.class);
    return option;
  }

  public static void print(Object object)
  {
    System.out.println(format(object));
  }

  public static void printPretty(Object object)
  {
    System.out.println(prettyFormat(object));
  }
}
