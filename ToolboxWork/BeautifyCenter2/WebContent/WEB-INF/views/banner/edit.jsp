<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.common.BannerEnum"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="com.toolbox.web.entity.BannerEntity"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String img_path = ConfigUtility.getInstance().getString("file.server.path");
BannerEntity banner = (BannerEntity) request.getAttribute("banner");
AppEnum[] apps = AppEnum.values();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
 ul{list-style:none;}
 ul li {float: left;}
</style>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script type="text/javascript">
function getContent(appType, elementIds) {
	console.log("apptype >>> "+apptype);
	console.log("elementIds >>> "+elementIds);
	//$.post("<%=basePath%>banner/contents", {"appType":appType, "elementIds":elementIds}, function(result) {
		
	//});
}
function searchContent() {
	var elementId = $("#sea_elementId").val();
	var appType = $("#sea_appType").val();
	$.post("<%=basePath%>banner/content", {"appType":appType, "elementId":elementId}, function(result) {
		if(!result) {
			alert("没有找到");
			return;
		}
		//$.parseJSON("{" + result + "}");
		var app = eval('('+result+ ')');
		var html = "<img src='<%=img_path%>"+app.previewImageUrl+"' width='330' height='292'>";
		$("#searchContent").show();
		$("#searchInfoContent").html(html);
		$("#sea_previewImageUrl").val(app.previewImageUrl);
	});
}
function bannerDelApp(bannerId, appId) {
	if(confirm("确定删除么？")) {
		$.get("<%=basePath%>banner/del/app/"+bannerId+"/"+appId, {}, function(result) {
			window.location.reload();
		});
	}
}
</script>
</head>
<body>
<button onclick="javascript:history.back()">返回列表</button>
<form action="<%=basePath%>banner/edit" method="post" enctype="multipart/form-data" >
	<input type="hidden" name="bannerId" value="<%=banner.getElementId()%>">
	<input type="hidden" name="previewImageUrl" id="sea_previewImageUrl">
	
<table>
	<tr>
		<td>ID</td>
		<td><%=banner.getElementId() %></td>
	</tr>
	<tr>
		<td>分类</td>
		<td>
			<%=BannerEnum.byType(banner.getBannerType()).getName() %>
		</td>
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
		<td colspan="2">
		<%JSONObject content = banner.getContent();
			if(banner.getBannerType().equals(BannerEnum.H5.getType())) {%>
		    H5 URL : <input type="text" name="h5url" value="<%=content!=null?content.getString("url"):""%>">
		<%} %>
		<%if(banner.getBannerType().equals(BannerEnum.Subject.getType())) {
				String appType = content!=null?content.getString("appType"):null;
				%>
		    ID:<input type="text" name="elementId" id="sea_elementId">
		    <select name="appType" id="sea_appType">
    		<%if(appType != null) {%>
				<option value="<%=appType%>"><%=AppEnum.byCollection(appType).getCnName() %>-<%=AppEnum.byCollection(appType).getEnName() %></option>
    		<%} else {%>
		    	<%for(AppEnum app : apps) {%>
		    	<option value="<%=app.getCollection()%>"><%=app.getCnName() %>-<%=app.getEnName() %></option>
	    		<%}%>
	    	<%} %>
		    </select>
		    <a href="javascript:searchContent()">查找</a>
		   	 <div id="searchContent" style="display: none">
		   	 	<div id="searchInfoContent"></div>
		   	 </div>
		<%} %>
		<%if(banner.getBannerType().equals(BannerEnum.SubjectGroup.getType())) {
				%>
			ID:<input type="text" name="elementId" id="sea_elementId">
		    <select name="appType" id="sea_appType">
		    	<option value="subject">专题</option>
		    </select>
		    <a href="javascript:searchContent()">查找</a>
		   	<br />
		   	 <div id="searchContent" style="display: none">
		   	 	<div id="searchInfoContent"></div>
		   	 </div>
		<%} %>
		</td>
	</tr>
	<tr>
		<td>
			<button type="submit">编辑</button>
		</td>	
	</tr>
</table>		
</form>
<hr />
<ul>
<%if(!banner.getBannerType().equals(BannerEnum.H5.getType())) {
    	JSONArray conApps = content.containsKey("apps") ? content.getJSONArray("apps") : new JSONArray();
    	for(int i=0; i<conApps.size(); i++) {
    	    JSONObject conApp = conApps.getJSONObject(i);%>
	<li>
		<img alt="封面" src="<%=img_path+conApp.getString("previewImageUrl")%>" width="330" height="292">
		<br />
		ID:<%=conApp.getString("elementId") %>
		<br />
		<a href="javascript:bannerDelApp('<%=banner.getElementId() %>', '<%=conApp.getString("elementId") %>')">删除</a>
	</li>    	    
    	<%}
	}%>
</ul>

</body>
</html>