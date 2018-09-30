<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String uuid = request.getParameter("uuid");
	String jsCatalog = "sourcejs";
%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8" />
<title>报表设计器</title>
<link rel="icon" href="common/img/favicon.ico">
<link rel="stylesheet" href="common/lib/handsontable/handsontable.full.css" />
<link rel="stylesheet" href="common/lib/colorpicker/spectrum.css" />
<link rel="stylesheet" href="common/lib/zTree_v3/css/metroStyle/metroStyle.css" />
<link rel="stylesheet" href="common/lib/layer/theme/default/layer.css" />
<link rel="stylesheet" href="common/lib/highlight/styles/github.css" />
<link rel="stylesheet" href="common/lib/webuploader/webuploader.css" />
<link rel="stylesheet" type="text/css" href="common/lib/cropper/cropper.min.css" />
<link rel="stylesheet" type="text/css" href="common/lib/font-awesome-4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="common/lib/select2/css/select2.css" />
<link rel="stylesheet" type="text/css" href="common/css/dataExpr.css" />
<link rel="stylesheet" href="common/css/main.css" />
<script type="text/javascript" src="common/all/jquery-1.11.0.js"></script>
<script type="text/javascript" src="common/all/common.js"></script>
</head>

<body>
	<%@ include file="../../report/common/loading.jsp"%>
	<div class="tb-top">
		<div class="top-header">
			<div class="logo"></div>
			<div class="title" id="reportTitle">报表名称</div>
		</div>
		<div class="toolbars" id="toolbars">
			<div class="group common">
				<div class="body">
					<button type="button" class="tag-btn" onclick="tb_modif()">
						<i class="tag modif"></i> <span>保存</span>
					</button>
					<button type="button" class="tag-btn" onclick="tb_save()">
						<i class="tag save"></i> <span>另存为</span>
					</button>
					<button type="button" class="tag-btn" onclick="tb_view()">
						<i class="tag view"></i> <span>预览</span>
					</button>
					<button type="button" class="tag-btn" id="fileupload">
						<i class="tag upload"></i> <span>导入</span>
					</button>
				</div>
				<div class="foot">常用</div>
			</div>
			<div class="group clipboard">
				<div class="body">
					<div class="sub sub-left">
						<button type="button" class="tag-btn paste-btn">
							<i class="tag paste"></i> <span>粘贴</span>
						</button>
					</div>
					<div class="sub sub-right">
						<button type="button" class="tag-btn">
							<i class="tag copy"></i> <span>复制</span>
						</button>
						<button type="button" class="tag-btn">
							<i class="tag cut"></i> <span>剪切</span>
						</button>
					</div>

				</div>
				<div class="foot">剪切板</div>
			</div>
			<div class="group font">
				<div class="body">
					<div class="sub sub-top">
						<select name="" id="fontfamily" class="fontfamily" onchange="setFontFamily(this)">
							<option value=""></option>
							<option value="SimHei">黑体</option>
							<option value="SimSun">宋体</option>
							<option value="Microsoft YaHei">微软雅黑</option>
							<option value="NSimSun">新宋体</option>
							<option value="FangSong">仿宋</option>
							<option value="KaiTi">楷体</option>
						</select> 
						<select name="" id="fontsize" class="fontsize" ><!-- onchange="setFontSize(this)" -->
							<option value=""></option>
							<option value="6px">6</option>
							<option value="8px">8</option>
							<option value="9px">9</option>
							<option value="10px">10</option>
							<option value="12px">12</option>
							<option value="14px">14</option>
							<option value="15px">15</option>
							<option value="16px">16</option>
							<option value="18px">18</option>
							<option value="20px">20</option>
							<option value="22px">22</option>
							<option value="24px">24</option>
							<option value="26px">26</option>
							<option value="28px">28</option>
							<option value="36px">36</option>
							<option value="48px">48</option>
							<option value="72px">72</option>
						</select>
					</div>
					<div class="sub sub-bottom">
						<button type="button" class="tag-btn btn-bold" onclick="setFontStyle('700')">
							<i class="tag bold"></i>
						</button>
						<button type="button" class="tag-btn btn-italic" onclick="setFontStyle('italic')">
							<i class="tag italic"></i>
						</button>
						<button type="button" class="tag-btn btn-underline" onclick="setFontStyle('underline')">
							<i class="tag underline"></i>
						</button>
						<button type="button" class="tag-btn btn-strike" onclick="setFontStyle('strike')">
							<i class="tag strike"></i>
						</button>
						<button type="button" class="tag-btn" id="bgColor">
							<i class="tag bgColor"></i>
						</button>
						<button type="button" class="tag-btn" id="fontColor">
							<i class="tag fontColor"></i>
						</button>
					</div>

				</div>
				<div class="foot">字体</div>
			</div>
			<div class="group alignment">
				<div class="body">
					<div class="sub sub-left">
						<button type="button" class="tag-btn btn-htTop btn-av" onclick="setAlignmentV('htTop')">
							<i class="tag topAlign"></i>
						</button>
						<button type="button" class="tag-btn btn-htMiddle btn-av" onclick="setAlignmentV('htMiddle')">
							<i class="tag velAlign"></i>
						</button>
						<button type="button" class="tag-btn btn-htBottom btn-av" onclick="setAlignmentV('htBottom')">
							<i class="tag bottomAlign"></i>
						</button>
						<button type="button" class="tag-btn btn-htLeft btn-ah" onclick="setAlignmentH('htLeft')">
							<i class="tag leftAlign"></i>
						</button>
						<button type="button" class="tag-btn btn-htCenter btn-ah" onclick="setAlignmentH('htCenter')">
							<i class="tag levelAlign"></i>
						</button>
						<button type="button" class="tag-btn btn-htRight btn-ah" onclick="setAlignmentH('htRight')">
							<i class="tag rightAlign"></i>
						</button>
					</div>
					<div class="sub sub-right">
						<button type="button" class="tag-btn merge-btn" onclick="tb_merge()">
							<i class="tag merge"></i> <span>合并单元格</span>
						</button>
						<button type="button" class="tag-btn split-btn" onclick="tb_split()">
							<i class="tag split"></i> <span>拆分单元格</span>
						</button>
					</div>

				</div>
				<div class="foot">对齐方式</div>
			</div>
			<div class="group cells">
				<div class="body">
					<div class="sub sub-left">
						<button type="button" class="tag-btn wrap-btn" onclick="tb_wrap()">
							<i class="tag wrap"></i> <span>自动换行</span>
						</button>
						<select name="category" id="category" class="category" onchange="tb_category(this);">
							<option value=""></option>
							<option value="headtitle">头标题</option>
							<option value="reporthead">报表头</option>
							<option value="dataarea">数据区</option>
							<option value="reportfoot">报表尾</option>
							<option value="foottitle">尾标题</option>
						</select>

					</div>
					<div class="sub sub-right">
						<button type="button" class="tag-btn rows-btn">
							<i class="tag rows"></i> <span>插入行</span>
						</button>
						<ul class="select-div select-ctrl-rows">
							<li data-ctrl="insertRowAbove">在上方插入一行</li>
							<li data-ctrl="insertRowBelow">在下方插入一行</li>
							<li data-ctrl="delRow">删除选中行</li>
						</ul>
						<button type="button" class="tag-btn cols-btn">
							<i class="tag cols"></i> <span>插入列</span>
						</button>
						<ul class="select-div select-ctrl-cols">
							<li data-ctrl="insertColLeft">在左方插入一列</li>
							<li data-ctrl="insertColRight">在右方插入一列</li>
							<li data-ctrl="delCol">删除选中列</li>
						</ul>
						<!--	<button type="button" class="tag-btn frame-btn">
							<i class="tag frame"></i> <span>边框</span>
						</button>-->

						<ul class="border-btn select-ctrl-border">
							<li data-ctrl="borderLeft"><i class="tag btn-borderLeft"></i></li>
							<li data-ctrl="borderRight"><i class="tag btn-borderRight"></i></li>
							<li data-ctrl="borderTop"><i class="tag btn-borderTop"></i></li>
							<li data-ctrl="borderBottom"><i class="tag btn-borderBottom"></i></li>
							<li data-ctrl="borderOut"><i class="tag btn-borderOut"></i></li>
							<li data-ctrl="borderIn"><i class="tag btn-borderIn"></i></li>
							<li data-ctrl="borderAll"><i class="tag btn-borderAll"></i></li>
							<li data-ctrl="borderNode"><i class="tag btn-borderNode"></i></li>
						</ul>
					</div>

				</div>
				<div class="foot">单元格</div>
			</div>
			<div class="group edit">
				<div class="body">
					<div class="sub sub-left">
						<button type="button" class="tag-btn exp-btn" onclick="openExpr()">
							<i class="tag exp"></i> <span>函数</span>
						</button>
					</div>
					<div class="sub sub-right">
						<div class="input-group">
							<label for="">宽</label> <input type="number" id="cell-width" class="cell-width" />
						</div>
						<div class="input-group">
							<label for="">高</label> <input type="number" id="cell-height" class="cell-height" />
						</div>
					</div>
				</div>
				<div class="foot">编辑</div>
			</div>
			<div class="group pagesetting">
				<div class="body">
					<select name="orientation" id="orientation" class="orientation" onchange="tb_orientation(this)">
						<option value="1">横向</option>
						<option value="0">纵向</option>
					</select> <select name="paging" id="paging" class="paging" onchange="tb_paging(this)">
						<option value="-1">不分页</option>
						<option value="0">自动分页</option>
						<option value="-2">瀑布流</option>
						<option value="10" selected="selected">10条/页</option>
						<option value="20">20条/页</option>
						<option value="30">30条/页</option>
						<option value="40">40条/页</option>
					</select> 
					<label>工具栏位置</label> 
					<select name="toolbarpos" id="toolbarpos" class="toolbarpos" onchange="tb_toolbarpos(this);">
						<option value="top">上</option>
						<option value="bottom">下</option>
					</select>
					<label>辅助线</label> 
					<select id="guide_line" class="toolbarpos" onchange="guide_line(this);">
						<option value="">去辅助线</option>
						<option value="A3">A3辅助线</option>
						<option value="A4">A4辅助线</option>
						<option value="A5">A5辅助线</option>
						<option value="B4">B4辅助线</option>
						<option value="B5">B5辅助线</option>
					</select>
				</div>
				<div class="foot">页面设置</div>
			</div>
		</div>
		<div class="top-foot">
			<i class="dot"></i>
			<div class="btn">
				<!-- <i class="expNo"></i> <i class="expYes"></i> -->
				<i class="expFor" onclick="openExpr()"></i>
			</div>
			<div class="form">
				<input type="text" name="cellval" id="cellval" value="" readonly="readonly" onclick="openExpr()" />
			</div>
			<div class="parmdic">
				<label>数据字典</label> <input type="text" id="parmdic" onclick="openDic()" readonly="readonly" />
			</div>
		</div>
	</div>
	<div class="tb-center tb-center_left on">
		<div class="showtable" id="showtable"></div>
	</div>
	<!--<div class="tb-right">
			<div class="prop" id="prop"></div>
		</div>-->
	<div class="tb-foot on">
		<i class="foot-switch"></i>
		<div class="prop" id="prop">
			<div class="box">
				<ul class="tabs">
					<li class="tab on">数据集与参数设置</li>
				</ul>
				<div class="content">
					<div class="ct on">
						<div class="dataTrees">
							<ul id="dataSets" class="ztree"></ul>
						</div>
						<div class="dataTrees">
							<ul id="dataParms" class="ztree"></ul>
						</div>
					</div>
					<!--<div class="ct">
							
						</div>-->
				</div>
			</div>
		</div>
	</div>
	<div class="progress-bg">
		<div class="progress-body">
			<div class="progress-line"></div>
		</div>
	</div>
	<input type="file" id="importImage" style="display:none;" />
	<div class="rdp-cropper">
		<div class="rdp-cropper-body">
			<div class="rdp-img-container">
				<img id="image" src="common/img/tags/defaultTag.png" alt="Picture">
			</div>
			<div class="rdp-img-view">
				<div class="rdp-img-preview preview-lg"></div>

				<div class="docs-data">
					<div class="input-group ">
						<label class="input-group-text" for="dataX">X</label> <input type="number" class="form-control cropper-data" id="dataX" placeholder="x"> <span class="input-group-text">px</span>

					</div>
					<div class="input-group ">
						<label class="input-group-text" for="dataY">Y</label> <input type="number" class="form-control cropper-data" id="dataY" placeholder="y"> <span class="input-group-text">px</span>

					</div>
					<div class="input-group ">
						<label class="input-group-text" for="dataWidth">宽</label> <input type="number" class="form-control cropper-data" id="dataWidth" placeholder="width"> <span class="input-group-text">px</span>

					</div>
					<div class="input-group ">
						<label class="input-group-text" for="dataHeight">高</label> <input type="number" class="form-control cropper-data" id="dataHeight" placeholder="height"> <span class="input-group-text">px</span>

					</div>
					<div class="input-group ">
						<label class="input-group-text" for="dataRotate">角度</label> <input type="number" class="form-control cropper-data" id="dataRotate" placeholder="rotate"> <span class="input-group-text">deg</span>

					</div>
					<div class="input-group ">
						<label class="input-group-text" for="dataScaleX">水平缩放</label> <input type="number" step="0.1" class="form-control cropper-data no-unit" id="dataScaleX" placeholder="scaleX">
					</div>
					<div class="input-group ">
						<label class="input-group-text" for="dataScaleY">垂直缩放</label> <input type="number" step="0.1" class="form-control cropper-data no-unit" id="dataScaleY" placeholder="scaleY">
					</div>
					<fieldset class="fieldset-ratio">
						<legend>等比缩放</legend>
						<div class="input-group ">
							<label class="input-group-text" for="dataWidthRatio">宽</label> <input type="number" class="form-control cropper-ratio" id="dataWidthRatio" placeholder="width"> <span class="input-group-text">px</span>

						</div>
						<div class="input-group ">
							<label class="input-group-text" for="dataHeightRatio">高</label> <input type="number" class="form-control cropper-ratio" id="dataHeightRatio" placeholder="height"> <span class="input-group-text">px</span>

						</div>
					</fieldset>

				</div>
				<div class="docs-ctrl layui-layer-btn">
					<a id="save-cropper-image" class="layui-layer-btn0">确认</a> <a id="cancel-cropper-image" class="layui-layer-btn1">取消</a>
				</div>
			</div>
			<div class="rdp-img-toolbars">
				<ul class="rdp-img-menu">
					<li class="menu-item menu-before" data-method="zoom" data-option="0.1" title="放大"><span class="fa fa-search-plus"></span></li>
					<li class="menu-item menu-after" data-method="zoom" data-option="-0.1" title="缩小"><span class="fa fa-search-minus"></span></li>
					<li class="menu-item menu-before" data-method="move" data-option="-10" data-second-option="0" title="左移"><span class="fa fa-arrow-left"></span></li>
					<li class="menu-item" data-method="move" data-option="10" data-second-option="0" title="右移"><span class="fa fa-arrow-right"></span></li>
					<li class="menu-item" data-method="move" data-option="0" data-second-option="-10" title="上移"><span class="fa fa-arrow-up"></span></li>
					<li class="menu-item menu-after" data-method="move" data-option="0" data-second-option="10" title="下移"><span class="fa fa-arrow-down"></span></li>
					<li class="menu-item menu-before" data-method="rotate" data-option="-45" title="向左旋转"><span class="fa fa-rotate-left"></span></li>
					<li class="menu-item" data-method="rotate" data-option="45" title="向右旋转"><span class="fa fa-rotate-right"></span></li>
					<li class="menu-item" data-method="scaleX" data-option="-1" title="水平翻转"><span class="fa fa-arrows-h"></span></li>
					<li class="menu-item menu-after" data-method="scaleY" data-option="-1" title="垂直翻转"><span class="fa fa-arrows-v"></span></li>
					<li class="menu-item menu-before menu-after" data-method="reset" title="重置"><span class="fa fa-refresh"></span></li>
				</ul>
				<ul class="rdp-img-menu">
					<li class="menu-item active menu-before docs-toggles" title="16:9"><label><input type="radio" checked="checked" class="sr-only" name="aspectRatio" value="1.7777777777777777">16:9</label></li>
					<li class="menu-item docs-toggles" title="4:3"><label><input type="radio" class="sr-only" name="aspectRatio" value="1.3333333333333333">4:3</label></li>
					<li class="menu-item docs-toggles" title="1:1"><label><input type="radio" class="sr-only" name="aspectRatio" value="1"> 1:1</label></li>
					<li class="menu-item docs-toggles" title="2:3"><label><input type="radio" class="sr-only" name="aspectRatio" value="0.6666666666666666"> 2:3</label></li>
					<li class="menu-item menu-after docs-toggles" title="自由裁剪"><label><input type="radio" class="sr-only" name="aspectRatio" value="NaN"> free</label></li>
				</ul>
			</div>
		</div>

	</div>
	<script type="text/javascript" src="common/lib/handsontable/handsontable.full.min.js"></script>
	<script type="text/javascript" src="common/lib/handsontable/ZH.js"></script>
	<script type="text/javascript" src="common/lib/colorpicker/spectrum.js"></script>
	<script type="text/javascript" src="common/lib/zTree_v3/js/jquery.ztree.all.min.js"></script>
	<script type="text/javascript" src="common/lib/layer/layer.js"></script>
	<script type="text/javascript" src="common/lib/sql-formatter/sql-formatter.js"></script>
	<script type="text/javascript" src="common/lib/highlight/highlight.pack.js"></script>
	<script type="text/javascript" src="common/lib/webuploader/webuploader.min.js"></script>
	<script type="text/javascript" src="common/lib/drag/drag.js"></script>
	<script type="text/javascript" src="common/lib/drag/resizable.js"></script>
	<script type="text/javascript" src="common/lib/html2canvas/html2canvas.min.js"></script>
	<script type="text/javascript" src="common/lib/cropper/cropper.min.js"></script>
	<script type="text/javascript" src="common/lib/select2/js/select2.full.min.js"></script>
	
	
	<script type="text/javascript" src="common/all/all.min.js"></script>



