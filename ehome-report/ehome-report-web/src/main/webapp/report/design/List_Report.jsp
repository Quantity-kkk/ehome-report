﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String conditionsname = request.getParameter("conditionsname");
	if (conditionsname == null) {
		conditionsname = "";
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8" />
<title>报表配置管理</title>
<link href="../common/css/css.css" rel="stylesheet" type="text/css" />
<link href="../lib/ui/jquery-ui.min.css" rel="stylesheet" />
<link href="../common/css/pagination.css" rel="stylesheet" />
<script type="text/javascript" src="../lib/jquery.min.js"></script>
<script type="text/javascript" src="../lib/jquery.pagination.js"></script>
<script type="text/javascript" src="../lib/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		$('.tbody tr').hover(function() {
			$(this).children('td').css('background', '#f1f6fc')
		}, function() {
			$(this).children('td').css('background', '#fff')
		});
		$(".exit").bind("click", function() {
			location.href = "../../logout.action";
		});
	});
	function deleteReport(name) {
		layer.confirm('确认要删除此模板吗？删除不可恢复！', function(index) {
			$.ajax({
				url : "../../deleteReport.action",
				data : {
					fileName : name + '.xml'
				},
				type : "post",
				success : function(data) {
					location.reload();
				}
			});
			layer.close(index);
		});
	}

	//生成新版本
	function versionReport(uuid, obj) {
		layer.confirm('确认要从该版本生成新的版本吗？', function(index) {
			var nm = $(obj).parent().parent().find("select").eq(2).val();
			var reportVersion = $(obj).parent().parent().find("select").eq(0).find("option:first").attr("value");
			layer.prompt({
				value : nm,
				title : '请输入简要的描述'
			}, function(memo, indexMemo, elemMemo) {
				$.ajax({
					url : "../../versionReport.action",
					data : {
						uuid : uuid,
						reportVersion : reportVersion,
						reportMemo : memo
					},
					type : "post",
					success : function(data) {
						layer.alert(data, function() {
							location.reload();
						})
					}
				});
				layer.close(indexMemo);
			});
			layer.close(index);
		});
	}

	function toMain(t, id, type) {
		if (type == "动态模板") {
			t.href = "Main_D.jsp?uuid=" + id;
		} else if (type == "普通模板") {
			t.href = "Main_N.jsp?uuid=" + id;
		} else {
			t.href = "Main_C.jsp?uuid=" + id;
		}
	}
	function toMain2(t, id, type) {
		if (type == "动态模板") {
			t.href = "Main_D_V2.jsp?uuid=" + id;
		} else if (type == "普通模板") {
			t.href = "Main_N.jsp?uuid=" + id;
		} else {
			t.href = "Main_C.jsp?uuid=" + id;
		}
	}
	function privew(type, id) {
		if (type == "动态模板") {
			return "Demo.jsp?reporttype=D&uid=" + id;
		} else if (type == "普通模板") {
			return "Demo.jsp?reporttype=N&uid=" + id;
		} else {
			return "Demo.jsp?reporttype=C&uid=" + id;
		}
	}
	//选择版本号
	function changeVersion(obj) {
		var selectindex = $(obj).prop("selectedIndex");
		//设置最近修改时间
		$(obj).parent().next().next().html($(obj).next().find("option").eq(selectindex).attr("value"));
		//设置title为“简要说明”
		$(obj).attr("title", $(obj).next().next().find("option").eq(selectindex).attr("value"));
		//设置修改、删除、生成新版本超链接的onclick事件
		var uuid = $(obj).next().next().next().find("option").eq(selectindex).attr("value");
		var reportType = $(obj).parent().next().html();
		$(obj).parent().parent().find("td:last").find("a").eq(0).attr("onclick", "toMain2(this, '" + uuid + "', '" + reportType + "')");
		//$(obj).parent().parent().find("td:last").find("a").eq(1).attr("onclick", "toMain2(this, '" + uuid + "', '" + reportType + "')");
		$(obj).parent().parent().find("td:last").find("a").eq(1).attr("onclick", "deleteReport('" + uuid + "');");
		$(obj).parent().parent().find("td:last").find("a").eq(2).attr("onclick", "versionReport('" + uuid + "', this);");
		//设置报表名称超链接
		$(obj).parent().parent().find("a").eq(0).attr("onmouseover", "$(this).attr('href',privew('" + reportType + "','" + uuid + "'))");
	}
