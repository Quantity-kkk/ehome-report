package report.java.echarts.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import report.java.echarts.code.SeriesType;
import report.java.echarts.series.Bar;
import report.java.echarts.series.Chord;
import report.java.echarts.series.Force;
import report.java.echarts.series.Funnel;
import report.java.echarts.series.Gauge;
import report.java.echarts.series.Island;
import report.java.echarts.series.K;
import report.java.echarts.series.Line;
import report.java.echarts.series.Map;
import report.java.echarts.series.Pie;
import report.java.echarts.series.Radar;
import report.java.echarts.series.Scatter;
import report.java.echarts.series.Series;

public class SeriesDeserializer
  implements JsonDeserializer<Series>
{
  public Series deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    throws JsonParseException
  {
    JsonObject jsonObject = json.getAsJsonObject();
    String _type = jsonObject.get("type").getAsString();
    SeriesType type = SeriesType.valueOf(_type);
    Series series = null;
    switch (type) {
    case bar:
      series = (Series)context.deserialize(jsonObject, Line.class);
      break;
    case chord:
      series = (Series)context.deserialize(jsonObject, Bar.class);
      break;
    case eventRiver:
      series = (Series)context.deserialize(jsonObject, Scatter.class);
      break;
    case map:
      series = (Series)context.deserialize(jsonObject, Funnel.class);
      break;
    case funnel:
      series = (Series)context.deserialize(jsonObject, Pie.class);
      break;
    case k:
      series = (Series)context.deserialize(jsonObject, Force.class);
      break;
    case pie:
      series = (Series)context.deserialize(jsonObject, Gauge.class);
      break;
    case line:
      series = (Series)context.deserialize(jsonObject, Map.class);
      break;
    case radar:
      series = (Series)context.deserialize(jsonObject, Island.class);
      break;
    case force:
      series = (Series)context.deserialize(jsonObject, K.class);
      break;
    case gauge:
      series = (Series)context.deserialize(jsonObject, Radar.class);
      break;
    case island:
      series = (Series)context.deserialize(jsonObject, Chord.class);
    }

    return series;
  }
}

/*
 * Qualified Name:     report.java.echarts.json.SeriesDeserializer
*
 */