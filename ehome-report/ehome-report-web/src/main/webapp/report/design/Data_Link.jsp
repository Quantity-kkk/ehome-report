﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
<meta charset="UTF-8">
<title>数据集配置</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../lib/ui/jquery-ui.min.css" />
<link rel="stylesheet" href="../lib/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../lib/jquery.min.js"></script>
<script src="../lib/ztree/jquery.ztree.min.js"></script>
<script src="../lib/ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="../lib/layer/layer.js"></script>
<script src="../lib/vl/jquery.validate.min.js"></script>
<script src="../lib/vl/localization/messages_zh.js"></script>
<script>
	var temstr = '';
	var filestr = '';
	var parmstr = '';
	var sDataSetsName = null;
	var uuid = "";
	var parmData = {};
	var dataSetsMap = {};
	var value = $('#keyv', window.parent.document)[0].value;
	$(document).ready(function() {
		$("#temlist").empty();
		$("#temlist").append("<option value=''>请选择模板</option>");

		var link = parent.rdes.Handsontable.expression.getHyperlink();
		if (link) {
			$('#myconfig').val(link);
			var arrs = link.split(',');
			for (var i = 0; i < arrs.length; i++) {
				var ar = arrs[i].split(":");
				if (ar[0] == "uuid") {
					uuid = ar[1];
				} else {
					parmData[ar[0]] = ar[1];
				}
			}
		}
		if (uuid != '') {
			$.ajax({
				url : "../../selectAllParmsByUUID.action",
				type : "post",
				data : {
					uuid : uuid
				},
				async : false,
				success : function(data) {
					parmstr = '';
					for (var key in data) {
						parmstr += "<option value=" + key + ">" + key + "</option>";
					}
				}
			});
		}
		$.ajax({
			url : "../../selectAllReportFile.action",
			type : "post",
			success : function(data) {
				for (var key in data) {
					var str = "";
					if (uuid == data[key].uuid) {
						str = " selected = 'selected'";
					}
					$("#temlist").append("<option value='" + data[key].uuid + ",reporttype:" + getreportstyle(data[key].reportStyle) + "' " + str + ">" + data[key].name + "</option>");
				}
				$('#temlist').selectmenu('refresh', true);
				$('.padd').show();
			}
		});

		$('#temlist').selectmenu({
			change : function() {
				sDataSetsName = this.value.split(',')[0];
				$("#plist").empty();
				$.ajax({
					url : "../../selectAllParmsByUUID.action",
					type : "post",
					data : {
						uuid : sDataSetsName
					},
					success : function(data) {
						parmstr = '';
						for (var key in data) {
							parmstr += "<option value=" + key + ">" + key + "</option>";
						}
					}
				});
			}
		});
		test($('#dataSetsModel', parent.rdes.document).val());
		var treeObj = parent.parent.rtree.$.fn.zTree.getZTreeObj("treed");
		var nodes = treeObj.getNodes()[0].children;
		var arr = new Array();
		for (var i = 0; i < nodes.length; i++) {
			if (nodes[i].id == 20 && nodes[i].isParent) {
				getChildren(arr, nodes[i]);
			}
		}
		//for(var z=0;z<arr.length;z++){
		//	filestr +="<option value="+z+">"+arr[z]+"</option>";
		//}


		for (var k in parmData) {
			if (k != 'url' && k != 'reporttype' && k != 'parms') {
				addp(parmData[k], k);
			}
		}
	});
	function getreportstyle(v) {
		var str = '';
		switch (v) {
		case '普通报表':
			str = 'N';
			break;
		case '图表报表':
			str = 'C';
			break;
		default:
			str = 'D';
			break;
		}
		return str;
	}
	function test(dataSetsStr) {
		var domParser = new DOMParser();
		xmlStrDoc = domParser.parseFromString(dataSetsStr, "text/xml");
		if (xmlStrDoc.childNodes[0].childNodes.length > 0) {
			var jsoStr = "{";
			for (var r = 0; r < xmlStrDoc.childNodes[0].childNodes.length; r++) {
				var node = xmlStrDoc.childNodes[0].childNodes[r];
				var name = node.getAttribute("name");
				jsoStr += name + ":";
				var fStr = "'";
				for (var c = 0; c < node.getElementsByTagName("fields")[0].childNodes.length; c++) {
					var value = node.getElementsByTagName("fields")[0].childNodes[c].getAttribute("name");
					fStr += value + ",";
				}
				fStr = fStr.substring(0, fStr.length - 1);
				jsoStr += fStr + "',";
			}
			jsoStr = jsoStr.substring(0, jsoStr.length - 1);
			jsoStr += "}";
			dataSetsMap = eval("(" + jsoStr + ")");
			initDataSets();
		}
	}
	function initDataSets() {
		if (value.indexOf("=") == 0) {
			var arrvalue = value.substring(1, value.length).split('.');
			changeDataSets(arrvalue[0]);
		}
	}
	function changeDataSets(val) {
		filestr = '';
		var arr1 = dataSetsMap[val].split(',');
		for (var i = 0; i < arr1.length; i++) {
			filestr += "<option value='(#" + "=" + val + "." + arr1[i] + "#)'" + ">" + arr1[i] + "</option>";
		}
	}
	function getChildren(arr, treeNode) {
		if (treeNode.isParent) {
			for (var obj in treeNode.children) {
				getChildren(arr, treeNode.children[obj]);
			}
		} else {
			arr.push(treeNode.name);
		}
		return arr;
	}
	function addp(f, p) {
		var n = (new Date()).getTime();
		var str = '<div class="top">' +
			'<p><select name="parmlist" class="parmlist" id="parmlist' + n + '"><option value="">请选择参数</option>' + parmstr + '</select></p><p style="font-size:16px">=</p>' +
			'<p><select name="filelist" class="filelist" id="filelist' + n + '"><option value="">请选择字段</option>' + filestr + '</select></p>' +
			'<p><input type="button" class="pdel" onclick="$(this).parent().parent(\'div\').detach();" /></p>' +
			'</div>';
		$("#plist").append(str);
		$('#filelist' + n).val(f);
		$('#parmlist' + n).val(p);
		$('#filelist' + n).selectmenu();
		$('#parmlist' + n).selectmenu();
	}
	function createlink() {
		var strUrl = "url:designPreviewIndex.jsp,uuid:" + $("#temlist").val() + ",";
		$("#plist").find('.top').each(function() {
			var colIndex = $(this).find('.filelist')[0].value;
			var parm = $(this).find('.parmlist')[0].value;
			strUrl += parm + ":" + colIndex + ",";
		});
		$('#myconfig').val(strUrl.substring(0, strUrl.length - 1));
	}
	function save() {
		//var strUrl="url:designPreviewIndex.jsp,uuid:"+$("#temlist").val()+",";
		//$("#plist").find('.top').each(function(){
		// var colIndex = $(this).find('.filelist')[0].value;
		// var parm = $(this).find('.parmlist')[0].value;
		// strUrl += parm+":"+colIndex+",";
		//});
		//strUrl = strUrl.substring(0,strUrl.length-1);
		if ($('#myconfig').val() != '' && $('#myconfig').val().indexOf('url') != -1 && $('#myconfig').val().indexOf('reporttype') != -1) {
			parent.rdes.Handsontable.expression.setHyperlink($('#myconfig').val());
			parent.closedialog();
		} else {
			layer.alert('请执行第一步生成链接！');
		}
	}
	;
