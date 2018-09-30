<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String reporttype = request.getParameter("reporttype");
	String uid = request.getParameter("uid");
 %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>报表预览</title>
<link href="../css/preview.css" rel="stylesheet" type="text/css">
<style type="text/css">
html,body{height:100%}
body {
    margin: 0px;
    padding: 0px;
    overflow: hidden;
    font-size: 12px;
    font-family: "Microsoft YaHei", SimSun, Arial, Sans-Serif;
}
#searchaddition{padding:8px;overflow:hidden;}
#searchaddition input{margin:0 5px;}
#searchaddition input[type="text"]{width:170px;}
#searchaddition select{width:176px;margin:0 5px;}
#searchaddition span{display:block;float:left;line-height:34px;font-size:12px;}
.findbtn{text-align:right;margin-right:10px;}
.findbtn input{cursor:pointer;}
.findspan1{ width:33%;}
.findspan2{width:22%;text-align:left;}
.findspan3{width:70%;text-align:left;}
#searchaddition input[type="text"].datetime,#searchaddition input[type="text"].numstyle{width:75px;text-align:center;}
.findspanChec{display:block;float:left;margin-right:4px;}
#searchForm legend{font-size:14px;font-weight:bold;}
.errorflag{color:red;}
</style>
<script src="../lib/jquery.js"></script>
<link rel="stylesheet" href="../lib/datetimepicker/css/jquery.datetimepicker.css">
<script src="../lib/datetimepicker/js/jquery.datetimepicker.js"></script>
<script type="text/javascript">

$(function(){

initParm();//动态加载查询条件表单

//数值框失去焦点
$("input[class=numstyle]").on("blur",function(){
	if($(this).val()!=""){
		if(!IsNum($(this).val())){
			alert("您输入的数据格式不正确!");
			$(this).addClass("errorflag");
		}
	}
});

//数值框获得焦点
$("input[class=numstyle]").on("focus",function(){
	if($(this).hasClass("errorflag"))
		$(this).val("");
	$(this).removeClass("errorflag");
});

});
//获取查询条件
function initParm(){
	$.ajax({
            url : '../../RptCustomAction_initSearch.action', 
            data:{
                rep_id:'<%=uid%>'
            },
            type:"post",
            async:false,
            success : function(data) {
        		$("#searchaddition").html(data.searchParm);
        	
        		//时间查询条件绑定插件 
        		$(".datetime").each(function(){
        			var saveformat = $(this).attr("saveformat");
        			if(saveformat=="" || saveformat=="null" || saveformat==undefined)
        				saveformat="Ymd";
        			$(this).datetimepicker({closeOnDateSelect:true,format: saveformat });
        		});
            }
	 });		
}

//查询数据--点击查询按钮时调用
function searchList(){
	ifmreport.prefrm.refresh();
	$("#currentPage").val("1");
	searchListSub();
}

//翻页查询调用
function searchListSub(){
	$.ajax({
           url : '../../CustomPreviewAction_customPreview.action?rep_id=<%=uid%>', 
           data:$('#searchForm').serialize(),
           type:"post",
           success : function(data) {
           		ifmreport.serachResult(data.body,data.currentPage,data.totalPage,data.pageSize);
           },
           error:function(){alert('数据加载出错，请联系管理员！');},
           beforeSend:function(){$('#ifmreport').contents().find('.Noprint span#pppg').html($("#currentPage").val());$('#ifmreport').contents().find('.Noprint').show();},
           complete:function(){$('#ifmreport').contents().find('.Noprint').hide();}
	});
}

//判断输入的是否为数值
function IsNum(s){
   if (s!=null && s!=""){
       return !isNaN(s);
   }
   return false;
}

</script>
</head>

<body>
<table style="width: 100%; height: 100%; table-layout: fixed;" border="0" cellspacing="0" cellpadding="0">
<tbody>
<tr><td style="overflow: auto;">
<table width="100%" height="100%" style="table-layout: fixed;"><tbody><tr><td><div style="-ms-overflow-x: auto;">
<form id="searchForm" name="searchForm">
<fieldset>
<legend>检索条件</legend>
<div id="searchaddition"></div>
<div class='findbtn'><input type='button' value='查询' id='searchlist' onclick='searchList()'/></div>
<input type="hidden" name="currentPage" id="currentPage" value="1"/>
<input type="hidden" name="pageType" id="pageType" value="1"/>
<input type="hidden" name="pageSize" id="pageSize" value="10"/>
<input type="hidden" name="uid" id="uid" value="<%=uid%>"/>
</fieldset>
</form>
</div></td></tr></tbody></table>
</td></tr>
<tr><td valign="top" height="100%"><div style="width:100%;height:100%;z-index:-1;"><iframe id="ifmreport" name="ifmreport" src="customPreviewIndex.jsp?reporttype=<%=reporttype %>&uid=<%=uid %>" frameborder="0" style="width: 100%; height: 100%;"></iframe></div></td></tr>
</tbody></table>

</body>
</html>
