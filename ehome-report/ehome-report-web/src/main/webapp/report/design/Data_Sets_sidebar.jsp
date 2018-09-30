﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <title>数据集配置</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../lib/ui/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../lib/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="../design/css/Data_Sets_sidebar.css" type="text/css">
    <script type="text/javascript" src="../lib/jquery.min.js"></script>
    <script src="../lib/ztree/jquery.ztree.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script src="../lib/vl/jquery.validate.min.js"></script>
    <script src="../lib/vl/localization/messages_zh.js"></script>
    <script type="text/javascript" src="../lib/layer/layer.js"></script>
    <script src="../js/dataSets_sidebar.js"></script>
</head>
<body>
    <div class="left"><ul id="treeds" class="ztree"></ul></div>
     <!-- <div id="top">
        <p><select name="dslist" id="dslist" style="border: 1px solid #ccc;height: 31px;line-height:28px;">
        	<option value=''>请选择数据集</option>
        </select></p>
       <p><input class="del ui-button ui-widget ui-state-default ui-button-text-icon-minus" id="del" onclick="dataSetsOption(this.id)" value="删除所选数据集"></p>
        <p><input class="edit ui-button ui-widget ui-state-default ui-button-text-icon-pencil"" id="edit" onclick="dataSetsOption(this.id)" value="修改所选数据集"></p>
	</div>
	 -->
        <input type="button" class="add ui-button ui-widget ui-state-default ui-button-text-icon-primary" id="add" onclick="openDataSet('add')" value="添加新数据集">
   <%--  <div class="right" style="display:none;">
        <form method="get" class="dsform" id="dsform" action="">
            <p>
                <label class="p">数据源名称</label>
                <select name="DataSourceName" id="DataSourceName" title="请选择数据源" class="sel" onchange="sw(this.value)" required>
                </select>
            </p>
            <p>
                <label class="p">数据集名称</label>
                <input name="DataSetName" id="DataSetName" title="请输入数据集名称" class="txt" required />
            </p>
            <div id="ddslist">
            <p>
                    <label class="p">数据字典</label>
                    <input type="checkbox" name="isdd" id="isdd" value="1" />
                </p>
                <p>
                    <label class="p">执行SQL</label>
                    <textarea cols="38" rows="5" name="CommandText" id="CommandText" title="请输入查询SQL" required></textarea>
                </p><p>
                    <label class="p">检验SQL</label>
                    <input type="checkbox" name="isvalid" id="isvalid" value="1" checked="checked" /> 用于修改，取消选中修改不再校验SQL准确性
                </p>
                <div id="parmlist">
                    <p><label class="p">参数列表</label><input type="button" class="padd" onclick="addparm(\'\',\'\')" /></p>
                </div>
                </div>
                <div id="jblist" style="display:none"> 
	                <p>
	                <label class="p">实体类</label>
	                <input name="FactoryClass" id="FactoryClass" title="请输入类工厂" class="txt" required />
	            	</p>
	            	<p style="display:none">
	                <label class="p">方法名</label>
	                <input name="StaticMethod" id="StaticMethod" title="请输入方法名" value="" class="txt" />
	            	</p>
                </div>
                
        </form>
    </div> --%>
</body>
</html>