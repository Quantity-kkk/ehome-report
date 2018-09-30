<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<title>报表预览</title>
<link href="../css/preview.css" rel="stylesheet" type="text/css">
<style type="text/css">
html, body {
	height: 100%
}

body {
	margin: 0px;
	padding: 0px;
	overflow: hidden;
	font-size: 12px;
	font-family: "Microsoft YaHei", SimSun, Arial, Sans-Serif;
}

.searchlist {
	padding: 0px;
}

.searchlist p {
	display: inline-block;
	width:30%;
	margin:2px 0;
	padding-top: 5px;
	text-align: right;
}

.searchlist input, .searchlist select {
	margin: 0;width:226px;
}
#searchadditionnew input{width:226px}

.searchlist input.btnsearch {
	float: right;
	margin: 4px 0;
	padding:3px 15px;
	width:auto;
	margin-right: 10%;
}
</style>
<link rel="stylesheet" type="text/css" href="../lib/multiselectSrc/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="../lib/assets/style.css" />
<link rel="stylesheet" type="text/css" href="../lib/assets/prettify.css" />
<link rel="stylesheet" type="text/css" href="../lib/ui/jquery-ui.min.css" />
<script type="text/javascript" src="../lib/jquery.js"></script>
<script type="text/javascript" src="../lib/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="../lib/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="../lib/assets/prettify.js"></script>
<script type="text/javascript" src="../lib/multiselectSrc/jquery.multiselect.js"></script>
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
						out.println("$('#searchaddition').append('<input type=\"hidden\" name=\"" + ok
								+ "\" id=\"" + ok + "\" value=\"" + value[k] + "\">')");
						out.println("$('#searchadditionnew').append('<input type=\"hidden\" name=\"" + ok
								+ "\" id=\"" + ok + "\" value=\"" + value[k] + "\">')");
					}
				}
			}%>
}
$(function(){
	setValue();
	initParm();
});
//获取查询条件
function initParm(){
	var parmsExtModel;
		$.ajax({
			 url : '../jrreport/initPreParms.action?uid=<%=uid%>', 
            cache:false,
			type : "post",
			success : function(data) {
				parmsModel = data.parmsModel;
				parmsExtModel = data.parmsExtModel;
				initSearchParm(parmsModel,0);
				initSearchParm(parmsExtModel,1);
			}
		});
	}

	function initSearchParm(parmsModel,isext) {
		if(isext==1){
			$.ajax({
				url : '../jrreport/initPreParmExt.action',
				cache : false,
				data:$('#newSearch').serialize()+"&parmsModel="+parmsModel,
				type : "post",
				async:false,
				success : function(data) {
					$("#searchadditionnew").html(data.searchParm);
					$("select[multiple='multiple']").multiselect({
				        noneSelectedText: "==请选择==",
				        checkAllText: "全选",
				        uncheckAllText: '全不选',
				        selectedList:4
				    });
				}
			});
		}else{
			$.ajax({
				url : '../jrreport/initPreParm.action',
				cache : false,
				data:$('#btnSearch').serialize()+"&parmsModel="+parmsModel,
				type : "post",
				async:false,
				success : function(data) {
					$("#searchaddition").html(data.searchParm);
					$('#searchlist').click();//自动显示数据，如需要请删除或注释
				}
			});
		}
	}
	function getopt(op,v,dbtype){
		if(dbtype=="text"||dbtype=="date"){
			v="'"+v+"'";
			if(dbtype=="date"){
				v=v.replace(/-/g, '');
			}
		}
		if(op=="in"||op=="notin"){
			v=v.replace(/,/g,"','");
		}
		var ret="=";
		switch(op){
		case "eq":
			ret="="+v;
			break;
		case "!eq":
			ret="!="+v;
			break;
		case "lt":
			ret=">"+v;
			break;
		case "gt":
			ret="<"+v;
			break;
		case "lteq":
			ret=">="+v;
			break;
		case "rteq":
			ret="<="+v;
			break;
		case "in":
			ret=" in ("+v+")";
			break;
		case "notin":
			ret=" not in ("+v+")";
			break;
		}
		return ret;
	}
	function dosearch(){
		var sqlStr="1=1";
		$('#searchadditionnew input').each(function(index,element){
			if($(this).val()!=''&&$(this).val()!=null){
				sqlStr+=" and "+$(this).attr('name');
				sqlStr+=getopt($(this).attr('vl'),$(this).val(),$(this).attr('type'));
			}
		});
		$('#searchadditionnew select').each(function(index,element){
			var v;
			if($(this).attr('multiple')!=undefined){
				v=$(this).multiselect("MyValues");
			}else{
				v=$(this).val();
			}
			if(v!=''&&v!=null){
				sqlStr+=" and "+$(this).attr('name');
				sqlStr+=getopt($(this).attr('vl'),v,$(this).attr('type'));
			}
		});
		$('#searchaddition input').each(function(index){
			if(index==0){
				$(this).val(sqlStr);
				console.log($(this).val())
			}
		});
		$('#searchlist').click();//请求数据
	}
</script>
</head>

<body>
	<table style="width: 100%; height: 100%; table-layout: fixed;" border="0" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td style="overflow: auto;">
					<table width="100%" height="100%" style="table-layout: fixed;">
						<tbody>
							<tr>
								<td><div class="searchlist" style="-ms-overflow-x: auto;">
										<!--数据请求表单开始 -->
										<form id="btnSearch" action="<%=preIndex%>?reporttype=<%=reporttype%>&uid=<%=uid%>" method="post" target="ifmreport" style="display:none">
											<div id="searchaddition">
												<!-- post参数放这里 -->
											</div>
											<input type="submit" value="查询" id="searchlist"/>
										</form>
										<form id="newSearch" action="<%=preIndex%>?reporttype=<%=reporttype%>&uid=<%=uid%>" method="post" target="ifmreport">
											<div id="searchadditionnew">
												<!-- post参数放这里 -->
											</div>
											<input type="button" class="btnsearch" value="查询" onclick="dosearch()"/>
										</form>
										<!--数据请求表单结束 -->
									</div></td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td valign="top" height="100%"><div style="width:100%;height:100%;z-index:-1;">
						<iframe id="ifmreport" name="ifmreport" src="about:blank" frameborder="0" style="width: 100%; height: 100%;"></iframe>
					</div></td>
			</tr>
		</tbody>
	</table>

</body>
</html>
