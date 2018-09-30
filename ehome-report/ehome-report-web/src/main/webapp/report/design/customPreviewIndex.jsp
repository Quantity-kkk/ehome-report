<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
    + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
.Noprint,.Noprint2 {
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
width: 18px;
display: inline-block;
cursor: pointer;
background-color: transparent;
background-repeat: no-repeat;
background-position: left center;
border: none;
font-size: 12px;
margin: 2px 5px;
}
.queryview-export {
width: 40px;
padding: 0 5px;
border: 1px solid #ccc;
position: absolute;
background: #f6f6f5;
z-index: 990;
margin-left: 55px;
margin-top: 8px;
display: none;
}
.queryview-export a {
color: #00B0F0;
text-decoration: none;
display: block;
}
.queryview-export a:hover {
color: #000;
}
.queryview-toolbar-refresh {
background: url(../images/pageImg3.png) no-repeat;margin-top:8px;margin-left:30px;
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

#searchaddition span{margin-right:10px;}
</style>
<script src="../lib/jquery.js"></script>
<script src="../lib/fileDownload.js"></script>
<%
	String reporttype = request.getParameter("reporttype");
	String uid = request.getParameter("uid");
 %>
<script type="text/javascript">
    $(function(){    	
        $('.queryview-export').hover(function(){$(this).show();},function(){$(this).hide('slow');});	
        
        //parent.searchListSub();//初始化查询--首次加载         
    });
    
    //设置翻页样式
    function setpage(pageindex,pagecount){
		if(pageindex>0&&pageindex<=pagecount){
			if(pageindex==pagecount){
				if(pageindex==1&&pagecount==1){
					$('#pagepre').attr('disabled',true);
					$('#pagenext').attr('disabled',true);
					}else if(pageindex==1){
					$('#pagepre').attr('disabled',true);
					$('#pagenext').attr('disabled',false);
					}else{
						$('#pagepre').attr('disabled',false);
						$('#pagenext').attr('disabled',true);
						}
				}else{
					$('#pagepre').attr('disabled',false);
					$('#pagenext').attr('disabled',false);
					}
					$('#pageindex').val(pageindex);
					$('#pagecount').html(pagecount);
		}
	}
	function prep(){var i=parseInt($('#pageindex').val());var t=parseInt($('#pagecount').html());if(i<=t&&i>1){init(i-1);}}
	function nextp(){var i=parseInt($('#pageindex').val());var t=parseInt($('#pagecount').html());if(i<t){init(i+1);}}
	
	//翻页查询
    function init(page){ 
    	$("#currentPage", parent.document).val(page); 
    	parent.searchListSub();//初始化查询   
    } 
        
    //打印
    function doPrint(how) {
        myDoc = {
        	printBackground:true,//打印背景颜色
            settings : {
                topMargin : 100,
                leftMargin : 100,
                bottomMargin : 100,
                rightMargin : 100,
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
    
    }
    
    //导出文件
	function exportFile(type) {
		var url = "CustomExportAction_customExport.action?type="+type;
// 		$("#searchForm", parent.document).attr("method","post");
// 		$("#searchForm", parent.document).attr("action",url);
// 		$("#searchForm", parent.document).submit();//表单提交 
		
		$("#exporting").show();
    	$(".queryview-export").hide();
    	$.fileDownload(url,{data:$('#searchForm', parent.document).serialize(),httpMethod:'POST',
	    	 successCallback: function (url) { $("#exporting").hide(); },
	    	 failCallback: function (responseHtml, url) { $("#exporting").hide();alert("导出失败！")}
	    	});
	}
		
	//显示查询结果
	function serachResult(body,currentPage,totalPage,pageSize){
		//给子页面的body赋值
        prefrm.frmChild(body,(pageSize==0?$('input[name="printfx"]:checked').val():3),currentPage);
        setpage(currentPage,totalPage);
	}
		
</script>
</head>

<body>
<div class="queryview-export">
  <p> <a href="javascript:exportFile('excel')">EXCEL</a> </p>
</div>
<table style="width: 100%; height: 100%; table-layout: fixed;" border="0" cellspacing="0" cellpadding="0">
  <tbody>
<tr height="100%" width="100%">
      <td class="_outerFrame" valign="top"  height="100%"><div class="_frameViewContainer"
                style="width: 100%; height: 100%; overflow: hidden;">
          <div style="height: 100%;">
          <table width="100%" height="100%" class="_outerTable" style="table-layout: fixed;" border="0" cellspacing="0" cellpadding="0">
              <!--BeginToobar-->
              <tbody>
              <tr height="26" class="_trToolbar queryview-toolbar" valign="top">
                  <td class="toolbarBg"><table style="width: 100%; table-layout: fixed;" cellspacing="0" cellpadding="0">
                      <tbody>
                      <tr>
                          <td><div style="width: 100%; overflow: hidden;">
                              <div style="width: 100%; position: relative; height:33px;line-height:33px;border-top:1px solid #ccc;background: url(../images/domeBg2.png) repeat-x;">
                              <table height="26" style="border-collapse: separate;" cellspacing="0" cellpadding="0">
                                  <tbody>
                                  <tr>       
                                  <td class="queryview-toolbar-button-normal"><input title="打印" class="_btnPrint queryview-toolbar-button queryview-toolbar-print" style="width: 48px; padding-left: 20px; background-position-x: left;" type="button" value="打印 " onclick="doPrint('打印...')">    	  
                                      <td class="queryview-toolbar-button-normal"><input title="导出" class="_btnExport queryview-toolbar-button queryview-toolbar-export" style="width: 48px; padding-left: 20px; background-position-x: left;" type="button" value="导出" onMouseOver="$('.queryview-export').show();"></td>
                                      <td nowrap="" style="width:100%;text-align:right;">
										<input id="pagepre" disabled="" class="queryview-toolbar-button" style="background-position: left top; width: 60px; padding-left: 15px; background-image: url(../images/previous_disabled.png); background-repeat: no-repeat;" type="button" value="上一页" onclick="prep();">
										<input style="width: 30px;" type="text" value="1" id="pageindex"> / <span id="pagecount">1</span> 页
										<input id="pagenext" disabled="" class="queryview-toolbar-button" style="background-position: left top; width: 60px; padding-left: 15px; background-image: url(../images/next_disabled.png); background-repeat: no-repeat;" type="button" value="下一页" onclick="nextp();">
										</td>
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
              <tr class="_content" style="width: 100%; height: 100%;" valign="top">
                  <td height="100%"><span></span>
                  <table width="100%" height="100%" cellspacing="0" cellpadding="0">
                      <tbody>
                      <tr height="100%">
                          <td valign="top" height="100%"><div style="width:100%;height:100%;z-index:-1;">
                              <iframe name="prefrm" id="prefrm" src="customPreviewNew.jsp" frameborder="0" style="width: 100%; height: 100%;"></iframe>
                            </div></td>
                        </tr>
                    </tbody>
                    </table></td>
                </tr>
            </tbody>
            </table>
          <div class="Noprint"
                        style="left: 35%; top: 35%; display: none; position: absolute; z-index: 101;"> <span class="loadingImg_c" id="loadingImg"></span> <span
                            class="display_c" id="display" style="cursor: pointer;">正在加载第<span id="pppg">1</span>页...</span> </div>
        </div>
        <div class="Noprint2" id="exporting"
                        style="left: 35%; top: 35%; display: none; position: absolute; z-index: 101;"> <span class="loadingImg_c"></span> <span
                            class="display_c" style="cursor: pointer;">数据导出中，请稍候……</span> </div>
        <div class="Noprint2" id="loading"
            style="left: 35%; top: 35%; display: none; position: absolute; z-index: 101;"> <span class="loadingImg_c"></span> <span
                class="display_c" style="cursor: pointer;">数据字典更新中，请稍候……</span> </div>
        </div>
        </td>
    </tr>
</tbody>
</table>
<OBJECT  ID="jatoolsPrinter" CLASSID="CLSID:B43D3361-D075-4BE2-87FE-057188254255" codebase="jatoolsPrinter.cab#version=8,6,0,0"></OBJECT>
<div>

</div>
</body>
</html>