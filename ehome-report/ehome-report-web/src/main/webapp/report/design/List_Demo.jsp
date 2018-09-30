<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报表演示</title>
<link href="../common/css/css.css" rel="stylesheet" type="text/css" />
<link href="../common/css/pagination.css" rel="stylesheet">
<script type="text/javascript" src="../lib/jquery.min.js"></script>
<script type=text/javascript src="../lib/jquery.pagination.js"></script>
<script>
$(function(){$('.tbody tr').hover(function(){$(this).children('td').css('background','#f1f6fc')},function(){$(this).children('td').css('background','#fff')})})

$(function(){
	//总数目
	var length = $("#hiddenresult tr").length;
	
	$("#Pagination").pagination(length, {
    num_edge_entries: 2,
    num_display_entries: 4,
    callback: pageselectCallback,
    items_per_page:10
	}); 
 
	function pageselectCallback(page_index, jq){
		
		var items_per_page =10;
		var max_elem = Math.min((page_index+1) * items_per_page, length);
		
		$("#Searchresult").html("");
		// 获取加载元素
		for(var i=page_index*items_per_page;i<max_elem;i++){
			$("#Searchresult").append($("#hiddenresult tr:eq("+i+")").clone());
		}
		//阻止单击事件
		}
		$(".exit").bind("click",function(){
				location.href="../../logout.action";
			});
	});
</script>
</head>

<body>
<div class="main">
<table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
  <tbody>
    <tr>
      <td valign="top">
 
		</td>
      <td valign="top"><div >
<div class="content"><div class="manage"><div class="managetitle">报表演示</div>
</div>
  <table width="94%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:10px;">
    <tr class="biaotou">
      <td height="35">文件名</td>
      <td width="200">报表类型</td>
      <td width="100">操作</td>
    </tr>
	<tbody id="Searchresult" class="tbody"></tbody>
	<tbody style="display:none" id="hiddenresult">
    <tr>
      <tr>
      <td><img src="images/icon.png" width="16" height="16" />折线图演示报表</td>
      <td>图表报表</td>
      <td width="80"><a href="Demo.jsp?reporttype=C&uid=f2103e79f1b1c8329fc2f4c0b4e6dc86" target="_blank">查看</a>
       &nbsp; <a href="Main_C.jsp?uuid=f2103e79f1b1c8329fc2f4c0b4e6dc86" target="_blank">设计</a></td>
      </tr>
      <tr>
      <td><img src="images/icon.png" width="16" height="16" />柱状图演示报表</td>
      <td>图表报表</td>
      <td width="80"><a href="Demo.jsp?reporttype=C&uid=f2b33f5c201ca24e1415c0349033a90a" target="_blank">查看</a>
       &nbsp; <a href="Main_C.jsp?uuid=f2b33f5c201ca24e1415c0349033a90a" target="_blank">设计</a></td>
      </tr>
      <tr>
      <td><img src="images/icon.png" width="16" height="16" />K线图演示报表</td>
      <td>图表报表</td>
      <td width="80"><a href="Demo.jsp?reporttype=C&uid=325b65c6e0330a69f9dedce35b8da5d3" target="_blank">查看</a>
       &nbsp; <a href="Main_C.jsp?uuid=325b65c6e0330a69f9dedce35b8da5d3" target="_blank">设计</a></td>
      </tr>
      <tr>
      <td><img src="images/icon.png" width="16" height="16" />饼图演示报表</td>
      <td>图表报表</td>
      <td width="80"><a href="Demo.jsp?reporttype=C&uid=667502436bc44f5fe7fe4f6552163f53" target="_blank">查看</a>
       &nbsp; <a href="Main_C.jsp?uuid=667502436bc44f5fe7fe4f6552163f53" target="_blank">设计</a></td>
      </tr>
      <tr>
      <td><img src="images/icon.png" width="16" height="16" />仪表盘演示报表</td>
      <td>图表报表</td>
      <td width="80"><a href="Demo.jsp?reporttype=C&uid=4790bd6ca036fadc6f0c6267c8b86d5b" target="_blank">查看</a>
       &nbsp; <a href="Main_C.jsp?uuid=4790bd6ca036fadc6f0c6267c8b86d5b" target="_blank">设计</a></td>
      </tr>
      <tr>
      <td><img src="images/icon.png" width="16" height="16" />图表组合报表</td>
      <td>图表报表</td>
      <td width="80"><a href="Demo.jsp?reporttype=C&uid=3b48b60bb1f746fa5cfe40ae98e0d75b" target="_blank">查看</a>
        &nbsp; <a href="Main_C.jsp?uuid=3b48b60bb1f746fa5cfe40ae98e0d75b" target="_blank">设计</a></td>
      </tr>
  </tbody>
  </table>
  <div style="padding:20px 30px;text-align:right;">
  <div id="Pagination" class="pagination"></div>
  </div>
</div></div>
 </td>
    </tr>
  </tbody>
</table></div>
</body>
</html>

