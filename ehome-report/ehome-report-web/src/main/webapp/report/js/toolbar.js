var copyData;
window.onload = function() {
	var mLayout = $('body').layout({
		applyDefaultStyles : false,
		north__size : 148,
		north__closable : false,
		north__resizable : false,
		north__spacing_open : 0,
		east : {
			initHidden : true
		},
		sliderTip : "显示/隐藏侧边栏", //在某个Pane隐藏后，当鼠标移到边框上显示的提示语。
		//togglerTip_open:"关闭",//pane打开时，当鼠标移动到边框上按钮上，显示的提示语  
		//togglerTip_closed:"打开",//pane关闭时，当鼠标移动到边框上按钮上，显示的提示语
		east__closable : false,
		east__spacing_open : 5,
		east__size : 360,
		east__childOptions : {
			inset : {
				top : 0,
				left : 0,
				right : 0,
				bottom : 0
			},
			applyDefaultStyles : false,
			north__closable : false,
			north__size : .4,
			north__spacing_open : 2
		},
		south__size:300,
		spacing_open:0
	});
	console.log(mLayout);
	try {
		rdes.initDesign();
		//$('div.loading').detach();
	//	$("#rdes").animate({bottom:300,height:"-=300px"});
		$('.ui-layout-north,.ui-layout-east,.ui-layout-center,.rt-config').fadeIn(function() {
			$("#clr-0 iframe").attr("src", "Data_Sets_sidebar.jsp");
			$("#clr-1 iframe").attr("src", "Data_Parm_sidebar.jsp");

		});
	} catch (e) {
		console.log(e);
	}
};
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]);
	return null;
}
var dialog;
function showdialog(title, url) {
	dialog = layer.open({
		title : title,
		type : 2,
		maxmin : true, //开启最大化最小化按钮
		area : [ '720px', '500px' ],
		content : url
	});
}
function closedialog() {
	try {
		layer.close(dialog);
	} catch (ee) {}
}
function setv(t, v) {
	var coords = rdes.Handsontable.tableStyle(t, v);
	rprop.SynProperties("", coords);
}
function pagechange(t, v) {
	if (rdes.document.getElementById("tableModel").value != null) {
		var XMLData = rdes.Handsontable.XML.parsingToXML();
		rdes.document.getElementById("tableModel").value = XMLData;
	}
//var coords = rdes.Handsontable.tableStyle(t,v);
//rprop.SynProperties("",coords);
}
function optJRByType(t, type) {
	var dataSetsModel = rdes.document.getElementById("dataSetsModel").value;
	var tableModel = rdes.document.getElementById("tableModel").value;
	//window.open("previewDesign.action");
	$.ajax({
		url : "previewDesign.action",
		data : {
			dataSetsModel : dataSetsModel,
			tableModel : tableModel,
			type : t,
			opt : type
		},
		type : "post",
		async : false,
		success : function(data) {
			data = eval("(" + data + ")");
			if (data == "error") {
				layer.alert("错误：	页面内容超出页面高度！")
			} else if (data == "preview") {
				window.open("../rbc/designPreview.jsp", "preview");
			} else if (data == "previewc") {
				window.open("../rbc/printPrieviewC.jsp", "preview");
			} else {
				window.location.href = "../rbc/export.action?type=" + type;
			}
		}
	});
/* $.post("previewDesign.action"); */
}
$(function() {
	$("#tabs").tabs({
		active : 1
	}); //工具栏选项卡；


	$("#tb_open").button({
		text : false,
		icons : {
			primary : "toolbar_icon_open"
		}
	});
	$("#tb_modif").button({
		text : true,
		icons : {
			primary : "toolbar_icon_modif"
		}
	});
	$("#tb_save").button({
		text : true,
		icons : {
			primary : "toolbar_icon_save"
		}
	});
	$("#td_Preview").button({
		text : true,
		icons : {
			primary : "toolbar_icon_view"
		}
	});
	$("#tb_parmext").button({
		icons : {
			primary : "toolbar_icon_dataSet"
		}
	});
	$("#tb_parm").button({
		icons : {
			primary : "toolbar_icon_dataSet"
		}
	});
	$("#tb_dataSet").button({
		icons : {
			primary : "toolbar_icon_dataSet2"
		}
	});

	$("#tb_undo").button({
		text : false,
		icons : {
			primary : "toolbar_icon_undo"
		}
	});
	$("#tb_rado").button({
		text : false,
		icons : {
			primary : "toolbar_icon_redo"
		}
	});
	$("#tb_cut").button({
		icons : {
			primary : "toolbar_icon_cut"
		}
	});
	$("#tb_copy").button({
		icons : {
			primary : "toolbar_icon_copy"
		}
	});
	$("#tb_paste").button({
		icons : {
			primary : "toolbar_icon_paste"
		}
	});

	$("#tb_bold").button({
		text : false,
		icons : {
			primary : "toolbar_icon_bold"
		}
	});
	$("#tb_LeftAlign").button({
		text : false,
		icons : {
			primary : "toolbar_icon_LeftAlign"
		}
	});
	$("#tb_levelAlign").button({
		text : false,
		icons : {
			primary : "toolbar_icon_levelAlign"
		}
	});
	$("#tb_RightAlign").button({
		text : false,
		icons : {
			primary : "toolbar_icon_RightAlign"
		}
	});
	$("#tb_TopAlign").button({
		text : false,
		icons : {
			primary : "toolbar_icon_TopAlign"
		}
	});
	$("#tb_velAlign").button({
		text : false,
		icons : {
			primary : "toolbar_icon_velAlign"
		}
	});
	$("#tb_BottomAlign").button({
		text : false,
		icons : {
			primary : "toolbar_icon_BottomAlign"
		}
	});

	$("#tb_italic").button({
		text : false,
		icons : {
			primary : "toolbar_icon_italic"
		}
	});
	$("#tb_underline").button({
		text : false,
		icons : {
			primary : "toolbar_icon_underline"
		}
	});
	$("#tb_strike").button({
		text : false,
		icons : {
			primary : "toolbar_icon_strike"
		}
	});

	$("#tb_fontfamily").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		},
		change : function() {
			setv("fontFamily", this.value);
		}
	});
	$("#tb_fontfamily").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		}
	});
	$("#tb_fontsize").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		},
		change : function() {
			setv("fontSize", this.value);
		}
	});
	$("#tb_category").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		},
		change : function() {
			setv("category", this.value);
		}
	});
	$("#tb_orientation").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		},
		change : function() {
			//setv("orientation",this.value);
			pagechange("tb_orientation", this.value);
		}
	});
	$("#tb_paging").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		},
		change : function() {
			//setv("paging",this.value);
			pagechange("tb_paging", this.value);
		}
	});
	$("#tb_format").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		},
		change : function() {
			setv("fotmat", this.value);
		}
	});
	$("#tb_formatsize").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		},
		change : function() {
			setv("formatsize", this.value);
		}
	});
	$("#tb_expression").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		},
		change : function() {
			setv("expression", this.value);
		}
	});
	$("#tb_dic").selectmenu({
		icons : {
			button : "toolbar_icon_triangle"
		},
		change : function() {
			setv("dic", this.value);
		}
	});
	//菜单参数、事件定义；
	var alignOption = {
		select : function(event, ui) {
			switch (ui.item.attr("id")) {
			case "tb_alignLeft":
				setv('align', 'left');
				break;
			case "tb_alignCenter":
				setv('align', 'center');
				break;
			case "tb_alignRight":
				setv('align', 'right');
				break;
			default:
				break;
			}
		}
	};
	var valignOption = {
		select : function(event, ui) {
			switch (ui.item.attr("id")) {
			case "tb_valignTop":
				setv('valign', 'top');
				break;
			case "tb_valignMiddle":
				setv('valign', 'middle');
				break;
			case "tb_valignBottom":
				setv('valign', 'bottom');
				break;
			default:
				break;
			}
		}
	};

	var rowsOption = {
		select : function(event, ui) {
			switch (ui.item.attr("id")) {
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
		select : function(event, ui) {
			switch (ui.item.attr("id")) {
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
		select : function(event, ui) {
			switch (ui.item.attr("id")) {
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
		select : function(event, ui) {
			switch (ui.item.attr("id")) {
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
	var frameOption = {
		select : function(event, ui) {
			switch (ui.item.attr("id")) {
			case "tb_borderLeft":
				setv('border', 'left');
				break;
			case "tb_borderRight":
				setv('border', 'right');
				break;
			case "tb_borderTop":
				setv('border', 'top');
				break;
			case "tb_borderBottom":
				setv('border', 'bottom');
				break;
			case "tb_borderOut":
				setv('border', 'outSide');
				break;
			case "tb_borderAll":
				setv('border', 'all');
				break;
			case "tb_borderNone":
				setv('border', 'no');
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
	function hideMenu() {
		$.each(toolsMenus, function() {
			$(this).hide();
		});
	}
	//单元格横向对齐；
	$("#tb_align").button({
		text : false,
		icons : {
			primary : "toolbar_icon_alignLeft",
			secondary : "toolbar_icon_arrowsDown2"
		}
	}).click(function() {
		if (toolsMenus[0].is(":visible")) {
			hideMenu();
		} else {
			hideMenu();
			toolsMenus[0].show().position({
				my : "left top",
				at : "left bottom",
				of : $(this)
			})
		}
		;
		$(document).one("click", function() {
			toolsMenus[0].hide();
		});
		return false;
	});
	//单元格纵向对齐；
	$("#tb_valign").button({
		text : false,
		icons : {
			primary : "toolbar_icon_valignTop",
			secondary : "toolbar_icon_arrowsDown2"
		}
	}).click(function() {
		if (toolsMenus[1].is(":visible")) {
			hideMenu();
		} else {
			hideMenu();
			toolsMenus[1].show().position({
				my : "left top",
				at : "left bottom",
				of : $(this)
			})
		}
		;
		$(document).one("click", function() {
			toolsMenus[1].hide();
		});
		return false;
	});
	$("#tb_cut").zclip({
		path : "../lib/ZeroClipboard.swf",
		copy : function() {
			$("#dp").val(rdes.priv.editProxy.triggerHandler('cut'));
			return $("#dp").val();
		},
		afterCopy : function() { /* 复制成功后的操作 */
			//alert(1111)
		}
	});
	$("#tb_copy").zclip({
		path : "../lib/ZeroClipboard.swf",
		copy : function() {
			$("#dp").val(rdes.priv.editProxy.triggerHandler('copy'));
			return $("#dp").val();
		},
		afterCopy : function() { /* 复制成功后的操作 */
			//alert('a');
		}
	});
	// 背景颜色
	var option = {
		cancelText : "取消",
		chooseText : "确定",
		showInput : true,
		showInitial : true,
		preferredFormat : "hex",
		showPalette : true,
		palette : [
			[ "#000", "#444", "#666", "#999", "#ccc", "#eee", "#f3f3f3", "#fff" ],
			[ "#f00", "#f90", "#ff0", "#0f0", "#0ff", "#00f", "#90f", "#f0f" ],
			[ "#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d0e0e3", "#cfe2f3", "#d9d2e9", "#ead1dc" ],
			[ "#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#9fc5e8", "#b4a7d6", "#d5a6bd" ],
			[ "#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6fa8dc", "#8e7cc3", "#c27ba0" ],
			[ "#c00", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3d85c6", "#674ea7", "#a64d79" ],
			[ "#900", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#0b5394", "#351c75", "#741b47" ],
			[ "#600", "#783f04", "#7f6000", "#274e13", "#0c343d", "#073763", "#20124d", "#4c1130" ]
		],
		hide : function(color) {
			var elm = $(this).spectrum("container").attr("myAttr");
			switch (elm) {
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
		text : false,
		icons : {
			primary : "toolbar_icon_bgColor"
		}
	}).click(function() {
		setv('bgcolor', $(this).attr("data-color"));
	});
	// 背景颜色菜单
	$("#tb_bgColor_slt").button({
		text : false,
		icons : {
			primary : "toolbar_icon_arrowsDown"
		}
	}).click(function() {
		$("#tb_bgcolor_pick").spectrum("show");
		$("#tb_bgcolor_pick").spectrum("container").attr("myAttr", "bgColor"); //参数“container”,是选色器的最外层容器。
		$(".sp-container").position({
			my : "left top",
			at : "left bottom",
			of : $("#tb_bgColor")
		});
		return false;
	});
	//设置背景颜色图标按钮的识别色；
	function setbgColor(color) {
		if (color) {
			$("#tb_bgColor > span.ui-icon").css({
				"border-bottom-color" : color
			});
			$("#tb_bgColor").attr("data-color", color);
		}
		;
		return false;
	}
	//设置字体颜色图标按钮的识别色；
	function setfontColor(color) {
		if (color) {
			$("#tb_fontColor > span.ui-icon").css({
				"border-bottom-color" : color
			});
			$("#tb_fontColor").attr("data-color", color);
		}
		;
		return false;
	}
	// 字体颜色
	$("#tb_fontcolor_pick").spectrum(option);
	$("#tb_fontColor").button({
		text : false,
		icons : {
			primary : "toolbar_icon_fontColor"
		}
	}).click(function() {
		setv('fontColor', $(this).attr("data-color"));
	});
	$("#tb_fontColor_slt").button({
		text : false,
		icons : {
			primary : "toolbar_icon_arrowsDown"
		}
	}).click(function() {
		$("#tb_fontcolor_pick").spectrum("show");
		$("#tb_fontcolor_pick").spectrum("container").attr("myAttr", "fontColor");
		$(".sp-container").position({
			my : "left top",
			at : "left bottom",
			of : $("#tb_fontColor")
		});
		return false;
	});
	//单元格边框线；
	$("#tb_border").button({
		text : false,
		icons : {
			primary : "toolbar_icon_borderNone"
		}
	}).click(function() {
		var elm = $(this).attr("data-id");
		$("#" + elm).button().triggerHandler("click");
	});


	$("#tb_border_slt").button({
		text : false,
		icons : {
			primary : "toolbar_icon_arrowsDown"
		}
	}).click(function() {
		if ($(".ui_buttonMenu").is(":visible")) {
			$(".ui_buttonMenu").hide();
		} else {
			$(".ui_buttonMenu").show().position({
				my : "left top",
				at : "left bottom",
				of : $("#tb_border")
			});
		}
		;
		$(document).one("click", function() {
			$(".ui_buttonMenu").hide();
		});
		return false;
	});
	//当选择边框线菜单里的按钮时，把选中的图标传给主按钮上。

	function borderIcon(t, icon) {
		$("#tb_border").button("option", {
			label : t,
			icons : {
				primary : icon
			}
		});
	}
	;
	// 下边框按钮；
	$("#tb_border_bottom").button({
		text : false,
		icons : {
			primary : "toolbar_icon_borderBottom"
		}
	}).click(function() {
		borderIcon("下边框", "toolbar_icon_borderBottom");
		$("#tb_border").attr("data-id", this.id);
		setv("border", "bottom");
	});
	// 上边框按钮
	$("#tb_border_top").button({
		text : false,
		icons : {
			primary : "toolbar_icon_borderTop"
		}
	}).click(function() {
		borderIcon("上边线", "toolbar_icon_borderTop");
		$("#tb_border").attr("data-id", this.id);
		setv("border", "top");
	});
	$("#tb_border_left").button({
		text : false,
		icons : {
			primary : "toolbar_icon_borderLeft"
		}
	}).click(function() {
		borderIcon("左边线", "toolbar_icon_borderLeft");
		$("#tb_border").attr("data-id", this.id);
		setv("border", "left");
	});
	$("#tb_border_right").button({
		text : false,
		icons : {
			primary : "toolbar_icon_borderRight"
		}
	}).click(function() {
		borderIcon("右边线", "toolbar_icon_borderRight");
		$("#tb_border").attr("data-id", this.id);
		setv("border", "right");
	});
	$("#tb_border_out").button({
		text : false,
		icons : {
			primary : "toolbar_icon_borderOut"
		}
	}).click(function() {
		borderIcon("外边线", "toolbar_icon_borderOut");
		$("#tb_border").attr("data-id", this.id);
		setv("border", "outSide");
	});
	$("#tb_border_in").button({
		text : false,
		icons : {
			primary : "toolbar_icon_borderIn"
		}
	}).click(function() {
		borderIcon("内边线", "toolbar_icon_borderIn");
		$("#tb_border").attr("data-id", this.id);
		setv("border", "inSide");
	});
	$("#tb_border_none").button({
		text : false,
		icons : {
			primary : "toolbar_icon_borderNone"
		}
	}).click(function() {
		borderIcon("无边线", "toolbar_icon_borderNone");
		$("#tb_border").attr("data-id", this.id);
		setv("border", "no");
	});
	$("#tb_border_all").button({
		text : false,
		icons : {
			primary : "toolbar_icon_borderAll"
		}
	}).click(function() {
		borderIcon("全边框", "toolbar_icon_borderAll");
		$("#tb_border").attr("data-id", this.id);
		setv("border", "all");
	});

	//表格行列操作；		
	$("#tb_rows").button({ //行操作；
		text : false,
		icons : {
			primary : "toolbar_icon_rows",
			secondary : "toolbar_icon_arrowsDown2"
		}
	}).click(function() {
		if (toolsMenus[2].is(":visible")) {
			hideMenu();
		} else {
			hideMenu();
			toolsMenus[2].show().position({
				my : "left top",
				at : "left bottom",
				of : $(this)
			})
		}
		;
		$(document).one("click", function() {
			toolsMenus[2].hide();
		});
		return false;
	});
	$("#tb_cols").button({ //列操作；
		text : false,
		icons : {
			primary : "toolbar_icon_cols",
			secondary : "toolbar_icon_arrowsDown2"
		}
	}).click(function() {
		if (toolsMenus[3].is(":visible")) {
			hideMenu();
		} else {
			hideMenu();
			toolsMenus[3].show().position({
				my : "left top",
				at : "left bottom",
				of : $(this)
			});
		}
		;
		$(document).one("click", function() {
			toolsMenus[3].hide();
		});
		return false;
	});
	$("#tb_frame").button({ //列操作；
		text : false,
		icons : {
			primary : "toolbar_icon_frame",
			secondary : "toolbar_icon_arrowsDown2"
		}
	}).click(function() {
		if (toolsMenus[6].is(":visible")) {
			hideMenu();
		} else {
			hideMenu();
			toolsMenus[6].show().position({
				my : "left top",
				at : "left bottom",
				of : $(this)
			});
		}
		;
		$(document).one("click", function() {
			toolsMenus[6].hide();
		});
		return false;
	});
	//函数
	$("#tb_exp").button({
		text : false,
		icons : {
			primary : "toolbar_icon_exp",
			secondary : "toolbar_icon_arrowsDown2"
		}
	}).click(function() {
		if (toolsMenus[4].is(":visible")) {
			hideMenu();
		} else {
			hideMenu();
			toolsMenus[4].show().position({
				my : "left top",
				at : "left bottom",
				of : $(this)
			});
		}
		;
		$(document).one("click", function() {
			toolsMenus[4].hide();
		});
		return false;
	});
	$("#tb_merge").button({
		icons : {
			primary : "toolbar_icon_merge"
		}
	});
	$("#tb_split").button({
		icons : {
			primary : "toolbar_icon_split"
		}
	});
	$("#tb_wrap").button({
		text : true,
		icons : {
			primary : "toolbar_icon_wrap"
		}
	});
	$("#tb_rowtype").button({
		text : false,
		icons : {
			primary : "toolbar_icon_rowtype"
		}
	});
});
function setProperties(key, value) {
	switch (key) {
	case "fontWeight":
		if (value == 1) {
			$("#tb_bold").prop("checked", true);
		} else {
			$('#tb_bold').prop("checked", false);
		}
		$("#tb_bold").button("refresh");
		break;
	case "fontStyle":
		if (value == 1) {
			$('#tb_italic').prop("checked", true);
		} else {
			$('#tb_italic').prop("checked", false);
		}
		$("#tb_italic").button("refresh");
		break;
	case "textDecoration":
		if (value == "underline") {
			$('#tb_underline').prop("checked", true);
			$('#tb_strike').prop("checked", false);
		} else if (value == "line-through") {
			$('#tb_underline').prop("checked", false);
			$('#tb_strike').prop("checked", true);
		} else {
			$('#tb_underline').prop("checked", false);
			$('#tb_strike').prop("checked", false);
		}
		$("#tb_underline").button("refresh");
		$("#tb_strike").button("refresh");
		break;
	case "fontFamily":
		$('#tb_fontfamily').val(value);
		$("#tb_fontfamily").selectmenu("refresh");
		break;
	case "fontSize":
		$('#tb_fontsize').val(value);
		$("#tb_fontsize").selectmenu("refresh");
		break;
	case "category":
		$('#tb_category').val(value);
		$("#tb_category").selectmenu("refresh");
		break;
	case "orientation":
		$('#tb_orientation').val(value);
		$("#tb_orientation").selectmenu("refresh");
		break;
	case "paging":
		$('#tb_paging').val(value);
		$("#tb_paging").selectmenu("refresh");
		break;
	case "align":
		if (value == "left") {
			$('#tb_LeftAlign').prop("checked", true);
			$('#tb_levelAlign').prop("checked", false);
			$('#tb_RightAlign').prop("checked", false);
		} else if (value == "center") {
			$('#tb_LeftAlign').prop("checked", false);
			$('#tb_levelAlign').prop("checked", true);
			$('#tb_RightAlign').prop("checked", false);
		} else {
			$('#tb_LeftAlign').prop("checked", false);
			$('#tb_levelAlign').prop("checked", false);
			$('#tb_RightAlign').prop("checked", true);
		}
		$("#tb_LeftAlign").button("refresh");
		$("#tb_levelAlign").button("refresh");
		$("#tb_RightAlign").button("refresh");
		break;
	case "valign":
		if (value == "top") {
			$('#tb_TopAlign').prop("checked", true);
			$('#tb_velAlign').prop("checked", false);
			$('#tb_BottomAlign').prop("checked", false);
		} else if (value == "middle") {
			$('#tb_TopAlign').prop("checked", false);
			$('#tb_velAlign').prop("checked", true);
			$('#tb_BottomAlign').prop("checked", false);
		} else {
			$('#tb_TopAlign').prop("checked", false);
			$('#tb_velAlign').prop("checked", false);
			$('#tb_BottomAlign').prop("checked", true);
		}
		$("#tb_TopAlign").button("refresh");
		$("#tb_velAlign").button("refresh");
		$("#tb_BottomAlign").button("refresh");
		break;
	case "wordWrap":
		if (value == 1) {
			$('#tb_wrap').prop("checked", true);
		} else {
			$('#tb_wrap').prop("checked", false);
		}
		$("#tb_wrap").button("refresh");
		break;
	default:
		try {
			if (key == 'text') {
				$('#keyv').val(value);
			} else {
				$('#' + key).val(value);
			}
		} catch (e) {}
		break;
	}
}

// Generate four random hex digits.
function S4() {
	return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
}
// Generate a pseudo-GUID by concatenating random hexadecimal.
function guid() {
	//return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
	return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}