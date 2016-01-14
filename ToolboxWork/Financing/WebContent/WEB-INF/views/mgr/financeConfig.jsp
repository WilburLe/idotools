<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.entity.ToolboxConfigEntity"%>
<%@ page language="java" contentType="text/html; charset=UTF—8" pageEncoding="UTF-8"%>
	
<%
String basePath = WebUtility.getBasePath(request);
ToolboxConfigEntity tbconfig = (ToolboxConfigEntity) request.getAttribute("config");

JSONObject content = JSONObject.parseObject(tbconfig.getContent());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF—8">
<title>finance config</title>
<script type="text/javascript" src="<%=basePath%>static/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript">
$(function() {
	$('#content_open').click(function() {
		var val = $(this).val();
		if(val=='true') {
			$('#content_open').val(false);
		} else {
			$('#content_open').val(true);
		}
	});
})
</script>
</head>
<body>

<a href="<%=basePath %>mgr/stat/10">查看统计</a>
<form action="<%=basePath%>mgr/finance" method="post">
<table border="1">
	<tr>
		<td>是否开启</td>
		<td>
			<%
			Boolean openk = content.getBoolean("open");
			boolean open = openk!=null?content.getBooleanValue("open"):false;
			%>
			<input type="checkbox" name="content_open" id="content_open" <%=open?"checked":"" %> value="<%=open%>">
		</td>
	</tr>
	<tr>
		<td>广告链接</td>
		<td><input type="text" name="content_adurl" value="<%=content.getString("adurl") %>"></td>
	</tr>
	<tr>
		<td colspan="2"><button type="submit">更新</button> </td>
	</tr>
</table>
</form>
</body>
</html>