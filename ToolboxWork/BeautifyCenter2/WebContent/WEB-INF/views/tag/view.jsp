<%@page import="com.toolbox.entity.tag.AppTagEntity"%>
<%@page import="com.toolbox.entity.tag.AppTagGroupEntity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.toolbox.framework.utils.ListUtiltiy"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.toolbox.common.CollectionEnum"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
List<AppTagGroupEntity> tags = (List<AppTagGroupEntity>) request.getAttribute("tags");
Map<String, AppTagGroupEntity> tagMap = ListUtiltiy.groupToObject(tags, "appType");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script src="<%=basePath %>static/jquery-ui.js"></script>
<link rel="stylesheet" href="<%=basePath %>static/dialog.css">
<style type="text/css">
.ddcontent ul{list-style:none;} 
</style>
<script type="text/javascript">
var dialog;
$(function() {
    dialog = $( "#dialog" ).dialog({
        autoOpen: false,
        height:100,
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
	var zh_CN = $("#cnName").val();
	var en_US = $("#enName").val();
	var tag = {};
	tag.appType = appType;
	var name = {};
	name.zh_CN = zh_CN;
	name.en_US = en_US;
	tag.name = name;
   $.post('<%=basePath%>tag/add/', {'tagdata': JSON.stringify(tag)}, function(data) {
    	dialog.dialog( "close" );
    	window.location.reload();
   });
}
function deleteTag(appType, uuid) {
   $.post('<%=basePath%>tag/delete/', {'appType': appType, 'tagUuid': uuid}, function(data) {
	   window.location.reload();
   });
}

function addTagDialog(appType, cname) {
	$("#settag").attr("appType", appType);
	dialog.dialog("option","title", "添加["+cname+"]分类").dialog("open");
}
</script>
</head>
<body>
<dl>
<%
CollectionEnum[] types = CollectionEnum.values();
for(int i=0; i<types.length; i++) {
    CollectionEnum type  = types[i];%>
	<dt style="background-color: gray;"><%=type.getCnName() %>
	</dt>
	<dd class="ddcontent">
		<ul>
		<% AppTagGroupEntity apptag = tagMap.get(type.getCollection());
			apptag = apptag==null?new AppTagGroupEntity():apptag;
			if(apptag.getTags() == null) {
			    apptag.setTags(new ArrayList());
			}
			for(int m=0; m<apptag.getTags().size(); m++) {
			    AppTagEntity tag = apptag.getTags().get(m);%>
			    <li><%=tag.getName().getZh_CN() %>-<%=tag.getName().getEn_US() %>
			    	<button onclick="deleteTag('<%=type.getCollection()%>', '<%=tag.getUuid()%>')">X</button></li>
			<%}%>
			<li><button onclick="addTagDialog('<%=type.getCollection()%>', '<%=type.getCnName() %>')">+</button></li>
		</ul>
	</dd>    
<%}%>
</dl>
<div id="dialog" title="新增分类">
	中文：<input type="text" name="cnName" id="cnName"><br />
	英文：<input type="text" name="enName" id="enName"><br />
	<button onclick="addTag(this)" id="settag" appType="">添加</button>
</div>
	
</body>
</html>