<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>溯源码管理</title>
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
			class="songti">&gt;</span><span>溯源码列表</span>
		<b class="fr">
			<shiro:hasPermission name="tracecode:traceCode:edit">
				<a href="${ctx}/tracecode/traceCode/form" class="min_btn btn_secondary marright">新增溯源码</a>
			</shiro:hasPermission>
		</b>
	</div>
	<div class="con">
		<form:form id="searchForm" modelAttribute="traceCode" action="${ctx}/tracecode/traceCode/" method="post" class="breadcrumb form-search">
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
						<td><nobr>溯源码（打印时创建溯源码信息）</nobr></td>
						<td><nobr>标签申请批次</nobr></td>
						<td><nobr>外部系统编号（接口型的系统编号才有数据）</nobr></td>
						<td><nobr>产品批次号（接口型在调用接口返回数据后需回写批次号）</nobr></td>
						<td><nobr>溯源类型 1配置型  2根据二维码调接口  3根据批次号调接口</nobr></td>
						<td><nobr>扫码次数</nobr></td>
						<td><nobr>包装类型</nobr></td>
						<td><nobr>防伪码(预留字段)</nobr></td>
						<td><nobr>溯源码状态   1已打印  2已激活</nobr></td>
						<td><nobr>打印日期</nobr></td>
						<td><nobr>激活日期</nobr></td>
						<td><nobr>激活人</nobr></td>
						<td><nobr>首次记录，并且以后不会进行修改</nobr></td>
						<td><nobr>创建人标识</nobr></td>
						<td><nobr>每次对数据进行操作，都需要将此字段修改</nobr></td>
						<td><nobr>更新人标识</nobr></td>
						<td><nobr>操作状态：A-新增，U-修改，D-删除</nobr></td>
						<shiro:hasPermission name="tracecode:traceCode:edit">
							<td><nobr>操作</nobr></td>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list}" var="traceCode">
					<tr>
							<td style="display:none;">${traceCode.id}</td>
							<td>${traceCode.traceCode}</td>
							<td>${traceCode.applyId}</td>
							<td>${traceCode.sysId}</td>
							<td>${traceCode.batchId}</td>
							<td>${traceCode.traceType}</td>
							<td>${traceCode.traceCount}</td>
							<td>${traceCode.packType}</td>
							<td>${traceCode.antiFakeCode}</td>
							<td>${traceCode.status}</td>
							<td>${traceCode.printDate}</td>
							<td>${traceCode.activationDate}</td>
							<td>${traceCode.activator}</td>
							<td>${traceCode.creatTime}</td>
							<td>${traceCode.creatUserid}</td>
							<td>${traceCode.updateTime}</td>
							<td>${traceCode.updateUserid}</td>
							<td>${traceCode.states}</td>
						<shiro:hasPermission name="tracecode:traceCode:edit"><td>
		    				<a href="${ctx}/tracecode/traceCode/form?id=${traceCode.id}">修改</a>
							<a href="${ctx}/tracecode/traceCode/delete?id=${traceCode.id}" onclick="return confirmx('确认要删除该溯源码吗？', this.href)">删除</a>
							<a href="${ctx}/tracecode/traceCode/information?id=${traceCode.id}">查看</a>
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
