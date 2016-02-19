<%@page import="com.toolbox.web.entity.BannerEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="com.toolbox.web.entity.SystemConfigEmtity"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String img_path = ConfigUtility.getInstance().getString("file.server.path");
AppEnum[] apps = AppEnum.values();
SystemConfigEmtity bannerConfig = (SystemConfigEmtity) request.getAttribute("bannerConfig");
List<BannerEntity> banners = (List<BannerEntity>) request.getAttribute("banners");
String appType = (String) request.getAttribute("appType");

//JSONObject 
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
function searchContent() {
	var elementId = $("#sea_elementId").val();
	$.get("<%=basePath%>banner/find/"+elementId, {}, function(banner) {
		if(!banner) {
			alert("没有找到");
			return;
		}
		//$.parseJSON("{" + result + "}");
		var html = "<img src='<%=img_path%>"+banner.previewImageUrl+"' width='330' height='292'>";
		$("#searchContent").show();
		$("#searchInfoContent").html(html);
		
	});
}
function addBanner(appType) {
	var bannerId = $("#sea_elementId").val();
	$.post("<%=basePath%>config/banner/add/", {"appType":appType, "bannerId":bannerId}, function(result) {
		window.location.reload();
	});
}
function delBanner(appType, bannerId) {
	if(confirm("确定删除么？")) {
		$.get("<%=basePath%>config/banner/del/"+appType+"/"+bannerId, {}, function(result) {
			window.location.reload();
		});
	}
}
</script>
</head>
<body>
<table>
	<tr>
		<td>
<ul>
	<li>APP类型</li>
	<%for(AppEnum app : apps) {%>
	<li><a href="<%=basePath %>config/banner/<%=app.getCollection()%>">
			<button style="background-color: <%=app.getCollection().equals(appType)?"green":""%>">
				<%=app.getCnName() %>-<%=app.getEnName() %></button></a></li>	    
	<%} %>
</ul>
		</td>
	</tr>
	<tr>
		<td>
		 Banner ID:<input type="text" name="sea_elementId" id="sea_elementId">
		    <a href="javascript:searchContent()">查找</a>
		   	 <div id="searchContent" style="display: none">
		   	 	<div id="searchInfoContent"></div>
		   	 	<button onclick="addBanner('<%=appType%>')">添加</button>
		   	 </div>
		</td>
	</tr>
</table>

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
					<a href="javascript:delBanner('<%=appType%>', '<%=banner.getElementId()%>')">删除</a>
					<a href="<%=basePath%>banner/edit/<%=banner.getElementId()%>">详情</a>
				</td>
			</tr>						
		</table>		
	</li>    
<%} %>
</ul>

</body>
</html>