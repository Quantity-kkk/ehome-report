<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>报表系统</title>
<%@ include file="report/common/head.jsp"%>
<link href="report/common/css/css.css" rel="stylesheet" type="text/css" />
<script src="report/lib/jquery.min.js"></script>
<script src="report/lib/jquery.layout.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("body").layout({
			applyDemoStyles : true,
			west__size : 150,
			west__spacing_open : 0, //边框的间隙 
			north__spacing_open : 0, //边框的间隙 
			togglerTip_open : "关闭", //pane打开时，当鼠标移动到边框上按钮上，显示的提示语    
			togglerTip_closed : "打开", //pane关闭时，当鼠标移动到边框上按钮上，显示的提示语 
		});
		$(".side > div.menu-list").bind("click", function() {
			$(this).addClass("curr").siblings().removeClass("curr");
		});
		/* $(".btn-side,.logo").bind("click", function() {
			if ($(".btn-side").hasClass("on")) {
				$(".menu-list_left").animate({
					"width":"35px"
				})
				$(".btn-side").removeClass("on");
				$(".ui-layout-center").animate({
					left : "150px",
					width : "-=94px"
				});
				$(".ui-layout-west").animate({
					width : "150px"
				});
			} else {
				$(".menu-list_left").animate({
					"width":"56px"
				})
				$(".btn-side").addClass("on");
				$(".ui-layout-center").animate({
					left : "56px",
					width : "+=94px"
				});
				$(".ui-layout-west").animate({
					width : "56px"
				});
			}

		}); */
		
		
		
		start();
	});
	function show(url) {
		$('#main').attr('src', url);
	}
	function start() {
		var today = new Date();
		var year = today.getFullYear();
		var month = today.getMonth() + 1;
		var day = today.getDate();
		var hours = today.getHours();
		var minutes = today.getMinutes();
		var seconds = today.getSeconds();
		//如果是单位数字，前面补0
		month = month < 10 ? "0" + month : month;
		day = day < 10 ? "0" + day : day;
		hours = hours < 10 ? "0" + hours : hours;
		minutes = minutes < 10 ? "0" + minutes : minutes;
		seconds = seconds < 10 ? "0" + seconds : seconds;

		var myddy = today.getDay(); //获取存储当前日期
		var weekday = [ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" ];

		//时间信息连成字符串
		var str = year + "年" + month + "月" + day + "日 " + hours + ":" + minutes + ":" + seconds + " " + weekday[myddy];
		//获取id=result的内容
		var obj = document.getElementById("datetime");
		obj.innerHTML = str;
		//延时器
		window.setTimeout("start()", 1000);
	}
</script>
</head>

<body>
	<div class="ui-layout-center">
		<div class="top">
			<button title="收缩菜单" class="btn-side" type="button"></button>
			<div class="exit" onclick="location.href='logout.action'"></div>
			<div class="info">
				<span class="user">管理员</span> <span calss="datetime" id="datetime"></span>
			</div>
		</div>
		<iframe id="main" name="main" class="panel-iframe"
			src="report/design/List_DB.jsp"></iframe>
	</div>
	<div class="ui-layout-west">
		<!-- <div class='box'>
			<div class='wave -one'></div>
			<div class='wave -two'></div>
			<div class='wave -three'></div>
		</div> -->
		<div class="side">
			<div class="header">
				<div class="logo" src="report/design/images/logo.png"></div>

			</div>
			<div class="menu-list menu curr"
				onclick="show('report/design/List_DB.jsp')">
				<div title="数据源管理" class="menuleft  menu-list_left">
					<em></em>
				</div>
				<div class="menuright menu-list_right">数据源管理</div>
			</div>
			<div class="menu-list menu2"
				onclick="show('report/design/List_Report.jsp')">
				<div title="报表配置" class="menu2left  menu-list_left">
					<em></em>
				</div>
				<div class="menu2right menu-list_right">报表配置</div>
			</div>
			<div class="menu-list menu3"
				onclick="window.open('http://rdp.mftcc.cn/REPORT_DEMO/');">
				<div title="报表演示" class="menu3left  menu-list_left">
					<em></em>
				</div>
				<div class="menu3right menu-list_right">报表演示</div>
			</div>
			<div class="menu-list menu6"
				onclick="show('ActivationServletReport?authMessage=1')">
				<div title="授权信息" class="menu6left  menu-list_left">
					<em></em>
				</div>
				<div class="menu5right menu-list_right">授权信息</div>
			</div>
			<!-- <div class="menu-list menu4"
				onclick="show('report/design/List_RptConfig.jsp')">
				<div title="自定义配置" class="menu4left  menu-list_left">
					<em></em>
				</div>
				<div class="menu4right menu-list_right">自定义配置</div>
			</div>
			<div class="menu-list menu5"
				onclick="show('report/design/List_RptCustom.jsp')">
				<div title="自定义报表" class="menu5left menu-list_left">
					<em></em>
				</div>
				<div class="menu5right menu-list_right">自定义报表</div>
			</div> -->
			<!-- <div class="exit" onclick="location.href='logout.action'"></div> -->
			
			
		</div>
	</div>
</body>
</html>
