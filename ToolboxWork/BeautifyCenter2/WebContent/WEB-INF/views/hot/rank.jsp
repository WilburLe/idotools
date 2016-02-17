<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@page import="com.toolbox.entity.HotRankEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.toolbox.entity.SystemConfigEmtity"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
String img_path = ConfigUtility.getInstance().getString("file.server.path");
AppEnum[] apps = AppEnum.values();
SystemConfigEmtity hconfig =  (SystemConfigEmtity) request.getAttribute("hconfig");
List<HotRankEntity> hotRanks = (List<HotRankEntity>) request.getAttribute("hotRanks");
String rankType = (String) request.getAttribute("rankType"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<style type="text/css">
 ul{list-style:none;}
 ul li {float: left;}
</style>
</head>
<body>
	<ul>
		<li><a href="<%=basePath %>hot/rank/<%=rankType %>/all"><button><%=rankType.equals("china")?"国内":"GooglePlay" %>-全部</button></a></li>
<%
	JSONObject hc = hconfig.getConfig();
	Iterator<String> it =hc.keySet().iterator();
	while(it.hasNext()) {
		String code = it.next();
		JSONObject hcc = hc.getJSONObject(code);
		int nu = hcc.getIntValue("nu");
		AppEnum app = AppEnum.byCollection(code);
%>
    	<li>
    		<a href="<%=basePath %>hot/rank/<%=rankType %>/<%=app.getCollection()%>"><button><%=app.getCnName()+"-"+app.getEnName() %></button></a>
    	</li>
<%}%>		
	</ul>
	<br>
	<ul>
	<%for(int i=0; i<hotRanks.size(); i++) {
	    	HotRankEntity hotRank = hotRanks.get(i);%>
	    	<li>
			<table>
				<tr>
					<td>
						<img alt="缩略图" src="<%=img_path%><%=hotRank.getPreviewImageUrl()%>">
					</td>
				</tr>
				<tr>
					<td>分类:<%=AppEnum.byCollection(hotRank.getAppType()).getCnName() %></td>
				</tr>				
				<tr>
					<td>ID:<%=hotRank.getElementId() %></td>
				</tr>
				<tr>
					<td>国内: <%=hotRank.getActionCount().getChina() %>，googlePlay:<%=hotRank.getActionCount().getGoogle() %></td>
				</tr>
			</table>			    		
	    	</li>	
	<%} %>
	</ul>
</body>
</html>