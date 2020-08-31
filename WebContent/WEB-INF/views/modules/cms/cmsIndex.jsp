<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>内容管理1</title> 
<meta name="decorator" content="cms_admin_default"/>
</head>
<body>
<div class="miandody">
  <div class="crumb"><i class="iconfont">&#xe60d;</i><strong>内容管理</strong>
  <span class="songti">&gt;</span>
  <c:if test="${type == '0'}">
  	<span>栏目管理</span>
  </c:if>
  <c:if test="${type == '1'}">
  	<span>文章管理</span>
  </c:if>
  </div>
	<div class="main_left_tree_frame">
		<iframe src="${ctx}/cms/tree?type=${type}"
		scrolling="yes" frameborder="0" width="100%" height="100%"></iframe>
	</div>
	<div class="main_right_tree_frame">
		<iframe id="gxcmsFrame" name="gxcmsFrame" src="${ctx}/cms/none"
		scrolling="yes" frameborder="0" width="100%" height="100%"></iframe>
	</div>
</div>
</body>
</html>