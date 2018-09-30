var setting = {
	view : {
		addHoverDom : addHoverDom,
		removeHoverDom : removeHoverDom
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : zTreeOnClick,
		beforeRemove : zTreeBeforeRemove
	},
	edit : {
		enable : true,
		showRenameBtn : false,
		showRemoveBtn : function(treeId, treeNode) {
			return treeNode.level == 1;
		},
		removeTitle : "删除"
	}
};
var tree;
var dataMap = {};
var sDataSetsName = null;
var state = "add";
$(document).ready(function() {
	//$('#dsform').validate();
	var dataXml = parent.rdes.document.getElementById("dataSetsModel").value;
	//$("button.add").button({icons: {primary: "ui-icon-plus"}});
	//$("button.edit").button({icons: {primary: "ui-icon-pencil"}});
	//$("button.del").button({icons: {primary: "ui-icon-minus"}});
	//    $('#dslist').on('change',function(){
	//		sDataSetsName = this.value;
	//	});
	$("#DataSourceName").empty();
	$("#DataSourceName").append('<option value=""></option><option value="javabean">javabean</option>');
	$.ajax({
		url : "../../getDataSourceNameAll.action",
		type : "post",
		success : function(data) {
			for (var i = 0; i < data.length; i++) {
				$("#DataSourceName").append("<option value='" + data[i].dataSourceName + "'>" + data[i].dataSourceName + "</option>");
			}
		}
	});
	if (dataXml == "") {
		tree = $.fn.zTree.init($("#treeds"), setting, []);
	} else {
		//		 $('input.submit').button();
		initTree(dataXml);
	}
});
function initTree(dataXml) {
	dataXml = dataXml.trim();
	//	$("#dslist").empty();
	//	$('#dslist').append("<option value=''>请选择数据集</option>"); 
	var domParser = new DOMParser();
	var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
	//	if(dataSets.childNodes.length!=2) return;
	var zNodes = new Array();
	var dataSetsCount = xmlStrDoc.childNodes[0].childNodes.length;
	if (dataSetsCount > 0) {
		zNodes.push({
			id : 1,
			pId : 0,
			name : "数据集列表",
			open : true,
			iconSkin : "home"
		});
	}
	for (var i = 0; i < dataSetsCount; i++) {
		var dataSets = xmlStrDoc.childNodes[0].childNodes[i];
		var id = i + 1;
		var dataSetName = dataSets.getAttribute('name');
		//	$("#dslist").append("<option value='"+dataSetName+"'>"+dataSetName+"</option>");   
		zNodes.push({
			id : id + '0',
			pId : 1,
			name : dataSetName
		});
		zNodes.push({
			id : id + '01',
			pId : id + '0',
			name : "字段"
		});
		zNodes.push({
			id : id + '02',
			pId : id + '0',
			name : "参数"
		});
		if (dataSets.getElementsByTagName("fields")[0].childNodes.length != 0) {
			var field = dataSets.getElementsByTagName("fields")[0].childNodes;
			for (var x = 0; x < field.length; x++) {
				var dd = x + 1;
				var name = field[x].childNodes[0].childNodes[0].nodeValue;
				var type = field[x].childNodes[1].childNodes[0].nodeValue;
				zNodes.push({
					id : id + '010' + dd,
					pId : id + '01',
					name : name + "(" + type + ")"
				});
			}

		}
		var Parameters = dataSets.getElementsByTagName("query")[0].childNodes[2].childNodes;
		var dataSourceName = dataSets.getElementsByTagName("query")[0].childNodes[0].childNodes[0].nodeValue;
		var CommandText = dataSets.getElementsByTagName("query")[0].childNodes[1].childNodes[0].nodeValue;
		var Parameter = {};
		for (var j = 0; j < Parameters.length; j++) {
			var d = j + 1;
			zNodes.push({
				id : id + '020' + d,
				pId : id + '02',
				name : Parameters[j].getAttribute('name')
			});
			Parameter[j] = Parameters[j].getAttribute('name');
		}
		dataMap[dataSets.getAttribute('name')] = {
			dataSetName : dataSetName,
			dataSourceName : dataSourceName,
			dd : dataSets.getAttribute('isdic'),
			CommandText : CommandText,
			Parameter : Parameter
		};
	}
	tree = $.fn.zTree.init($("#treeds"), setting, zNodes);
	initDataSetsSelect();
}
function initDataSetsSelect() {
	parent.rprop.parseDataSets(parent.rdes.document.getElementById("dataSetsModel").value);
}