</body>
<div id="dataSetsInfo">
	<div class="form-info">
		<div class="input-items">
			<label class="input-label">数据源名称</label> <select name="dataSourceName" id="DataSourceName" title="请选择数据源">
			</select>
		</div>
		<div class="input-items">
			<label class="input-label">数据集名称</label> <input type="text" name="dataSetName" id="DataSetName" title="请输入数据集名称" />
		</div>
		<div id="ddslist">
			<div class="input-items">
				<label class="input-label">数据字典</label> <input type="checkbox" name="dic" id="dic" value="1" />
			</div>
			<div class="input-items">
				<label class="input-label">执行SQL</label>
				<textarea cols="38" rows="5" name="commandText" id="CommandText" title="请输入查询SQL"></textarea>

			</div>
			<div class="input-items">
				<button type="button" class="formatterBtn">格式化</button>
			</div>
			<div class="input-items">
				<label class="input-label">检验SQL</label> <input type="checkbox" name="isvalid" id="isvalid" value="1" checked="checked" /> 用于修改，取消选中修改不再校验SQL准确性
			</div>
			<div class="input-items">
				<label class="input-label">参数列表</label> <i class="icon icon-insert" onclick="addParmItem()">+</i>
				<div class="nospace"></div>
			</div>
			<div class="parm-list"></div>
		</div>
		<div id="jblist" style="display:none;">
			<div class="input-items">
				<label class="input-label">实体类</label> <input type="text" name="factoryClass" id="FactoryClass" title="请输入实体类名称" />
			</div>
		</div>

	</div>

