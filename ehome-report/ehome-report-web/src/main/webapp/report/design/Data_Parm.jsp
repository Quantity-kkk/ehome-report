﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <title>参数配置</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../lib/ui/jquery-ui.min.css"/>
    <script type="text/javascript" src="../lib/jquery.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script src="../lib/vl/jquery.validate.min.js"></script>
    <script src="../lib/vl/localization/messages_zh.js"></script>
    <script type="text/javascript" src="../lib/layer/layer.js"></script>
    <script>
        var sDataSetsName=null;
        $(document).ready(function() {
            //$("button.add").button({icons: {primary: "ui-icon-plus"}});
            //$("button.edit").button({icons: {primary: "ui-icon-pencil"}});
            //$("button.del").button({icons: {primary: "ui-icon-minus"}});
            $('#dslist').on('change',function(){
                    sDataSetsName = this.value;
                });
            initTree();
        });

        function initTree(){
            var parms=$(parent.rdes.document.getElementById('parmsModel').value);
            $("#dslist").empty();
            $('#dslist').append("<option value=''>请选择参数</option>");
            parms.find('parm').each(function(){
                $('#dslist').append("<option value='"+$(this).attr('name')+"'>"+$(this).attr('name')+"</option>");
            });
            //$('#dslist').selectmenu('refresh', true);
        }
        function dsop(id){
            var vvv=parent.rdes.document.getElementById('parmsModel').value;
            if(vvv==''||vvv==undefined) vvv='<parmsList></parmsList>';
            var parms=$(vvv);
            switch(id){
                case "add":
                    $('#reset').click();
                    $('.right').show();
                    break;
                case "edit":
                    $('#reset').click();
                    parms.find('parm[name="'+$("#dslist").val()+'"]').each(function(){
                        $("#parmName").val($(this).attr('name'));
                        $("#parmCName").val($(this).attr('cname'));
                        $("#parmName").attr('readonly','true');
                        $("#dbType").val($(this).attr('dbtype'));
                        $("#parmType").val($(this).attr('type'));
                        $("#showType").val($(this).attr('showtype')==undefined?"input":$(this).attr('showtype'));
                        $("#parmvl").val($(this).attr('vl'));
                        if($(this).attr('isnull')=="1"){
                            $('#isnull').prop('checked',true);
                        }else{
                            $('#isnull').prop('checked',false);
                        }
                    });
                    $('.right').show();
                    break;
                case "del":
                	parms.find('parm[name="'+$("#dslist").val()+'"]').detach();
                	parent.rdes.document.getElementById('parmsModel').value='<parmsList>'+parms.html()+'</parmsList>';
                        layer.alert('操作成功！',function(){location.href=location.href;});
                    break;
                case "submit":
                        if(parms.find('parm[name="'+$("#parmName").val()+'"]').length==0) {
                            parms.append('<parm name="'+$("#parmName").val()+'" cname="'+$("#parmCName").val()+'" dbtype="'+$("#dbType").val()+'" isnull="'+($("#isnull").is(':checked')==true?"1":"0")+'" type="'+$("#parmType").val()+'" vl="'+$("#parmvl").val()+'"></parm>');
                        }else{
                            parms.find('parm[name="'+$("#parmName").val()+'"]').each(function(){
                                $(this).attr('dbtype',$("#dbType").val());
                                $(this).attr('cname',$("#parmCName").val());
                                $(this).attr('type',$("#parmType").val());                                
                                $(this).attr('showtype',$("#showType").val());
                                $(this).attr('isnull',($("#isnull").is(':checked')==true?"1":"0"));
                                $(this).attr('vl',$("#parmvl").val());
                            });
                        }
                    parent.rdes.document.getElementById('parmsModel').value='<parmsList>'+parms.html()+'</parmsList>';
                        layer.alert('操作成功！',function(){location.href=location.href;});
                    break;
            }
        }
    </script>
    <style type="text/css">
        body {font-size:12px;font-weight: normal;margin:0;}
        #top p{display:inline-block;float:left;margin-left:10px;}
        #top #dslist {width: 200px;}
        .clearfix{clear: both;}
        .left{width:30%;float:left;}
        .ztree li span.button.home_ico_open,.ztree li span.button.home_ico_close{margin-right:2px; background:url(../lib/ztree/css/zTreeStyle/img/diy/1_open.png) no-repeat scroll 0 0 transparent; vertical-align:top;*vertical-align:middle}
        .right{width:95%;margin:0 auto;}
        .ui-button {width:80px;height:20px;line-height:20px;background:transparent;color:#ffffff;}
        .del{background:#de5567;}
        .edit{background:#ed9b61;}
        .add{background:#3d9f85;}
        .dsform{margin:0;border:0;padding:0;line-height:1.8;font-size:13px;}
        .dsform p{marign:0;padding:0;border:0;}
        .dsform legend{font-weight: bold;font-size:14px;}
        .dsform label.p{display:inline-block;float:left;width:70px;padding:0 10px;}
        .dsform label.error{display:inline-block;color:red;padding-left:10px;}
        .dsform .txt{width:200px;border: 1px solid #ccc;height: 26px;}
        .dsform .txtp{width:100px;border: 1px solid #ccc;height: 26px;}
        .dsform .sel{width:204px;border: 1px solid #ccc;height: 31px;}
        .dsform .selp{width:104px;margin:0 5px;border: 1px solid #ccc;height: 31px;}
        .dsform textarea{width:400px;height:60px;border: 1px solid #ccc;}
        .dsform .submit{margin-left:100px;width:auto;padding:5px 15px;background: none repeat scroll 0 0 #277b9d;border: 1px solid #ccc;color:#fff;height: 30px;}
        .dsform .padd{width:50px; height:25px;border:0;cursor:pointer;background:transparent url(../images/plusmin.png) top center no-repeat;}
        .dsform .pdel{width:50px; height:25px;border:0;cursor:pointer;background:transparent url(../images/plusmin.png) 12px -25px no-repeat;}
    </style>
</head>
<body><div id="top">
    <p><select name="dslist" id="dslist" style="border: 1px solid #ccc;height: 31px;">
        <option value=''>请选择参数</option>
    </select></p>
    <p><input class="del ui-button ui-widget ui-state-default ui-button-text-icon-minus" id="del" onclick="dsop(this.id)" value="删除所选参数"></p>
    <p><input class="edit ui-button ui-widget ui-state-default ui-button-text-icon-pencil" id="edit" onclick="dsop(this.id)" value="修改所选参数"></p>
    <p><input class="add ui-button ui-widget ui-state-default ui-button-text-icon-primary" id="add" onclick="dsop(this.id)" value="添加参数"></p>
</div>
<div class="clearfix"></div>
<div>
    <div class="right" style="display:none;">
        <form method="get" class="dsform" id="dsform" action="">
            <fieldset>
                <legend>添加参数</legend>
                <p>
                <label class="p">参数名称</label>
                <input name="parmName" id="parmName" title="请输入参数名称" class="txt" required />
            </p>
	            <p>
	                <label class="p">中文名称</label>
	                <input name="parmCName" id="parmCName" title="请输入中文名称" class="txt" required />
	            </p>
                <p style="display:none">
                    <label class="p">可否为空</label>
                    <input type="checkbox" name="isnull" id="isnull" value="1" />
                </p>
                <p>
                    <label class="p">展现类型</label>
                    <select name="showType" id="showType" title="请选择表单类型" class="sel" required>
                        <option value="input">输入框</option>
                        <option value="select">选择框</option>
                    </select>
                </p>
                <p>
                    <label class="p">参数类型</label>
                    <select name="parmType" id="parmType" title="请选择参数类型" class="sel" required>
                        <option value="normal">普通参数</option>
                        <option value="dynamic">动态参数</option>
                        <!-- <option value="session">会话变量</option> -->
                    </select>
                </p>
                <p>
                    <label class="p">数据类型</label>
                    <select name="dbType" id="dbType" style="border: 1px solid #ccc;height: 31px;" title="请选择数据类型" required>
                        <option value="string">字符串</option>
                        <option value="date">日期</option>
                        <option value="double">货币</option>
                        <option value="int">整数</option>
                    </select>
                </p>
                <p>
                    <label class="p">值/表达式</label>
                    <textarea cols="38" rows="5" name="parmvl" id="parmvl" title="请输入值/表达式"></textarea>
                </p>
                <p>
                    <input id="submit" class="submit" type="button" value="保存" onclick="dsop(this.id)"/>
                </p>
                <input id="reset" type="reset" style="display:none"/>

            </fieldset>
        </form>
    </div>
</div>
</body>
</html>