</script>
<style type="text/css">
body {
	font-size: 75%;
	font-weight: normal;
	margin: 0;
}

.top {
	width: 100%;
	overflow: hidden;
	display: block;
}

.top p {
	display: block;
	float: left;
	margin-left: 10px;
}

.top #temlist {
	width: 200px;
}

.top .filelist {
	width: 160px;
}

.top .parmlist {
	width: 160px;
}

.top .padd {
	width: 50px;
	height: 25px;
	border: 0;
	cursor: pointer;
	background: transparent url(../images/plusmin.png) top center no-repeat;
}

.top .pdel {
	width: 50px;
	height: 25px;
	border: 0;
	cursor: pointer;
	background: transparent url(../images/plusmin.png) 12px -25px no-repeat;
}

.button {
	margin-left: 100px;
	padding: 5px 20px;
}
</style>
</head>
<body>
	<div class="top">
		<p>
			<select name="temlist" id="temlist"></select>
		</p>
		<p>
			<input type="button" class="padd" onclick="addp('','')" style="display:none" />
		</p>
	</div>
	<div id="plist"></div>
	<div style="padding:0 20px;">
		自定义链接：<br>
		<textarea id="myconfig" rows="5" col="20" style="width:90%;height:50px;"></textarea>
		<br>说明：<br> 1、自定义链接格式 name1:value1,name2:value2<br>2、可修改的值为url对应的值即:designPreviewIndex.jsp<br>3、可附加的参数 parms:activeParm (parms为固定自定义name，activeParm为designPreviewIndex.jsp页面隐藏域id为activeParm的值,如:userid=1&from=1&to=2)
	</div>
	<div>
		<p>
			<input id="submit" class="button" type="button" value="第一步生成自定义链接" onclick="createlink()" /> <input id="submit" class="button" type="button" value="第二步保存" onclick="save()" />
		</p>
	</div>
	</div>
</body>
</html>