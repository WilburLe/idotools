<%@page import="com.toolbox.web.entity.LockscreenEntity"%>
<%@page import="com.toolbox.common.AppMarketEnum"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String img_path = ConfigUtility.getInstance().getString("file.server.path");
String basePath = WebUtility.getBasePath(request);
List<LockscreenEntity> locks = (List<LockscreenEntity>) request.getAttribute("locks");
String market = (String) request.getAttribute("market");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<style type="text/css">
ul{list-style:none;} 
ul li{float:left;}
</style>
<script type="text/javascript">
	function dellockscreen(elementId) {
		if(confirm("确定要删除这个锁屏么？")) {
			$.get("<%=basePath%>lockscreen/delete/"+elementId, {}, function(result) {
				window.location.reload();
			});
		}
	}
</script>
</head>
<body>
<form action="<%=basePath %>lockscreen/upload/apk"  enctype="multipart/form-data" method="post">
	<table>
		<tr>
			<td>上架范围</td>
			<td>
				<input type="checkbox" name="market" value="<%=AppMarketEnum.China.getCode() %>" checked><%=AppMarketEnum.China.getName() %>
				<input type="checkbox" name="market" value="<%=AppMarketEnum.GooglePlay.getCode() %>" checked><%=AppMarketEnum.GooglePlay.getName() %>
			</td>
		</tr>
		<tr>
			<td><input type="file" name="apk"></td>
			<td><button type="submit">新增锁屏</button></td>
		</tr>
	</table>
</form>
<hr />
<ul>
	<li><a href="<%=basePath%>lockscreen/view/all/0/">
		<button style="background-color: <%="all".equals(market)?"green":""%>">全部</button></a></li>
<%AppMarketEnum[] markets = AppMarketEnum.values();
	for(AppMarketEnum mar : markets) {
	    String color = mar.getCode().equals(market)?"green":"";
	%>
	<li><a href="<%=basePath%>lockscreen/view/<%=mar.getCode() %>/0/">
		<button style="background-color: <%=color%>"><%=mar.getName() %></button></a></li>
<%}%>	
</ul>
<br />
<ul>
<%
for(int i=0; i<locks.size(); i++) {
    LockscreenEntity lock = locks.get(i);
%>
	<li>
		<table>
			<tr>
				<td colspan="2">
					<a href="<%=basePath%>lockscreen/info/<%=lock.getElementId() %>">
					<img alt="缩略图" src="<%=img_path%><%=lock.getPreviewImageUrl()%>">
					</a>
				</td>
			</tr>
			<tr>
				<td colspan="2"><%=lock.getPackageName() %></td>
			</tr>
			<tr>
				<td colspan="2">ID:<%=lock.getElementId() %></td>
			</tr>
			<tr>
				<td>下载量：0</td>
				<td><button onclick="dellockscreen('<%=lock.getElementId()%>')">删除</button></td>
			</tr>
		</table>
	</li>
<%    
}
%>
</ul>

</body>
</html>