<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
String uid = request.getParameter("uid");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>报表预览</title>
<style type="text/css">
html,body{height:100%}
body {
    margin: 0px;
    padding: 0px;
    overflow: hidden;
    font-size: 12px;
    font-family: "Microsoft YaHei", SimSun, Arial, Sans-Serif;
}
#searchaddition{padding:8px;}
#searchaddition input{margin:0 5px;}
</style>
<script src="../lib/jquery.js"></script>
<script type="text/javascript">
var dataSetsModel;
var tableModel;
var parmsModel;
$(function(){
$.ajax({
		            url : '../../initPreParms.action', 
		            data:{
		                uid:'<%=uid%>'
            },
            type:"post",
            success : function(data) {
                tableModel=data.tableModel;
        		dataSetsModel=data.dataSetsModel;
        		parmsModel=data.parmsModel;
        		optJRByType('C','previewc');
            }
   });
});
function optJRByType(t,type){
	$.ajax({
		url : "../../previewDesign.action", 
		data:{
			dataSetsModel:dataSetsModel,
			tableModel:tableModel,
			type:t,
			opt:type
		},
		type:"post",
		async:false,
		success : function(data) {
			data = eval("("+data+")");
			if(data=="error"){
				alert("错误：	页面内容超出页面高度！")
			}else if(data=="previewc"){
				//window.open("printPrieviewC.jsp","preview");
				location.href="../rbc/printPrieviewC.jsp";
			}else{
				window.location.href="../rbc/export.action?type="+type;
			}
		}
	});
	/* $.post("previewDesign.action"); */
}
//查询数据
function searchList(){
   	var activeParm="";
   	 $("#searchaddition input[type=text]").each(function(i){
       	if(i != $("#searchaddition input[type=text]").length-1)
       		activeParm +=$(this).attr("name")+"="+encodeURIComponent($(this).val())+"&";
       	else
       		activeParm +=$(this).attr("name")+"="+encodeURIComponent($(this).val());
       });
   	ifmreport.search(activeParm);
}
</script>
</head>

<body>
<!-- 
<div class="top"><div style="float:left"><img src="images/logo.png" width="39" height="36" /><img src="images/title.png" width="175" height="36" /></div><div class="exit"></div></div>
 -->
<div class="main"><div class="side"><div class="menu"><div class="menuleft"><em></em></div>
<table style="width: 100%; height: 100%; table-layout: fixed;" border="0" cellspacing="0" cellpadding="0">
<tbody>
<tr><td style="overflow: auto;background:#cccccc;">
<table width="100%" height="100%" style="table-layout: fixed;"><tbody><tr><td><div style="-ms-overflow-x: auto;">
<div id="searchaddition"></div>
</td></tr></tbody></table>
</td></tr>
<tr><td valign="top" height="100%"><div style="width:100%;height:100%;z-index:-1;"><iframe id="ifmreport" name="ifmreport" src="about:blank;" frameborder="0" style="width: 100%; height: 100%;"></iframe></div></td></tr>
</tbody></table>

</body>
</html>
