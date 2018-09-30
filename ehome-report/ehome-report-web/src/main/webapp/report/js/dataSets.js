var setting = {
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {

	}
};
var tree;
var dataMap={};
var sDataSetsName=null;
var state="add";
$(document).ready(function() {
    //$('#dsform').validate();
	var dataXml = parent.rdes.document.getElementById("dataSetsModel").value;
	//$("button.add").button({icons: {primary: "ui-icon-plus"}});
    //$("button.edit").button({icons: {primary: "ui-icon-pencil"}});
    //$("button.del").button({icons: {primary: "ui-icon-minus"}});
    $('#dslist').on('change',function(){
		sDataSetsName = this.value;
	});
    $("#DataSourceName").empty();
    $("#DataSourceName").append('<option value=""></option><option value="javabean">javabean</option>');
	$.ajax({
		url : "../../getDataSourceNameAll.action", 
		type:"post",
		success : function(data) {
			for(var i=0;i<data.length;i++){
				$("#DataSourceName").append("<option value='"+data[i].dataSourceName+"'>"+data[i].dataSourceName+"</option>"); 
			}
		}
	});
    if (dataXml == ""){
		 tree = $.fn.zTree.init($("#treeds"), setting, []);
	}else{
//		 $('input.submit').button();
		initTree(dataXml);
	}
});
function initTree(dataXml){
	dataXml = dataXml.trim();
	$("#dslist").empty();
	$('#dslist').append("<option value=''>请选择数据集</option>"); 
	var domParser = new DOMParser();
	var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
//	if(dataSets.childNodes.length!=2) return;
	var zNodes = new Array();
	var dataSetsCount = xmlStrDoc.childNodes[0].childNodes.length;
	if(dataSetsCount>0){
		zNodes.push({ id:1, pId:0, name:"数据集列表", open:true, iconSkin:"home"});
	}
	for(var i=0;i<dataSetsCount;i++){
		var dataSets = xmlStrDoc.childNodes[0].childNodes[i];
		var id= i+1;
		var dataSetName = dataSets.getAttribute('name');
		$("#dslist").append("<option value='"+dataSetName+"'>"+dataSetName+"</option>");   
		zNodes.push({ id:id+'0', pId:1, name:dataSetName});
		zNodes.push({ id:id+'01', pId:id+'0', name:"字段"});
		zNodes.push({ id:id+'02', pId:id+'0', name:"参数"});
		if(dataSets.getElementsByTagName("fields")[0].childNodes.length!=0){
			var field = dataSets.getElementsByTagName("fields")[0].childNodes;
			for(var x=0;x<field.length;x++){
				var dd = x+1;
				var name = field[x].childNodes[0].childNodes[0].nodeValue;
				var type = field[x].childNodes[1].childNodes[0].nodeValue;
				zNodes.push({ id:id+'010'+dd, pId:id+'01', name:name+"("+type+")"});
			}
			
		}
		var Parameters = dataSets.getElementsByTagName("query")[0].childNodes[2].childNodes;
		var dataSourceName = dataSets.getElementsByTagName("query")[0].childNodes[0].childNodes[0].nodeValue;
		var CommandText = dataSets.getElementsByTagName("query")[0].childNodes[1].childNodes[0].nodeValue;
		var Parameter = {};
		for(var j=0;j<Parameters.length;j++){
			var d= j+1;
			//zNodes.push({ id:id+'020'+d, pId:id+'02', name:Parameters[j].getAttribute('Name')+"("+Parameters[j].getAttribute('DbType')+")"});
			zNodes.push({ id:id+'020'+d, pId:id+'02', name:Parameters[j].getAttribute('name')});
			Parameter[j] = Parameters[j].getAttribute('name');
		}
		dataMap[dataSets.getAttribute('name')] = {
				dataSetName:dataSetName,
				dataSourceName:dataSourceName,
				dd:dataSets.getAttribute('isdic'),
				CommandText:CommandText,
				Parameter:Parameter
		};
	}
	//$('#dslist').selectmenu('refresh', true);
	 tree = $.fn.zTree.init($("#treeds"), setting, zNodes);
	 initDataSetsSelect();
	 
}
function initDataSetsSelect(){
	parent.rprop.parseDataSets(parent.rdes.document.getElementById("dataSetsModel").value);
}
function dataSetsOption(type){
	var dataSetsMap={};
	var flag=1;
	var flag1=1;
	var dataSetsModel = parent.rdes.document.getElementById("dataSetsModel").value;
	switch (type) {
	case "add":
		$('#reset').click();
		$('#DataSetName').attr('readonly',false);
		$('.newp').remove();
		$('.right').show();
		state="add";
		break;
	case "edit":
		$('#reset').click();
		$('.newp').remove();
		var dsname=dataMap[sDataSetsName].dataSourceName;
		//$('#DataSourceName').empty();
		if(sDataSetsName!=""&&sDataSetsName!=null){
			$('#DataSetName').attr('readonly',true);
			$('#DataSetName').val(dataMap[sDataSetsName].dataSetName);
			$('#DataSourceName').val(dsname);
			if(dataMap[sDataSetsName].dd=="true"){
				$('#isdd').prop("checked",true);
			}else{
				$('#isdd').prop("checked",false);}
			sw(dsname);
			var cmdtest=dataMap[sDataSetsName].CommandText;
			if(dsname=='javabean'){
				if(cmdtest.indexOf('|')!=-1){
					$('#FactoryClass').val(cmdtest.split('|')[0]);
					$('#StaticMethod').val(cmdtest.split('|')[1]);
				}
			}else{				
			$('#CommandText').val(cmdtest.replace(/&quot;/g, "\"").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&amp;/g, "&"));
			var Parameter = dataMap[sDataSetsName].Parameter;
			for(var k in Parameter){
				addparm(k,Parameter[k]);
			}
			}
		}
		$('.right').show();
		state="edit";
		break;
	case "del":
		state="del";
		parsingToXML(dataSetsMap,state);
		initTree(parent.rdes.document.getElementById("dataSetsModel").value);
		$('.right').hide();
		break;
	case "submit":
		var dataSetName = $('#DataSetName').val();
		if(dataSetName==''){
			layer.alert('请选择数据源');
			break;
		}
		var dataSourceName = $('#DataSourceName').val();
		var SQLText = '';
		if(dataSourceName=='javabean'){
			SQLText = $('#FactoryClass').val()+"|"+$('#StaticMethod').val();
			//SQLText = $('#FactoryClass').val()+"|createBeanCollection";
		}else{
			SQLText = $('#CommandText').val();
		}
		var dd = $('#isdd').is(':checked');
		var isvalid = $('#isvalid').is(':checked');
		var txtp = new Array();
		$('#parmlist').find('.selp').each(function(){
			txtp.push(this.value);
		});
		dataSetsMap.dataSetName = dataSetName;
		dataSetsMap.dataSourceName = dataSourceName;
		dataSetsMap.CommandText = SQLText;
		dataSetsMap.dd=dd;
		dataSetsMap.Parameters = {
				txtp:txtp
		};
		
		//$('.right').hide();
		if(dd&&isvalid){
			$.ajax({
				url : "../../selectDic.action", 
				type:"post",
				async:false,
				data:{
					SQLText:SQLText,
					dataSourceName:dataSourceName
				},
				success : function(data) {
//					if(parent.dicData[dataSetName]==null){
//						parent.dicData[dataSetName] = {};
//					}
//					var dicData = parent.dicData[dataSetName]
					if(data=='null'){
						$('.right').show();
						flag=0;
						layer.alert("sql错误");
					}else{
						data = eval("("+data+")");
						data = eval("("+data+")");
						parent.rdes.dicData[dataSetName] = data;
					}
				}
			});
		}
		if(flag==0){
			break;
		}
		parsingToXML(dataSetsMap,state);
		var dsm=parent.rdes.document.getElementById("dataSetsModel").value;
		if(isvalid){
		$.ajax({
			url : "../../parFields.action", 
			type:"post",
			async:false,
			data:{
				dataSetsModel:dsm,
				dataSetName:dataSetName
			},
			success : function(data) {
				if(data =="null"||data==""||data.length<3){
					layer.alert("SQL错误");
					parent.rdes.document.getElementById("dataSetsModel").value = dataSetsModel;	
					flag1=0;
				}else{
					$('.right').hide();layer.msg('校验通过，保存成功', {time:2000});
					parent.rdes.document.getElementById("dataSetsModel").value = eval("("+data+")");
				}
			},
			error:function(){layer.alert("SQL错误");
			parent.rdes.document.getElementById("dataSetsModel").value = dataSetsModel;	
			flag1=0;},
			beforeSend:function(){layer.msg('SQL校验中……', {icon: 16});},
			complete:function(){layer.closeAll('loading');}
		});
		}
		if(flag1==0){
			break;
		}
		initTree(parent.rdes.document.getElementById("dataSetsModel").value);
		if(dd)
			if(flag=1){
				parent.showdialog('字段选择','Data_Dic_Selectfield.jsp?dataSetName='+dataSetName);
			}
		$('#reset').click();
		$('.newp').remove();
		break;
	default:
		break;
	}
}

