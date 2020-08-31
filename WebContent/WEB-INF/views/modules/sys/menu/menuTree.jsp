<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>溯源管理平台</title>
	<meta name="decorator" content="default"/>
</head>
<body class="no-skin">
	<div id="sidebar" class="sidebar responsive ace-save-state">
	<script type="text/javascript">
		try{ace.settings.loadState('sidebar')}catch(e){}
	</script>
	<div class="sidebar-shortcuts" id="sidebar-shortcuts">
		<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
			<button class="btn btn-success">
				<i class="ace-icon fa fa-signal"></i>
			</button>
	
			<button class="btn btn-info">
				<i class="ace-icon fa fa-pencil"></i>
			</button>
	
			<button class="btn btn-warning">
				<i class="ace-icon fa fa-users"></i>
			</button>
	
			<button class="btn btn-danger">
				<i class="ace-icon fa fa-cogs"></i>
			</button>
		</div>
	
		<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
			<span class="btn btn-success"></span>
	
			<span class="btn btn-info"></span>
	
			<span class="btn btn-warning"></span>
	
			<span class="btn btn-danger"></span>
		</div>
	</div><!-- /.sidebar-shortcuts -->
	
	<ul class="nav nav-list">
		<c:set var="menuList" value="${fns:getMenuList()}"/>
		<c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
		<c:if test="${menu.parent.id eq '1' && menu.isShow eq '1'}">
		<li class="">	
			<c:if test="${empty menu.href}">
				<a href="#">
					<i class="menu-icon fa fa-desktop"></i>
					<span class="menu-text"> ${menu.name} </span>
					<b class="arrow fa fa-angle-down"></b>
				</a>
			</c:if>
			<c:if test="${not empty menu.href}">
				<a href="${fn:indexOf(menu.href, '://') eq -1 ? ctx : ''}${menu.href}?menuId=${menu.id}">
					<i class="menu-icon fa fa-desktop"></i>
					<span class="menu-text"> ${menu.name} </span>
				</a>
			</c:if>
			<b class="arrow"></b>
			<ul class="submenu">
				<li class="">
					<a href="#" class="dropdown-toggle">
						<i class="menu-icon fa fa-caret-right"></i>
						${menuChild.name}
						<b class="arrow fa fa-angle-down"></b>
					</a>
					<b class="arrow"></b>
					<ul class="submenu">
						<li class="">
							<a href="#" class="dropdown-toggle">
								<i class="menu-icon fa fa-pencil orange"></i>
								4th level
								<b class="arrow fa fa-angle-down"></b>
							</a>
	
							<b class="arrow"></b>
						</li>
					</ul>
				</li>
			</ul>
		</li>	
		</c:if>
		</c:forEach>
	</ul><!-- /.nav-list -->
	
	<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
		<i id="sidebar-toggle-icon" class="ace-icon fa fa-angle-double-left ace-save-state" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
	</div>
	</div>
	<script src="${ctxStatic}/assets/js/jquery-2.1.4.min.js"></script>
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${ctxStatic}/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="${ctxStatic}/assets/js/bootstrap.min.js"></script>

		<!-- page specific plugin scripts -->

		<!--[if lte IE 8]>
		  <script src="${ctxStatic}/assets/js/excanvas.min.js"></script>
		<![endif]-->
		<script src="${ctxStatic}/assets/js/jquery-ui.custom.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.ui.touch-punch.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.easypiechart.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.sparkline.index.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.flot.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.flot.pie.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.flot.resize.min.js"></script>

		<!-- ace scripts -->
		<script src="${ctxStatic}/assets/js/ace-elements.min.js"></script>
		<script src="${ctxStatic}/assets/js/ace.min.js"></script>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
			
			})
		</script>
</body>
</html>