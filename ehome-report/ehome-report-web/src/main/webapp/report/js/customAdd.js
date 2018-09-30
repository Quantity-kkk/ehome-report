//定义全局变量
var divs = ["condDiv", "styleDiv", "orderDiv", "nameDiv","createDiv"];
var curDiv = "condDiv";
var ifGroupRpt = false;
var ifCreate = false;
var errmsg = "";
var colDataArr;
var tabDataArr;
var curCol;
var tableName;
/***********************工具操作方法 开始*************************/
//根据ID取对象
function getObjById(elemid){
	return document.getElementById(elemid);
}

//添加新选项
function addOption(selectObj,opText,opValue){
	selectObj.add(new Option(opText, opValue));
}

//复制选项
function copyOption(fromObj,fromIndex,toObj){
	addOption(toObj,fromObj.options[fromIndex].text,options[fromIndex].value);
}

//删除选项
function deleteOption(selectObj,opindex){
	selectObj.options[opindex] = null;
}

//查询列字段详细信息
function indexOfCol(colName){
	var col;
	for (var i = 0; i < colDataArr.length; i++) {
		if (colName == colDataArr[i].col_name) {
			col = colDataArr[i];
			break;
		}
	}
	return col;
}

//查询select标签选项，返回第一个选中的索引值
function indexOption(selectObj,opValue){
	var index = -1;
	for (var i = 0; i < selectObj.options.length; i++) {
		if (opValue == selectObj[i].value) {
			index = i;
			break;
		}
	}
	return index;
}

//选项替换
function replaceOption(obj, f_index, t_index) {
	var text = obj.options[t_index].text;
	var value = obj.options[t_index].value;
	obj.options[t_index].text = obj.options[f_index].text;
	obj.options[t_index].value = obj.options[f_index].value;
	obj.options[f_index].text = text;
	obj.options[f_index].value = value;
}
/***********************工具操作方法 结束*************************/

//设置查询表达式
function setExpress(objstr,value){
	var obj = getObjById(objstr);
	var index = obj.selectedIndex;
	var colObj = indexOfCol(obj.value);
	var opt = obj.options[index];
	var opts = opt.text.split("@");
	if(colObj.col_type == "1" || colObj.col_type == "4"){//如果是字符或者多选，则只适用一个运算符
		opt.text = opts[0]+"@"+value;
	}else if(colObj.col_type == "2" || colObj.col_type == "3"){//如果是数值或者日期，适用两个运算符
		if(opts.length == 1){
			opt.text = opt.text+"@"+value;
		}else if(opts.length == 2){
			if(opts[1] != value){
				opt.text = opt.text+"@"+value;
			}
		}else if(opts.length == 3){
			opt.text = opts[0] +"@" +opts[1] + "@" + value;
		}
	}
}


//取select标签值，分隔符str
function getSelectValues(obj,str){
	var valueStr = "";
	if(obj.options.length === 0){
		return "";
	}
	for (var n = 0; n < obj.options.length; n++) {
		if(typeof(obj[n].value) != "undefined" && obj[n].value.length > 0){
			valueStr += obj[n].value + str;
		}
	}
	return valueStr.substring(0,valueStr.length-1);
}

//将fromID选择的项复制到toID isDel=true,复制后从fromID中删除选择项
function copySelectOptions(fromID, toID,isDel) {
	var fObj = getObjById(fromID);
	if (fObj.options.length === 0) {
		return;
	}
	var tObj = getObjById(toID);
	for (var n = 0; n < fObj.options.length; n++) {
		var s_o = fObj[n];
		if (s_o.selected && indexOption(tObj,s_o.value) <0 ) {
			var st=s_o.text;
			var colname=s_o.getAttribute("name");
			if(fromID=='rpt_sortcolumn'&&toID=='rpt_select_sortcolumn')
				st+='--升序';
			if(fromID=='rpt_select_sortcolumn'&&toID=='rpt_sortcolumn')
				st=st.replace('--升序','').replace('--降序','');
			if(colname!="" && colname!=null && colname!="null"){
				if(s_o.value.indexOf(",")==-1)
					addOption(tObj,st, s_o.value+","+colname);
			}
			else{
				if(s_o.value.indexOf(",")==-1)
					addOption(tObj,st, s_o.value);
			}
		}
	}
	if (isDel) {
		deleteSelectOptions(fromID);
	}
}

