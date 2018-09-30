<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- <link rel="stylesheet" type="text/css" href="style/css/main.css"> -->
<html>
<head>
<%-- <base href="<%=basePath%>"> --%>

<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="../lib/ztree/css/zTreeStyle/zTreeStyle.css"
	type="text/css">
<link href="../css/staging.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="../css/demo.css" type="text/css">
<script type="text/javascript" src="../lib/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../lib/ztree/jquery.ztree.core-3.5.js"></script>
<SCRIPT type="text/javascript">
	var setting = {
		view : {
			showIcon : showIconForTree
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback: {
			onClick: zTreeOnClick
		}
	};

	var zNodes = [ {
		id : 1,
		pId : 0,
		name : "全部文件",
		isParent : true
	}, {
		id : 2,
		pId : 0,
		name : "报表文件"
	}, {
		id : 21,
		pId : 2,
		name : "一般合同表"
	}, {
		id : 22,
		pId : 2,
		name : "多级表头列表"
	}, {
		id : 23,
		pId : 2,
		name : "租后跟踪检查"
	}, {
		id : 24,
		pId : 2,
		name : "统计列表"
	}, {
		id : 25,
		pId : 2,
		name : "资本负债表"
	}, {
		id : 26,
		pId : 2,
		name : "所有者权益表"
	}, {
		id : 3,
		pId : 0,
		name : "我的数据源"
	}, {
		id : 31,
		pId : 3,
		name : "数据源1"
	}, {
		id : 32,
		pId : 3,
		name : "数据源2"
	}, {
		id : 33,
		pId : 3,
		name : "数据源3"
	}, ];
	
	function zTreeOnClick(event, treeId, treeNode) {
		var src;
	    if(treeNode.level==0){
	    	switch(treeNode.name){
	    	case "全部文件":
    			break;
	    	case "报表文件":
	    		src = "Report_Detail.jsp";
    			break;
	    	case "我的数据源":
	    		src = "selectDataSourceAll.action";
	    		break;
	    	}
	    	$('#content').attr('src',src);
	    }
	};
	
	function showIconForTree(treeId, treeNode) {
		return !treeNode;
	};

	$(document).ready(function() {
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});
	$(function() {
		window.onload = function() {
			function auto_height() {
				//var h= document.documentElement.clientHeight-200; 
				//var high = document.getElementById("box"); 
				//high.style.height=h+"px"; 
				document.getElementById("box").style.height = document.documentElement.clientHeight
						- 50 + "px";
				document.getElementById("content").style.height = document.documentElement.clientHeight
						- 62 + "px";

			}
			auto_height();
			onresize = auto_height;
		};

	});
</SCRIPT>
</head>

<body>
	<form action="test.action" method="post">
		<div class="top">
			<div class="logo">
				<img src="../images/logo.gif" width="171" height="37" /> | 无标题电子表格
			</div>
			<div class="cl">
				<img src="../images/cl.gif" width="16" height="16" />
			</div>
			<div class="topright">
				<span class="brown">最后保存于2015-3-1 16:20 |</span> 当前用户（张三）
			</div>
		</div>
		<div class="main">
			<div class="side">
				<div class="botton">
					<div class="bottonl">新建</div>
					<div class="bottonr">上传</div>
				</div>
				<div class="content_wrap" id="content_wrap">
					<div class="zTreeDemoBackground left">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
				</div>
			</div>
			<iframe  id="content" class="content" src="Report_Detail.jsp"></iframe>
		</div>
	</form>
</body>
</html>
