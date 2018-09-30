<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <title></title>
    <link href="css/print.css" rel="stylesheet" type="text/css">
    <style type="text/css">div.print{margin:50px auto;}</style>
    <style type="text/css" media="print">
        div.print {
        margin:0;
            display: block;
            box-shadow: none;
            background-color: transparent;
            border: none;
        }
    </style>
    <style type="text/css">
        div.print{ padding:10px;}
    </style>
    <script src="../lib/jquery.min.js"></script>
    <script src="../lib/echarts/echarts-all.js"></script>
    <script type="text/javascript">
        $(function(){ 
        <% 
	String json = request.getSession().getAttribute("chartsOpt").toString();
	%>
     		var json = <%=json%>;
     		for(var i=0;i<json.length;i++){
     			var $div = $("<div class='echart' style='width:"+json[i].width+"px;height:"+json[i].height+"px'></div>");
     			$('#page1').append($div);
     			var myChart = echarts.init($div[0]);
     			myChart.setOption(json[i].option);
     		}
     	
          });

    </script>
</head>
<body>
<div  id="page1" class="print"></div>
</body>
</html>