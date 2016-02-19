<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="com.toolbox.web.entity.BannerEntity"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String img_path = ConfigUtility.getInstance().getString("file.server.path");
BannerEntity banner = (BannerEntity) request.getAttribute("banner");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
</script>
</head>
<body>
<button onclick="javascript:history.back()">返回列表</button>
<form action="<%=basePath%>banner/edit" method="post" enctype="multipart/form-data" >
	<input type="hidden" name="elementId" value="<%=banner.getElementId()%>">
<table>
	<tr>
		<td>ID</td>
		<td><%=banner.getElementId() %></td>
	</tr>
	<tr>
		<td>封面</td>
		<td>
			<img alt="封面" src="<%=img_path%><%=banner.getPreviewImageUrl()%>" width="330" height="292">
			<input type="file" name="cover">
		</td>
	</tr>
	<tr>
		<td>Title</td>
		<td><input type="text" name="title" value="<%=banner.getTitle() %>"></td>
	</tr>
	<tr>
		<td>Intro</td>
		<td>
			<textarea rows="3" cols="13" name="intro"><%=banner.getIntro() %></textarea>
		</td>
	</tr>
	<tr>
		<td>链接</td>
		<td>
		    <input type="text" name="url" value="<%=banner.getUrl()%>">
		</td>
	</tr>
	<tr>
		<td>
			<button type="submit">编辑</button>
		</td>	
	</tr>
</table>		
</form>

</body>
</html>