    <%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
    <!DOCTYPE html>
    <html lang="zh-cn">
    <head>
    <meta charset="utf-8">
    <title></title>
    <link rel="stylesheet" href="../lib/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <style type="text/css">
    body{margin:0;padding:0;}
    .ztree li span.button.home_ico_open,.ztree li span.button.home_ico_close{margin-right:2px; background:url(../lib/ztree/css/zTreeStyle/img/diy/1_open.png) no-repeat scroll 0 0 transparent; vertical-align:top;*vertical-align:middle}
    .ztree li span.button.pIcon01_ico_close{margin-right:2px; background:url(../lib/ztree/css/zTreeStyle/img/diy/1_close.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
    </style>
    <script src="../lib/jquery.min.js"></script>
    <script type="text/javascript" src="../lib/ztree/jquery.ztree.min.js"></script>
    <script src="../js/modelTree.js"></script>
    <script type="text/javascript">
    var setting = {
    data: {
    simpleData: {
    enable: true
    }
    },
    callback: {
    onClick: onClick
    }
    };

    var zNodes =[
    { id:1, pId:0, name:"报表主体", open:true, iconSkin:"home"},
    { id:10, pId:1, name:"头标题"},
    { id:20, pId:1, name:"报表头"},
    { id:30, pId:1, name:"数据区"},
    { id:40, pId:1, name:"报表尾"},
    { id:50, pId:1, name:"尾标题"}
    ];
    $(document).ready(function(){
    var tree = $.fn.zTree.init($("#treed"), setting, zNodes);
    });
    function onClick(event, treeId, treeNode, clickFlag){
    if(typeof(treeNode.coords) != "undefined"){
    	var row = treeNode.coords.split(',')[0];
    	var col = treeNode.coords.split(',')[1];
    	parent.rdes.priv.currentBorder.appear([{row:row,col:col}, {row:row,col:col}]);
    	parent.rdes.priv.fillHandle.appear([{row:row,col:col}, {row:row,col:col}]);
    	parent.rdes.priv.selectionBorder.appear([{row:row,col:col}, {row:row,col:col}]);
    	var jsonp = parent.rdes.Handsontable.XML.getCellProperties({start:{row:row,col:col},end:{row:row,col:col}});
    	for(var key in jsonp){
    		parent.rprop.setProperties(key, jsonp[key]);
    	}

    }
    }
    </script>
    </head>
    <body>
    <ul id="treed" class="ztree"></ul>
    </body>
    </html>