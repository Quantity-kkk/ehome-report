<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html> 
 <head>  
  <meta http-equiv="content-type" content="text/html; charset=utf-8">  
 </head>  
  
 <body>  
    <script type="text/javascript">  
    	var url = location.search;
    	url = url.substring(url.indexOf("?")+1);
        parent.window.ifmreport.ifmreport.getSearch("1","",url);
    </script>  
 </body>  
</html>  