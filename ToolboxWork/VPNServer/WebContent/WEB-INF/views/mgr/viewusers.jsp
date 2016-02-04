<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@page import="com.toolbox.common.RadgroupTypeEnum"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
JSONArray arr = (JSONArray) request.getAttribute("result");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="60"> 
<title>viewusers</title>
<script type="text/javascript" src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script language="JavaScript">
	function subscribetype(obj, userid) {
		$.post("<%=basePath%>mgr/alterusers1", {"userid":userid, "subscribetype":$(obj).val()}, function(result) {
			window.location.href=window.location.href;
		});
	}
	function alterDifferDays(userid, subscribetype) {
		var surplus = $("#surplus_"+userid).val();
		$.post("<%=basePath%>mgr/alterusers2", {"userid":userid, "subscribetype":subscribetype, "surplus": surplus}, function(result) {
			window.location.href=window.location.href;
		});
	}
</script>
</head>
<body>
<table border="1">
	<tr>
		<td>用户名</td>
		<td>用户类型(免费/VIP)</td>
		<td>余量(免费用户每月1GB)</td>
		<td>状态(在线/离线)</td>
		<td>会话建立时间(最后一次)</td>
		<td>会话终止时间(最后一次)</td>
		<td>上行流量(字节)</td>
		<td>下行流量(字节)</td>
	</tr>
<%for(int i=0; i<arr.size(); i++) {
    JSONObject useracc = arr.getJSONObject(i);%>
	<tr>
		<td><%=useracc.getString("username")%><br /><%=useracc.getString("bindid") %></td>
		<td>
			<select name="subscribetype" onchange="subscribetype(this, '<%=useracc.getString("userid")%>')">
			<%RadgroupTypeEnum[] types = RadgroupTypeEnum.values(); 
				for(RadgroupTypeEnum type : types) {
				    boolean selected = false;
					if(useracc.getString("subscribetype").equals(type.getName())) {
					    selected = true;
					}%>
				<option value="<%=type.getName() %>" <%=selected?"selected":"" %>><%=type.getName() +" / "+ type.getDays()+"天" %></option>
				<%}%>
			</select>
		</td>
		<td>
			<%if(useracc.getString("subscribetype").equals(RadgroupTypeEnum.FREE.getName())) {%>
			<input type="text" name="freeaccts" id="surplus_<%=useracc.getString("userid")%>" value="<%=useracc.getLong("freeaccts")/1024%>">M
			<%} else {%>
			<input type="text" name="differDays" id="surplus_<%=useracc.getString("userid")%>" value="<%=useracc.getString("differDays")%>">天
			<%} %>
			<button onclick="alterDifferDays('<%=useracc.getString("userid")%>', '<%=useracc.getString("subscribetype")%>')">修改</button>
		</td>
		<td><%=useracc.getString("acctstoptime")%></td>
		<td><%=useracc.getString("acctstarttime")%></td>
		<td><%=useracc.getString("acctstoptime")%></td>
		<td><%=useracc.getString("acctoutputoctets")%></td>
		<td><%=useracc.getString("acctinputoctets")%></td>
	</tr>
<%}%>	
</table>
</body>
</html>