</div>
<div id="dataParmsInfo">
	<div class="input-items">
		<label class="input-label">参数名称</label> <input name="parmName" id="parmName" title="请输入参数名称" type="text" required />
	</div>
	<div class="input-items">
		<label class="input-label">中文描述</label> <input name="parmCName" id="parmCName" title="请输入中文名称" type="text" required />
	</div>
	<div class="input-items">
		<label class="input-label">表单类型</label> <select name="showType" id="showType" title="请选择表单类型" class="sel" required>
			<option value="input">输入框</option>
			<option value="select">选择框</option>
		</select>
	</div>
	<div class="input-items" style="display: none;">
		<label class="input-label">是否可以为空</label> <input type="checkbox" name="isnull" id="isnull" value="1" checked="checked" />
	</div>
	<div class="input-items">
		<label class="input-label">参数类型</label> <select name="parmType" id="parmType" title="请选择参数类型" class="sel" required>
			<option value="normal">普通参数</option>
			<option value="dynamic">动态参数</option>
		</select>
	</div>
	<div class="input-items">
		<label class="input-label">数据类型</label> <select name="dbType" id="dbType" title="请选择数据类型" required>
			<option value="string">字符串</option>
			<option value="date">日期</option>
			<option value="double">货币</option>
			<option value="int">整数</option>
		</select>
	</div>
	<div class="input-items">
		<label class="input-label">值/表达式</label>
		<textarea cols="20" rows="3" name="parmvl" id="parmvl" title="请输入值/表达式"></textarea>
		<p class="intro">说明：当前值只在预览和使用报表内置页面作为显示时有效；表单类型为输入框时当前值为输入框默认值；表单类型为选择框时下拉框值格式为：文本:值,文本:值</p>
	</div>
