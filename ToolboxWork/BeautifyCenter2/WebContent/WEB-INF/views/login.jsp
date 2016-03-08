<%@page import="com.toolbox.framework.utils.StringUtility"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String msg = (String) request.getAttribute("msg");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>static/table.css">
</head>
<body>

	<div align="center" style="font-style: red; height: 20px;"><%= StringUtility.isNotEmpty(msg)?msg+"<br />":"" %></div>
	<form action="<%=basePath%>login" method="post">
	  <table id="customers" align="center" width="300px">
	  	<tr>
			<th>用户</th>
			<td><input type="text" name="name"></td>  	
	  	</tr>
	  	<tr>
			<th>密码</th>
			<td><input type="password" name="password"></td>  	
	  	</tr>
	  	<tr>
			<td colspan="2"><input type="submit" value="登录"></td>  	
	  	</tr>
	  </table>
	</form>
</body>
</html>