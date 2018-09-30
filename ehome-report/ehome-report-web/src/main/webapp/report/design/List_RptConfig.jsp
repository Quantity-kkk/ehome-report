﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String tab_name = request.getParameter("tab_name");
	String tab_desc = request.getParameter("tab_desc");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8" />
<title>自定义报表配置</title>
<link href="../common/css/css.css" rel="stylesheet" type="text/css" />
<link href="../common/css/pagination.css" rel="stylesheet" />
<style type="text/css">
.btna {
	height: 20px;
	line-height: 20px;
	display: inline-block;
	cursor: pointer;
	margin-right: 8px;
	color: #115780;
	padding: 0 4px;
}

.btna:hover {
	text-decoration: underline;
}
</style>
<link href="../lib/ui/jquery-ui.min.css" rel="stylesheet" />
<script type="text/javascript" src="../lib/jquery.min.js"></script>
<script src="../lib/ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="../lib/layer/layer.js"></script>
<script type="text/javascript" src="../lib/jquery.pagination.js"></script>
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

		var $currentA;
		$("#reportiterator .tbody").on("click", "a.tabadd", function() {
			var tab_id = $(this).attr("id").split("_")[1];
			$currentA = $(this);
			$.ajax({
				url : "../../RptConfigAction_getColList.action?tab_id=" + tab_id,
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data.length > 0) {
						$currentA.parent().parent().after("<tr><td></td><td colspan='3' id='tabsub" + tab_id + "'></td></tr>");
						$("#tabsub" + tab_id).append("<table><thead><tr><td>编号</td><td>列名称</td><td>列说明</td><td>操作</td></tr></thead><tbody></tbody></table>");

						var str = "";
						for (var i = 0; i < data.length; i++) {
							var updateStr = "showdialog('自定义配置修改','rptColConfig_add.jsp?col_id=" + data[i].col_id + "');";
							str += "<tr><td>" + (i + 1) + "</td><td>" + data[i].col_name + "</td><td>" + data[i].col_desc + "</td><td style='text-align:center;'><a class='btna' onclick='updateCol(\"" + data[i].col_id + "\")'>修改</a><a class='btna' onclick='deleteCol(\"" + data[i].col_id + "\")'>删除</a></td></tr>";
						}
						$("#tabsub" + tab_id + " table tbody").html(str);
						$currentA.html("-");
						$currentA.removeClass("tabadd").addClass("tabdel");

					} else {
						$currentA.parent().parent().after("<tr><td></td><td colspan='3' id='tabsub" + tab_id + "'>没有对应的字段信息，请配置</td></tr>");
						$currentA.html("-");
						$currentA.removeClass("tabadd").addClass("tabdel");
					}
				}
			});
		});
		$("#reportiterator .tbody").on("click", "a.tabdel", function() {
			$(this).parent().parent().next("tr").remove();
			$(this).html("+");
			$(this).removeClass("tabdel").addClass("tabadd");
		});

	});

	//弹出新增窗口
	function showdialog(title, url) {
		var dialog = layer.open({
			title : title,
			type : 2,
			maxmin : false, //开启最大化最小化按钮
			area : [ '700px', '450px' ],
			content : url
		});
	}

	//删除表
	function deleteTab(tab_id) {
		layer.confirm('确认删除？', function(index) {
			$.ajax({
				url : "../../RptConfigAction_delTabById.action?tab_id=" + tab_id,
				type : "post",
				success : function(data) {
					if (data == "1") {
						layer.alert("删除成功", function(index) {
							location.href = location.href;layer.close(index);
						});
					} else if (data == "2") {
						layer.alert("该表包含配置列，若要删除，请先删除配置列");
					} else {
						layer.alert("删除失败，请稍候重试!");
					}
				}
			});
			layer.close(index);
		});
	}

	//删除列
	function deleteCol(col_id) {
		layer.confirm('确认删除？', function(index) {
			$.ajax({
				url : "../../RptConfigAction_delColById.action?col_id=" + col_id,
				type : "post",
				success : function(data) {
					layer.alert("删除成功", function(index) {
						location.href = location.href;layer.close(index);
					});
				}
			});
			layer.close(index);
		});
	}

	//修改列
	function updateCol(col_id) {
		showdialog('自定义配置列修改', 'rptColConfig_add.jsp?&col_id=' + col_id);
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
									<div class="managetitle">自定义报表配置</div>
									<div class="botton"
										onclick="showdialog('自定义配置新增','rptTabConfig_add.jsp');">新增</div>
									<div class="search">
										<form method="post" theme="simple" name="cms_form"
											id="searchform" onsubmit="return search()">
											<input type="text" id="tab_name"
												name="tab_name" value="<%=tab_name == null ? "" : tab_name%>"  style="display:none"/>
											<input type="text" id="tab_desc"
												name="tab_desc" value="<%=tab_desc == null ? "" : tab_desc%>"  placeholder=""/> <input
												type="submit" value="" class="sub" />
										</form>
									</div>
								</div>

								<div id="reportiterator" style="font-size:12px;">
									<table width="94%" border="0" align="center" cellpadding="0"
										cellspacing="0" style="margin-top:10px;">
										<thead>
											<tr class="biaotou">
												<td width="80">编号</td>
												<td>表名</td>
												<td width="200">表名称</td>
												<td width="220">操作</td>
											</tr>
										</thead>
										<tbody id="Searchresult" class="tbody"></tbody>
										<tbody style="display:none" id="hiddenresult">
										</tbody>
									</table>
									<div style="padding:20px 30px;text-align:right;">
										<div id="Pagination" class="pagination"></div>
									</div>
								</div>

							</div>
						</div></td>
				</tr>
			</tbody>
		</table>
	</div>
	<script>
function search(){
	location.href='List_RptConfig.jsp?tab_name='+encodeURIComponent($('#tab_name').val())+'&tab_desc='+encodeURIComponent($('#tab_desc').val());
	return false;
}
$(function(){
	$.ajax({ 
		url : "../../<%=tab_name != null ? "RptConfigAction_getSearchTabList" : "RptConfigAction_getTabList"%>.action", 
		type:"post",
		<%if (tab_name != null) {%>
		data:{'tab_name':$('#tab_name').val(),'tab_desc':$('#tab_desc').val()},
		<%}%>
		success:function(data){
			if(data.flag=='error'){
				top.location.href='../../login.action';
			}else{
				var str=''
				$(data).each(function(index,item){
						str+='<tr>';
						str+='<td>'+(index+1)+'</td>';
						str+='<td>'+item.tab_name+'</td>';
						str+='<td>'+item.tab_desc+'</td>';
						str+='<td><a class="tabadd btna" id=\'tab_'+item.tab_id+'\' style="margin-left:4px;">+</a>';
						str+='<a class="btna" onclick="showdialog(\'自定义配置修改\',\'rptTabConfig_add.jsp?tab_id='+item.tab_id+'\');">修改</a>';
						str+='<a class="btna" onclick="deleteTab(\''+item.tab_id+'\')">删除</a>';
						str+='<a class="btna" onclick="showdialog(\'自定义配置列新增\',\'rptColConfig_add.jsp?tab_id='+item.tab_id+'\')">添加列</a>';
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