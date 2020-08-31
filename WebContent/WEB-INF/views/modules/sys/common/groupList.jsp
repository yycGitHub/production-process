<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目组管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {			
			$("#btnImport").click(function(){
				location.href = "${ctx}/sys/group/form?parent.groupId=${group.groupId}&rebackId=${group.groupId}";
			});
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
			class="songti">&gt;</span><span>项目组列表</span>
		<b class="fr">
			<shiro:hasPermission name="sys:group:edit">
				<%-- <a href="${ctx}/sys/group/form" class="min_btn btn_secondary marright">新增项目组</a> --%>
				<li><input class="btn btn_secondary min_btn" id="btnImport" name="" type="button" value="添加下级组"></li>
			</shiro:hasPermission>
		</b>
	</div>
	<div class="con">
		<form:form id="searchForm" modelAttribute="group" action="${ctx}/sys/group/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="search">
				<!-- <dl>
					<dt>
						<span><input id="btnSubmit" class="btn btn_primary" type="submit" value="查询"/></span>
					</dt>
				</dl> -->
			</div>
		</form:form>
		<tags:message content="${message}"/>
		<div class="tabcon">
			<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<td><nobr>组名</nobr></td>
						<!-- <td><nobr>父id</nobr></td>
						<td><nobr>所有父id</nobr></td> 
						<td><nobr>组类型</nobr></td>-->
						<td><nobr>创建者</nobr></td>
						<td><nobr>创建时间</nobr></td>
						<td><nobr>更新者</nobr></td>
						<td><nobr>更新时间</nobr></td>
						<td><nobr>备注</nobr></td>
						<td><nobr>删除标记</nobr></td>
						<shiro:hasPermission name="sys:group:edit">
							<td><nobr>操作</nobr></td>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list}" var="groupTemp">
					<tr>
							<td style="display:none;">${groupTemp.groupId}</td>
							<td>${groupTemp.name}</td>
							<%-- <td>${groupTemp.parentId}</td>
							<td>${groupTemp.parentIds}</td> 
							<td>${groupTemp.groupType}</td>--%>
							<td>${groupTemp.createBy.name}</td>
							<td>${groupTemp.createDate}</td>
							<td>${groupTemp.updateBy.name}</td>
							<td>${groupTemp.updateDate}</td>
							<td>${groupTemp.remarks}</td>
							<td>${groupTemp.delFlag}</td>
						<shiro:hasPermission name="sys:group:edit"><td>
							<a href="${ctx}/sys/group/memberManage?groupId=${groupTemp.groupId}&rebackId=${group.groupId}">成员管理</a>
		    				<a href="${ctx}/sys/group/form?groupId=${groupTemp.groupId}&rebackId=${group.groupId}">修改</a>
							<%-- <a href="${ctx}/sys/group/delete?id=${groupTemp.groupId}" onclick="return confirmx('确认要删除该项目组吗？', this.href)">删除</a> --%>
							<a href="${ctx}/sys/group/delete?groupId=${groupTemp.groupId}&rebackId=${group.groupId}"  onclick="return confirmx('要删除该机构及所有子机构项吗？', this.href)">删除</a>
							<%-- <a href="${ctx}/sys/group/information?id=${groupTemp.groupId}">查看</a> --%>
						</td></shiro:hasPermission>
					</tr>
				</c:forEach>
					
				</tbody>
				<tfoot>
					<td colspan="40">
						<div class="fr">
							<div class="page">${page}</div>
						</div>
					</td>
				</tfoot>
			</table>
		</div>
	</div>
</div>

</body>
</html>
