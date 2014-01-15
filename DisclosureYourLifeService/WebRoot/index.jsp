<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    This is my JSP page. <br><br>
    <form method="post" action="http://hzh:8080/DisclosureYourLife/Upload.action" name="upload" enctype="multipart/form-data"><p>&nbsp;</p><p>&nbsp;
         <input type="text" name="str">
         <input type="file" name="file"></p><p>&nbsp;
         <input type="submit" name="button1"></p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>
    </form>
  </body>
</html>
