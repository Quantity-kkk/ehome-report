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
    <script type="text/javascript" src="../lib/layer/layer.js"></script>
     <script src="../js/dataSets.js"></script>
    <style type="text/css">
        body {font-size:12px;font-weight: normal;margin:0;}
        #top p{display:inline-block;float:left;margin-left:10px;}
        #top #dslist {width: 200px;}
        .clearfix{clear: both;}
        .left{width:20%;height:100%;float:left;overflow-x:auto;}
        .ztree li span.button.home_ico_open,.ztree li span.button.home_ico_close{margin-right:2px; background:url(../lib/ztree/css/zTreeStyle/img/diy/1_open.png) no-repeat scroll 0 0 transparent; vertical-align:top;*vertical-align:middle}
        .right{width:78%;float:right;}
        .ui-button {width:100px;height:20px;line-height:20px;background:transparent;color:#ffffff;}
        .del{background:#de5567;}
        .edit{background:#ed9b61;}
        .add{background:#3d9f85;}
        .dsform{margin:0;border:0;padding:0;line-height:1.8;font-size:12px;}
        .dsform p{marign:0;padding:0;border:0;}
        .dsform legend{font-weight: bold;font-size:14px;}
        .dsform label.p{display:inline-block;float:left;width:70px;padding:0 10px;}
        .dsform label.error{display:inline-block;color:red;padding-left:10px;}
        .dsform .txt{width:200px;border: 1px solid #ccc;height: 26px;}
        .dsform .txtp{width:100px;border: 1px solid #ccc;height: 26px;}
        .dsform .sel{width:204px;border: 1px solid #ccc;height: 31px;}
        .dsform .selp{width:104px;margin:0 5px;border: 1px solid #ccc;height: 31px;}
        .dsform textarea{width:75%;height:80px;border: 1px solid #ccc;}
        .dsform .submit{margin-left:100px;width:auto;padding:5px 15px;background: none repeat scroll 0 0 #277b9d;border: 1px solid #ccc;color:#fff;height: 30px;}
        .dsform .padd{width:50px; height:25px;border:0;cursor:pointer;background:transparent url(../images/plusmin.png) top center no-repeat;}
        .dsform .pdel{width:50px; height:25px;border:0;cursor:pointer;background:transparent url(../images/plusmin.png) 12px -25px no-repeat;}
    </style>
</head>
<body><div id="top">
        <p><select name="dslist" id="dslist" style="border: 1px solid #ccc;height: 31px;line-height:28px;">
        	<option value=''>请选择数据集</option>
        </select></p>
        <p><input class="del ui-button ui-widget ui-state-default ui-button-text-icon-minus" id="del" onclick="dataSetsOption(this.id)" value="删除所选数据集"></p>
        <p><input class="edit ui-button ui-widget ui-state-default ui-button-text-icon-pencil"" id="edit" onclick="dataSetsOption(this.id)" value="修改所选数据集"></p>
        <p><input class="add ui-button ui-widget ui-state-default ui-button-text-icon-primary" id="add" onclick="dataSetsOption(this.id)" value="添加新数据集"></p>
</div>
<div class="clearfix"></div>
<div>
    <div class="left"><ul id="treeds" class="ztree"></ul></div>
    <div class="right" style="display:none;">
        <form method="get" class="dsform" id="dsform" action="">
            <fieldset>
                <legend>添加数据集</legend>
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
                    <p><label class="p">参数列表</label><input type="button" class="padd" onclick="addparm('','')" /></p>
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
                <p>
                    <input id="submit" class="submit" type="button" value="保存" onclick="dataSetsOption(this.id)"/>
                </p>
                <input id="reset" type="reset" style="display:none"/>
                
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>