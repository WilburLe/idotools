<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.web.entity.AppTabEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.common.LanguageEnum"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
AppEnum[] apps = AppEnum.values();
LanguageEnum[] lgs = LanguageEnum.values();
List<AppTabEntity> tabs = (List<AppTabEntity>) request.getAttribute("tabs"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script src="<%=basePath %>static/jquery-ui.js"></script>
<link rel="stylesheet" href="<%=basePath %>static/dialog.css?<%=System.currentTimeMillis()%>">
<script type="text/javascript">
var dialog;
var editdialog;
$(function() {
    dialog = $( "#dialog" ).dialog({
        autoOpen: false,
        height:350,
        width: 400,
        modal: true,
        buttons: {
        },
        close: function() {
        }
      });
    editdialog = $( "#editdialog" ).dialog({
        autoOpen: false,
        height:350,
        width: 400,
        modal: true,
        buttons: {
        },
        close: function() {
        }
      });	

});
function addTab() {
	var code = $("#lgCode").val()
	var sortNu = $("#lgSortNu").val()
	var name = {};
	$(".lgName").each(function() {
		var lg = $(this).attr("name");
		var val = $(this).val();
		name[lg] = val;
	});
	var apps=[];
	$("input[name=apps]").each(function() {
		if($(this).is(":checked")) {
			apps.push($(this).val());
		}
	});
   $.post('<%=basePath%>config/tab/add/', {'code':code, 'sortNu':sortNu, 'name': JSON.stringify(name), 'apps':JSON.stringify(apps)}, function(data) {
    	window.location.reload();
   });
}
function deleteTab(elementId) {
	if(confirm("确定删除这个TAB么？")) {
   		$.post('<%=basePath%>config/tab/delete/', {'elementId': elementId}, function(data) {
		   window.location.reload();
	   });
	}
}

function addTabDialog() {
	dialog.dialog("open");
}
function editTabDialog(tabId) {
	$.get("<%=basePath%>config/tab/byid/"+tabId, {}, function(result) {
		var data = result.data;
		$("#elgTabId").val(data.elementId);
		$("#elgCode").val(data.code);
		$("#elgSortNu").val(data.sortNu);
		
		var name = data.name;
		var apps = data.apps;
		$(".elgName").each(function() {
			var lg = $(this).attr("name");
			var val = name[lg];
			$(this).val(val);
		});
		$("input[id=elgapps]").each(function() {
			var tval = $(this).val();
			for(var m in apps) {
				if(tval == apps[m]) {
					$(this).prop("checked",true);
					break;
				}
			}
		});
		editdialog.dialog("option","title", "编辑-"+data.code).dialog("open");
	});
}
function editTag() {
	var elementId = $("#elgTabId").val()
	var code = $("#elgCode").val()
	var sortNu = $("#elgSortNu").val()
	
	var name = {};
	$(".elgName").each(function() {
		var lg = $(this).attr("name");
		var val = $(this).val();
		name[lg] = val;
	});
	var apps=[];
	$("input[id=elgapps]").each(function() {
		if($(this).is(":checked")) {
			apps.push($(this).val());
		}
	});
   $.post('<%=basePath%>config/tab/add/', {'elementId':elementId, 'code':code, 'sortNu':sortNu, 'name': JSON.stringify(name), 'apps':JSON.stringify(apps)}, function(data) {
    	window.location.reload();
   });
}
</script>
</head>
<body>
<button onclick="addTabDialog()">添加一个TAB</button>
Tab列表
<table border="1">
	<tr>
		<td>Index</td>
		<td>Tab Code</td>
		<td>Name</td>
		<td>所属APP</td>
		<td>操作</td>
	</tr>
<%for(AppTabEntity tab : tabs) {%>
    <tr>
    	<td><%=tab.getSortNu() %></td>
		<td><%=tab.getCode() %></td>
		<td>
			<%
			JSONObject  name = tab.getName();
			for(LanguageEnum lg : lgs) {%>
				<%=lg.getName() %> - <%=name.containsKey(lg.getCode())?name.getString(lg.getCode()):""%><br />
		<%} %>
		</td>
		<td>
			<%String[] sapps = tab.getApps();
				for(AppEnum app : apps) {
					boolean checked = false;
					if(sapps != null && sapps.length>0) {
					    for(String sapp : sapps) {
						    if(app.getCollection().equals(sapp)) {
						        checked = true;
						        break;
						    }
						}
					}
				%>
			<input type="checkbox" <%=checked?"checked":"" %>><%=app.getAppName() %><br />
			<%}%>
		</td>
		<td>
			<button onclick="editTabDialog('<%=tab.getElementId()%>')">编辑</button> 
			<button onclick="deleteTab('<%=tab.getElementId()%>')">删除</button>
		</td>
	</tr>
<%} %>	
</table>

<div id="dialog" title="添加一个TAB">
	<table>
		<tr>
			<td>Index</td>
			<td><input type="text" name="sortNu" id="lgSortNu"></td>
		</tr>
		<tr>
			<td>CODE</td>
			<td><input type="text" name="code" id="lgCode"></td>
		</tr>
<%for(LanguageEnum lg : lgs) {%>		
		<tr>
			<td><%=lg.getName() %></td>
			<td><input type="text" name="<%=lg.getCode() %>" class="lgName"></td>
		</tr>		
	<%} %>
    <tr>
		<td>所属APP</td>
		<td>
			<%for(AppEnum app : apps) {%>
			<input type="checkbox" name="apps" id="apps" value="<%=app.getCollection()%>"><%=app.getAppName() %><br />
			<%}%>
		</td>
	</tr>	
		<tr>
			<td colspan="2"><button onclick="addTab()">添加</button></td>
		</tr>
	</table>
</div>

<div id="editdialog">
	<input type="hidden" id="elgTabId">
	<table>
		<tr>
			<td>Index</td>
			<td><input type="text" id="elgSortNu"></td>
		</tr>
		<tr>
			<td>CODE</td>
			<td><input type="text"  id="elgCode"></td>
		</tr>
<%for(LanguageEnum lg : lgs) {%>		
		<tr>
			<td><%=lg.getName() %></td>
			<td><input type="text" name="<%=lg.getCode() %>" class="elgName"></td>
		</tr>		
	<%} %>
    <tr>
		<td>所属APP</td>
		<td>
			<%for(AppEnum app : apps) {%>
			<input type="checkbox" id="elgapps" value="<%=app.getCollection()%>"><%=app.getAppName() %><br />
			<%}%>
		</td>
	</tr>	
		<tr>
			<td colspan="2"><button onclick="editTag()">编辑</button></td>
		</tr>
	</table>
</div>
</body>
</html>