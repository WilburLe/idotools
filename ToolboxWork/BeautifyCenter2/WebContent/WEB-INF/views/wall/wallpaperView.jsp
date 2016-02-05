<%@page import="com.toolbox.entity.tag.AppTagEntity"%>
<%@page import="com.toolbox.entity.tag.AppTagGroupEntity"%>
<%@page import="com.toolbox.entity.WallpaperEntity"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String img_path = ConfigUtility.getInstance().getString("file.server.path");
String basePath = WebUtility.getBasePath(request);
List<WallpaperEntity> wallpapers = (List<WallpaperEntity>) request.getAttribute("wallpapers");
AppTagGroupEntity apptag = ( AppTagGroupEntity ) request.getAttribute("apptag");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>wallpaperView</title>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<style type="text/css">
ul{list-style:none;} 
ul li{float:left;} 
</style>
<script type="text/javascript">
	function delwallpaper(elementId) {
		$.get("<%=basePath%>wallpaper/delete/"+elementId, {}, function(result) {
			window.location.reload();
		});
	}
</script>
</head>
<body>
<a href="<%=basePath %>wallpaper/view/all/0"><button>全部</button></a>
<%
List<AppTagEntity> tags = apptag.getTags();
for(int i=0; i<tags.size(); i++) {
    AppTagEntity tag = tags.get(i);%>
 	 <a href="<%=basePath %>wallpaper/view/<%=tag.getUuid()%>/0">
 	 	<button><%=tag.getName().getZh_CN()  %>-<%=tag.getName().getEn_US() %></button>
 	 </a>
<%}%>
<ul class="test">
<%
for(int i=0; i<wallpapers.size(); i++) {
    WallpaperEntity wallpaper = wallpapers.get(i);
%>
	<li>
		<table>
			<tr>
				<td colspan="2">
					<a href="<%=basePath%>wallpaper/info/<%=wallpaper.getElementId() %>">
					<img alt="缩略图" src="<%=img_path%><%=wallpaper.getPreviewImageUrl()%>">
					</a>
				</td>
			</tr>
			<tr>
				<td colspan="2">ID:<%=wallpaper.getElementId() %></td>
			</tr>
			<tr>
				<td>下载量：0</td>
				<td><button onclick="delwallpaper('<%=wallpaper.getElementId()%>')">删除</button></td>
			</tr>
		</table>
	</li>
<%    
}
%>
</ul>
</body>
</html>