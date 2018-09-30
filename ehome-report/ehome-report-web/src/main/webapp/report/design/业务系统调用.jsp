<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String url = basePath + "/report/design/designPreviewIndex.jsp?reporttype=D&uid=3e1c7bf656bb14ec50507131128fc93a";
	//url说明
	//designPreviewIndex.jsp--报表调用地址，或者是designPreviewIndexC.jsp
	//reporttype--报表类型
	//uid---报表的id
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>业务报表调用示例</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

</head>

<body>
	<h1>这只是一个简单的调用示例，详细的可以看designPreviewMain.jsp根据业务系统自己改造</h1>
	<h2 style="color:red">业务系统参数传递为post传参，请注意看下面的form</h2>
	<form action="<%=url%>" method="post" target="ifmreport">
		<!-- post参数放这里 -->
		参数1：<input type="text" name="parm1">
		参数2： <select name="parm2"></select>
		参数…
		 <input type="submit" value=" 提交调用 " />
	</form>
	<iframe id="ifmreport" name="ifmreport" src="about:blank" frameborder="0" style="width: 100%; height: 100%;"></iframe>
</body>
</html>
