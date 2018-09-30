package report.java.jrreport.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import report.java.common.utils.Tools;
import report.java.db.bean.DataSourceBean;
import report.java.db.util.DBUtil;
import report.java.db.util.XmlUtil;
import report.java.design.action.BaseAction;
import report.java.echarts.DataZoom;
import report.java.echarts.Polar;
import report.java.echarts.Toolbox;
import report.java.echarts.axis.Axis;
import report.java.echarts.axis.AxisTick;
import report.java.echarts.axis.CategoryAxis;
import report.java.echarts.axis.SplitLine;
import report.java.echarts.axis.ValueAxis;
import report.java.echarts.code.AxisType;
import report.java.echarts.code.Orient;
import report.java.echarts.code.Tool;
import report.java.echarts.code.Trigger;
import report.java.echarts.code.X;
import report.java.echarts.code.Y;
import report.java.echarts.json.GsonOption;
import report.java.echarts.series.Bar;
import report.java.echarts.series.Gauge;
import report.java.echarts.series.K;
import report.java.echarts.series.Line;
import report.java.echarts.series.Pie;
import report.java.echarts.series.Radar;
import report.java.jrreport.dao.PreviewReportDao;
import report.java.jrreport.report.jr.util.ReportElementUtil;
import report.java.jrreport.util.JRFunExpImpl;
import report.java.jrreport.util.JRUtilNew;
import report.java.jrreport.util.StringHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PreviewReportService {
	public static List<String> groupslist = new ArrayList();
	public static int topCount = 0;

	public Map<String, Object> compileReportNew(String dataSetsModel) {
		Map DataSetMap = new CaseInsensitiveMap();
		Map map = new CaseInsensitiveMap();
		if (dataSetsModel.equals(""))
			return map;
		try {
			Document dataSets = DocumentHelper.parseText(dataSetsModel);
			listNodesNew(map, DataSetMap, dataSets.getRootElement());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return map;
	}

	public Map<String, Object> compileReport(String dataSetsModel) {
		Map DataSetMap = new HashMap();
		Map map = new HashMap();
		if (dataSetsModel.equals(""))
			return map;
		try {
			Document dataSets = DocumentHelper.parseText(dataSetsModel);
			listNodes(map, DataSetMap, dataSets.getRootElement());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return map;
	}

	public void dicMap(Map<String, Object> dsMap,
			Map<String, List<String>> dicWhereMap) {
		if (dicWhereMap.size() > 0)
			for (String key : dicWhereMap.keySet()) {
				List<String> list = (List) dicWhereMap.get(key);
				StringBuilder nstr = new StringBuilder();
				for (String nkey : list) {
					if (DBUtil.DicMap.get(nkey) == null) {
						nstr.append("'" + nkey + "',");
					}
				}
				if (nstr.toString().length() > 0) {
					String where = nstr.toString().substring(0,
							nstr.toString().length() - 1);
					Map queryMap = (Map) ((Map) dsMap.get(key)).get("query");
					Map<String, Object> FieldsList = (Map) ((Map) dsMap
							.get(key)).get("fields");
					String dataSourceName = queryMap.get("datasourcename")
							.toString();
					DataSourceBean dataSourceBean = new DataSourceBean();
					if (!DBUtil.isJndi().booleanValue()) {
						dataSourceBean.setDataSourceName(dataSourceName);
						String realPath = BaseAction.realPath + "db-config.xml";
						dataSourceBean = XmlUtil.selectXML(dataSourceBean,
								realPath);
					}
					String sql = queryMap.get("commandtext").toString();
					String KEY_NAME = "KEY_NAME";
					String OPT_CODE = "OPT_CODE";
					String OPT_NAME = "OPT_NAME";
					for (Map.Entry obj : FieldsList.entrySet()) {
						String DataField = (String) obj.getKey();
						Object dicType = "";
						if (((Map) FieldsList.get(DataField)).get("dictype") != null) {
							dicType = ((Map) FieldsList.get(DataField))
									.get("dictype");
						}
						if (dicType.equals("type")) {
							KEY_NAME = DataField.toUpperCase();
						}
						if (dicType.equals("code")) {
							OPT_CODE = DataField.toUpperCase();
						}
						if (dicType.equals("text")) {
							OPT_NAME = DataField.toUpperCase();
						}
					}
					if (sql.length() > 0) {
						sql = sql.trim();
						if (sql.endsWith(";")) {
							sql = sql.substring(0, sql.length() - 1);
						}
					}
					if (sql.toLowerCase().indexOf(" where ") != -1)
						sql = sql + " and " + KEY_NAME + " in (" + where + ")";
					else {
						sql = sql + " where " + KEY_NAME + " in (" + where
								+ ")";
					}
					List dicslist = DBUtil.getResultToList(dataSourceBean,
							Boolean.valueOf(true), sql, new Object[0]);
					for (int i = 0; i < dicslist.size(); i++) {
						Map m = new HashMap();
						Set<String> ss = ((Map) dicslist.get(i)).keySet();
						for (String set : ss) {
							if (set.toUpperCase().equals(KEY_NAME))
								m.put("KEY_NAME",
										((Map) dicslist.get(i)).get(set));
							else if (set.toUpperCase().equals(OPT_CODE))
								m.put("OPT_CODE",
										((Map) dicslist.get(i)).get(set));
							else if (set.toUpperCase().equals(OPT_NAME)) {
								m.put("OPT_NAME",
										((Map) dicslist.get(i)).get(set));
							}
						}
						Map map = new HashMap();
						if (!"".equals(m.get("OPT_CODE").toString())) {
							map.put(m.get("OPT_CODE").toString(),
									m.get("OPT_NAME").toString());
							if ((DBUtil.DicMap.get(m.get("KEY_NAME")) == null)
									&& (!"".equals(m.get("KEY_NAME").toString()))) {
								DBUtil.DicMap.put(m.get("KEY_NAME").toString(),
										map);
							} else {
								map = (Map) DBUtil.DicMap.get(m.get("KEY_NAME")
										.toString());
								map.put(m.get("OPT_CODE").toString(),
										m.get("OPT_NAME").toString());
								DBUtil.DicMap.put(m.get("KEY_NAME").toString(),
										map);
							}
						}
					}
				}
			}
	}

	public Map<String, Object> queryExecuterNew(Connection conn,
			List<String> dataSetslist, Map<String, Object> map,
			String parmsModel, JSONArray jsonArray, Map<String, Object> mmdtl,
			int currentPage, int pageSize, Boolean isTotal,
			Map<String, BigDecimal> sumlist, Boolean iscustom) {
		PreviewReportDao pd = new PreviewReportDao();
		if (pageSize == -2) {
			pageSize = 40;
		}
		int pg = pageSize;
		if (!isTotal.booleanValue()) {
			pg = 0;
		}
		List list = new ArrayList();
		Map sum = sumlist;
		int totalRecord = 0;

		List nodes = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		if ((parmsModel != null) && (!parmsModel.equals(""))) {
			try {
				Document document = DocumentHelper.parseText(parmsModel);
				Element rootElm = document.getRootElement();
				nodes = rootElm.elements("parm");
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		int kindex = 0;
		for (String key : map.keySet()) {
			if (dataSetslist.contains(key)) {
				Map queryMap = (Map) ((Map) map.get(key)).get("query");
				Map<String, Object> FieldsList = (Map) ((Map) map.get(key))
						.get("fields");
				String dataSourceName = queryMap.get("datasourcename")
						.toString();
				DataSourceBean dataSourceBean = new DataSourceBean();
				if (!DBUtil.isJndi().booleanValue()) {
					dataSourceBean.setDataSourceName(dataSourceName);
				}
				String sql = queryMap.get("commandtext").toString();
				List l = new ArrayList();
				for (Map.Entry obj : FieldsList.entrySet()) {
					String DataField = (String) obj.getKey();
					Object DataType = "";
					if (((Map) FieldsList.get(DataField)).get("datatype") != null) {
						DataType = ((Map) FieldsList.get(DataField))
								.get("datatype");
					}
					Object datadic = "";
					if (iscustom.booleanValue()) {
						Object dic = ((Map) FieldsList.get(DataField))
								.get("datadic");
						if (dic != null) {
							if (dic.toString().toLowerCase()
									.startsWith("select "))
								datadic = dic.toString();
							else {
								datadic = JSON.toJSON(dic);
							}
						}
					}
					JSONObject ob = new JSONObject();
					ob.put("datafield", DataField);
					ob.put("datatype", DataType);
					ob.put("datadic", datadic);
					l.add(ob);
					groupslist.add(DataField);
				}

				if ("javabean".equals(dataSourceName)) {
					totalRecord = jsonArray.size();
					int strf = (currentPage - 1) * pageSize;
					int strt = currentPage * pageSize;
					if (strt > totalRecord) {
						strt = totalRecord;
					}
					JSONArray jsa = new JSONArray();
					for (int i = strf; i < strt; i++) {
						jsa.add(jsonArray.get(i));
					}
					if (jsa.size() > 0)
						pd.queryFromObjectNew(sql, l, sum, list, key, jsa);
					else
						pd.queryFromObjectNew(sql, l, sum, list, key, jsonArray);
				} else {
					if (!DBUtil.isJndi().booleanValue()) {
						String realPath = BaseAction.realPath + "db-config.xml";
						dataSourceBean = XmlUtil.selectXML(dataSourceBean,
								realPath);
					}
					sql = sql.replaceAll("  ", " ");
					LinkedHashMap<String, Object> parameters = (LinkedHashMap) queryMap
							.get("parms");
					ArrayList objParamsList = new ArrayList();
					int ii = 0;
					int iii = 0;
					String[] sqlarr = sql.split("\\?");
					String nslq = sql;
					String sin = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTNVWXYZ0123456789-_";
					String dbtype;
					for (Object k : parameters.keySet()) {
						Map parameter = (Map) parameters.get(k);
						Object value = request.getParameter(parameter.get(
								"name").toString());
						dbtype = "";
						if ((nodes != null) && (nodes.size() > 0)) {
							for (Iterator it = nodes.iterator(); it.hasNext();) {
								Element elm = (Element) it.next();
								if (parameter.get("name").equals(
										elm.attributeValue("name"))) {
									dbtype = elm.attributeValue("dbtype");
								}
							}
						}
						Boolean isw = Boolean.valueOf(false);
						if ((dbtype != null)
								&& (dbtype.length() > 0)
								&& ((dbtype.toLowerCase().equals("double")) || (dbtype
										.toLowerCase().equals("int")))
								&& (sqlarr[iii].length() > 4)
								&& (sin.indexOf(sqlarr[iii]
										.substring(sqlarr[iii].length() - 1)) != -1)) {
							isw = Boolean.valueOf(true);
						}

						if (isw.booleanValue()) {
							nslq = nslq.replace(sqlarr[iii] + "?", sqlarr[iii]
									+ String.valueOf(value));
						} else {
							if ((dbtype != null)
									&& (dbtype.length() > 0)
									&& ((dbtype.toLowerCase().equals("double")) || (dbtype
											.toLowerCase().equals("int"))))
								objParamsList.add("【" + value + "】");
							else {
								objParamsList.add(value);
							}
							ii++;
						}
						iii++;
					}
					Object[] objParams = objParamsList.toArray();
					if ((mmdtl.size() > 0) && (sql.indexOf("?") != -1)) {
						String[] arrdtcol = null;
						int kkk = 0;
						for (String kk : mmdtl.keySet()) {
							if (kkk == kindex) {
								if (mmdtl.get(kk).toString().indexOf(",") != -1)
									arrdtcol = mmdtl.get(kk).toString()
											.split(",");
								else {
									arrdtcol = new String[] { mmdtl.get(kk)
											.toString() };
								}
							}
							kkk++;
						}
						if (arrdtcol != null) {
							for (int i = 0; i < arrdtcol.length; i++) {
								if (i == 0)
									try {
										pd.queryExecuterNew(
												dataSourceBean,
												conn,
												nslq.replaceFirst("\\?", "'"
														+ arrdtcol[i] + "'"),
												l, sum, list, key, "",
												currentPage, pg,
												dataSourceBean.getType(),
												objParams);
									} catch (SQLException e) {
										e.printStackTrace();
									}
								else {
									try {
										pd.queryExecuterNew(
												dataSourceBean,
												conn,
												nslq.replaceFirst("\\?", "'"
														+ arrdtcol[i] + "'"),
												l, sum, list, key, "_" + i,
												currentPage, pg,
												dataSourceBean.getType(),
												objParams);
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
								if (isTotal.booleanValue()) {
									if (currentPage == 1) {
										int trd = 0;
										if (pageSize > 0) {
											String nsqln = StringHelper
													.getCountSql(nslq)
													.replaceFirst(
															"\\?",
															"'" + arrdtcol[i]
																	+ "'");
											trd = pd.queryExecuterRecord(
													dataSourceBean, conn,
													nsqln, objParams);
										} else {
											trd = list.size();
										}
										if (trd > totalRecord)
											totalRecord = trd;
									} else {
										try {
											totalRecord = Integer
													.parseInt(request
															.getParameter("totalRecord"));
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
						kindex++;
					} else {
						try {
							pd.queryExecuterNew(dataSourceBean, conn, nslq, l,
									sum, list, key, "", currentPage, pg,
									dataSourceBean.getType(), objParams);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						if (isTotal.booleanValue()) {
							if (currentPage == 1) {
								int trd = 0;
								if (pageSize > 0) {
									String nsqln = StringHelper
											.getCountSql(nslq);
									trd = pd.queryExecuterRecord(
											dataSourceBean, conn, nsqln,
											objParams);
								} else {
									trd = list.size();
								}
								if (trd > totalRecord)
									totalRecord = trd;
							} else {
								try {
									totalRecord = Integer.parseInt(request
											.getParameter("totalRecord"));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}

		Map mp = new HashMap();
		mp.put("list", list);
		mp.put("totalRecord", Integer.valueOf(totalRecord));

		mp.put("sum", sum);
		return mp;
	}

	public Map<String, Object> queryExecuter(Connection conn,
			List<String> dataSetslist, Map<String, Object> map,
			String parmsModel, JSONArray jsonArray, HashMap<String, Object> mm,
			int currentPage, int pageSize, Boolean isTotal, Map dicMap) {
		PreviewReportDao pd = new PreviewReportDao();
		if (pageSize == -2) {
			pageSize = 40;
		}
		List list = new ArrayList();
		int totalRecord = 0;

		List nodes = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		if ((parmsModel != null) && (!parmsModel.equals(""))) {
			try {
				Document document = DocumentHelper.parseText(parmsModel);
				Element rootElm = document.getRootElement();
				nodes = rootElm.elements("parm");
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		int kindex = 0;
		for (String key : map.keySet()) {
			if (dataSetslist.contains(key)) {
				Map queryMap = (Map) ((Map) map.get(key)).get("query");
				List FieldsList = (List) ((Map) map.get(key)).get("fields");
				String dataSourceName = queryMap.get("datasourcename")
						.toString();
				DataSourceBean dataSourceBean = new DataSourceBean();
				if (!DBUtil.isJndi().booleanValue()) {
					dataSourceBean.setDataSourceName(dataSourceName);
				}
				String sql = queryMap.get("commandtext").toString();
				List l = new ArrayList();
				for (Iterator localIterator2 = FieldsList.iterator(); localIterator2
						.hasNext();) {
					Object obj = localIterator2.next();
					String DataField = "";
					if (((Map) obj).get("datafield") != null) {
						DataField = ((Map) obj).get("datafield").toString();
					}
					Object FormatValue = "";
					if (((Map) obj).get("formatvalue") != null) {
						FormatValue = ((Map) obj).get("formatvalue");
					}
					Object FormatType = "";
					if (((Map) obj).get("formattype") != null) {
						FormatType = ((Map) obj).get("formattype");
					}
					Object DataType = "";
					if (((Map) obj).get("datatype") != null) {
						DataType = ((Map) obj).get("datatype");
					}
					Object datadic = "";
					if ((JRUtilNew.baseMap.get("dicdb") != null)
							&& (((String) JRUtilNew.baseMap.get("dicdb"))
									.equals("1"))) {
						if (dicMap.get(key + "." + DataField) != null) {
							datadic = JSON.toJSON(DBUtil.DicMap.get(dicMap
									.get(key + "." + DataField)));
						}
					} else if (((Map) obj).get("datadic") != null) {
						datadic = ((Map) obj).get("datadic").toString();
					}

					JSONObject ob = new JSONObject();
					ob.put("datafield", DataField);
					ob.put("formatvalue", FormatValue);
					ob.put("formattype", FormatType);
					ob.put("datatype", DataType);
					ob.put("datadic", datadic);
					l.add(ob);
					groupslist.add(DataField);
				}

				if ("javabean".equals(dataSourceName)) {
					totalRecord = jsonArray.size();
					int strf = (currentPage - 1) * pageSize;
					int strt = currentPage * pageSize;
					if (strt > totalRecord) {
						strt = totalRecord;
					}
					JSONArray jsa = new JSONArray();
					for (int i = strf; i < strt; i++) {
						jsa.add(jsonArray.get(i));
					}
					if (jsa.size() > 0)
						pd.queryFromObject(sql, l, list, key, jsa);
					else
						pd.queryFromObject(sql, l, list, key, jsonArray);
				} else {
					if (!DBUtil.isJndi().booleanValue()) {
						String realPath = BaseAction.realPath + "db-config.xml";
						dataSourceBean = XmlUtil.selectXML(dataSourceBean,
								realPath);
					}

					sql = sql.replaceAll("  ", " ");
					LinkedHashMap<String, Object> parameters = (LinkedHashMap) queryMap
							.get("parms");
					ArrayList objParamsList = new ArrayList();
					int ii = 0;
					int iii = 0;
					String[] sqlarr = sql.split("\\?");
					String nslq = sql;
					String sin = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTNVWXYZ0123456789-_";
					String dbtype;
					for (String k : parameters.keySet()) {
						Map parameter = (Map) parameters.get(k);
						Object value = request.getParameter(parameter.get(
								"name").toString());

						dbtype = "";
						if ((nodes != null) && (nodes.size() > 0)) {
							for (Iterator it = nodes.iterator(); it.hasNext();) {
								Element elm = (Element) it.next();
								if (parameter.get("name").equals(
										elm.attributeValue("name"))) {
									dbtype = elm.attributeValue("dbtype");
								}
							}
						}
						Boolean isw = Boolean.valueOf(false);
						if ((dbtype != null)
								&& (dbtype.length() > 0)
								&& ((dbtype.toLowerCase().equals("double")) || (dbtype
										.toLowerCase().equals("int")))
								&& (sqlarr[iii].length() > 4)
								&& (sin.indexOf(sqlarr[iii]
										.substring(sqlarr[iii].length() - 1)) != -1)) {
							isw = Boolean.valueOf(true);
						}

						if (isw.booleanValue()) {
							nslq = nslq.replace(sqlarr[iii] + "?", sqlarr[iii]
									+ String.valueOf(value));
						} else {
							if ((dbtype != null)
									&& (dbtype.length() > 0)
									&& ((dbtype.toLowerCase().equals("double")) || (dbtype
											.toLowerCase().equals("int"))))
								objParamsList.add("【" + value + "】");
							else {
								objParamsList.add(value);
							}
							ii++;
						}
						iii++;
					}

					Object[] objParams = objParamsList.toArray();

					if ((mm.size() > 0) && (sql.indexOf("?") != -1)) {
						String[] arrdtcol = null;
						int kkk = 0;
						for (String kk : mm.keySet()) {
							if (kkk == kindex) {
								if (mm.get(kk).toString().indexOf(",") != -1)
									arrdtcol = mm.get(kk).toString().split(",");
								else {
									arrdtcol = new String[] { mm.get(kk)
											.toString() };
								}
							}
							kkk++;
						}
						if (arrdtcol != null) {
							for (int i = 0; i < arrdtcol.length; i++) {
								if (i == 0)
									try {
										pd.queryExecuter(dataSourceBean, conn,
												nslq.replaceFirst("\\?",
														arrdtcol[i]), l, list,
												key, "", currentPage, pageSize,
												dataSourceBean.getType(),
												objParams);
									} catch (SQLException e) {
										e.printStackTrace();
									}
								else {
									try {
										pd.queryExecuter(dataSourceBean, conn,
												nslq.replaceFirst("\\?",
														arrdtcol[i]), l, list,
												key, "_" + i, currentPage,
												pageSize, dataSourceBean
														.getType(), objParams);
									} catch (SQLException e) {
										e.printStackTrace();
									}

								}

								if (isTotal.booleanValue()) {
									if (currentPage == 1) {
										int trd = 0;
										if (pageSize > 0) {
											String nsqln = StringHelper
													.getCountSql(nslq)
													.replaceFirst("\\?",
															arrdtcol[i]);
											trd = pd.queryExecuterRecord(
													dataSourceBean, conn,
													nsqln, objParams);
										} else {
											trd = list.size();
										}
										if (trd > totalRecord)
											totalRecord = trd;
									} else {
										try {
											totalRecord = Integer
													.parseInt(request
															.getParameter("totalRecord"));
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
						kindex++;
					} else {
						try {
							pd.queryExecuter(dataSourceBean, conn, nslq, l,
									list, key, "", currentPage, pageSize,
									dataSourceBean.getType(), objParams);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						if (isTotal.booleanValue()) {
							if (currentPage == 1) {
								int trd = 0;
								if (pageSize > 0) {
									String nsqln = StringHelper
											.getCountSql(nslq);
									trd = pd.queryExecuterRecord(
											dataSourceBean, conn, nsqln,
											objParams);
								} else {
									trd = list.size();
								}
								if (trd > totalRecord)
									totalRecord = trd;
							} else {
								try {
									totalRecord = Integer.parseInt(request
											.getParameter("totalRecord"));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}

		Map mp = new HashMap();
		mp.put("list", list);
		mp.put("totalRecord", Integer.valueOf(totalRecord));
		return mp;
	}

	public List<HashMap<String, Object>> queryExecuterNN(Connection conn,
			List<String> dataSetslist, Map<String, Object> map,
			String parmsModel) {
		PreviewReportDao pd = new PreviewReportDao();
		List list = new ArrayList();

		List nodes = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			Document document = DocumentHelper.parseText(parmsModel);
			Element rootElm = document.getRootElement();
			nodes = rootElm.elements("parm");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		for (String key : map.keySet()) {
			if (dataSetslist.contains(key)) {
				Map queryMap = (Map) ((Map) map.get(key)).get("query");
				List FieldsList = (List) ((Map) map.get(key)).get("fields");
				String dataSourceName = queryMap.get("datasourcename")
						.toString();
				DataSourceBean dataSourceBean = new DataSourceBean();
				if (!DBUtil.isJndi().booleanValue()) {
					dataSourceBean.setDataSourceName(dataSourceName);
				}
				String sql = queryMap.get("commandtext").toString();
				List l = new ArrayList();
				String DataField;
				for (Iterator localIterator2 = FieldsList.iterator(); localIterator2
						.hasNext();) {
					Object obj = localIterator2.next();
					DataField = "";
					if (((Map) obj).get("datafield") != null) {
						DataField = ((Map) obj).get("datafield").toString();
					}
					Object FormatValue = "";
					if (((Map) obj).get("formatvalue") != null) {
						FormatValue = ((Map) obj).get("formatvalue");
					}
					Object FormatType = "";
					if (((Map) obj).get("formattype") != null) {
						FormatType = ((Map) obj).get("formattype");
					}
					Object datadic = "";
					if (((Map) obj).get("datadic") != null) {
						datadic = ((Map) obj).get("datadic").toString();
					}
					JSONObject ob = new JSONObject();
					ob.put("datafield", DataField);
					ob.put("formatvalue", FormatValue);
					ob.put("formattype", FormatType);
					ob.put("datadic", datadic.toString());
					l.add(ob);
					groupslist.add(DataField);
				}
				if (!DBUtil.isJndi().booleanValue()) {
					String realPath = BaseAction.realPath + "db-config.xml";
					dataSourceBean = XmlUtil
							.selectXML(dataSourceBean, realPath);
				}

				sql = sql.replaceAll("  ", " ");

				LinkedHashMap<String, Object> parameters = (LinkedHashMap) queryMap
						.get("parms");
				for (String k : parameters.keySet()) {
					Map parameter = (Map) parameters.get(k);

					Object value = request.getParameter(parameter.get("name")
							.toString());

					String dbtype = "";
					if ((nodes != null) && (nodes.size() > 0)) {
						for (Iterator it = nodes.iterator(); it.hasNext();) {
							Element elm = (Element) it.next();
							if (parameter.get("name").equals(
									elm.attributeValue("name"))) {
								dbtype = elm.attributeValue("dbtype");
							}
						}
					}
					if ((!"double".equals(dbtype)) && (!"int".equals(dbtype))) {
						value = "'" + value + "'";
					}
					sql = sql.replaceFirst("\\?", (String) value)
							.replace(" '%'", " '%").replace("'%' ", "%' ")
							.replace("'null'", "null");
				}
				try {
					pd.queryExecuter(dataSourceBean, conn, sql, l, list, key,
							"", 0, 0, dataSourceBean.getType(), new Object[0]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public Map<String, List<HashMap<String, Object>>> queryExecuterN(
			Connection conn, List<String> dataSetslist,
			Map<String, Object> map, String parmsModel) {
		PreviewReportDao pd = new PreviewReportDao();

		List nodes = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			Document document = DocumentHelper.parseText(parmsModel);
			Element rootElm = document.getRootElement();
			nodes = rootElm.elements("parm");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Map dataMap = new HashMap();
		for (String key : map.keySet()) {
			if (dataSetslist.contains(key)) {
				List list = new ArrayList();
				Map queryMap = (Map) ((Map) map.get(key)).get("query");
				List<Map<String, Object>> FieldsList = (List) ((Map) map
						.get(key)).get("fields");
				DataSourceBean dataSourceBean = new DataSourceBean();
				if (!DBUtil.isJndi().booleanValue()) {
					String dataSourceName = queryMap.get("datasourcename")
							.toString();
					dataSourceBean.setDataSourceName(dataSourceName);
					String realPath = BaseAction.realPath + "db-config.xml";
					dataSourceBean = XmlUtil
							.selectXML(dataSourceBean, realPath);
				}

				String sql = queryMap.get("commandtext").toString();
				sql = sql.replaceAll("  ", " ");
				LinkedHashMap<String, Object> parameters = (LinkedHashMap) queryMap
						.get("parms");
				for (String k : parameters.keySet()) {
					Map parameter = (Map) parameters.get(k);
					String value = request.getParameter(parameter.get("name")
							.toString());
					if ((value == null) || (value.equals(""))) {
						value = "null";
					} else {
						String dbtype = "";
						if (nodes.size() > 0) {
							for (Iterator it = nodes.iterator(); it.hasNext();) {
								Element elm = (Element) it.next();
								if (parameter.get("name").equals(
										elm.attributeValue("name"))) {
									dbtype = elm.attributeValue("dbtype");
								}
							}
						}
						if ((!"double".equals(dbtype))
								&& (!"int".equals(dbtype))) {
							value = "'" + value + "'";
						}
					}
					sql = sql.replaceFirst("\\?", value);
				}
				List l = new ArrayList();
				// for ( Map<String,Object> parameter = FieldsList;
				// parameter.hasNext(); ) { Object obj = parameter.next();
				for (Map<String, Object> obj : FieldsList) {
					String DataField = "";
					if (((Map) obj).get("datafield") != null) {
						DataField = ((Map) obj).get("datafield").toString();
					}
					Object FormatValue = "";
					if (((Map) obj).get("formatvalue") != null) {
						FormatValue = ((Map) obj).get("formatvalue");
					}
					Object FormatType = "";
					if (((Map) obj).get("formattype") != null) {
						FormatType = ((Map) obj).get("formattype");
					}
					Object datadic = "";
					if (((Map) obj).get("datadic") != null) {
						datadic = ((Map) obj).get("datadic").toString();
					}
					JSONObject ob = new JSONObject();
					ob.put("datafield", DataField);
					ob.put("formatvalue", FormatValue);
					ob.put("formattype", FormatType);
					ob.put("datadic", datadic.toString());
					l.add(ob);
					groupslist.add(DataField);
				}
				try {
					pd.queryExecuter(dataSourceBean, conn, sql, l, list, key,
							"", 0, 0, dataSourceBean.getType(), new Object[0]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dataMap.put(key, list);
			}
		}

		return dataMap;
	}

	public void listNodesNew(Map<String, Object> maps,
			Map<String, Object> DataSetMap, Element node) {
		if (node.getName().equals("dataset")) {
			DataSetMap = new CaseInsensitiveMap();
			DataSetMap.put("dataset", node.attributeValue("name"));
		} else if (node.getName().equals("query")) {
			DataSetMap.put("query", new HashMap());
		} else if (node.getName().equals("datasourcename")) {
			((Map) DataSetMap.get("query")).put("datasourcename",
					node.getText());
		} else if (node.getName().equals("commandtext")) {
			((Map) DataSetMap.get("query")).put("commandtext", node.getText());
		} else if (node.getName().equals("parameters")) {
			((Map) DataSetMap.get("query")).put("parms", new LinkedHashMap());
		} else if (node.getName().equals("parameter")) {
			LinkedHashMap qmap = (LinkedHashMap) ((Map) DataSetMap.get("query"))
					.get("parms");
			Map map = new CaseInsensitiveMap();
			map.put("name", node.attributeValue("name"));
			map.put("dbType", node.attributeValue("dbtype"));
			qmap.put(qmap.size(), map);
		} else if (node.getName().equals("fields")) {
			DataSetMap.put("fields", new CaseInsensitiveMap());
		} else if (node.getName().equals("field")) {
			Map map = (Map) DataSetMap.get("fields");
			Map map2 = new CaseInsensitiveMap();
			map2.put("datatype", node.element("datatype").getData());
			if (node.element("datadic") != null) {
				map2.put("datadic", node.element("datadic").getData());
			}
			if (node.attribute("dicType") != null) {
				map2.put("dictype", node.attributeValue("dicType"));
			}
			map.put(node.attributeValue("name"), map2);
			DataSetMap.put("fields", map);
		}

		if (DataSetMap.get("dataset") != null) {
			maps.put((String) DataSetMap.get("dataset"), DataSetMap);
		}
		Iterator iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = (Element) iterator.next();
			listNodesNew(maps, DataSetMap, e);
		}
	}

	public void listNodes(Map<String, Object> maps,
			Map<String, Object> DataSetMap, Element node) {
		if (node.getName().equals("dataset")) {
			DataSetMap = new HashMap();
			DataSetMap.put("dataset", node.attributeValue("name"));
		} else if (node.getName().equals("query")) {
			DataSetMap.put("query", new HashMap());
		} else if (node.getName().equals("datasourcename")) {
			((Map) DataSetMap.get("query")).put("datasourcename",
					node.getText());
		} else if (node.getName().equals("commandtext")) {
			((Map) DataSetMap.get("query")).put("commandtext", node.getText());
		} else if (node.getName().equals("parameters")) {
			((Map) DataSetMap.get("query")).put("parms", new LinkedHashMap());
		} else if (node.getName().equals("parameter")) {
			LinkedHashMap qmap = (LinkedHashMap) ((Map) DataSetMap.get("query"))
					.get("parms");
			Map map = new HashMap();
			map.put("name", node.attributeValue("name"));
			map.put("dbType", node.attributeValue("dbtype"));
			qmap.put(qmap.size(), map);
		} else if (node.getName().equals("fields")) {
			DataSetMap.put("fields", new ArrayList());
		} else if (node.getName().equals("datafield")) {
			Map map = new HashMap();

			map.put("datafield", node.getText());
			map.put("formatvalue", node.attributeValue("formatvalue"));
			map.put("formattype", node.attributeValue("formattype"));
			((List) DataSetMap.get("fields")).add(map);
		} else if ((node.getName().equals("dataType"))
				|| (node.getName().equals("datatype"))) {
			List list = (List) DataSetMap.get("fields");
			((Map) list.get(list.size() - 1)).put("datatype", node.getText());
		} else if (node.getName().equals("datadic")) {
			List list = (List) DataSetMap.get("fields");
			((Map) list.get(list.size() - 1)).put("datadic", node.getText());
		}

		if (DataSetMap.get("dataset") != null) {
			maps.put((String) DataSetMap.get("dataset"), DataSetMap);
		}
		Iterator iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = (Element) iterator.next();
			listNodes(maps, DataSetMap, e);
		}
	}

	public Map<String, Object> compileParms(String parms) {
		if ((parms == null) || (parms.equals("")))
			return null;
		Map map = new HashMap();
		try {
			Document parmsDoc = DocumentHelper.parseText(parms);

			Element parmRoot = parmsDoc.getRootElement();
			Iterator iterator = parmRoot.elementIterator();
			while (iterator.hasNext()) {
				Element node = (Element) iterator.next();

				map.put(node.attributeValue("name"), node);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}

	public Map<String, Object> compileTableReport(String tableModel) {
		try {
			Document tableDesign = DocumentHelper.parseText(tableModel);
			Map map = new HashMap();

			return convertTableMap(map);
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Map<String, Object> compileTableReportN(String tableModel) {
		try {
			topCount = 0;
			Document tableDesign = DocumentHelper.parseText(tableModel);
			Map map = new HashMap();
			listTableNodesN(map, tableDesign.getRootElement(), null);
			return convertTableMapN(map);
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Map<String, Object> convertTableMap(Map<String, Object> map) {
		List list = (List) map.get("dataSets");
		List dl = new ArrayList();
		List gl = new ArrayList();
		Iterator localIterator;
		if (list.size() != 0) {
			for (localIterator = list.iterator(); localIterator.hasNext();) {
				Object obj = localIterator.next();
				String str = ((Map) obj).get("header").toString();
				if (str.equals(""))
					continue;
				try {
					dl.add(str.substring(1, str.indexOf(".")));
					Map m = (Map) obj;
					m.put("header", str.substring(str.indexOf(".") + 1));
					gl.add(m);
				} catch (Exception e) {
					Map m = (Map) obj;
					m.put("header", str);
					gl.add(m);
				}
			}
		} else {
			list = (List) map.get("header");
			for (localIterator = list.iterator(); localIterator.hasNext();) {
				Object obj = localIterator.next();
				for (Iterator e = ((List) obj).iterator(); e.hasNext();) {
					Object ob = e.next();
					String str = ((Map) ob).get("header").toString();

					Map m = (Map) ob;

					gl.add(m);
				}
			}
		}

		dl = new ArrayList(new HashSet(dl));
		map.put("dataSets", dl);
		map.put("groups", gl);
		return map;
	}

	public Map<String, Object> convertTableMapN(Map<String, Object> map) {
		List list = (List) map.get("dataSets");
		List dl = new ArrayList();
		List gl = new ArrayList();
		if ((list != null) && (list.size() != 0)) {
			for (Iterator localIterator = list.iterator(); localIterator
					.hasNext();) {
				Object obj = localIterator.next();
				String str = ((Map) obj).get("header").toString();
				dl.add(str.substring(1, str.indexOf(".")));
				if (str.startsWith("%")) {
					Map m = new HashMap();
					m.put("header", str.substring(str.indexOf(".") + 1));
					m.put("style", ((Map) obj).get("style"));
					gl.add(m);
				}

			}

		}

		dl = new ArrayList(new HashSet(dl));
		map.put("dataSets", dl);
		map.put("groups", gl);
		return map;
	}

	public void getdicList(Element node, Map dicMap) {
		for (Iterator childNode = node.elementIterator(); childNode.hasNext();) {
			Element eChild = (Element) childNode.next();
			String txt = eChild.getText();
			if (("".equals(txt)) || (eChild.attribute("dic") == null)
					|| ("".equals(eChild.attribute("dic").getValue())))
				continue;
			if (txt.substring(0, 1).equals("=")) {
				txt = txt.substring(1);
			}
			if (eChild.attribute("dic").getValue().indexOf("=") != -1)
				dicMap.put(txt,
						eChild.attribute("dic").getValue().split("\\=")[1]);
			else {
				dicMap.put(txt, eChild.attribute("dic").getValue());
			}

		}

		Iterator iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = (Element) iterator.next();
			getdicList(e, dicMap);
		}
	}

	public String[] listTableNode(String tableType, Element node,
			String[] backStr, boolean flag) {
		if (tableType.equals(node.attributeValue("category"))) {
			int tmp18_17 = 0;
			String[] tmp18_16 = backStr;
			tmp18_16[tmp18_17] = (tmp18_16[tmp18_17] + "<tr>");
			for (Iterator childNode = node.elementIterator(); childNode
					.hasNext();) {
				String styleStr = "";
				String styleUtil = "";
				String tdStr = "";
				String expText = "";
				String extParm = "";
				Element eChild = (Element) childNode.next();
				if ((!"".equals(eChild.getText()))
						|| (eChild.attribute("link").getValue().length() > 0)) {
					List<Attribute> list = eChild.attributes();
					for (Attribute attribute : list) {
						if (isStyle(attribute.getName())) {
							if ((!flag)
									&& ("height".equals(attribute.getName()))) {
								backStr[1] = attribute.getValue().split("px")[0];
								flag = true;
							}
							styleStr = styleStr
									+ getTDStyle(attribute.getName(),
											attribute.getValue());
							if (!"border".equals(attribute.getName()))
								styleUtil = styleUtil
										+ getTDStyle(attribute.getName(),
												attribute.getValue());
							try {
								extParm = extParm
										+ getBDParm(attribute.getName(),
												attribute.getValue());
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						} else {
							String tempValue = attribute.getValue();
							if (("null".equals(tempValue))
									|| (tempValue == null)
									|| ("".equals(tempValue)))
								tempValue = "";
							if ((attribute.getName().equals("link"))
									&& (!tempValue.equals(""))
									&& (tempValue.indexOf(":") != -1))
								tdStr = tdStr
										+ " class='link' onclick='openlink(\""
										+ tempValue.replace("uuid:", "uid:")
										+ "\")' ";
							else if ((attribute.getName().equals("frameid"))
									&& (!tempValue.equals("")))
								tdStr = tdStr + " class='frameid' id=\"frm_"
										+ tempValue + "\"";
							else {
								tdStr = tdStr + attribute.getName() + "='"
										+ tempValue + "' ";
							}
						}
						if (("expText".equals(attribute.getName()))
								&& (attribute.getValue().length() > 0)) {
							expText = attribute.getValue();
						}
					}

					String eTex = "";
					String neTex = eChild.getStringValue();

					boolean v = false;
					if ((neTex.indexOf("＋") != -1) && (neTex.startsWith("="))) {
						v = true;
						eTex = neTex.split("＋")[0];
					} else {
						eTex = neTex;
					}
					if ((!expText.equals("")) && (expText != null)) {
						expText = ReportElementUtil.getRplcExp(expText);

						while (expText.indexOf("$F{") != -1) {
							if (expText.substring(expText.indexOf("$F{"),
									expText.indexOf("}") + 1).indexOf('.') == -1) {
								expText = expText
										.replace(
												expText.substring(
														expText.indexOf("$F{"),
														expText.indexOf("}") + 1),
												"\"<"
														+ eTex.split("\\.")[0]
														+ "."
														+ expText.substring(
																expText.indexOf("$F{") + 3,
																expText.indexOf("}"))
														+ ">\"");
								continue;
							}
							expText = expText.replace(
									expText.substring(expText.indexOf("$F{"),
											expText.indexOf("}") + 1),
									"\"<="
											+ expText.substring(
													expText.indexOf("$F{") + 3,
													expText.indexOf("}"))
											+ ">\"");
						}

						try {
							eTex = "{{" + expText + "}}";
						} catch (Exception localException) {
						}
					} else if ((eTex.indexOf("{#=") == -1)
							&& (neTex.startsWith("="))) {
						eTex = "<" + eTex + ">";
					} else {
						eTex = eTex.replaceAll("\\{\\#", "<").replaceAll(
								"\\#\\}", ">");
					}

					if (v) {
						eTex = eTex + neTex.split("＋")[1];
					}

					if ("dataarea".equals(tableType)) {
						int tmp1087_1086 = 0;
						String[] tmp1087_1085 = backStr;
						tmp1087_1085[tmp1087_1086] = (tmp1087_1085[tmp1087_1086]
								+ "<td "
								+ tdStr
								+ "style=\""
								+ styleStr.replace("\"", "") + "\">" + eTex + "</td>");
						report.java.jrreport.util.SelectUtil.styleString = styleUtil
								.replace("\"", "");
					} else {
						String eStr = eChild.getText();
						if ((!"".equals(eStr))
								&& ((eStr.indexOf("=") != -1) || (((eStr
										.indexOf("$V{") != -1) || (eStr
										.indexOf("subreport:") != -1)) && (!""
										.equals(expText))))) {
							int tmp1234_1233 = 0;
							String[] tmp1234_1232 = backStr;
							tmp1234_1232[tmp1234_1233] = (tmp1234_1232[tmp1234_1233]
									+ "<td "
									+ tdStr
									+ "style=\""
									+ styleStr.replace("\"", "") + "\">" + eTex + "</td>");
						} else {
							if ((tableType.equals("reporthead"))
									&& (eStr.indexOf(",") != -1))
								try {
									eStr = "<img src=\"../../imageLine?str="
											+ URLEncoder.encode(eStr, "utf-8")
											+ extParm + "\">";
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
							int tmp1372_1371 = 0;
							String[] tmp1372_1370 = backStr;
							tmp1372_1370[tmp1372_1371] = (tmp1372_1370[tmp1372_1371]
									+ "<td "
									+ tdStr
									+ "style=\""
									+ styleStr.replace("\"", "") + "\">" + eStr + "</td>");
						}
					}
				}
			}
			int tmp1448_1447 = 0;
			String[] tmp1448_1446 = backStr;
			tmp1448_1446[tmp1448_1447] = (tmp1448_1446[tmp1448_1447] + "</tr>");
		}

		Iterator iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = (Element) iterator.next();
			backStr = listTableNode(tableType, e, backStr, flag);
		}
		return backStr;
	}

	public LinkedHashMap getDocMap(Document tableDesign,
			Map<String, Object> dataSetMap) {
		LinkedHashMap map = new LinkedHashMap();
		LinkedHashMap rowmap = null;
		LinkedHashMap colmap = null;
		Element elem = tableDesign.getRootElement();
		Map cellShow = new HashMap();
		int rc = 0;
		Iterator itl = elem.elementIterator();
		while (itl.hasNext()) {
			Element rows = (Element) itl.next();
			Iterator itcs = rows.elementIterator();
			int jc = 0;
			while (itcs.hasNext()) {
				Element cols = (Element) itcs.next();
				if ((cols.attributeValue("colspan") != null)
						&& (!cols.attributeValue("colspan").equals("0"))
						&& (!cols.attributeValue("colspan").equals("1"))) {
					for (int l = 1; l < Integer.parseInt(cols
							.attributeValue("colspan")); l++) {
						cellShow.put(rc + "_" + (jc + l),
								Boolean.valueOf(false));
					}
				}
				if ((cols.attributeValue("rowspan") != null)
						&& (!cols.attributeValue("rowspan").equals("0"))
						&& (!cols.attributeValue("rowspan").equals("1"))) {
					for (int ll = 1; ll < Integer.parseInt(cols
							.attributeValue("rowspan")); ll++) {
						cellShow.put(rc + ll + "_" + jc, Boolean.valueOf(false));
						if ((cols.attributeValue("colspan") != null)
								&& (!cols.attributeValue("colspan").equals("0"))
								&& (!cols.attributeValue("colspan").equals("1"))) {
							for (int l = 1; l < Integer.parseInt(cols
									.attributeValue("colspan")); l++) {
								cellShow.put(rc + ll + "_" + (jc + l),
										Boolean.valueOf(false));
							}
						}
					}
				}
				if (cellShow.get(rc + "_" + jc) == null) {
					cellShow.put(rc + "_" + jc, Boolean.valueOf(true));
				}
				jc++;
			}
			rc++;
		}
		int j = 0;
		Iterator it = elem.elementIterator();
		while (it.hasNext()) {
			j++;
			Element row = (Element) it.next();
			rowmap = new LinkedHashMap();
			rowmap.put("category", row.attributeValue("category"));
			int i = 0;
			int c = 0;
			Iterator itc = row.elementIterator();
			while (itc.hasNext()) {
				Element col = (Element) itc.next();
				if ((cellShow.get(j - 1 + "_" + c) != null)
						&& (((Boolean) cellShow.get(j - 1 + "_" + c))
								.booleanValue())) {
					i++;
					colmap = new LinkedHashMap();
					String text = col.getText();
					String dynamic = "0";
					if ((text.startsWith("="))
							&& (StringUtils.indexOf(text, ".") != -1)) {
						if (StringUtils.indexOf(text, "＋") != -1) {
							String[] textArr = StringUtils.split(col.getText(),
									"＋");

							text = StringUtils.substring(textArr[0], 1);
							dynamic = "1";
						} else {
							text = StringUtils.substring(col.getText(), 1);
							dynamic = "1";
						}

						if (dynamic.equals("1")) {
							String[] arr = StringUtils.split(text, ".");
							String datatype = JRUtilNew.getKeyFromDatasetMap(
									dataSetMap, arr[0], arr[1], "datatype");
							if (Tools.isNum(datatype).booleanValue()) {
								colmap.put("isnum", "1");
							}
						}
					}
					if ((text.startsWith("$V{")) && (text.endsWith("}"))) {
						dynamic = "1";
					}

					if ((StringUtils.indexOf(text, "{#=") != -1)
							&& (StringUtils.indexOf(text, "#}") != -1)) {
						text = StringUtils.replace(text, "{#=", "$F{");
						text = StringUtils.replace(text, "#}", "}");
						dynamic = "1";
						Pattern r = Pattern.compile("(\\$F\\{[\\w\\-\\.]+})");
						Matcher m = r.matcher(text);
						while (m.find()) {
							text = text.substring(0, m.start())
									+ text.substring(m.start(), m.end())
											.toUpperCase()
									+ text.substring(m.end());
						}
					}
					colmap.put("dynamic", dynamic);
					colmap.put("text", text);
					List<Attribute> colAttrs = col.attributes();
					StringBuilder styleStr = new StringBuilder();
					Integer colspan = Integer.valueOf(1);
					for (Attribute cattr : colAttrs) {
						if (cattr.getName().equals("colspan")) {
							colspan = Integer.valueOf(Integer.parseInt(cattr
									.getValue()));
						}
					}
					for (Attribute cattr : colAttrs) {
						if (isStyle(cattr.getName())) {
							styleStr.append(getTDStyle(cattr.getName(),
									StringUtils.replace(cattr.getValue(), "\"",
											"")));
						} else {
							String tempValue = cattr.getValue();
							if (("null".equals(tempValue))
									|| (tempValue == null)
									|| ("".equals(tempValue)))
								tempValue = "";
							if (tempValue.length() > 0) {
								if (cattr.getName().equals("link")) {
									colmap.put("class", "link");
									colmap.put(
											"onclick",
											"openlink(\""
													+ tempValue.replace(
															"uuid:", "uid:")
													+ "\")");
								} else if (cattr.getName().equals("frameid")) {
									colmap.put("class", "frameid");
									colmap.put("frameid", tempValue);
								} else {
									colmap.put(cattr.getName(), tempValue);
								}
							}
						}
						if (("expText".equals(cattr.getName()))
								&& (cattr.getValue().length() > 0)) {
							String exp = cattr.getValue();
							if (exp.startsWith("group(")) {
								colmap.put("group", "1");
							} else {
								Pattern r = Pattern
										.compile("(\\$[FV]{1}\\{[\\w\\-\\.]+})");
								Matcher m = r.matcher(exp);
								while (m.find()) {
									exp = exp.substring(0, m.start())
											+ exp.substring(m.start(), m.end())
													.toUpperCase()
											+ exp.substring(m.end());
								}
								colmap.put("expText",
										ReportElementUtil.getRplcExp(exp));
							}
						}
					}
					colmap.put("style", styleStr.toString());
					rowmap.put("td-" + i, colmap);
				}
				c++;
			}
			map.put("tr-" + j, rowmap);
		}
		return map;
	}

	public LinkedHashMap resetDocMap(Map docMap, Map<String, Object> dtlMap,
			Map<String, Integer> loopMap) {
		LinkedHashMap map = new LinkedHashMap();
		LinkedHashMap rowmap = null;
		LinkedHashMap colmap = null;
		int sumcol = 0;
		int grpindex = 0;
		for (String k : loopMap.keySet()) {
			if (dtlMap.get(k) != null) {
				sumcol += ((Integer) ((Map) dtlMap.get(k)).get("colspan"))
						.intValue()
						* (((Integer) loopMap.get(k)).intValue() - 1);
				grpindex = ((Integer) ((Map) dtlMap.get(k)).get("rowindex"))
						.intValue();
			}
		}
		Iterator it = docMap.entrySet().iterator();
		int rowindex = 0;
		Map rowColMap = new HashMap();
		LinkedHashMap<String, Object> lcolMap = new LinkedHashMap();
		while (it.hasNext()) {
			rowmap = new LinkedHashMap();
			Map.Entry e = (Map.Entry) it.next();
			Map lr = (Map) e.getValue();
			String category = lr.get("category").toString();
			rowmap.put("category", category);
			Iterator itr = lr.entrySet().iterator();
			int colindex = 0;
			int oldcolindex = 0;
			while (itr.hasNext()) {
				Map.Entry trm = (Map.Entry) itr.next();
				if (trm.getKey().toString().startsWith("td-")) {
					Map tdm = (Map) trm.getValue();
					int colspan = tdm.get("colspan") != null ? Integer
							.parseInt(tdm.get("colspan").toString()) : 1;
					if (rowColMap
							.get(String.valueOf(rowindex) + "-" + colindex) != null) {
						colindex += ((Integer) rowColMap.get(String
								.valueOf(rowindex) + "-" + colindex))
								.intValue();
					}
					int precolindex = colindex;
					int preoldcolindex = oldcolindex;
					if (tdm.get("colspan") != null) {
						colindex += Integer.parseInt(tdm.get("colspan")
								.toString());
						oldcolindex += Integer.parseInt(tdm.get("colspan")
								.toString());
					} else {
						colindex++;
						oldcolindex++;
					}
					if (tdm.get("rowspan") != null) {
						int rowspan = Integer.parseInt(tdm.get("rowspan")
								.toString());
						if (rowspan > 1) {
							for (int i = 1; i < rowspan; i++) {
								rowColMap.put(String.valueOf(rowindex + 1)
										+ "-" + precolindex,
										Integer.valueOf(colspan));
							}
						}
					}
					if (rowindex < grpindex) {
						if ((tdm.get("colspan") == null)
								|| (tdm.get("colspan").toString().equals("0")))
							tdm.put("colspan", Integer.valueOf(1 + sumcol));
						else {
							tdm.put("colspan",
									Integer.valueOf(Integer.parseInt(tdm.get(
											"colspan").toString())
											+ sumcol));
						}
						String style = tdm.get("style").toString();
						if (style.indexOf(";height:") != -1) {
							tdm.put("style",
									"height:" + style.split(";height:")[1]);
						}
					}
					colmap = new LinkedHashMap();
					Iterator itd = tdm.entrySet().iterator();
					while (itd.hasNext()) {
						Map.Entry tdd = (Map.Entry) itd.next();
						if ((category.equals("reporthead"))
								&& (tdd.getKey().equals("expText"))
								&& (tdd.getValue().toString()
										.startsWith("new report.java.jrreport.util.JRFunExpImpl().gpc(")))
							continue;
						colmap.put(tdd.getKey(), tdd.getValue());
					}

					rowmap.put(trm.getKey(), colmap);
					if (rowindex >= grpindex) {
						for (String kk : dtlMap.keySet()) {
							Map mkMap = (Map) dtlMap.get(kk);
							if ((precolindex >= ((Integer) mkMap
									.get("colindex")).intValue())
									&& (precolindex <= ((Integer) mkMap
											.get("colindex")).intValue()
											+ ((Integer) mkMap.get("colspan"))
													.intValue() - 1)) {
								for (int n = 0; n < ((Integer) loopMap.get(kk))
										.intValue() - 1; n++) {
									Iterator nitd = colmap.entrySet()
											.iterator();
									LinkedHashMap ncolmap = new LinkedHashMap();
									String tdv;
									Matcher m;
									while (nitd.hasNext()) {
										Map.Entry tdd = (Map.Entry) nitd.next();
										if ((category.equals("reporthead"))
												&& (tdd.getKey()
														.equals("expText"))
												&& (tdd.getValue().toString()
														.startsWith("new report.java.jrreport.util.JRFunExpImpl().gpc(")))
											continue;
										if ((tdd.getKey().toString()
												.equals("text"))
												&& (colmap.get("dynamic")
														.equals("1"))) {
											if ((category.equals("reportfoot"))
													&& (tdd.getValue()
															.toString()
															.startsWith("$V{"))
													&& (tdd.getValue()
															.toString()
															.endsWith("}")))
												ncolmap.put(
														tdd.getKey(),
														tdd.getValue()
																.toString()
																.substring(
																		0,
																		tdd.getValue()
																				.toString()
																				.length() - 1)
																+ "_"
																+ (n + 1)
																+ "}");
											else {
												ncolmap.put(tdd.getKey(),
														tdd.getValue() + "_"
																+ (n + 1));
											}
										} else if ((tdd.getKey().toString()
												.equals("expText"))
												&& (colmap.get("dynamic")
														.equals("1"))) {
											tdv = tdd.getValue().toString();
											Pattern r = Pattern
													.compile("(\\$F\\{[\\w\\-\\.]+})");
											m = r.matcher(tdv);
											while (m.find()) {
												tdv = tdv
														.replace(
																tdv.substring(
																		m.start(),
																		m.end()),
																tdv.substring(
																		m.start(),
																		m.end())
																		.replace(
																				"}",
																				"_"
																						+ (n + 1)
																						+ "}"));
											}
											ncolmap.put(tdd.getKey(), tdv);
										} else {
											ncolmap.put(tdd.getKey(),
													tdd.getValue());
										}

									}

									if ((((Integer) mkMap.get("colspan"))
											.intValue() > 1)
											&& (rowindex > grpindex)) {
										if (lcolMap.get(String.valueOf(n + 1)) == null) {
											LinkedHashMap ll = new LinkedHashMap();
											ll.put(trm.getKey() + "_" + (n + 1),
													ncolmap);
											lcolMap.put(String.valueOf(n + 1),
													ll);
										} else {
											LinkedHashMap ll = (LinkedHashMap) lcolMap
													.get(String.valueOf(n + 1));
											ll.put(trm.getKey() + "_" + (n + 1),
													ncolmap);
											lcolMap.put(String.valueOf(n + 1),
													ll);
										}
										if ((colindex == ((Integer) mkMap
												.get("colindex")).intValue()
												+ ((Integer) mkMap
														.get("colspan"))
														.intValue())
												&& (lcolMap.size() > 0)) {
											for (String kkk : lcolMap.keySet()) {
												for (String kkkk : ((LinkedHashMap<String, Object>) lcolMap
														.get(kkk)).keySet()) {
													rowmap.put(
															kkkk,
															((LinkedHashMap) lcolMap
																	.get(kkk))
																	.get(kkkk));
												}
											}
											lcolMap.clear();
										}
									} else {
										rowmap.put(
												trm.getKey() + "_" + (n + 1),
												ncolmap);
									}
								}
							}
						}
					}
				}
			}

			map.put(e.getKey(), rowmap);
			rowindex++;
		}

		return map;
	}

	public void listTableNodeN(String tableType, Element node,
			Map<Integer, String[]> map) {
		boolean flag = true;
		String[] heightArray = { "", "", "" };
		if (tableType.equals(node.attributeValue("category"))) {
			int tmp43_42 = 0;
			String[] tmp43_40 = heightArray;
			tmp43_40[tmp43_42] = (tmp43_40[tmp43_42] + "<tr>");
			for (Iterator childNode = node.elementIterator(); childNode
					.hasNext();) {
				String styleStr = "";
				String tdStr = "";
				String expText = "";
				Element eChild = (Element) childNode.next();
				if (!"".equals(eChild.getText())) {
					List<Attribute> list = eChild.attributes();
					for (Attribute attribute : list) {
						if (isStyle(attribute.getName())) {
							if ((flag)
									&& ("height".equals(attribute.getName()))) {
								heightArray[1] = attribute.getValue().split(
										"px")[0];
								flag = false;
							}

							if ((map.size() == 1)
									&& ("colwidth".equals(attribute.getName()))) {
								int tmp235_234 = 2;
								String[] tmp235_232 = heightArray;
								tmp235_232[tmp235_234] = (tmp235_232[tmp235_234] + attribute
										.getValue().split("px")[0]);
							}
							styleStr = styleStr
									+ getTDStyle(attribute.getName(),
											attribute.getValue());
						} else {
							String tempValue = attribute.getValue();
							if (("null".equals(tempValue))
									|| (tempValue == null)
									|| ("".equals(tempValue)))
								tempValue = "''";
							if ((attribute.getName().equals("link"))
									&& (!tempValue.equals(""))
									&& (tempValue.indexOf(":") != -1))
								tdStr = tdStr
										+ "onclick='window.open(\"designPreviewIndex.jsp?reporttype=D&"
										+ tempValue.replace("uuid:", "uid:")
												.replaceAll(":", "=")
												.replaceAll(",", "&") + "\")' ";
							else if ((attribute.getName().equals("frameid"))
									&& (!tempValue.equals("")))
								tdStr = tdStr + " class='frameid' id='\""
										+ tempValue + "\")' ";
							else {
								tdStr = tdStr + attribute.getName() + "="
										+ tempValue + " ";
							}
						}
						if (("expText".equals(attribute.getName()))
								&& (attribute.getValue().length() > 0)) {
							expText = attribute.getValue();
						}
					}

					String eTex = eChild.getText();
					if ((!expText.equals("")) && (expText != null)) {
						try {
							if (expText.indexOf('.') == -1) {
								eTex = "$F{"
										+ expText
												.replace(
														"$F{",
														new StringBuilder("<")
																.append(eTex
																		.split("\\.")[0])
																.append(".")
																.toString())
												.split("}")[0].trim()
										+ ">"
										+ expText.replace(
												"$F{",
												new StringBuilder("<").append(
														eTex.split("\\.")[0])
														.toString()).split("}")[1]
										+ "}";
								break;
							}
							eTex = "$F{"
									+ expText.replace("$F{", "<=").split("}")[0]
											.trim()
									+ ">"
									+ expText.replace(
											"$F{",
											new StringBuilder("<").append(
													eTex.split("\\.")[0])
													.toString()).split("}")[1]
									+ "}";
						} catch (Exception localException1) {
						}

					} else if (eTex.indexOf("＋") != -1) {
						eTex = "<" + eTex.split("＋")[0] + ">"
								+ eTex.split("＋")[1];
					} else if (eTex.indexOf("{#=") == -1)
						eTex = "<" + eTex + ">";
					else {
						eTex = eTex.replaceAll("\\{\\#", "<").replaceAll(
								"\\#\\}", ">");
					}

					 if ("dataarea".equals(tableType)) {
						int tmp1015_1014 = 0;
						String[] tmp1015_1012 = heightArray;
						tmp1015_1012[tmp1015_1014] = (tmp1015_1012[tmp1015_1014]
								+ "<td "
								+ tdStr
								+ "style=\""
								+ styleStr.replace("\"", "") + "\">" + eTex + "</td>");
					} else {
						String eStr = eChild.getText();
						if ((!"".equals(eStr)) && (eStr.indexOf("=") != -1)) {
							int tmp1116_1115 = 0;
							String[] tmp1116_1113 = heightArray;
							tmp1116_1113[tmp1116_1115] = (tmp1116_1113[tmp1116_1115]
									+ "<td "
									+ tdStr
									+ "style=\""
									+ styleStr.replace("\"", "") + "\">" + eTex + "</td>");
						} else {
							int tmp1186_1185 = 0;
							String[] tmp1186_1183 = heightArray;
							tmp1186_1183[tmp1186_1185] = (tmp1186_1183[tmp1186_1185]
									+ "<td "
									+ tdStr
									+ "style=\""
									+ styleStr.replace("\"", "") + "\">" + eStr + "</td>");
						}
					}
				}
			}
			int tmp1263_1262 = 0;
			String[] tmp1263_1260 = heightArray;
			tmp1263_1260[tmp1263_1262] = (tmp1263_1260[tmp1263_1262] + "</tr>");
			Object[] obj = map.keySet().toArray();
			Arrays.sort(obj);
			map.put(Integer.valueOf(((Integer) obj[(obj.length - 1)])
					.intValue() + 1), heightArray);
		}

		Iterator iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = (Element) iterator.next();
			listTableNodeN(tableType, e, map);
		}
	}

	private boolean isStyle(String styleName) {
		String styleArr = "rowspan,colspan,relatedid,link,frameid,dic,base64,image,bgcolors";

		return !styleArr.contains(styleName);
	}

	private String getTDStyle(String attrName, String attrValue) {
		String[] tempArr = { "border", "font-color", "background-color",
				"colwidth", "height", "font-family", "font-size",
				"font-weight", "font-style", "text-decoration",
				"vertical-align", "text-align", "white-space" };
		String returnStr = "";

		if (Arrays.asList(tempArr).contains(attrName)) {
			if ("border".equals(attrName)) {
				if (!"".equals(attrValue)) {
					returnStr = attrValue
							.replaceAll("&quot;", "")
							.replace("leftColor:", "border-left:1px solid ")
							.replace("topColor:", "border-top:1px solid ")
							.replace("rightColor:", "border-right:1px solid ")
							.replace("bottomColor:", "border-bottom:1px solid ")
							.replace("{", "").replace("}", ";")
							.replaceAll("204", "255").replaceAll(",b", ";b");
				}

			} else if ("font-color".equals(attrName))
				returnStr = "color:" + attrValue + ";";
			else if (("white-space".equals(attrName))
					&& (attrValue.equals("normal")))
				returnStr = "word-break:break-all;";
			else if ("colwidth".equals(attrName))
				returnStr = "width:" + attrValue + ";";
			else if (("background-color".equals(attrName))
					&& ("transparent".equals(attrValue)))
				returnStr = "";
			else
				returnStr = attrName + ":" + attrValue + ";";
		}
		return returnStr;
	}

	private String getBDParm(String attrName, String attrValue)
			throws UnsupportedEncodingException {
		String[] tempArr = { "font-color", "background-color", "colwidth",
				"height", "font-family", "font-size", "font-weight" };
		String returnStr = "";
		if (Arrays.asList(tempArr).contains(attrName)) {
			if ("colwidth".equals(attrName))
				returnStr = "&width=" + URLEncoder.encode(attrValue, "utf-8");
			else if (("background-color".equals(attrName))
					&& ("transparent".equals(attrValue)))
				returnStr = "";
			else
				returnStr = "&" + attrName + "="
						+ URLEncoder.encode(attrValue, "utf-8");
		}
		return returnStr;
	}

	public void listTableNodesN(Map<String, Object> map, Element node,
			String flag) {
		if (node.getName().equals("row")) {
			if ("headtitle".equals(node.attributeValue("category"))) {
				flag = "title";
			} else if ("reporthead".equals(node.attributeValue("category"))) {
				if (topCount > 0) {
					if (map.get("footer") == null) {
						map.put("footer", new ArrayList());
					}
					List list = (List) map.get("footer");
					list.add(new ArrayList());
					flag = "footer";
				} else {
					if (map.get("header") == null) {
						map.put("header", new ArrayList());
					}
					List list = (List) map.get("header");
					list.add(new ArrayList());
					flag = "header";
				}
			} else if ("dataarea".equals(node.attributeValue("category"))) {
				if (map.get("dataSets") == null) {
					map.put("dataSets", new ArrayList());
				}
				List list = (List) map.get("header");
				list.add(new ArrayList());
				flag = "dataSets";
			}
		} else if (node.getName().equals("col")) {
			List<Attribute> list = node.attributes();
			if ((flag.equals("title")) && (!node.getTextTrim().equals(""))) {
				Map styMap = new HashMap();
				Map styleMap = new HashMap();
				for (Attribute attribute : list) {
					styleMap.put(attribute.getName(), attribute.getValue());
				}
				styMap.put("style", styleMap);
				styMap.put("title", node.getText());
				map.put("title", styMap);
			} else if ((flag.equals("header"))
					&& (!node.getTextTrim().equals(""))) {
				Map styMap = new HashMap();
				Map styleMap = new HashMap();
				for (Attribute attribute : list) {
					styleMap.put(attribute.getName(), attribute.getValue());
				}
				styMap.put("style", styleMap);
				styMap.put("header", node.getText());
				List l = (List) map.get("header");
				((List) l.get(l.size() - 1)).add(styMap);
				if (node.getTextTrim().startsWith("=")) {
					if (map.get("dataSets") == null) {
						map.put("dataSets", new ArrayList());
					}
					((List) map.get("dataSets")).add(styMap);
				}
			} else if ((flag.equals("footer"))
					&& (!node.getTextTrim().equals(""))) {
				Map styMap = new HashMap();
				Map styleMap = new HashMap();
				for (Attribute attribute : list) {
					styleMap.put(attribute.getName(), attribute.getValue());
				}
				styMap.put("style", styleMap);
				styMap.put("header", node.getText());
				List l = (List) map.get("footer");
				((List) l.get(l.size() - 1)).add(styMap);
				if (node.getTextTrim().startsWith("="))
					((List) map.get("dataSets")).add(styMap);
			} else if ((flag.equals("dataSets"))
					&& (node.getTextTrim().startsWith("="))) {
				Map styMap = new HashMap();
				Map styleMap = new HashMap();
				for (Attribute attribute : list) {
					styleMap.put(attribute.getName(), attribute.getValue());
					if (attribute.getName().equals("offsetTop")) {
						topCount = Integer.parseInt(attribute.getValue());
					}

				}

				styMap.put("style", styleMap);
				styMap.put("header", "%" + node.getText().substring(1));
				List l = (List) map.get("header");
				((List) l.get(l.size() - 1)).add(styMap);
				((List) map.get("dataSets")).add(styMap);
			}
		}
		Iterator iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = (Element) iterator.next();
			listTableNodesN(map, e, flag);
		}
	}

	public String[][] listToArr(String type, List<Object> list) {
		if (list == null)
			return null;
		String[][] arr = new String[list.size()][];
		for (int i = 0; i < arr.length; i++) {
			if (type.equals("group")) {
				if (i < arr.length - 1) {
					arr[i] = new String[0];
				} else {
					String[] temp = new String[arr.length];
					for (int j = 0; j < temp.length; j++) {
						temp[j] = ((Map) list.get(j)).get("header").toString();
					}
					arr[i] = temp;
				}
			} else if (type.equals("alias")) {
				List l = (List) list.get(i);
				String[] temp = new String[l.size()];
				for (int j = 0; j < l.size(); j++) {
					temp[j] = ((Map) l.get(j)).get("header").toString();
				}
				arr[i] = temp;
			} else if (type.equals("summary")) {
				List l = (List) list.get(i);
				String[] temp = new String[l.size()];
				for (int j = 0; j < l.size(); j++) {
					temp[j] = ((Map) l.get(j)).get("reportfoot").toString();
				}
				arr[i] = temp;
			}
		}
		return arr;
	}

	public String[][] listToArrN(String type, List<Object> list,
			Map<String, List<HashMap<String, Object>>> dataMap) {
		String[][] arr = new String[list.size()][];
		String[][] garr = new String[1][];
		for (int i = 0; i < arr.length; i++) {
			if (type.equals("group")) {
				String[] temp = new String[arr.length];
				for (int j = 0; j < temp.length; j++) {
					String str = ((Map) list.get(j)).get("header").toString();
					temp[j] = str;
				}
				garr[0] = temp;
				return garr;
			}
			if ((type.equals("alias")) || (type.equals("footer"))) {
				List l = (List) list.get(i);
				String[] temp = new String[l.size()];
				for (int j = 0; j < l.size(); j++) {
					String str = ((Map) l.get(j)).get("header").toString();
					if (str.startsWith("=")) {
						String key = str.substring(1, str.indexOf("."));
						String value = str.substring(str.indexOf(".") + 1);
						str = ((HashMap) ((List) dataMap.get(key)).get(0)).get(
								value).toString();
					} else if (str.startsWith("%")) {
						str = "";
					}
					temp[j] = str;
				}
				arr[i] = temp;
			}
		}
		return arr;
	}

	public String previewCharts(String chartsModel, String dataSetsModel,
			String parmsModel) {
		Map DataSetMap = compileReport(dataSetsModel);
		return compileCharts(chartsModel, DataSetMap, parmsModel);
	}

	public String compileCharts(String chartsModel,
			Map<String, Object> DataSetMap, String parmsModel) {
		try {
			String jsonStr = "[";
			Document charts = DocumentHelper.parseText(chartsModel);
			Element body = charts.getRootElement();
			List chartsList = body.selectNodes("charts");
			List<Node> chartList = ((Element) chartsList.get(0)).selectNodes("chart");
			for (Node node : chartList) {
				Element ec = (Element)node;
				String type = ec.attribute("category").getStringValue();
				String uuid = ec.attribute("uuid").getStringValue();
				String width = ec.attribute("width").getStringValue();
				String height = ec.attribute("height").getStringValue();
				String legend = "";

				GsonOption option = createOption(type);
				Element title = (Element) ec.selectNodes("titles").get(0);
				String zbt = title.elementTextTrim("text");
				String fbt = title.elementTextTrim("subtext");
				option.title().setText(zbt);
				option.title().setSubtext(fbt);

				List seriesList = ec.selectNodes("series");
				List<Node> serieList = ((Element) seriesList.get(0))
						.selectNodes("serie");
				for (Node node1 : serieList) {
					Element serie = (Element)node1;
					String name = serie.elementTextTrim("name");

					String data = serie.elementTextTrim("data");
					String dataSetName = data.split("\\.")[0].substring(1);

					JsonArray jsonarr = getDataByCol(dataSetName, DataSetMap,
							parmsModel, type);
					getSeries(option, name, jsonarr, type);
				}
				Map m = new HashMap();
				String ss = "{width:" + width + ",height:" + height
						+ ",option:" + option.toString() + "}";
				jsonStr = jsonStr + ss + ",";
			}
			jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
			jsonStr = jsonStr + "]";
			return jsonStr;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public GsonOption createOption(String type) {
		GsonOption option = new GsonOption();
		((Toolbox) option.toolbox().show(Boolean.valueOf(true)))
				.feature(new Object[] { Tool.saveAsImage });
		if ((type.equals("line")) || (type.equals("bar"))) {
			option.tooltip().setTrigger(Trigger.axis);
			option.calculable(Boolean.valueOf(true));
		} else if (type.equals("k")) {
			((DataZoom) option.dataZoom().show(Boolean.valueOf(true)))
					.realtime(Boolean.valueOf(true)).start(Integer.valueOf(50))
					.end(Integer.valueOf(100));
			option.tooltip().setTrigger(Trigger.axis);
		} else if (type.equals("pie")) {
			option.title().x(X.center);
			option.tooltip().trigger(Trigger.item)
					.formatter("{a} <br/>{b} : {c} ({d}%)");
			option.legend().orient(Orient.vertical);
			option.legend().x(X.left);
			option.calculable(Boolean.valueOf(true));
		} else if (type.equals("radar")) {
			option.tooltip().setTrigger(Trigger.axis);
			option.legend().orient(Orient.vertical);
			option.legend().x(X.right);
			option.legend().y(Y.bottom);
			option.polar().add(new Polar());
			option.calculable(Boolean.valueOf(true));
		} else if (type.equals("gauge")) {
			option.tooltip().setFormatter("{a} <br/>{b} : {c}%");
		}
		return option;
	}

	public void getSeries(GsonOption option, String name, JsonArray jsonarr,
			String type) {
		if (type.equals("line")) {
			for (int i = 0; i < jsonarr.size(); i++) {
				JsonObject jo = (JsonObject) jsonarr.get(i);
				List l = option.legend().data();
				l.add(jo.get("name").getAsString());
				option.legend().setData(l);

				Line line = new Line();
				line.name(jo.get("name").getAsString());
				List valueList = new ArrayList();
				String value = jo.get("value").getAsString();
				String[] arrValue = value.split(",");
				for (int j = 0; j < arrValue.length; j++) {
					valueList
							.add(Integer.valueOf(Integer.parseInt(arrValue[j])));
				}
				line.setData(valueList);
				option.series().add(line);

				if (i == 0) {
					List listTemp = new ArrayList();
					String columnName = jo.get("columnName").getAsString();
					String[] arr = columnName.split(",");
					for (int j = 0; j < arr.length; j++) {
						listTemp.add(arr[j]);
					}
					CategoryAxis categoryAxis = new CategoryAxis();
					categoryAxis.setType(AxisType.category);
					categoryAxis.setBoundaryGap(Boolean.valueOf(false));
					categoryAxis.setData(listTemp);
					option.xAxis(new Axis[] { categoryAxis });
				}

			}

			ValueAxis valueAxis = new ValueAxis();
			valueAxis.setType(AxisType.value);
			option.yAxis(new Axis[] { valueAxis });
		} else if (type.equals("bar")) {
			Bar bar = new Bar();
			bar.name(name);
			List list = new ArrayList();
			List listX = new ArrayList();
			for (int i = 0; i < jsonarr.size(); i++) {
				JsonObject jo = (JsonObject) jsonarr.get(i);
				list.add(jo.get("value"));
				listX.add(jo.get("name").getAsString());
			}

			bar.setData(list);
			List l = option.legend().data();
			l.add(name);
			option.legend().setData(l);
			option.series().add(bar);

			CategoryAxis categoryAxis = new CategoryAxis();
			categoryAxis.setType(AxisType.category);
			categoryAxis.setBoundaryGap(Boolean.valueOf(true));
			AxisTick axisTick = new AxisTick();
			axisTick.setOnGap(Boolean.valueOf(false));
			categoryAxis.setAxisTick(axisTick);
			SplitLine splitLine = new SplitLine();
			splitLine.setShow(Boolean.valueOf(false));
			categoryAxis.setSplitLine(splitLine);
			categoryAxis.setData(listX);
			option.xAxis(new Axis[] { categoryAxis });

			ValueAxis valueAxis = new ValueAxis();
			valueAxis.setType(AxisType.value);
			valueAxis.setScale(Boolean.valueOf(true));
			valueAxis.setData(list);
			option.yAxis(new Axis[] { valueAxis });
		} else if (type.equals("k")) {
			K k = new K();
			k.name(name);
			List list = new ArrayList();
			List listX = new ArrayList();
			for (int i = 0; i < jsonarr.size(); i++) {
				JsonObject jo = (JsonObject) jsonarr.get(i);

				String temp = jo.get("value").toString();

				String[] arr = temp.split(",");

				Double[] douArr = new Double[arr.length];
				for (int j = 0; j < arr.length; j++) {
					douArr[j] = Double.valueOf(Double.parseDouble(arr[j]));
				}
				list.add(douArr);
				listX.add(jo.get("name").getAsString());

				if (i == 0) {
					String tipContent = "var res = params[0].seriesName + ' ' + params[0].name;";
					List listTemp = new ArrayList();
					String columnName = jo.get("columnName").getAsString();
					String[] arrtip = columnName.split(",");
					for (int j = 0; j < arrtip.length; j++) {
						tipContent = tipContent + " res += '<br/>" + arrtip[j]
								+ ": ' + params[0].value[" + j + "];";
					}
					tipContent = "function (params) {" + tipContent
							+ "return res;}";
					k.tooltip().trigger(Trigger.axis).formatter(tipContent);
				}
			}
			k.setData(list);

			List l = option.legend().data();
			l.add(name);
			option.legend().setData(l);
			option.series().add(k);

			CategoryAxis categoryAxis = new CategoryAxis();
			categoryAxis.setType(AxisType.category);
			categoryAxis.setBoundaryGap(Boolean.valueOf(true));
			AxisTick axisTick = new AxisTick();
			axisTick.setOnGap(Boolean.valueOf(false));
			categoryAxis.setAxisTick(axisTick);
			SplitLine splitLine = new SplitLine();
			splitLine.setShow(Boolean.valueOf(false));
			categoryAxis.setSplitLine(splitLine);
			categoryAxis.setData(listX);
			option.xAxis(new Axis[] { categoryAxis });

			ValueAxis valueAxis = new ValueAxis();
			valueAxis.setType(AxisType.value);
			valueAxis.setScale(Boolean.valueOf(true));

			option.yAxis(new Axis[] { valueAxis });
		} else if (type.equals("pie")) {
			Pie pie = new Pie();
			pie.name(name);

			List list = new ArrayList();
			for (int i = 0; i < jsonarr.size(); i++) {
				JsonObject jo = (JsonObject) jsonarr.get(i);
				Map map = new HashMap();
				map.put("value", jo.get("value").getAsString());
				map.put("name", jo.get("name").getAsString());
				List l = option.legend().data();
				l.add(jo.get("name").getAsString());
				option.legend().setData(l);
				list.add(map);
			}
			pie.setData(list);
			pie.radius("55%");
			String[] s = new String[2];
			s[0] = "50%";
			s[1] = "60%";
			pie.center(s);
			option.series().add(pie);
		} else if (type.equals("radar")) {
			Radar radar = new Radar();
			radar.name(name);
			int max = 1000;
			int min = 500;
			Random random = new Random();

			List list = new ArrayList();
			for (int i = 0; i < 3; i++) {
				Map map = new HashMap();
				List arr = new ArrayList();
				((Polar) option.polar().get(0)).indicator().clear();
				for (int j = 0; j < 5; j++) {
					int s = random.nextInt(max) % (max - min + 1) + min;
					arr.add(Integer.valueOf(s));
					Map m = new HashMap();
					m.put("text", Integer.valueOf(s));
					((Polar) option.polar().get(0)).indicator().add(m);
				}
				map.put("value", arr);
				map.put("name", "数据" + i);
				List l = option.legend().data();
				l.add("数据" + i);
				option.legend().setData(l);
				list.add(map);
			}
			radar.setData(list);
			option.series().add(radar);
		} else if (type.equals("gauge")) {
			Gauge gauge = new Gauge();
			List list = new ArrayList();
			for (int i = 0; i < jsonarr.size(); i++) {
				JsonObject jo = (JsonObject) jsonarr.get(i);
				Map map = new HashMap();
				map.put("value", jo.get("value").getAsString());
				map.put("name", jo.get("name").getAsString());
				list.add(map);
				gauge.setData(list);
				gauge.detail().formatter(jo.get("value").getAsString() + "%");
				option.series().add(gauge);
			}
		}
	}

	public JsonArray getDataByCol(String dataSetName,
			Map<String, Object> DataSetMap, String parmsModel, String tabType) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JsonArray jsonarr = null;

		List nodes = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			Document document = DocumentHelper.parseText(parmsModel);
			Element rootElm = document.getRootElement();
			nodes = rootElm.elements("parm");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		String str = "[";
		Map queryMap = (Map) ((Map) DataSetMap.get(dataSetName)).get("query");
		String dataSourceName = queryMap.get("datasourcename").toString();
		String sql = queryMap.get("commandtext").toString();
		DataSourceBean dataSourceBean = new DataSourceBean();
		dataSourceBean.setDataSourceName(dataSourceName);
		String realPath = BaseAction.realPath + "db-config.xml";
		dataSourceBean = XmlUtil.selectXML(dataSourceBean, realPath);
		Connection conn = DBUtil.getConn(dataSourceBean);
		if (conn != null) {
			LinkedHashMap<String,Map> parameters = (LinkedHashMap) queryMap.get("parms");
			for (String k : parameters.keySet()) {
				Map parameter = (Map) parameters.get(k);
				String value = request.getParameter(parameter.get("name")
						.toString());
				String dbtype = "";
				if (nodes.size() > 0) {
					for (Iterator it = nodes.iterator(); it.hasNext();) {
						Element elm = (Element) it.next();
						if (parameter.get("name").equals(
								elm.attributeValue("name"))) {
							dbtype = elm.attributeValue("dbtype");
						}
					}
				}
				if ((!"double".equals(dbtype)) && (!"int".equals(dbtype))) {
					value = "'" + value + "'";
				}
				sql = sql.replaceFirst("\\?", value).replace(" '%'", " '%")
						.replace("'%' ", "%' ");
			}
			if (!sql.equals("")) {
				sql = sql.replaceAll("&quot;", "\"").replaceAll("&gt;", ">")
						.replaceAll("&lt;", "<").replaceAll("&amp;", "&");
			}
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				boolean flag = true;
				while (rs.next()) {
					if (("bar".equals(tabType)) || ("pie".equals(tabType))
							|| ("gauge".equals(tabType))) {
						str = str + "{name:'" + rs.getString(1) + "',value:'"
								+ rs.getString(2) + "'},";
					} else if (("line".equals(tabType))
							|| ("k".equals(tabType))) {
						ResultSetMetaData rsmd = rs.getMetaData();
						int columnCount = rsmd.getColumnCount();
						String tempValue = "";
						String columnName = "";

						for (int i = 2; i <= columnCount; i++) {
							if (i != columnCount)
								tempValue = tempValue + rs.getString(i) + ",";
							else {
								tempValue = tempValue + rs.getString(i);
							}
						}
						if (flag) {
							for (int i = 2; i <= columnCount; i++) {
								if (i != columnCount)
									columnName = columnName
											+ rsmd.getColumnName(i) + ",";
								else
									columnName = columnName
											+ rsmd.getColumnName(i);
							}
							str = str + "{name:'" + rs.getString(1)
									+ "',value:'" + tempValue
									+ "',columnName:'" + columnName + "'},";
							flag = false;
						} else {
							str = str + "{name:'" + rs.getString(1)
									+ "',value:'" + tempValue + "'},";
						}
					}
				}
				str = str + "]";
				jsonarr = new JsonParser().parse(str.replace("},]", "}]"))
						.getAsJsonArray();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			} finally {
				DBUtil.release(new Object[] { ps, rs });
			}
		}
		return jsonarr;
	}

	private static String cExp(String str) {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager
				.getEngineByName("javascript");
		scriptEngine.put("JRFunExp", new JRFunExpImpl());
		return str;
	}

	public static String saveFillReport(String dataSourceName,
			String tableName, String dataArray) {
		String exeFlag = "";
		String realPath = BaseAction.realPath + "db-config.xml";
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSourceBean dataSourceBean = new DataSourceBean();
		dataSourceBean.setDataSourceName(dataSourceName);
		DataSourceBean dsb = XmlUtil.selectXML(dataSourceBean, realPath);
		Connection conn = DBUtil.getConn(dsb);
		int updateCount = 0;
		if (conn != null) {
			JsonArray jsonArray = new JsonArray();
			if (!"".equals(dataArray)) {
				jsonArray = new JsonParser().parse(dataArray).getAsJsonArray();
			}
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonArray objName = (JsonArray) jsonArray.get(i)
						.getAsJsonObject().get("name");
				Map<String,String> fieldName = (Map) objName.get(0);

				StringBuilder updateSql = new StringBuilder();
				updateSql.append("update " + tableName + " set ");
				String tempName = "";
				for (String key : fieldName.keySet()) {
					tempName = tempName + key + "='"
							+ (String) fieldName.get(key) + "',";
				}

				if ("".equals(tempName))
					continue;
				try {
					tempName = tempName.substring(0, tempName.length() - 1);
					updateSql.append(tempName);
					JsonArray objKey = (JsonArray) jsonArray.get(i)
							.getAsJsonObject().get("key");
					Map<String,String> keyName = (Map) objKey.get(0);
					String tempKey = "";
					for (String key : keyName.keySet()) {
						tempKey = tempKey + key + "='"
								+ (String) keyName.get(key) + "' and";
					}
					if (!"".equals(tempKey)) {
						updateSql.append(" where ");
						tempKey = tempKey.substring(0, tempKey.length() - 4);
						updateSql.append(tempKey);
					}
					ps = conn.prepareStatement(updateSql.toString());
					updateCount += ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		}

		return String.valueOf(updateCount);
	}
}
 