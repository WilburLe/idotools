<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.entity.ToolboxConfigEntity"%>
<%@ page language="java" contentType="text/html; charset=UTF—8" pageEncoding="UTF-8"%>
	
<%
String basePath = WebUtility.getBasePath(request);
JSONArray stats = (JSONArray) request.getAttribute("stats");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF—8">
<title>finance stat</title>
</head>
<body>
<a href="<%=basePath%>mgr/finance">返回配置</a>
<table border="1">
<tr>
	<td>DATE</td>
	<td>uv</td>
	<td>pv</td>
</tr>
<%
for(int i=0; i<stats.size(); i++) {
    JSONObject obj = stats.getJSONObject(i);
    String date = obj.getString("date");
    String val = obj.getString("val");
    JSONObject valJson = JSON.parseObject(val);
    int uv = valJson.getIntValue("uv");
    int pv = valJson.getIntValue("pv");%>
    
    <tr>
    	<td><%=date %></td>
    	<td><%=uv %></td>
    	<td><%=pv %></td>
    </tr>
<%}
%>
</table>
</body>
</html>