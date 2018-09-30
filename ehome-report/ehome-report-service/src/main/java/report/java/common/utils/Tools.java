package report.java.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map map = new HashMap();

		JsonObject json = new JsonParser().parse(jsonStr).getAsJsonObject();
		for (Iterator localIterator1 = json.entrySet().iterator(); localIterator1
				.hasNext();) {
			Object k = localIterator1.next();
			JsonArray v = json.getAsJsonArray(k.toString());

			if ((v instanceof JsonArray)) {
				List list = new ArrayList();
				Iterator it = v.iterator();
				while (it.hasNext()) {
					JsonElement json2 = (JsonElement) it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	public static Map<String, Object> parseJSON2MapList(String jsonStr,
			List<JSONObject> l) {
		Map map = new HashMap();

		JSONObject json = JSON.parseObject(jsonStr);
		for (Iterator localIterator1 = json.entrySet().iterator(); localIterator1
				.hasNext();) {
			Object k = localIterator1.next();
			Object v = null;
			for (JSONObject o : l) {
				if (o.get("datafield").toString().equals(k)) {
					v = getDbValue(o.get("datatype").toString(),
							json.get(k.toString()));
				}
			}

			if ((v instanceof JsonArray)) {
				List list = new ArrayList();
				Iterator it = ((JsonArray) v).iterator();
				while (it.hasNext()) {
					JsonElement json2 = (JsonElement) it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	public static Boolean isNum(String datatype) {
		Boolean num = Boolean.valueOf(false);
		if ((datatype != null) && (datatype.length() > 0)) {
			datatype = datatype.toLowerCase();
			if ((datatype.equals("integer")) || (datatype.equals("tinyint"))
					|| (datatype.equals("decimal"))
					|| (datatype.equals("number"))
					|| (datatype.equals("double"))
					|| (datatype.equals("float")) || (datatype.equals("2"))) {
				num = Boolean.valueOf(true);
			}
		}
		return num;
	}

	public static Object getDbValue(String datatype, Object value) {
		Object v = null;
		if ((datatype != null) && (!"".equals(datatype))) {
			datatype = datatype.toLowerCase();
		}
		if ((value != null) && (!"".equals(value))) {
			try {
				if ((datatype.equals("integer"))
						|| (datatype.equals("tinyint"))) {
					v = Integer.valueOf(Integer.parseInt(value.toString()));
					//break label233;
				}
				if ((datatype.equals("number")) || (datatype.equals("decimal"))) {
					v = new BigDecimal(value.toString());
					//break label233;
				}
				if ((datatype.equals("double")) || (datatype.equals("float"))) {
					v = Double.valueOf(Double.parseDouble(value.toString()));
					//break label233;
				}
				v = value.toString();
			} catch (Exception ex) {
				v = value.toString();
			}
		} else if ((datatype.equals("integer")) || (datatype.equals("tinyint")))
			v = Integer.valueOf(0);
		else if ((datatype.equals("number")) || (datatype.equals("decimal")))
			v = new BigDecimal("0");
		else if ((datatype.equals("double")) || (datatype.equals("float")))
			v = Integer.valueOf(0);
		else {
			v = "";
		}

		label233: datatype = null;
		return v;
	}

	public static Class<?> getCls(List<String> dataSetslist,
			Map<String, Object> datasetmap, String filename) {
		Class cls = String.class;
		if ((filename != null) && (filename.indexOf("$V{") != -1)) {
			filename = filename.replace("$V{", "").replace("}", "");
		}
		for (String key : datasetmap.keySet()) {
			if (dataSetslist.contains(key)) {
				List FieldsList = (List) ((Map) datasetmap.get(key))
						.get("fields");
				for (Iterator localIterator2 = FieldsList.iterator(); localIterator2
						.hasNext();) {
					Object obj = localIterator2.next();
					String DataField = ((Map) obj).get("datafield").toString();
					if (DataField.equals(filename)) {
						String datatype = (String)((Map) obj).get("datatype");
						if (!"".equals(datatype)) {
							datatype = datatype.toLowerCase();
						}
						String datadic = (String)((Map) obj).get("datadic");
						if (datadic.length() > 4) {
							cls = String.class;
						} else if ((datatype.equals("integer"))
								|| (datatype.equals("tinyint")))
							cls = Integer.class;
						else if ((datatype.equals("number"))
								|| (datatype.equals("decimal")))
							cls = BigDecimal.class;
						else if ((datatype.equals("double"))
								|| (datatype.equals("float"))) {
							cls = Double.class;
						}
					}
				}

			}

		}

		return cls;
	}

	public static String[] array_unique(String[] a) {
		List list = new LinkedList();
		for (int i = 0; i < a.length; i++) {
			if (!list.contains(a[i])) {
				list.add(a[i]);
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static boolean isNumber(Object str) {
		Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]*)?$");
		Matcher match = pattern.matcher(str.toString());
		return match.matches();
	}

	public static void main(String[] args) {
	}
}

/*
 * Location:
 * E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\com.report.jar
 * Qualified Name: report.java.common.utils.Tools JD-Core Version: 0.6.0
 */