<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>溯源产品管理管理</title>
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
			class="songti">&gt;</span><span>溯源产品管理列表</span>
		<b class="fr">
			<shiro:hasPermission name="traceproduct:traceProduct:edit">
				<a href="${ctx}/traceproduct/traceProduct/form" class="min_btn btn_secondary marright">新增溯源产品管理</a>
			</shiro:hasPermission>
		</b>
	</div>
	<div class="con">
		<form:form id="searchForm" modelAttribute="traceProduct" action="${ctx}/traceproduct/traceProduct/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="search">
				<dl>
					<dt>
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
						<td><nobr>企业编号</nobr></td>
						<td><nobr>溯源产品编号</nobr></td>
						<td><nobr>溯源产品名称</nobr></td>
						<td><nobr>溯源产品标题</nobr></td>
						<td><nobr>显示类型 1模块  2时间轴</nobr></td>
						<td><nobr>溯源主题编号</nobr></td>
						<td><nobr>产品图片</nobr></td>
						<td><nobr></nobr></td>
						<td><nobr>溯源标签申请审核开关  0关闭  1开启</nobr></td>
						<td><nobr>排序（升序）默认值统一设置为99，按照升序排列</nobr></td>
						<td><nobr>是否启用</nobr></td>
						<td><nobr>首次记录，并且以后不会进行修改</nobr></td>
						<td><nobr>创建人标识</nobr></td>
						<td><nobr>每次对数据进行操作，都要对此字段进行修改</nobr></td>
						<td><nobr>更新人标识</nobr></td>
						<td><nobr>操作状态：A-新增，U-修改，D-删除</nobr></td>
						<shiro:hasPermission name="traceproduct:traceProduct:edit">
							<td><nobr>操作</nobr></td>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list}" var="traceProduct">
					<tr>
							<td style="display:none;">${traceProduct.id}</td>
							<td>${traceProduct.officeId}</td>
							<td>${traceProduct.productCode}</td>
							<td>${traceProduct.productName}</td>
							<td>${traceProduct.productTitle}</td>
							<td>${traceProduct.showType}</td>
							<td>${traceProduct.themeId}</td>
							<td>${traceProduct.productPic}</td>
							<td>${traceProduct.productDiscription}</td>
							<td>${traceProduct.lableAuditFlag}</td>
							<td>${traceProduct.sort}</td>
							<td>${traceProduct.status}</td>
							<td>${traceProduct.creatTime}</td>
							<td>${traceProduct.creatUserid}</td>
							<td>${traceProduct.updateTime}</td>
							<td>${traceProduct.updateUserid}</td>
							<td>${traceProduct.states}</td>
						<shiro:hasPermission name="traceproduct:traceProduct:edit"><td>
		    				<a href="${ctx}/traceproduct/traceProduct/form?id=${traceProduct.id}">修改</a>
							<a href="${ctx}/traceproduct/traceProduct/delete?id=${traceProduct.id}" onclick="return confirmx('确认要删除该溯源产品管理吗？', this.href)">删除</a>
							<a href="${ctx}/traceproduct/traceProduct/information?id=${traceProduct.id}">查看</a>
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
