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
    <script>
    	var dicdata={};
    	var dataSetName;
    	var dicDataType;
    	var v='<%=request.getParameter("v")%>';
        var nm='<%=request.getParameter("n")%>';
        $(function() {
            $('#ds').empty();
            $('#ds').append('<option value="">请选择数据集</option>');
            $('#ds').on('change',function(){
           		dicdata = parent.rdes.dicData[this.value];
           		if (dicdata != null)
           			dicDataType = dicdata.dicDataType;
           	});
            var obj=$(parent.rdes.document.getElementById('dataSetsModel').value);
            obj.find('dataset').each(function(i){
            	if($(this).attr('isdic')!=null&&$(this).attr('isdic')=="true")
                $('#ds').append('<option value="'+$(this).attr('name')+'">'+$(this).attr('name')+'</option>');
            });
            if(v!=""&&v.indexOf('=')!=-1){
	            $('#ds').val(v.split('.')[0]);
	            $('#dicTypeVal').val(v.split('=')[1]);
            }
            //$('#ds').selectmenu('refresh', true);
        });
        function submit(){
        	if(dicdata == null || jQuery.isEmptyObject(dicdata)){
        		dicdata = parent.rdes.dicData[$('#ds').val()];
        	}  
        	var dicvv='';  	
        	if(dicdata==undefined||dicdata ==null){
        		dicvv='';
        		dicdata='';
        	}else{
	        	dicDataType = dicdata.dicDataType;
	        	dicdata = dicdata[$('#dicTypeVal').val()];   
        		dicvv=$('#ds').val()+'.'+dicDataType+'='+$('#dicTypeVal').val();
        	}
        	var dataXml = parent.rdes.document.getElementById("dataSetsModel").value;
        	var domParser = new DOMParser();
			var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
			for(var i=0;i<xmlStrDoc.childNodes[0].childNodes.length;i++){
				if(xmlStrDoc.childNodes[0].childNodes[i].getAttribute('name')==parent.rprop.$("#ds").val()){
					var fields = xmlStrDoc.childNodes[0].childNodes[i].getElementsByTagName("fields")[0].children;
					for(var j=0;j<fields.length;j++){
						if(fields[j].getAttribute('name')==parent.rprop.$("#dsvs").val()){
							if(fields[j].getElementsByTagName("datadic").length==0){
								var dataDic=xmlStrDoc.createElement("datadic");
								dataDic.innerHTML=JSON.stringify(dicdata);
								fields[j].appendChild(dataDic);
							}else{
							 	fields[j].getElementsByTagName("datadic")[0].innerHTML=JSON.stringify(dicdata);
							}
							break;
						}
					}
					parent.rdes.document.getElementById("dataSetsModel").value = xmlStrDoc.childNodes[0].outerHTML;
					break;
				}
			}
        	$('#dic',parent.document).val(dicvv);
        	parent.rprop.document.getElementById(nm).value=dicvv;
        	parent.rdes.Handsontable.dataFormat.setDic(dicvv);
        	parent.closedialog();
        }
    </script>
    <style type="text/css">
        body {font-size:13px;font-weight: normal;margin:0;}
        input {border: 1px solid #ccc;height: 26px; margin-right: 10px;width:200px;}
        select{height: 31px;border: 1px solid #ccc;width:200px;}
        .submit{width:auto;padding:5px 15px;background: none repeat scroll 0 0 #277b9d;border: 1px solid #ccc;color:#fff;height: 30px;}
        .reset{width:auto;padding:5px 15px;background: none repeat scroll 0 0 gray;border: 1px solid #ccc;color:#fff;height: 30px;}
        #dicTypeVal{width:188px;padding:0 5px;height:26px;border:1px solid #ccc;}
    </style>
</head>
<body>
<table width="60%" cellspacing="5" align="center">
    <tr>
        <td width="60">数据集</td>
        <td><label for="ds"></label>
            <select name="ds" id="ds">
            </select></td>
        </tr>
    <tr>
        <td>字典类型</td>
        <td valign="top"><input type="text" name="dicTypeVal" id="dicTypeVal"/></td>
    </tr>
    <tr>
    <td> </td>
    <td width="60" valign="top"><input type="button" name="sub" id="sub" class="submit" value="确认" onclick="submit()" />
            <!-- <br /><br />
            <input type="button" name="reset" id="reset" value="取消" class="reset" onclick="parent.closedialog();" />--></td>
    
    </tr>
</table>
</body>
</html>