function AscDesc(obj){
	if(obj.val().indexOf(' ')==-1){
		obj.val(obj.val()+' desc');
		obj.text(obj.text().replace('--升序','--降序'));
	}else{
		obj.val(obj.val().split(' ')[0]);
		obj.text(obj.text().replace('--降序','--升序'));
	}
}

//将fromID所有的项复制到toID，regexp
function copyAllOptions(fromID, toID) {
	var fObj = getObjById(fromID);
	if (fObj.options.length === 0) {
		return;
	}
	var tObj = getObjById(toID);
	for (var n = 0; n < fObj.options.length; n++) {
		var s_o = fObj[n];
		if (indexOption(tObj,s_o.value) <0 ) {
			addOption(tObj,s_o.text, s_o.value);
		}
	}
}

//有条件的将fromID所有的项复制到toID
function copyAllOptionsExp(fromID, toID,expstr) {
	var fObj = getObjById(fromID);
	if (fObj.options.length === 0) {
		return;
	}
	var tObj = getObjById(toID);
	tObj.options.length=0;
	for (var n = 0; n < fObj.options.length; n++) {
		var s_o = fObj[n];
		if (s_o.value.indexOf(expstr)>=0 && indexOption(tObj,s_o.value) <0 ) {
			addOption(tObj,s_o.text, s_o.value);
		}
	}
}

//删除objID选中的项
function deleteSelectOptions(objID) {
	var obj = getObjById(objID);
	if (obj.options.length === 0) {
		return;
	}
	for (var n = obj.options.length - 1; n >= 0; n--) {
		if (obj[n].selected) {
			obj.options[n] = null;
		}
	}
}

//移动一个单位 direct:up上移 down下移
function moveSelectOptions(objID, direct) {
	var obj = getObjById(objID);
	if (obj.options.length === 0) {
		return;
	}
	if (direct == "up" || direct == "UP") {
		for (var i = 0; i < obj.options.length; i++) {
			if (obj[i].selected && i > 0) {
				replaceOption(obj, i, i - 1);
			}
		}
	}
	if (direct == "down" || direct == "DOWN") {
		for (var n = obj.options.length - 1; n >= 0; n--) {
			if (obj[n].selected && n < (obj.options.length - 1)) {
				replaceOption(obj, n, n + 1);
			}
		}
	}
}

//更换视图
function changeDiv(divID) {
	$('.steplist ol').hide();
	$('.steplist ol.'+divID).show();
	if (divID == curDiv) {
		return;
	}
	for (var n = 0; n < divs.length; n++) {
		if (divs[n] == divID) {
			getObjById(divs[n]).style.display = "block";
			curDiv = divs[n];
		} else {
			getObjById(divs[n]).style.display = "none";
		}
	}	
	if ("styleDiv" == divID) {//列视图
		if(ifGroupRpt){
			//getGroupCol();
		}else{
			//getStyleCol();
		}
	}else if("orderDiv" == divID){//排序
		//getOrderCol();
	}else if("createDiv" == divID){//生成报表
		createSql('reportSql');
	}
}

//拼where条件
function getWhereStr(){
	var wobj = getObjById("selectedCondNames");
	var value,text;
	var str = "";
	for (var n = 0; n < wobj.options.length; n++) {
		value = wobj.options[n].value.toString().split(",")[1];
		text = wobj.options[n].text;
		var texts = text.split("@");
		if(texts.length == 1){
			layer.alert(text+" 未配置运算条件");
			break;
		}else{
			for(var m=1;m<texts.length;m++){
				if(texts[m]=="in"){
					str += "AND " +value + " " + texts[m] + "( ? )";
				}else{
					str += "AND " +value + " " + texts[m] + " ? ";
				}
			}
		}
	}
	return str;
}

