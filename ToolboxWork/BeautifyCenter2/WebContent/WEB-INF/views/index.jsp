<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Beautify Center</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="<%=WebUtility.getBasePath(request) %>static/bootstrap/css/bootstrap.min.css">
   
    <!-- Theme style -->
    <link rel="stylesheet" href="<%=WebUtility.getBasePath(request) %>static/adminLTE/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
          page. However, you can choose any other skin. Make sure you
          apply the skin class to the body tag so the changes take effect.
    -->
    <link rel="stylesheet" href="<%=WebUtility.getBasePath(request) %>static/adminLTE/dist/css/skins/skin-blue.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style type="text/css">
    	.content{min-height: 800px;}
    </style>
    <script type="text/javascript">
    	function loadData(url) {
    		$("#web_content").attr("src", url);
    	}
    </script>
</head>
  <body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
<%@include file="common/head.jsp" %>
<%@include file="common/menu.jsp" %>
      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1>
            Page Header
            <small>Optional description</small>
          </h1>
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
            <li class="active">Here</li>
          </ol>
        </section>

        <!-- Main content -->
        <section class="content">

          <iframe id="web_content" frameborder="0" width="100%" height="780px" src="<%=basePath%>static/welcome.html"></iframe>
	
        </section><!-- /.content -->
      </div><!-- /.content-wrapper -->

<%@include file="common/foot.jsp" %>
<div class="control-sidebar-bg"></div>
    </div><!-- ./wrapper -->
    
<!-- jQuery 2.1.4 -->
    <script src="<%=WebUtility.getBasePath(request) %>static/jquery-2.2.0.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="<%=WebUtility.getBasePath(request) %>static/bootstrap/js/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="<%=WebUtility.getBasePath(request) %>static/adminLTE/dist/js/app.min.js"></script>
</body>
</html>