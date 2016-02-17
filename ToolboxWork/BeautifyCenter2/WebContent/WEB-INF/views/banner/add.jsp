<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
AppEnum[] apps = AppEnum.values();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

1. 添加一个H5运营
2. 添加一个专题
3. 添加一个专题合辑

<%for(AppEnum app : apps) {%>
    
<%}%>




</body>
</html>