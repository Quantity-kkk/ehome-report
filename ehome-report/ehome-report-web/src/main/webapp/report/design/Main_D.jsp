<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>动态报表设计器报表设计器</title>
<%@ include file="../../report/common/head.jsp"%>
<link rel="stylesheet" type="text/css" href="../lib/spectrum/spectrum.css">
<link href="../lib/ui/jquery-ui.min.css" rel="stylesheet">
<link href="../css/head.css" rel="stylesheet" type="text/css">
<script src="../lib/jquery.min.js"></script>
<script type="text/javascript" src="../lib/layer/layer.js"></script>
<script src="../lib/ui/jquery-ui.min.js"></script>
<script src="../lib/jquery.layout.js"></script>
<script type="text/javascript" src="../lib/spectrum/spectrum.js"></script>
<script type="text/javascript" src="../js/toolbar.js"></script>
<script type="text/javascript" src="../lib/jquery.zclip.min.js"></script>
<link href="../lib/uploadify/uploadify.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../lib/uploadify/swfobject.js"></script>
<script type="text/javascript" src="../lib/uploadify/jquery.uploadify.v2.1.4.min.js"></script>
<script src="../lib/aes.js"></script>
<script src="../lib/tbl/jquery.handsontable.js"></script>
<!-- 数据集tab页 -->
<link href="css/colorfulTab.css" rel="stylesheet" type="text/css">
<script src="js/colorfulTab.js"></script>
<!-- 上传图片插件 -->
<script type="text/javascript">
	var jsessionid = "<%=session.getId()%>";  //勿删，uploadify兼容火狐用到
