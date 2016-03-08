<%@page import="com.toolbox.web.entity.AppTagEntity"%>
<%@page import="com.toolbox.web.entity.WallpaperEntity"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.toolbox.framework.utils.StringUtility"%>
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
List<AppTagEntity> apptags = ( List<AppTagEntity> ) request.getAttribute("apptags");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>wallpaperView</title>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script src="<%=basePath %>static/jquery-ui.js"></script>
<link rel="stylesheet" href="<%=basePath %>static/dialog.css?<%=System.currentTimeMillis()%>">
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
			String[] dbtags = wallpaper.getTags();
			Map<String, AppTagEntity> dbtagmap = new HashMap<String, AppTagEntity>();
			for(int i=0; i<apptags.size(); i++) {
		    	AppTagEntity apptag = apptags.get(i);
		    	if(apptag.getStatus() == -1) {
		    	    continue;
		    	}
			    String uuid = apptag.getElementId();
			    boolean checked = false;
			    for(int j=0; j<dbtags.length; j++) {
			        String dbtag = dbtags[j];
			        if(uuid.equals(dbtag)) {
			            checked = true;
			            dbtagmap.put(uuid, apptag);
			        }
			    }
			    %>
			    <input type="checkbox"  class="cgtag" value="<%=apptag.getElementId()%>" <%=checked?"checked":"" %>>
			    <%=apptag.getName().getString("zh_CN") %>-<%=apptag.getName().getString("en_US") %>
		<%}%>		
	</div>
	<button onclick="javascript:history.back()">返回列表</button>
	<table>
		<tr>
			<td>壁纸图片ID <%=wallpaper.getElementId() %></td>
		</tr>
		<tr>
			<td>所属分类
				<%
				if(dbtags==null || dbtags.length == 0) {
				    out.print("无");
				} else {
					for(int i=0; i<dbtags.length; i++) {
					    String dbtag = dbtags[i];
					    AppTagEntity tag = dbtagmap.get(dbtag);
					    if(tag == null) {
					        out.print("Tag 异常请重新选择Tag");
					    } else {
					        out.print("["+tag.getName().getString("zh_CN")+" - "+tag.getName().getString("en_US")+"] ");
					    }
					}
				}%>
				<button onclick="javascript:dialog.dialog('open')">编辑</button>
			</td>
		</tr>
		<tr>
			<td>	
				<ul>
					<li>
						<table>
							<%if(StringUtility.isNotEmpty(wallpaper.getActionUrl().getString("hdpi")) ) {%>
							<tr>
								<td>1080p-<%=wallpaper.getFileSize().getLong("hdpi")/1024 %>k</td>
							</tr>
							<tr>
								<td><a href="<%=img_path%><%=wallpaper.getActionUrl().getString("hdpi") %>" target="_blank">
									<img alt="mdpi" src="<%=img_path%><%=wallpaper.getPreviewImageUrl() %>"></a></td>
							</tr>
							<%}%>
						</table>
					</li>
					<li>
						<table>
							<%if(StringUtility.isNotEmpty(wallpaper.getActionUrl().getString("mdpi")) ) {%>
							<tr>
								<td>720p-<%=wallpaper.getFileSize().getLong("mdpi")/1024 %>k</td>
							</tr>
							<tr>
								<td><a href="<%=img_path%><%=wallpaper.getActionUrl().getString("mdpi") %>" target="_blank">
									<img alt="ldpi" src="<%=img_path%><%=wallpaper.getPreviewImageUrl() %>"></a></td>
							</tr>
							<%}%>
						</table>
					</li>
					<li>
						<table>
							<%if(StringUtility.isNotEmpty(wallpaper.getActionUrl().getString("ldpi")) ) {%>
							<tr>
								<td>480p-<%=wallpaper.getFileSize().getLong("ldpi")/1024 %>k</td>
							</tr>
							<tr>
								<td><a href="<%=img_path%><%=wallpaper.getActionUrl().getString("ldpi") %>" target="_blank">
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