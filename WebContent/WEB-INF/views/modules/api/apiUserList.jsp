<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接入方注册管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong></strong><span
			class="songti">&gt;</span><span>接入方注册列表</span>
		<b class="fr">
			<shiro:hasPermission name="api:apiUser:edit">
				<a href="${ctx}/api/apiUser/form" class="min_btn btn_secondary marright">新增接入方注册</a>
			</shiro:hasPermission>
		</b>
	</div>
	<div class="con">
		<form:form id="searchForm" modelAttribute="apiUser" action="${ctx}/api/apiUser/list" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="search">
				<dl>
					<dt>
						<span>
							<form:input path="name" htmlEscape="false"
								maxlength="50" cssClass="input_txt w260" placeholder="接入方名称" />
						</span>
						<span><input id="btnSubmit" class="btn btn_primary" type="submit" value="查询"/></span>
					</dt>
				</dl>
			</div>
		</form:form>
		<tags:message content="${message}"/>
		<div class="tabcon">
			<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<td><nobr>接入方名称</nobr></td>
						<td><nobr>appId</nobr></td>
						<td><nobr>secretCode</nobr></td>
						<td><nobr>备注信息</nobr></td>
						<shiro:hasPermission name="api:apiUser:edit">
							<td><nobr>操作</nobr></td>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list}" var="apiUser">
					<tr>
							<td style="display:none;">${apiUser.id}</td>
							<td>${apiUser.name}</td>
							<td>${apiUser.appId}</td>
							<td>${apiUser.secretCode}</td>
							<td>${apiUser.remarks}</td>
						<shiro:hasPermission name="api:apiUser:edit">
						<td>
		    				<a href="${ctx}/api/apiUser/form?id=${apiUser.id}">修改</a>
							<a href="${ctx}/api/apiUser/delete?id=${apiUser.id}" onclick="return confirmx('确认要删除该接入方吗？', this.href)">删除</a>
							<a href="${ctx}/api/apiUser/information?id=${apiUser.id}">查看</a>
							<a class="fancybox fancybox.iframe" href="${ctx}/api/apiInterface/upInterUserface?appId=${apiUser.appId}">接口权限</a>
						</td>
						</shiro:hasPermission>
					</tr>
				</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="40">
							<div class="fr">
								<div class="page">${page}</div>
							</div>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</div>

</body>
</html>