function parsingToXML(dataSetsMap,state){
	var data ="";
	var dataXml = parent.rdes.document.getElementById("dataSetsModel").value;
	if (dataXml == ""){
		data +="<datasets><dataset name='"+dataSetsMap.dataSetName+"' isdic='"+dataSetsMap.dd+"'>";
		data +="<fields></fields>";
		data +="<query><datasourcename>"+dataSetsMap.dataSourceName+"</datasourcename>";
		data +="<commandtext>"+dataSetsMap.CommandText.replace(/\r\n/g," ").replace(/\n/g," ").replace(/\r/g," ").replace(/\t/g," ").replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;").replace(/\"/g, "&quot;").replace(/   */g," ")+"</commandtext>";
		data +="<parameters>";
		var txtp = dataSetsMap.Parameters.txtp;
		for(var i=0;i<txtp.length;i++){
			data +="<parameter name='"+txtp[i]+"' />";
		}
		data +="</parameters></query></dataset></datasets>";
		parent.rdes.document.getElementById("dataSetsModel").value = data;
	}else{
		if(state=="add"){
			var domParser = new DOMParser();
			var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
			data +="<datasets>";
			for(var z=0;z<xmlStrDoc.childNodes[0].childNodes.length;z++){
//				alert(xmlStrDoc.childNodes[0].childNodes[z].toString())
				data +=xmlStrDoc.childNodes[0].childNodes[z].outerHTML;
			}
			data +="<dataset name='"+dataSetsMap.dataSetName+"' isdic='"+dataSetsMap.dd+"'>";
			data +="<fields></fields>";
			data +="<query><datasourcename>"+dataSetsMap.dataSourceName+"</datasourcename>";
			data +="<commandtext>"+dataSetsMap.CommandText.replace(/\r\n/g," ").replace(/\n/g," ").replace(/\r/g," ").replace(/\t/g," ").replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;").replace(/\"/g, "&quot;").replace(/   */g," ")+"</commandtext>";
			data +="<parameters>";
			var txtp = dataSetsMap.Parameters.txtp;
			for(var i=0;i<txtp.length;i++){
				data +="<parameter name='"+txtp[i]+"' />";
			}
			data +="</parameters></query></dataset>";
			data +="</datasets>";
			parent.rdes.document.getElementById("dataSetsModel").value = data;
		}else if(state=="edit"){
			var domParser = new DOMParser();
			var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
			for(var i=0;i<xmlStrDoc.childNodes[0].childNodes.length;i++){
				if(xmlStrDoc.childNodes[0].childNodes[i].getAttribute('name')==dataSetsMap.dataSetName){
					xmlStrDoc.childNodes[0].childNodes[i].setAttribute("isdic",dataSetsMap.dd);
					xmlStrDoc.childNodes[0].childNodes[i].getElementsByTagName("query")[0].childNodes[0].childNodes[0].nodeValue=dataSetsMap.dataSourceName;
					xmlStrDoc.childNodes[0].childNodes[i].getElementsByTagName("query")[0].childNodes[1].childNodes[0].nodeValue = dataSetsMap.CommandText.replace(/\r\n/g," ").replace(/\n/g," ").replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;").replace(/\"/g, "&quot;");
					var data ="<parameters>";
					var txtp = dataSetsMap.Parameters.txtp;
					for(var j=0;j<txtp.length;j++){
						data +="<parameter name='"+txtp[j]+"' />";
					}
					data +="</parameters>";
					var xmlP = domParser.parseFromString(data, "text/xml");
					xmlStrDoc.childNodes[0].childNodes[i].getElementsByTagName("query")[0].childNodes[2].outerHTML = xmlP.childNodes[0].outerHTML;
					parent.rdes.document.getElementById("dataSetsModel").value = xmlStrDoc.childNodes[0].outerHTML;
					break;
				}
			}
		}else if(state=="del"){
			var domParser = new DOMParser();
			var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
			for(var i=0;i<xmlStrDoc.childNodes[0].childNodes.length;i++){
				if(xmlStrDoc.childNodes[0].childNodes[i].getAttribute('name')==sDataSetsName){
					xmlStrDoc.childNodes[0].childNodes[i].outerHTML="";
					parent.rdes.document.getElementById("dataSetsModel").value = xmlStrDoc.childNodes[0].outerHTML;
					break;
				}
			}
		}
		sDataSetsName = "";
	}
}
function addparm(p,v){
    var n=Math.floor(Math.random()*1000);
    var vl = parent.rdes.document.getElementById('parmsModel').value;
    var parms=$(vl);
    var parml='';
    parms.find('parm').each(function(){
        parml+='<option value="'+$(this).attr("name")+'"'+($(this).attr("name")==v?" selected":"")+'>'+$(this).attr("name")+'</option>';
    });
    var str='<p class="newp"><label class="p">参数</label>'+
            '<select name="Name" id="Name'+n+'" class="selp">'+
            '<option value="">请选择参数</option>'+parml+
            '</select>'+
            '<input type="button" class="padd" onclick="$(this).parent().append($(this).parent().prop(\'outerHTML\'))" /><input type="button" class="pdel" onclick="$(this).parent(\'p\').detach();" />'+
            '</p>';
    $('#parmlist').append(str);
}
function sw(v){
	if(v=='javabean'){
		$('#ddslist').hide();$('#jblist').show();
	}else{
		$('#ddslist').show();$('#jblist').hide();
	}
}