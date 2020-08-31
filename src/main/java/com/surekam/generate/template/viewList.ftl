<#assign columnList = sourceTable.sourceColumnList>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${functionName}管理</title>
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
		<i class="iconfont">&#xe60d;</i><strong>${subModuleName}</strong><span
			class="songti">&gt;</span><span>${functionName}列表</span>
		<b class="fr">
			<shiro:hasPermission name="${permissionPrefix}:edit">
				<a href="${r"${ctx}"}/${urlPrefix}/form" class="min_btn btn_secondary marright">新增${functionName}</a>
			</shiro:hasPermission>
		</b>
	</div>
	<div class="con">
		<form:form id="searchForm" modelAttribute="${className}" action="${r"${ctx}"}/${urlPrefix}/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${r"${page.pageNo}"}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${r"${page.pageSize}"}"/>
			<div class="search">
				<dl>
					<dt>
						<span><input id="btnSubmit" class="btn btn_primary" type="submit" value="查询"/></span>
					</dt>
				</dl>
			</div>
		</form:form>
		<tags:message content="${r"${message}"}"/>
		<div class="tabcon">
			<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<#list columnList as column>
							<#if column.instance != sourceTable.idKey>
						<td><nobr>${column.logicalName!""}</nobr></td>
							</#if>
						</#list>
						<shiro:hasPermission name="${permissionPrefix}:edit">
							<td><nobr>操作</nobr></td>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${r"${page.list}"}" var="${className}">
					<tr>
						<#list columnList as column>
							<#if column.physicalName == 'BLOB'>
							<td>${"${"+className+".${column.instance}String}"}</td>
							<#elseif column.instance == sourceTable.idKey>
							<td style="display:none;">${"${"+className+".${column.instance}}"}</td>
							<#else>
							<td>${"${"+className+".${column.instance}}"}</td>
							</#if>
						</#list>
						<shiro:hasPermission name="${permissionPrefix}:edit"><td>
		    				<a href="${r"${ctx}"}/${urlPrefix}/form?id=${"${"+className+".${sourceTable.idKey}}"}">修改</a>
							<a href="${r"${ctx}"}/${urlPrefix}/delete?id=${"${"+className+".${sourceTable.idKey}}"}" onclick="return confirmx('确认要删除该${functionName}吗？', this.href)">删除</a>
							<a href="${r"${ctx}"}/${urlPrefix}/information?id=${"${"+className+".${sourceTable.idKey}}"}">查看</a>
						</td></shiro:hasPermission>
					</tr>
				</c:forEach>
					
				</tbody>
				<tfoot>
					<td colspan="40">
						<div class="fr">
							<div class="page">${r"${page}"}</div>
						</div>
					</td>
				</tfoot>
			</table>
		</div>
	</div>
</div>

</body>
</html>