</div>


<div class="dataExpr" id="dataExpr" style="display: none;">
	<ul class="dataExpr-tabs">
	  <li class="current"><a href="#" title="基础配置" onclick="dataExprTabs(this);">基础配置</a></li>
	 <!--  <li><a href="#" title="过滤" onclick="dataExprTabs(this);">过滤</a></li> -->
	</ul>
	<div class="tabs-content">
		<div class="tabs-pages tabs0">
			<table width="100%" cellpadding="5">
				<tr>
					<td>
						<fieldset>
							<legend>数据集</legend>
							<div style="padding:2px 0 1px 0">
								<select name="ds" id="ds" onchange="changeDataSets(this.value);">
									<option value=""></option>
								</select>
							</div>
						</fieldset>
		
					</td>
					<td valign="top">
						<fieldset>
							<legend>文本/字段</legend>
							<div>
								<input name="text" id="dsvt" style="width:200px" value="" onblur="setv(this.name,this.value);"> <select name="text" id="dsvs" style="display:none;" onchange="setdv(this);">
								</select>
							</div>
						</fieldset>
		
					</td>
				</tr>
				<tr>
					<td width="50%" valign="top">
						<fieldset>
							<legend>表达式</legend>
							<div>
								<textarea name="exptext" id="exptext" cols="35" rows="6" style="width:345px;height:95px;"></textarea>
							</div>
						</fieldset>
					</td>
					<td valign="top" class="czf">
						<fieldset>
							<legend>操作符</legend>
							<div id="czz" style="padding:17px 0">
								<input name="btn_jia" type="button" id="btn_jia" value="+" /> <input name="btn_jian" type="button" id="btn_jian" value="-" /> <input name="btn_cheng" type="button" id="btn_cheng" value="*" /> <input name="btn_chu" type="button" id="btn_chu" value="/" /> <input name="btn_dayu" type="button" id="btn_dayu" value="&gt;" /> <input name="btn_xiaoyu" type="button" id="btn_xiaoyu" value="&lt;" /> <input name="btn_kuoz" type="button" id="btn_kuoz" value="(" /> <input name="btn_kuoy" type="button" id="btn_kuoy" value=")" /> <input name="btn_and" type="button" id="btn_and" value="AND" /> <input name="btn_or" type="button" id="btn_or" value="OR" /> <input name="btn_not" type="button" id="btn_not" value="NOT" /> <input name="btn_deng" type="button" id="btn_deng" value="=" />
							</div>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fieldset>
							<legend>名称/字段</legend>
							<div style="padding-bottom:4px">
								<select name="aexpr" size="15" multiple="multiple" id="aexpr" onchange="sl(this.value)" style="width:100px;height:250px;"></select> <select name="bexpr" size="15" multiple="multiple" id="bexpr" ondblclick="filld('$F{'+this.value+'}')" style="width:239px;height:250px;margin-left:6px">
								</select>
							</div>
						</fieldset>
					</td>
					<td valign="top">
						<fieldset>
							<legend>可选函数</legend>
							<div style="min-height:253px">
								<ul id="dataExpertree" class="ztree"></ul>
							</div>
						</fieldset>
					</td>
				</tr>
			</table>
		</div>
		<div class="tabs-pages tabs1 tabs-hiden">
			<table class="table-warning">
				<tbody>
					<tr class="tr-attr">
						<td>
							<div class="td-attribute">
							   <span class="attr_title">属性</span>
								<ul>
									<li>
										<div class="attrspan">
											<lable>字号</lable><input class="text" type="text" name="tdFontSize" value="">
										</div>
										<div class="attrspan">
											<lable>加粗</lable><input class="text" type="text" name="tdFontWeight" value="">
										</div>
									</li>
									<li>
										<div class="attrspan">
											<lable>颜色</lable><input class="text" type="text" name="tdFontColor" value=""><span class="selectColor">
												<i id="tdchangeColor" style="background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAIAAADZF8uwAAAAGUlEQVQYV2M4gwH+YwCGIasIUwhT25BVBADtzYNYrHvv4gAAAABJRU5ErkJggg==);"></i>
											</span>
										</div>
										<div class="attrspan">
											<lable>背景色</lable><input class="text" type="text" name="tdFontBgColor" value=""><span class="selectColor">
												<i id="tdchangeBgColor" style="background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAIAAADZF8uwAAAAGUlEQVQYV2M4gwH+YwCGIasIUwhT25BVBADtzYNYrHvv4gAAAABJRU5ErkJggg==);"></i>
											</span>
										</div>
									</li>
								</ul>
							</div>
						</td>
					</tr>
					<tr class="tr-symbol">
						<td>
							<div class="td-attribute">
							   <span class="attr_title">条件</span>
							   <div>
									<select class="operator" name="operator" style="width: 15%;">
										<option value="0">等于</option>
										<option value="1">大于</option>
										<option value="-1">小于</option>
										<option value="10">大于等于</option>
										<option value="-10">小于等于</option>
										<option value="F">不等于</option>
										<option value="S">以它为开头</option>
										<option value="E">以它为结尾</option>
										<option value="IN">存在于</option>
									</select>
									<input class="td-value" type="text" placeholder="填写值" name="tdValue" style="width:80%;">
								</div>
								<div>
									<span>
										<input type="radio" name="andOr" value="and" checked="checked">与（AND）
										<input type="radio" name="andOr" value="or">或（OR）
									</span>
								</div>
								<div style="margin-top: 5px;">
									<span class="explain-text">
										条件说明
									</span>
									<span class="explain-value">
										<textarea class="td-value" name="explain" style="resize: none; width: 100%;height: 40px;"></textarea>
									</span>
								</div>
							</div>
						</td>
					</tr>
					<tr class="tr-button">
						<td>
							<div class="btn-attribute">
								<input class="btn dataExprAttrAdd" type="button" value="新增">
								<input class="btn dataExprAttrUpdate" type="button" value="更新">  
								<input class="btn dataExprAttrDel" type="button" value="删除">
							</div>
						</td>
					</tr>
					<tr class="tr-list">
						<td>
							<div class="td-attribute">
							   <span class="attr_title" style="width:60px;text-align: center;">配置条件</span>
							   <div class="list-context">
							   		<ul>
							   		</ul>
							   </div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>	
	</div>
