<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
    + path + "/";
String reporttype = request.getParameter("reporttype");
String uid = request.getParameter("uid");
String list = request.getParameter("list")==null?"":request.getParameter("list");
String flags = request.getParameter("flags")==null?"0":request.getParameter("flags");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>报表详情预览页面</title>
<style type="text/css">
html, body {
	height: 100%
}

body {
	margin: 0px;
	padding: 0px;
	overflow: hidden;
	font-size: 12px;
	font-family: "Microsoft YaHei", SimSun, Arial, Sans-Serif;
}

td {
	font-size: 12px;
}

#pageindex {
	text-align: center;
	height: 14px;
	line-height: 1;
	margin: 0;
}

.Noprint, .Noprint2 {
	background: #fff;
	border: 1px solid #3447DF;
	padding: 5px;
}

.loadingImg_c {
	float: left;
	width: 16px;
	height: 16px;
	background: url(../images/loading.gif) no-repeat left center;
}

.display_c {
	font-family: "Microsoft Yahei", "SimSun";
	font-size: 10pt;
	float: left;
	height: 16px;
	line-height: 16px;
	margin-left: 4px;
}

.queryview-toolbar-button {
	height: 18px;
	width: 48px;
	display: inline-block;
	cursor: pointer;
	background-color: transparent;
	background-repeat: no-repeat;
	background-position: left center;
	border: none;
	font-size: 12px;
	margin: 2px;
}

.queryview-export {
	width: 60px;
	padding: 0 5px;
	border: 1px solid #ccc;
	position: absolute;
	background: #f6f6f5;
	z-index: 990;
	margin-left: 80px;
	margin-top: 0;
	display: none;
}

.queryview-export a {
	color: #00B0F0;
	text-decoration: none;
	display: block;
}

.queryview-export a:hover {
	background: #277b9d;
	color: #fff;
}

.queryview-toolbar-refresh {
	background: url(../images/pageImg3.png) no-repeat;
	margin-top: 8px;
	margin-left: 30px;
}

.queryview-toolbar-export {
	background: url(../images/pageImg1.png) no-repeat;
}

.queryview-toolbar-print {
	background: url(../images/pageImg2.png) no-repeat;
}

.queryview-toolbar-refreshzd {
	background: url(../images/pageImg4.png) no-repeat;
}

.queryview-toolbar-save {
	background: url(../images/pageImg5.png) no-repeat;
}

#searchaddition span {
	margin-right: 10px;
}

#prefrm {
	width: 100%;
	min-height: 100px;
	height: 100%;
}