</script>
<style type="text/css">
body{margin:0}
div.loading{width:100px;height:30px;background:url(../images/loading.gif) 10px center no-repeat;padding-left:30px;line-height:30px;border:1px solid #aae;color:#15b;margin:100px auto;}
.ui-layout-north,.ui-layout-east,.ui-layout-center{display:none;}
</style>
<%
	String uuid = request.getParameter("uuid");
 %>
<script type="text/javascript">
	$(function(){
		var uuid = '<%=uuid%>';
		if(uuid=="" || uuid=="null"){
			$("#tb_modif").hide();
			$("#tb_save").find('span.ui-button-text').text("保存");
		}else{
			$("#tb_modif").attr("title","保存");
			$("#tb_save").attr("title","另存为");
		}
		
		//导入模板
		$("#td_picture").uploadify({
			'buttonImg'	: 	"../lib/uploadify/induce.png",
			'uploader'	:	"../lib/uploadify/uploadify.swf",
			'script'    :	"importExcelAction!importExcel.action",
			'cancelImg' :	"../lib/uploadify/cancel.png",
			'folder'	:	"../uploadFiles/uploadfiles",//上传文件存放的路径,请保持与uploadFile.jsp中PATH的值相同
			'queueId'	:	"fileQueue",
			'queueSizeLimit'	:	1,//限制上传文件的数量
			'fileExt'     : '*.xls',
			'fileDesc'    : 'Please choose(.xls)',
			'auto'		:	true,
			'multi'		:	true,//是否允许多文件上传
			'simUploadLimit':	2,//同时运行上传的进程数量
			'buttonText':	"files",
			'width':'20',
			'height':'20',
			'scriptData':	{'uploadPath':'/uploadFiles/uploadfiles/','fileNmae':''},//这个参数用于传递用户自己的参数，此时'method' 必须设置为GET, 后台可以用request.getParameter('name')获取名字的值
			'method'	:	"GET",
			'onComplete':function(event,queueId,fileObj,response,data){
        		rdes.loadData(eval(response));
			}
		});
	});

	//预览报表
	function windowOpen(){
		var uuid = '<%=uuid%>';
		if(uuid=="" || uuid=="null")
			uuid=parent.guid();
		window.open('designPreview.jsp?reporttype=D&uid='+uuid,'','left=0,top=0,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=no, status=no');	
	}
</script>
<%@ include file="../../report/common/loading.jsp"%>
</head>
<body>
<!--  <div class="loading">模块装载中…</div> -->
<div name="rtop" id="rtop" class="ui-layout-north">
<div class="headTitle">
	<ul>
    	<li class="headLogo"></li>
      <!--   <li><button type="button" id="tb_modif" onclick="rdes.Handsontable.XML.updateXML();">保存</button></li>
        <li><button type="button" id="tb_save" onclick="rdes.Handsontable.XML.saveXML();">另存为</button></li> -->
        <li style="display:none"><button type="button" id="tb_undo">撤销</button></li>
        <li style="display:none"><button type="button" id="tb_rado">重做</button></li>
     <!--    <li style="display:none"><button type="button" onclick="optJRByType('D','preview');" id="td_Preview" title="预览"></button></li> -->
    <!-- 	<li><button type="button" onclick="windowOpen()" id="td_Preview" title="预览"></button></li>
        <li style="padding-top:5px;margin-left:14px;" title="导入模板"><button type="button" id="td_picture" title="导入模板"></button></li> -->
    </ul>
    <h1><span id="rptTitle">新建报表</span>-<em>动态报表</em></h1>
</div>
<div class="headTool">
	<div class="headNormal">
    	<div class="Shearplate1">
            <div class="NormalL">
               <button type="button" id="tb_modif" onclick="rdes.Handsontable.XML.updateXML();">保存</button>
               <button type="button" onclick="windowOpen()" id="td_Preview" title="预览">预览</button>
            </div>
            <div class="NormalR">
                <button type="button" id="tb_save" onclick="rdes.Handsontable.XML.saveXML();">另存为</button>
                <button type="button" id="td_picture" title="导入模板">导入</button>
                <span class="td_picture_text">导入</span>
                <!-- <button type="button" id="tb_copy" onclick="rdes.priv.editProxy.triggerHandler('copy');">复制</button> -->
            </div>
        </div>
        <div class="Shearplate2">
        	<span>常用</span>
            <a></a>
        </div>
    </div>
 	<div class="headShearplate">
    	<div class="Shearplate1">
            <div class="Shearplate1L">
                <button type="button" id="tb_paste" onclick="rdes.priv.editProxy.triggerHandler('paste');">粘贴</button>
            </div>
            <div class="Shearplate1R">
                <button type="button" id="tb_cut" onclick="rdes.priv.editProxy.triggerHandler('cut');">剪切</button>
                <button type="button" id="tb_copy" onclick="rdes.priv.editProxy.triggerHandler('copy');">复制</button>
            </div>
        </div>
        <div class="Shearplate2">
        	<span>剪切板</span>
            <a></a>
        </div>
    </div> 
    <div class="headFont">
    		<div style="height:72px;">
              <div class="headFont1" >
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
                  </select>
                  <select id="tb_fontsize" style="width:70px;">
	                <option value="5">5</option>
	                <option value="6">6</option>
                  	<option value="7">7</option>
                  	<option value="8">8</option>
                  	<option value="9">9</option>
                  	<option value="10">10</option>
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
            <input type="checkbox" name="checkbox" id="tb_bold" value="粗体" onclick="$(this).is(':checked')?setv('fontWeight','bold'):setv('fontWeight','normal')"/>
            <label for="tb_bold">粗体</label> 
            <input type="checkbox" name="checkbox" id="tb_italic" value="斜体" onclick="$(this).is(':checked')?setv('fontStyle','italic'):setv('fontStyle','normal')"/>
            <label for="tb_italic">斜体</label> 
            <input type="checkbox" name="checkbox" id="tb_underline" value="下划线" onclick="$(this).is(':checked')?setv('textDecoration','underline'):setv('textDecoration','none')"/> 
            <label for="tb_underline">下划线</label> 
            <input type="checkbox" name="checkbox" id="tb_strike" value="删除线" onclick="$(this).is(':checked')?setv('textDecoration','line-through'):setv('textDecoration','none')">   
            <label for="tb_strike" class="toolDeteleline">删除线</label> 
            <div class="splitBtn splitBtn1">
                <button type="button" id="tb_bgColor" data-color="#ffff00">背景颜色</button>
                <button type="button" id="tb_bgColor_slt" class="smallBtn">选择更多颜色</button>
                <input type='text' id="tb_bgcolor_pick"/>
            </div>
            <div class="splitBtn">
                <button type="button" id="tb_fontColor" data-color="#000000">字体颜色</button>
                <button type="button" id="tb_fontColor_slt" class="smallBtn">选择更多颜色</button>
                <input type='text' id="tb_fontcolor_pick"/>
            </div> 
        </div>
       </div> 
        <div class="Shearplate2">
        	<span>字体</span>
            <a></a>
        </div>
    </div>
    <div class="headAlignment"> 
    	<div class="headAlignment1">
            <div class="ui-buttonset">
                 <input type="checkbox" name="checkbox" id="tb_TopAlign" value="上对齐" onclick="$(this).is(':checked')?setv('valign','top'):setv('fontWeight','middle')"/>
            <label for="tb_TopAlign">上对齐</label> 
                 <input type="checkbox" name="checkbox" id="tb_velAlign" value="垂直居中" onclick="$(this).is(':checked')?setv('valign','middle'):setv('fontWeight','middle')"/>
            <label for="tb_velAlign">垂直居中</label> 
              <input type="checkbox" name="checkbox" id="tb_BottomAlign" value="下对齐" onclick="$(this).is(':checked')?setv('valign','bottom'):setv('fontWeight','middle')"/>
            <label for="tb_BottomAlign">下对齐</label> 
              <input type="checkbox" name="checkbox" id="tb_LeftAlign" value="左对齐" onclick="$(this).is(':checked')?setv('align','left'):setv('fontWeight','left')"/>
            <label for="tb_LeftAlign">左对齐</label> 
              <input type="checkbox" name="checkbox" id="tb_levelAlign" value="水平居中" onclick="$(this).is(':checked')?setv('align','center'):setv('fontWeight','left')"/>
            <label for="tb_levelAlign">水平居中</label> 
              <input type="checkbox" name="checkbox" id="tb_RightAlign" value="右对齐" onclick="$(this).is(':checked')?setv('align','right'):setv('fontWeight','left')"/>
            <label for="tb_RightAlign">右对齐</label> 
            </div>
            <div class="headCell">
            	 <button type="button" id="tb_merge" onclick="rdes.Handsontable.core.Mergecells()">合并单元格</button>
                 <button type="button" id="tb_split" onclick="rdes.Handsontable.core.MergecellsOpen()">拆分单元格</button>
            </div>
        </div>
        <div class="Shearplate2">
        	<span>对齐方式</span>
            <a></a>
        </div>
    </div>
    <div class="headCells">
    	 <div class="headCell">
            	 <input type="checkbox" name="wordWrap" id="tb_wrap" onclick="$(this).is(':checked')?setv(this.name,'normal'):setv(this.name,'nowrap')" /><label for="tb_wrap">自动换行</label>
            
                 <!--<button type="button" id="tb_rowtype">行类型</button>-->
                 <select name="tb_category" id="tb_category" style="width:78px; display:block; float:left; margin-top:4px; margin-left:10px;">
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
        	<span>单元格</span>
            <a></a>
        </div>
    </div>
    <div class="headEdit">
     <div class="headEditTop">
    	 <div class="ui-buttonset">  
                <button type="button" id="tb_exp">函数</button>
                <label class="editExp">函数</label>
        </div>
        <div class="headEdit1">
            <div class="headEdit1T" style="display:none">
            <label>格式</label>
            <input name="format" id="format" style="width:125px" onclick="parent.showdialog('格式选择','Data_Format.jsp?v='+encodeURIComponent(this.value)+'&amp;n='+this.name);" onchange="setv(this.name,this.value)" value="">
            </div>
           <!--  <div class="headEdit1T"> -->
           <!--  <button type="button" id="tb_parmext"  onclick="showdialog('弹出式查询参数配置','Data_ParmExt.jsp');">弹出式查询参数配置</button> -->
              <!--     <button type="button" id="tb_dataSet"  onclick="showdialog('配置数据集','Data_Sets.jsp');">数据集设置</button> -->
           <!--  </div> -->
           <!--  <div class="headEdit1R">
            	  <button type="button" id="tb_parm"  onclick="showdialog('配置参数','Data_Parm.jsp');">参数设置</button>
            </div> -->
        </div>
        <div class="headAttr1">
        	<label>宽度</label>
            <input name="width" id="width" value="" onchange="setv(this.name,this.value)">
            <label>高度</label>
            <input name="height" id="height" value="" onchange="setv(this.name,this.value)">
            
        </div>
        </div>
        <div class="Shearplate2">
        	<span>编辑</span>
            <a></a>
        </div>
    </div>
    <div class="headPage">
    	<div style="overflow:hidden;">
    	<select name="tb_orientation" id="tb_orientation" style="width:90px; display:block; float:left; margin-top:4px; margin-left:10px;">
            <option value="2">横向</option>
            <option value="1" selected="selected">纵向</option>
          </select>
          <select name="tb_paging" id="tb_paging" style="width:90px; display:block; float:left; margin-top:4px; margin-left:10px;">
            <option value="-1">不分页</option>
            <option value="0">自动分页</option>
            <option value="-2">瀑布流</option>
            <option value="10" selected="selected">10条/页</option>
            <option value="20">20条/页</option>
            <option value="30">30条/页</option>
            <option value="40">40条/页</option>
          </select>
          </div>        
        <div class="Shearplate2">
        	<span>页面设置</span>
            <a></a>
        </div>
    </div>
</div>
<div class="toolExp">
	<ul>
    	<li><a href="#" class="expNo"></a></li>
        <li><a href="#" class="expYes"></a></li>
        <li><a href="#" class="expFor"></a></li>
    </ul>
    <input title="表达式配置" type="text" style="width:200px" name="expr" id="expr" value="" onchange="setv(this.name,this.value)" onclick="parent.showdialog('表达式配置','Data_Expr.jsp?v='+encodeURIComponent(this.value)+'&n='+this.name);">
    <label>字典</label>
      <input name="dic" id="dic" style="width:130px" value="" onchange="setv(this.name,this.value)" onclick="parent.showdialog('字典项设置','Data_Dic.jsp?v='+encodeURIComponent(this.value)+'&n='+this.name);">
      <label>字段</label>
            <input name="keyv" id="keyv" style="width:160px" value=""  onclick="showdialog('配置字段','Data_File.jsp?n='+encodeURIComponent(this.value));" onchange="setv(this.name,this.value)">
</div>

        </div>
        <div class="ui-layout-east">
        <iframe name="rtree" id="rtree" class="ui-layout-north" src="Tree_D.jsp">树</iframe>
        <iframe name="rprop" id="rprop" class="ui-layout-center" src="Property_D.jsp">属性</iframe>
        </div>
        <iframe name="rdes" id="rdes" class="ui-layout-center" src="Design_D.jsp">设计器 </iframe>
        <input type="text" id="dp" value="" style="display:none">
        <ul id="tb_align_menu" style="width:120px; display:none">
            <li id="tb_alignLeft"><span class="ui-icon toolbar_icon_alignLeft"></span>左边齐</li>
    <li id="tb_alignCenter"><span class="ui-icon toolbar_icon_alignCenter"></span>居中对齐</li>
    <li id="tb_alignRight"><span class="ui-icon toolbar_icon_alignRight"></span>右对齐</li>
</ul>    
<ul id="tb_valign_menu" style="width:120px; display:none">
    <li id="tb_valignTop"><span class="ui-icon toolbar_icon_valignTop"></span>上边齐</li>
    <li id="tb_valignMiddle"><span class="ui-icon toolbar_icon_valignMiddle"></span>居中对齐</li>
    <li id="tb_valignBottom"><span class="ui-icon toolbar_icon_valignBottom"></span>下对齐</li>
   </ul>  
<ul id="tb_rows_menu" style="width:120px; display:none">
       <li id="tb_insRowAbove"><span class="ui-icon toolbar_icon_insRowAbove"></span>前插入行</li>
    <li id="tb_insRowbelow"><span class="ui-icon toolbar_icon_insRowbelow"></span>后插入行</li>
    <li id="tb_delRow"><span class="ui-icon toolbar_icon_delRow"></span>删除行</li>
</ul>   
<ul id="tb_cols_menu" style="width:120px; display:none">
    <li id="tb_insColLeft"><span class="ui-icon toolbar_icon_insColLeft"></span>左插入列</li>
    <li id="tb_insColRight"><span class="ui-icon toolbar_icon_insColRight"></span>右插入列</li>
    <li id="tb_delCol"><span class="ui-icon toolbar_icon_delCol"></span>删除列</li>
</ul>
<ul id="tb_frame_menu" style="width:120px; display:none">
    <li id="tb_borderLeft"><span class="ui-icon toolbar_icon_borderLeft"></span>左边框</li>
    <li id="tb_borderRight"><span class="ui-icon toolbar_icon_borderRight"></span>右边框</li>
    <li id="tb_borderTop"><span class="ui-icon toolbar_icon_borderTop"></span>上边框</li>
    <li id="tb_borderBottom"><span class="ui-icon toolbar_icon_borderBottom"></span>下边框</li>
    <li id="tb_borderOut"><span class="ui-icon toolbar_icon_borderOut"></span>外边框</li>
    <!--<li id="tb_borderIn"><span class="ui-icon toolbar_icon_borderIn"></span>内边框</li>-->
    <li id="tb_borderAll"><span class="ui-icon toolbar_icon_borderAll"></span>所有边框</li>
    <li id="tb_borderNone"><span class="ui-icon toolbar_icon_borderNone"></span>无边框</li>
</ul>  
<ul id="tb_exp_menu" style="width:120px; display:none">
    <li id="tb_sum"><span class="ui-icon toolbar_icon_expSum"></span>总合</li>
    <li id="tb_average"><span class="ui-icon toolbar_icon_expAverage"></span>平均值</li>
    <li id="tb_count"><span class="ui-icon toolbar_icon_expCount"></span>计数</li>
    <li id="tb_distinctCount"><span class="ui-icon toolbar_icon_expDistinctCount"></span>计数去重</li>
    <li id="tb_highest"><span class="ui-icon toolbar_icon_expHighest"></span>最大值</li>
    <li id="tb_lowest"><span class="ui-icon toolbar_icon_expLowest"></span>最小值</li>
    <li id="tb_delexp"><span class="ui-icon toolbar_icon_expDelexp"></span>清除</li>
</ul> 
<ul id="tb_undo_menu" style="width:120px; display:none">
	<li id="tb_aa"><span class="ui-icon toolbar_icon_insColLeft"></span>左插入列</li>
    <li id="tb_ab"><span class="ui-icon toolbar_icon_insColRight"></span>右插入列</li>
    <li id="tb_ac"><span class="ui-icon toolbar_icon_delCol"></span>删除列</li>
</ul> 
<div class="ui-layout-south">
   <div class="rt-config">
        <div class="rt-body">
            <div class="colorful-tab-wrapper" id="colorful">
                <ul class="colorful-tab-menu">
                    <li class="colorful-tab-menu-item active">
                        <a href="#clr-0">
                        <!-- <i class="colorful-icon"></i> -->
                        数据集设置</a>
                    </li>
                    <li class="colorful-tab-menu-item">
                        <a href="#clr-1">
                       <!--  <i class="colorful-icon"></i> -->
                        参数设置</a>
                    </li>
                </ul>
                <i class="hidebtn"></i>
                <div class="colorful-tab-container">
                    <div class="colorful-tab-content active" id="clr-0">
                      <iframe height="100%" width="100%" src="" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
                    </div>
                    <div class="colorful-tab-content" id="clr-1">
                      <iframe height="100%" width="100%" src="" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
$(function(){
	//$('body').show('slow');
	$('#tb_category-menu .ui-menu-item').addClass("1");
	$("#colorful").colorfulTab();
})
</script>              
</body>
</html>		