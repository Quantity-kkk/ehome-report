<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%
	String reporttype = request.getParameter("reporttype");
	String uid = request.getParameter("uid");
	String preIndex = "designPreviewIndex.jsp";
	//String jasperPrint = (String) session
	//	.getAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
	if (reporttype.equals("C")) {
		preIndex = "designPreviewIndexC.jsp";
		//response.sendRedirect("Demo3.jsp?uid="+uid);
	}
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>报表展示</title>
<%@ include file="../../report/common/head.jsp"%>
<link href="../css/preview.css" rel="stylesheet" type="text/css">
<style type="text/css">
</style>
<script src="../lib/jquery.js"></script>
<script type="text/javascript">
function setValue(){
<%Map map = request.getParameterMap();
			Set keSet = map.entrySet();
			for (Iterator itr = keSet.iterator(); itr.hasNext();) {
				Map.Entry me = (Map.Entry) itr.next();
				Object ok = me.getKey();
				Object ov = me.getValue();
				String[] value = new String[1];
				if (ov instanceof String[]) {
					value = (String[]) ov;
				} else {
					value[0] = ov.toString();
				}

				for (int k = 0; k < value.length; k++) {
					//System.out.println(ok+"="+value[k]); 
					if (!"reporttype".equals(ok) && !"uid".equals(ok)) {
						//将参数附加到form#btnSearch
						out.println("$('#searchaddition').append('<input type=\"hidden\" name=\"" + ok + "\" id=\"" + ok
								+ "\" value=\"" + value[k] + "\">')");
					}
				}
			}%>
}
$(function(){
    setValue();
    initSearchParm('<%=uid%>');
	$(".rt-swicth").bind("click",function(){
		if($(".rt-parmlist").hasClass("on")){
			$(".rt-parmlist").removeClass("on");
		}else{
			$(".rt-parmlist").addClass("on");
		}
		
	});
});
//获取查询条件
function initParm(){
	initSearchParm('<%=uid%>');
}

function initSearchParm(uid){
	$.ajax({
            url : '../../initPreParm.action?uid='+uid, 
            data:$('#btnSearch').serialize(),
            cache:false,
            type:"post",
            success : function(data) {
            	//console.log(data.searchParmJson);
            	if(data.searchParmJson.length<=0){
            		$("#parmlist").hide();
            	}else{
            		createParmHtml(data.searchParmJson);
            		$("#btnSearch").append('<input type="submit" value="查询" id="searchlist" />')
            	}
               // $("#searchaddition").html(data.searchParm);
               // $('#searchlist').click();//自动显示数据，如需要请删除或注释
            	$("#btnSearch").submit();
            }
    });	
}

function createParmHtml(jsonarr){
	$.each(jsonarr,function(index,node){
		var html = $('<div class="form-group"></div>');
		var label = $('<label class="form-label"></label>');
		if(node.cname){
			label.text(node.cname);
		}else{
			label.text(node.key);
		}
		html.append(label);
		if("dynamic"==node.ptype){
			html.hide();
		}
		if(node.showtype&&node.showtype=="select"){
			var select = $('<select name="'+node.key+'" id="'+node.key+'" class="form-select"></select>');
			if(!node.keylist||node.keylist.indexOf(":") == -1){
				select.append("<option value=''></option>");
				select.append("<option value='"+node.value+"'>"+node.value+"</option>");
			}else{
				if(node.keylist.indexOf(",") != -1){
					var keys = node.keylist.split(",");
					select.append("<option value=''></option>");
					for(var i=0;i<keys.length;i++){
						select.append("<option value='"+keys[i].split(":")[1]+"'>"+keys[i].split(":")[0]+"</option>");
					}
				}else{
					select.append("<option value='"+keylist.split(":")[1]+"'>"+keylist.split(":")[0]+"</option>");
				}
			}
			html.append(select);
		
		}else{
			var input = $('<input name="'+node.key+'" id="'+node.key+'" value="'+node.value+'" class="form-input" type="text"/>');
			html.append(input);
		}
		html.appendTo($("#searchaddition"));
	/* 	html.clone().appendTo($("#searchaddition"));
		html.clone().appendTo($("#searchaddition"));
		html.clone().appendTo($("#searchaddition"));
		html.clone().appendTo($("#searchaddition"));
		html.clone().appendTo($("#searchaddition"));
		html.clone().appendTo($("#searchaddition"));
		html.clone().appendTo($("#searchaddition")); */
		
	});
}

</script>
</head>

<body>
	<div class="rt-parmlist on" id="parmlist">
		<div class="rt-side">
			<i class="rt-swicth"></i>
		</div>
		<!--数据请求表单开始 -->
		<form id="btnSearch"
			action="<%=preIndex%>?reporttype=<%=reporttype%>&uid=<%=uid%>"
			method="post" target="ifmreport">
			<div id="searchaddition">
				<!-- post参数放这里 -->
			</div>
		</form>
		<!--数据请求表单结束 -->
	</div>
	<table class="rt-view" border="0" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td valign="top" height="100%"><div
						style="width:100%;height:100%;z-index:-1;">
						<iframe id="ifmreport" name="ifmreport" src="about:blank"
							frameborder="0" style="width: 100%; height: 100%;"></iframe>
					</div></td>
			</tr>
		</tbody>
	</table>

</body>
</html>