function parsingToXML(dataSetsMap, state) {
	var data = "";
	var dataXml = parent.rdes.document.getElementById("dataSetsModel").value;
	if (dataXml == "") {
		data += "<datasets><dataset name='" + dataSetsMap.dataSetName + "' isdic='" + dataSetsMap.dd + "'>";
		data += "<fields></fields>";
		data += "<query><datasourcename>" + dataSetsMap.dataSourceName + "</datasourcename>";
		data += "<commandtext>" + dataSetsMap.CommandText.replace(/\r\n/g, " ").replace(/\n/g, " ").replace(/\r/g, " ").replace(/\t/g, " ").replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;").replace(/\"/g, "&quot;").replace(/   */g, " ") + "</commandtext>";
		data += "<parameters>";
		var txtp = dataSetsMap.Parameters.txtp;
		for (var i = 0; i < txtp.length; i++) {
			data += "<parameter name='" + txtp[i] + "' />";
		}
		data += "</parameters></query></dataset></datasets>";
		parent.rdes.document.getElementById("dataSetsModel").value = data;
	} else {
		if (state == "add") {
			var domParser = new DOMParser();
			var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
			data += "<datasets>";
			for (var z = 0; z < xmlStrDoc.childNodes[0].childNodes.length; z++) {
				//				alert(xmlStrDoc.childNodes[0].childNodes[z].toString())
				data += xmlStrDoc.childNodes[0].childNodes[z].outerHTML;
			}
			data += "<dataset name='" + dataSetsMap.dataSetName + "' isdic='" + dataSetsMap.dd + "'>";
			data += "<fields></fields>";
			data += "<query><datasourcename>" + dataSetsMap.dataSourceName + "</datasourcename>";
			data += "<commandtext>" + dataSetsMap.CommandText.replace(/\r\n/g, " ").replace(/\n/g, " ").replace(/\r/g, " ").replace(/\t/g, " ").replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;").replace(/\"/g, "&quot;").replace(/   */g, " ") + "</commandtext>";
			data += "<parameters>";
			var txtp = dataSetsMap.Parameters.txtp;
			for (var i = 0; i < txtp.length; i++) {
				data += "<parameter name='" + txtp[i] + "' />";
			}
			data += "</parameters></query></dataset>";
			data += "</datasets>";
			parent.rdes.document.getElementById("dataSetsModel").value = data;
		} else if (state == "edit") {
			var domParser = new DOMParser();
			var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
			for (var i = 0; i < xmlStrDoc.childNodes[0].childNodes.length; i++) {
				if (xmlStrDoc.childNodes[0].childNodes[i].getAttribute('name') == dataSetsMap.dataSetName) {
					xmlStrDoc.childNodes[0].childNodes[i].setAttribute("isdic", dataSetsMap.dd);
					xmlStrDoc.childNodes[0].childNodes[i].getElementsByTagName("query")[0].childNodes[0].childNodes[0].nodeValue = dataSetsMap.dataSourceName;
					xmlStrDoc.childNodes[0].childNodes[i].getElementsByTagName("query")[0].childNodes[1].childNodes[0].nodeValue = dataSetsMap.CommandText.replace(/\r\n/g, " ").replace(/\n/g, " ").replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;").replace(/\"/g, "&quot;");
					var data = "<parameters>";
					var txtp = dataSetsMap.Parameters.txtp;
					for (var j = 0; j < txtp.length; j++) {
						data += "<parameter name='" + txtp[j] + "' />";
					}
					data += "</parameters>";
					var xmlP = domParser.parseFromString(data, "text/xml");
					xmlStrDoc.childNodes[0].childNodes[i].getElementsByTagName("query")[0].childNodes[2].outerHTML = xmlP.childNodes[0].outerHTML;
					parent.rdes.document.getElementById("dataSetsModel").value = xmlStrDoc.childNodes[0].outerHTML;
					break;
				}
			}
		} else if (state == "del") {
			var domParser = new DOMParser();
			var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
			for (var i = 0; i < xmlStrDoc.childNodes[0].childNodes.length; i++) {
				if (xmlStrDoc.childNodes[0].childNodes[i].getAttribute('name') == sDataSetsName) {
					xmlStrDoc.childNodes[0].childNodes[i].outerHTML = "";
					parent.rdes.document.getElementById("dataSetsModel").value = xmlStrDoc.childNodes[0].outerHTML;
					break;
				}
			}
		}
		sDataSetsName = "";
	}
}
function addparm(pdom, p, v) {
	var n = Math.floor(Math.random() * 1000);
	var vl = parent.rdes.document.getElementById('parmsModel').value;
	var parms = $(vl);
	var parml = '';
	parms.find('parm').each(function() {
		parml += '<option value="' + $(this).attr("name") + '"' + ($(this).attr("name") == v ? " selected" : "") + '>' + $(this).attr("name") + '</option>';
	});
	var str = '<p class="newp"><label class="p">参数</label>' +
		'<select name="Name" id="Name' + n + '" class="selp">' +
		'<option value="">请选择参数</option>' + parml +
		'</select>' +
		  '<input type="button" class="padd" onclick="$($(this).parent().prop(\'outerHTML\')).insertAfter($(this).parent())" />'+
		  '<input type="button" class="pdel" onclick="$(this).parent(\'p\').remove();" />'+
		//'<input type="button" class="pdel" onclick="$(this).parent(\'p\').remove();" />' +
		'</p>';
	pdom.find('#parmlist').append(str);
}

