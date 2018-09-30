<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.io.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<base href="<%=basePath%>">
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<script type="text/javascript" src="<%=basePath%>report/lib/jquery.1.7.2min.js">
</script>
		<style type="text/css">
a {
	text-decoration: none
}
label{display:block;}
html,body {
	margin: 0px;
	height: 100%;
}

#page1 {
	height: 100%;
}

.pageTop {
	position:fixed;
	height: 15px;
	width: 100%;
	background-color: #ddd;
}

.pageBottom {
	position: absolute;
	bottom:0;
	height: 15px;
	width: 100%;
	background-color: #ddd;
}

.normalCss { /*background-image: url(report/images/sort.gif);*/
	background-repeat: no-repeat;
	background-position: right center;
}

.sortDescCss {
	background-image: url(report/images/desc.gif);
	background-repeat: no-repeat;
	background-position: right center;
	padding-right:10px;
}

.sortAscCss {
	background-image: url(report/images/asc.gif);
	background-repeat: no-repeat;
	background-position: right center;
	padding-right:10px;
}

.fixedheadwrap {
	
}

.fixheadCss {
	background-color: #FFF;
	border: 1px solid;
	border-color: #000;
}
</style>
	</head>

	<body text="#000000" link="#000000" alink="#000000" vlink="#000000">
		<!--<div style="height: 15px;width: 95%;background-color: #ddd;position: absolute;bottom: 0px;"></div>
		--><div class="pageTop"></div>
		<div id="page1">
			<table height="100%" width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="50%" style="background-color: #ddd">
						&nbsp;
					</td>
					<td id="str">
					</td> 
					<td width="50%" style="background-color: #ddd">
						&nbsp;
					</td>
				</tr>
			</table>
		</div>
		
		<script type="text/javascript">
//数据格式错误错误提示
/* var table = document.getElementById("str").getElementsByTagName("table")[0];
 var trs = table.children[0].children;
 for ( var i = 0; i < trs.length; i++) {
 if (trs[i].attributes.length == 1) {
 for ( var j = 0; j < trs[i].children.length; j++) {
 if (trs[i].children[j].getElementsByTagName("p").length > 0
 && trs[i].children[j].children[0].children[0].innerHTML == "不支持该数据格式") {
 trs[i].children[j].children[0].children[0].style.color = "red";
 }
 }
 }
 } */
//表头排序
var sortlist = [ {
	"sortName" : "租金余额",//"客户名称"
	"sortType" : "desc",//"desc"
	"isSorted" : false
//是否被排序
		}, {
			"sortName" : "",
			"sortType" : "",
			"isSorted" : false
		} ];
