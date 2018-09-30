﻿<%@ page language="java" import="java.util.*,report.java.rpt.util.*" pageEncoding="utf-8" %>

<%
	String dataSourceName = RptGlobal.DATA_SOURCE;
String tab_id=request.getParameter("tab_id");
 %>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <title>自定义配置新增</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../lib/ui/jquery-ui.min.css"/>
    <script type="text/javascript" src="../lib/jquery.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script src="../lib/vl/jquery.validate.min.js"></script>
    <script src="../lib/vl/localization/messages_zh.js"></script>
    <script type="text/javascript" src="../lib/layer/layer.js"></script>
    <style type="text/css">
        body {font-size:13px;font-weight: normal;margin:0;}
        #top p{display:block;float:left;margin-left:10px;}
        #top #dslist {width: 200px;}
        .clearfix{clear: both;}
        .right{width:90%;}
        .tabform{margin:0;border:0;padding:0;line-height:1.8;}
        .tabform p{marign:0;padding:0;border:0;}
        .tabform label.p{display:inline-block;width:100px;padding:0 10px;text-align:right;}
        .tabform label.error{display:inline-block;color:red;padding-left:10px;}
        .tabform input {border: 1px solid #ccc;height: 26px; margin-right: 10px;}
        .tabform select{height: 31px;border: 1px solid #ccc;width:204px;}
        .tabform .txt{width:200px;}
        .tabform .txtp{width:100px;height:20px;}
        .tabform .sel{width:204px;height:26px;}
        .tabform .submit{margin-left:130px;padding:5px 20px;background: none repeat scroll 0 0 #277b9d;border: 1px solid #ccc;color:#fff;height: 30px;}
    </style>
    <script type="text/javascript">
    	$(function(){
    		//gettablename("zlxt","");
    		if(GetQueryString("tab_id")==null){
    			getdataSourceBeanList();
    		}
    	});
    	function GetQueryString(name)
		{
		     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		     var r = window.location.search.substr(1).match(reg);
		     if(r!=null)return  unescape(r[2]); return null;
		}
    	//获取数据源列表
    	function getdataSourceBeanList(){
    		$.ajax({
				url : "../../getdataSourceBeanList.action", 
				type:"post",
				async: false,
				success : function(data) {
					$("#dsname").empty();
					for(var i=0;i<data.length;i++){
						$("#dsname").append("<option value='"+data[i].dataSourceName+"'>"+data[i].dataSourceName+"</option>"); 
					}
				}
			});
    	}
    	//根据数据源获取表名
        function gettablename(datasourcename,tableName){
        	$.ajax({
				url : "../../getDataTableNameAll.action", 
				type:"post",
				async: false,
				data:{dataSourceName : datasourcename},
				success : function(data) {
					$("#datatable").empty();
					for(var i=0;i<data.length;i++){
						$("#datatable").append("<option value='"+data[i]+"'>"+data[i]+"</option>"); 
					}
					if(tableName!=""){
						$("#datatable").val(tableName);
					}
				}
			});
        }
        
        //保存  
        function saveTab(url,operFlag){
        	var flag=true;
        	if($("#tab_name").val()==""){
        		layer.alert("请输入表名!");
        		flag=false;
        		return false;
        	}
        	
        	if($("#tab_desc").val()==""){
        		layer.alert("请输入表名称!");
        		flag=false;
        		return false;
        	}
        	if($("#dsname").val()==""){
        		layer.alert("请选择数据源!");
        		flag=false;
        		return false;
        	}
        	if(flag){
        		$.ajax({
					url : url, 
					type:"post",
					data:$('#tabform').serialize(),
					async: false,
					success : function(data) {
						if(operFlag=="1"){
							if(data=="1"){
								layer.alert("新增保存成功!",function(index){parent.location.href=parent.location.href;layer.close(index);});								
							}else if(data=="2"){
								layer.alert("表名已存在，请重新输入!");
							}
							else{
								layer.alert("新增保存失败!");
							}
						}else{
							if(data=="1"){
								layer.alert("修改保存成功!",function(index){parent.location.href=parent.location.href;layer.close(index);});								
							}else if(data=="2"){
								layer.alert("表名已存在，请重新输入!");
							}
							else{
								layer.alert("修改保存失败!");
							}
						}
					}
				});
        	}
        	
        }
        <%if(tab_id!=null){ %>
        $.ajax({
			url :'../../RptConfigAction_getTabById.action?tab_id=<%=tab_id%>',
			type:'post',
			success :function(data){
       				var item=data;
       				$('#dsname').empty();
       				$('#dsname').append('<option value="'+item.tab_dsname+'">'+item.tab_dsname+'</option>');
       				$('#tab_name').val(item.tab_name);
       				$('#tab_desc').val(item.tab_desc);
       			}
        });
        <%}%>
    </script>
</head>
<body>
<div class="clearfix"></div>
<div>
    <div class="right">
        <form method="post" class="tabform" id="tabform" >
        	<input type="hidden" name="tab_id" value="<%=tab_id!=null?tab_id:""%>"/>
            <p style="display:block;">
                <label class="p">数据源名称</label>
                 <select name="DataSourceName" id="dsname" title="请选择数据源" class="tdselect" required>
                       <option value=""></option>
	            </select>
            </p>
            <p style="display:none;">
                <label class="p">表类型</label>
                <input type="hidden" name="tab_type" value="1"/>
            </p>
            <p>
                <label class="p">表名</label>
                <input type="text" name="tab_name" class="txt" id="tab_name" value="" required/>
                <label style="margin-left:6px;">注：此处输入数据库中表的真实名称</label>
                <!--  
                <select name="tab_name" id="datatable" title="请选择数据表" class="tdselect" required>
	                	<option value=""></option>
	            </select>
	            -->
            </p>
            <p>
                <label class="p">表名称</label>
                <input type="text" name="tab_desc" class="txt"  id="tab_desc" value="" required/>
                <label style="margin-left:6px;">注：此处输入数据库表的中文描述</label>
            </p>
           
            <p>
            <%if(tab_id!=null){ %>
            		<input id="submit" class="submit" type="button" value="保存" onclick="saveTab('../../RptConfigAction_updateTab.action','2')"/>
            		<%}else{ %>
            		<input id="submit" class="submit" type="button" value="保存" onclick="saveTab('../../RptConfigAction_insertTab.action','1')"/>
            		<%} %>
                
            </p>
            <input id="reset" type="reset" style="display:none"/>
                
        </form>
    </div>
</div>
</body>
</html>