//获取where条件字段
function getWhereField(){
	var wobj = getObjById("selectedCondNames");
	var value,text;
	var str = "";
	for (var n = 0; n < wobj.options.length; n++) {
		value = wobj.options[n].value.toString().split(",")[1];
		text = wobj.options[n].text;
		var texts = text.split("@");
		str += value;
		if(texts.length==1){
			str +=  "#=";
		}else{
			for(var m=1;m<texts.length;m++){
				str += "#" + texts[m] ;
			}
		}
		str += "@";
	}
	return str;
}
//拼order by条件
function getOrderStr(){
	var wobj = getObjById("rpt_select_sortcolumn");
	var value;
	var str = " ORDER BY ";
	for (var n = 0; n < wobj.options.length; n++) {
		value = wobj.options[n].value;
		str += value +", ";
	}
	if(str.indexOf(",") < 0){
		return "";
	}
	return str.substring(0,str.length-2);
}

//排序时处理
function copyOrderOptions(fromID,toID,flag){
	var order = getObjById('doa_order').value;
	var fObj = getObjById(fromID);
	if (fObj.options.length === 0) {
		return;
	}
	var tObj = getObjById(toID);
	for (var n = 0; n < fObj.options.length; n++) {
		var s_o = fObj[n];
		if (s_o.selected) {
			if(flag){//正移动
				addOption(tObj,s_o.text, s_o.value +" " + order);
			}else{//反移动
				addOption(tObj,s_o.text, s_o.value.substring(0,s_o.value.indexOf(" ")));
			}
		}
	}
	deleteSelectOptions(fromID);
}


//生成SQL
function createSql(sqlID){
	var tableName = getObjById("tabNames").value;
	var sql = "SELECT ";
	if(ifGroupRpt){
		var colg = getSelectValues(getObjById('rpt_select_column'),",");
		sql += colg;
		sql += ",SUM(BAL)/10000 AS BAL,COUNT(*) AS CNT FROM RPT_XD WHERE 1=1 " ;
		var whstrg = getWhereStr();
		sql += whstrg;
		sql += " GROUP BY "+colg;
	}else{
		var cols = getSelectValues(getObjById('rpt_select_column'),",");
		if(cols === null || cols.length === 0){
			layer.alert("请选择报表样式！");
			cols = "*";
		}
		sql += cols;		
		sql += " FROM "+tableName+" WHERE 1=1 " ;
		var whstr = getWhereStr();
		sql += whstr;
		var orderstr = getOrderStr();
		sql += orderstr;
	}
	getObjById(sqlID).value = sql;
}

//SQL测试
function sqlTest(sql){
	$.ajax({
		url : '../../RptCustomAction_sqlTest.action', 
		type:"post",
		data:{testSql:asencrypt(sql)},
		async: false,
		success : function(data) {
			if(data=="1")
				layer.alert("测试成功");
			else
				layer.alert("测试失败，请联系系统管理员");
		}
	});
}

//SQL测试
function sqlTest(sql,dsname){
	$.ajax({
		url : '../../RptCustomAction_sqlTest.action', 
		type:"post",
		data:{"testSql":asencrypt(sql),"dsname":dsname},
		async: false,
		success : function(data) {
			if(data=="1")
				layer.alert("测试成功");
			else
				layer.alert("测试失败，请联系系统管理员");
		}
	});
}


//监测是否是分组报表
function setGroupReport(value){
	ifGroupRpt = value;
}

function changeRpt(val){
	if(2 == val){
		self.open("group_design.jsp","_self");
	}else if(1 == val){
		self.open("simple_design.jsp","_self");
	}
}