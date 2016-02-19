<%@page import="com.toolbox.common.SystemConfigEnum"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.web.entity.BannerResourceEntity"%>
<%@page import="com.toolbox.common.BannerResourceEnum"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="com.toolbox.web.entity.BannerEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String img_path = ConfigUtility.getInstance().getString("file.server.path");
BannerResourceEntity resource = (BannerResourceEntity) request.getAttribute("resource");
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
	$.post("<%=basePath%>banner/resource/search", {"appType":appType, "elementId":elementId}, function(banner) {
		if(!banner) {
			alert("没有找到");
			return;
		}
		//$.parseJSON("{" + result + "}");
		var data = eval('('+banner+')');
		console.log(">>> "+data);
		var html = "<img src='<%=img_path%>"+data.previewImageUrl+"' width='330' height='292'>";
		$("#searchContent").show();
		$("#searchInfoContent").html(html);
		
	});
}
</script>
</head>
<body>
<table>
	<tr>
		<td>
			<%=BannerResourceEnum.byType(resource.getResourceType()).getName() %>
			<%=resource.getElementId() %>
		</td>
	</tr>
<%if(BannerResourceEnum.H5.getType().equals(resource.getResourceType())) {%>
	<tr>
		<td>
    		<input name="h5"><button onclick="addBanner('<%=resource.getAppType()%>')">编辑</button>
    	</td>
	</tr>
<%} else {%>
	<tr>
		<td>
	    <%if(BannerResourceEnum.Subject.getType().equals(resource.getResourceType())) {%>
			<select id="appType">
				<%for(AppEnum app : apps) {%>
				<option value="<%=app.getCollection() %>"><%=app.getCnName() %>-<%=app.getEnName() %></option>
				<%} %>
			</select>
	<%} else if(BannerResourceEnum.BannerGroup.getType().equals(resource.getResourceType())) {%>
			<select id="appType">
				<option value="<%=SystemConfigEnum.config_banner.getType() %>"><%=SystemConfigEnum.config_banner.getName() %></option>
			</select>
	<%}%>
		 	<input type="text" name="sea_elementId" id="sea_elementId">
		    <a href="javascript:searchContent()">查找</a>
		   	 <div id="searchContent" style="display: none">
		   	 	<div id="searchInfoContent"></div>
		   	 	<button onclick="addBanner('<%=resource.getAppType()%>')">添加</button>
		   	 </div>
		</td>
	</tr>
	<tr>
		<td>
			<ul>
			<% %>
			</ul>
		</td>
	</tr>
    <%}%>
</table>    

</body>
</html>