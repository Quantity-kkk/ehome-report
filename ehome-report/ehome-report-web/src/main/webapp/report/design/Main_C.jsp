<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>图表报表设计器</title>
<%@ include file="../../report/common/head.jsp"%>
<link rel="stylesheet" type="text/css"
	href="../lib/spectrum/spectrum.css">
<link href="../lib/ui/jquery-ui.min.css" rel="stylesheet">
<link href="../css/head.css" rel="stylesheet" type="text/css">
<script src="../lib/jquery.min.js"></script>
<script type="text/javascript" src="../lib/layer/layer.js"></script>
<script src="../lib/ui/jquery-ui.min.js"></script>
<script src="../lib/jquery.layout.js"></script>
<script type="text/javascript" src="../lib/spectrum/spectrum.js"></script>
<script type="text/javascript" src="../lib/jquery.zclip.min.js"></script>
<style type="text/css">
body {
	margin: 0
}

div.loading {
	width: 100px;
	height: 30px;
	background: url(../images/loading.gif) 10px center no-repeat;
	padding-left: 30px;
	line-height: 30px;
	border: 1px solid #aae;
	color: #15b;
	margin: 100px auto;
}

.ui-layout-north, .ui-layout-east, .ui-layout-center {
	display: none;
}
</style>
<script type="text/javascript">   
var copyData;
window.onload = function(){
var mLayout =$('body').layout({
applyDefaultStyles:false,
north__size:148,
north__closable:false,
north__resizable:false,
north__spacing_open:0,
sliderTip:"显示/隐藏侧边栏",//在某个Pane隐藏后，当鼠标移到边框上显示的提示语。
//togglerTip_open:"关闭",//pane打开时，当鼠标移动到边框上按钮上，显示的提示语  
//togglerTip_closed:"打开",//pane关闭时，当鼠标移动到边框上按钮上，显示的提示语
east__closable:false,
east__spacing_open:5,
east__size:360,
east__childOptions: {
inset: {
top: 0,
left: 0,
right: 0,
bottom:0
},
applyDefaultStyles: false,
north__closable:false,
north__size: .4,
north__spacing_open:2
}
});
try{rdes.initDesign();$('div.loading').detach();$('.ui-layout-north,.ui-layout-east,.ui-layout-center').show();}catch(e){}
};
function GetQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
var dialog;
function showdialog(title, url) {
   dialog=layer.open({
		  title: title,
		  type: 2, 
		  maxmin: false, //开启最大化最小化按钮
		  area: ['720px', '500px'],
		  content: url
		});
}
function closedialog() {
   try {
	   layer.close(dialog);
   } catch (ee) {
   }
}
function setv(t,v){
	var coords = rdes.Handsontable.tableStyle(t,v);
	rprop.SynProperties("",coords);
}
function optJRByType(t,type){
	var dataSetsModel = rdes.document.getElementById("dataSetsModel").value;
	var tableModel = rdes.document.getElementById("tableModel").value;
	//window.open("previewDesign.action");
	$.ajax({
		url : "previewDesign.action", 
		data:{
			dataSetsModel:dataSetsModel,
			tableModel:tableModel,
			type:t,
			opt:type
		},
		type:"post",
		async:false,
		success : function(data) {
			data = eval("("+data+")");
			if(data=="error"){
				layer.alert("错误：	页面内容超出页面高度！")
			}else if(data=="preview"){
				window.open("../rbc/designPreview.jsp","preview");
			}else if(data=="previewc"){
				window.open("../rbc/printPrieviewC.jsp","preview");
			}else{
				window.location.href="../rbc/export.action?type="+type;
			}
		}
	});
	/* $.post("previewDesign.action"); */
}
$(function(){
	 $( "#tabs" ).tabs({active: 1});   //工具栏选项卡；
	
	
	$("#tb_open").button({
			text: false,
			icons: {
				primary: "toolbar_icon_open"
			}
		});
		$("#tb_modif").button({
			text: true,
			icons: {
				primary: "toolbar_icon_modif"
			}
		});
		$("#tb_save").button({
			text: true,
			icons: {
				primary: "toolbar_icon_save"
			}
		});
		$("#tb_parm").button({
			icons: {
				primary: "toolbar_icon_dataSet"
			}
		});
		$("#td_Preview").button({
			text: true,
			icons: {
				primary: "toolbar_icon_view"
			}
		});
		$("#tb_dataSet").button({
			icons: {
				primary: "toolbar_icon_dataSet2"
			}
		});
		
		$("#tb_undo").button({
			text: false,
			icons: {
				primary: "toolbar_icon_undo"
			}
		});
		$("#tb_rado").button({
			text: false,
			icons: {
				primary: "toolbar_icon_redo"
			}
		});
		$("#tb_cut").button({
			icons: {
				primary: "toolbar_icon_cut"
			}
		});
		$("#tb_copy").button({
			icons: {
				primary: "toolbar_icon_copy"
			}
		});
		$("#tb_paste").button({
			icons: {
				primary: "toolbar_icon_paste"
			}
		});
		
		$("#tb_bold").button({
			text: false,
			icons: {
				primary: "toolbar_icon_bold"
			}
		});
		$("#tb_LeftAlign").button({
			text: false,
			icons: {
				primary: "toolbar_icon_LeftAlign"
			}
		});
		$("#tb_levelAlign").button({
			text: false,
			icons: {
				primary: "toolbar_icon_levelAlign"
			}
		});
		$("#tb_RightAlign").button({
			text: false,
			icons: {
				primary: "toolbar_icon_RightAlign"
			}
		});
		$("#tb_TopAlign").button({
			text: false,
			icons: {
				primary: "toolbar_icon_TopAlign"
			}
		});
		$("#tb_velAlign").button({
			text: false,
			icons: {
				primary: "toolbar_icon_velAlign"
			}
		});
		$("#tb_BottomAlign").button({
			text: false,
			icons: {
				primary: "toolbar_icon_BottomAlign"
			}
		});
		
		$("#tb_italic").button({
			text: false,
			icons: {
				primary: "toolbar_icon_italic"
			}
		});
		$("#tb_underline").button({
			text: false,
			icons: {
				primary: "toolbar_icon_underline"
			}
		});
		$("#tb_strike").button({
			text: false,
			icons: {
				primary: "toolbar_icon_strike"
			}
		});

		$("#tb_fontfamily").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			},
			change: function(){
				setv("fontFamily",this.value);
			}
		});
		$("#tb_fontfamily").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			}
		});
		$("#tb_fontsize").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			},
			change: function(){
				setv("fontSize",this.value);
			}
		});	
		$("#tb_category").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			},
			change: function(){
				setv("category",this.value);
			}
		});	
		$("#tb_orientation").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			},
			change: function(){
				//setv("orientation",this.value);
				pagechange("tb_orientation",this.value);
			}
		});	
		$("#tb_paging").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			},
			change: function(){
				//setv("paging",this.value);
				pagechange("tb_paging",this.value);
			}
		});
		$("#tb_format").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			},
			change: function(){
				setv("fotmat",this.value);
			}
		});
		$("#tb_formatsize").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			},
			change: function(){
				setv("formatsize",this.value);
			}
		});
		$("#tb_expression").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			},
			change: function(){
				setv("expression",this.value);
			}
		});
		$("#tb_dic").selectmenu({
			icons: {
				button: "toolbar_icon_triangle"
			},
			change: function(){
				setv("dic",this.value);
			}
		});
		//菜单参数、事件定义；
		var alignOption ={
				select: function(event,ui) {
					switch(ui.item.attr("id")){
						case "tb_alignLeft":
							setv('align','left');
						break;
						case "tb_alignCenter":
							setv('align','center');
						break;
						case "tb_alignRight":
							setv('align','right');
						break;
						default:
						break;
					}
				}
			};
		var valignOption = {
				select: function(event,ui) {
					switch(ui.item.attr("id")){
						case "tb_valignTop":
							setv('valign','top');
						break;
						case "tb_valignMiddle":
							setv('valign','middle');
						break;
						case "tb_valignBottom":
							setv('valign','bottom');
						break;
						default:
						break;
					}
				}	
			};
		
		var rowsOption = {
				select: function(event,ui) {
					switch(ui.item.attr("id")){
						case "tb_insRowAbove":
							rdes.Handsontable.core.row_above();
						break;
						case "tb_insRowbelow":
							rdes.Handsontable.core.row_below();
						break;
						case "tb_delRow":
							rdes.Handsontable.core.remove('remove_row');
						break;
						default:
						break;
					}
				}
			};
		var colsOption = {
				select: function(event,ui) {
					switch(ui.item.attr("id")){
						case "tb_insColLeft":
							rdes.Handsontable.core.col_left();
						break;
						case "tb_insColRight":
							rdes.Handsontable.core.col_right();
						break;
						case "tb_delCol":
							rdes.Handsontable.core.remove('remove_col');
						break;
						default:
						break;
					}
				}
			};
		var expOption = {
				select: function(event,ui) {
					switch(ui.item.attr("id")){
						case "tb_sum":
							rdes.Handsontable.expression.creExpFun("=SUM(");
						break;
						case "tb_average":
							rdes.Handsontable.expression.creExpFun("=AVERAGE(");
						break;
						case "tb_count":
							rdes.Handsontable.expression.creExpFun("=COUNT(");
						break;
						case "tb_distinctCount":
							rdes.Handsontable.expression.creExpFun("=DISCOUNT(");
							break;
						case "tb_highest":
							rdes.Handsontable.expression.creExpFun("=MAX(");
							break;
						case "tb_lowest":
							rdes.Handsontable.expression.creExpFun("=MIN(");
							break;
						case "tb_delexp":
							rdes.Handsontable.expression.clearExpFun();
							break;
						default:
						break;
					}
				}
			};
		var undoOption = {
				select: function(event,ui) {
					switch(ui.item.attr("id")){
						case "tb_insRowAbove":
						//alert("前插入行");
						break;
						case "tb_insRowbelow":
						//alert("后插入行");
						break;
						case "tb_delRow":
						//alert("删除行");
						break;
						default:
						break;
					}
				}
			};
		var frameOption={
				select: function(event,ui) {
					switch(ui.item.attr("id")){
						case "tb_borderLeft":
							setv('border','left');
							break;
						case "tb_borderRight":
							setv('border','right');
							break;
						case "tb_borderTop":
							setv('border','top');
							break;
						case "tb_borderBottom":
							setv('border','bottom');
							break;
						case "tb_borderOut":
							setv('border','outSide');
							break;
						case "tb_borderAll":
							setv('border','all');
							break;
						case "tb_borderNone":
							setv('border','no');
							break;
						default:
						break;
					}
				}
			};
		//各种菜单；
		var toolsMenus = new Array();
		toolsMenus[0] = $("#tb_align_menu").menu(alignOption);
		toolsMenus[1] = $("#tb_valign_menu").menu(valignOption);
		toolsMenus[2] = $("#tb_rows_menu").menu(rowsOption);
		toolsMenus[3] = $("#tb_cols_menu").menu(colsOption);
		toolsMenus[4] = $("#tb_exp_menu").menu(expOption);
		toolsMenus[5] = $("#tb_undo_menu").menu(undoOption);
		toolsMenus[6] = $("#tb_frame_menu").menu(frameOption);
		//隐藏下拉菜单。
		function hideMenu(){
			$.each(toolsMenus,function(){
				$(this).hide();
			});
		}
		//单元格横向对齐；
		$("#tb_align").button({
			text:false,
			icons:{
				primary: "toolbar_icon_alignLeft",
				secondary: "toolbar_icon_arrowsDown2"
			}
		}).click(function(){
			if(toolsMenus[0].is(":visible")){
				hideMenu();
			}else{
				hideMenu();
				toolsMenus[0].show().position({
					my: "left top",
					at: "left bottom",
					of: $(this)
				})
			};
			$( document ).one( "click", function() {
				toolsMenus[0].hide();
			});
			return false;
		});
		//单元格纵向对齐；
		$("#tb_valign").button({
			text:false,
			icons:{
				primary: "toolbar_icon_valignTop",
				secondary: "toolbar_icon_arrowsDown2"
			}
		}).click(function(){
			if(toolsMenus[1].is(":visible")){
				hideMenu();
			}else{
				hideMenu();
				toolsMenus[1].show().position({
					my: "left top",
					at: "left bottom",
					of: $(this)
				})
			};
			$( document ).one( "click", function() {
				toolsMenus[1].hide();
			});
			return false;
		});
		$("#tb_cut").zclip({ 
			path:"../lib/ZeroClipboard.swf", 
			copy: function(){
				$("#dp").val(rdes.priv.editProxy.triggerHandler('cut'));
		        return $("#dp").val();
		    },
		    afterCopy:function(){/* 复制成功后的操作 */
	           //alert(1111)
	        }
		}); 
		$("#tb_copy").zclip({ 
			path:"../lib/ZeroClipboard.swf", 
		    copy: function(){
		    	$("#dp").val(rdes.priv.editProxy.triggerHandler('copy'));
		        return $("#dp").val();
		    },
		    afterCopy:function(){/* 复制成功后的操作 */
		    	//alert('a');
	        }
		}); 	
		// 背景颜色
		var option ={cancelText: "取消",
        	chooseText: "确定",
			showInput: true,
			showInitial: true,
			preferredFormat: "hex",
			showPalette: true,
			palette: [
				["#000","#444","#666","#999","#ccc","#eee","#f3f3f3","#fff"],
				["#f00","#f90","#ff0","#0f0","#0ff","#00f","#90f","#f0f"],
				["#f4cccc","#fce5cd","#fff2cc","#d9ead3","#d0e0e3","#cfe2f3","#d9d2e9","#ead1dc"],
				["#ea9999","#f9cb9c","#ffe599","#b6d7a8","#a2c4c9","#9fc5e8","#b4a7d6","#d5a6bd"],
				["#e06666","#f6b26b","#ffd966","#93c47d","#76a5af","#6fa8dc","#8e7cc3","#c27ba0"],
				["#c00","#e69138","#f1c232","#6aa84f","#45818e","#3d85c6","#674ea7","#a64d79"],
				["#900","#b45f06","#bf9000","#38761d","#134f5c","#0b5394","#351c75","#741b47"],
				["#600","#783f04","#7f6000","#274e13","#0c343d","#073763","#20124d","#4c1130"]
			],
			hide:function(color){
				var elm = $(this).spectrum("container").attr("myAttr");
				switch(elm){
					case "bgColor":  
						setbgColor(color.toHexString());	
						setv('bgcolor', color.toHexString());
					break;
					case "fontColor":
						setfontColor(color.toHexString());	
						setv('fontColor', color.toHexString());
					break;
						default:
					return false;
				}
			}
		};
		
		$("#tb_bgcolor_pick").spectrum(option);
		//背景颜色按钮；
		$("#tb_bgColor").button({
			text: false,
			icons: {
				primary: "toolbar_icon_bgColor"
			}
		}).click(function(){
				setv('bgcolor', $(this).attr("data-color"));
		});
		// 背景颜色菜单
		$("#tb_bgColor_slt").button({
			text: false,
			icons: {
				primary: "toolbar_icon_arrowsDown"
			}
		}).click(function(){
				$("#tb_bgcolor_pick").spectrum("show");
				$("#tb_bgcolor_pick").spectrum("container").attr("myAttr","bgColor"); //参数“container”,是选色器的最外层容器。
				$(".sp-container").position({
					my: "left top",
					at: "left bottom",
					of: $("#tb_bgColor")
				});
			return false;
		});
		//设置背景颜色图标按钮的识别色；
		function setbgColor(color){
			if(color){
				$("#tb_bgColor > span.ui-icon").css({"border-bottom-color":color});
				$("#tb_bgColor").attr("data-color",color);
			};
			return false;
		}
		//设置字体颜色图标按钮的识别色；
		function setfontColor(color){
			if(color){
				$("#tb_fontColor > span.ui-icon").css({"border-bottom-color":color});
				$("#tb_fontColor").attr("data-color",color);
			};
			return false;
		}
		// 字体颜色
		$("#tb_fontcolor_pick").spectrum(option);
		$("#tb_fontColor").button({
			text: false,
			icons: {
				primary: "toolbar_icon_fontColor"
			}
		}).click(function(){
			setv('fontColor', $(this).attr("data-color"));
		});
		$("#tb_fontColor_slt").button({
			text: false,
			icons: {
				primary: "toolbar_icon_arrowsDown"
			}
		}).click(function(){
			$("#tb_fontcolor_pick").spectrum("show");
			$("#tb_fontcolor_pick").spectrum("container").attr("myAttr","fontColor");
				$(".sp-container").position({
					my: "left top",
					at: "left bottom",
					of: $("#tb_fontColor")
				});
			return false;
		});
		//单元格边框线；
		$("#tb_border").button({
			text: false,
			icons: {
			primary: "toolbar_icon_borderNone"
			}
		}).click(function(){
			var elm = $(this).attr("data-id");
			$("#"+ elm).button().triggerHandler("click");
		});
		
		
		$("#tb_border_slt").button({
		text: false,
		icons: {
		primary: "toolbar_icon_arrowsDown"
		}
		}).click(function(){
			if($(".ui_buttonMenu").is(":visible")){
				$(".ui_buttonMenu").hide();
			}else{
				$(".ui_buttonMenu").show().position({
				my: "left top",
				at: "left bottom",
				of: $("#tb_border")
				});
			};
			$( document ).one( "click", function() {
				$(".ui_buttonMenu").hide();
			});
			return false;
		});
		//当选择边框线菜单里的按钮时，把选中的图标传给主按钮上。

		function borderIcon(t,icon) {
			$("#tb_border").button("option",{
				label:t,
				icons: {
					primary:icon
				}
			});
		};
		// 下边框按钮；
		$("#tb_border_bottom").button({  
		text: false,
		icons: {
		primary: "toolbar_icon_borderBottom"
		}
		}).click(function(){
			borderIcon("下边框","toolbar_icon_borderBottom");
			$("#tb_border").attr("data-id",this.id);
			setv("border","bottom");
		});
		// 上边框按钮
		$("#tb_border_top").button({
		text: false,
		icons: {
			primary: "toolbar_icon_borderTop"
		}
		}).click(function(){
			borderIcon("上边线","toolbar_icon_borderTop");
			$("#tb_border").attr("data-id",this.id);
			setv("border","top");
		});
		$("#tb_border_left").button({
			text: false,
			icons: {
			primary: "toolbar_icon_borderLeft"
			}
		}).click(function(){
			borderIcon("左边线","toolbar_icon_borderLeft");
			$("#tb_border").attr("data-id",this.id);
			setv("border","left");
		});
		$("#tb_border_right").button({
			text: false,
			icons: {
			primary: "toolbar_icon_borderRight"
			}
		}).click(function(){
			borderIcon("右边线","toolbar_icon_borderRight");
			$("#tb_border").attr("data-id",this.id);
			setv("border","right");
		});
		$("#tb_border_out").button({
		text: false,
		icons: {
		primary: "toolbar_icon_borderOut"
		}
		}).click(function(){
			borderIcon("外边线","toolbar_icon_borderOut");
			$("#tb_border").attr("data-id",this.id);
			setv("border","outSide");
		});
		$("#tb_border_in").button({
		text: false,
		icons: {
		primary: "toolbar_icon_borderIn"
		}
		}).click(function(){
			borderIcon("内边线","toolbar_icon_borderIn");
			$("#tb_border").attr("data-id",this.id);
			setv("border","inSide");
		});
		$("#tb_border_none").button({
		text: false,
		icons: {
		primary: "toolbar_icon_borderNone"
		}
		}).click(function(){
			borderIcon("无边线","toolbar_icon_borderNone");
			$("#tb_border").attr("data-id",this.id);
			setv("border","no");
		});
		$("#tb_border_all").button({
		text: false,
		icons: {
		primary: "toolbar_icon_borderAll"
		}
		}).click(function(){
			borderIcon("全边框","toolbar_icon_borderAll");
			$("#tb_border").attr("data-id",this.id);
			setv("border","all");
		});
		