</div>
<div class="dataReports" id="dataReports" style="display: none;">
	<div class="top">
		<select name="temlist" id="temlist" onchange="setFrameId(this)">
			<option value=""></option>
		</select>
	</div>
</div>
<div class="dataLink" id="dataLink" style="display: none;">
	<div class="top">
		<p>
			<select name="temlist" id="temlist"></select> <input type="button" id="padd" onclick="addp('','')" style="display:none" value="添加参数" /><input id="submit" class="button" type="button" value="第一步生成自定义链接" onclick="createlink()" />
		</p>
	</div>
	<div id="plist"></div>
	<div style="padding:0 20px;">
		自定义链接：<br>
		<textarea id="myconfig" rows="5" col="20" style="width:90%;height:50px;"></textarea>
		<br>说明： <br> 1、自定义链接格式 name1:value1,name2:value2 <br>2、可修改的值为url对应的值即:designPreviewIndex.jsp <br>3、可附加的参数 parms:activeParm (parms为固定自定义name，activeParm为designPreviewIndex.jsp页面隐藏域id为activeParm的值,如:userid=1&from=1&to=2)
	</div>
	<!-- <div>
		<p>
			<input id="submit" class="button" type="button" value="第一步生成自定义链接" onclick="createlink()" /> <input id="submit" class="button" type="button" value="第二步保存" onclick="savelink()" />
		</p>
	</div> -->
