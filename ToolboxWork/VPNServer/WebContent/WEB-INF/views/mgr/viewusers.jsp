<%@page import="com.toolbox.service.impl.ReporthistoryServiceImpl"%>
<%@page import="com.toolbox.controller.server.CheckinController"%>
<%@page import="com.toolbox.framework.utils.DateUtility"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.toolbox.common.UserEnum"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@page import="com.toolbox.common.RadgroupTypeEnum"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<script type="text/javascript"
	src="<%=basePath%>static/jquery-2.2.0.min.js"></script>
<script language="JavaScript">
	function subscribetype(obj, userid) {
		$.post("<%=basePath%>mgr/subscribetype", {"userid":userid, "subscribetype":$(obj).val()}, function(result) {
			window.location.href=window.location.href;
		});
	}
	function alterDifferDays(userid, subscribetype) {
		var surplus = $("#surplus_"+userid).val();
		$.post("<%=basePath%>mgr/alterDifferDays", {"userid":userid, "subscribetype":subscribetype, "surplus": surplus}, function(result) {
			window.location.href=window.location.href;
		});
	}
	//删除用户
	function deluser(username) {
		if(confirm("确定要删除这个用户么？")) {
			$.post("<%=basePath%>mgr/deluser", {"username":username}, function(result) {
				window.location.href=window.location.href;
			});	
		}
	}

	//取消某一天的签到
	function uncheckinday(userid, date) {
		if(confirm("确定取消"+date+"的签到么？")) {
			$.post("<%=basePath%>mgr/uncheckinday", {"userid":userid, "date":date}, function(result) {
				window.location.href=window.location.href;
			});	
		}
	}
	//连续签到几天
	function checkindays(userid) {
		var days = $("#checkindays_"+userid).val();
		if(confirm("确定套连续签到"+days+"天么？")) {
			$.post("<%=basePath%>mgr/checkindays", {"userid":userid, "days":days}, function(result) {
				window.location.href=window.location.href;
			});	
		}
	}
</script>
</head>
<body>
	<table border="1">
		<tr>
			<td>用户名</td>
			<td>用户等级(免费/VIP)</td>
			<td>余量(免费用户每月1GB)</td>
			<td>连续签到几天</td>
			<td>状态(在线/离线)</td>
			<td>会话建立时间(最后一次)</td>
			<td>会话终止时间(最后一次)</td>
			<td>上行流量(字节)</td>
			<td>下行流量(字节)</td>
		</tr>
		<%
		    for (int i = 0; i < arr.size(); i++) {
		        JSONObject useracc = arr.getJSONObject(i);
		%>
		<tr>
			<td>
				username：<%=useracc.getString("username")%><br />
				bindid：<%=useracc.getString("bindid")%><br />
				<%if(UserEnum.named.name().equals(useracc.getString("usertype")) ) {%>
				    实名用户
				<%} else {%>
				     匿名用户
				<%}%>
				<button onclick="deluser('<%=useracc.getString("username")%>')">删除这个用户</button>
			</td>
			<td>
				<select name="subscribetype" onchange="subscribetype(this, '<%=useracc.getString("userid")%>')">
					<%  RadgroupTypeEnum[] types = RadgroupTypeEnum.values();
					        for (RadgroupTypeEnum type : types) {
					            boolean selected = false;
					            if (useracc.getString("subscribetype").equals(type.getName())) {
					                selected = true;
					            } %>
						<option value="<%=type.getName()%>" <%=selected ? "selected" : ""%>><%=type.getName() + " / " + type.getDays() + "天"%></option>
						<%  } %>
				</select>
			</td>
			<td>
				<%if(RadgroupTypeEnum.FREE.getName().equals(useracc.getString("subscribetype")) || RadgroupTypeEnum.Guest.getName().equals(useracc.getString("subscribetype"))) {%> 
					<input type="text" name="freeaccts" 	id="surplus_<%=useracc.getString("userid")%>" value="<%=useracc.getLong("freeaccts") / 1024%>">M 
				<% } else { %>
				 	<input type="text" name="differDays" id="surplus_<%=useracc.getString("userid")%>" value="<%=useracc.getString("differDays")%>">天
			 	<% } %>
				<button onclick="alterDifferDays('<%=useracc.getString("userid")%>', '<%=useracc.getString("subscribetype")%>')">修改</button>
			</td>
			<td>
			<%if(RadgroupTypeEnum.FREE.getName().equals(useracc.getString("subscribetype")) || RadgroupTypeEnum.Guest.getName().equals(useracc.getString("subscribetype"))) {
						out.print(useracc.getInteger("checkInCount")+"天，总共送："+(useracc.getInteger("reportRemail")/1024)+"M<hr />");
						Calendar cld = Calendar.getInstance();
						if(useracc.containsKey("isCheckedInToday") && useracc.getInteger("isCheckedInToday")==1) {
						    cld.add(Calendar.DAY_OF_MONTH,1);   
						}
						
						JSONArray checkInDays = useracc.getJSONArray("checkInDays");
						for(int m=0; m<checkInDays.size(); m++) {
						    if(m>0) {
						        out.print("<hr />");
						    }
						    JSONArray days = checkInDays.getJSONArray(m);
						    out.print(days.size()+"天，送："+(ReporthistoryServiceImpl.reportRemail(days.size())/1024)+"M<br />");
						    for(int s=0; s<days.size(); s++) {
						        if(s>0) {
							        out.print("<br />");
							    }
						        String cday = days.getString(s);	%>
						    <%=cday %><button onclick="uncheckinday('<%=useracc.getString("userid")%>', '<%=cday %>')">x</button>
						    <%}
						}
					%>
				<br />
				设置连续签到天数：<input type="text" name="checkindays" id="checkindays_<%=useracc.getShort("userid") %>" size="4">
				<button onclick="checkindays('<%=useracc.getString("userid")%>')">确定</button>
			<% } else {out.print("VIP用户不可以签到");}%>
			</td>
			<td><%=useracc.getString("acctstoptime")%></td>
			<td><%=useracc.getString("acctstarttime")%></td>
			<td><%=useracc.getString("acctstoptime")%></td>
			<td><%=useracc.getString("acctoutputoctets")%></td>
			<td><%=useracc.getString("acctinputoctets")%></td>
		</tr>
		<%  } %>
	</table>
</body>
</html>