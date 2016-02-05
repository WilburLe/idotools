<%@page import="com.toolbox.framework.utils.StringUtility"%>
<%@page import="com.toolbox.entity.tag.AppTagEntity"%>
<%@page import="com.toolbox.entity.tag.AppTagGroupEntity"%>
<%@page import="com.toolbox.entity.WallpaperEntity"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String img_path = ConfigUtility.getInstance().getString("file.server.path");
String basePath = WebUtility.getBasePath(request);
WallpaperEntity wallpaper  = (WallpaperEntity) request.getAttribute("wallpaper");
AppTagGroupEntity apptag = ( AppTagGroupEntity ) request.getAttribute("apptag");

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
	$.post("<%=basePath%>wallpaper/changetag/", {"elementId":"<%=wallpaper.getElementId()%>", "tags":JSON.stringify(tags)}, function() {
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
			List<String> dbtags = wallpaper.getTags();
			Map<String, AppTagEntity> dbtagmap = new HashMap<String, AppTagEntity>();
			List<AppTagEntity> tags = apptag.getTags();
			for(int i=0; i<tags.size(); i++) {
		    	AppTagEntity tag = tags.get(i);
			    String uuid = tag.getUuid();
			    boolean checked = false;
			    for(String dbtag : dbtags) {
			        if(uuid.equals(dbtag)) {
			            checked = true;
			            dbtagmap.put(uuid, tag);
			        }
			    }
			    %>
			    <input type="checkbox"  class="cgtag" value="<%=tag.getUuid()%>" <%=checked?"checked":"" %>>
			    <%=tag.getName().getZh_CN() %>-<%=tag.getName().getEn_US() %>
		<%}%>		
	</div>
	<button onclick="javascript:history.back()">返回列表</button>
	<table>
		<tr>
			<td>壁纸图片<%=wallpaper.getElementId() %></td>
		</tr>
		<tr>
			<td>所属分类
				<%
				if(dbtags==null || dbtags.size() == 0) {
				    out.print("无");
				} else {
					for(int i=0; i<dbtags.size(); i++) {
					    String uuid = dbtags.get(i);
					    AppTagEntity tag = dbtagmap.get(uuid);%>
					    <%=tag.getName().getZh_CN() %>-<%=tag.getName().getEn_US() %>
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
							<%if(StringUtility.isNotEmpty(wallpaper.getActionUrl().getHdpi()) ) {%>
							<tr>
								<td>1080p-<%=wallpaper.getFileSize().getHdpi()/1024 %>k</td>
							</tr>
							<tr>
								<td><a href="<%=img_path%><%=wallpaper.getActionUrl().getHdpi() %>" target="_blank">
									<img alt="hdpi" src="<%=img_path%><%=wallpaper.getPreviewImageUrl() %>"></a></td>
							</tr>
							<%}%>
						</table>
					</li>
					<li>
						<table>
							<%if(StringUtility.isNotEmpty(wallpaper.getActionUrl().getMdpi()) ) {%>
							<tr>
								<td>1080p-<%=wallpaper.getFileSize().getMdpi()/1024 %>k</td>
							</tr>
							<tr>
								<td><a href="<%=img_path%><%=wallpaper.getActionUrl().getMdpi() %>" target="_blank">
									<img alt="mdpi" src="<%=img_path%><%=wallpaper.getPreviewImageUrl() %>"></a></td>
							</tr>
							<%}%>
						</table>
					</li>
					<li>
						<table>
							<%if(StringUtility.isNotEmpty(wallpaper.getActionUrl().getLdpi()) ) {%>
							<tr>
								<td>1080p-<%=wallpaper.getFileSize().getLdpi()/1024 %>k</td>
							</tr>
							<tr>
								<td><a href="<%=img_path%><%=wallpaper.getActionUrl().getLdpi() %>" target="_blank">
									<img alt="ldpi" src="<%=img_path%><%=wallpaper.getPreviewImageUrl() %>"></a></td>
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