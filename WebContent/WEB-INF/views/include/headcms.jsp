<%@ page contentType="text/html;charset=UTF-8" %>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">

<link rel="stylesheet" href="${ctxStatic}/zn/css/alert.css" />
<link rel="stylesheet" href="${ctxStatic}/zn/css/s+.css" />
<link rel="stylesheet" href="${ctxStatic}/zn/css/base.css" />
<link rel="stylesheet" href="${ctxStatic}/zn/css/inside.css" />
<link rel="stylesheet" href="${ctxStatic}/zn/css/style.css" />
<link rel="Shortcut Icon"  href="${ctxStatic}/zn/images/32X32.ico"/>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<%-- <script src="${ctxStatic}/zn/js/jquery.js" type="text/javascript"></script> --%>
<script src="${ctxStatic}/zn/js/s+.js"></script>
<script src="${ctxStatic}/zn/js/common_file.js"></script>
<script src="${ctxStatic}/zn/control/datetime/laydate.js"></script>
<script src="${ctxStatic}/zn/control/datetime/datetime.js"></script>

<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
<!--[if lt IE 9]> <script src="${ctxStatic}/common/html5.js"></script><![endif]-->
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/list/list.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/fancyBox/source/jquery.fancybox.js" type="text/javascript"></script>
<link href="${ctxStatic}/fancyBox/source/jquery.fancybox.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/zn/control/examonline/js/layer/layer.js" type="text/javascript"></script>

<script src="${ctxStatic}/zn/js/iealert.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$("#mainFrame").load(function(){ // 绑定加载完成之后的动作
		$("#mian_frame").iealert();
	});
});
</script>