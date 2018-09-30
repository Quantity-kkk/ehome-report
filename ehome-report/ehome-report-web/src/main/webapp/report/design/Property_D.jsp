<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <title></title>
    <link rel="stylesheet" type="text/css" href="../lib/spectrum/spectrum.css">
    <link href="../css/shuxing.css" rel="stylesheet" type="text/css">
    <link href="../lib/ui/jquery-ui.min.css" rel="stylesheet">
    <script src="../lib/jquery.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../lib/spectrum/spectrum.js"></script>
    <script src="../js/shuxing.js"></script>
    <script>
        $(function () {
            $(".trtitle a").click(function () {
                $(this).toggleClass("ajiahao");
                $(this).parent().parent().parent().next().toggleClass("miss");
            });

            $("#bgcolor").spectrum({
                hide: function (tinycolor) {
                    setv('bgcolor', tinycolor.toHexString());
                },
                color: "#ffffff"
            });
            $("#fontColor").spectrum({
                hide: function (tinycolor) {
                    setv('fontColor', tinycolor.toHexString());
                },
                color: "#000000"
            });
        })

    </script>
</head>
<body>
<form id="sxform">
    <table id="propertyTable" border="0" cellspacing="0" cellpadding="1" width="100%" class="tableshuxing">
        <thead>
        <tr class="trtitle row" id="duanduo">
            <td class="shuxingleftcolor row"><a href="#"></a></td>
            <td colspan="2" class="tdtitle" width="94%">行属性</td>
        </tr>
        </thead>
        <tbody>
        <tr class="trbody row">
            <td width="6%" class="shuxingleftcolor row"><a href="#"></a></td>
            <td width="47%">行类型</td>
            <td width="47%">
                <select name="category" id="category" onchange="setv(this.name,this.value)">
                    <option value=""></option>
                    <option value="headtitle">头标题</option>
                    <option value="reporthead">报表头</option>
                    <option value="dataarea" selected="selected">数据区</option>
                    <option value="reportfoot">报表尾</option>
                    <option value="foottitle">尾标题</option>
                </select>
           </td>
        </tr>
        <tr class="trbody row">
            <td width="6%" class="shuxingleftcolor row"><a href="#"></a></td>
            <td width="47%">是否循环</td>
            <td width="47%">
                <input name="isselect" id="isselect" type="checkbox"
                       onclick="$(this).is(':checked')?setv(this.name,1):setv(this.name,0)">
           </td>
        </tr>
        </tbody>
        <thead>
        <tr class="trtitle row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td colspan="2" class="tdtitle" width="94%">段落</td>
        </tr>
        </thead>
        <tbody>
        <tr class="trbody duanluo row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>水平对齐</td>
            <td>
                <select name="align" id="align" onchange="setv(this.name,this.value)">
                    <option value=""></option>
                    <option value="left">左对齐</option>
                    <option value="right">右对齐</option>
                    <option value="center">居中对齐</option>
                </select>
           </td>
        </tr>
         <tr class="trbody duanluo row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>纵向对齐</td>
            <td>
                <select name="valign" id="valign" onchange="setv(this.name,this.value)">
                    <option value=""></option>
                    <option value="top">顶端对齐</option>
                    <option value="middle">垂直居中</option>
                    <option value="bottom">底端对齐</option>
                </select>
           </td>
        </tr>
        <tr class="trbody duanluo row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>自动换行</td>
            <td><input name="wordWrap" id="wordWrap" type="checkbox"
                       onclick="$(this).is(':checked')?setv(this.name,'normal'):setv(this.name,'nowrap')"></td>
        </tr>
        </tbody>
        <thead>
        <tr class="trtitle cell">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td colspan="2" class="tdtitle" width="94%">数据</td>
        </tr>
        </thead>
        <tbody>
        <tr class="trbody cell">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>格式</td>
            <td>
                <input name="format" id="format" style="width:95%" value="" onchange="setv(this.name,this.value)" onclick="parent.showdialog('格式选择','Data_Format.jsp?v='+encodeURIComponent(this.value)+'&n='+this.name);">
            </td>
        </tr>
        <tr class="trbody cell">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>字典</td>
            <td>
                <input name="dic" id="dic" style="width:95%" value="" onchange="setv(this.name,this.value)" onclick="parent.showdialog('字典项设置','Data_Dic.jsp?v='+encodeURIComponent(this.value)+'&n='+this.name);">
            </td>
        </tr>
        <tr class="trbody cell">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>数据集</td>
            <td>
                <select name="ds" id="ds" onchange="changeDataSets(this.value)">
                    <option value=""></option>
                </select>
            </td>
        </tr>
        <tr class="trbody cell">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>文本/字段</td>
            <td>
                <input name="text" id="dsvt" style="width:95%" value="" onchange="setv(this.name,this.value)">
                <select name="text" id="dsvs" style="display:none;" onchange="setdv(this)">
                </select>
            </td>
        </tr>
        <tr class="trbody cell">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>表达式</td>
            <td>
                <input name="expr" id="expr" style="width:95%" value="" onchange="setv(this.name,this.value)" onclick="parent.showdialog('表达式配置','Data_Expr.jsp?v='+encodeURIComponent(this.value)+'&n='+this.name);">
            </td>
        </tr>
        </tbody>
        <thead>
        <tr class="trtitle row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td colspan="2" class="tdtitle">属性</td>
        </tr>
        </thead>
        <tbody>
        <tr class="trbody row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>高度</td>
            <td><input name="height" id="height" value="" onchange="setv(this.name,this.value)">px</td>
        </tr>
        <tr class="trbody cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>宽度</td>
            <td><input name="width" id="width" value="" onchange="setv(this.name,this.value)">px</td>
        </tr>
        <tr class="trbody row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>背景颜色</td>
            <td><input name="bgcolor" id="bgcolor" type="text">
            </td>
        </tr>
        </tbody>
        <thead>
        <tr class="trtitle" id="kejian" style="display:none">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td colspan="2" class="tdtitle">可见</td>
        </tr>
        </thead>
        <tobdy>
            <tr class="trbody kejianl" style="display:none">
                <td class="shuxingleftcolor"><a href="#"></a></td>
                <td>是否可见</td>
                <td><input type="checkbox"></td>
            </tr>
        </tobdy>
        <thead>
        <tr class="trtitle row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td colspan="2" class="tdtitle">字体</td>
        </tr>
        </thead>
        <tbody>
        <tr class="trbody row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>文本字体</td>
            <td><select name="fontFamily" id="fontFamily" onchange="setv(this.name,this.value)">
                <option value="PMingLiU">新细明体</option>
                <option value="MingLiU">细明体</option>
                <option value="DFKai-SB">标楷体</option>
                <option value="SimHei">黑体</option>
                <option value="SimSun">宋体</option>
                <option value="NSimSun">新宋体</option>
                <option value="FangSong">仿宋</option>
                <option value="KaiTi">楷体</option>
                <option value="Microsoft JhengHei">微软正黑体</option>
                <option value="Microsoft YaHei">微软雅黑</option>
            </select></td>
        </tr>
        <tr class="trbody row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>文字大小</td>
            <td><input name="fontSize" id="fontSize" value="" onchange="setv(this.name,this.value)">px</td>
        </tr>
        <tr class="trbody row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>字体颜色</td>
            <td><input name="fontColor" id="fontColor" type='text'/></td>
        </tr>
        <tr class="trbody cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>字体加粗</td>
            <td><input name="fontWeight" id="fontWeight" type="checkbox"
                       onclick="$(this).is(':checked')?setv(this.name,'bold'):setv(this.name,'normal')"></td>
        </tr>
        <tr class="trbody row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>字体倾斜</td>
            <td><input name="fontStyle" id="fontStyle" type="checkbox"
                       onclick="$(this).is(':checked')?setv(this.name,'italic'):setv(this.name,'normal')"></td>
        </tr>
        <tr class="trbody row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>字体样式</td>
            <td>
                <select name="textDecoration" id="textDecoration" onchange="setv(this.name,this.value)">
                    <option value=""></option>
                    <option value="underline">下划线</option>
                    <option value="line-through">删除线</option>
                </select>
            </td>
        </tr>
        </tbody>
        <thead>
        <tr class="trtitle row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td colspan="2" class="tdtitle">边框</td>
        </tr>
        </thead>
        <tbody>
        <tr class="trbody row cell col">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>边框</td>
            <td><select name="border" id="border" onchange="setv(this.name,this.value)">
                <option value="" selected="selected"></option>
                <option value="all">所有边框</option>
                <option value="top">上边框</option>
                <option value="bottom">下边框</option>
                <option value="left">左边框</option>
                <option value="right">右边框</option>
                <option value="outSide">外部边框</option>
                <option value="no">无边框</option>
            </select></td>
        </tr>
        </tbody>
    </table>
    <input type="reset" id="reset" style="display:none">
</form>
</body>
</html>