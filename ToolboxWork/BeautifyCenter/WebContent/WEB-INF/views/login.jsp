<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<form action="<%=basePath%>login" method="post">
	name:<input type="text" name="name"><br/>
	pass:<input type="password" name="password"><br/>
	<button type="submit">LOGIN</button>
</form>
</body>
</html>