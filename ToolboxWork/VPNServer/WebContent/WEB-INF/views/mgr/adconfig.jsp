<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basepath = WebUtility.getBasePath(request);
JSONObject result = (JSONObject) request.getAttribute("result");
boolean idotoolsad = result.containsKey("idotoolsad")?result.getBooleanValue("idotoolsad"):false;
%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="<%=basepath %>mgr/adconfig" method="post">
	是否开启小工具广告：
	<select name="idotoolsad">
		<option value="0">关闭</option>
		<option value="1" <%=idotoolsad?"selected":"" %>>开启</option>
	</select>
	<button type="submit">保存</button>
</form>

</body>
</html>