var lastSorted = "";
$(function() {
	//点击查询往里边加内容
	var $table = $(parent.document.getElementById("pub_print_str").value);
	$('#str').append($table);
	//表头添加事件
	/* $("[name='biaotou']").on("click", function() {
		if ($(this).attr('class') == "normalCss") {
			defaultCss();
			$(this).attr("class", "sortDescCss");
			//调用倒序
			sortlist[0].sortName = $(this).text();
			sortlist[0].sortType = "desc";
			if ($(this).text() != lastSorted) {
				sortlist[0].isSorted = false;
			}
			sortTr(sortlist);
			lastSorted = $(this).text();
		} else if ($(this).attr('class') == "sortDescCss") {
			defaultCss();
			$(this).attr("class", "sortAscCss");
			//调用正序
			sortlist[0].sortName = $(this).text();
			sortlist[0].sortType = "asc";
			sortTr(sortlist);
		} else {
			defaultCss();
			$(this).attr("class", "sortDescCss");
			sortlist[0].sortName = $(this).text();
			sortlist[0].sortType = "desc";
			sortTr(sortlist);
		}
	}); */
	//表头固定上方
	//fixThead();
})
//排序主方法
function sortTr(sortlist) {
	//获取全部行
	var contentTr = $(document.getElementById("str").getElementsByTagName("table")[0].children[0].children);
	var contentHead = $("[name='biaotou']")[0].parentNode.parentNode;
	var rowStr = new Array();//找到有内容的行 列成数组
	//console.info(contentTr.length);
	for ( var i = (contentHead.rowIndex + 1); i < (contentTr.length - 1); i++) {
		//TODO 这里可以判断样式是否乱了 提示用户修改列长度
		rowStr.push(i);
	}
	var column = 0;//要排第几列
	var headtds = contentHead.getElementsByTagName("td");
	for ( var i = 0; i < headtds.length; i++) {//找到点击的是第几列
		if (!headtds[i].getElementsByTagName("div")[0] == "") {
			if (headtds[i].getElementsByTagName("div")[0].childNodes[0].nodeValue == sortlist[0].sortName) {
				column = i;
				break;
			}
		}
	}
	//循环多列排序 暂时是单列的
	for ( var i = 0; i < sortlist.length - 1; i++) {
		if (!sortlist[i].isSorted) {
			if (sortlist[i].sortType == "desc") { //降序
				for ( var i1 = 0; i1 < contentTr.length; i1++) {
					for ( var j = i1; j < contentTr.length; j++) {
						//判断当前行的当前列是否为空
						if (convert($($(contentTr[rowStr[j]]).children()[column]).find("span").text()) == "") {
							changeBlackTr(contentTr, rowStr[j], rowStr);
							//空白行移动到最后也要实时刷新contentTr否则会少比较一行 这个bug改了一天。。。
							contentTr = $(document.getElementById("str").getElementsByTagName("table")[0].children[0].children);
						}
						if (compareTd(contentTr[rowStr[i1]], contentTr[rowStr[j]], column) == -1) {
							changeTr(contentTr, rowStr[i1], rowStr[j]);
							contentTr = $(document.getElementById("str").getElementsByTagName("table")[0].children[0].children);
						}
					}
				}
				sortlist[i].isSorted = true;
			}
		} else if (sortlist[i].isSorted && sortlist[i].sortType == "asc") {
			contentTr = $(document.getElementById("str").getElementsByTagName("table")[0].children[0].children);
			reverseAllTr(contentTr, rowStr);
		} else {
			contentTr = $(document.getElementById("str").getElementsByTagName("table")[0].children[0].children);
			reverseAllTr(contentTr, rowStr);
		}
	}
}
//交换trIndex2和trIndex1的TR
function changeTr(allTrs, trIndex1, trIndex2) {
	if (trIndex1 < trIndex2) {
		$(allTrs[trIndex2]).insertBefore($(allTrs[trIndex1]));
	}
}
//翻转所有有内容的tr
function reverseAllTr(allTrs, trArray) {
	//注意这里不能往19行之前插第19行 会丢行
	for ( var i = 0; i < trArray.length - 1; i++) {
		$(allTrs[trArray[i]]).insertAfter($(allTrs[trArray[trArray.length - 1]]));
	}
}
//把含空字符串的行移动到最后
function changeBlackTr(allTrs, trIndex, allRowStr) {
	//注意这里不能往19行后边插第19行 也会丢行
	if (trIndex < allRowStr[allRowStr.length - 1]) {
		$(allTrs[trIndex]).insertAfter($(allTrs[allRowStr[allRowStr.length - 1]]));
	}
}
//比较两行中第col列的值 若大于返回1 小于返回-1 等于返回0
function compareTd(trone, trtwo, col) {
	var troneValue = convert($($(trone).children()[col]).find("span").text());
	var trtwoValue = convert($($(trtwo).children()[col]).find("span").text());
	if ($.isNumeric(troneValue) && $.isNumeric(trtwoValue)) {
		if (troneValue == trtwoValue) {
			return 0;
		}
		if (troneValue > trtwoValue) {
			return 1;
		}
		if (troneValue < trtwoValue) {
			return -1;
		}
	}
	//使用本地比较方法 能处理大多数汉字、字符串的排序 数字不适用
	return troneValue.toString().localeCompare(trtwoValue);
}
//转化要比较的值 去左右空 解析数字 
function convert(value) {
	value = $.trim(value);
	if ($.isNumeric(value)) {
		return parseFloat(value);
	}
	return value.toString();
}

function defaultCss(){
	for(var i=0;i<$("[name='biaotou']").length;i++){
		$("[name='biaotou']")[i].className= "normalCss";
	}
}
/*
固定表头主要思想：
1>将原有的TABLE中的THEAD元素复制一份放在一个新的DIV(fixedheadwrap)中
2>设置这个fixedheadwrap为绝对位于原来的TABLE的位置
*/
function fixThead(){
	var $contentHead = $($("[name='biaotou']")[0].parentNode);
	var $this=$($contentHead.parent())
	var $thisParentDiv = $($contentHead.parent().parent());//这是table外边套的td
	var p = $thisParentDiv.position();
	//TODO  这里的top 47有待确定是否可以写死
	var fixedDiv = $("<div id='fixedheadwrap' class='fixedheadwrap' style='clear:both;overflow:hiden;z-index:2;position:fixed;border:1px;margin-top:-117px;' ></div>").insertBefore($thisParentDiv).css({ "width": $thisParentDiv[0].clientWidth+10, "left": parseInt(p.left)-1, "top": p.top,"height":$contentHead.css("height") });
	//循环往fixedDiv添加子div
	for(var i=0;i<$thisParentDiv.find("td").length;i++){
		var eachDiv=$("<div class='a'></div>").append($($thisParentDiv.find("td")[i]).clone()[0].innerHTML.replace(/name="biaotou"/g,"").replace(/class="normalCss"/g,""));
		eachDiv.css("width",(parseInt($($($thisParentDiv.find("td")[i])[0]).css("width"))+1)+"px");
		eachDiv.css("height",$($($thisParentDiv.find("td")[i])[0]).css("height"));
		eachDiv.css({"float":"left","text-align":"center"});
		eachDiv.find("p").addClass("fixheadCss");
		eachDiv.appendTo(fixedDiv);
	}
	//先隐藏div
	$("#fixedheadwrap").hide();
	//给滚动添加事件
	//$(window).scroll(function(){
	//这是表头距离页面顶部的高度120.5
		//var top=$contentHead.position().top+parseInt($contentHead.css("height"))/2;
		//if($(window).scrollTop()>top){
		 //   $("#fixedheadwrap").show();
		//}else{
		//    $("#fixedheadwrap").hide();
		//}
	//});
}

</script>
	</body>
</html>
