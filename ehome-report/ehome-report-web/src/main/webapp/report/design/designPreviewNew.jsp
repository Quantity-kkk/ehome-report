<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Object obj = report.java.jrreport.util.JRUtilNew.baseMap.get("iscellauto");
	String iscellauto = "0";
	if (obj != null) {
		iscellauto = obj.toString();
	}
	String iscellauto_parm = request.getParameter("iscellauto");//传参iscellauto：是否启用单元格自动扩展(1:启用 0:禁用）
	if (iscellauto_parm != null) {
		iscellauto = iscellauto_parm;
	}
	if (!iscellauto.equals("0") && !iscellauto.equals("1")) {
		iscellauto = "0";
	}
	Object objh = report.java.jrreport.util.JRUtilNew.baseMap.get("isfixheader");
	String isfixheader = "0";
	if (objh != null) {
		isfixheader = objh.toString();
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>报表详情预览页面</title>
<link type="text/css" rel="stylesheet" href="../lib/reportshow.css" media="print">
<style type="text/css">
html, body {
	height: 100%;
}

.fy {
	text-align: center;
	font-size: 12px;
}

.page1 {
	width: 180mm;
	height: 267mm;
	padding: 10mm;
	/* background: #ffffff; */
	margin: 0 auto;
	/*overflow:auto;*/
}


.page2 {
	width: 267mm;
	height: 180mm;
	padding: 10mm;
	/* background: #ffffff; */
	margin: 0 auto;
	/*overflow:auto;*/
}

.page3 {
	height: 100%;
	/* background: #ffffff; */
	margin: 0 auto;
	/*overflow:auto;*/
}

body {
	background: #fff;
	/* background-image:url('images/tablebg.png'); */
	background-size: 100%;
	background-repeat: no-repeat;
	height: 100%;
	margin: 0;
	padding: 0;
	background-position: bottom;
}

table {
	border-collapse: collapse;
	background: #ffffff;
	margin: auto;
}

.normal {
	padding: 0px;
	word-break: keep-all;
}

<%if (iscellauto.equals("1")) {%> .
	normal td{padding: 0 3px;
}

<%} else {%> .
	normal td{text-indent: 2px;
}

<%}%>
.dynamic .rtitle td label {
	vertical-align: middle;
	/* mso-pattern: auto; */
	margin: auto;
}

.dynamic .rhead td label {
	vertical-align: middle;
}

.dynamic tbody td label {
/* 	color: rgb(0, 0, 0);
	text-decoration: none;
	text-line-through: none; */
}

.dynamic label {
	display: block;
	overflow: hidden;
	margin: 0;
	padding: 0;
	padding-left: 2px;
	padding-right: 2px;
	text-overflow: ellipsis; /* for IE */
	-moz-text-overflow: ellipsis; /* for Firefox,mozilla */
}

tfoot td {
	vertical-align: middle;
	overflow-y: hidden;
}

.confirmbtn {
	margin: 0 5px;
	border: 1px solid #999;
	background: rgba(0, 0, 0, 0) url('../images/domeBg1.png') repeat-x
		scroll 0 0;
}

table tbody td.link, table tbody td.link label {
	cursor: pointer !important;
	color: blue !important;
}

.dynamic tbody td div {
	height: auto !important;
	overflow: hidden !important;
}

.fy {
	display: none
}
</style>
<script src="../lib/jquery.js"></script>
<script src="../lib/jquery-migrate-1.2.1.min.js"></script>
<script src="../lib/jquery.jqprint-0.3.js"></script>
<script src="../lib/fixheader.js"></script>
<script src="./js/fixed-head.js"></script>
<script src="./js/columnDrag.js"></script>
<link type="text/css" rel="stylesheet" href="./css/columnDrag.css">

<script src="../lib/WdatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
	var selectData = {};
	var radioData = {};
	var checkData = {};
	function frmChild(bodyhtml, fx, page) {
	if(fx==undefined){fx=3}
			$('#datalist').empty();
			if (parent.ispubu) {//瀑布流分页
				var nextHref = $("#nextpage a").attr("href");
				// 给浏览器窗口绑定 scroll 事件
				$(window).bind("scroll", function() {
					// 判断窗口的滚动条是否接近页面底部
					if ($(document).scrollTop() + $(window).height() > $(document).height() - 10) {
						if (nextHref != undefined) {
							parent.nextp();
						} else {
							$("#nextpage").remove();
						}
					}
				});
			}
		if($('#pagesize',parent.document).val()!=-2){//非瀑布流
		$('#datalist').empty();
		}
		if ($('#page' + page).length == 0) {
			$('#datalist').append('<div rel="pg" class="page3"><div id="page'+page+'"></div></div><p class="fy">--第' + page + '页--</p>');
			//$('#datalist').append('<div rel="pg" class="page3"><div id="page1"></div></div><p class="fy">--第'+page+'页--</p>');
		}
		//$('div[rel=pg]').attr('class', 'page' + fx);
		$('#page' + page).html(bodyhtml);
		//$('#page1').html(bodyhtml);
		/* $("html,body").stop(true);
		$("html,body").animate({
			scrollTop : $('#page' + page).offset().top
		}, 500); 
		var parentField = $("#fieldModel", parent.document).val();

		var reportType = $("#reporttype", parent.document).val();

		getTdAttribute(parentField, reportType);*/

		var ddc = (document.documentElement.clientHeight - 70);
		if (ddc < 200) {
			ddc = 200;
		}
		if($('#page' + page+' .dynamic').size()==1){
		<%if (!iscellauto.equals("1")) {%>
		//加title
		//if($('#page' + page+' .dynamic').children('table'))
		$('#page' + page+' .dynamic td').each(function() {
			if($(this).html().indexOf('<img') == -1){
			$(this).attr('title', $(this).text());
			$(this).text($(this).text().replace(/  /g,"　"));//双空格替换
			if ($(this).attr("style") != undefined && $(this).attr("style") != '') {
				if ($(this).attr("style").toLowerCase().indexOf('width') != -1) {
					$(this).wrapInner('<label style="width' + $(this).attr("style").toLowerCase().split("width")[1].split(";")[0] + '"></label>');
				}
			}
			}
		});
		<%} else {%>
		/* $('#page' + page+' .dynamic td').each(function() {
			//$(this).attr('title', $(this).text());
			//$(this).text($(this).text().replace(/  /g,"　"));//双空格替换
			//$(this).attr("style",$(this).attr("style")+"vnd.ms-excel.numberformat:@;");
			if ($(this).attr("style") != undefined && $(this).attr("style") != '') {
				if ($(this).attr("style").toLowerCase().indexOf('width') != -1) {
					$(this).attr('style',$(this).attr("style").toLowerCase().replace("width","w"));
				}
			}
		}); */
		<%}%>
		}else{
			/* $('#page' + page+' .dynamic td').each(function() {
				$(this).attr("style",$(this).attr("style")+"vnd.ms-excel.numberformat:@;");
			}); */
			$('.dynamic td table').css('border',0).css('width','100%');
		}
		//var tdcount=$('.dynamic tbody tr:eq(0)').children().length;
		//$('.dynamich').css('width',($('.dynamich').width()+tdcount)+'px');
		var isChrome = window.navigator.userAgent.indexOf("Chrome") !== -1;
		if (isChrome) {
			$('#page' + page+' .dynamich').css('width', $('.dynamic').width() + 'px');
		} else {
			$('#page' + page+' .dynamich').css('width', $('.dynamic').width() + 1 + 'px');
		}
		<%if (isfixheader.equals("1") && iscellauto.equals("0")) {%>
		//固定表头配置：默认宽度100%,高度450px，使用时可自行配置
		var thwidth=document.body.clientWidth-10;
		ScrollableTable.set('page'+page, thwidth, 420,true);
		<%}%>
		<%String extend = request.getParameter("extend");
		 if(extend!=null&&extend.indexOf("fixedHead")!=-1){%>
			$(".dynamic").fixedHead();
		<%} if(extend!=null&&extend.indexOf("fixedCol")!=-1){%>
			$(".dynamic").fixedCol({cols:[0,1]});
		<%} if(extend!=null&&extend.indexOf("highlight")!=-1){%>
			$(".dynamic").highlight({cols:[0],start:111,end:115,innum:[121,122]});
		<%} 
		if(extend!=null&&extend.indexOf("btn")!=-1){
		%>
		/* 	$("body").append('<input class="td_btn_submit" type="submit" value="提交1">');
			$("body").append('<style>.td_btn_submit{height: 33px; line-height: 30px; background: #00A3FF; border-radius: 2px; font-size: 14px; color: #FFF; cursor: pointer; border: none; padding: 0 30px; outline: none; bottom: 40px; right: 53px; margin-top: 5px;}</style>');
		 */<%} if(extend!=null&&extend.indexOf("tableSort")!=-1){%>
			$(".dynamic").attr("id","tableSort");
			$("#page1").append('<div id="box"></div>');
			Drag("tableSort");
			$(".dynamic").find("thead tr td").bind("click",function(){
				var thisIndex = $(this).index();
				clearTimeout(time);
			    //执行延时
			    time = setTimeout(function(){
			    	sortTable('tableSort',thisIndex);
			    },300);
			});
			changethead("tableSort");
			initcol("tableSort");
		 <%}%>
		//td单元格双击事件/*
		/* $("#page" + page+" .dynamic tbody tr td[iswrite=1], .normal thead tr td[iswrite=1]").dblclick(function() {
			$(".tempshow").html("").hide();
			var id = $(this).attr("relatedid");
			var val = $(this).html();
			$updateTd = $(this);
			var editstyle = $(this).attr("editstyle");
			var offset = $(this).offset();
			switch (editstyle) {
			case "1":
				$("#updatetext").val($(this).html()).css({
					top : offset.top,
					left : offset.left,
					height : ($(this).height() - 6) + "px",
					width : ($(this).width() - 3) + "px",
					display : "block"
				}).focus().select();

				$("body").unbind("mousedown");
				$("body").mousedown(function() {
					var $input = $("#updatetext");
					var val = $input.val();
					if ($updateTd.html() != val) {
						var type = $updateTd.attr("datatype");
						if (type == "2" || type == "3") {
							if (!isNum(val)) {
								alert("输入的数据格式不正确,请重新输入!");
								$input.focus().select();
								return false;
							}
						}
						//合法性校验
						var checkstylevals = $updateTd.attr("checkexpval");
						if (checkstylevals != "" && checkstylevals != null) {
							var checkformulas = checkstylevals.split(";");
							for (var i = 0; i < checkformulas.length - 1; i++) {
								var formula = checkformulas[i].split(":")[0];
								var temp = formula.substring(formula.indexOf("{"), formula.indexOf("}") + 1);
								if (formula.indexOf("match") != -1)
									formula = formula.replace(new RegExp(temp, 'g'), "'" + val + "'");
								else
									formula = formula.replace(new RegExp(temp, 'g'), val);
								var prompt = checkformulas[i].split(":")[1];
								if (eval(formula) == null || eval(formula) == false) {
									alert(prompt);
									return false;
								}
							}
						}
						$updateTd.parent("tr").attr("updateflag", "1"); //插入修改标志
						$updateTd.css("background-color", "#E0FFFF");
					}
					$updateTd.html(val);
					$input.val("").hide();
					$("body").unbind("mousedown");
				});
				break;
			case "2":
				$("#updatearea").val($(this).html()).css({
					top : offset.top,
					left : offset.left,
					height : ($(this).height() - 6) + "px",
					width : ($(this).width() - 3) + "px",
					display : "block"
				}).focus().select();
				$("body").unbind("mousedown");
				$("body").mousedown(function() {
					var $area = $("#updatearea");
					var val = $area.val();
					if ($updateTd.html() != val) {
						var type = $updateTd.attr("datatype");
						if (type == "2" || type == "3") {
							if (!isNum(val)) {
								alert("输入的数据格式不正确,请重新输入!");
								$area.focus().select();
								return false;
							}
						}
						$updateTd.parent().attr("updateflag", "1");
						$updateTd.css("background-color", "#E0FFFF");
					}
					$updateTd.html(val);
					$area.val("").hide();
					$("body").unbind("mousedown");
				});
				break;
			case "3":
				var isFirst = $(this).attr("isfirst");
				var selectval = "";
				if (isFirst == "1") {//表示不是第一次点击
					selectval = getselectval($(this).html(), id, isFirst);
				} else {
					selectval = getselectval($(this).html(), id, isFirst);
					$(this).attr("isfirst", "1");
				}
				$("#updateselect").html(selectData[id]);
				$("#updateselect").val(selectval).css({
					top : offset.top,
					left : offset.left,
					height : ($(this).height()) + "px",
					width : ($(this).width() + 3) + "px",
					display : "block"
				}).focus();

				if (flag[2]) {
					$("#updateselect").blur(function() {
						var val = $('#updateselect option:selected').val();
						if ($updateTd.html() != val) {
							$updateTd.parent().attr("updateflag", "1");
							$updateTd.css("background-color", "#E0FFFF");
						}
						$updateTd.html(val);
						$(this).hide();
					});
					flag[2] = false;
				}
				break;
			case "4":
				$("#radiodiv").html(radioData[id]);
				$("#radiodiv").append("<input type='button' value='确定' id='saveradio' class='confirmbtn'/>");
				$("#radiodiv").css({
					top : offset.top + 30,
					left : offset.left,
					backgroundColor : "#eeffff",
					display : "block"
				}).focus();
				var htmltext = $(this).html();
				$("input[name=updateradio]").each(function() {
					if (htmltext == $(this).attr("text") || htmltext == $(this).val())
						$(this).attr("checked", "checked");
				});

				$("#saveradio").on("click", function() {
					$updateTd.html($("input[name=updateradio]:checked").val());
					if ($updateTd.html() != val) {
						$updateTd.parent().attr("updateflag", "1");
						$updateTd.css("background-color", "#E0FFFF");
					}
					$("#radiodiv").html("").hide();
				});
				break;
			case "5":
				$("#checkdiv").html(checkData[id]);
				$("#checkdiv").append("<input type='button' value='确定' id='savecheck' class='confirmbtn'/>");
				$("#checkdiv").css({
					top : offset.top + 30,
					left : offset.left,
					backgroundColor : "#eeffff",
					display : "block"
				}).focus();
				var htmltext = $(this).html();
				$("input[name=updatecheck]").each(function() {
					if (htmltext == $(this).attr("text") || htmltext == $(this).val())
						$(this).attr("checked", "checked");
				});

				$("#savecheck").on("click", function() {
					$updateTd.html($("input[name=updatecheck]:checked").val());
					if ($updateTd.html() != val) {
						$updateTd.parent().attr("updateflag", "1");
						$updateTd.css("background-color", "#E0FFFF");
					}
					$("#checkdiv").html("").hide();
				});
				break;
			case "6":
				var time = $(this).html();
				var datetype = $(this).attr("styleval");
				var datestyle = "";
				var datatype = $updateTd.attr("datatype");
				switch (datetype) {
				case "1":
					datestyle = "yyyy,MM,dd HH:mm:ss";
					break;
				case "2":
					datestyle = "yyyy,MM,dd";
					break;
				case "3":
					datestyle = "yyyy,MM";
					break;
				case "4":
					datestyle = "yyyy";
					break;
				case "5":
					datestyle = "HH:mm:ss";
					break;
				}
				var time;
				if (datatype == "2") {
					time = new Date((parseInt($(this).html()) * 1000)).Format(datestyle);
				} else {
					time = $(this).html();
				}
				$("#inputdate").val(time).css({
					top : offset.top,
					left : offset.left,
					height : ($(this).height()) + "px",
					//width : ($(this).width() + 3) + "px",
					display : "block"
				}).focus(function() {
					WdatePicker({
						skin : 'default',
						dateFmt : datestyle,
						isShowToday : false,
						isShowClear : false
					});
				}).select();
				$("body").unbind("mousedown");
				$("body").mousedown(function() {
					var $input = $("#inputdate");
					var val = $input.val();
					if ($updateTd.html() != val) {
						var type = $updateTd.attr("datatype");
						if (type == "2" || type == "3") {
							$updateTd.html(parseInt(new Date(val).getTime()) / 1000);
						} else {
							//val = val.replace(/,/g, "-");
							$updateTd.html(val);
						}
						$updateTd.parent("tr").attr("updateflag", "1"); //插入修改标志
						$updateTd.css("background-color", "#E0FFFF");
					}
					$input.val("").hide();
					$("body").unbind("mousedown");
				});
				break;
			}
		}); */
	}
	function refresh() {
		window.location.reload();
	}
	//调用父页面的初始化方法
	var $updateTd;
	var flag = new Array("true", "true", "true");
	$(function() {
		parent.initMain();
	});
	//获取td属性
	function getTdAttribute(dataXml, reportType) {
		var xmlStrDoc = $.parseXML(dataXml);
		if (xmlStrDoc != null) {
			if (reportType == "D") {
				getDynamicTdAttr(xmlStrDoc);
			} else {
				getNormalTdAttr(xmlStrDoc);
			}
		}
	}
	Date.prototype.Format = function(fmt) { //author: meizz
		var o = {
			"M+" : this.getMonth() + 1, //月份
			"d+" : this.getDate(), //日
			"H+" : this.getHours(), //小时
			"m+" : this.getMinutes(), //分
			"s+" : this.getSeconds(), //秒
			"q+" : Math.floor((this.getMonth() + 3) / 3), //季度
			"S" : this.getMilliseconds()
		//毫秒
		};
		if (/(y+)/.test(fmt))
			fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(fmt))
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	}
	//获取动态报表属性
	function getDynamicTdAttr(xmlStrDoc) {
		for (var i = 0; i < xmlStrDoc.childNodes[0].childNodes.length; i++) {
			var childNode = xmlStrDoc.childNodes[0].childNodes[i];
			var id = childNode.getAttribute('id');
			var datasourcename = childNode.getAttribute('datasourcename');
			var tablename = childNode.getAttribute('tablename');
			var datatype = childNode.getAttribute('datatype');
			var editstyle = childNode.getAttribute('editstyle');
			var iswrite = childNode.getAttribute('iswrite');
			var styleval = childNode.getAttribute('styleval');
			var checkexpval = childNode.getAttribute('checkexpval');
			$(".dynamic tbody tr td[relatedid='" + id + "']").attr("datasourcename", datasourcename).attr("tablename", tablename).attr("datatype", datatype).attr("editstyle", editstyle).attr("iswrite", iswrite).attr("styleval", styleval).attr("checkexpval", checkexpval);
			if (editstyle == "3" || editstyle == "4" || editstyle == "5")
				buildElement(id, editstyle, styleval);//存储所有下拉框option
			for (var j = 0; j < childNode.childNodes.length; j++) {
				var ccNode = childNode.childNodes[j];
				var updateval = ccNode.getAttribute('updateval');
				if (updateval != null) {
					var index = parseInt(updateval.substring(0, 1).charCodeAt()) - 65;
					$(".dynamic tbody tr").each(function() {
						$(this).find("td:eq('" + index + "')").attr("name", ccNode.getAttribute('name')).attr("iskey", ccNode.getAttribute('iskey'));
					});
				}
			}
		}
	}

	//获取普通模板td属性
	function getNormalTdAttr(xmlStrDoc) {
		for (var i = 0; i < xmlStrDoc.childNodes[0].childNodes.length; i++) {
			var childNode = xmlStrDoc.childNodes[0].childNodes[i];
			var id = childNode.getAttribute('id');
			var datasourcename = childNode.getAttribute('datasourcename');
			var tablename = childNode.getAttribute('tablename');
			var datatype = childNode.getAttribute('datatype');
			var editstyle = childNode.getAttribute('editstyle');
			var iswrite = childNode.getAttribute('iswrite');
			var styleval = childNode.getAttribute('styleval');
			var checkexpval = childNode.getAttribute('checkexpval');
			$(".normal thead tr td[relatedid='" + id + "']").attr("datasourcename", datasourcename).attr("tablename", tablename).attr("datatype", datatype).attr("editstyle", editstyle).attr("iswrite", iswrite).attr("styleval", styleval).attr("checkexpval", checkexpval);

			if (editstyle == "3" || editstyle == "4" || editstyle == "5")
				buildElement(id, editstyle, styleval);//存储所有下拉框option
			for (var j = 0; j < childNode.childNodes.length; j++) {
				var ccNode = childNode.childNodes[j];
				var updateval = ccNode.getAttribute('updateval');
				if (updateval != null) {
					var colindex = parseInt(updateval.substring(0, 1).charCodeAt()) - 65;
					var rowindex = parseInt(updateval.substring(1)) - 1;
					$(".normal thead tr:eq('" + rowindex + "') td:eq('" + colindex + "')").attr("name", ccNode.getAttribute('name')).attr("iskey", ccNode.getAttribute('iskey'));
				}
			}
		}
	}

	//生成   
	function buildElement(id, editstyle, styleval) {
		var str = "";
		switch (editstyle) {
		case "1"://文本框
			break;
		case "2"://文本域
			break;
		case "3"://下拉列表框
			if (styleval != "") {
				var option = styleval.split(";");
				for (var j = 0; j < option.length - 1; j++) {
					var val = option[j].split(":")[0];
					var text = option[j].split(":")[1];
					str += "<option value='"+val+"'>" + text + "[" + val + "]" + "</option>";
				}
			}
			selectData[id] = str;
			break;
		case "4"://单选按钮
			if (styleval != "") {
				var option = styleval.split(";");
				for (var j = 0; j < option.length - 1; j++) {
					var val = option[j].split(":")[0];
					var text = option[j].split(":")[1];
					str += "<label style='margin-right:4px;'><input type='radio' text='"+text+"' value='"+val+"' name='updateradio'/>" + text + "[" + val + "]</label>";
				}
			}
			radioData[id] = str;
			break;
		case "5"://复选框
			if (styleval != "") {
				var option = styleval.split(";");
				for (var j = 0; j < option.length - 1; j++) {
					var val = option[j].split(":")[0];
					var text = option[j].split(":")[1];
					str += "<label style='margin-right:4px;'><input type='checkbox' text='"+text+"' value='"+val+"' name='updatecheck'/>" + text + "[" + val + "]</label>";
				}
			}
			checkData[id] = str;
			break;
		case "6"://下拉日历
			break;
		}
	}

	//获取下拉框值
	function getselectval(html, id, isFirst) {
		var selectval = "";
		var options = $("#updateselect")[0];
		for (var i = 0; i < options.length; i++) {
			if (isFirst == "1") {
				selectval = html;
			} else {
				var optionhtml = options[i].innerHTML;
				var subhtml = optionhtml.substring(optionhtml.indexOf("["), optionhtml.indexOf("]") + 1);
				optionhtml = optionhtml.replace(subhtml, "");
				if (optionhtml == html) {
					selectval = options[i].value;
					break;
				}
			}
		}
		return selectval;
	}

	//判断是否为数字
	function isNum(s) {
		if (s != null && s != "") {
			return !isNaN(s);
		}
		return false;
	}
	//打开链接
	function openlink(v) {
		if (v != '' && v.indexOf(',') != -1 && v.indexOf('url') != -1 && v.indexOf('reporttype') != -1) {
			var arr = v.split(',');
			var url = '';
			var parm = '';
			var parms = '';
			for (var i = 0; i < arr.length; i++) {
				if (arr[i].indexOf('url:') != -1) {//链接地址
					url = arr[i].split(':')[1];
				} else if (arr[i].indexOf('parms:') != -1) {//自定义参数
					parms = $('#' + arr[i].split(':')[1], parent.document).val();
				} else {//配置参数
					parm += arr[i].replace(':', '=') + '&';
				}
			}
			if (url != '' && parm != '') {
				if (parms == '') {
					window.open(url + '?' + parm.substring(0, parm.length - 1));
				} else {
					window.open(url + '?' + parm + parms);
				}
			}
		} else {
			alert('链接不合法，请重新配置模板！')
		}
	}
	function print(){
	$("#datalist").jqprint({});
	}
</script>
<%
	String skinType = request.getParameter("skinType");
%>
<link type="text/css" rel="stylesheet" href="skin/<%=skinType%>" id="skin">
</head>

<body>
	<input id='updatetext' class='updatetext' type='text' style='display:none;position:absolute;'>
	<textarea style='position:absolute;display:none;' id='updatearea' name='updatearea'></textarea>
	<select id='updateselect' class='updateInput' style='display:none;position:absolute;'></select>
	<div id='radiodiv' style='display:none;position:absolute;font-size:14px;' class='tempshow'></div>
	<div id='checkdiv' style='display:none;position:absolute;font-size:14px;' class='tempshow'></div>
	<input id="inputdate" type="text" runat="server" class="Wdate" style="display:none;position:absolute;" readonly />
	<div id="datalist" class="normal"></div>
	<div id="nextpage" style="padding-top:60px;display:none">
		<a href="link_href"></a>
	</div>
</body>
</html>