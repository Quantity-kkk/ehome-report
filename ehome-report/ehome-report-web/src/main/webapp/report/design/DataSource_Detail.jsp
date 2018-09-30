<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="../../style/css/main.css">
<link href="../lib/ui/jquery-ui.min.css" rel="stylesheet">
<link href="../css/staging.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="../css/demo.css" type="text/css">
<script type="text/javascript" src="../lib/jquery.min.js"></script>
<script type="text/javascript" src="../lib/ui/jquery-ui.min.js"></script>
<SCRIPT type="text/javascript">
	$(function() {
		$("#dialog").dialog({
			autoOpen : false,
			width : 720,
			buttons : [ {
				text : "上一步",
				click : function() {
					$(this).dialog("close");
				}
			}, {
				text : "下一步",
				click : function() {
					$(this).dialog("close");
				}
			}, ]
		});
		// Link to open the dialog
		$("#dialog-link").click(function(event) {
			$("#dialog").dialog("open");
			event.preventDefault();
		});
		$(".ui-dialog-buttonset button").eq(0)
				.css("backgroundColor", "#ff6600");
		$(".ui-dialog-buttonset button").eq(0).hover(function() {
			$(this).css("backgroundColor", "#ff9900");
		}, function() {
			$(this).css("backgroundColor", "#ff6600");
		});
		$(".ui-dialog-buttonset button").eq(1).hover(function() {
			$(this).css("backgroundColor", "#00ccff");
		}, function() {
			$(this).css("backgroundColor", "#0090ff");
		});
		$(".ui-dialog-buttonset button").eq(0).css("marginRight", "45px");
		$(".ui-dialog-buttonset button").eq(1)
				.css("backgroundColor", "#0090ff");
	});
</script>
</head>

<body>
	<div>
		<div class="title">
			<div class="titl">当前数据源</div>
			<div class="titr">
				<a href="#" id="dialog-link" class="ui-state-default ui-corner-all"><img
					src="../images/zn.gif" width="110" height="25" /></a>
			</div>
		</div>
		<div class="name">
			<div class="box1">数据源名称</div>
			<div class="titl" style="margin-left:145px;">当前状态</div>
			<div class="box2">数据源类型</div>
		</div>
		<s:iterator value="dataSourceBeanList" id="dataSourceBean">
			<div class="list">
				<div class="box1">
					<input type="checkbox" name="checkbox" id="checkbox"
						style="margin-top:11px;" />
				</div>
				<div class="box1" style="padding-left:5px;">
					<img src="../images/tubiao.gif" width="12" height="35" />
				</div>
				<div class="box1a" style="padding-left:5px; color:#00cc00;">
					<a href="#"><s:property value="dataSourceName" /></a>
				</div>
				<div class="box1" style="color:#00cc00;">
					<a href="#"> <s:if test="state==0">已断开</s:if> <s:else>连接中</s:else>
					</a>
				</div>
				<div class="you" style="color:#00cc00;">
					<a href="#"><s:property value="type" /></a>
				</div>
			</div>
		</s:iterator>
	</div>
	<!-- ui-dialog -->
	<div id="dialog" title="新建向导">
		<div class="liucheng">
			<div class="round">1</div>
			<div class="xt"></div>
			<div class="round2">2</div>
			<div class="xt"></div>
			<div class="round3">3</div>
			<div class="xt"></div>
			<div class="round3">4</div>
			<div class="xt"></div>
			<div class="round3">5</div>
		</div>
		<div class="zi">连接数据源 数据集类型 配置数据型 报表类型 其它类型</div>
		<div class="box3"></div>
	</div>
</body>
</html>
