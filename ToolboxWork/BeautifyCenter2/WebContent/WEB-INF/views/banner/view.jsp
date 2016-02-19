<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="com.toolbox.web.entity.BannerEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String img_path = ConfigUtility.getInstance().getString("file.server.path");
List<BannerEntity> banners = (List<BannerEntity>) request.getAttribute("banners");
String bannerType = (String) request.getAttribute("bannerType");
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
<script type="text/javascript">
	function addBanner() {
		$("#addForm").show();
		var bt = $("#bannerType  option:selected").val();
		if(bt == "h5") {
			$("#h5url").show();
		} else {
			$("#h5url").hide();
		}
	}
	function delBanner(elementId) {
		if(confirm("确定删除Banner？")) {
			$.get("<%=basePath%>banner/del/"+elementId, {}, function(result) {
				window.location.reload();
			});
		}
	}
</script>
</head>
<body>
<button onclick="addBanner()">添加Banner</button>
<br />
<form action="<%=basePath%>banner/add" method="post" enctype="multipart/form-data" style="display: none;" id="addForm">
<table>
	<tr>
		<td>选择封面</td>
		<td><input type="file" name="cover"></td>
	</tr>
	<tr>
		<td>Title</td>
		<td><input type="text" name="title"></td>
	</tr>
	<tr>
		<td>Intro</td>
		<td>
			<textarea rows="3" cols="13" name="intro"></textarea>
		</td>
	</tr>
	<tr id="url">
		<td>链接</td>
		<td><input type="text" name="url"></td>
	</tr>
	<tr>
		<td colspan="2"><button type="submit">保存</button></td>
	</tr>
</table>
</form>
<br />
<ul>
<%for(int i=0; i<banners.size(); i++) {
    	BannerEntity banner = banners.get(i);%>
    	<li>
			<table>
				<tr>
					<td>
						<img alt="封面" src="<%=img_path%><%=banner.getPreviewImageUrl()%>" width="330" height="292">
					</td>
				</tr>
				<tr>
					<td>ID:<%=banner.getElementId() %></td>
				</tr>
				<tr>
					<td>Title: <%=banner.getTitle() %></td>
				</tr>
				<tr>
					<td>
						<a href="javascript:delBanner('<%=banner.getElementId()%>')">删除</a>
						<a href="<%=basePath%>banner/edit/<%=banner.getElementId()%>">编辑</a>
					</td>
				</tr>						
			</table>			    	
    	</li>
<%}%>
</ul>

</body>
</html>