//表格行列操作；		
		$("#tb_rows").button({	//行操作；
			text: false,
			icons: {
			primary: "toolbar_icon_rows",
			secondary: "toolbar_icon_arrowsDown2"
			}
		}).click(function(){
			if(toolsMenus[2].is(":visible")){
				hideMenu();
			}else{
				hideMenu();
				toolsMenus[2].show().position({
					my: "left top",
					at: "left bottom",
					of: $(this)
				})
			};
			$( document ).one( "click", function() {
				toolsMenus[2].hide();
			});
			return false;
		});
		$("#tb_cols").button({	//列操作；
			text: false,
			icons: {
			primary: "toolbar_icon_cols",
			secondary: "toolbar_icon_arrowsDown2"
			}
		}).click(function(){
			if(toolsMenus[3].is(":visible")){
				hideMenu();
			}else{
				hideMenu();
				toolsMenus[3].show().position({
					my: "left top",
					at: "left bottom",
					of: $(this)
				});
			};
			$( document ).one( "click", function() {
				toolsMenus[3].hide();
			});
			return false;
		});
		$("#tb_frame").button({	//列操作；
			text: false,
			icons: {
			primary: "toolbar_icon_frame",
			secondary: "toolbar_icon_arrowsDown2"
			}
		}).click(function(){
			if(toolsMenus[6].is(":visible")){
				hideMenu();
			}else{
				hideMenu();
				toolsMenus[6].show().position({
					my: "left top",
					at: "left bottom",
					of: $(this)
				});
			};
			$( document ).one( "click", function() {
				toolsMenus[6].hide();
			});
			return false;
		});
		//函数
		$("#tb_exp").button({	
			text: false,
			icons: {
			primary: "toolbar_icon_exp",
			secondary: "toolbar_icon_arrowsDown2"
			}
		}).click(function(){
			if(toolsMenus[4].is(":visible")){
				hideMenu();
			}else{
				hideMenu();
				toolsMenus[4].show().position({
					my: "left top",
					at: "left bottom",
					of: $(this)
				});
			};
			$( document ).one( "click", function() {
				toolsMenus[4].hide();
			});
			return false;
		});
		$("#tb_merge").button({
			icons: {
			primary: "toolbar_icon_merge"
			}
		});
		$("#tb_split").button({
			icons: {
			primary: "toolbar_icon_split"
			}
		});
		$("#tb_wrap").button({
			text:true,
			icons: {
			primary: "toolbar_icon_wrap"
			}
		});
		$("#tb_rowtype").button({
			text: false,
			icons: {
			primary: "toolbar_icon_rowtype"
			}
		});
});
function setProperties(key, value){
	switch (key){
		case "fontWeight":
			if(value==1){
				$("#tb_bold").prop("checked",true);
			}else{
				$('#tb_bold').prop("checked",false);
			}
			$("#tb_bold").button( "refresh" );
			break;
		case "fontStyle":
			if(value==1){
				$('#tb_italic').prop("checked",true);
			}else{
				$('#tb_italic').prop("checked",false);
			}
			$("#tb_italic").button( "refresh" );
			break;
		case "textDecoration":
			if(value=="underline"){
				$('#tb_underline').prop("checked",true);
				$('#tb_strike').prop("checked",false);
			}else if(value=="line-through"){
				$('#tb_underline').prop("checked",false);
				$('#tb_strike').prop("checked",true);
			}else{
				$('#tb_underline').prop("checked",false);
				$('#tb_strike').prop("checked",false);
			}
			$("#tb_underline").button( "refresh" );
			$("#tb_strike").button( "refresh" );
			break;
		case "fontFamily":
			$('#tb_fontfamily').val(value);
			$("#tb_fontfamily").selectmenu( "refresh" );
			break;
		case "fontSize":
			$('#tb_fontsize').val(value);
			$("#tb_fontsize").selectmenu( "refresh" );
			break;
		case "category":
			$('#tb_category').val(value);
			$("#tb_category").selectmenu( "refresh" );
			break;
		case "align":
			if(value=="left"){
				$('#tb_LeftAlign').prop("checked",true);
				$('#tb_levelAlign').prop("checked",false);
				$('#tb_RightAlign').prop("checked",false);
			}else if(value=="center"){
				$('#tb_LeftAlign').prop("checked",false);
				$('#tb_levelAlign').prop("checked",true);
				$('#tb_RightAlign').prop("checked",false);
			}else{
				$('#tb_LeftAlign').prop("checked",false);
				$('#tb_levelAlign').prop("checked",false);
				$('#tb_RightAlign').prop("checked",true);
			}
			$("#tb_LeftAlign").button( "refresh" );
			$("#tb_levelAlign").button( "refresh" );
			$("#tb_RightAlign").button( "refresh" );
			break;
		case "valign":
			if(value=="top"){
				$('#tb_TopAlign').prop("checked",true);
				$('#tb_velAlign').prop("checked",false);
				$('#tb_BottomAlign').prop("checked",false);
			}else if(value=="middle"){
				$('#tb_TopAlign').prop("checked",false);
				$('#tb_velAlign').prop("checked",true);
				$('#tb_BottomAlign').prop("checked",false);
			}else{
				$('#tb_TopAlign').prop("checked",false);
				$('#tb_velAlign').prop("checked",false);
				$('#tb_BottomAlign').prop("checked",true);
			}
			$("#tb_TopAlign").button( "refresh" );
			$("#tb_velAlign").button( "refresh" );
			$("#tb_BottomAlign").button( "refresh" );
			break;
		case "wordWrap":
			if(value==1){
				$('#tb_wrap').prop("checked",true);
				}else{
				$('#tb_wrap').prop("checked",false);
				}
			$("#tb_wrap").button( "refresh" );
			break;
			default:
				try{if(key=='text'){$('#keyv').val(value);}else{$('#'+key).val(value);}}catch(e){}
				break;
	}
}

