<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假单管理</title>
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
		<i class="iconfont">&#xe60d;</i><strong>请假单管理</strong><span
			class="songti">&gt;</span><span>我的请假单</span>
		<shiro:hasPermission name="sample:gXQJ0101:edit">
			<b class="fr">
				<a class="min_btn btn_secondary marright" href="${ctx}/sample/gXQJ0101/form">新增</a>
			</b>
		</shiro:hasPermission>
	</div>
	<div class="con">
		
		<form:form id="searchForm" modelAttribute="GXQJ0101" action="${ctx}/studentvacation/gXQJ0101/mylist" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="search">
					<dl>
						<dt>
							<span class="drop_down_triangle"> 
							<form:select path="zt" class="drop_down gray w200">
									<form:option label="请选择请假单状态" value="" />
									<form:options
										items="${fns:getDictList('XG_STUDENTVACATION_ZT')}"
										itemLabel="label" itemValue="value" htmlEscape="false" />
								</form:select>
							</span>
							<span><input class="btn btn_primary" id="btnSubmit"
								name="" type="submit" value="查询"></span>
						</dt>
					</dl>
				</div>
		</form:form>
		<tags:message content="${message}"/>
		<div class="clearfix">
			<p class="martop">&nbsp;</p>
		</div>
		<div class="tabcon">
			<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<td><nobr>请假单标题</nobr></td>
						<td><nobr>请假类别</nobr></td>
						<td><nobr>申请日期</nobr></td>
						<td><nobr>请假开始时间</nobr></td>
						<td><nobr>请假结束时间</nobr></td>
						<td><nobr>请假时长（天）</nobr></td>
						<td><nobr>实际时长（天）</nobr></td>
						<td><nobr>请假单状态</nobr></td>
						<td><nobr>续假状态</nobr></td>
						<td><nobr>销假状态</nobr></td>
						<shiro:hasPermission name="sample:gXQJ0101:edit">
							<td><nobr>操作</nobr></td>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list}" var="gXQJ0101">
					<tr>
							<td>${gXQJ0101.qjdbt}</td>
							<td>${fns:getDictLabel(gXQJ0101.qjlb, 'XG_STUDENTVACATION_QJLB', '无')}</td>
							<td>${fns:getDateString(gXQJ0101.sqrq)}</td>
							<td>${fns:getDateString(gXQJ0101.qjksrq)}&nbsp;&nbsp;${fns:getTimeString(gXQJ0101.qjkssj)}</td>
							<td>${fns:getDateString(gXQJ0101.qjjsrq)}&nbsp;&nbsp;${fns:getTimeString(gXQJ0101.qjjssj)}</td>
							<td>${gXQJ0101.qjzsc}</td>
							<td>${gXQJ0101.sjqjzsj}</td>
							<td>${fns:getDictLabel(gXQJ0101.zt, 'XG_STUDENTVACATION_ZT', '无')}</td>
							<td>${fns:getDictLabel(gXQJ0101.xjsfyjs, 'XG_STUDENTVACATION_XJZT', '未续假')}</td>
							<td>${fns:getDictLabel(gXQJ0101.sfyxj, 'XG_STUDENTVACATION_XJZT', '未销假')}</td>
							<shiro:hasPermission name="sample:gXQJ0101:edit"><td>
								<a href="${ctx}/sample/gXQJ0101/form?id=${gXQJ0101.gxqj0101Id}">修改</a>
								<a href="${ctx}/studentvacation/gXQJ0101/delete?id=${gXQJ0101.gxqj0101Id}" onclick="return confirmx('确认要删除该请假单吗？', this.href)">删除</a>
								<a href="${ctx}/sample/gXQJ0101/information?id=${gXQJ0101.gxqj0101Id}">查看</a>
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
