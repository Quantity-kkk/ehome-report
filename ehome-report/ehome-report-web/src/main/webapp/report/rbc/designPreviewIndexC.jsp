<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
    + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表详情预览页面</title>
<style type="text/css">
html, body {
height: 100%
}
body {
margin: 0px;
padding: 0px;
overflow: hidden;
font-size: 12px;
font-family: "Microsoft YaHei", SimSun, Arial, Sans-Serif;
}
td {
font-size: 12px;
}
#pageindex{text-align:center;height:14px;line-height:1;margin:0;}
.Noprint,.Noprint2 {
background: #fff;
border: 1px solid #3447DF;
padding: 5px;
}
.loadingImg_c {
float: left;
width: 16px;
height: 16px;
background: url(../images/loading.gif) no-repeat left center;
}
.display_c {
font-family: "Microsoft Yahei", "SimSun";
font-size: 10pt;
float: left;
height: 16px;
line-height: 16px;
margin-left: 4px;
}
.queryview-toolbar-button {
height: 18px;
width: 48px;
display: inline-block;
cursor: pointer;
background-color: transparent;
background-repeat: no-repeat;
background-position: left center;
border: none;
font-size: 12px;
margin: 2px 5px;
}
.queryview-export {
width: 55px;
padding: 0 5px;
border: 1px solid #ccc;
position: absolute;
background: #f6f6f5;
z-index: 990;
margin-left: 75px;
margin-top: 0;
display: none;
}
.queryview-export a {
color: #00B0F0;
text-decoration: none;
display: block;
}
.queryview-export a:hover {
background:#277b9d;
color: #fff;
}
.queryview-toolbar-refresh {
background: url(../images/pageImg3.png) no-repeat;margin-top:8px;margin-left:30px;
}
.queryview-toolbar-export {
background: url(../images/pageImg1.png) no-repeat;
}
.queryview-toolbar-print {
background: url(../images/pageImg2.png) no-repeat;
}
.queryview-toolbar-refreshzd {
background: url(../images/pageImg4.png) no-repeat;
}

.queryview-toolbar-save {
background: url(../images/pageImg5.png) no-repeat;
}

#searchaddition span{margin-right:10px;}
.print{padding:10px;display: block;
            box-shadow: none;
            background-color: transparent;
            border: none;width:90%;height:auto;}
</style>
<script src="../lib/jquery.js"></script>
<script src="../lib/echarts/echarts-all.js"></script>
<%
	String reporttype = request.getParameter("reporttype");
	String uid = request.getParameter("uid");	
	String req="";
	Map map=request.getParameterMap();  
    Set keSet=map.entrySet();  
    for(Iterator itr=keSet.iterator();itr.hasNext();){  
        Map.Entry me=(Map.Entry)itr.next();  
        Object ok=me.getKey();  
        Object ov=me.getValue();  
        String[] value=new String[1];  
        if(ov instanceof String[]){  
            value=(String[])ov;  
        }else{  
            value[0]=ov.toString();  
        }  
  
        for(int k=0;k<value.length;k++){  
            //System.out.println(ok+"="+value[k]); 
            if(!ok.equals("reporttype")&&!ok.equals("uid")){
            req +="&amp;"; 
            req += (ok+"="+value[k]);
            }
        }  
      } 
      if(!req.equals("")){
      req=req.replaceAll("&amp;", "&");
      }
 %>
