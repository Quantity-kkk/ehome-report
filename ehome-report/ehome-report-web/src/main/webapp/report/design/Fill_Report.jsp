﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <title>数据集配置</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../lib/ui/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../lib/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <style type="text/css">
        body {font-size: 75%;font-weight: normal;margin:0;}
        .submit{margin-left:200px;padding:5px 20px;}
        .divtab{display:block;marin-top:10px;text-align:left;margin-left:30px;border:1px solid #666;}
        .stytable tr{height:30px;line-height:30px;}
        .stytable tr td{font-size:13px;}
		.ParameterConf tr td{border:1px solid #999; text-align:center; line-height:0px;}
		#tabs .ParameterConf .ui-state-default{ margin-top:0px; border-radius:0;}
        .tdselect{width:175px;height:26px;}
		#tabs .ui-state-default{ margin-top:10px;}
		.paraButton{ width:50px; float:right; margin-right:6px;}
		.paraButton input{ float:left;}
		.temptr{background-color:#e1e1e1;}
		#updatefields input{width:70px;}
		#selectoption tr{height:30px;line-height:30px;}
        #selectoption tr td{font-size:13px;}
		#selectoption tr td{border:1px solid #999; text-align:center; line-height:0px;}
		#selectoption thead tr td{background-color:#eee}
		.ParameterConf thead tr td{background-color:#eee}
		.ParameterConf1 tr td{line-height:30px;}
		#checkexp input{width:180px;}
    </style>
    <script type="text/javascript" src="../lib/jquery.min.js"></script>
    <script src="../lib/ztree/jquery.ztree.min.js"></script>
    <script src="../lib/ui/jquery-ui.min.js"></script>
    <script src="../lib/vl/jquery.validate.min.js"></script>
    <script src="../lib/vl/localization/messages_zh.js"></script>
    <script>
    	var tempid="";
    	var colandrow="";
        $(function(){
			$("#tabs").tabs();
			$(".isbutton").button();
			
			var getvalue = parent.rdes.Handsontable.expression.getFillProperties();
			
			tempid = getvalue.split(",")[0];
			colandrow = getvalue.split(",")[1];
			if(tempid !=""){
					$.ajax({
						url : "../../getDataSourceNameAll.action", 
						type:"post",
						async: false,
						success : function(data) {
							for(var i=0;i<data.length;i++){
								$("#dataSourceName").append("<option value='"+data[i].dataSourceName+"'>"+data[i].dataSourceName+"</option>"); 
							}
							
							var dataXml = parent.rdes.document.getElementById("fieldModel").value;
							var xmlStrDoc = $.parseXML(dataXml);
							for(var i=0;i<xmlStrDoc.childNodes[0].childNodes.length;i++){
								var xmlNode = xmlStrDoc.childNodes[0].childNodes[i];
								if(xmlNode.getAttribute('id')==tempid){
									$("#datatype").val(xmlNode.getAttribute('datatype'));
						        	$("#editstyle").val(xmlNode.getAttribute('editstyle'));
						        	
						        	if($("#editstyle").val()=="1" || $("#editstyle").val()=="2" || $("#editstyle").val()=="")
										$("#setfield").hide();
									else
										$("#setfield").show();
										
									$("#editstyleval").val(xmlNode.getAttribute('styleval'));	
						        	
						        	var isWrite = xmlNode.getAttribute('iswrite');
						        	if(isWrite=="1")
						        		$("input[name=iswirte]").attr("checked","checked");
						        	
						        	$("#dataSourceName").val(xmlNode.getAttribute('datasourcename'));
						        	$("#datatable").val(xmlNode.getAttribute('tablename'));
						        	
						        	gettablename($("#dataSourceName").val(),xmlNode.getAttribute('tablename'));
						        	
						        	for(var j=0;j<xmlNode.childNodes.length;j++){
						        		var childNode = xmlNode.childNodes[j];
						        		var fieldName = childNode.getAttribute("name");
						        		var isKey = childNode.getAttribute("iskey");
						        		var updateVal = childNode.getAttribute("updateval");
						        		if(isKey=="1")
						        			isKey="checked='checked'";
						        		var temptr=$("#relatefileds").html();
						        		var tempstr="<tr><td>"+(j+1)+"</td><td width='180'>"+temptr+"</td><td width='70'><input type='text' value='"+updateVal+"' /></td><td><input type='checkbox'  "+isKey+"/></td></tr>";
						        		$("#updatefields tbody").append(tempstr);
						        		$("#updatefields tbody tr").eq(j).find("select").val(fieldName);
						        	}
						        	
						        	var checkstr="";
						        	var checkexpval=xmlNode.getAttribute('checkexpval');
						        	if(checkexpval!="" && checkexpval!=undefined){
						        		var temparr = checkexpval.split(";");
						        		for ( var k = 0; k < temparr.length-1; k++) {
											var tempstr1=temparr[k].split(":");
											checkstr +="<tr><td style='text-align:center;'>"+(k+1)+"</td><td><input type='text' style='width:180px;' name='value0' value='"+tempstr1[0]+"'/></td><td><input type='text' style='width:180px;' name='value1' value='"+tempstr1[1]+"'/></td></tr>";
										}
						        	}
						        	$("#checkexp tbody").append(checkstr);
								}
							}
							
						}
					});										
			}else{
				$.ajax({
					url : "../../getDataSourceNameAll.action", 
					type:"post",
					async: false,
					success : function(data) {
						for(var i=0;i<data.length;i++){
							$("#dataSourceName").append("<option value='"+data[i].dataSourceName+"'>"+data[i].dataSourceName+"</option>"); 
						}
						gettablename($("#dataSourceName").val(),"");
					}
				});
			
			}
			
			//切换数据源
			$("#dataSourceName").on("change",function(){
				var datasourcename = $("#dataSourceName").val();
		    	gettablename(datasourcename,$("#datatable").val());
			});
			
			//切换数据表
			$("#datatable").on("change",function(){
				var datasourcename = $("#dataSourceName").val();
				var tableName = $("#datatable").val();
   				getFiledNames(datasourcename,tableName);
   				$("#updatefields tbody").html("");
			});
			
			//table tr单击事件
			$("#updatefields tbody tr,#selectoption tbody tr,#checkexp tbody tr").bind("click",function(){
				$(this).addClass("temptr");
				$(this).siblings().removeClass("temptr");
			});
			
			//风格设置显示
			$("#editstyle").change(function(){
				if($(this).val()=="1" || $(this).val()=="2" || $(this).val()=="")
					$("#setfield").hide();
				else
					$("#setfield").show();
			});							
        });
        
        //根据数据源获取表名
        function gettablename(datasourcename,tableName){
        	$.ajax({
				url : "getDataTableNameAll.action", 
				type:"post",
				async: false,
				data:{dataSourceName : datasourcename},
				success : function(data) {
					$("#datatable").empty();
					for(var i=0;i<data.length;i++){
						$("#datatable").append("<option value='"+data[i]+"'>"+data[i]+"</option>"); 
					}
					if(tableName!=""){
						$("#datatable").val(tableName);
					}
					getFiledNames(datasourcename,$("#datatable").val());
				}
			});
        }
        
        //获取字段名
        function getFiledNames(dataSourceName,tableName){
        	$.ajax({
        		type:"post",
        		async: false,
        		url:"getFieldNames.action",
        		data:{dataSourceName:dataSourceName,tableName:tableName},
        		success:function(data){
        			var optionstr="";
        			$("#relatefileds").empty();
        			$("#relatefileds").append("<select class='addselect tdselect'></select>");
        			for(var i=0;i<data.length;i++){
        				optionstr+="<option value='"+data[i]+"'>"+data[i]+"</option>";
        			}
        			$(".addselect").append(optionstr);
        			$(".addselect").val("");
        		}
        	});	
        }
        
        //新增字段
        function addField(){
        	var tempselect = $("#relatefileds").html();
        	var index = $("#updatefields tbody tr").length;
        	var newstr = "<tr><td>"+(index+1)+"</td><td width='180'>"+tempselect+"</td><td width='70'><input type='text'/></td><td><input type='checkbox' /></td></tr>";
        	$("#updatefields tbody").append(newstr);
        	$("#updatefields tbody tr").bind("click",function(){
				$(this).addClass("temptr");
				$(this).siblings().removeClass("temptr");
			});
        }
        
        //删除字段
        function delField(){
        	$("#updatefields tbody tr").each(function(){
        		if($(this).hasClass("temptr"))
        			$(this).remove();
        	});
        	
        	var index=1;
        	$("#updatefields tbody tr").find("td:eq(0)").each(function(){
        		$(this).html(index);
        		index++;
        	});
        }
        
        //新增校验
        function addCheck(){
        	var tempselect = $("#relatefileds").html();
        	var index = $("#checkexp tbody tr").length;
        	var newstr = "<tr><td>"+(index+1)+"</td><td width='150'><input type='text'/></td><td width='150'><input type='text'/></td></tr>";
        	$("#checkexp tbody").append(newstr);
        	$("#checkexp tbody tr").bind("click",function(){
				$(this).addClass("temptr");
				$(this).siblings().removeClass("temptr");
			});
        }
        
        //删除校验
        function delCheck(){
        	$("#checkexp tbody tr").each(function(){
        		if($(this).hasClass("temptr"))
        			$(this).remove();
        	});
        	
        	var index=1;
        	$("#checkexp tbody tr").find("td:eq(0)").each(function(){
        		$(this).html(index);
        		index++;
        	});
        }
        
        //设置下拉框、单选、多选按钮选项
        function setOption(){
        	var editStyle=$("#editstyle").val();
        	if(editStyle=="6")
        		setTime();
        	else
        		setOtherOption();
        }
        
        //设置时间
        function setTime(){
        	var tempstr="<div class='dialog-form'><label>取值类型：</label><select id='selecttime'><option value='1'>年月日时分秒</option><option value='2'>年月日</option><option value='3'>年月</option><option value='4'>年</option><option value='5'>时分秒</option></select></div>";
        	$(tempstr).dialog({
                title: "时间设置",
                autoOpen: true,
                height: 200,
                width: 270,
                modal: true,
                resizable: false,
                buttons: {
                    "确认": function () {
                    	$("#editstyleval").val($("#selecttime").val());
                    	$(this).remove();
                    },
                    "取消":function(){
                    	$(this).remove();
                    }
                },
                close: function () {
                    $(this).remove();
                }
            });
            var editstyleval=$("#editstyleval").val();
            if(editstyleval!=""){
            	$("#selecttime").val(editstyleval);
            }
            //$(".isbutton").button();
            $("#selectoption").parent().next().find(".ui-dialog-buttonset").css("float","none").css("textAlign","center");
        }
        
        //设置下拉框、单选、多选
        function setOtherOption(){
        	var editstyleval=$("#editstyleval").val();
        	var tempstr="";
        	var str ="";
        	if(editstyleval!=""){
        		var temparr = editstyleval.split(";");
        		for ( var i = 0; i < temparr.length-1; i++) {
					var tempstr1=temparr[i].split(":");
					str +="<tr><td style='text-align:center;'>"+(i+1)+"</td><td><input type='text' style='width:90%;' name='value0' value='"+tempstr1[0]+"'/></td><td><input type='text' style='width:90%;' name='value1' value='"+tempstr1[1]+"'/></td></tr>";
				}
        	}else{
        		str="<tr><td style='text-align:center;'>1</td><td><input type='text' style='width:90%;' name='value0'/></td><td><input type='text' style='width:90%;' name='value1'/></td></tr>";
        		
        	}
        	tempstr="<div class='dialog-form'><table id='selectoption' style='width:80%;border-collapse:collapse;float:left;'><thead><tr><td width='60'>序号</td><td width='100'>代码值</td><td width='80'>显示值</td></tr></thead><tbody>"+str+"</tbody></table><div class='paraButton'>"
							+"<input type='button' value='新增'  class='isbutton' onclick='addOption()'  style='margin-bottom:4px;'/>"
							+"<input type='button' value='删除'  class='isbutton' onclick='delOption()'/>"
							+"</div></div>";
        	
        	$(tempstr).dialog({
                title: "选项内容设置",
                autoOpen: true,
                height: 300,
                width: 470,
                modal: true,
                resizable: false,
                buttons: {
                    "确认": function () {
                    	var tempStr="";
                    	$("#selectoption tr").not(":eq(0)").each(function(){
                    		tempStr+=$(this).find("input[name=value0]").val()+":"+$(this).find("input[name=value1]").val()+";";
                    	});
                    	$("#editstyleval").val(tempStr);
                    	$(this).remove();
                    },
                    "取消":function(){
                    	$(this).remove();
                    }
                },
                close: function () {
                    $(this).remove();
                }
            });
            $(".isbutton").button();
            $("#selectoption").parent().next().find(".ui-dialog-buttonset").css("float","none").css("textAlign","center");
            $("#selectoption tbody tr").bind("click",function(){
				$(this).addClass("temptr");
				$(this).siblings().removeClass("temptr");
			});
        }
        
        //添加选项
        function addOption(){
        	var index = parseInt($('#selectoption tbody tr:last td').eq(0).html());
        	var tempstr ="<tr><td style='text-align:center;'>"+(index+1)+"</td><td><input type='text' style='width:90%;' name='value0'/></td><td><input type='text' style='width:90%;' name='value1'/></td></tr>"; 
        	$("#selectoption tbody").append(tempstr);
        	$("#selectoption tbody tr").bind("click",function(){
				$(this).addClass("temptr");
				$(this).siblings().removeClass("temptr");
			});
        }
        
        //删除选项
        function delOption(){
        	$("#selectoption tbody tr").each(function(){
        		if($(this).hasClass("temptr"))
        			$(this).remove();
        	});
        	
        	var index=1;
        	$("#selectoption tbody tr").find("td:eq(0)").each(function(){
        		$(this).html(index);
        		index++;
        	});
        }
        
        //保存
        function save(){
        	var fillReportMap={};
        	var dataType = $("#datatype").val();
        	var editStyle=$("#editstyle").val();
        	var isWrite = "";
        	if($("input[name=iswirte]").is(':checked')==true)
        		isWrite="1";
        	else
        		isWrite="0";
        	var dataSourceName = $("#dataSourceName").val();
        	var dataTable = $("#datatable").val();
        	var editstyleval=$("#editstyleval").val();
        	
        	var saveflag=true;
        	
        	var fieldRelate = "";
        	if($("#updatefields tbody tr").length >0){
        		$("#updatefields tbody tr").each(function(){
	        		if($(this).find("td:eq(2)").children().val() ==""){
	        			alert("请输入需要更改的字段!");
	        			saveflag= false;
	        			$(this).find("td:eq(2)").children().css("border","1px solid red");
						return false;	        		
	        		}else{
	        			var isKey = "";
	        			if($(this).find("td:eq(3)").children().is(':checked'))
	        				isKey = "1";
	        			else
	        				isKey = "0";
	        			fieldRelate +=$(this).find("td:eq(1)").children().val()+","+$(this).find("td:eq(2)").children().val()+","+isKey+";";
	        		}
	        		
	        	});
        	}
        	
        	var fieldCheck = "";
        	if($("#checkexp tbody tr").length >0){
        		$("#checkexp tbody tr").each(function(){
	        		if($(this).find("td:eq(2)").children().val() ==""){
	        			alert("请设置校验表达式或出错提示!");
	        			saveflag= false;
	        			$(this).find("td:eq(2)").children().css("border","1px solid red");
						return false;	        		
	        		}else{
	        			fieldCheck +=$(this).find("td:eq(1)").children().val()+":"+$(this).find("td:eq(2)").children().val()+";";
	        		}
	        		
	        	});
        	}
        	if($("#dataSourceName").val()!=""&&($("#datatable").val()==""||$("#datatable").val()==null)){
        		alert("请选择数据表");
        		return false;	
        	}
        	if(saveflag){
        		fillReportMap.dataType = dataType;
				fillReportMap.editStyle = editStyle;
				fillReportMap.editStyleVal = editstyleval;
				fillReportMap.isWrite = isWrite;
				fillReportMap.dataSourceName = dataSourceName;
				fillReportMap.tableName = dataTable;
				fillReportMap.fieldRelate = fieldRelate;//关联字段
				fillReportMap.fieldCheck = fieldCheck;//校验
				fillReportMap.id = colandrow;
				//判断是新增还是修改
				if(tempid =="")
					parsingToXML(fillReportMap,"add");
				else
					parsingToXML(fillReportMap,"edit");
				parent.rdes.Handsontable.expression.setFillProperties(colandrow);
	        	parent.closedialog();
        	}
			
        }
        
        
        function parsingToXML(fillReportMap,state){
			var data ="";
			var dataXml = parent.rdes.document.getElementById("fieldModel").value;
			if (dataXml == ""){
				data +="<fillreports><fillreport id='"+colandrow+"' datasourcename='"+fillReportMap.dataSourceName+"' tablename='"+fillReportMap.tableName+"'"
						+" datatype='"+fillReportMap.dataType+"' editstyle='"+fillReportMap.editStyle+"' styleval='"+fillReportMap.editStyleVal+"' checkexpval='"+fillReportMap.fieldCheck+"' iswrite='"+fillReportMap.isWrite+"'>";
				var temprelate=fillReportMap.fieldRelate;
				if(temprelate !="" && temprelate !=null && temprelate != undefined){
					var temprelatearr = temprelate.split(";");
					for ( var j = 0; j < temprelatearr.length-1; j++) {
						var profield = temprelatearr[j].split(",");
						data+="<field name='"+profield[0]+"' iskey='"+profield[2]+"' updateval='"+profield[1]+"'></field>";
					}
				}
				data +="</fillreport></fillreports>";
				parent.rdes.document.getElementById("fieldModel").value = data;
			}else{
				if(state=="add"){
					var domParser = new DOMParser();
					var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
					data +="<fillreports>";
					for(var z=0;z<xmlStrDoc.childNodes[0].childNodes.length;z++){
						data +=xmlStrDoc.childNodes[0].childNodes[z].outerHTML;
					}
					data +="<fillreport id='"+colandrow+"' datasourcename='"+fillReportMap.dataSourceName+"' tablename='"+fillReportMap.tableName+"'"
						+" datatype='"+fillReportMap.dataType+"' editstyle='"+fillReportMap.editStyle+"' styleval='"+fillReportMap.editStyleVal+"' checkexpval='"+fillReportMap.fieldCheck+"' iswrite='"+fillReportMap.isWrite+"'>";
					var temprelate=fillReportMap.fieldRelate;
					if(temprelate !="" && temprelate !=null && temprelate != undefined){
						var temprelatearr = temprelate.split(";");
						for ( var j = 0; j < temprelatearr.length-1; j++) {
							var profield = temprelatearr[j].split(",");
							data+="<field name='"+profield[0]+"' iskey='"+profield[2]+"' updateval='"+profield[1]+"'></field>";
						}
					}
					data +="</fillreport></fillreports>";
					parent.rdes.document.getElementById("fieldModel").value = data;
				}else if(state=="edit"){
					var domParser = new DOMParser();
					var xmlStrDoc = $.parseXML(dataXml);
					for(var i=0;i<xmlStrDoc.childNodes[0].childNodes.length;i++){
						if(xmlStrDoc.childNodes[0].childNodes[i].getAttribute('id')==fillReportMap.id){
							xmlStrDoc.childNodes[0].childNodes[i].setAttribute("datatype",fillReportMap.dataType);
							xmlStrDoc.childNodes[0].childNodes[i].setAttribute("editstyle",fillReportMap.editStyle);
							xmlStrDoc.childNodes[0].childNodes[i].setAttribute("styleval",fillReportMap.editStyleVal);
							xmlStrDoc.childNodes[0].childNodes[i].setAttribute("iswrite",fillReportMap.isWrite);
							xmlStrDoc.childNodes[0].childNodes[i].setAttribute("datasourcename",fillReportMap.dataSourceName);
							xmlStrDoc.childNodes[0].childNodes[i].setAttribute("tablename",fillReportMap.tableName);
							xmlStrDoc.childNodes[0].childNodes[i].setAttribute("checkexpval",fillReportMap.fieldCheck);
							xmlStrDoc.childNodes[0].childNodes[i].innerHTML="";
							var temprelate=fillReportMap.fieldRelate;
							if(temprelate !="" && temprelate !=null && temprelate != undefined){
								var temprelatearr = temprelate.split(";");
								for ( var j = 0; j < temprelatearr.length-1; j++) {
									var profield = temprelatearr[j].split(",");
									var newel=document.createElement("field");
									newel.setAttribute("name",profield[0]);
									newel.setAttribute("iskey",profield[2]);
									newel.setAttribute("updateval",profield[1]);
									newel.removeAttribute("xmlns");
									xmlStrDoc.childNodes[0].childNodes[i].appendChild(newel);
								}
							}
							var ohtml= (new XMLSerializer()).serializeToString(xmlStrDoc.childNodes[0]);
							parent.rdes.document.getElementById("fieldModel").value = ohtml;
						}
					}
				}
			}
		}
    </script>

</head>
<body>
<div class="top" id="tabs">
	<ul class="" id="myTab">
       <li><a href="#step1">常规</a></li>
       <li><a href="#step2">更新</a></li>
       <li><a href="#step3">校验</a></li>
     </ul>
	<div id="step1" class="divtab">
			<table class="stytable">
			<tr>
				<td>填报数据类型</td>
				<td style="width:20px;"></td>
				<td>
					<select name="datatype" id="datatype" class="tdselect">
						<option value="1">字符串</option>
						<option value="2">整数</option>
						<option value="3">数值型</option>
						<option value="4">日期型</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>编辑风格</td>
				<td style="width:20px;"></td>
				<td>
					<select name="editstyle" id="editstyle" class="tdselect">
						<option value="1">文本框</option>
						<option value="2">文本域</option>
						<option value="3">下拉列表框</option>
						<option value="4">单选按钮</option>
						<option value="5">复选框</option>
						<option value="6">下拉日历</option>
					</select>
					<input type="hidden" name="editstyleval" id="editstyleval" />
				</td>
				<td><input type="button" style="margin-top:0px;display:none;" value="设置"  class="isbutton" id="setfield" onclick="setOption()" /></td>
			</tr>
			<tr>
				<td>是否可写</td>
				<td style="width:20px;"></td>
				<td><input type="checkbox" name="iswirte" id="iswirte" value="1"/></td>
			</tr>
		</table>
	</div>
	<div id="step2" style="display:none" class="divtab">
		<table class="stytable">
			<tr>
				<td>数据源</td>
				<td style="width:5px;"></td>
				<td>
					<select name="dataSourceName" id="dataSourceName" title="请选择数据源" class="tdselect" required>
						<option value=""></option>
	                </select>
	            </td>
	            <td style="width:35px;"></td>
	            <td>数据表</td>
				<td style="width:5px;"></td>
				<td>
					<select name="datatable" id="datatable" title="请选择数据表" class="tdselect" required>
	                	<option value=""></option>
	                </select>
	                <div style="display:none;" id="relatefileds"></div>
	            </td>
	        </tr>
			<tr>
				<td colspan="7">
					<div>
						<table id="updatefields" style="width:80%;border-collapse:collapse;float:left;" cellpadding="0" cellspacing="0" border="1" class="ParameterConf">
							<thead><tr><td width="80">序号</td><td width="180">字段</td><td width="80">更新值</td><td width="80">主键</td></tr></thead>
							<tbody>
							</tbody>
						</table>
						<div class="paraButton">
							<input type="button" value="新增"  class="isbutton" onclick="addField()"/>
							<input type="button" value="删除"  class="isbutton" onclick="delField()"/>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div id="step3" class="divtab" style="overflow:hidden;">
		<table id="checkexp" style="width:80%;border-collapse:collapse;float:left;" cellpadding="0" cellspacing="0" border="1" class="ParameterConf  ParameterConf1">
			<thead>
				<tr><td width="80">序号</td><td width="150">表达式</td><td width="150">出错提示</td></tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<div class="paraButton">
			<input type="button" value="新增"  class="isbutton" onclick="addCheck()"/>
			<input type="button" value="删除"  class="isbutton" onclick="delCheck()"/>
		</div>
	</div>
	
</div>
<div>
	<p>
		<input id="submit" class="submit iswirte" type="button" value="保存" onclick="save()" />
	</p>
</div>	
</body>
</html>