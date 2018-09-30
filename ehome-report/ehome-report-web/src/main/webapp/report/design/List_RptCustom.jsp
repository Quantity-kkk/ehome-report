﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String rep_name = request.getParameter("rep_name");
	if (rep_name == null) {
		rep_name = "";
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8" />
<title>自定义报表管理</title>
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
<script type="text/javascript" src="../lib/jquery.pagination.js"></script>
<script type="text/javascript" src="../lib/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		$(function() {
			$('.tbody tr').hover(function() {
				$(this).children('td').css('background', '#f1f6fc')
			}, function() {
				$(this).children('td').css('background', '#fff')
			})
		})
		window.onload = function() {
			function auto_height() {
				//document.getElementById("box").style.height=document.documentElement.clientHeight-50+"px"; 
				//document.getElementById("content").style.height=document.documentElement.clientHeight-62+"px"; 
			}
			auto_height();
			onresize = auto_height;
		}

	});


	function showdialog(title, url) {
		var dialog = layer.open({
			title : title,
			type : 2,
			maxmin : false, //开启最大化最小化按钮
			area : [ '960px', '500px' ],
			content : url
		});
	}

	//删除报表
	function deleteReport(rep_id) {
		layer.confirm('确认删除？', function(index) {
			$.ajax({
				url : "../../RptCustomAction_delRepById.action?rep_id=" + rep_id,
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

	//查看报表
	function windowOpen(uuid) {
		window.open('customPreviewMain.jsp?reporttype=D&uid=' + uuid, '', 'left=0,top=0,width=' + (screen.availWidth - 10) + ',height=' + (screen.availHeight - 50) + ',toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=no, status=no');
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
									<div class="managetitle">自定义报表列表</div>
									<div class="botton"
										onclick="showdialog('自定义报表设计','rptCustom_add.jsp');">新增</div>
									<div class="search">
										<form method="post" theme="simple" name="cms_form"
											onsubmit="return search()">
											<input type="text" id="rep_name"
												name="rep_name" value="<%=rep_name%>" placeholder="" /> <input
												type="submit" value="" class="sub" />
										</form>
									</div>
								</div>

								<div id="reportiterator">
									<table width="94%" border="0" align="center" cellpadding="0"
										cellspacing="0" style="margin-top:10px;">
										<thead>
											<tr class="biaotou">
												<td width="80">编号</td>
												<td>报表名称</td>
												<td width="120">报表类型</td>
												<td width="80">报表状态</td>
												<td width="120">操作</td>
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
	location.href='List_RptCustom.jsp?rep_name='+encodeURIComponent($('#rep_name').val());
	return false;
}
$(function(){
	$.ajax({ 
		url : "../../<%=rep_name.length() > 0 ? "RptCustomAction_getSearchRepList" : "RptCustomAction_getTabList"%>.action", 
		data:{'rep_name':'<%=rep_name%>'},
		type:"post",
		success:function(data){
			if(data.flag=='error'){
				top.location.href='../../login.action';
			}else{
				var str=''
				$(data).each(function(index,item){
						str+='<tr>';
						str+='<td>'+(index+1)+'</td>';
						str+='<td>'+item.rep_name+'</td>';
						str+='<td>'+(item.rep_type=="1"?"简单报表":"分组报表")+'</td>';
						str+='<td>'+(item.rep_flag=="1"?"启用":"停用")+'</td>';
						str+='<td><a class="btna" style="margin-left:6px;" onclick="windowOpen(\''+item.rep_id+'\')">查看</a>';
						str+='<a class="btna" onclick="deleteReport(\''+item.rep_id+'\')">删除</a>';
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