//节点点击事件
function zTreeOnClick(event, treeId, treeNode) {
	console.log(treeNode);
}
//删除节点
function zTreeBeforeRemove(treeId, treeNode) {
	var dataSetsMap = {};
	sDataSetsName = treeNode.name;
	parsingToXML(dataSetsMap, "del");
	initTree(parent.rdes.document.getElementById("dataSetsModel").value);
	return true;
}
//鼠标移动到节点上
function addHoverDom(treeId, treeNode) {
	if (treeNode.level != 1) {
		return false;
	}
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#" + treeNode.tId + "_editBtn").length > 0) return;
	var addStr = "<span class='button edit' id='" + treeNode.tId
		+ "_editBtn' title='修改' onfocus='this.blur();'></span>";
	sObj.after(addStr);
	var btn = $("#" + treeNode.tId + "_editBtn");
	if (btn) btn.bind("click", function() {
			sDataSetsName = treeNode.name;
			openDataSet("edit");
			return false;
		});
}
;
//鼠标移出节点
function removeHoverDom(treeId, treeNode) {
	if (treeNode.level != 1) {
		return false;
	}
	$("#" + treeNode.tId + "_editBtn").unbind().remove();
}
;


//打开编辑数据集
function openDataSet(state) {
	window.top.layer.open({
		resize : true,
		area: ['700px', '535px'],
		title : "新增数据集", //标题
		content : '<form method="get"class="dsform"id="dsform"action=""><p><label class="p">数据源名称</label><select name="DataSourceName"id="DataSourceName"title="请选择数据源"class="sel""required><option value=""></option><option value="javabean">javabean</option></select></p><p><label class="p">数据集名称</label><input name="DataSetName"id="DataSetName"title="请输入数据集名称"class="txt"required/></p><div id="ddslist"><p><label class="p">数据字典</label><input type="checkbox"name="isdd"id="isdd"value="1"/></p><p><label class="p">执行SQL</label><textarea cols="38"rows="5"name="CommandText"id="CommandText"title="请输入查询SQL"required></textarea></p><p><label class="p">检验SQL</label><input type="checkbox"name="isvalid"id="isvalid"value="1"checked="checked"/>用于修改，取消选中修改不再校验SQL准确性</p><div id="parmlist"><p><label class="p">参数列表</label><input  type="button"class="padd"/></p></div></div><div id="jblist"style="display:none"><p><label class="p">实体类</label><input name="FactoryClass"id="FactoryClass"title="请输入类工厂"class="txt"required/></p><p style="display:none"><label class="p">方法名</label><input name="StaticMethod"id="StaticMethod"title="请输入方法名"value=""class="txt"/></p></div></form>', //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
		btn : [ '保存', '取消' ],
		success : function(layero, index) {
			$.ajax({
				url : "../../getDataSourceNameAll.action",
				type : "post",
				success : function(data) {
					for (var i = 0; i < data.length; i++) {
						$(layero).find("#DataSourceName").append("<option value='" + data[i].dataSourceName + "'>" + data[i].dataSourceName + "</option>");
					}
					if(state=="edit"){
						editDataSet(layero);
					}
				}
			});
			$(layero).find("#DataSourceName").bind("change", function() {
				if ($(this).val() == 'javabean') {
					$(layero).find('#ddslist').hide();$(layero).find('#jblist').show();
				} else {
					$(layero).find('#ddslist').show();$(layero).find('#jblist').hide();
				}
			})
			$(layero).find(".padd").bind("click", function() {
				addparm($(layero));
			});
			
		},
		yes : function(index, layero) {
			saveDataSet(layero,state);
			return false;
		},
		btn2 : function(index, layero) {
			window.top.layer.close(index)
		},
		cancel : function() {}
	});
}

