<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <title></title>
    <link href="../css/shuxing.css" rel="stylesheet" type="text/css">
    <link href="../lib/ui/jquery-ui.min.css" rel="stylesheet">
    <script src="../lib/jquery.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script>
        $(function () {
            $(".trtitle a").click(function () {
                $(this).toggleClass("ajiahao");
                $(this).parent().parent().parent().next().toggleClass("miss");
            });
        });
        function seta(v){
            var obj=$(parent.rdes.getTM());
            var uuid=$('#uid').val();
            var dd=obj.find('chart[uuid="'+uuid+'"]');
            var xa=dd.find('xaxis').html();
            var xb=dd.find('yaxis').html();

            if(v=='x'){
                var f=dd.find('xaxis > type').text();
                if(f!='category'){
                    dd.find('xaxis').detach();
                    dd.find('yaxis').detach();
                    dd.append('<xaxis>'+xa+'</xaxis><yaxis>'+xb+'</yaxis>');
                    parent.rdes.updateChart(obj.html());
                }

                $('#xyb').val('y');

            }else{
                var f=dd.find('yaxis > type').text();
                if(f!='category'){
                    dd.find('xaxis').detach();
                    dd.find('yaxis').detach();
                    dd.append('<yaxis>' + xa + '</yaxis><xaxis>' + xb + '</xaxis>');
                    parent.rdes.updateChart(obj.html());
                }

                $('#xyb').val('x');
            }
        }
        function setb(v){
            var obj=$(parent.rdes.getTM());
            var uuid=$('#uid').val();
            var dd=obj.find('chart[uuid="'+uuid+'"]');
            var xa=dd.find('xaxis').html();
            var xb=dd.find('yaxis').html();

            if(v=='x'){
                var f=dd.find('yaxis > type').text();
                if(f!='category'){
                    dd.find('xaxis').detach();
                    dd.find('yaxis').detach();
                    dd.append('<yaxis>' + xa + '</yaxis><xaxis>' + xb + '</xaxis>');
                    parent.rdes.updateChart(obj.html());
                }

                $('#xya').val('y');
            }else{
                var f=dd.find('xaxis > type').text();
                if(f!='category'){
                    dd.find('xaxis').detach();
                    dd.find('yaxis').detach();
                    dd.append('<xaxis>'+xa+'</xaxis><yaxis>'+xb+'</yaxis>');
                    parent.rdes.updateChart(obj.html());
                }

                $('#xya').val('x');
            }
        }
        function initDataSets(did){
            var dd=$(parent.rdes.getDS());
            $("#"+did).empty();
            $("#"+did).append('<option value=""></option>');
            dd.find('dataset').each(function(){
                $("#"+did).append("<option value='"+$(this).attr('name')+"'>"+$(this).attr('name')+"</option>");
            });
        }
        function initFiles(sid,dsname) {
            var df = $(parent.rdes.getDS()).find('dataset[name="' + dsname + '"]');
            $("#" + sid).empty();
            $("#" + sid).append("<option value=''></option>");
            df.find('dataset').detach();//ie bug track
            df.find('fields > field').each(function () {
                $("#" + sid).append("<option value='" + $(this).attr('name') + "'>" + $(this).attr('name') + "</option>");
            });
        }
        function changeDataSetx(inpid,selid,val){
            if(val==''){
                $('#'+inpid).show();
                $('#'+selid).hide();
            }else{
                initFiles(selid, val);
                $('#'+inpid).hide();
                $('#'+selid).show();
            }
        }
        function setv(t,v,type){
        	parent.rdes.updateChartDom(t,v);
            var obj=$(parent.rdes.getTM());
            var uuid=$('#uid').val();
            var sid=$('#sid').val();
            var isuptree=false;
            var dd=obj.find('chart[uuid="'+uuid+'"]');
            if(type=="s"){
                var dds=dd.find('serie[id="' + sid + '"]');
                var category=dd.attr('category');
                switch(t){
                    case 'sname':
                        dds.children('name').text(v);
                        isuptree=true;
                    break;
                    case 'sdt':
                        dds.children('data').text(v);
                        break;
                    case 'sdtf':
                        dds.children('dataf').text(v);
                        break;
                    case 'sds':
                        dds.children('data').text(v);
                        break;
                    case 'sdsf':
                        dds.children('dataf').text(v);
                        break;
                }
            }else{
                switch(t){
                    case 'text':
                        dd.find('titles > text').text(v);
                        break;
                    case 'subtext':
                        dd.find('titles > subtext').text(v);
                        break;
                    case 'legdata':
                        dd.find('legend > data').text(v);
                        break;
                    case 'xdt':
                        if($('#xya').val()=='x'){
                            dd.find('xaxis > data').text(v);
                        }else{
                            dd.find('yaxis > data').text(v);
                        }
                        break;
                    case 'xds':
                        if($('#xya').val()=='x'){
                            dd.find('xaxis > data').text(v);
                        }else{
                            dd.find('yaxis > data').text(v);
                        }
                        break;
                    case 'ydata':
                            if($('#xyb').val()=='y'){
                                dd.find('yaxis > formatter').text(v);
                            }else{
                                dd.find('xaxis > formatter').text(v);
                            }
                        break;
                }
            }
            parent.rdes.updateChart(obj.html());
            if(isuptree){
                parent.rtree.initModelTree();
            }
        }
        function showser(flag,uuid,sid){
            $("#reset").click();
            $('#uid').val(uuid);
            $('#sid').val(sid);
            var obj=$(parent.rdes.getTM());
            var dt=obj.find('chart[uuid="'+uuid+'"]');
            var category=dt.attr('category');
            if(flag) {
                if(sid!='') {
                    initDataSets('dss');
                    dt.find('serie[id="' + sid + '"]').each(function () {
                        $('#sname').val($(this).children('name').text());
                        var sv = $(this).children('data').text();
                        if (sv.indexOf("=") == 0) {
                            $('#sdt').hide();
                            var arrvalue = sv.substring(1, sv.length).split('.');
                            initFiles('sds', arrvalue[0]);
                            $('#dss').val(arrvalue[0]);
                            $('#sds').val(arrvalue[1]);
                            $('#sds').show();
                        } else {
                            $('#dss').val("");
                            $('#sds').hide();
                            $('#sdt').val(sv).show();
                        }
                        if(category=='pie') {
                            initDataSets('dssf');
                            var svf = $(this).children('dataf').text();
                            if (svf.indexOf('=') == 0) {
                                var arrv = svf.substring(1, svf.length.split('.'));
                                initFiles('sdsf', arrv[0]);
                                $('#dssf').val(arrv[0]);
                                $('#sdsf').val(arrv[1]);
                                $('#sdsf').show();
                            }else{
                                $('#dssf').val("");
                                $('#sdsf').hide();
                                $('#sdtf').val(svf).show();
                            }
                        }
                    });
                }
                $('.tableshuxing tr').hide();
                $('.sera').show();
                if(category=='pie'){
                    $('.pie').show();
                }else{
                    $('.pie').hide();
                }
            }else {
                $('#text').val(dt.find('titles > text').text());
                $('#subtext').val(dt.find('titles > subtext').text());

                $('.tableshuxing tr').show();
                $('.sera').hide();

                if (category == 'k' || category == 'bar' || category == 'line') {
                    initDataSets('dse');
                    var sv = dt.find('xaxis > type').text() == "category" ? dt.find('xaxis > data').text() : dt.children('yaxis > data').text();
                    if (sv.indexOf("=") == 0) {
                        $('#xdt').hide();
                        var arrvalue = sv.substring(1, sv.length).split('.');
                        initFiles('xds', arrvalue[0]);
                        $('#dse').val(arrvalue[0]);
                        $('#xds').val(arrvalue[1]);
                        $('#xds').show();
                    } else {
                        $('#dse').val("");
                        $('#xds').hide();
                        $('#xdt').val(sv).show();
                    }
                    var yd = dt.find('xaxis > type').text() == "category" ? dt.find('yaxis > formatter').text() : dt.children('xaxis > formatter').text();
                    $('#ydata').val(yd);
                }else{
                    $('.exp').hide();
                }
            }
        }
     </script>