// Generate four random hex digits.
function S4() {
	return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}
// Generate a pseudo-GUID by concatenating random hexadecimal.
function guid() {
	//return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
	return (S4()+S4()+S4()+S4()+S4()+S4()+S4()+S4());
}


  
    function saveData(){
	    var nm=prompt('请输入报表名称','新报表');
	    if (nm!=null && nm!='') {
	    	var memo = prompt('请输入简要的描述',nm);
	    	if (memo != null && memo != "") {
				var uid=guid();
		        $.ajax({
		            url : "../../saveDesignXML.action",
		            data:{
		                parmsModel:rdes.document.getElementById('parmsModel').value,
		                dataSetsModel:rdes.getTM(),
		                tableXMLData:rdes.getDS(),
		                uuid:uid,
		                desc:nm,
		                type:'C',
		                fileName:uid+".xml",
	    				reportVersion:"1.0",
	    				reportMemo:memo,
	    				mainUuid:uid
		            },
		            type:"post",
		            success : function(data) {
		                layer.alert(data)
		            }
		        });
		    }
        }
    }
    function updateData(){
        $.ajax({
            url : "../../updateDesignXML.action",
            data:{
                parmsModel:rdes.document.getElementById('parmsModel').value,
                tableXMLData:rdes.getTM(),
                dataSetsModel:rdes.getDS(),
                uuid:parent.GetQueryString("uuid")
            },
            type:"post",
            success : function(data) {
                layer.alert(data)
            }
        });
    }        
