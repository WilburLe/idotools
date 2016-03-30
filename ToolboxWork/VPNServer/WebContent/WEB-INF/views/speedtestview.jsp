<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script type="text/javascript">
//speedtestview
$(function() {
        var saveDataAry=[];  
        var data={"userName":"ququ","address":"gr"};  
        $.ajax({
            type:"POST", 
            url:"<%=basePath%>speedtest", 
            dataType:"json",      
            contentType:"application/json",               
            data:JSON.stringify(data), 
            success:function(data){ 

            }
         }); 
})
</script>
</head>
<body>


</body>
</html>