</head>
<body>
<form id="sxform">
    <table id="propertyTable" border="0" cellspacing="0" cellpadding="1" width="100%" class="tableshuxing">
        <thead>
        <tr class="trtitle norm">
            <td class="shuxingleftcolor row"><a href="#"></a></td>
            <td colspan="2" class="tdtitle" width="94%">标题</td>
        </tr>
        </thead>
        <tbody>
        <tr class="trbody norm">
            <td width="6%" class="shuxingleftcolor row"><a href="#"></a></td>
            <td width="47%">主标题</td>
            <td width="47%">
                <input name="text" id="text" style="width:95%" value="" onchange="setv(this.name,this.value,'')">
            </td>
        </tr>
        <tr class="trbody norm">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>副标题</td>
            <td>
                <input name="subtext" id="subtext" style="width:95%" value="" onchange="setv(this.name,this.value,'')">
            </td>
        </tr>
        </tbody>
        <thead>
        <tr class="trtitle exp">
            <td class="shuxingleftcolor row"><a href="#"></a></td>
            <td colspan="2" class="tdtitle" width="94%">
                <select name="xya" id="xya" onchange="seta(this.value)">
                <option value="x" selected="selected">X轴</option>
                <option value="y">Y轴</option>
            </select></td>
        </tr>
        </thead>
        <tbody>
        <tr class="trbody exp">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>类型</td>
            <td>
                <select name="xtype" id="xtype">
                    <option value="top">类别</option>
                </select>
            </td>
        </tr>
        <tr class="trbody exp">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>数据集</td>
            <td>
                <select name="dse" id="dse" onchange="changeDataSetx('xdt','xds',this.value)">
                    <option value=""></option>
                </select>
            </td>
        </tr>
        <tr class="trbody exp">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>数据</td>
            <td>
                <input name="xdt" id="xdt" style="width:95%" value="" onchange="setv(this.name,this.value,'')">
                <select name="xds" id="xds" style="display:none;" onchange="setv(this.name,'='+$('#dse').val()+'.'+this.value,'')"></select>
            </td>
        </tr>
        </tbody>
        <thead>
    <tr class="trtitle exp">
        <td class="shuxingleftcolor row"><a href="#"></a></td>
        <td colspan="2" class="tdtitle" width="94%"><select name="xyb" id="xyb" onchange="setb(this.value)">
            <option value="x">X轴</option>
            <option value="y" selected="selected">Y轴</option>
        </select></td>
    </tr>
    </thead>
        <tbody>
        <tr class="trbody exp">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>类型</td>
            <td>
                <select name="ytype" id="ytype">
                    <option value="middle">值</option>
                </select>
            </td>
        </tr>
        <tr class="trbody exp">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>格式</td>
            <td>
                <input name="ydata" id="ydata" style="width:95%" value="" onchange="setv(this.name,this.value,'')">
            </td>
        </tr>
        </tbody>
        <thead>
        <tr class="trtitle sera">
            <td class="shuxingleftcolor row"><a href="#"></a></td>
            <td colspan="2" class="tdtitle" width="94%">数据序列</td>
        </tr>
        </thead>
        <tbody>
        <tr class="trbody sera">
            <td width="6%" class="shuxingleftcolor"><a href="#"></a></td>
            <td width="47%">名称</td>
            <td width="47%">
                <input name="sname" id="sname" style="width:95%" value="" onchange="setv(this.name,this.value,'s')">
            </td>
        </tr>
        <tr class="trbody sera">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>数据集</td>
            <td>
                <select name="dss" id="dss" onchange="changeDataSetx('sdt','sds',this.value)">
                    <option value=""></option>
                </select>
            </td>
        </tr>
        <tr class="trbody sera">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>数据</td>
            <td>
                <input name="sdt" id="sdt" style="width:95%" value="" onchange="setv(this.name,this.value,'s')">
                <select name="sds" id="sds" style="display:none;" onchange="setv(this.name,'='+$('#dss').val()+'.'+this.value,'s')"></select>
            </td>
        </tr>
        <tr class="trbody sera pie" style="display:none">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>数据集</td>
            <td>
                <select name="dssf" id="dssf" onchange="changeDataSetx('sdtf','sdsf',this.value)">
                    <option value=""></option>
                </select>
            </td>
        </tr>
        <tr class="trbody sera pie" style="display:none">
            <td class="shuxingleftcolor"><a href="#"></a></td>
            <td>字段</td>
            <td>
                <input name="sdtf" id="sdtf" style="width:95%" value="" onchange="setv(this.name,this.value,'s')">
                <select name="sdsf" id="sdsf" style="display:none;" onchange="setv(this.name,'='+$('#dss').val()+'.'+this.value,'s')"></select>
            </td>
        </tr>
        </tbody>
    </table>
    <input type="hidden" id="uid"><input type="hidden" id="sid">
    <input type="reset" id="reset" onclick="" style="display:none">
</form>
</body>
</html>