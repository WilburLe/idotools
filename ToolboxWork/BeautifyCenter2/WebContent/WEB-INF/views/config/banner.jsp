<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
AppEnum[] apps = AppEnum.values();
JSONObject bnconfig = (JSONObject) request.getAttribute("config");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<style type="text/css">
 ul{list-style:none;}
 ul li {float: left;}
</style>
</head>
<body>
<ul>
	<%for(AppEnum app : apps) {%>
	<li><a href=""><button><%=app.getCnName() %>-<%=app.getEnName() %></button></a></li>	    
	<%} %>
</ul>
<br />
<button>添加一个Banner</button>
H5

专题 专题 专题

合辑


</body>
</html>