.tr-toolbars {
	background-color: #E6E5E5;
}
</style>
<script src="../lib/jquery.js"></script>
<script src="../js/common.js"></script>
<script src="../lib/fileDownload.js"></script>
<script type="text/javascript">
var ispubu=false;//瀑布流
var pageflag=0;
var totalRecord=0;
var jsonStr='';
var subreport=false;//主子报表
	var isset=0;
	var dicData={};
    $(function(){
        $('.queryview-export').hover(function(){$(this).show();},function(){$(this).hide();});	
        
        $("#pageindex").bind("keydown",function(event){
			oninputkeydown(event);
		});
        $("#pagesize").change(function(){
        pageflag=1;
        	init(1);
        });
    });
    
    function isIE() { 
	 if (!!window.ActiveXObject || "ActiveXObject" in window)
	  return true;
	  else
	  return false;
	 }
    
    function oninputkeydown(event) {
		if(event.keyCode == 13){   
			var i=parseInt($('#pageindex').val());
			var t=parseInt($('#pagecount').html());
			if(i<=t&&i>=1){init(i);}
		 }   
	}
	
    function setpage(pageindex,pagecount){
	if(pageindex>0&&pageindex<=pagecount){
		if(pageindex==pagecount){
			if(pageindex==1&&pagecount==1){
				$('#pagefirst').attr('disabled',true)
				$('#pagepre').attr('disabled',true);
				$('#pagenext').attr('disabled',true);
				$('#pagelast').attr('disabled',true);
				}else if(pageindex==1){
				$('#pagefirst').attr('disabled',true)
				$('#pagepre').attr('disabled',true);
				$('#pagenext').attr('disabled',false);
				$('#pagelast').attr('disabled',false);
				}else{
					$('#pagefirst').attr('disabled',false);
					$('#pagepre').attr('disabled',false);
					$('#pagenext').attr('disabled',true);
					$('#pagelast').attr('disabled',true);
					}
			}else{
				$('#pagefirst').attr('disabled',false)
				$('#pagepre').attr('disabled',false);
				$('#pagenext').attr('disabled',false);
				$('#pagelast').attr('disabled',false);
				}
				$('#pageindex').val(pageindex);
				$('#pagecount').html(pagecount);
	}
	}
	function first(){init(1);}
	function prep(){var i=parseInt($('#pageindex').val());var t=parseInt($('#pagecount').html());if(i<=t&&i>1){init(i-1);}}
	function nextp(){var i=parseInt($('#pageindex').val());var t=parseInt($('#pagecount').html());if(i<t){init(i+1);}}
	function last(){var t=parseInt($('#pagecount').html());init(t);}
    function initMain(){
        try{
        if(parent.jsonStr!=undefined&&parent.jsonStr!=''){jsonStr=parent.jsonStr;$('#reportJson').val(JSON.stringify(jsonStr));}
        }catch(e){}
        init(1);   
    }
    
    function init(page){ 
        getSearch(page);
    } 
    
    
    function getSearch(page){
        $("#reporttype").val('<%=reporttype%>');
               
               
        var url = "../../previewDesignNew.action";
        var pageSize,pageType;
			if(pageflag==0){
			if(jsonStr!=''){
        	pageSize=parent.jsonStr.page;
        	$("#pagesize").val(pageSize);
        	}else{
        	pageSize=0;
        	}
        	}else{
        	pageSize=$("#pagesize").val();
        	}
        	if(jsonStr!=''){
        	pageType=parent.jsonStr.pageorder;
        	}else{
        	pageType='1';
        	}
			//}
            var urlsub=url+"?opt=previewNew&currentPage="+page+"&pageSize="+pageSize+"&pageType="+pageType;
        url=url+"?uid=<%=uid%>&opt=previewNew&currentPage="+page+"&pageSize="+pageSize+"&pageType="+pageType+"&totalRecord="+totalRecord;
        $.ajax({
            url : url, 
            data:$('#exportData').serialize(),
            type:"post",
            async:true,
            success : function(data) {
            	if(data.expires!='0'){
            		$('#license').html(data.exprisemsg);
            	}
            	if(pageflag==0&&jsonStr==''){$("#pagesize").val(data.pageSize);}
            	var dbody=$('<div>'+data.body+'</div>');
            	var ncss='<style>'+data.css+'</style>';
            	var newbody=ncss+dbody.html();
            	subreport=data.subreport;
                if(page==1){
                	totalRecord=parseInt(data.totalRecord);
                	if(totalRecord>0&&!subreport){
	                $('#totalrecord').html(data.totalRecord);
	                if(!subreport){//主子报表不显示总记录数
	                $('#totalrecord').parent('label').show();
	                }
                	}
                	if(data.pageSize!=-2){if(!subreport&&data.pageSize!=-1){$('#pageparm').show();}}else{ispubu=true;}
                }
                $("#prefrm")[0].contentWindow.frmChild(newbody,(pageSize==0?$('input[name="printfx"]:checked').val():3),data.currentPage);
                setpage(data.currentPage,data.totalPage);
                /* if(data.toolbar=='bottom'){//下工具栏
                	$('.queryview-export').css('bottom','1px');
                	$('.rt-parmlist',parent.document).css('top','0').css('bottom','34px');
                	var toolbar=$('._trToolbar');
                	$('._trToolbar').detach();
                	toolbar.insertAfter('#maintr');
                } */
            },
            error:function(){alert('数据加载出错，请联系管理员！');},
            beforeSend:function(){$('.Noprint').show();$('#pppg').html(page);},
            complete:function(){setTimeout("$('.Noprint').hide()",200)}
        });
    	
    	
    }
     
    function doPrint(how) {
    if(isIE()){
    	//加载打印控件
    	if ($("#jatoolsPrinter").length == 0) {
    		var $printer = "<OBJECT  ID='jatoolsPrinter' CLASSID='CLSID:B43D3361-D075-4BE2-87FE-057188254255' codebase='jatoolsPrinter.cab#version=8,6,0,0'></OBJECT>";
    		$("body").append($printer);
    	}
    	
        myDoc = {
        	printBackground:true,//打印背景颜色
            settings : {
                topMargin : 100,
                leftMargin : 80,
                bottomMargin : 100,
                rightMargin : 80,
                orientation : $('input[name="printfx"]:checked').val(),
                paperName : 'a4'
            }, // 配置页边距（单位1/10mm），orientation:1为纵向,2为横向，选择a4纸张进行打印
            documents : window.frames["prefrm"].document,
            copyrights : '杰创软件拥有版权  www.jatools.com' // 版权声明,必须   
        };
        if (how == '打印预览...')
            jatoolsPrinter.printPreview(myDoc); // 打印预览
        else if (how == '打印...')
            jatoolsPrinter.print(myDoc, true); // 打印前弹出打印设置对话框
        else
            jatoolsPrinter.print(myDoc, false); // 不弹出对话框打印
      }else{
      	$("#prefrm")[0].contentWindow.print();
      }
    }
    
    //导出文件
    function exportFile(type) {
    	$.ajax({
            url : '../../exportFlag.action?uid=<%=uid%>', 
            type:"post",
            async:true,
            success : function(data) {
            	if(data==1){
		    	$("#exporting").show();
		    	$(".queryview-export").hide();
		    	var url = "exportFromDesignNew.action?type=" + type+"&uid=<%=uid%>";
		    	if(subreport){
		    		url = "exportSubDesignNew.action?type=" + type+"&uid=<%=uid%>";
		    	}
			    	$.fileDownload(url,{data:$('#exportData').serialize(),httpMethod:'POST',
			    	 successCallback: function (url) { $("#exporting").hide(); },
			    	 failCallback: function (responseHtml, url) { $.get('../../exportFlag.action?uid=<%=uid%>&stat=1');$("#exporting").hide();alert("导出失败！")}
			    	});
            	}else{
            		alert('当前报表正在导出或导出已达最大数目，请稍候再来导出');
            	}
            }
    	});
	}
    
    //动态参数保存并刷新子页面
    function search(){
    	 $("#prefrm")[0].contentWindow.refresh();
    }
	
	$(function(){
		$("#changgeSkin").bind("change",function(){
			$(prefrm.document).find("#skin").attr("href","skin/"+$(this).val());
		})
		<%String skinType = request.getParameter("skinType");
			String defaultCss = "table-default.css";
			if ("1".equals(skinType)) {
				defaultCss = "table-layui.css";
			} else if ("2".equals(skinType)) {
				defaultCss = "table-bootstrap.css";
			} else if ("3".equals(skinType)) {
				defaultCss = "table-iview.css";
			} else if ("4".equals(skinType)) {
				defaultCss = "table-source.css";
			}%>
		$("#changgeSkin").val('<%=defaultCss%>');
	});    
    
