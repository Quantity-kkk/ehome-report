<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>导出监视</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
</head>

<body>
	<%
		if (request.getParameter("clear") == null) {
	%>
	<input type="button" value="清空导出" onclick="location.href='<%=basePath%>/report/design/export_stat.jsp?clear=1'" />
	<%
		} else {
			report.java.jrreport.action.ExportAction.exportMap.put("curexport", "0");
	%>
	<input type="button" value="返回" onclick="location.href='<%=basePath%>/report/design/export_stat.jsp'" />
	<%
		}
	%><br>
	<br>最大导出数目：<%=report.java.jrreport.util.JRUtilNew.baseMap.get("maxexport")%>
	<br>
	<br> 当前导出数目：<%=report.java.jrreport.action.ExportAction.exportMap.get("curexport")%>
</body>
</html>
