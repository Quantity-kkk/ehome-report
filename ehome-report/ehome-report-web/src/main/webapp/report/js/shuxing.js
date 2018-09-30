var dataSetsMap={};
$(document).ready(function(){

});
function setv(t,v){
   var coords = parent.rdes.Handsontable.tableStyle(t,v);
    var jsonp = parent.rdes.Handsontable.XML.getCellProperties(coords);
	for(var key in jsonp){
      parent.setProperties(key, jsonp[key]);
    }
}
function setdv(v){
	var val ="";
	if(v.value!="") val="="+$('#ds').val()+"."+v.value;
    parent.rdes.Handsontable.tableStyle(v.name,val);
    $('#keyv',parent.document).val(val);
}
function SynProperties(flag,coords){
    $("#reset").click();
	var jsonp = parent.rdes.Handsontable.XML.getCellProperties(coords);
	for(var key in jsonp){
      setProperties(key, jsonp[key]);
      parent.setProperties(key, jsonp[key]);
    }
	if(flag=='row'){//行属性
		$('.cell').hide();
		$('.col').hide();
		$('.row').show();
	}else if(flag=='col'){//列属性
		$('.row').hide();
		$('.cell').hide();
		$('.col').show();
	}else{//单元格属性
		$('.row').hide();
		$('.col').hide();
		$('.cell').show();
	}
}
function setProperties(name,value){
	var $dom = $("#propertyTable [name='"+name+"']"); 
	if($dom.is('input')&&$dom.prop('type')=="checkbox"){
		if(value==1){
			$dom.prop("checked",true);
		}else{
			$dom.prop("checked",false);
		}
	}else{
		if($dom.attr('id')=="bgcolor"||$dom.prop('id')=="fontColor"){
            $dom.spectrum("set", value);
		}else if($dom.attr('id')=="dsvs"||$dom.attr('id')=="dsvt"){
			if(value.indexOf("=")==0){
				$('#dsvt').hide();
				$('#dsvs').show();
				var arrvalue = value.substring(1,value.length).split('.');
				changeDataSets(arrvalue[0]);
				$('#ds').val(arrvalue[0]);
				$('#dsvs').val(arrvalue[1]);
			}else{
				$('#dsvt').show().val("");
				$('#dsvs').hide();
				$dom.val(value);
			}
		}else{
			$dom.val(value);
		}
	}
}
function initDataSets(){
	$("#ds").empty();
	$('#ds').append("<option value=''></option>"); 
	for(var key in dataSetsMap){
		$("#ds").append("<option value='"+key+"'>"+key+"</option>");   
	}
}
function parseDataSets(dataSetsStr){
	var domParser = new DOMParser();
	xmlStrDoc = domParser.parseFromString(dataSetsStr, "text/xml");
	if(xmlStrDoc.childNodes[0].childNodes.length>0){
		var jsoStr="{";
		for(var r=0;r<xmlStrDoc.childNodes[0].childNodes.length;r++){
			var node = xmlStrDoc.childNodes[0].childNodes[r];
			var name = node.getAttribute("name");
			jsoStr += name+":";
			var fStr="'";
			for(var c=0;c<node.getElementsByTagName("fields")[0].childNodes.length;c++){
				var value = node.getElementsByTagName("fields")[0].childNodes[c].getAttribute("name");
				fStr +=value+",";
			}
			fStr = fStr.substring(0,fStr.length-1);
			jsoStr += fStr+"',";
		}
		jsoStr = jsoStr.substring(0,jsoStr.length-1);
		jsoStr += "}";
		parent.rprop.dataSetsMap=eval("("+jsoStr+")");
		parent.rprop.initDataSets();
	}
}
function changeDataSets(val){
	$("#dsvs").empty();
	$('#dsvs').append("<option value=''></option>"); 
	if(val==""){
		$('#dsvt').show().val("");
		$('#dsvs').hide();
	}else{
		var arr = dataSetsMap[val].split(',');
		for(var i=0;i<arr.length;i++){
			$('#dsvs').append("<option value='"+arr[i]+"'>"+arr[i]+"</option>");   
		}
		$('#dsvt').hide();
		$('#dsvs').show();
	}
}
function changeDataSetx(inpid,selid,val){
	$('#'+selid).empty();
	$('#'+selid).append("<option value=''></option>");
	if(val==""){
		$('#'+inpid).show().val("");
		$('#'+selid).hide();
	}else{
		var arr = dataSetsMap[val].split(',');
		for(var i=0;i<arr.length;i++){
			$('#'+selid).append("<option value='"+arr[i]+"'>"+arr[i]+"</option>");
		}
		$('#'+inpid).hide();
		$('#'+selid).show();
	}
}