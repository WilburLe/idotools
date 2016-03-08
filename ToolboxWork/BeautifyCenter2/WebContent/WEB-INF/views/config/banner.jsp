<%@page import="com.toolbox.common.LanguageEnum"%>
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
JSONArray banners = (JSONArray) request.getAttribute("banners");
String appType = (String) request.getAttribute("appType");

//JSONObject 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script src="<%=basePath %>static/jquery-ui.js"></script>
<link rel="stylesheet" href="<%=basePath %>static/dialog.css?<%=System.currentTimeMillis()%>">
<style type="text/css">
 ul{list-style:none;}
 ul li {float: left;}
</style>
<script type="text/javascript">
var nudialog;
$(function() {
	nudialog = $( "#nudialog" ).dialog({
        autoOpen: false,
        height:150,
        width: 250,
        modal: true,
        buttons: {
        	 "确定更改": changeSortNu
        },
        close: function() {
        }
      });	
});
function openNuDialog(appType, sortNu, elementId) {
	$("#config_appType").val(appType);
	$("#config_sortNu").val(sortNu);
	$("#config_elementId").val(elementId);
	nudialog.dialog("option","title", "修改排序").dialog("open");
}
function changeSortNu() {
	var appType = $("#config_appType").val();
	var sortNu = $("#config_sortNu").val();
	var bannerId = $("#config_elementId").val();
	$.post("<%=basePath%>config/banner/edit/", {"appType":appType, "bannerId":bannerId, "sortNu":sortNu}, function(banner) {
		window.location.reload();
	});
}
function searchContent() {
	var elementId = $("#sea_elementId").val();
	$.post("<%=basePath%>banner/search/", {"appType":"subject", "elementId":elementId}, function(banner) {
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
    JSONObject banner = banners.getJSONObject(i);%>
	<li>
		<table>
			<tr>
				<td>
					<img alt="封面" src="<%=img_path%><%=banner.getString("previewImageUrl")%>" width="330" height="292">
				</td>
			</tr>
			<tr>
				<td>ID:<%=banner.getString("elementId") %></td>
			</tr>
			<tr>
				<td>Title: <%=banner.getJSONObject("title").getString(LanguageEnum.zh_CN.getCode()) %></td>
			</tr>
			<tr>
				<td>
					序号:
					<button  onclick="openNuDialog('<%=appType%>', '<%=banner.getIntValue("sortNu")%>', '<%=banner.getString("elementId")%>')"><%=banner.getIntValue("sortNu")%></button>
					<a href="javascript:delBanner('<%=appType%>', '<%=banner.getString("elementId")%>')">删除</a>
					<a href="<%=basePath%>banner/edit/<%=banner.getString("elementId")%>">详情</a>
				</td>
			</tr>						
		</table>		
	</li>    
<%} %>
</ul>

<div id="nudialog" title="">
	<input type="hidden" id="config_appType">
	<input type="hidden" id="config_elementId">
	<input type="text" value="0" id="config_sortNu">
</div>
</body>
</html>