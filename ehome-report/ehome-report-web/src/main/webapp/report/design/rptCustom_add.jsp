<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="UTF-8">
		<title>自定义报表设计</title>
		<link href="../lib/ui/jquery-ui.min.css" rel="stylesheet">
		<style>
		 .sbtn{width:70px;}
		 .sbtn input{display:block;margin-top:20px;width:60px;}
		 a{text-decoration:none;}
		 .next{padding:10px 20px;}
		 #condDiv{padding:5px 20px;}
		 .labeltable_middle_td{text-align:center;line-height:30px;font-weight:bold;font-size:16px;padding-bottom:5px;}
		 td select{height:280px;}
		 ol{list-style:none;}
		 .btn-privew,.btn-next{background-color: #3595cc; color: #fff; display: inline-block; font-size: 14px; line-height: 26px; margin-left: 15px; padding: 3px 20px; text-shadow: 1px 1px 0 #393d49; vertical-align: top; z-index: 100;}
		 .btn-next{background-color:#83c56c;}
		 .btn-privew:hover,.btn-next:hover{background-color: #5b5b5b;color: #fff;}
		 /* main-flow */
		 .main-flow{width:480px;margin:10px auto;}
		.main-flow li, .main-flow li.first, .main-flow li a, .main-flow li.last a, .main-flow .current, .main-flow .first-current, .main-flow .last-current, .main-flow .first-current, .main-flow .current a, .main-flow .first-current a, .main-flow .last-current a, .main-flow .last-current a{background:url(images/stepflow.png) no-repeat}
		.main-flow{overflow:hidden;zoom:1;height:28px;}
		.main-flow a{font-size:12px;font-weight:600;text-decoration:none;color:#fff;}
		.main-flow li{float:left;position:relative;z-index:9;background-position:left -164px;line-height:1.0;}
		.main-flow li.first{background-position:left -4px;}
		.main-flow li a{display:block;float:left;position:relative;z-index:10;height:21px;margin:0 -15px 0 0;padding:7px 25px 0;line-height:12px;background-position:right -204px;}
		.main-flow li.first a{padding:7px 25px 0 15px;}
		.main-flow li.last a{background-position:right -84px;}
		.main-flow .current, .main-flow .first-current, .main-flow .last-current{position:relative;z-index:11;background-position:left -244px;}
		.main-flow .first-current{background-position:left -44px;}
		.main-flow .current a, .main-flow .first-current a, .main-flow .last-current a{color:#427c9b;position:relative;z-index:12;background-position:right -284px;}
		.main-flow .first-current a{padding:7px 25px 0 15px;}
		.main-flow .last-current a{background-position:right -124px;}
		 
		</style>
		<script src="../lib/jquery.js"></script>
		<script type="text/javascript" src="../lib/layer/layer.js"></script>
		<script src="../lib/aes.js"></script>
		<script type='text/javascript' src='../js/customAdd.js'></script>
		<script type="text/javascript">
		
			$(function(){
				getAllTables();
				
				//获取表对应字段
				$("#tabNames").on("click",function(){
				    var that=this;
					var tab_id = $(this).find("option:selected").attr("tabid");
					var tab_dsname = $(that).find("option:selected").attr("tabdsname");
					var tab_name = $(this).val();
					$("#result_table_name").val(tab_id);
					$("#result_dsname").val(tab_dsname);
					$.ajax({
						url : "../../RptConfigAction_getColList.action?tab_id="+tab_id, 
						type:"post",
						dataType: "json",
						success : function(data) {
								$("#rpt_column").html("");
								$("#condNames").html("");
								$("#rpt_sortcolumn").html("");
							for(var i=0;i<data.length;i++){
								//判断字段类型
								var colFlag = data[i].col_flag;
								if(colFlag=="1"){
									//展示
									$("#rpt_column").append("<option value='"+data[i].col_name+"'>"+data[i].col_desc+"</option>"); 
								}else if(colFlag=="2"){
									//查询
									$("#condNames").append("<option name='"+data[i].col_name+"' value='"+data[i].col_id+"'>"+data[i].col_desc+"</option>"); 
								}else if(colFlag=="3"){
									//通用
									$("#rpt_column").append("<option value='"+data[i].col_name+"'>"+data[i].col_desc+"</option>"); 
									$("#condNames").append("<option name='"+data[i].col_name+"' value='"+data[i].col_id+"'>"+data[i].col_desc+"</option>"); 
								}
								
								var colSortFlag=data[i].col_sortflag;
								if(colSortFlag=="1")//排序
									$("#rpt_sortcolumn").append("<option value='"+data[i].col_name+"'>"+data[i].col_desc+"</option>"); 
								
							}
						},
						error:function(){
							layer.alert("系统错误，请联系管理员!");
						}
					});
				});
				
				//根据字段id获取字段操作符
				$("#selectedCondNames").click(function(){
					if($(this).val()!=null){
					var col_id = $(this).val().toString().split(",")[0];
					$.ajax({
						url : "../../RptConfigAction_getColById.action?col_id="+col_id, 
						type:"post",
						dataType: "json",
						success : function(data) {
							$("#oper").html("");
							if(data.col_operate !=""){
								var arroperate=data.col_operate.split("@");
								for ( var i = 0; i < arroperate.length; i++) {
									$("#oper").append("<option colid='"+data.col_id+"' value='"+data.col_name+"'>"+arroperate[i]+"</option>");
								}
							}
						},
						error:function(){
							layer.alert("系统错误，请联系管理员!");
						}
					});
					}
				});
				
				//根据运算符重新生成查询条件
				$("#oper").dblclick(function(){
					var col_id = $(this).find("option:selected").attr("colid");
					var col_name = $(this).find("option:selected").val();
					
					var oldStr=$("#selectedCondNames option[value='"+col_id+","+col_name+"']").html();
					$("#selectedCondNames option[value='"+col_id+","+col_name+"']").html(oldStr+"@"+$(this).find("option:selected").html());
					$("#selectedCondNames option[value='"+col_id+","+col_name+"']").attr("name",$(this).find("option:selected").attr("value"));
					$(this).find("option:selected").remove();
				});
				
			});
			
			//获取所有表
			function getAllTables(){
				$.ajax({
					url : "../../RptConfigAction_getTabList.action?accessType=1", 
					type:"post",
					dataType: "json",
					success : function(data) {
						for(var i=0;i<data.length;i++){
							$("#tabNames").append("<option tabid='"+data[i].tab_id+"' tabdsname='"+data[i].tab_dsname+"' value='"+data[i].tab_name+"'>"+data[i].tab_desc+"</option>"); 
						}
					},
					error:function(){
						layer.alert("系统错误，请稍后重试!");
					}
				});
			}
			
			//生成报表
			function createReport(){
				var wf = getWhereField();
				getObjById("condText").value=wf;
				
				//列名存储
				var cnName="";
				$("#rpt_select_column option").each(function(){
					cnName+=$(this).html()+",";
				});
				$("#cn_name").val(cnName);
				var sql=$('#reportSql').val();
				$('#reportSql').val(asencrypt(sql));
				$.ajax({
					url : '../../RptCustomAction_createReport.action', 
					type:"post",
					data:$("#rptForm").serialize(),
					async: false,
					success : function(data) {
						layer.alert("保存成功",function(index){parent.location.href=parent.location.href;layer.close(index);});						
					},complete:function(){
						$('#reportSql').val(sql);
					}
				});	
			}
			
			
		</script>
	</head>
	
	<body>
	<table cellpadding="0" cellspacing="0" width="100%" class="labeltable_top">
	<tr>
		<td align="center" class="steplist">
		<ol class="main-flow condDiv">
		    <li class="first-current"><a href="javascript:changeDiv('condDiv');">1.查询条件</a></li>
		    <li><a href="javascript:changeDiv('styleDiv');">2.报表样式</a></li>
		    <li><a href="javascript:changeDiv('orderDiv');">3.报表排序</a></li>
		    <li><a href="javascript:changeDiv('nameDiv');">4.报表名称</a></li>
		    <li class="last"><a href="javascript:changeDiv('createDiv');">5.生成报表</a></li>
		  </ol>
		  <ol class="main-flow styleDiv" style="display:none">
		    <li class="first"><a href="javascript:changeDiv('condDiv');">1.查询条件</a></li>
		    <li class="current"><a href="javascript:changeDiv('styleDiv');">2.报表样式</a></li>
		    <li><a href="javascript:changeDiv('orderDiv');">3.报表排序</a></li>
		    <li><a href="javascript:changeDiv('nameDiv');">4.报表名称</a></li>
		    <li class="last"><a href="javascript:changeDiv('createDiv');">5.生成报表</a></li>
		  </ol>
		  <ol class="main-flow orderDiv" style="display:none">
		    <li class="first"><a href="javascript:changeDiv('condDiv');">1.查询条件</a></li>
		    <li><a href="javascript:changeDiv('styleDiv');">2.报表样式</a></li>
		    <li class="current"><a href="javascript:changeDiv('orderDiv');">3.报表排序</a></li>
		    <li><a href="javascript:changeDiv('nameDiv');">4.报表名称</a></li>
		    <li class="last"><a href="javascript:changeDiv('createDiv');">5.生成报表</a></li>
		  </ol>
		  <ol class="main-flow nameDiv" style="display:none">
		    <li class="first"><a href="javascript:changeDiv('condDiv');">1.查询条件</a></li>
		    <li><a href="javascript:changeDiv('styleDiv');">2.报表样式</a></li>
		    <li><a href="javascript:changeDiv('orderDiv');">3.报表排序</a></li>
		    <li class="current"><a href="javascript:changeDiv('nameDiv');">4.报表名称</a></li>
		    <li class="last"><a href="javascript:changeDiv('createDiv');">5.生成报表</a></li>
		  </ol>
		  <ol class="main-flow createDiv" style="display:none">
		    <li class="first"><a href="javascript:changeDiv('condDiv');">1.查询条件</a></li>
		    <li><a href="javascript:changeDiv('styleDiv');">2.报表样式</a></li>
		    <li><a href="javascript:changeDiv('orderDiv');">3.报表排序</a></li>
		    <li><a href="javascript:changeDiv('nameDiv');">4.报表名称</a></li>
		    <li class="last-current"><a href="javascript:changeDiv('createDiv');">5.生成报表</a></li>
		  </ol>
		</td>		
	</tr>
	</table>
	
	<!-- 查询条件 -->
	<div id="condDiv" style="display:block;">
		<table cellspacing="0" cellpadding="0" class="labeltable_middle" width="100%">
		<tr> 
			<td class="labeltable_middle_td" colspan="5">1.请选择报表条件并配置条件要素</td> 
		</tr>
		<tr>
			<td valign="top">
				<select name="tabNames" id="tabNames"  multiple="multiple" size="20" style="WIDTH: 200px" class="select">
				</select>
			</td>
			<td  valign="top">
				<select name="condNames" id="condNames"  multiple="multiple" size="20" style="WIDTH: 200px" class="select"  
					ondblclick="copySelectOptions('condNames','selectedCondNames',false);">
				</select>
			</td>
			
			<td align="left" class="sbtn">
				<input type="button" value="右移->"  class="button" onclick="copySelectOptions('condNames','selectedCondNames',false);">
				<input type="button" value="<-左移" class="button" onclick="copySelectOptions('selectedCondNames','condNames',true);">
				<input type="button" value="上移->" class="button"  onclick="moveSelectOptions('selectedCondNames','up')">
				<input type="button" value="下移->" class="button"  onclick="moveSelectOptions('selectedCondNames','down')">
			</td>
			<td align="left" valign="top">
				<select name="selectedCondNames"  id="selectedCondNames" multiple="multiple" size="20"  style="WIDTH: 200px" class="select"> 
				</select>
			</td>
			
			<td align="left" valign="top">
				<select name='oper' id="oper" style='WIDTH: 200px' multiple="multiple" size="20" > 
				</select>
			</td> 
		</tr> 
		<tr> 
			<td align="right" colspan="5" class="next">
			<a class="btn-next" href="javascript:changeDiv('styleDiv');">下一步</a> 
		</tr> 
		</table> 
		<table class="labeltable_bottom"> 
		<tr>
			<td align="center" colspan="3"></td>
		</tr> 
		</table> 
	</div>
	
	<!-- 报表显示字段 -->
	<div id="styleDiv" style='display:none'>
		<table cellspacing='0' cellpadding='0' class='labeltable_middle' width='100%' bordercolor="1" > 
		<tr> 
			<td class='labeltable_middle_td' colspan='5'>2.请选择报表要显示的列</td> 
		</tr> 
		<tr> 
			<td align='right'>
				<select name='rpt_column' id='rpt_column' multiple='multiple' size='19'  style='WIDTH: 200px' class='select'  
					ondblclick="copySelectOptions('rpt_column','rpt_select_column',true);">  
				</select>
			</td> 
			<td align='center' class="sbtn">
				<input type='button' value='右移->' class='button'  
					onclick="copySelectOptions('rpt_column','rpt_select_column',true)">
				<input type='button' value='<-左移' class='button'  
					onclick="copySelectOptions('rpt_select_column','rpt_column',true)">
				<input type='button' value='上移->' class='button'  
					onclick="moveSelectOptions('rpt_select_column','up')">
				<input type='button' value='下移->' class='button'  
					onclick="moveSelectOptions('rpt_select_column','down')">
			</td>
			<td align='left'>
				<select name='rpt_select_column' id='rpt_select_column' multiple='multiple' size='19' style='WIDTH: 200px'  
					ondblclick="copySelectOptions('rpt_select_column','rpt_column',true)">  
				</select>
			</td> 
		</tr> 
		<tr> 
			<td></td> 
		</tr> 
		<tr> 
			<td align='right' colspan='5' class='next'>
			<a class="btn-privew" href="javascript:changeDiv('condDiv');">上一步</a>
			<a class="btn-next" href="javascript:changeDiv('orderDiv');">下一步</a> 
			</td> 
		</tr>
		</table> 
		<table class='labeltable_bottom'>  
		<tr> 
			<td align='center' colspan='3'></td> 
		</tr> 
		</table>
	</div>
	
	<!-- 排序 -->
	<div id="orderDiv" style='display:none'>
	<table cellspacing='0' cellpadding='0' class='labeltable_middle' width='100%' bordercolor="1" > 
		<tr> 
			<td class='labeltable_middle_td' colspan='4'>3.请选择报表的排序字段</td> 
		</tr> 
		<tr> 
			<td align='right'>
				<select name='rpt_sortcolumn' id='rpt_sortcolumn' multiple='multiple' size='19'  style='WIDTH: 200px' class='select'  
					ondblclick="copySelectOptions('rpt_sortcolumn','rpt_select_sortcolumn',true);">  
				</select>
			</td> 
			<td align='center' class="sbtn">
				<input type='button' value='右移->' class='button'  
					onclick="copySelectOptions('rpt_sortcolumn','rpt_select_sortcolumn',true)">
				<input type='button' value='<-左移' class='button'  
					onclick="copySelectOptions('rpt_select_sortcolumn','rpt_sortcolumn',true)">
				<input type='button' value='上移->' class='button'  
					onclick="moveSelectOptions('rpt_select_sortcolumn','up')"> 
				<input type='button' value='下移->' class='button'  
					onclick="moveSelectOptions('rpt_select_sortcolumn','down')">
			</td>
			<td align='left'>
				<select name='rpt_select_sortcolumn' id='rpt_select_sortcolumn' multiple='multiple' size='19' style='WIDTH: 200px'  
					ondblclick="copySelectOptions('rpt_select_sortcolumn','rpt_sortcolumn',true)" onclick="AscDesc($(this).find('option:selected'))">  
				</select>
			</td> 
		</tr> 
		<tr> 
			<td></td> 
		</tr> 
		<tr> 
			<td align='right' colspan='4' class='next'>
			<a class="btn-privew" href="javascript:changeDiv('styleDiv');">上一步</a>
			<a class="btn-next" href="javascript:changeDiv('nameDiv');">下一步</a> 
			</td> 
		</tr>
		</table> 
	</div>
	<form method="post" name="rptForm" id="rptForm">
	<!-- 命名 -->
	<div id="nameDiv" style='display:none'>
	<table cellspacing='0' cellpadding='0' class='labeltable_middle' width='100%' bordercolor="1" height="40%"> 
		<tr> 
			<td class='labeltable_middle_td'>4.请输入报表名称（显示为报表标题）</td> 
		</tr> 
		<tr> 
			<td align='center'>
				报表名称：<input type="text" name="reportFormBean.rep_name" maxlength="50" style=" border: 1px solid #ccc;width:400px;height: 26px">
			</td> 
		</tr> 
		<tr> 
			<td align='center'>
				&nbsp;<br>
			</td>
		</tr>
		<tr> 
			<td align='right' class='next'>
			<a class="btn-privew" href="javascript:changeDiv('orderDiv');">上一步</a>
			<a class="btn-next" href="javascript:changeDiv('createDiv');">下一步</a>
			</td> 
		</tr>
		</table> 
	</div>
	
	<!-- 生成 -->
	<div id="createDiv" style='display:none'>
		<table cellspacing='0' cellpadding='0' class='labeltable_middle' width='100%'>
			<tr>
				<td class='labeltable_middle_td' colspan='2'>5、生成的报表语句 </td>
			</tr>
			<tr>
				<td align='right' width="15%" nowrap>
					生成SQL语句
				</td>
				<td align='left' width="80%">
					<textarea rows='6' cols='100' style="width:700px;height:200px;" id='reportSql' name='reportFormBean.reportSql' readOnly style="display:block"></textarea>
				</td>
			</tr>
			<tr>
				<td align='right' colspan='2' class='next'>
				<a class="btn-next" href="javascript:sqlTest(getObjById('reportSql').value,$('#result_dsname').val());">SQL测试</a>
				<a class="btn-privew" href="javascript:changeDiv('nameDiv');">上一步</a>
			<a class="btn-next" href="javascript:createReport();">生成</a>
				</td>
			</tr>
		</table>
		<table class='labeltable_bottom'>
			<tr>
				<td align='center'>
					<input type="hidden" name="reportFormBean.condText" id="condText" value="">
					<input type="hidden" name="reportFormBean.result_table_name" id="result_table_name" value="">
					<input type="hidden" name="reportFormBean.rep_type" id="rep_type" value="1">
					<input type="hidden" name="reportFormBean.cn_name" id="cn_name" value="">
					<input type="hidden" name="action" id="action" value="customReport">
					<input type="hidden" name="reportFormBean.result_dsname" id="result_dsname" value="">
				</td>
			</tr>
		</table>
	</div>
	</form>
	</body>