</script>
</head>
<body>
	<div class="main">
		<table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
			<tbody>
				<tr>
					<td valign="top"></td>
					<td valign="top"><div>
							<div class="content">
								<div class="manage">
									<div class="managetitle">报表配置</div>
									
									<div class="botton" onclick="window.open('Main_N.jsp')"
										style="display:none">添加普通模板</div>
									<div class="botton" onclick="window.open('Main_D_V2.jsp')">添加动态模板</div>
								    <div class="botton" onclick="window.open('Main_D_V2.jsp')">添加动态模板v2</div>
								    <div class="botton" onclick="window.open('Main_C.jsp')">添加图表模板</div>
									<div class="search">
										<form id="cms_form" name="cms_form" action=""
											onsubmit="return search()" >
											<input type="text" id="conditionsname"
												name="conditionsname" value="<%=conditionsname%>" placeholder=""/> <input
												type="submit" value="" class="sub" />
										</form>
									</div>
								</div>
								<table width="94%" border="0" align="center" cellpadding="0"
									cellspacing="0" style="margin-top:10px;">
									<tr class="biaotou">
										<td height="35">文件名</td>
										<td width="200">版本</td>
										<td width="200">模板类型</td>
										<td width="200">最后修改时间</td>
										<td width="250">操作</td>
									</tr>
									<tbody id="Searchresult" class="tbody"></tbody>
									<tbody style="display:none" id="hiddenresult">
									</tbody>
								</table>

								<div style="padding:20px 30px;text-align:left;">
									<div id="Pagination" class="pagination"></div>
								</div>
							</div>
						</div></td>
				</tr>
			</tbody>
		</table>
	</div>
	<script>
function search(){
	location.href='List_Report.jsp?conditionsname='+encodeURIComponent($('#conditionsname').val());
	return false;
}
$(function(){
		$.ajax({ 
			url : "../../selectReportFileAll.action", 
			type:"post",
			data:{'conditions.name':'<%=conditionsname%>'},
			success:function(data){
				if(data.flag=='error'){
					top.location.href='../../login.action';
				}else{
					var str=''
					$(data).each(function(index,item){
							str+='<tr>';
							str+='<td><a href="javascript:void(0)" target="_blank" onmouseover="$(this).attr(\'href\',privew(\''+item.reportStyle+'\',\''+item.uuid+'\'))">'+item.name+'</a></td>';
							str+='<td><select name="reportVersion" id="reportVersion" onchange="changeVersion(this);">';
							$(item.reportFiles).each(function(ind,elem){
								str+='<option value='+elem.reportVersion+'>'+elem.reportVersion+'</option>';
							});
							str+='</select><select name="" style="display:none;">';
							$(item.reportFiles).each(function(ind,elem){
								str+='<option value='+elem.lastEditDate+'>'+elem.lastEditDate+'</option>';
							});
							str+='</select><select name="" style="display:none;">';
							$(item.reportFiles).each(function(ind,elem){
								str+='<option value='+elem.reportMemo+'>'+elem.reportMemo+'</option>';
							});
							str+='</select><select  name="" style="display:none;">';
							$(item.reportFiles).each(function(ind,elem){
								str+='<option value='+elem.uuid+'>'+elem.uuid+'</option>';
							});
							str+='</select></td>';
							str+='<td>'+item.reportStyle+'</td>';
							str+='<td></td><td>';
							str+='<a href="javascript:void(0)" target="_blank" onclick="toMain2(this,\''+item.uuid+'\',\''+item.reportStyle+'\')" title="修改" >编辑</a>&nbsp;';
							//str+='<a href="javascript:void(0)" target="_blank" onclick="toMain2(this,\''+item.uuid+'\',\''+item.reportStyle+'\')" title="修改" >编辑v2</a>&nbsp;';
							str+='<a href="javascript:void(0)" onclick="deleteReport(\''+item.uuid+'\');" title="删除">删除</a>&nbsp;';
							str+='<a href="javascript:void(0)" onclick="versionReport(\''+item.uuid+'\', this);" title="生成新版本">新增</a>';
							str+='</td>';
							str+='</tr>';
						});
					
					$('#hiddenresult').html(str);
					//总数目
					var length = $("#hiddenresult tr").length;
					$("#Pagination").pagination(length, {
					    num_edge_entries: 2,
					    num_display_entries: 4,
					    callback: pageselectCallback,
					    items_per_page:10
					}); 
					
					//设置最近修改时间
					$("select[name=reportVersion]").each(function() {
						changeVersion(this);
					});
					
				}
			},befenSend:function(){layer.load()},complete:function(){layer.closeAll('loading')}
		});
	function pageselectCallback(page_index, jq){
		
		var items_per_page =10;
		var max_elem = Math.min((page_index+1) * items_per_page, $("#hiddenresult tr").length);
		
		$("#Searchresult").html("");
		// 获取加载元素
		for(var i=page_index*items_per_page;i<max_elem;i++){
			$("#Searchresult").append($("#hiddenresult tr:eq("+i+")").clone());
		}
	}
});
</script>
</body>
</html>