</script>
<%
	String uuid = request.getParameter("uuid");
 %>
<script type="text/javascript">
	$(function() {
		var uuid = '<%=uuid%>';
		if (uuid == "" || uuid == "null") {
			$("#tb_modif").hide();
		} else {
			$("#tb_modif").attr("title", "保存");
			$("#tb_save").attr("title", "另存为");
		}
	});
	//预览报表
	function windowOpen() {
		var uuid = '<%=uuid%>';
		if (uuid == "" || uuid == "null")
			uuid = parent.guid();
		window.open('../rbc/designPreviewMain.jsp?reporttype=C&uid=' + uuid, '', 'left=0,top=0,width=' + (screen.availWidth - 10) + ',height=' + (screen.availHeight - 50) + ',toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=no, status=no');
	}
</script>
</head>

<body>
	<div class="loading">模块装载中…</div>
	<div name="rtop" id="rtop" class="ui-layout-north">
		<div class="headTitle">
			<ul>
				<li class="headLogo"></li>
				<!--      <li><button type="button" id="tb_modif" onclick="updateData()">修改</button></li>
        <li><button type="button" id="tb_save" onclick="saveData()">保存</button></li> -->
				<li style="display:none"><button type="button" id="tb_undo">撤销</button></li>
				<li style="display:none"><button type="button" id="tb_rado">重做</button></li>
				<!--   <li><button type="button" onclick="windowOpen();" id="td_Preview" title="预览"></button></li> -->
			</ul>
			<h1>
				<span id="rptTitle">新建报表</span>-<em>图表报表</em>
			</h1>
		</div>
		<div class="headTool">
			<div class="headNormal">
				<div class="Shearplate1">
					<div class="NormalL">
						<button type="button" id="tb_modif"
							onclick="rdes.Handsontable.XML.updateXML();">保存</button>
						<button type="button" onclick="windowOpen()" id="td_Preview"
							title="预览">预览</button>
					</div>
					<div class="NormalR">
						<button type="button" id="tb_save"
							onclick="rdes.Handsontable.XML.saveXML();">另存为</button>
						<!-- <button type="button" id="td_picture" title="导入模板">导入</button> -->
						<!-- <span class="td_picture_text">导入</span> -->
						<!-- <button type="button" id="tb_copy" onclick="rdes.priv.editProxy.triggerHandler('copy');">复制</button> -->
					</div>
				</div>
				<div class="Shearplate2">
					<span>常用</span> <a></a>
				</div>
			</div>
			<div class="headShearplate">
				<div class="Shearplate1">
					<div class="Shearplate1L">
						<button type="button" id="tb_paste"
							onclick="rdes.priv.editProxy.triggerHandler('paste');">粘贴</button>
					</div>
					<div class="Shearplate1R">
						<button type="button" id="tb_cut">剪切</button>
						<button type="button" id="tb_copy">复制</button>
					</div>
				</div>
				<div class="Shearplate2">
					<span>剪切板</span> <a></a>
				</div>
			</div>
			<div class="headFont">
				<div style="height:72px;">
					<div class="headFont1">
						<select id="tb_fontfamily" style="width:130px;">
							<option value="PMingLiU">新细明体</option>
							<option value="MingLiU">细明体</option>
							<option value="DFKai-SB">标楷体</option>
							<option value="SimHei">黑体</option>
							<option value="SimSun">宋体</option>
							<option value="NSimSun">新宋体</option>
							<option value="FangSong">仿宋</option>
							<option value="KaiTi">楷体</option>
							<option value="Microsoft JhengHei">微软正黑体</option>
							<option value="Microsoft YaHei">微软雅黑</option>
						</select> <select id="tb_fontsize" style="width:70px;">
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="20">20</option>
							<option value="24">24</option>
							<option value="26">26</option>
							<option value="28">28</option>
							<option value="30">30</option>
						</select>
					</div>
					<div class="ui-buttonset">
						<input type="checkbox" name="checkbox" id="tb_bold" value="粗体"
							onclick="$(this).is(':checked')?setv('fontWeight','bold'):setv('fontWeight','normal')" />
						<label for="tb_bold">粗体</label> <input type="checkbox"
							name="checkbox" id="tb_italic" value="斜体"
							onclick="$(this).is(':checked')?setv('fontStyle','italic'):setv('fontStyle','normal')" />
						<label for="tb_italic">斜体</label> <input type="checkbox"
							name="checkbox" id="tb_underline" value="下划线"
							onclick="$(this).is(':checked')?setv('textDecoration','underline'):setv('textDecoration','none')" />
						<label for="tb_underline">下划线</label> <input type="checkbox"
							name="checkbox" id="tb_strike" value="删除线"
							onclick="$(this).is(':checked')?setv('textDecoration','line-through'):setv('textDecoration','none')">
						<label for="tb_strike" class="toolDeteleline">删除线</label>
						<div class="splitBtn splitBtn1">
							<button type="button" id="tb_bgColor" data-color="#ffff00">背景颜色</button>
							<button type="button" id="tb_bgColor_slt" class="smallBtn">选择更多颜色</button>
							<input type='text' id="tb_bgcolor_pick" />
						</div>
						<div class="splitBtn">
							<button type="button" id="tb_fontColor" data-color="#000000">字体颜色</button>
							<button type="button" id="tb_fontColor_slt" class="smallBtn">选择更多颜色</button>
							<input type='text' id="tb_fontcolor_pick" />
						</div>
					</div>
				</div>
				<div class="Shearplate2">
					<span>字体</span> <a></a>
				</div>
			</div>
			<div class="headAlignment">
				<div class="headAlignment1">
					<div class="ui-buttonset">
						<input type="checkbox" name="checkbox" id="tb_TopAlign"
							value="上对齐"
							onclick="$(this).is(':checked')?setv('valign','top'):setv('fontWeight','middle')" />
						<label for="tb_TopAlign">上对齐</label> <input type="checkbox"
							name="checkbox" id="tb_velAlign" value="垂直居中"
							onclick="$(this).is(':checked')?setv('valign','middle'):setv('fontWeight','middle')" />
						<label for="tb_velAlign">垂直居中</label> <input type="checkbox"
							name="checkbox" id="tb_BottomAlign" value="下对齐"
							onclick="$(this).is(':checked')?setv('valign','bottom'):setv('fontWeight','middle')" />
						<label for="tb_BottomAlign">下对齐</label> <input type="checkbox"
							name="checkbox" id="tb_LeftAlign" value="左对齐"
							onclick="$(this).is(':checked')?setv('align','left'):setv('fontWeight','left')" />
						<label for="tb_LeftAlign">左对齐</label> <input type="checkbox"
							name="checkbox" id="tb_levelAlign" value="水平居中"
							onclick="$(this).is(':checked')?setv('align','center'):setv('fontWeight','left')" />
						<label for="tb_levelAlign">水平居中</label> <input type="checkbox"
							name="checkbox" id="tb_RightAlign" value="右对齐"
							onclick="$(this).is(':checked')?setv('align','right'):setv('fontWeight','left')" />
						<label for="tb_RightAlign">右对齐</label>
					</div>
					<div class="headCell">
						<button type="button" id="tb_merge"
							onclick="rdes.Handsontable.core.Mergecells()">合并单元格</button>
						<button type="button" id="tb_split"
							onclick="rdes.Handsontable.core.MergecellsOpen()">拆分单元格</button>
					</div>
				</div>
				<div class="Shearplate2">
					<span>对齐方式</span> <a></a>
				</div>
			</div>
			<div class="headCells">
				<div class="headCell">
					<input type="checkbox" name="wordWrap" id="tb_wrap"
						onclick="$(this).is(':checked')?setv(this.name,'normal'):setv(this.name,'nowrap')" /><label
						for="tb_wrap">自动换行</label>

					<!--<button type="button" id="tb_rowtype">行类型</button>-->
					<select name="tb_category" id="tb_category"
						style="width:78px; display:block; float:left; margin-top:4px; margin-left:10px;">
						<option value=""></option>
						<option value="headtitle">头标题</option>
						<option value="reporthead">报表头</option>
						<option value="dataarea" selected="selected">数据区</option>
						<option value="reportfoot">报表尾</option>
						<option value="foottitle">尾标题</option>
					</select>
				</div>
				<div class="ui-buttonset">

					<button type="button" id="tb_rows">行操作</button>
					<label class="cellRow">插入行</label>
					<button type="button" id="tb_cols">列操作</button>
					<label class="cellCol">插入列</label>
					<button type="button" id="tb_frame">边框</button>
					<label class="cellFrame">边框</label>
				</div>

				<div class="Shearplate2">
					<span>单元格</span> <a></a>
				</div>
			</div>
			<div class="headEdit">
				<div class="headEditTop">
					<div class="ui-buttonset">
						<button type="button" id="tb_exp">函数</button>
						<label class="editExp">函数</label>
					</div>
					<div class="headEdit1">
						<div class="headEdit1T">
							<label>格式</label> <input name="format" id="format"
								style="width:125px"
								onclick="parent.showdialog('格式选择','Data_Format.jsp?v='+encodeURIComponent(this.value)+'&amp;n='+this.name);"
								onchange="setv(this.name,this.value)" value="">
						</div>
						<div class="headEdit1R">
							<button type="button" id="tb_parm"
								onclick="showdialog('配置参数','Data_Parm.jsp');">参数设置</button>
							<button type="button" id="tb_dataSet"
								onclick="showdialog('配置数据集','Data_Sets.jsp');">数据集设置</button>
						</div>
					</div>
					<div class="headAttr1">
						<label>宽度</label> <input name="width" id="width" value=""
							onchange="setv(this.name,this.value)"> <label>高度</label>
						<input name="height" id="height" value=""
							onchange="setv(this.name,this.value)">

					</div>
				</div>
				<div class="Shearplate2">
					<span>编辑</span> <a></a>
				</div>
			</div>
			<div class="headPage">
				<div style="overflow:hidden;">
					<select name="tb_orientation" id="tb_orientation"
						style="width:90px; display:block; float:left; margin-top:4px; margin-left:10px;">
						<option value="2">横向</option>
						<option value="1" selected="selected">纵向</option>
					</select> <select name="tb_paging" id="tb_paging"
						style="width:90px; display:block; float:left; margin-top:4px; margin-left:10px;">
						<option value="-1">不分页</option>
						<option value="0">自动分页</option>
						<option value="10" selected="selected">10条/页</option>
						<option value="20">20条/页</option>
						<option value="30">30条/页</option>
						<option value="40">40条/页</option>
					</select>
				</div>
				<div class="Shearplate2">
					<span>页面设置</span> <a></a>
				</div>
			</div>
		</div>
		<div class="toolExp">
			<label>表达式</label> <input name="expr" id="expr" style="width:102px"
				value="" onchange="setv(this.name,this.value)"
				onclick="parent.showdialog('表达式配置','Data_Expr.jsp?v='+encodeURIComponent(this.value)+'&n='+this.name);">
			<span></span>
			<ul>
				<li><a href="#" class="expNo"></a></li>
				<li><a href="#" class="expYes"></a></li>
				<li><a href="#" class="expFor"></a></li>
			</ul>
			<input type="text"> <label>字典</label> <input name="dic"
				id="dic" style="width:130px" value=""
				onchange="setv(this.name,this.value)"
				onclick="parent.showdialog('字典项设置','Data_Dic.jsp?v='+encodeURIComponent(this.value)+'&n='+this.name);">
		</div>

	</div>
	<div class="ui-layout-east">
		<iframe name="rtree" id="rtree" class="ui-layout-north"
			src="Tree_C.jsp">树</iframe>
		<iframe name="rprop" id="rprop" class="ui-layout-center"
			src="Property_C.jsp">属性</iframe>
	</div>
	<iframe name="rdes" id="rdes" class="ui-layout-center"
		src="Design_C.jsp">设计器 </iframe>
	<input type="text" id="dp" value="" style="display:none">
	<script>
		$(function() {
			//$('body').show('slow');
		})
	</script>
</body>
</html>