//保存数据集
function saveDataSet(layero,state) {
	var dataSetsMap = {};
	var flag = 1;
	var flag1 = 1;
	var dataSetsModel = parent.rdes.document.getElementById("dataSetsModel").value;
	var dataSetName = $(layero).find('#DataSetName').val();
	var dataSourceName = $(layero).find('#DataSourceName').val();
	if (dataSourceName == '') {
		window.top.layer.tips('请选择数据源', $(layero).find('#DataSourceName'), {
			tipsMore : true
		});
		return false;
	}
	if (dataSetName == '') {
		window.top.layer.tips('数据集名称不能为空', $(layero).find('#DataSetName'), {
			tipsMore : true
		});
		return false;
	}
	var SQLText = '';
	if (dataSourceName == 'javabean') {
		SQLText = $(layero).find('#FactoryClass').val() + "|" + $(layero).find('#StaticMethod').val();
	} else {
		SQLText = $(layero).find('#CommandText').val();
	}
	var dd = $(layero).find('#isdd').is(':checked');
	var isvalid = $(layero).find('#isvalid').is(':checked');
	var txtp = new Array();
	$(layero).find('#parmlist').find('.selp').each(function() {
		txtp.push(this.value);
	});
	dataSetsMap.dataSetName = dataSetName;
	dataSetsMap.dataSourceName = dataSourceName;
	dataSetsMap.CommandText = SQLText;
	dataSetsMap.dd = dd;
	dataSetsMap.Parameters = {
		txtp : txtp
	};

	if (dd && isvalid) {
		$.ajax({
			url : "../../selectDic.action",
			type : "post",
			async : false,
			data : {
				SQLText : SQLText,
				dataSourceName : dataSourceName
			},
			success : function(data) {
				if (data == 'null') {
					flag = 0;
					window.top.layer.tips('sql错误', $(layero).find('#CommandText'), {
						tipsMore : true
					});
				} else {
					data = eval("(" + data + ")");
					parent.rdes.dicData[dataSetName] = data;
				}
			}
		});
	}
	if (flag == 0) {
		return false;
	}
	parsingToXML(dataSetsMap, state);
	var dsm = parent.rdes.document.getElementById("dataSetsModel").value;
	if (isvalid) {
		$.ajax({
			url : "../../parFields.action",
			type : "post",
			async : false,
			data : {
				dataSetsModel : dsm,
				dataSetName : dataSetName
			},
			success : function(data) {
				if (data == "null" || data == "" || data.length < 3) {
					window.top.layer.tips('sql错误', $(layero).find('#CommandText'), {
						tipsMore : true
					});
					parent.rdes.document.getElementById("dataSetsModel").value = dataSetsModel;
					flag1 = 0;
				} else {
					window.top.layer.msg('校验通过，保存成功', {
						time : 2000
					});
					parent.rdes.document.getElementById("dataSetsModel").value = eval("(" + data + ")");
				}
			},
			error : function() {
				window.top.layer.tips('sql错误', $(layero).find('#CommandText'), {
					tipsMore : true
				});
				parent.rdes.document.getElementById("dataSetsModel").value = dataSetsModel;
				flag1 = 0;
			},
			beforeSend : function() {
				window.top.layer.load(1, {
					  shade: [0.1,'#fff'] //0.1透明度的白色背景
					});
//				window.top.layer.msg('SQL校验中……', {
//					icon : 16
//				});
			},
			complete : function() {
				window.top.layer.closeAll('loading');
			}
		});
	}
	if (flag1 == 0) {
		return false;
	}
	initTree(parent.rdes.document.getElementById("dataSetsModel").value);
	if (dd)
		if (flag = 1) {
			parent.showdialog('字段选择', 'Data_Dic_Selectfield.jsp?dataSetName=' + dataSetName);
	}
	$('.newp').remove();
}

//修改数据集
function editDataSet(layero) {
	var dsname = dataMap[sDataSetsName].dataSourceName;
	//$('#DataSourceName').empty();
	if (sDataSetsName != "" && sDataSetsName != null) {
		$(layero).find('#DataSetName').attr('readonly', true);
		$(layero).find('#DataSetName').val(dataMap[sDataSetsName].dataSetName);
		$(layero).find('#DataSourceName').val(dsname);
		if (dataMap[sDataSetsName].dd == "true") {
			$(layero).find('#isdd').prop("checked", true);
		} else {
			$(layero).find('#isdd').prop("checked", false);
		}
		if(dsname=='javabean'){
			$(layero).find('#ddslist').hide();$(layero).find('#jblist').show();
		}else{
			$(layero).find('#ddslist').show();$(layero).find('#jblist').hide();
		}
		var cmdtest = dataMap[sDataSetsName].CommandText;
		if (dsname == 'javabean') {
			if (cmdtest.indexOf('|') != -1) {
				$(layero).find('#FactoryClass').val(cmdtest.split('|')[0]);
				$(layero).find('#StaticMethod').val(cmdtest.split('|')[1]);
			}
		} else {
			$(layero).find('#CommandText').val(cmdtest.replace(/&quot;/g, "\"").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&amp;/g, "&"));
			var Parameter = dataMap[sDataSetsName].Parameter;
			for (var k in Parameter) {
				addparm($(layero),k, Parameter[k]);
			}
		}
	}
}