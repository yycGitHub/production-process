<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口管理</title>
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
			class="songti">&gt;</span><span>接口列表</span>
		<b class="fr">
			<shiro:hasPermission name="api:apiInterface:edit">
				<a href="${ctx}/api/apiInterface/form" class="min_btn btn_secondary marright">新增接口</a>
			</shiro:hasPermission>
		</b>
	</div>
	<div class="con">
		<form:form id="searchForm" modelAttribute="apiInterface" action="${ctx}/api/apiInterface/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="search">
				<dl>
					<dt>
						<span>
							<form:input path="name" htmlEscape="false"
								maxlength="50" cssClass="input_txt w260" placeholder="接口名称" />
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
						<td><nobr>接口名称</nobr></td>
						<td><nobr>接口URL</nobr></td>
						<td><nobr>接口状态</nobr></td>
						<td><nobr>备注信息</nobr></td>
						<shiro:hasPermission name="api:apiInterface:edit">
							<td><nobr>操作</nobr></td>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list}" var="apiInterface">
					<tr>
							<td style="display:none;">${apiInterface.id}</td>
							<td>${apiInterface.name}</td>
							<td>${apiInterface.url}</td>
							<td>${apiInterface.status}</td>
							<td>${apiInterface.remarks}</td>
						<shiro:hasPermission name="api:apiInterface:edit"><td>
		    				<a href="${ctx}/api/apiInterface/form?id=${apiInterface.id}">修改</a>
							<a href="${ctx}/api/apiInterface/delete?id=${apiInterface.id}" onclick="return confirmx('确认要删除该接口吗？', this.href)">删除</a>
							<a href="${ctx}/api/apiInterface/information?id=${apiInterface.id}">查看</a>
						</td></shiro:hasPermission>
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