</script>

<!--[if lte IE 7]>
<script type="text/javascript">
$(function(){
function onresize() { 
window.detachEvent("onresize", onresize);
$("#prefrm").height((document.documentElement.clientHeight - $('#miantdh').offset().top)-80);
 window.attachEvent("onresize", onresize); 
} 
window.attachEvent("onresize", onresize);
});
</script>
<![endif]-->
<!--[if gt IE 7]>
<script type="text/javascript">
$(function(){
function onresize() { 
window.detachEvent("onresize", onresize);
$("#prefrm").height((document.documentElement.clientHeight - $('#miantdh').offset().top)-50);
 window.attachEvent("onresize", onresize); 
} 
window.attachEvent("onresize", onresize);
});
</script>
<![endif]-->
</head>

<body>
	<div class="queryview-export">
		<p>
			<a href="javascript:exportFile('excel')">EXCEL</a>
		</p>
		<p style="display:none">
			<a href="javascript:exportFile('word')">WORD</a>
		</p>
		<p style="display:none">
			<a href="javascript:exportFile('pdf')">PDF</a>
		</p>
		<form action="" id="exportData" method="post" style="display:none;">
			<%
				Map map = request.getParameterMap();
				Set keSet = map.entrySet();
				for (Iterator itr = keSet.iterator(); itr.hasNext();) {
					Map.Entry me = (Map.Entry) itr.next();
					Object ok = me.getKey();
					Object ov = me.getValue();
					String[] value = new String[1];
					if (ov instanceof String[]) {
						value = (String[]) ov;
					} else {
						value[0] = ov.toString();
					}
					for (int k = 0; k < value.length; k++) {
						if (!ok.equals("reporttype") && !ok.equals("uid") && !ok.equals("list")) {
							out.println("<input type=\"hidden\" name=\"" + ok + "\" id=\"" + ok + "\" value=\"" + value[k]
									+ "\">");
						}
					}
				}
			%>
			<textarea name="jsonlist" id="jsonlist" style="display:none;"><%=list%></textarea>
			<textarea name="reportJson" id="reportJson" style="display:none;"></textarea>
			<input id="frmsub" type="submit" />
		</form>
	</div>
	<table style="width: 100%; height: 100%; table-layout: fixed;" border="0" cellspacing="0" cellpadding="0">
		<tbody>
			<tr height="100%" width="100%">
				<td class="_outerFrame" valign="top" height="100%"><div class="_frameViewContainer" style="width: 100%; height: 100%; overflow: hidden;">
						<div style="height: 100%;">
							<table width="100%" height="100%" class="_outerTable" style="table-layout: fixed;" border="0" cellspacing="0" cellpadding="0">
								<tbody>

									<tr class="_content" style="width: 100%; height: 100%;" valign="top">
										<td height="100%"><span></span>
											<table width="100%" height="100%" cellspacing="0" cellpadding="0">
												<tbody>
													<!--BeginToobar-->
													<tr height="26" class="_trToolbar queryview-toolbar" valign="top">
														<td class="toolbarBg"><table style="width: 100%; table-layout: fixed;" cellspacing="0" cellpadding="0">
																<tbody>
																	<tr>
																		<td><div style="width: 100%; overflow: hidden;">
																				<div style="width: 100%; position: relative; height:33px;line-height:33px;border-top:1px solid #ccc;border-bottom:1px solid #ccc;">
																					<table height="26" style="border-collapse: separate;" cellspacing="0" cellpadding="0">
																						<tbody>
																							<tr class="tr-toolbars">
																								<td class="queryview-toolbar-button-normal"><input title="打印" class="_btnPrint queryview-toolbar-button queryview-toolbar-print" style="margin:0 15px;width: 48px; padding-left: 20px; background-position-x: left;" type="button" value="打印 " onclick="doPrint('打印...')"></td>
																								<td class="queryview-toolbar-button-normal"><input title="导出" class="_btnExport queryview-toolbar-button queryview-toolbar-export" style="width: 48px; padding-left: 20px; background-position-x: left;" type="button" value="导出" onMouseOver="$('.queryview-export').show();"></td>
																								<%
																									System.out.println(request.getParameter("editReport"));
																								%>
																								<%
																									if ((String) request.getParameter("editReport") != null && !request.getParameter("editReport").equals("")
																											&& !request.getParameter("editReport").equals("null")) {
																								%>
																								<td class="queryview-toolbar-button-normal"><a class="_btnExport queryview-toolbar-button" style="text-decoration:none;color: black;width: 80px;text-align: center;" href="/REPORT_SOURCE/report/design/Main_D_V2.jsp?uuid=<%=request.getParameter("uid")%>" title="编辑报表" target="_blank">编辑报表</a></td>
																								<%
																									}
																								%>
																								<!-- <td class="queryview-toolbar-button-normal"><label><input title="纵向" value="1" type="radio" name="printfx" checked="checked" onclick="prefrm.refresh();"> 纵向</label> <label><input title="纵向" value="2" type="radio" name="printfx" onclick="prefrm.refresh();"> 横向</label></td>-->
																								<td><select id="changgeSkin" style="display:none;">
																										<option value="table-source.css">原始样式</option>
																										<option value="table-default.css">默认样式</option>
																										<option value="table-layui.css">LayUI样式</option>
																										<option value="table-bootstrap.css">Bootstrap样式</option>
																										<option value="table-antDesign.css">AntDesign样式</option>
																										<option value="table-iview.css">IView样式</option>
																								</select></td>
																								<td nowrap="" style="width:100%;padding-right:10px;text-align:right;"><label style="display:none">总记录：<span id="totalrecord">0</span>条
																								</label> <label id="pageparm" style="display:none"> <select name="pagesize" id="pagesize"><option value="0">自动分页</option>
																											<option value="-1">不分页</option>
																											<option value="10" selected="selected">10条/页</option>
																											<option value="20">20条/页</option>
																											<option value="30">30条/页</option>
																											<option value="40">40条/页</option>
																											<option value="50">50条/页</option>
																											<option value="100">100条/页</option></select> <input id="pagefirst" disabled="" class="queryview-toolbar-button" style="background-position: left top; width: 45px; margin-left:10px;padding-left: 20px; background: url(../images/first.png) left center no-repeat;" type="button" value="首页" onclick="first();"> <input id="pagepre" disabled="" class="queryview-toolbar-button" style="background-position: left top; width: 60px; padding-left: 15px; background-image: url(../images/previous.png); background-repeat: no-repeat;" type="button" value="上一页" onclick="prep();"> <input style="width: 30px;" type="text" value="1" id="pageindex" title="输入页码回车确认"> / <span id="pagecount">1</span> 页 <input id="pagenext" disabled="" class="queryview-toolbar-button" style="background-position: left top; width: 60px; padding-left: 15px; background-image: url(../images/next.png); background-repeat: no-repeat;" type="button" value="下一页" onclick="nextp();"> <input
																										id="pagelast" disabled="" class="queryview-toolbar-button" style="background-position: left top; width: 45px; padding-left: 20px; background: url(../images/last.png) left center no-repeat;" type="button" value="尾页" onclick="last();">
																								</label></td>
																								<td><span class="_space2 queryview-toolbar-space"></span></td>
																						</tbody>
																					</table>
																				</div>
																			</div></td>
																	</tr>
																</tbody>
															</table></td>
													</tr>
													<!--EndToobar-->
													<tr height="100%" id="maintr">
														<td valign="top" height="100%" id="miantdh"><iframe name="prefrm" id="prefrm" src="designPreviewNew.jsp?extend=<%=request.getParameter("extend")%>&skinType=<%=defaultCss%><%=request.getParameter("iscellauto") != null
					? "&iscellauto=" + request.getParameter("iscellauto")
					: ""%>" frameborder="0"></iframe></td>
													</tr>
												</tbody>
											</table></td>
									</tr>
								</tbody>
							</table>
							<div class="Noprint" style="left: 45%; top: 35%; display:none; position: absolute; z-index: 101;">
								<span class="loadingImg_c" id="loadingImg"></span> <span class="display_c" id="display" style="cursor: pointer;">正在加载第<span id="pppg">1</span>页...
								</span>
							</div>
						</div>
						<div class="Noprint2" id="exporting" style="left: 40%; top: 35%; display: none; position: absolute; z-index: 101;">
							<span class="loadingImg_c"></span> <span class="display_c" style="cursor: pointer;">数据导出中，请稍候……</span>
						</div>
						<div class="Noprint2" id="loading" style="left: 40%; top: 35%; display: none; position: absolute; z-index: 101;">
							<span class="loadingImg_c"></span> <span class="display_c" style="cursor: pointer;">数据字典更新中，请稍候……</span>
						</div>
					</div></td>
			</tr>
			<tr>
				<td id="license" style="text-align:center"></td>
			</tr>
		</tbody>
	</table>
	<div></div>
<!-- 	<script>
		var _hmt = _hmt || [];
		(function() {
			var hm = document.createElement("script");
			hm.src = "https://hm.baidu.com/hm.js?fce36ae7e6ec58c8f362bfa72c7cfb00";
			var s = document.getElementsByTagName("script")[0];
			s.parentNode.insertBefore(hm, s);
		})();
	</script> -->

</body>
</html>