<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="java.util.*"%>

<% 
	String list = (String)request.getAttribute("list");

 %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>列表</title>
	</head>
	<style>
		table{border-collapse: collapse;background:#ffffff;}
		.dynamic{border:1px solid #000;width:90%;}
		.dynamic .rtitle td{text-align:center;vertical-align:middle;mso-pattern:auto;font-size:14pt;font-weight:700;font-style:normal;font-family:"Microsoft YaHei UI","sans-serif";border-bottom:2px solid #cdcdcd;height: 23.25pt;}
		
		.dynamic .rhead td{ text-align:left;vertical-align:middle;font-size:12pt;font-weight:400;font-style:normal;font-family:"Microsoft YaHei UI","sans-serif";border-bottom:1px solid #bfbfbf;overflow:hidden;}
		
		.dynamic tbody td{height: 26.45pt;color: rgb(0, 0, 0);font-family: "Microsoft YaHei UI";font-size: 10pt;font-weight: 400;border-bottom:1px solid #bfbfbf;text-decoration: none;text-line-through: none;padding-left:3px;padding-right:6px;}
		tfoot td{ text-align:left;vertical-align:middle;padding-left:3px;overflow:hidden;}
	</style>
	<script type="text/javascript">
		$(function(){
			$.ajax({
				type: 'POST',
				url:'/REPORT_SOURCE/report/rbc/previewReportJavabean.action?pageSize=-1&reporttype=D&uuid=fb8043209a85c7eb3e563aea049d2bd5',
				data:{list:'<%=list%>'},
				success:function(data){
					$("#rbc_report_iframe").html(data.body);
				},
				error:function(msg){
					alert("error"+msg);
				}
			});	
		});
	</script>
	<body class="body_bg">
	<h1>说明：本页面为例子，请放在业务系统</h1>
	<s:form method="post" theme="simple" name="cms_form"
		action="CdIqpDocumentInAction_findForList.action">
		<div class="right_bg">
			<div class="right_w">
				<div class="from_bg">
					<div class="right_v">
						<table width="100%" align="center" class="searchstyle">
							<tr>
								<td>
									<dhcc:searchTag property="formiqp0501" mode="query" />
								</td>
								<td>
								    <div class="tools_372">
							            <dhcc:button value="查询" action="查询" commit="true"
								            typeclass="btn_80"></dhcc:button>
						            </div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
		<p class="p_blank">&nbsp;</p>
		
		<div class="right_bg">
			<div class="right_w">
				<div class="from_bg">
					<div class="right_v">
						<div data-options="region:'center',title:''," style="overflow: hidden;margin-top:20px;" id="rbc_report_iframe">
							
						</div>	
					</div>
				</div>
			</div>
		</div>
		
		<!--  
		<div class="right_bg">
			<div class="right_w">
				<div class="from_bg">
					<div class="right_v">
						<div class="tabCont">
							<div class="tabTitle">信息列表</div>
							<dhcc:button value="入库" action="新增" typeclass="t_ico_tj"
								onclick="CdIqpDocumentInAction_input.action"></dhcc:button>
						</div>
						
						<dhcc:tableTag paginate="cdIqpDocumentInList" property="tableiqp0501"
									head="true" />
					</div>
				</div>
			</div>
		</div>
		-->
	</s:form>
	</body>
</html>