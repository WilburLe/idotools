<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.web.entity.SystemConfigEmtity"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
SystemConfigEmtity adconfig = (SystemConfigEmtity) request.getAttribute("adconfig");
adconfig = adconfig==null?new SystemConfigEmtity():adconfig;
JSONObject adc = adconfig.getConfig();
adc = adc==null?new JSONObject():adc;
boolean isopen = adc.getBooleanValue("isopen");
int count = adc.getIntValue("count");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>广告配置</title>
</head>
<body>

<form action="<%= basePath%>config/ad/save" method="post">
	是否开启
	<select name="isopen">
		<option value="0">关闭</option>
		<option value="1" <%=isopen?"selected":"" %>>开启</option>
	</select>
	<br />
	间隔数量<input type="text" name="count" value="<%=count%>">
	<br />
	<button type="submit">保存设置</button>
</form>

</body>
</html>