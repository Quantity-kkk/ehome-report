<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <title></title>
    <link href="../lib/ui/jquery-ui.min.css" rel="stylesheet">
    <script src="../lib/jquery.min.js"></script>
    <script type="text/javascript" src="../lib/layer/layer.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script src="../lib/echarts/echarts-all.js"></script>
	<script src="../lib/echarts/echartsOption.js"></script>
    <style type="text/css" media="all">
        .tools{position:fixed;top:0;left:0;width:100%;padding:5px;overflow:hidden;text-align:center;z-index:10;}
        .tools p{ margin: 5px;width: 55px;height:80px;cursor:pointer;text-align:center;border:1px solid blue;display:inline-block;}
        .tools p img{filter:alpha(opacity=50);-moz-opacity:0.5;opacity: 0.5;}
        .main{margin:100px auto 20px auto;width:780px;height:1100px;border:1px dashed #0000FF;overflow:hidden;}
        .main p{position:relative;border:1px solid #ccc;display:block;padding:10px;float:left;margin:10px;min-width:200px;min-height:50px;}
        .main p a.close{curspor:pointer;z-index:10;width:12px;height:12px;position:absolute;top:2px;right:2px;background:url(../images/chart/close.png) no-repeat;display:none;}
        #tips{width:200px;position:fixed;top:200px;right:10px;color:red;text-align:center;}
    </style>
    <style type="text/css" media="print">
        .tools,#tips{display:none;}
        .main{border: none;}
    </style>
    <script>
    var echartData = {};
    var selectedChart;
        $(function() {
            var $tableModel = $('#tableModel');
            var $dataSetsModel = $('#dataSetsModel');
            var $parmsModel=$('#parmsModel');
            var $XMLModel = $('#XMLModel').val('<?xml version="1.0" encoding="UTF-8"?><dhccReport reportDescription="新报表"><dataSet></dataSet><body></body></dhccReport>');
            var myuuid=parent.GetQueryString("uuid");
            var setData = [];
            var tableStr,dataSetsStr;
            var xmlStrDoc = null;
            if(myuuid ==null || myuuid.toString().length<1){
                $tableModel.val('<body><charts></charts></body>');
                tableStr = '<body><charts></charts></body>';
                $dataSetsModel.val('');
                dataSetsStr= '';
                init();
                loadData(false);
                initData();
            }else {
                $.ajax({
                    url: "../../openDesignXML.action",
                    type: "post",
                    data: {
                        uuid: myuuid
                    },
                    success: function (data) {
                        tableStr = data.xml.body;
                        $tableModel.val(tableStr);
                        dataSetsStr = data.xml.dataSets;
                        $dataSetsModel.val(dataSetsStr);
                        $parmsModel.val(data.xml.parmslist);
                        $('#rptTitle',parent.document).text(data.xml.title);
                        init();
                        loadData(true);
                        initData();
                    }
                });
            }
        });
        function init(){
            $( "#sortable" ).sortable({revert: true});
            $("#draggable p ").attr("title","拖拽至虚线框");
            $( "#draggable p" ).draggable({//拖拽
                connectToSortable: "#sortable",
                helper: "clone",
                revert: "invalid",
                stop: function(event,ui) {
                    initData();
                    initchart($(this).attr('type'),$(ui.helper[0]));
                }
            });
            parent.rtree.initModelTree();
        }
        function initData(){
            $( "#sortable p").resizable({containment: "#sortable",
                resize: function( event, ui ) {
                	$('#tips').html('宽度：'+ui.size.width+"px,高度："+ui.size.height+'px');
                },
                stop:function(event, ui){
                	$('#tips').empty();
                	var uuid=$(this).attr('uuid')!=undefined?$(this).attr('uuid'):$(this).prop('uuid');
                	var $d1 = $($(this).find('.echart')[0]);
                	$d1.css('width',ui.size.width+"px");
                	$d1.css('height',ui.size.height+"px");
                	/* $d1.find('div').css('width',ui.size.width+"px");
                	$d1.find('div').css('height',ui.size.height+"px");
                	$d1.find('canvas').css('width',ui.size.width+"px");
                	$d1.find('canvas').css('height',ui.size.height+"px"); */
                	echartData[uuid].resize();
                }
            }).draggable({//拖拽
                cursor: "move", containment: "#containment-wrapper", scroll: false,revert:true,
                drag: function( event, ui ) {
                    //$('#tips').html('上：'+ui.offset.top+"px,左："+ui.offset.left+'px');
                },stop:function(event, ui){$('#tips').empty();} });
            $( "#sortable p img").detach();
            $( "#sortable p").unbind('click').removeAttr('title');//解除重复绑定、移除标题
            $( "#sortable p").bind('click',function(){//绑定点击事件
                var uuid=$(this).attr('uuid')==undefined?$(this).prop('uuid'):$(this).attr('uuid');
                selectedChart = echartData[uuid];
                $( "#sortable p a.close").detach();//移除重复的删除
                $( "#sortable p").css('border','1px solid #cccccc').children('a.close').hide();
                $(this).css('border','1px dashed red').append('<a class="close" title="删除" onclick="$(this).parent().detach();deleteChart($(this).parent().attr(\'uuid\')==undefined?$(this).parent().prop(\'uuid\'):$(this).parent().attr(\'uuid\'));"></a>').children('a.close').show();
                if($(this).attr('uuid')==undefined&&$(this).prop('uuid')==undefined){
                    var guid=parent.guid();
                    $(this).prop('uuid',guid);
                    addChart(guid,$(this).width(),$(this).height(),$(this).attr('type'));
                }else{
                    var obj=$(getTM());
                    var width=$(this).width();
                    var height=$(this).height();
                    obj.find('chart[uuid="'+uuid+'"]').attr('width',width).attr('height',height);
                    updateChart(obj.html());
                }
                parent.rtree.initModelTree();
                parent.rtree.selectByParam(uuid);//选择树节点
            });
        }
        function initchart(type,$chart){
        	$chart.append("<div class='echart' style='width:100%;height:100%'></div>"); 
        	var guid=parent.guid();
        	$chart.prop('uuid',guid);
            addChart(guid,$chart.width(),$chart.height(),type);
            
        	var myChart = echarts.init($chart.find('.echart')[0]);
        	myChart.clear();
        	var option = createOption(type);
			myChart.setOption(option);
			echartData[guid] = myChart;
        }
        function initchartShow(guid,type,$chart){
        	$chart.append("<div class='echart' style='width:100%;height:100%'></div>"); 
        	$chart.prop('uuid',guid);
            addChart(guid,$chart.width(),$chart.height(),type);
            
        	var myChart = echarts.init($chart.find('.echart')[0]);
        	myChart.clear();
        	var option = createOption(type);
			myChart.setOption(option);
			echartData[guid] = myChart;
        }
        function createOption(type){
        	var option;
        	switch(type){
			case "line":
				option = optionData.line;
				break;
			case "bar":
				option = optionData.bar;
				break;
			case "k":
				option = optionData.k;
				break;
			case "pie":
				option = optionData.pie;
				break;
			case "radar":
				option = optionData.radar;
				break;
			case "chord":
				option = optionData.chord;
				break;
			case "gauge":
				option = optionData.gauge;
				break;
			default :
				option = optionData.line;
				break;
			}
        	return option;
        }
        function updateChartDom(t,v){
        	var opt = selectedChart.getOption();
        	if(t=="text"||t=="subtext"){
        		opt.title[t] = v;
        	}
        	selectedChart.setOption(opt);
        	selectedChart.refresh();
        }
        function loadData(v){
            var xmlStrDoc = null;
            var obj=$(getTM());
            obj.find('chart').each(function(){
            	var $p = $('<p type="'+$(this).attr('category')+'" uuid="'+$(this).attr('uuid')+'" style="width:'+$(this).attr('width')+'px;height:'+$(this).attr('height')+'px"></p>');
            	$p.append("<div class='echart' style='width:"+$(this).attr('width')+"px;height:"+$(this).attr('height')+"px'></div>"); 
            	var myChart = echarts.init($p.find('.echart')[0]);
            	var option = createOption($(this).attr('category'));
    			myChart.setOption(option);
    			echartData[$(this).attr('uuid')] = myChart;
            	$('#sortable').append($p);
            	if(v){
            	initchartShow($(this).attr('uuid'),$(this).attr('category'),$p);
            	}
            });
        }
        function getTM(){
            var str=$('#tableModel').val();
            return str;
        }
        function getDS(){
            return $('#dataSetsModel').val();
        }
        ///选择chart
        function selectChart(id){
            $('#sortable p').each(function(){
                var uuid=$(this).attr('uuid')!=undefined?$(this).attr('uuid'):$(this).prop('uuid');
                if(uuid==id){
                    $( "#sortable p a.close").detach();//移除重复的删除
                    $( "#sortable p").css('border','1px solid #cccccc').children('a.close').hide();
                    $(this).css('border','1px dashed red').append('<a class="close" title="删除" onclick="$(this).parent().detach()"></a>').children('a.close').show();
                }
            });
        }
        function addChart(id,width,height,category) {
            var obj = $(getTM());
            var suid=parent.guid();
            if (category == 'k' || category == 'bar' || category == 'line') {
                obj.append('<Chart category="' + category + '" uuid="' + id + '" width="' + width + '" height="' + height + '"><titles><text>主标题</text><subtext>副标题</subtext></titles><xAxis><type>category</type><data></data></xAxis><yAxis><type>value</type><formatter></formatter></yAxis><series><serie id="'+suid+'"><name>序列名称</name><type>' + category + '</type><data>[]</data><markLine><type>average</type><name>平均值</name></markLine></serie></series></Chart>');
            } else {
                obj.append('<Chart category="' + category + '" uuid="' + id + '" width="' + width + '" height="' + height + '"><titles><text>主标题</text><subtext>副标题</subtext></titles><series><serie id="'+suid+'"><name>序列名称</name><type>' + category + '</type><data>[]</data><markLine><type>average</type><name>平均值</name></markLine></serie></series></Chart>');
            }
            $('#tableModel').val('<body><charts>' + obj.html() + '</charts></body>');
            parent.rtree.initModelTree();
        }
        function updateChart(str){
            $('#tableModel').val('<body><charts>'+str+'</charts></body>');
        }
        function deleteChart(uuid){
            var obj=$(getTM());
            obj.find('chart[uuid="'+uuid+'"]').detach();
            $('#tableModel').val('<body><charts>'+obj.html()+'</charts></body>');
            parent.rtree.initModelTree();
        }
        function RunOnBeforeUnload() {window.onbeforeunload = function(){ return ""; } }
    </script>
</head>
<body>
<div class="tools" id="draggable">
    <p type="line"><img src="../images/chart/line.jpg"></p>
    <p type="bar"><img src="../images/chart/bar.jpg"></p>
    <p type="k"><img src="../images/chart/k.jpg"></p>
    <p type="pie"><img src="../images/chart/pie.jpg"></p>
    <p type="radar"><img src="../images/chart/radar.jpg"></p>
    <p type="chord" style="display:none"><img src="../images/chart/chord.jpg"></p>
    <p type="gauge"><img src="../images/chart/gauge.jpg"></p>
</div>
<div id="tips"></div>
<div class="main" id="sortable">

</div>
<div class='echarts' style="width:100%;height:100%;display:none;"></div>
<textarea id ="tableModel" style="display:none;"></textarea>
<textarea id ="dataSetsModel" style="display:none;"></textarea>
<textarea id ="parmsModel" style="display:none;"></textarea>
<textarea id ="XMLModel" style="display:none;"></textarea>
</body>
</html>