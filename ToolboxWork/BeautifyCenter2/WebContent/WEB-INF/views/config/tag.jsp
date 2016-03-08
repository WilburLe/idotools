<%@page import="com.toolbox.web.entity.AppTagEntity"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.toolbox.framework.utils.ListUtiltiy"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
List<AppTagEntity> tags = (List<AppTagEntity>) request.getAttribute("tags");
Map<String, List<AppTagEntity>> tagMap = ListUtiltiy.groupToList(tags, "appType");
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
.ddcontent ul{list-style:none;} 
</style>
<script type="text/javascript">
var dialog;
$(function() {
    dialog = $( "#dialog" ).dialog({
        autoOpen: false,
        height:150,
        width: 300,
        modal: true,
        buttons: {
        },
        close: function() {
        }
      });	

});
function addTag(obj) {
	var appType = $(obj).attr("appType");
	var elementId = $("#elementId").val();
	var sortNu = $("#sortNu").val();
	var zh_CN = $("#cnName").val();
	var en_US = $("#enName").val();
	var name = {};
	name.zh_CN = zh_CN;
	name.en_US = en_US;
   $.post('<%=basePath%>config/tag/add/', {'elementId':elementId, 'appType':appType, 'sortNu':sortNu, 'name': JSON.stringify(name)}, function(data) {
    	dialog.dialog( "close" );
    	window.location.reload();
   });
}
function deleteTag(elementId, status) {
   $.post('<%=basePath%>config/tag/delete/', {'elementId': elementId, 'status':status}, function(data) {
	   window.location.reload();
	});
}

function addTagDialog(appType, cname) {
	$("#elementId").val("");
	$("#cnName").val("");
	$("#enName").val("");
	$("#sortNu").val(0);
	$("#settag").attr("appType", appType);
	dialog.dialog("option","title", "添加["+cname+"]分类").dialog("open");
}
function editTagDialog(elementId, cnName, enName, sortNu) {
	$("#elementId").val(elementId);
	$("#cnName").val(cnName);
	$("#enName").val(enName);
	$("#sortNu").val(sortNu);
	dialog.dialog("option","title", "编辑["+cnName+"]分类").dialog("open");
}
		
</script>
</head>
<body>
<dl>
<%
AppEnum[] apps = AppEnum.values();
for(int i=0; i<apps.length; i++) {
    AppEnum app  = apps[i];%>
	<dt style="background-color: gray;"><%=app.getCnName() %>-<%=app.getEnName() %>
	</dt>
	<dd class="ddcontent">
		<ul>
		<% List<AppTagEntity> apptag = tagMap.get(app.getCollection());
			apptag = apptag==null?new ArrayList<AppTagEntity>():apptag;
			for(int m=0; m<apptag.size(); m++) {
			    AppTagEntity tag = apptag.get(m);
			    %>
			    <li>
			    	<%=tag.getSortNu()%>-
			    	<%=tag.getName().getString("zh_CN") %>-<%=tag.getName().getString("en_US") %>
			    	<button onclick="editTagDialog('<%=tag.getElementId()%>', '<%=tag.getName().getString("zh_CN")%>', '<%=tag.getName().getString("en_US")%>', <%=tag.getSortNu()%>)">编辑</button>
			    	<%if(tag.getStatus() == -1) {%>
			    	<button onclick="deleteTag('<%=tag.getElementId()%>', 0)" style="background-color:red">恢复</button>
			    	<%} else {%>
			    	<button onclick="deleteTag('<%=tag.getElementId()%>', -1)">弃用</button>			    	    
			    	<%} %>
			    </li>
			<%}%>
			<li><button onclick="addTagDialog('<%=app.getCollection()%>', '<%=app.getCnName() %>')">+</button></li>
		</ul>
	</dd>    
<%}%>
</dl>
<div id="dialog">
	<input type="hidden" name="elementId" id="elementId">
	序号：<input type="text" name="sortNu" id="sortNu"><br />
	中文：<input type="text" name="cnName" id="cnName"><br />
	英文：<input type="text" name="enName" id="enName"><br />
	<button onclick="addTag(this)" id="settag" appType="">添加</button>
</div>
	
</body>
</html>