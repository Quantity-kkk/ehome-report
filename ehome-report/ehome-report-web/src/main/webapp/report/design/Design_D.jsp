    <%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
        <!doctype html>
        <html lang="zh-cn">
        <head>
        <meta charset="utf-8">
        <title></title>
        <link rel="stylesheet" href="../css/jquery.handsontable.css">
        <link rel="stylesheet" href="../css/jquery.contextMenu.css">
        <script src="../lib/jquery.1.7.2min.js"></script>
        <script type="text/javascript" src="../lib/layer/layer.js"></script>
        <script src="../lib/aes.js"></script>
        <script src="../lib/tbl/jquery.handsontable.js"></script>
        <script src="../lib/tbl/jquery.contextMenu.js"></script>
        
        <script>
        function initDesign(){//init-1839
        var colnum = window.screen.width/59+1;
        if(colnum >0)
        	colnum = parseInt(colnum);
        $('#demo').handsontable({//setting-2300,keepEmptyRows-498,selection-822
        rows: 20,
        cols: colnum,
        rowHeaders: false, //turn off 1, 2, 3, ...
        // colHeaders: ["Car", "Year", "Chassis color", "Bumper color"],
        colHeaders: true,
        minSpareCols: 0,//最小显示
        minSpareRows: 0,
        contextMenu: true//handsontable.js-315行
        });
       /*  var data2 = [
        {a:"00", b:"01", c:"02", d:"03", e:"04",f:"05",g:"06", h:"07", i:"08", j:"09"},
        {a:"10", b:"11", c:"12", d:"13", e:"14",f:"15",g:"16", h:"17", i:"18", j:"19"},
        {a:"20", b:"21", c:"22", d:"23", e:"24",f:"25",g:"26", h:"27", i:"28", j:"29"},
        {a:"30", b:"31", c:"32", d:"33", e:"34",f:"35",g:"36", h:"37", i:"38", j:"39"},
        {a:"40", b:"41", c:"42", d:"43", e:"44",f:"45",g:"46", h:"47", i:"48", j:"49"},
        {a:"50", b:"51", c:"52", d:"53", e:"54",f:"55",g:"56", h:"57", i:"58", j:"59"},
        {a:"60", b:"61", c:"62", d:"63", e:"64",f:"65",g:"66", h:"67", i:"68", j:"69"},
        {a:"70", b:"71", c:"72", d:"73", e:"74",f:"75",g:"76", h:"77", i:"78", j:"79"},
        {a:"80", b:"81", c:"82", d:"83", e:"84",f:"85",g:"86", h:"87", i:"88", j:"89"},
        {a:"90", b:"91", c:"92", d:"93", e:"94",f:"95",g:"96", h:"97", i:"98", j:"99"}
        ];
        $("#demo").handsontable("loadData", data2); */
        
        
        
        $('#textColor').bind('click',function(){
            Handsontable.tableStyle('color','red');
            });
        $('#textBold').bind('click',function(){
            Handsontable.tableStyle('bold',null);
            });
        $('#textUnderline').bind('click',function(){
            Handsontable.tableStyle('underline',null);
            });
        $('#saveXML').bind('click',function(){
        	Handsontable.XML.saveXML();
        });
        $('#openXML').bind('click',function(){
        	Handsontable.XML.openXML();
        });
        $('#updateXML').bind('click',function(){
        	Handsontable.XML.updateXML();
        });
        
        Handsontable.XML.openXML();
        
        $('body').append("<div style='position: absolute;left:753px;top:0px;width:1px;height:10000px;z-index:-1;border-left:dotted 2px red; background:#FFF'></div>");
        }
        function getTM(){
            var str=$('#tableModel').val();
            return str;
        }
        function getDS(){
            return $('#dataSetsModel').val();
        }
        function RunOnBeforeUnload() {window.onbeforeunload = function(){ return ""; } }
        
        function loadData(data){
        	// $("#demo").handsontable("loadData", data);
        	 $("#tableModel").val(data);
        	 Handsontable.XML.importExcel();
        }
        </script>
        <style type="text/css">
        html,body{padding: 0px; margin: 0px;height: 100%;overflow:hidden;z-index: -1}
        </style>
        </head>

        <body>
        <div id="demo" class="dataTable" style="width: 100%;height: 100%;overflow: auto;"></div>
        <!-- <input type="text" id="XMLName" value="11">
        <button id="saveXML">保存XML</button>
        <button id="openXML">打开XML</button>
        <button id="updateXML">修改XML</button> -->
        <textarea id ="tableModel" style="display:none;"></textarea>
        <textarea id ="dataSetsModel" style="display:none;"></textarea>
        <textarea id ="parmsModel" style="display:none;"></textarea>
        <textarea id ="parmsExtModel" style="display:none;"></textarea>
        <textarea id ="fieldModel" style="display:none;"></textarea>
       <!--  <textarea id ="XMLModel" style="display:none;"></textarea> -->
        </body>

        </html>