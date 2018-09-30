<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
<meta charset="UTF-8">
<title>字段配置</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../lib/ui/jquery-ui.min.css" />
<link rel="stylesheet" href="../lib/ztree/css/zTreeStyle/zTreeStyle.css"
	type="text/css">
<script type="text/javascript" src="../lib/jquery.min.js"></script>
<script src="../lib/ztree/jquery.ztree.min.js"></script>
<script src="../lib/ui/jquery-ui.min.js"></script>
<script src="../lib/vl/jquery.validate.min.js"></script>
<script src="../lib/vl/localization/messages_zh.js"></script>
<script>
	var dataSetsMap = {};
	var value = '<%=request.getParameter("n")%>';
	$(function() {
		parseDataSets($('#dataSetsModel', parent.rdes.document).val());
	});

	function submit() {
		parent.closedialog();
	}
	function parseDataSets(dataSetsStr) {
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
		$("#ds").empty();
		$("#ds").append("<option value=''></option>");
		for (var key in dataSetsMap) {
			$("#ds").append("<option value='" + key + "'>" + key + "</option>");
		}
		if (value.indexOf('＋') != -1) {
			//$('#ext').val(value.split('＋')[1]);
			value = value.split('＋')[0];
		}
		if (value.indexOf("=") == 0) {
			$('#dsvt').hide();
			$('#dsvs').show();
			var arrvalue = value.substring(1, value.length).split('.');
			changeDataSets(arrvalue[0]);
			$('#ds').val(arrvalue[0]);
			$('#dsvs').val(arrvalue[1]);
		} else {
			$('#dsvt').show().val(value);
			$('#dsvs').hide();
		}
	}
	function setdv(v) {
		var val = "";
		if (v.value != "")
			val = "=" + $('#ds').val() + "." + v.value;
		parent.rdes.Handsontable.tableStyle(v.name, val);
		$('#keyv', parent.document).val(val);
	}
	function setv(t, v) {
		var coords = parent.rdes.Handsontable.tableStyle(t, v);
		var jsonp = parent.rdes.Handsontable.XML.getCellProperties(coords);
		for (var key in jsonp) {
			parent.setProperties(key, jsonp[key]);
		}
	}
	function changeDataSets(val) {
		$("#dsvs").empty();
		$('#dsvs').append("<option value=''></option>");
		if (val == "") {
			$('#dsvt').show().val("");
			$('#dsvs').hide();
		} else {
			var arr = dataSetsMap[val].split(',');
			for (var i = 0; i < arr.length; i++) {
				$('#dsvs').append("<option value='" + arr[i] + "'>" + arr[i] + "</option>");
			}
			$('#dsvt').hide();
			$('#dsvs').show();
		}
	}
	//附加值改变时
	function setdsv() {
		var val = $("#ds").val();
		if (val == "") {
			setv("text", $("#dsvt").val());
			$('#dsvt', parent.rprop.document).val($("#dsvt").val());
		} else {
			setdv(document.getElementById("dsvs"));
			$('#dsvs', parent.rprop.document).val($("#dsvs".val()));
		}
	}
</script>
<style type="text/css">
body {
	font-size: 75%;
	font-weight: normal;
	margin: 0;
}

input {
	border: 1px solid #ccc;
	height: 26px;
	margin-right: 10px;
	width: 200px;
}

textarea {
	border: 1px solid #ccc;
}

select {
	height: 31px;
	border: 1px solid #ccc;
	width: 204px;
}

.submit {
	width: auto;
	padding: 5px 15px;
	background: none repeat scroll 0 0 #277b9d;
	border: 1px solid #ccc;
	color: #fff;
	height: 30px;
}
</style>
</head>
<body>
	<table width="60%" cellspacing="5" align="center">
		<tr>
			<td>数据集</td>
			<td><label for="ds"></label> <select name="ds" id="ds"
				onchange="changeDataSets(this.value);parent.rprop.changeDataSets(this.value);$('#ds',parent.rprop.document).val(this.value);">
					<option value=""></option>
			</select></td>

		</tr>
		<tr>
			<td>文本/字段</td>
			<td valign="top"><input name="text" id="dsvt"
				style="width:200px" value=""
				onblur="setv(this.name,this.value);$('#dsvt',parent.rprop.document).val(this.value);">
				<select name="text" id="dsvs" style="display:none;"
				onchange="setdv(this);$('#dsvs',parent.rprop.document).val(this.value);">
			</select><br />
			<br /> 总记录数：{TotalRecord}<br />自动序号请输入：ROWNUM<br />并在sql语句中添加 ''
				as ROWNUM<br />ORACLE数据库请使用自带的ROWNUM列</td>
		</tr>
		<tr>
			<td></td>
			<td valign="top"><input type="button" name="sub" id="sub"
				class="submit" value="确认" onclick="submit()" /> <br />
			<br /></td>
		</tr>
	</table>
</body>
</html>