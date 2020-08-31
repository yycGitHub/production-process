<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>栏目管理</title>
	<meta name="decorator" content="cms_admin_default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script src="${ctxStatic}/common/pony.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 3});
		});
    	function updateSort() {
			loading('正在提交，请稍等...');
	    	$("#listForm").attr("action", "${ctx}/cms/category/updateSort");
	    	$("#listForm").submit();
    	}
	</script>
</head>
<body>
<div class="miandody">
    <div class="crumb">
		<b class="fr">
			<shiro:hasPermission name="cms:category:edit">
				<a class="min_btn btn_secondary marright" href="${ctx}/cms/category/form?id=${category.id}">修改本级栏目</a>
				<a class="min_btn btn_secondary marright" href="${ctx}/cms/category/form?parent.id=${category.id}">添加下级栏目</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="cms:category:edit">
				<a class="min_btn btn_secondary marright" onclick="updateSort();">保存排序</a>
			</shiro:hasPermission>
		</b>
		<tags:message content="${message}"/>
	</div>
	<div class="con">
	<div class="tabcon">
	<form id="listForm" method="post">
		 <table id="treeTable" class="tab" cellpadding="0" cellspacing="0">
			<tr><th>栏目名称</th><th style="text-align:center;">排序</th><th title="是否在导航中显示该栏目">导航菜单</th><th title="是否在分类页中显示该栏目的文章列表">栏目列表</th><th>展现方式</th><th>操作</th></tr>
			<c:forEach items="${list}" var="tpl">
				<tr id="${tpl.id}" pId="${tpl.parent.id ne '1' ? tpl.parent.id : '0'}">
					<td><a href="${ctx}/cms/category/form?id=${tpl.id}">${tpl.name}</a></td>
<%-- 					<td>${tpl.office.name}</td>
					<td>${fns:getDictLabel(tpl.module, 'cms_module', '公共模型')}</td> --%>
					<td style="text-align:center;">
						<shiro:hasPermission name="cms:category:edit">
							<input type="hidden" name="ids" value="${tpl.id}"/>
							<input name="sorts" type="text" value="${tpl.sort}" style="width:50px;margin:0;padding:0;text-align:center;">
						</shiro:hasPermission>
						<shiro:lacksPermission name="cms:category:edit">
							${tpl.sort}
						</shiro:lacksPermission>
					</td>
					<td>${fns:getDictLabel(tpl.inMenu, 'show_hide', '隐藏')}</td>
					<td>${fns:getDictLabel(tpl.inList, 'show_hide', '隐藏')}</td>
					<td>${fns:getDictLabel(tpl.showModes, 'cms_show_modes', '默认展现方式')}</td>
					<td>
						<a href="${pageContext.request.contextPath}${fns:getFrontPath()}/list-${tpl.id}${fns:getUrlSuffix()}" target="_blank">访问</a>
						<shiro:hasPermission name="cms:category:edit">
							<a href="${ctx}/cms/category/form?id=${tpl.id}">修改</a>
							<a href="${ctx}/cms/category/delete?id=${tpl.id}" onclick="return confirmx('要删除该栏目及所有子栏目项吗？', this.href)">删除</a>
							<a href="${ctx}/cms/category/form?parent.id=${tpl.id}">添加下级栏目</a>
							<a href="${ctx}/cms/categoryRoleUser/toPlateAdmin?id=${tpl.id}">栏目添加管理员</a>
							<shiro:hasPermission name="sys:standard:expview">
								<a href="${ctx}/standard/standardItemList?stId=${tpl.id}">扩展字段</a>
							</shiro:hasPermission>
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
		</table>
	</form>
	</div>
	</div>
</div>
</body>
</html>