<script type="text/javascript">
	var flag=1;
	var dicData={};
    $(function(){    
       initMain();  
    });
    function initMain(){
    if(parent.window.opener&&parent.window.opener!=undefined&&parent.window.opener.rdes!=undefined){flag=1;}else{flag=0;}
    	if(flag==0){$('#refreshdic').show();}
    	if(flag==1){
    		var tableModel = parent.window.opener.rdes.document.getElementById("tableModel").value;
       		var dataSetsModel = parent.window.opener.rdes.document.getElementById("dataSetsModel").value;
        	var parmsModel = parent.window.opener.rdes.document.getElementById("parmsModel").value;
        	//var fieldModel = parent.window.opener.rdes.document.getElementById("fieldModel").value;
        	$("#tableModel").val(tableModel);
        	$("#dataSetsModel").val(dataSetsModel);
        	$("#parmsModel").val(parmsModel);
        	//$("#fieldModel").val(fieldModel);
        	init(1);
    	}else{
    		$.ajax({
		            url : '../../initPreParms.action', 
		            data:{
		                uid:'<%=uid%>'
		            },
		            type:"post",
		            async: false,
		            success : function(data) {
		                $("#tableModel").val(data.tableModel);
		        		$("#dataSetsModel").val(data.dataSetsModel);
		        		$("#parmsModel").val(data.parmsModel);
		        		$("#fieldModel").val(data.fieldModel);
		        		init(1);
		            }
		   });	
    	}  
    }
    
    function init(page){ 
        var activeParm = $("#activeParm").val();   
        getSearch(page,"",activeParm);
    } 
    
    
    function getSearch(page,list,activeParm){
    	$('#activeParm').val(activeParm);
    	var tableModel = $("#tableModel").val();
		var dataSetsModel = $("#dataSetsModel").val();
		var parmsModel = $("#parmsModel").val();
		var fieldModel = $("#fieldModel").val();
        $("#reporttype").val('<%=reporttype%>');
               
               
        var url = "../../previewDesignNew.action?"+activeParm+"<%=req%>";
        
        	if (window.DOMParser) {
			var domParser = new DOMParser();
			xmlStrDoc = domParser.parseFromString(tableModel, "text/xml");
			}
        
        $.ajax({
            url : url, 
            data:{
                dataSetsModel:dataSetsModel,
                tableModel:tableModel,
                parmsModel:parmsModel,
                fieldModel:fieldModel,
                reporttype:'<%=reporttype%>',
                opt:'previewc',
                currentPage:1,
                pageSize:1,
 				pageType:-1,
 				uid:'<%=uid%>',
 				jsonlist:list
            },
            type:"post",
            success : function(data) {
                //alert(data.chartsOpt.length);
                var json=eval(data.chartsOpt);
                var hh=0;
                for(var i=0;i<json.length;i++){
	     			var $div = $("<div class='echart' style='width:"+json[i].width+"px;height:"+json[i].height+"px'></div>");
	     			$('#page1').append($div);
	     			var myChart = echarts.init($div[0]);
	     			myChart.setOption(json[i].option);
	     			hh+=parseInt(json[i].height);
	     		}
	     		$('#page1').css('height',hh+30+'px');
            },
            error:function(){alert('数据加载出错，请联系管理员！');},
            beforeSend:function(){$('.Noprint').show();},
            complete:function(){$('.Noprint').hide();}
        });
    	
    	
    }    
	
</script>
</head>

<body>
  <form action="" id="exportData" method="post" style="display:none;">
  	<textarea rows="" cols="" id="tableModel" name="tableModel" style="display:none;" ></textarea>
	<textarea id ="dataSetsModel" name="dataSetsModel" style="display:none;"></textarea>
	<textarea id ="parmsModel" name="parmsModel" style="display:none;"></textarea>
   	<textarea id ="reporttype" name="reporttype" style="display:none;"></textarea>
   	<textarea id ="fieldModel" name="fieldModel" style="display:none;"></textarea>
   	<input type="hidden" name="activeParm" id="activeParm"/>
   	<input type="hidden" name="jsonlist" id="jsonlist"/>
   	<input id="frmsub" type="submit"/>
  </form>
<table style="width: 100%; height: 100%; table-layout: fixed;" border="0" cellspacing="0" cellpadding="0">
  <tbody>
<tr height="100%" width="100%">
      <td class="_outerFrame" valign="top"  height="100%"><div class="_frameViewContainer"
                style="width: 100%; height: 100%; overflow: scroll;">
          <div id="page1" style="width:100%;height:100%;z-index:-1;">
                            </div>
        </div>
        </div></td>
    </tr>
</tbody>
</table>
 <div class="Noprint"
                        style="left: 45%; top: 35%; display: none; position: absolute; z-index: 101;"> <span class="loadingImg_c" id="loadingImg"></span> <span
                            class="display_c" id="display" style="cursor: pointer;">加载中... </div>
        </div>
</body>
</html>