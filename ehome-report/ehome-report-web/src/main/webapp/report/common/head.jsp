<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String hpath = request.getContextPath();
String hbasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+hpath+"/";
%>
<link rel="shortcut icon" href="<%=hbasePath%>/report/common/images/favicon.png" type="image/png" />