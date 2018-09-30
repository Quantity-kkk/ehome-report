var rdpConfig = function(){
	var config = {};
	$.getJSON("common/file/config.json",function(data){
		config = data;
	});
	return {
		getConfig:function(){
			return config;
		},
		getConfigParm:function(name){
			return config[name];
		}
	}
}();



$(function(){
	var dataSetPos = rdpConfig.getConfigParm("dataSet");
	if(dataSetPos=="left"){
		$(".tb-foot").addClass("tb-foot_left");
	}else{
		
	}
	
});