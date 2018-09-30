﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="UTF-8">
<title>数据源</title>
<script src="../lib/jquery.min.js"></script>
<script src="../lib/vl/jquery.validate.min.js"></script>
<script src="../lib/vl/localization/messages_zh.js"></script>
<script type="text/javascript" src="../lib/layer/layer.js"></script>
<script>
    var dsname='<%=request.getParameter("dsname")%>';
	var state = "add";
	$(function() {
		if (dsname != '') {
			state = "update";
			$.ajax({
				url : "../../selectDataSourceAll.action",
				type : "post",
				success : function(data) {
					if (data.flag == 'error') {
						top.location.href = '../../login.action';
					} else {
						$(data).each(function(index, item) {
							if (item.dataSourceName == dsname) {
								$('#dsform')[0].reset();
								$('#dataSourceName').val(dsname);
								$('#dataSourceName').attr('readonly', 'readonly');
								$('#dataSourceType').val(item.type);
								sw('dataSourceDirver', item.type);
								$('#dataSourceDirver').val(item.driver);
								swu('dataSourceUrl', item.driver);
								$('#dataSourceUrl').val(item.dataBaseUrl);
								$('#dataSourceUsername').val(item.userName);
								$('#dataSourcePassword').val(item.password);
							}
						});
					}
				}
			});

		}
		$('#dsform').validate({
			submitHandler : function() {
				var url;
				if (state == "add") {
					url = '../../addDataSource.action';
				} else {
					url = '../../updateDataSource.action';
				}
				$.ajax({
					type : 'post',
					data : $('#dsform').serialize(),
					url : url,
					success : function(msg) {
						layer.alert("保存成功！", function() {
							parent.location.reload();
							parent.closedialog();
						});
					}
				});
				return false;
			}
		});
	});
	function testconn() {
		if($('#dsform').valid()){
			$.ajax({
				type : 'post',
				data : $('#dsform').serialize(),
				url : '../../testDataSource.action',
				success : function(msg) {
					if (msg.flag == "1") {
						layer.alert("连接测试成功！");
					} else {
						layer.alert("连接测试失败！");
					}
				},
				beforeSend:function(){
					layer.load();
				},
				complete:function(){
					layer.closeAll('loading');
				}
			});
		}
	}
	function sw(id, v) {
		$('#' + id).empty();
		$('#' + id).append('<option value=""> </option>');
		switch (v) {
		case "oracle":
			$('#' + id).append('<option value="oracle.jdbc.driver.OracleDriver">oracle.jdbc.driver.OracleDriver</option>');
			break;
		case "db2":
			$('#' + id).append('<option value="COM.ibm.db2.jdbc.app.DB2Driver">COM.ibm.db2.jdbc.app.DB2Driver</option><option value="COM.ibm.db2.jdbc.net.DB2Driver">COM.ibm.db2.jdbc.net.DB2Driver</option><option value="com.ibm.db2.jcc.DB2Driver">com.ibm.db2.jcc.DB2Driver</option>');
			break;
		case "mysql":
			$('#' + id).append('<option value="com.mysql.jdbc.Driver">com.mysql.jdbc.Driver</option>');
			break;
		case "sqlserver":
			$('#' + id).append('<option value="com.newatlanta.jturbo.driver.Driver">com.newatlanta.jturbo.driver.Driver</option><option value="com.microsoft.sqlserver.jdbc.SQLServerDriver">com.microsoft.sqlserver.jdbc.SQLServerDriver</option>');
			break;
		}
	}
	function swu(id, v) {
		$('#' + id).val();
		switch (v) {
		case "oracle.jdbc.driver.OracleDriver":
			$('#' + id).val('jdbc:oracle:thin:@192.168.0.1:1521:[数据库名]');
			break;
		case "COM.ibm.db2.jdbc.app.DB2Driver":
			$('#' + id).val('jdbc:db2://192.168.0.1:6789/[sample]');
			break;
		case "COM.ibm.db2.jdbc.net.DB2Driver":
			$('#' + id).val('jdbc:db2://192.168.0.1:6789/[sample]');
			break;
		case "com.ibm.db2.jcc.DB2Driver":
			$('#' + id).val('jdbc:db2://192.168.0.1:6789/[sample]');
			break;
		case "com.mysql.jdbc.Driver":
			$('#' + id).val('jdbc:mysql://192.168.0.1:3306/[数据库名]');
			break;
		case "com.newatlanta.jturbo.driver.Driver":
			$('#' + id).val('jdbc:JTurbo://192.168.0.1/[DB Name]/charset=GBK');
			break;
		case "com.microsoft.sqlserver.jdbc.SQLServerDriver":
			$('#' + id).val('jdbc:sqlserver://192.168.0.1:1433;DatabaseName=[DB Name]');
			break;
		}
	}
	function changeF(v) {
        $('#dataSourceDirver').val(v);
		swu('dataSourceUrl',v);
    }
