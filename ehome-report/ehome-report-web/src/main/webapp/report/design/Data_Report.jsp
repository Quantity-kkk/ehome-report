<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
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
   <script type="text/javascript" src="../lib/layer/layer.js"></script>
    <script src="../lib/vl/jquery.validate.min.js"></script>
    <script src="../lib/vl/localization/messages_zh.js"></script>
    <script>
        var temstr='';
        var filestr='';
        var parmstr='';
        var sDataSetsName=null;
        var parmData={};
        var dataSetsMap={};
        $(document).ready(function() {
        	$("#temlist").empty();
			$("#temlist").append("<option value=''>请选择子报表</option>"); 
			var frameid='<%=request.getParameter("frameid")%>';
        	$.ajax({
    			url : "../../selectAllReportFile.action", 
    			type:"post",
    			success : function(data) {
    				for(var key in data){
    					var str = "";
    					if(frameid==data[key].uuid){
    						str = " selected = 'selected'";
    					}
    					$("#temlist").append("<option value='"+data[key].uuid+"' "+str+">"+data[key].name+"</option>"); 
    				}
    				$('#temlist').selectmenu('refresh', true);
    			}
    		});
        	
            $('#temlist').selectmenu({
                change:function(){
                    sDataSetsName = this.value;
                    $("#plist").empty();
                    $.ajax({
            			url : "selectAllParmsByUUID.action", 
            			type:"post",
            			data:{
            				uuid:sDataSetsName
            			},
            			success : function(data) {
            				for(var key in data){
            					parmstr +="<option value="+key+">"+key+"</option>";
            				}
            			}
            		});
                }
            });
            
        });
        
        function save(){
            parent.rdes.Handsontable.expression.setFrameid($('#temlist').val(),$('#temlist').find('option:selected').text());
        	 parent.closedialog();
        }
    </script>
    <style type="text/css">
        body {font-size: 75%;font-weight: normal;margin:0;}
        .top{width:100%;overflow:hidden;display:block;}
        .top p{display:block;float:left;margin-left:10px;}
        .top #temlist {width: 200px;}
        .top .filelist {width: 160px;}
        .top .parmlist {width: 160px;}
        .button{margin-left:100px;padding:5px 20px;}
    </style>
</head>
<body>
<div class="top">
    <p><select name="temlist" id="temlist"></select></p>
    <p><input id="submit" class="button" type="button" value="保存" onclick="save()"/></p>
</div>
</body>
</html>