<%@page import="com.toolbox.common.LanguageEnum"%>
<%@page import="java.util.Map"%>
<%@page import="com.toolbox.web.entity.BannerContentEntity"%>
<%@page import="com.toolbox.common.BannerEnum"%>
<%@page import="com.toolbox.web.entity.BannerEntity"%>
<%@page import="com.toolbox.framework.utils.StringUtility"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.common.SystemConfigEnum"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String img_path = ConfigUtility.getInstance().getString("file.server.path");
BannerEntity banner = (BannerEntity) request.getAttribute("banner");
List<BannerContentEntity> contents = (List<BannerContentEntity>) request.getAttribute("contents");
Map<String, JSONObject> contentMap = (Map<String, JSONObject>) request.getAttribute("contentMap");
AppEnum[] apps = AppEnum.values();


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
	var appType = $("#appType option:selected").val();
	var elementId = $("#sea_elementId").val();
	$.post("<%=basePath%>banner/search", {"appType":appType, "elementId":elementId}, function(banner) {
		if(!banner) {
			alert("没有找到");
			return;
		}
		//$.parseJSON("{" + result + "}");
		var data = eval('('+banner+')');
		var html = "<img src='<%=img_path%>"+data.previewImageUrl+"' width='330' height='292'>";
		$("#searchContent").show();
		$("#searchInfoContent").html(html);
	});
}
function delBannerApp(bannerId, appId) {
	if(confirm("确定删除么？")) {
		$.post("<%=basePath%>banner/app/del", {"bannerId":bannerId, "appId":appId}, function(banner) {
			window.location.reload();
		});
	}
}
</script>
</head>
<body>
<button onclick="javascript:history.back()">返回列表</button><br />

<form action="<%=basePath%>banner/edit" method="post" enctype="multipart/form-data">
<input type="hidden" name="elementId" value="<%=banner.getElementId()%>">
<table border="1">
	<tr>
		<td>类型</td>
		<td><%=BannerEnum.byType(banner.getBannerType()).getName() %></td>
	</tr>
	<tr>
		<td>ID</td>
		<td>	<%=banner.getElementId()%></td>
	</tr>
	<tr>
		<td>选择封面</td>
		<td>
			国内:<img alt="封面" src="<%=img_path+banner.getPreviewImageUrl()%>" width="330" height="292">
			<input type="file" name="cover">
			<br />
			googleplay:<img alt="封面" src="<%=img_path+banner.getEnPreviewImageUrl()%>" width="330" height="292">
			<input type="file" name="encover">
		</td>
	</tr>
	<tr>
		<td>中文描述</td>
		<td><input type="text" name="cnTitle" value="<%=banner.getTitle().getString(LanguageEnum.zh_CN.getCode())%>"></td>
	</tr>
	<tr>
		<td>英文描述</td>
		<td><input type="text" name="enTitle"  value="<%=banner.getTitle().getString(LanguageEnum.en_US.getCode())%>"></td>
	</tr>
<%if(BannerEnum.H5.getType().equals(banner.getBannerType())) {%>
	<tr>
		<td>H5链接</td>
		<td>
			<input type="text" name="h5url" value="<%=banner.getContent().getString("url")%>">
		</td>
	</tr>
<%}%>	
	<tr>
		<td>打开方式</td>
		<td>
			<select name="isOpenInBrowser">
				<option value="false">webview</option>
				<option value="true" <%=banner.isOpenInBrowser()?"selected":"" %>>系统浏览器</option>
			</select>
		</td>
	</tr>
	<tr>
		<td colspan="2"><button type="submit">保  存</button></td>
	</tr>
</table>
</form>
<%if(!BannerEnum.H5.getType().equals(banner.getBannerType())) {%>
<form action="<%=basePath%>banner/edit/content" method="post">
<input type="hidden" name="bannerId" value="<%=banner.getElementId()%>">
<table border="1">
	<tr>
		<td>
			<select id="appType" name="appType">
		<%if(BannerEnum.Group.getType().equals(banner.getBannerType())) {%>
				<option value="<%=BannerEnum.Subject.getType() %>"><%=BannerEnum.Subject.getName() %></option>
		<%} else {%>
	   	 	<%for(AppEnum app : apps) {%>
				<option value="<%=app.getCollection() %>"><%=app.getCnName() %>-<%=app.getEnName() %></option>
				<%}
			}%>
			</select>
		 	<input type="text" name="appId" id="sea_elementId">
		    <a href="javascript:searchContent()">查找</a>
		</td>
	</tr>
	<tr>
		<td>
			<div id="searchContent" style="display: none">
		   	 	<div id="searchInfoContent"></div>
		   	 	<button type="submit">添加该内容</button>
		   	 </div>		
		</td>
	</tr>
</table>
</form>
<%}%>
<ul>
<%
	for(int i=0; i<contents.size(); i++) {
	    BannerContentEntity content =  contents.get(i);
	   	JSONObject json = contentMap.get(content.getAppId());%>
	<li>
		<table>
			<tr>
				<td><img alt="封面" src="<%=img_path+json.getString("previewImageUrl") %>" width="330" height="292"></td>
			</tr>
			<tr>
				<td>
					ID:<%=json.getString("elementId") %>
					<br />
					类型：
					<%if(BannerEnum.Group.getType().equals(banner.getBannerType())) {%>
					    <%=BannerEnum.Subject.getName() %>
					<%} else {%>
						<%=AppEnum.byCollection(content.getAppType()).getCnName()%>    
					<%}%>
				</td>
			</tr>
			<tr>
				<td>
					国内:<%=json.getJSONObject("actionCount").getIntValue("china") %> 
					googlePlay:<%=json.getJSONObject("actionCount").getIntValue("googlePlay") %> 
					<button onclick="delBannerApp('<%=banner.getElementId() %>','<%=content.getAppId() %>')">删除</button>
				</td>
			</tr>			
		</table>
	</li>
	<%}%>
</ul>
</body>
</html>