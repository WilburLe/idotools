<%@page import="com.toolbox.web.entity.BannerResourceEntity"%>
<%@page import="com.toolbox.common.BannerResourceEnum"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String img_path = ConfigUtility.getInstance().getString("file.server.path");
List<BannerResourceEntity> resources = (List<BannerResourceEntity>) request.getAttribute("resources");
String resourceType = (String) request.getAttribute("resourceType");
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
	function delBanner(elementId) {
		if(confirm("确定删除？")) {
			$.get("<%=basePath%>banner/resource/del/"+elementId, {}, function(result) {
				window.location.reload();
			});
		}
	}
</script>
</head>
<body>
<form action="<%=basePath%>banner/resource/upsert" method="post">
	<select name="resourceType" id="resourceType">
		<%BannerResourceEnum[] bannerTypes = BannerResourceEnum.values();
			for(BannerResourceEnum bt : bannerTypes) {
				boolean selected = false;
				if(bt.getType().equals(resourceType)) {
				    selected = true;
				}
			%>
		<option value="<%=bt.getType()%>" <%=selected?"selected":"" %>><%=bt.getName() %></option>
		<%}%>
	</select>
	<button type="submit">新建一个资源</button>
</form>
<ul>
<li><a href="<%=basePath%>banner/resource/view/all"><button>全部</button></a></li>
<%
BannerResourceEnum[] brs = BannerResourceEnum.values();
for(BannerResourceEnum br : brs) {
    boolean selected = false;
	if(br.getType().equals(resourceType)) {
	    selected = true;
	}
%>
	<li><a href="<%=basePath%>banner/resource/view/<%=br.getType() %>"><button style="background-color: <%=selected?"green":""%>"><%=br.getName() %></button></a></li>    
<%} %>	
</ul>
<br />
<table>
	<tr>
		<td>ID</td>
		<td>操作</td>
	</tr>
<%for(int i=0; i<resources.size(); i++) {
    	BannerResourceEntity resource = resources.get(i);%>
	<tr>
		<td>
			<%=BannerResourceEnum.byType(resource.getResourceType()).getName() %>
			<%=resource.getElementId() %>
			<a href="javascript:delBanner('<%=resource.getElementId()%>')">删除</a>
			<a href="<%=basePath%>banner/resource/edit/<%=resource.getElementId()%>">编辑</a>
		</td>
	</tr>
<%}%>
</table>			    	

</body>
</html>