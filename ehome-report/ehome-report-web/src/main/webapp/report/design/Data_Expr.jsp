﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <title>表达式配置</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../lib/ui/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../lib/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="../lib/jquery.min.js"></script>
    <script src="../lib/ztree/jquery.ztree.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script src="../lib/vl/jquery.validate.min.js"></script>
    <script src="../lib/vl/localization/messages_zh.js"></script>
    <style type="text/css">
        body {font-size: 75%;font-weight: normal;margin:0;}
        .czf input{width:40px;height:25px;text-align:center;margin:5px;font-size:11px;}
        .ztree{height:240px;overflow-x:hidden;overflow-y:scroll;}
        .ztree li span.button.home_ico_open,.ztree li span.button.home_ico_close{margin-right:2px; background:url(../lib/ztree/css/zTreeStyle/img/diy/1_open.png) no-repeat scroll 0 0 transparent; vertical-align:top;*vertical-align:middle}
        .ztree li span.button.pIcon01_ico_close{margin-right:2px; background:url(../lib/ztree/css/zTreeStyle/img/diy/1_close.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
        .submit{width:auto;padding:5px 15px;background: none repeat scroll 0 0 #277b9d;border: 1px solid #ccc;color:#fff;height: 30px;}
        .reset{width:auto;padding:5px 15px;background: none repeat scroll 0 0 gray;border: 1px solid #ccc;color:#fff;height: 30px;}
    </style>
    <script>
    	var dsname='';
        var setting = {
            data: {
                simpleData: {
                    enable: true
                },
                key:{
                title:"title"
                }
            },
            callback: {
            	onDblClick: onDblClick
            }
        };

        var zNodes =[
            { id:1, pId:0, name:"全部", open:true},
            { id:10, pId:1, name:"数据集函数"},
            { id:1001, pId:10, name:"select()",title:"参数：1.select(String select_exp)\n2、select(String select_exp,boolean filter_exp)\n说明：功能待定\n返回值：1、原字符串\n      2、filter_ex为true返回原字符串否则返回空字符串"},
            { id:1002, pId:10, name:"group()",title:"参数：group(String str)\n说明：分组函数，同类别同行合并\n返回值：原字符串 "},
            { id:1003, pId:10, name:"gpc()",title:"参数：gpc(String str)\n说明：动态列，用作动态扩展列\n返回值：查询动态字符串 "},
            { id:20, pId:1, name:"单元格函数"},
            { id:2001, pId:20, name:"sum()",title:"参数：sum(String... args)\n说明：转Double后求和\n返回值：和字符串"},
            { id:2002, pId:20, name:"avg()",title:"参数：avg(String... args)\n说明：转Double后求平均数\n返回值：平均值字符串"},
            { id:2003, pId:20, name:"max()",title:"参数：max(String... args)\n说明：转Double后求最大值\n返回值：最大值字符串"},
            { id:2004, pId:20, name:"min()",title:"参数：min(String... args)\n说明：转Double后求最小值\n返回值：最小值字符串"},
            { id:2005, pId:20, name:"count()",title:"参数：1、count()\n2、count(boolean filterExp)\n3、count(String nullCheckExp)\n说明：功能待定\n返回值：1、返回整数1\n2、filterExp为true返回整数1否则返回整数0\n3、nullCheckExp为空字符串返或null返回整数1否则返回整数0"},
            { id:2006, pId:20, name:"nvl()",title:"参数：nvl(String value1,String value2)\n说明：处理空值\n返回值：当value1为null或空字符串时返回value2，否则返回value1"},
            { id:30, pId:1, name:"时间日期函数"},
            { id:3001, pId:30, name:"day()",title:"参数：day(String dateStr)，格式：yyyy-MM-dd HH:mm:ss\n说明：获取日期\n返回值：日期字符串"},
            { id:3002, pId:30, name:"month()",title:"参数：month(String dateStr)，格式：yyyy-MM-dd HH:mm:ss\n说明：获取月份\n返回值：月份字符串"},
            { id:3003, pId:30, name:"year()",title:"参数：year(String dateStr)，格式：yyyy-MM-dd HH:mm:ss\n说明：获取年份\n返回值：年份字符串"},
            { id:3004, pId:30, name:"now()",title:"参数：now()\n说明：返回当前时间\n返回值：当前时间字符串（格式：yyyy-MM-dd HH:mm:ss）"},
            { id:3005, pId:30, name:"time()",title:"参数：time()\n说明：返回当前时间\n返回值：当前时间字符串（格式：HH:mm:ss）"},
            { id:40, pId:1, name:"字符串函数"},
            { id:4001, pId:40, name:"chn()",title:"参数：1、chn(String str)\n2、chn(String str,boolean flag1,boolean flag2)\n3、chn(String str,boolean flag1)\n说明：数字转中文大写\n返回值：1、纯中文大写（例：2800->二八零零）\n	    2、flag2为true返回大写（例：2800->贰仟捌佰），其它同1\n	    3、flag1为true返回大写（例：2800->二千八百）,其它同1\n"},
            { id:4002, pId:40, name:"left()",title:"参数：left(String str,int n)\n说明：截取左边n个字符\n返回值：截取后的字符串（长度不够直接返回原字符串）"},
            { id:4003, pId:40, name:"right()",title:"参数：right(String str,int n)\n说明：截取右边n个字符\n返回值：截取后的字符串（长度不够直接返回原字符串）"},
            { id:4004, pId:40, name:"mid()",title:"参数：1、mid(String str,int start)\n2、mid(String str,int start,int end)\n说明：截取字符串\n返回值：1、返回从start位置开始的字符串\n		2、返回从start位置开始，end位置结束的字符串"},
            { id:4005, pId:40, name:"ltrim()",title:"参数：ltrim(String str)\n说明：去除左空格\n返回值：去除左空格的字符串"},
            { id:4006, pId:40, name:"rtrim()",title:"参数：rtrim(String str)\n说明：去除右空格\n返回值：去除右空格的字符串"},
            { id:4007, pId:40, name:"trim()",title:"参数：trim(String str)\n说明：去除左右空格\n返回值：去除左右空格的字符串"},
            { id:4008, pId:40, name:"len()",title:"参数：len(String str)\n说明：取长度\n返回值：字符串的长度"},
            { id:4009, pId:40, name:"lower()",title:"参数：lower(String str)\n说明：转小写\n返回值：转小写后的字符串"},
            { id:4010, pId:40, name:"upper()",title:"参数：upper(String str)\n说明：转大写\n返回值：转大写后的字符串"},
            { id:4011, pId:40, name:"pos()",title:"参数：1、pos(String str1,String str2)\n2、pos(String str1,String str2,int index)\n说明：查找位置\n返回值：1、返回str2在str1中的第一次出现位置\n		2、返回str2在tr1中的第index次出现位置"},
            { id:4012, pId:40, name:"rmb()",title:"参数：rmb(Number num)、rmb(String num)\n说明：转人民币大写\n返回值：人民币大写字符串"},
            { id:4013, pId:40, name:"rplc()",title:"参数：rplc(String str,String strz,String strt)\n说明：全部替换\n返回值：将str中的strz全部替换成strt后的字符串"},
            { id:4014, pId:40, name:"space()",title:"参数：space(int n)\n说明：取空格\n返回值：有n个空格的字符串"},
            { id:4015, pId:40, name:"split()",title:"参数：split(String str,String flag)\n说明：以flag分隔字符串\n返回值：分隔后的数组"},
            { id:4016, pId:40, name:"wordCap()",title:"参数：wordCap(String str)\n说明：去除空白字符并转为大写\n返回值：大写字符串"},
            { id:4017, pId:40, name:"rmQuote()",title:"参数：rmQuote(String str)\n说明：替换字符串中的单、双引号\n返回值：替换后的字符串"},
            { id:50, pId:1, name:"数据类型转换"},
            { id:5001, pId:50, name:"date()",title:"参数：date(String dataStr)，格式：yyyy-MM-dd\n说明：日期字符串格式化为长日期\n返回值：格式化后的日期"},
            { id:5002, pId:50, name:"dateTime()",title:"参数：dateTime(String dataStr)、dateTime(Long dataStr)\n说明：日期格式化\n返回值：格式化后的日期（yyyy-MM-dd HH:mm:ss）"},
            { id:5003, pId:50, name:"dateTime2()",title:"参数：dateTime2(String dataStr, String format)\n说明：按格式格式化日期\n返回值：返回format格式化后的日期"},
            { id:5004, pId:50, name:"toDouble()",title:"参数：toDouble(String str)\n说明：转换成double类型\n返回值：double值"},
            { id:5005, pId:50, name:"toFloat()",title:"参数：toFloat(String str)\n说明：转换成float类型\n返回值：float值"},
            { id:5006, pId:50, name:"toInt()",title:"参数：toInt(String str)\n说明：转换成int类型\n返回值：int值"},
            { id:5007, pId:50, name:"toLong()",title:"参数：toLong(String str)\n说明：转换成long类型\n返回值：long值"},
            { id:5008, pId:50, name:"toNumber()",title:"参数：toNumber(String str)\n说明：转换成number类型\n返回值：number值"},
            { id:5009, pId:50, name:"toStr()",title:"参数：String toStr(Object obj)\n说明：对象转换成字符串\n返回值：转换后的字符串"},
            { id:5010, pId:50, name:"toDef()",title:"参数：toDef(Object str,String pattern)，pattern格式：DecimalFormat支持的格式（例：#,##0.00）\n说明：自定义数值格式化（str自动转成double类型）\n返回值：格式化后的字符串"},
            { id:5011, pId:50, name:"toDtf()",title:"参数：toDtf(Object str,String frompattern,String topattern)，pattern格式：SimpleDateFormat支持的格式\n说明：自定义日期格式化\n返回值：格式化后的日期"},
            { id:5012, pId:50, name:"toDwf()",title:"参数：toDwf(Object str,String pattern)，pattern格式：DecimalFormat支持的格式（例：#,##0.00）\n说明：自定义数值格式化（str自动转成double类型并除以10000）\n返回值：格式化后的字符串"}
        ];
        var nm='<%=request.getParameter("n")%>';
        $(function(){
            var tree = $.fn.zTree.init($("#treed"), setting, zNodes);
            $('#expr').val(parent.rprop.document.getElementById('expr').value.replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&amp;/g, "&"));
            $('#czz input').click(function(){filld(' '+$(this).val()+' ')});
            init();
        });
        function onDblClick(event, treeId, treeNode, clickFlag) {
            if(treeNode.pId>1){
                filld(treeNode.name);
            }
        }

        function filld(v){
        	if(dsname!='') v=v.replace('$F{','$F{'+dsname+'.');
            insertAtCursor(document.getElementById('expr'),v);
            var fc=$('#expr').val().indexOf('()');
            if(fc>0){setCursorPosition(document.getElementById('expr'),fc+1)};
        }
        function init(){
            var ds=$(parent.rdes.getDS());
            $('#a').empty();
            ds.find('dataset').each(function(){
                $('#a').append('<option value="'+$(this).attr("name")+'">'+$(this).attr("name")+'</option>');
            });
            $('#a').append('<option value="parm">参数</option>');
        }
        function sl(v){
            $('#b').empty();
            if(v=='parm'){
                var dp=$(parent.rdes.document.getElementById('parmsModel').value);
                dp.find('parm').each(function(){
                    $('#b').append('<option value="'+$(this).attr("name")+'">'+$(this).attr("name")+'</option>')
                });
            }else {
            	dsname=v;
                var ds = $(parent.rdes.getDS());
                var dd=ds.find('dataset[name=' + v + ']');
                dd.find('dataset').detach();//ie bug
                dd.find('Fields>Field').each(function () {
                    $('#b').append('<option value="'+$(this).attr("name")+'">'+$(this).attr("name")+'</option>')
                });
            }
        }
        function setCursorPosition(elem, index) {
            var val = elem.value
            var len = val.length

            // 超过文本长度直接返回
            if (len < index) return
            setTimeout(function() {
                elem.focus();
                if (elem.setSelectionRange) { // 标准浏览器
                    elem.setSelectionRange(index, index)
                } else { // IE9-
                    var range = elem.createTextRange()
                    range.moveStart("character", -len)
                    range.moveEnd("character", -len)
                    range.moveStart("character", index)
                    range.moveEnd("character", 0)
                    range.select()
                }
            }, 10)
        }
        function insertAtCursor(myField, myValue)
        {
            //IE support
            if (document.selection)
            {
                myField.focus();
                sel = document.selection.createRange();
                sel.text = myValue;
                sel.select();
            }
            //MOZILLA/NETSCAPE support
            else if (myField.selectionStart || myField.selectionStart == '0')
            {
                var startPos = myField.selectionStart;
                var endPos = myField.selectionEnd;
                // save scrollTop before insert
                var restoreTop = myField.scrollTop;
                myField.value = myField.value.substring(0, startPos) + myValue + myField.value.substring(endPos,myField.value.length);
                if (restoreTop > 0)
                {
                    // restore previous scrollTop
                    myField.scrollTop = restoreTop;
                }
                myField.focus();
                myField.selectionStart = startPos + myValue.length;
                myField.selectionEnd = startPos + myValue.length;
            } else {
                myField.value += myValue;
                myField.focus();
            }
        }
        function submit(){
        	try{
        	//alert($('#expr').val())
        	var vv=$('#expr').val().replace(/\&/g, "&amp;").replace(/\>/g, "&gt;").replace(/\</g, "&lt;").trim();
        	$('#expr',parent.document).val($('#expr').val());
        	parent.document.getElementById(nm).value=vv;
        	parent.rprop.document.getElementById(nm).value=vv;
        	parent.rdes.Handsontable.dataFormat.setExpText(vv);
        	//alert(vv);
        	}catch(e){}
        	parent.closedialog();
        }
    </script>
</head>
<body>
<table width="100%" cellpadding="5">
    <tr>
        <td width="50%" valign="top"><fieldset><legend>表达式</legend><div><textarea name="expr" id="expr" cols="35" rows="6" style="width:318px;height:95px;"></textarea></div></fieldset></td>
        <td valign="top" class="czf"><fieldset><legend>操作符</legend>
            <div id="czz"><input name="btn_jia" type="button" id="btn_jia" value="+" />
                <input name="btn_jian" type="button" id="btn_jian" value="-" />
                <input name="btn_cheng" type="button" id="btn_cheng" value="*" />
                <input name="btn_chu" type="button" id="btn_chu" value="/" />
                <input name="btn_dayu" type="button" id="btn_dayu" value="&gt;" />
                <input name="btn_xiaoyu" type="button" id="btn_xiaoyu" value="&lt;" />
                <input name="btn_kuoz" type="button" id="btn_kuoz" value="(" />
                <input name="btn_kuoy" type="button" id="btn_kuoy" value=")" />
                <input name="btn_and" type="button" id="btn_and" value="AND" />
                <input name="btn_or" type="button" id="btn_or" value="OR" />
                <input name="btn_not" type="button" id="btn_not" value="NOT" />
                <input name="btn_deng" type="button" id="btn_deng" value="=" />
            </div>
        </fieldset></td>
        <td width="60" rowspan="2" valign="top"><input type="button" name="sub" id="sub" class="submit" value="确认" onclick="submit();" />
            <!-- <br /><br />
            <input type="button" name="reset" id="reset" value="取消" class="reset" onclick="parent.closedialog();" /> --></td>
    </tr>
    <tr>
        <td valign="top"><fieldset><legend>名称/字段</legend>
            <div>
            <%-- <select name="a" size="15" multiple="multiple" id="a" onchange="sl(this.value)" style="width:80px;height:250px;" ondblclick="if(this.value!='parm'){filld(this.value+'.')}"></select>
             --%>
             <select name="a" size="15" multiple="multiple" id="a" onchange="sl(this.value)" style="width:80px;height:250px;" ></select>
             <select name="b" size="15" multiple="multiple" id="b" ondblclick="filld('$F{'+this.value+'}')" style="width:190px;height:250px;">
            </select></div></fieldset></td>
        <td valign="top"><fieldset><legend>可选函数</legend><div><ul id="treed" class="ztree"></ul></div></fieldset></td>
    </tr>
</table>
</body>
</html>