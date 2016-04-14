<%@page import="com.toolbox.common.TabEnum"%>
<%@page import="com.toolbox.common.BannerEnum"%>
<%@page import="com.toolbox.web.entity.HotRankEntity"%>
<%@page import="com.toolbox.web.entity.SystemConfigEmtity"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="java.util.List"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String img_path = ConfigUtility.getInstance().getString("file.server.path");
AppEnum[] apps = AppEnum.values();
SystemConfigEmtity hconfig =  (SystemConfigEmtity) request.getAttribute("hconfig");
List<HotRankEntity> hotRanks = (List<HotRankEntity>) request.getAttribute("hotRanks");
String market = (String) request.getAttribute("market");
String appType = (String) request.getAttribute("appType"); 
System.out.print("appType > "+appType);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script src="<%=basePath %>static/jquery-ui.js"></script>
<link rel="stylesheet" href="<%=basePath %>static/dialog.css?<%=System.currentTimeMillis()%>">
<script type="text/javascript">
var dialog;
$(function() {
	dialog = $( "#dialog" ).dialog({
        autoOpen: false,
        height:'auto',
        width: 'auto',
        modal: true,
        dialogClass:'no-close success-dialog'
      });	
});
function openDialog(elementId, sortNu, previewImageUrl) {
	$("#sea_sortNu").val(sortNu);
	$("#hotElementIdId").val(elementId);
	$("#hotSortNu").val(sortNu);
	var html = "<img src='<%=img_path%>"+previewImageUrl+"' width='330' height='292'>";
	$("#searchInfoContent").show();
	$("#searchInfoContent").html(html);
	dialog.dialog("option","title", "编辑 》"+elementId).dialog("open");
}
var sea_result = false;
function searchContent() {
	var appType = $("#sea_appType option:selected").val();
	var elementId = $("#sea_elementId").val();
	$.post("<%=basePath%>banner/search", {"appType":appType, "elementId":elementId}, function(banner) {
		if(!banner) {
			alert("没有找到");
			return;
		}
		//$.parseJSON("{" + result + "}");
		var data = eval('('+banner+')');
		var html = "<img src='<%=img_path%>"+data.previewImageUrl+"' width='330' height='292'>";
		$("#searchInfoContent").show();
		$("#searchInfoContent").html(html);
		sea_result =true;
	});
}

function editHot() {
	var sortNuNew = $("#sea_sortNu").val();
	var elementId = $("#hotElementIdId").val();
	var sortNuOld = $("#hotSortNu").val();
	
	var sea_elementId = $("#sea_elementId").val();
	var sea_appType = $("#sea_appType option:selected").val();
	$.post("<%=basePath%>hot/rank/edit", {"elementId":elementId, "sea_elementId":sea_elementId, "sea_appType":sea_appType, "market":"<%=market %>", "sortNuNew":sortNuNew, "sortNuOld":sortNuOld}, function(result) {
		window.location.reload();
	});
}
</script>
<style type="text/css">
 .beautify_ul {list-style:none;}
 .beautify_ul li {float: left;}
</style>
</head>
<body>

	<ul class="beautify_ul">
<%
	JSONObject hc = hconfig.getConfig();
	JSONObject appConfig = hc.getJSONObject("appConfig");
	Iterator<String> it = appConfig.keySet().iterator();
	while(it.hasNext()) {
		String code = it.next();
		JSONObject hcc = appConfig.getJSONObject(code);
		TabEnum app = TabEnum.byCollection(code);
		String color = app.getCollection().equals(appType)?"green":"";
%>
    	<li>
    		<a href="<%=basePath %>hot/rank/<%=market %>/<%=app.getCollection()%>">
    			<button style="background-color: <%=color%>"><%=app.getCnName()+"-"+app.getEnName() %></button>
    		</a>
    	</li>
<%}%>	
	</ul>
	<br />
	<ul class="beautify_ul">
	<%for(int i=0; i<hotRanks.size(); i++) {
	    	HotRankEntity hotRank = hotRanks.get(i);%>
	    	<li>
			<table>
				<tr>
					<td>
						<img alt="缩略图" src="<%=img_path%><%=hotRank.getPreviewImageUrl()%>" width="330" height="292">
					</td>
				</tr>
				<tr>
					<td>
					序号：<%=hotRank.getSortNu() %>
					分类：<%=AppEnum.byCollection(hotRank.getAppType()).getCnName() %>
					</td>
				</tr>				
				<tr>
					<td>ID:<%=hotRank.getElementId() %></td>
				</tr>
				<tr>
					<td>国内: <%=hotRank.getActionCount().getChina() %>，googlePlay:<%=hotRank.getActionCount().getGooglePlay() %></td>
				</tr>
				<tr>
					<td><button onclick="openDialog('<%=hotRank.getElementId()%>', <%=hotRank.getSortNu() %>, '<%=hotRank.getPreviewImageUrl()%>')">编辑</button></td>
				</tr>
			</table>			    		
	    	</li>	
	<%} %>
	</ul>
	
<div id="dialog" title="编辑热门列表">
	<input type="hidden" id="hotElementIdId">
	<input type="hidden" id="hotSortNu">
	<table>
		<tr>
			<td colspan="2"><div id="searchInfoContent" style="display: none"></div></td>
		</tr>
		<tr>
			<td>分类</td>
			<td>
				<select id="sea_appType" name="appType">
			  	 	<%for(AppEnum app : apps) {%>
					<option value="<%=app.getCollection() %>"><%=app.getCnName() %>-<%=app.getEnName() %></option>
					<%}%>
				</select>			
			</td>
		</tr>
		<tr>
			<td>ID</td>
			<td><input type="text" name="sea_elementId" id="sea_elementId"></td>
		</tr>
		<tr>
			<td colspan="2"><button onclick="searchContent()">查  找</button></td>
		</tr>
		<tr>
			<td>序号</td>
			<td><input type="text" name="sea_sortNu" id="sea_sortNu"></td>
		</tr>
		<tr>
			<td colspan="2"><button onclick="editHot()">编辑</button></td>
		</tr>		
	</table>
</div>
</body>
</html>