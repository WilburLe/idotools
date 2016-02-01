<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String img_path = ConfigUtility.getInstance().getString("file.server.path");
String basePath = WebUtility.getBasePath(request);
JSONObject wallpaper  = (JSONObject) request.getAttribute("wallpaper");
JSONArray mytags = wallpaper.getJSONArray("tags");

Map<String, JSONObject> tagmap = new HashMap<String, JSONObject>();
JSONObject tags = ( JSONObject ) request.getAttribute("tags");
JSONArray arr = tags==null?new JSONArray():tags.getJSONArray("arr");
arr = arr==null?new JSONArray():arr;
for(int i=0; i<arr.size(); i++) {
	JSONObject tag = arr.getJSONObject(i);
	tagmap.put(tag.getString("uuid"), tag);
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>wallpaperView</title>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script src="<%=basePath %>static/jquery-ui.js"></script>
<link rel="stylesheet" href="<%=basePath %>static/dialog.css">
<style type="text/css">
ul{list-style:none;} 
ul li{float:left;}
</style>
<script type="text/javascript">
var dialog;
$(function() {
	dialog = $( "#dialog" ).dialog({
        autoOpen: false,
        height:150,
        width: 500,
        modal: true,
        buttons: {
        	 "确定更改": changeTag
        },
        close: function() {
        }
      });	

});
function changeTag() {
	var tags=[];
	$(".cgtag").each(function() {
		if($(this).is(":checked")) {
			tags.push($(this).val());
		}
	});
	$.post("<%=basePath%>wallpaper/changetag/", {"elementId":"<%=wallpaper.getString("elementId")%>", "tags":JSON.stringify(tags)}, function() {
		window.location.reload();
	});
}
function changeTagDialog() {
	dialog.dialog("open");
}
</script>
</head>
<body>
	<div id="dialog" title="更改分类">
		<%
			for(int i=0; i<arr.size(); i++) {
			    JSONObject tag = arr.getJSONObject(i);
			    String uuid = tag.getString("uuid");
			    boolean checked = false;
			    if(mytags.contains(uuid)) {
			        checked = true;    
			    }
			    %>
			    <input type="checkbox"  class="cgtag" value="<%=tag.getString("uuid")%>" <%=checked?"checked":"" %>><%=tag.getString("cnName") %>-<%=tag.getString("enName") %>
		<%}%>		
	</div>
	<button onclick="javascript:history.back()">返回列表</button>
	<table>
		<tr>
			<td>壁纸图片<%=wallpaper.getString("elementId") %></td>
		</tr>
		<tr>
			<td>所属分类
				<%
				if(mytags==null || mytags.size() == 0) {
				    out.print("其它");
				} else {
					for(int i=0; i<mytags.size(); i++) {
					    String uuid = mytags.getString(i);
					    JSONObject tag = tagmap.get(uuid);%>
					    <%=tag.getString("cnName") %>-<%=tag.getString("enName") %>
					<%}
				}%>
				<button onclick="javascript:dialog.dialog('open')">编辑</button>
			</td>
		</tr>
		<tr>
			<td>	
				<ul>
					<li>
						<table>
							<%if(wallpaper.getJSONObject("actionUrl").containsKey("hdpi")) {%>
							<tr>
								<td><%=wallpaper.getJSONObject("fileSize").getString("hdpi") %></td>
							</tr>
							<tr>
								<td><a href="<%=img_path%><%=wallpaper.getJSONObject("actionUrl").getString("hdpi") %>" target="_blank">
									<img alt="hdpi" src="<%=img_path%><%=wallpaper.getString("previewImageUrl") %>"></a></td>
							</tr>
							<%}%>
						</table>
					</li>
					<li>
						<table>
							<%if(wallpaper.getJSONObject("actionUrl").containsKey("mdpi")) {%>
							<tr>
								<td><%=wallpaper.getJSONObject("fileSize").getString("mdpi") %></td>
							</tr>
							<tr>
								<td><a href="<%=img_path%><%=wallpaper.getJSONObject("actionUrl").getString("mdpi") %>" target="_blank">
									<img alt="mdpi" src="<%=img_path%><%=wallpaper.getString("previewImageUrl") %>"></a></td>
							</tr>
							<%}%>
						</table>
					</li>
					<li>
						<table>
							<%if(wallpaper.getJSONObject("actionUrl").containsKey("ldpi")) {%>
							<tr>
								<td><%=wallpaper.getJSONObject("fileSize").getString("ldpi") %></td>
							</tr>
							<tr>
								<td><a href="<%=img_path%><%=wallpaper.getJSONObject("actionUrl").getString("ldpi") %>" target="_blank">
									<img alt="ldpi" src="<%=img_path%><%=wallpaper.getString("previewImageUrl") %>"></a></td>
							</tr>
							<%}%>
						</table>
					</li>
				</ul>				
			</td>
		</tr>
	</table>
</body>
</html>