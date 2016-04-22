<%@page import="com.toolbox.common.AppMarketEnum"%>
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
String market = (String) request.getAttribute("market");

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
function openNuDialog(market, sortNu, elementId) {
	$("#config_market").val(market);
	$("#config_sortNu").val(sortNu);
	$("#config_elementId").val(elementId);
	nudialog.dialog("option","title", "修改排序").dialog("open");
}
function changeSortNu() {
	var market = $("#config_market").val();
	var sortNu = $("#config_sortNu").val();
	var bannerId = $("#config_elementId").val();
	$.post("<%=basePath%>config/banner/edit/", {"market":market, "bannerId":bannerId, "sortNu":sortNu}, function(banner) {
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
function addBanner(market) {
	var bannerId = $("#sea_elementId").val();
	$.post("<%=basePath%>config/banner/add/", {"market":market, "bannerId":bannerId}, function(result) {
		window.location.reload();
	});
}
function delBanner(market, bannerId) {
	if(confirm("确定删除么？")) {
		$.get("<%=basePath%>config/banner/del/"+market+"/"+bannerId, {}, function(result) {
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
			<a href="<%=basePath %>config/banner/<%=AppMarketEnum.China.getCode()%>">国内列表</a>
			<a href="<%=basePath %>config/banner/<%=AppMarketEnum.GooglePlay.getCode()%>">海外列表</a>
		</td>
	</tr>
	<tr>
		<td>
		 Banner ID:<input type="text" name="sea_elementId" id="sea_elementId">
		    <a href="javascript:searchContent()">查找</a>
		   	 <div id="searchContent" style="display: none">
		   	 	<div id="searchInfoContent"></div>
		   	 	<button onclick="addBanner('<%=market%>')">添加</button>
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
				<td>Title: <%=banner.getString("title")%></td>
			</tr>
			<tr>
				<td>
					序号:
					<button  onclick="openNuDialog('<%=market%>', '<%=banner.getIntValue("sortNu")%>', '<%=banner.getString("elementId")%>')"><%=banner.getIntValue("sortNu")%></button>
					<a href="javascript:delBanner('<%=market%>', '<%=banner.getString("elementId")%>')">删除</a>
					<a href="<%=basePath%>banner/edit/<%=banner.getString("elementId")%>">详情</a>
				</td>
			</tr>						
		</table>		
	</li>    
<%} %>
</ul>

<div id="nudialog" title="">
	<input type="hidden" id="config_market">
	<input type="hidden" id="config_elementId">
	<input type="text" value="0" id="config_sortNu">
</div>
</body>
</html>