<%@page import="com.toolbox.common.TabEnum"%>
<%@page import="com.toolbox.web.entity.SystemConfigEmtity"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
SystemConfigEmtity hconfig =  (SystemConfigEmtity) request.getAttribute("hconfig");
hconfig = hconfig==null?new SystemConfigEmtity():hconfig;
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
var nudialog;
$(function() {
	nudialog = $( "#nudialog" ).dialog({
        autoOpen: false,
        height:150,
        width: 500,
        modal: true,
        buttons: {
        	 "确定更改": changeNu
        },
        close: function() {
        }
      });	
});
var cycledialog;
$(function() {
	cycledialog = $( "#cycledialog" ).dialog({
        autoOpen: false,
        height:150,
        width: 500,
        modal: true,
        buttons: {
        	 "确定更改": changeCycle
        },
        close: function() {
        }
      });	
});
function changeNu() {
	var nu = $("#hotNu").val();
	var code = $("#code").val();
	$.post("<%=basePath%>config/hot/updateNu", {"code":code, "nu":nu}, function(result) {
		window.location.reload();
	});
}
function changeCycle() {
	var cycle = $("#hotCycle").val();
	$.post("<%=basePath%>config/hot/updateCycle", {"cycle":cycle}, function(result) {
		window.location.reload();
	});
}
function openNuDialog(title, code, nu) {
	$("#hotNu").val(nu);
	$("#code").val(code);
	nudialog.dialog("option","title", "数量-"+title).dialog("open");
}
function openCycleDialog(cycle) {
	$("#hotCycle").val(cycle);
	cycledialog.dialog("option","title", "更改刷新周期").dialog("open");
}
function checkApp(code) {
	var apps=[];
	$("input[name="+code+"]").each(function() {
		if($(this).is(":checked")) {
			apps.push($(this).val());
		}
	});
	$.post("<%=basePath%>config/hot/updateApps", {"code":code, "apps":JSON.stringify(apps)}, function(result) {
		window.location.reload();
	});
}
</script>
</head>
<body>
<table border="1">
	<tr>
		<td>APP</td>
<%
	AppEnum[] apps = AppEnum.values();
	JSONObject hc = hconfig.getConfig();
	hc = hc==null?new JSONObject():hc;
	JSONObject appconfig = hc.getJSONObject("appConfig");
	
	Iterator<String> it =appconfig.keySet().iterator();
	while(it.hasNext()) {
		String code = it.next();
		JSONObject hcc = appconfig.getJSONObject(code);
		int nu = hcc.getIntValue("nu");
		TabEnum tab = TabEnum.byCollection(code);
%>
    	<td>
    		<%=tab.getAppName() %>
    	</td>
<%}%>		
	</tr>
	<tr>
		<td>数量设置</td>
<%
	Iterator<String> it1 =appconfig.keySet().iterator();
	while(it1.hasNext()) {
		String code = it1.next();
		JSONObject hcc = appconfig.getJSONObject(code);
		int nu = hcc.getIntValue("nu");
		TabEnum tab = TabEnum.byCollection(code);
%>
    	<td>
    	<button  onclick="openNuDialog('<%=tab.getCnName()+"-"+tab.getEnName() %>', '<%=code%>', <%=nu %>)">
    		TOP:<%=nu %>
    	</button>
    	</td>
<%}%>		
	</tr>	
	<tr>
		<td>包含的模块设置</td>
<%
	Iterator<String> it2 =appconfig.keySet().iterator();
	while(it2.hasNext()) {
		String code = it2.next();
		JSONObject hcc = appconfig.getJSONObject(code);
		JSONArray dapps  = hcc.getJSONArray("apps");
%>
    	<td>
    		<ul>
    			<%for(AppEnum app : apps) {
    			    	boolean checked = false;
    			    	if(dapps.contains(app.getCollection())) {
    			        	checked = true;
   			        	}%>
    			<li><input type="checkbox" <%=checked?"checked":"" %> name="<%=code%>" value="<%=app.getCollection()%>"><%=app.getCnName() %>-<%=app.getEnName() %></li>
    			<%} %>
    		</ul>
    		<button onclick="checkApp('<%=code%>')">确定更改</button>
    	</td>
<%}%>		
	</tr>
	<tr>
		<td>刷新周期设置</td>
    	<td colspan="<%=AppEnum.values().length%>">
 		    <button  onclick="openCycleDialog(<%=hc.getInteger("cycle") %>)">
		   		<%=hc.getInteger("cycle")  %>/Hour
		   	</button>
    	</td>
	</tr>	
	
</table>
<div id="nudialog" title="">
	<input type="hidden" id="code">
	<input type="text" value="0" id="hotNu">
</div>
<div id="cycledialog" title="">
	<input type="text" value="0" id="hotCycle">
</div>
</body>
</html>