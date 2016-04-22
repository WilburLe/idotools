<%@page import="com.toolbox.common.AppMarketEnum"%>
<%@page import="com.toolbox.common.BannerEnum"%>
<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">

	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">

		<!-- Sidebar user panel (optional) -->
		<div class="user-panel">
			<div class="pull-left image">
				<img src="<%=WebUtility.getBasePath(request) %>static/adminLTE/dist/img/user2-160x160.jpg" class="img-circle"
					alt="User Image">
			</div>
			<div class="pull-left info">
				<p>Admin</p>
				<!-- Status -->
				<a href="#"><i class="fa fa-circle text-success"></i> Online</a>
			</div>
		</div>

		<!-- search form (Optional) -->
		<form action="#" method="get" class="sidebar-form">
			<div class="input-group">
				<input type="text" name="q" class="form-control"
					placeholder="Search..."> <span class="input-group-btn">
					<button type="submit" name="search" id="search-btn"
						class="btn btn-flat">
						<i class="fa fa-search"></i>
					</button>
				</span>
			</div>
		</form>
		<!-- /.search form -->

		<!-- Sidebar Menu -->
		<ul class="sidebar-menu">
			<li class="header">HEADER</li>
			<!-- Optionally, you can add icons to the links -->
			<li class="active">
				<a href="#">
					<i class="fa fa-link"></i> 
					<span>系统设置</span>
					<i class="fa fa-angle-left pull-right"></i>
				</a>
				<ul class="treeview-menu">
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>config/tab')">Tab设置</a></li>
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>config/tag')">分类设置</a></li>
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>config/hot')">热门设置</a></li>
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>config/ad')">广告设置</a></li>
				</ul>				
			</li>
			<li class="treeview">
				<a href="#">
					<i class="fa fa-link"></i>
					<span>Banner</span>
					<i class="fa fa-angle-left pull-right"></i>
				</a>
				<ul class="treeview-menu">
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>banner/<%=BannerEnum.Group.getType() %>/view/<%=AppMarketEnum.China.getCode()%>')">合辑管理</a></li>
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>banner/<%=BannerEnum.Subject.getType() %>/view/<%=AppMarketEnum.China.getCode()%>')">专题管理</a></li>
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>banner/<%=BannerEnum.H5.getType() %>/view/<%=AppMarketEnum.China.getCode()%>')">H5管理</a></li>
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>config/banner/<%=AppMarketEnum.China.getCode()%>')">展示设置</a></li>
					<li><a href="#">回收站</a></li>
				</ul>
			</li>			
			<li class="treeview">
				<a href="#">
					<i class="fa fa-link"></i>
					<span>热门</span>
					<i class="fa fa-angle-left pull-right"></i>
				</a>
				<ul class="treeview-menu">
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>hot/rank/china/<%=AppEnum.wallpaper.getCollection()%>')">国内</a></li>
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>hot/rank/googlePlay/<%=AppEnum.wallpaper.getCollection()%>')">GooglePlay</a></li>
				</ul>
			</li>						
			<li class="treeview">
				<a href="#">
					<i class="fa fa-link"></i>
					<span>壁纸</span>
					<i class="fa fa-angle-left pull-right"></i>
				</a>
				<ul class="treeview-menu">
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>wallpaper/view/all/0/')">列表管理</a></li>
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>wallpaper/upload/')">批量上传</a></li>
					<li><a href="#">回收站</a></li>
				</ul>
			</li>
			<li>
				<a href="#"><i class="fa fa-link"></i> <span>锁屏</span></a>
				<ul class="treeview-menu">
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>lockscreen/view/all/0/')">列表管理</a></li>
					<li><a href="javascript: loadData('<%=WebUtility.getBasePath(request) %>lockscreen/upload/')">批量上传</a></li>
					<li><a href="#">回收站</a></li>
				</ul>
			</li>
			<li>
				<a href="#"><i class="fa fa-link"></i> <span>天气</span></a>
			</li>
		</ul>
		<!-- /.sidebar-menu -->
	</section>
	<!-- /.sidebar -->
</aside>