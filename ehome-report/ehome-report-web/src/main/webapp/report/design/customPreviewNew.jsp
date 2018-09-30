<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表详情预览页面</title>
<style type="text/css">
.fy{text-align:center;font-size:12px;}
.page1{width:180mm;height:267mm;padding:10mm;background:#ffffff;margin:8px auto;overflow:auto;}
.page2{width:267mm;height:180mm;padding:10mm;background:#ffffff;margin:8px auto;overflow:auto;}
.page3{width:100%:height:100%;background:#ffffff;margin:0px auto;overflow:auto;}
body{margin:0;background:#f3f3f3;}
table{background:#ffffff;}
.dynamic{border:1px solid #000;width:98%;}
.dynamic td{border-right:1px solid rgb(204, 204, 204);}
.dynamic .rtitle td{text-align:center;vertical-align:middle;mso-pattern:auto;font-size:14pt;font-weight:700;font-style:normal;font-family:"Microsoft YaHei UI","sans-serif";border-bottom:2px solid #cdcdcd;height: 23.25pt;}

.dynamic .rhead td{position:relative;text-align:center;vertical-align:middle;font-size:12pt;font-weight:400;font-style:normal;font-family:"Microsoft YaHei UI","sans-serif";border-bottom:1px solid #bfbfbf;overflow:hidden;}

.dynamic tbody td{height: 26.45pt;color: rgb(0, 0, 0);font-family: "Microsoft YaHei UI";font-size: 10pt;font-weight: 400;border-bottom:1px solid #bfbfbf;text-decoration: none;text-line-through: none;text-align:center;}
tfoot td{ text-align:left;vertical-align:middle;padding-left:3px;overflow:hidden;}
.confirmbtn{margin:0 5px;border:1px solid #999;background:rgba(0, 0, 0, 0) url('../images/domeBg1.png') repeat-x scroll 0 0;}
.coldelete{cursor:pointer;width:12px;height:13px;background:url('../images/deleteTr.png');background-size:12px 30px;display:block;position:absolute;right:5%;top:14px;}
.coldelete:hover{background-position:0 -17px;}
</style>
<style type="text/css" media="print">
body{background:#ffffff;}
</style>
<script src="../lib/jquery.js"></script>
<script type="text/javascript">
	
	$(function(){
		parent.init(1);
	});

	function frmChild(bodyhtml,fx,page){
		if($('#page'+page).length==0){
		$('body').append('<div rel="pg" class="page1"><div id="page'+page+'"></div></div><p class="fy">--第'+page+'页--</p>');
		}
		
		$('div[rel=pg]').attr('class','page'+fx);
		$('#page'+page).html(bodyhtml);
		
		
		$("#page"+page+" table.dynamic .rhead td").hover(function(){
			$(this).append("<i class='coldelete'></i>");
			
			//删除指定列
			$(this).children("i").on("click",function(){
				var index = $(this).parent().index();
				var colspan = parseInt($("table.dynamic .rtitle td").attr("colspan"));
				$("table.dynamic .rhead tr :nth-child("+(index+1)+"),table.dynamic tbody tr :nth-child("+(index+1)+")").remove();
				$("table.dynamic .rtitle td").attr("colspan",colspan-1);
				
			});
		},function(){
			$(this).children("i").remove();
		});
		
		$("html,body").stop(true);
		$("html,body").animate({scrollTop: $('#page'+page).offset().top}, 1000);	
	}
	//刷新页面
	function refresh(){
		window.location.reload();
	}		
</script>
</head>

<body>
</body>
</html>