</script>
<style type="text/css">
body {
	margin: 0;
	padding: 2px;
}

.dsform {
	margin: 0;
	border: 0;
	padding: 0;
	line-height: 1.8;
	font-size: 13px;
}

.dsform p {
	margin: 10px 0 0 0;
	padding: 0;
	border: 0;
}

.dsform legend {
	font-weight: bold;
	font-size: 14px;
}

.dsform label.p {
	display: inline-block;
	width: 160px;
	padding: 0 10px;
	text-align: right;
}

.dsform label.error {
	display: inline-block;
	color: red;
	padding-left: 10px;
}

.dsform .txt {
	width: 200px;
}

.dsform .sel {
	width: 204px;
}

.dsform input {
	border: 1px solid #ccc;
	height: 26px;
	width: 200px;
	vertical-align: middle;
}

.dsform textarea {
	border: 1px solid #ccc;
	vertical-align: middle;
}

.dsform select {
	height: 31px;
	border: 1px solid #ccc;
	width: 204px;
	vertical-align: middle;
}

.dsform .submit {
	margin-top: 10px;
	margin-left: 120px;
	float: left;
	width: auto;
	padding: 0 15px;
	background: none repeat scroll 0 0 #02baff;
	border: 1px solid #ccc;
	color: #fff;
	height: 30px;
}
</style>
</head>
<body>
	<form method="post" class="dsform" id="dsform" action="">
		<p>
			<label class="p">数据源名称</label> <input name="dataSourceName" id="dataSourceName" title="请输入数据源名称" class="txt" required />
		</p>
		<p>
			<label class="p">数据库类型</label> <select name="dataSourceType" id="dataSourceType" class="sel" title="请选择数据库类型" required onchange="sw('sel',this.value)">
				<option value="">请选择</option>
				<option value="oracle">oracle</option>
				<option value="db2">db2</option>
				<option value="mysql">mysql</option>
				<option value="sqlserver">sqlserver</option>
			</select>
		</p>
		<p style="overflow:hidden">
			<label class="p">驱动类型</label> <div style="position: relative; width: 240px; height: 20px; top:-20px;left:83px;">
        <select id="sel"
            style="float: right; height: 24px; width: 204px; z-index: 88; position: absolute; left: 100px; top: 0px;"
            onchange="changeF(this.value);">
        </select> <input type="text" id="dataSourceDirver" name="dataSourceDirver" value=""
            style="position: absolute; width: 185px; height: 18px; left: 101px; top: 1px; z-index: 99; border: 1px #FFF solid" required />
    </div>
		</p>
		<p>
			<label class="p">数据源地址</label> <input name="dataSourceUrl" id="dataSourceUrl" title="请输入数据源地址" class="txt" required />
		</p>
		<p>
			<label class="p">用户名</label> <input name="dataSourceUsername" id="dataSourceUsername" title="请输入数据库用户名" class="txt" required />
		</p>
		<p>
			<label class="p">密码</label> <input name="dataSourcePassword" id="dataSourcePassword" title="请输入数据库密码" class="txt" />
		</p>
		<p>
			<input class="submit" type="button" onclick="testconn()" value="测试连接" /> <input class="submit" type="submit" value="保存" />
		</p>
	</form>

</body>
</html>