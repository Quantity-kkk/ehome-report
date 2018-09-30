﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
	String tab_id = request.getParameter("tab_id");
String accessType=request.getParameter("accessType");
String col_id=request.getParameter("col_id");
 %>
<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <title>自定义配置列新增</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../lib/ui/jquery-ui.min.css"/>
    <script type="text/javascript" src="../lib/jquery.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script src="../lib/vl/jquery.validate.min.js"></script>
    <script src="../lib/vl/localization/messages_zh.js"></script>
    <script type="text/javascript" src="../lib/layer/layer.js"></script>
    <style type="text/css">
        body {font-size:13px;font-weight: normal;margin:0;}
        .clearfix{clear: both;}
        .coltable{width:100%;}
        .colform{margin:15px 0 0 0;border:0;padding:0;line-height:30px;font-size:13px;}
        .colform p{marign:0;padding:0;border:0;text-align:center;}
        .colform input {border: 1px solid #ccc;height: 26px; margin-right: 10px;width:200px;}
        .colform textarea{border: 1px solid #ccc;}
        .colform select{height: 31px;border: 1px solid #ccc;width:204px;}
        .colform .submit{width:60px;padding:5px 20px;background: none repeat scroll 0 0 #277b9d;border: 1px solid #ccc;color:#fff;height: 30px;}
        
        .colstyle{text-align:right;padding-right:6px;width:15%;}
    </style>
    <script type="text/javascript">
    
    $(function(){
    	var coltype=$("#col_type").val();
    	$("#col_operate").html(getOption(coltype));
    	
    	//列类型切换事件
    	$("#col_type").change(function(){
    		var coltype = $("#col_type").val();
    		$("#col_operate").html(getOption(coltype));
    	});
    });
    
    //根据列类型获取运算符选项
    function getOption(coltype){
    	var optionstr="";
    	if(coltype=="1"){
    			//字符串
   			optionstr="<option value=''></option><option value='like'>like</option><option value='='>=</option>";
   		}else if(coltype=="2" || coltype=="3"){
   			//数值、日期
   			optionstr="<option value=''></option><option value='>@<'>>@<</option><option value=' >=@<='> >=@<=</option>"+
   					  "<option value='>'>></option><option value='<'><</option>"+
   					  "<option value='>='>>=</option><option value='<='><=</option>";
   		}else if(coltype=="4"){
   			//多选
   			optionstr="<option value=''></option><option value='in'>in</option>";
   		}else{
   			//下拉选
   			optionstr="<option value=''></option><option value='='>=</option>";
   		}
   		return optionstr;
    }
    
    //保存
    function saveCol(url,operFlag){
    	var flag=true;
    	
    	//列名称不能为空
    	if($("#col_name").val()==""){
    		layer.alert("列名称不能为空!");
    		flag = false;
    		return false;
    	}
    	
    	//列描述不能为空
    	if($("#col_desc").val()==""){
    		layer.alert("列描述不能为空!");
    		flag = false;
    		return false;
    	}
    	
    	if($("#col_type").val()=="4" || $("#col_type").val()=="5"){
    		if($("#col_dic").val()==""){
    			layer.alert("请配置数据字典!");
    			flag = false;
    			return false;
    		}
    	}
    	
    	//当类型为日期，且为查询条件时,必须选择日期类型
    	if($("#col_type").val()=="3" && ($("#col_flag").val()=="2" || $("#col_flag").val()=="3")){
    		if($("#save_format").val()==""){
    			layer.alert("请配置存储格式!");
    			flag = false;
    			return false;
    		}
    	}
    	
    	//当选择是查询条件时，必须配置运算符
    	if($("#col_flag").val()=="2" && $("#col_operate").val()==""){
   			layer.alert("请配置运算符!");
   			flag = false;
   			return false;
    	}
    	
    	if(flag){
    		$.ajax({
				url : url, 
				type:"post",
				data:$('#colform').serialize(),
				async: false,
				success : function(data) {
					if(operFlag=="1"){
							if(data=="1"){
								layer.alert("新增保存成功!",function(){parent.location.href=parent.location.href;});								
							}else if(data=="2"){
								layer.alert("列名已在该表中已存在，请重新输入!");
							}
							else{
								layer.alert("新增保存失败!");
							}
						}else{
							if(data=="1"){
								layer.alert("修改保存成功!",function(){parent.location.href=parent.location.href;});								
							}else if(data=="2"){
								layer.alert("列名已在该表中已存在，请重新输入!");
							}
							else{
								layer.alert("修改保存失败!");
							}
						}
				}
			});
    	}
    	
    }
    <%if(col_id!=null){%>
    $.ajax({
		url : "../../RptConfigAction_getColById.action?<%=accessType!=null?"accessType="+accessType+"&":""%>col_id=<%=col_id%>", 
		type:"post",
		success : function(data) {
			$('#tab_id').val(data.tab_id);
			$('#col_id').val(data.col_id);
			$('#col_name').val(data.col_name);
			$('#col_desc').val(data.col_desc);
			$('#col_type').val(data.col_type);
			$('#show_format').val(data.show_format);
			$('#col_flag').val(data.col_flag);
			$('#col_sortflag').val(data.col_sortflag);
			$('#col_operate').val(data.col_operate);
			$('#col_order').val(data.col_order);
			$('#field_type').val(data.field_type);
			$('#col_size').val(data.col_size);
			$('#save_format').val(data.save_format);
			$('#col_dic').text(data.col_dic);
		}
	});
    <%}%>
    </script>
</head>
<body>
<div class="clearfix"></div>
<div>
    <div class="right">
        <form method="post" class="colform" id="colform">
        	 <%if(col_id!=null){%>
        	<input type="hidden" name="tab_id" id="tab_id" value=""/>
        	<%}else{ %>
        	<input type="hidden" name="tab_id" id="tab_id" value="<%=tab_id%>"/>
        	<%} %>
        	<input type="hidden" id="col_id" name="col_id" value=""/>
        	<table class="coltable">
        		<tr>
        			<td class="colstyle"><span style="color:red;padding:0 2px;">*</span>列名称</td>
        			<td width="60px;"><input type="text" id="col_name" name="col_name" value="" required/></td>
        			<td class="colstyle"><span style="color:red;padding:0 2px;">*</span>列描述</td>
        			<td><input type="text" id="col_desc" name="col_desc" value=""/></td>
        		</tr>
        		<tr>
        			<td class="colstyle">列类型</td>
        			<td>
        			
        				<select name="col_type" id="col_type" class="tdselect">
							<option value="1">字符串</option>
							<option value="2">数值</option>
							<option value="3">日期</option>
							<option value="4">多选</option>
							<option value="5">下拉选</option>
						</select>
        			</td>
        			<!-- <td class="colstyle">显示格式</td>
        			<td>
        				<input id="show_format" name="show_format" value="">
        			</td> -->
        			<td class="colstyle">列长度</td>
        			<td>
        				<input type="text" name="col_size" id="col_size" value=""/>
        			</td>
        		</tr>
        		<tr>
        			<td class="colstyle">列标志</td>
        			<td>
        				<select name="col_flag" id="col_flag" class="tdselect">
							<option value="1">展示</option>
							<option value="2">查询</option>
							<option value="3">通用</option>
							<option value="4">暂时不用</option>
						</select>
        			</td>
        			<td class="colstyle">排序</td>
        			<td>
        				<select name="col_sortflag" id="col_sortflag" class="tdselect">
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
        			</td>
        		</tr>
        		<tr>
        			<td class="colstyle">运算符</td>
        			<td>
        				<select id="col_operate" name="col_operate">
        				</select>
        			<!--  	<input type="text" name="col_operate" id="col_operate" value="<s:property value='colConfigBean.col_operate'/>"/>
        				<div class="seloperate" style="display:none;border: 1px solid #0000aa; overflow: hidden; width: 18%; position: absolute; background-color: #ccffff;">
        					<select id="sel_oper" style="width:100px;">
        					</select>
        				</div>
        				-->
        			</td>
        			<td class="colstyle">次序</td>
        			<td>
        				<input type="text" name="col_order" id="col_order" value=""/>
        			</td>
        		</tr>
        		<tr>
        			<td class="colstyle">字段类型</td>
        			<td>
        				<input type="text" name="field_type" id="field_type" value=""/>
        			</td>
        			<td class="colstyle" valign="top">数据字典</td>
        			<td>
        				<textarea id="col_dic" name="col_dic" style="width:200px;font-size:13px;"></textarea>
        			</td>
        		</tr>
        		<tr>
        		<td colspan="4" style="padding:0 20px">数据字典示例说明：<br>普通(键-值)：【<span style="color:red">1-正常|2-关注|3-可疑|4-次级|5-损失</span>】<br>
        				动态(键：opt_code,值：opt_name)：【<span style="color:red">select opt_code,opt_name from parm_dic where key_name='FIVE_STS'</span>】</td>
        		</tr>
        		<!-- <tr>
        			<td class="colstyle" valign="top">存储格式</td>
        			<td valign="top">
        				<input id="save_format" name="save_format" value="">
        			</td>
        			
        		</tr> -->
        	</table>
            <p>
            	 <%if(col_id!=null){%>
                	<input id="submit" class="submit" type="button" value="保存" onclick="saveCol('../../RptConfigAction_updateCol.action','2')"/>
            	<%}else{ %>
            		<input id="submit" class="submit" type="button" value="保存" onclick="saveCol('../../RptConfigAction_insertCol.action','1')"/>
            	<%} %>
            </p>
        </form>
    </div>
</div>
</body>
</html>