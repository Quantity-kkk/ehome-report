﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8" />
    <title>数据源管理</title>
    <link href="../common/css/css.css" rel="stylesheet" type="text/css" />
    <link href="../lib/ui/jquery-ui.min.css" rel="stylesheet" />
    <script type="text/javascript" src="../lib/jquery.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../lib/layer/layer.js"></script>
    <script type="text/javascript">
    var map = "{";
        var dialog;
        function showdialog(option,title, url) {
            dialog=layer.open({
			  title: title,
			  type: 2, 
			  maxmin: true, //开启最大化最小化按钮
			  area: ['600px', '400px'],
			  content: url
			});
        }
        function closedialog() {
            try {
                //dialog.dialog("close");
                layer.close(dialog);
            } catch (ee) {
            }
        }
        function deleteDataSource(name){
        layer.confirm('确认删除？', function(index){
        	$.ajax({ 
        		url : "../../deleteDataSource.action", 
        		data:{
        			dataSourceName:name
        		},
        		type:"post",
        		success:function(data){
        			location.reload();
        		}
        	});
        	layer.close(index);
        	});
        }
        $(function(){
        	//$('.tbody tr').hover(function(){$(this).children('td').css('background','#f1f6fc')},function(){$(this).children('td').css('background','#fff')});
        })
    </script>
</head>
<body>
<div class="main">
<table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
  <tbody>
    <tr>
      <td valign="top">
 
		</td>
      <td valign="top"><div>
<div class="content">
<div class="manage"><div class="managetitle">数据源管理</div>
  <div class="botton" onclick="showdialog('add','添加数据源','Data_Source.jsp?dsname=');">新增数据源</div>
</div>
  <table class="rdp-table" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr class="biaotou">
      <td height="35">数据源名称</td>
      <td width="200">数据源类型</td>
      <td width="100">操作</td>
    </tr>
	<tbody class="tbody">
 </tbody>
  </table>
</div></div>
 </td>
    </tr>
  </tbody>
</table>
</div>
<script>
$(function(){
	$.ajax({ 
		url : "../../selectDataSourceAll.action", 
		type:"post",
		success:function(data){
			if(data.flag=='error'){
				top.location.href='../../login.action';
			}else{
				var str=''
				$(data).each(function(index,item){
						str+='<tr>';
						str+='<td>'+item.dataSourceName+'</td>';
						str+='<td>'+item.type+'</td>';
						str+='<td><a href="javascript:void(0)" onclick="showdialog(\'update\',\'修改数据源\',\'Data_Source.jsp?dsname='+item.dataSourceName+'\');" title="修改" >编辑</a>';
						str+='		&nbsp;';
						str+='<a href="javascript:void(0)" onclick="deleteDataSource(\''+item.dataSourceName+'\');" title="删除">删除</a>';
						str+='</td>';
						str+='</tr>';
					});
				$('.tbody').html(str);
			}
		},befenSend:function(){layer.load()},complete:function(){layer.closeAll('loading')}
	});
});
</script>
</body>
</html>