</div>
<div id="dataSetsDic" style="display: none;">
	<table width="100%" cellspacing="5">
		<tr>
			<td>字典类型</td>
			<td>字典信息ID</td>
			<td>字典信息内容</td>
		</tr>
		<tr>
			<td><select name="dicType" id="dicType">
			</select></td>
			<td><select name="dicID" id="dicID">
			</select></td>
			<td><select name="dicText" id="dicText">
			</select></td>
		</tr>
	</table>
</div>
<div id="dataDic" style="display: none;">
	<table width="60%" cellspacing="5" align="center">
		<tr>
			<td width="60">数据集</td>
			<td><select name="dicds" id="dicds">
			</select></td>
		</tr>
		<tr>
			<td>字典类型</td>
			<td valign="top"><input type="text" name="dicTypeVal" id="dicTypeVal" /></td>
		</tr>
	</table>
</div>
<div id="overlapping" style="display: none;">
	<fieldset class="overlap-view">
		<legend>预览</legend>
		<div id="view-border">
			<div id="view-td"></div>
		</div>
	</fieldset>
	<fieldset class="overlap-size">
		<legend>尺寸</legend>
		<div class="">
			<label class="input-label">高</label> <input id="lineHeight" title="请输入高" type="number" required />
		</div>
		<div class="">
			<label class="input-label">宽</label> <input id="lineWidth" title="请输入宽" type="number" required />
		</div>
	</fieldset>
	<fieldset class="overlap-config">
		<div>
			<label class="input-label">斜线数量</label> <select name="line" id="line" required>
				<option value="0">0</option>
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
			</select>
			<button type="button" id="viewOverlap">预览</button>
		</div>
		<div id="overlap-items"></div>
	</fieldset>

	<div class="overlap-items"></div>
</div>
</html>