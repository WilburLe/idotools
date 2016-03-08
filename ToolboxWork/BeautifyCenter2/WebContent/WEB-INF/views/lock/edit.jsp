<%@page import="com.toolbox.utils.JSONUtility"%>
<%@page import="com.alibaba.druid.support.json.JSONUtils"%>
<%@page import="com.toolbox.common.AppMarketEnum"%>
<%@page import="com.toolbox.common.LanguageEnum"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="com.toolbox.web.entity.LockscreenEntity"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@page import="com.toolbox.framework.utils.ConfigUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String img_path = ConfigUtility.getInstance().getString("file.server.path");
String basePath = WebUtility.getBasePath(request);
LockscreenEntity lock = (LockscreenEntity) request.getAttribute("lock");
Object msg = request.getAttribute("msg");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<button onclick="javascript:history.back()">返回列表</button>
<form action="<%=basePath%>lockscreen/edit" enctype="multipart/form-data" method="post">
	<input type="hidden" name="elementId" value="<%=lock.getElementId()%>">
	<table border="1">
		<tr>
			<td><button type="submit">确 定 修 改</button></td>
			<td><%=msg!=null?msg.toString():"" %></td>
		</tr>
		<tr>
			<td>替换APK</td>
			<td><input type="file" name="apk"></td>
		</tr>		
		<tr>
			<td>ICON</td>
			<td><img alt="ICON" src="<%=img_path%><%=lock.getIconUrl() %>"></td>
		</tr>	
		<tr>
			<td>ID</td>
			<td><%=lock.getElementId() %></td>
		</tr>
		<tr>
			<td>包名</td>
			<td><%=lock.getPackageName() %></td>
		</tr>			
		<tr>
			<td>中文名</td>
			<td><%=lock.getName().getString(LanguageEnum.zh_CN.getCode()) %></td>
		</tr>
		<tr>
			<td>英文名</td>
			<td><%=lock.getName().getString(LanguageEnum.en_US.getCode()) %></td>
		</tr>
		<tr>
			<td>中文描述</td>
			<td><%=(lock.getDescription()!=null&&lock.getDescription().containsKey(LanguageEnum.zh_CN.getCode()))?lock.getDescription().getString(LanguageEnum.zh_CN.getCode()):"" %></td>
		</tr>
		<tr>
			<td>英文描述</td>
			<td><%=(lock.getDescription()!=null&&lock.getDescription().containsKey(LanguageEnum.en_US.getCode()))?lock.getDescription().getString(LanguageEnum.en_US.getCode()):"" %></td>
		</tr>
		<tr>
			<td>上架范围</td>
			<td>
				<%String[] markets = lock.getMarket();
					boolean chinaMarket = false;
					boolean googleMarket = false;
					for(String market : markets) {
					    if(AppMarketEnum.China.getCode().equals(market)) {
					        chinaMarket = true;
					    }
					    if(AppMarketEnum.GooglePlay.getCode().equals(market)) {
					        googleMarket = true;
					    }
				}%>
				<input type="checkbox" name="market" class="market" value="<%=AppMarketEnum.China.getCode() %>" <%=chinaMarket?"checked":"" %>><%=AppMarketEnum.China.getName() %>
				<input type="checkbox" name="market" class="market" value="<%=AppMarketEnum.GooglePlay.getCode() %>" <%=googleMarket?"checked":"" %>><%=AppMarketEnum.GooglePlay.getName() %>
			</td>
		</tr>
		<tr>
			<td>下载地址</td>
			<td>
				<%=img_path%><%=lock.getActionUrl().getString(LanguageEnum.zh_CN.getCode()) %>
				<br />
				<%=lock.getActionUrl().getString(LanguageEnum.en_US.getCode()) %>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<%String[] aus = lock.getDetailUrl();
					for(int i=0; i<aus.length; i++) {%>
					 <img alt="INFO" src="<%=img_path%><%=aus[i]%>">   
				<%}%>
			</td>
		</tr>
	</table>

</form>
</body>
</html>