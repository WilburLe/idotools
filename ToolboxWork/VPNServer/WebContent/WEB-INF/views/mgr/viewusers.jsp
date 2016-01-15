<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
JSONArray arr = (JSONArray) request.getAttribute("result");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="60"> 
<title>viewusers</title>
<script language="JavaScript">
function refresh() {
       window.location.reload();
}
//setTimeout('refresh()',1000); //指定1秒刷新一次
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
		<td><%=useracc.getString("username")%></td>
		<td><%=useracc.getString("subscribetype")%></td>
		<td><%=useracc.getString("differDays")%>天-<%=useracc.getString("freeaccts")%></td>
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