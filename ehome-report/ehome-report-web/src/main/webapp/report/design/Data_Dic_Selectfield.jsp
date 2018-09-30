﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <title>数据集配置</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../lib/ui/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../lib/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="../lib/jquery.min.js"></script>
    <script src="../lib/ztree/jquery.ztree.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script src="../lib/vl/jquery.validate.min.js"></script>
    <script src="../lib/vl/localization/messages_zh.js"></script>
    <script src="../js/common.js"></script>
 	<script>
 	var dataSetName='<%=new String(request.getParameter("dataSetName").getBytes("ISO-8859-1"),"UTF-8")%>';
 	var dicType;
 	var dicID;
 	var dicText;
 	$(function() {
        $('#dicType').empty();
        $('#dicID').empty();
        $('#dicText').empty();
        $('#dicType').append('<option value="">请选择字典类型</option>');
        $('#dicID').append('<option value="">请选择字段名称</option>');
        $('#dicText').append('<option value="">请选择值名称</option>');
        $('#dicType').selectmenu({
        	change:function(){
        		dicType = this.value;
        	}
        });
        $('#dicID').selectmenu({
        	change:function(){
        		dicID = this.value;
        	}
        });
        $('#dicText').selectmenu({
			change:function(){
				dicText = this.value;
        	}
        });
        var obj=$(parent.rdes.document.getElementById('dataSetsModel').value);
        var df=obj.find('dataset[name='+dataSetName+']');
        df.find('dataset').detach();//ie bug track;
        df.find('Fields>Field').each(function(i){
        	$('#dicType').append('<option value="'+$(this).attr('name')+'">'+$(this).attr('name')+'</option>')
        	$('#dicID').append('<option value="'+$(this).attr('name')+'">'+$(this).attr('name')+'</option>')
        	$('#dicText').append('<option value="'+$(this).attr('name')+'">'+$(this).attr('name')+'</option>')
        });
        $('#dicType').selectmenu('refresh', true);
        $('#dicID').selectmenu('refresh', true);
        $('#dicText').selectmenu('refresh', true);
    });
 	function submit(){
 		var data = parent.rdes.dicData[dataSetName];
 		var dic = {};
 		for(var i=0;i<data.length;i++){
 			if(dic[data[i][dicType]]==null){
 				dic[data[i][dicType]] = {};
 				dic[data[i][dicType]][data[i][dicID]] = data[i][dicText];
 			}else{
 				dic[data[i][dicType]][data[i][dicID]] = data[i][dicText];
 			}
 		}
 		
 		//var obj=$(parent.rdes.document.getElementById('dataSetsModel').value);
        //var df=obj.find('dataset[name='+dataSetName+']');
        //df.find('dataset').detach();//ie bug track;
        //df.find('fields>field').each(function(i){
        //	if($(this).attr('name')==dicType){
        //		$(this).attr('dicType',"type");
        //	}else if($(this).attr('name')==dicID){
        //		$(this).attr('dicType',"code");
        //	}else if($(this).attr('name')==dicText){
        //		$(this).attr('dicType',"text");
        //	}
        //});
        var obj=String2XML(parent.rdes.document.getElementById('dataSetsModel').value);
        var objln = obj.childNodes[0].childNodes.length;
        for(var i=0;i<objln;i++){
		var dss = obj.childNodes[0].childNodes[i];
		var dsName = dss.getAttribute('name');
		if(dsName==dataSetName){
		var fields=dss.getElementsByTagName("fields")[0].childNodes;
		for(var f=0;f<fields.length;f++){
			var fname=fields[f].getAttribute("name");
			if(fname==dicType){
			fields[f].setAttribute("dicType","type");
			}else if(fname==dicID){
			fields[f].setAttribute("dicType","code");
			}else if(fname==dicText){
			fields[f].setAttribute("dicType","text");
			}
		}
		}
		}
 		
        parent.rdes.document.getElementById('dataSetsModel').value = XML2String(obj);
 		dic.dicDataType = dicType;
 		parent.rdes.dicData[dataSetName] = dic;
 		parent.closedialog();
 	}
 	</script>
    <style type="text/css">
        body {font-size: 75%;font-weight: normal;margin:0;}
        select{width:200px}
    </style>
</head>
<body>
<table width="100%" cellspacing="5">
    <tr>
        <td>字典类型</td>
        <td>字典信息ID</td>
        <td>字典信息内容</td>
    </tr>
    <tr>
        <td><select name="dicType" id="dicType">
            </select></td>
        <td><select name="dicID" id="dicID">
        </select></td>
        <td><select name="dicText" id="dicText">
        </select></td>
    </tr>
</table>
<div style="padding-top:25px;padding-left:260px;"><input type="button" name="sub" id="sub" value="确认" onclick="submit();" />
</div>
</body>
</html>