package report.java.echarts.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import report.java.echarts.axis.Axis;
import report.java.echarts.axis.CategoryAxis;
import report.java.echarts.axis.TimeAxis;
import report.java.echarts.axis.ValueAxis;
import report.java.echarts.code.AxisType;

public class AxisDeserializer
  implements JsonDeserializer<Axis>
{
  public Axis deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    throws JsonParseException
  {
    JsonObject jsonObject = json.getAsJsonObject();
    String _type = jsonObject.get("type").getAsString();
    AxisType type = AxisType.valueOf(_type);
    Axis axis = null;
    switch (type) {
    case category:
      axis = (Axis)context.deserialize(jsonObject, CategoryAxis.class);
      break;
    case time:
      axis = (Axis)context.deserialize(jsonObject, ValueAxis.class);
      break;
    case value:
      axis = (Axis)context.deserialize(jsonObject, TimeAxis.class);
    }

    return axis;
  }
}

/* 
 * Qualified Name:     report.java.echarts.json.AxisDeserializer
*
 */