<%@ page contentType="text/html;charset=UTF-8" %>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<title>sureserve</title>
	<meta name="description" content="overview &amp; stats" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<script>
	 		var basePath = "/sureserve";
	    </script>
		<link rel="stylesheet" href="${ctxStatic}/assets/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${ctxStatic}/assets/font-awesome/4.5.0/css/font-awesome.min.css" />
		<link rel="stylesheet" href="${ctxStatic}/assets/css/fonts.googleapis.com.css" />
		<link rel="stylesheet" href="${ctxStatic}/assets/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style" />
	    <!--[if lte IE 9]>
	        <link rel="stylesheet" href="${ctxStatic}/assets/css/ace-part2.min.css" class="ace-main-stylesheet" />
	    <![endif]-->
	    <link rel="stylesheet" href="${ctxStatic}/assets/css/ace-skins.min.css" />
	    <link rel="stylesheet" href="${ctxStatic}/assets/css/ace-rtl.min.css" />
	    <!--[if lte IE 9]>
	      <link rel="stylesheet" href="${ctxStatic}/assets/css/ace-ie.min.css" />
	    <![endif]-->
	    <script src="${ctxStatic}/assets/js/ace-extra.min.js"></script>
	    <!--[if lte IE 8]>
	    <script src="${ctxStatic}/assets/js/html5shiv.min.js"></script>
	    <script src="${ctxStatic}/ssets/js/respond.min.js"></script>
	    <![endif]-->
		
		<link rel="shortcut icon" href="${ctxStatic}/favicon.ico" type="image/x-icon">
		
		<!-- ace tree -->
		<link rel="stylesheet" href="${ctxStatic}/assets/css/tree.css" />
		
		<!-- sureserve special scc   add by ligm 2018-07-20
		<link rel="stylesheet" href="${ctxStatic}/assets/css/sureserve.css" />
		-->
		
		<style type="text/css">
			body {
				overflow: hidden;
			}
			.tab-content {
				border: none;
				padding: 10px 0px;
			}
			#menu_li_0 {
				display: none;
			}
		</style>
		
		<!--[if !IE]> -->
	  	<script src="${ctxStatic}/assets/js/jquery-2.1.4.min.js"></script>
		<!-- <![endif]-->
		<!--[if IE]>
		  <script src="${ctxStatic}/assets/js/jquery-1.11.3.min.js"></script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${ctxStatic}/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="${ctxStatic}/assets/js/bootstrap.min.js"></script>
		<script src="${ctxStatic}/assets/js/ace-elements.min.js"></script>
		<script src="${ctxStatic}/assets/js/ace.min.js"></script>
		
		<script src="${ctxStatic}/assets/js/wizard.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery-validation/1.11.1/jquery.validate.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery-validation/1.11.1/localization/messages_zh.js"></script>
		<script src="${ctxStatic}/assets/js/jquery-additional-methods.min.js"></script>
		<script src="${ctxStatic}/assets/js/bootbox.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.maskedinput.min.js"></script>
		<script src="${ctxStatic}/assets/js/select2.min.js"></script>
	
		<script src="${ctxStatic}/assets/js/sidebar-menu.js"></script>
	
		<script src="${ctxStatic}/assets/js/jquery-ui-1.10.3.custom.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.ui.touch-punch.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.easypiechart.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.sparkline.index.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.flot.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.flot.pie.min.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.flot.resize.min.js"></script>
		
		<script src="${ctxStatic}/assets/js/base.js"></script>
		
		<script src="${ctxStatic}/assets/js/ace-tab.js"></script>
		<!-- ace tree -->
		<script src="${ctxStatic}/assets/js/tree.min.js"></script>
		
		<script src="${ctxStatic}/assets/js/bootbox.js"></script>
		
		
		
		