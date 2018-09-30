
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>报表详情预览（已被designpriview代替）</title>
<link href="css/print.css" rel="stylesheet" type="text/css">
<style type="text/css">div.print{margin:50px auto;}</style>
<style type="text/css" media="print">
div {
	display: none;
}

div.print {
	display: block;
	box-shadow: none;
	background-color: transparent;
	border: none;
}

.printTitle {
	display: block;
	color: #0F0E1E
}
</style>

  <script src="../lib/jquery.min.js"></script>
<script language="javascript" >
<%
System.out.println("@@:"+request.getAttribute("jasperPrint"));
%>
//var json = <%=request.getAttribute("jasperPrint")%>;
//alert(json);
	function doPrint(how) {
            myDoc = {
                settings:{
                	topMargin:50,
                	leftMargin:50,
                	bottomMargin:50,
                	rightMargin:50,
                	orientation:1,
                	paperName:'a4'},   // 配置页边距（单位mm），orientation:1为纵向,2为横向，选择a4纸张进行打印
                documents: window.frames["previewReport"].document,
                copyrights: '杰创软件拥有版权  www.jatools.com'  // 版权声明,必须   
            }; 
            if(how == '打印预览...')
           			document.getElementById("jatoolsPrinter").printPreview(myDoc );   // 打印预览
          	else if(how == '打印...')
          	        document.getElementById("jatoolsPrinter").print(myDoc ,true);   // 打印前弹出打印设置对话框
            else 
          	        document.getElementById("jatoolsPrinter").print(myDoc ,false);       // 不弹出对话框打印

        }
	
	function hyperLinkCilck(t,data){
		var tdArr =  $(t).parent().parent().parent().find("td");
		var str = "";
		for(var key in data){
			if(key=="uuid"){
				str +=key+"="+data[key]+"&";
			}else{
				var vl = $($(tdArr[parseInt(data[key])+1]).find('span')[0]).html();
				str +=key+"="+vl+"&";
			}
		}
		str = str.substring(0,str.length-1);
		//window.open("preDes.action?uuid=6d2316aa77c72c2b7657f3f95c0d00f2&id=10001892&type=2");
		window.open("preDes.action?"+str);
	}
</script>
</head>
<body>
<div class="printhead"> 
    <a href="javascript:void(0);" onclick="doPrint('打印...')" class="aPrint" title="打印"></a>
    <a href="javascript:void(0);" onclick="doPrint('打印')" class="aPrint" title="直接打印"></a>
    <a href="javascript:void(0);" onclick="doPrint('打印预览...')" class="aPrint" title="打印预览"></a>
</div>
<div  id="preint" class="print">
<iframe id="previewReport" name="previewReport" width = "100%" height="100%" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"  src="exporter.jsp"></iframe>
</div>
<OBJECT ID="jatoolsPrinter" CLASSID="CLSID:B43D3361-D075-4BE2-87FE-057188254255" 
         codebase="jatoolsPrinter.cab#version=8,6,0,0"></OBJECT>
</body>
</html>