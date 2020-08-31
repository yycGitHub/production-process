<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>项目组管理</title> 
<meta name="decorator" content="default"/>
</head>
<body>
<div class="miandody">
  <div class="crumb"><i class="iconfont">&#xe60d;</i><strong>机构用户</strong><span class="songti">&gt;</span><span>项目组管理</span></div>
	<div class="main_left_frame">
		<iframe src="${ctx}/sys/group/groupTree"
		scrolling="yes" frameborder="0" width="100%" height="100%"></iframe>
	</div>
	<div class="main_right_frame">
		<iframe id="groupFrame" name="groupFrame" src=""
		scrolling="no" frameborder="0" width="100%" height="100%"></iframe>
	</div>
</div>
</body>
</html>