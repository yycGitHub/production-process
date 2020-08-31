<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>审批管理</title>
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/testExpenses/">审批列表</a></li>
		<shiro:hasPermission name="oa:testAudit:edit"><li><a href="${ctx}/oa/testExpenses/form">费用流程</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="testExpenses" action="${ctx}/oa/testExpenses/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>姓名：</label>
		<input id="userId" name="user.id" type="hidden" value="${testExpenses.user.id}"/>
		<input id="userName" name="userName" readonly="readonly" type="text" value="${testExpenses.user.name}" style="width: 150px;"/>
		<a id="userButton" href="javascript:void(0);" class="btn"><i class="icon-search"></i></a>&nbsp;&nbsp;
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>姓名</th><th>部门</th><th>岗位职级</th><th>调整原因</th><th>申请时间</th><shiro:hasPermission name="oa:testAudit:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="testExpenses">
			<tr>
				<td><a href="${ctx}/oa/testExpenses/form?id=${testExpenses.id}">${testExpenses.createBy.name}</a></td>
				<td>${testExpenses.office.name}</td>
				<td>${testExpenses.post}</td>
				<td>${testExpenses.content}</td>
				<td><fmt:formatDate value="${testExpenses.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<shiro:hasPermission name="oa:testAudit:edit"><td>
    				<a href="${ctx}/oa/testExpenses/form?id=${testExpenses.id}">详情</a>
					<a href="${ctx}/oa/testExpenses/delete?id=${testExpenses.id}" onclick="return confirmx('确认要删除该审批吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<script type="text/javascript">	
	$("#userButton").click(function(){     
		// 正常打开	
		top.$.jBox.open("iframe:${ctx}/sys/user/upUserTable","选择员工",700, 520, {
			buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
				if(v == "ok"){
					 var selectedUserId = h.find("iframe")[0].contentWindow.$("input[name='selectedUserId']:checked").val();
					 var selectedUserName = h.find("iframe")[0].contentWindow.$("input[name='selectedUserId']:checked").next().val();
					 $("#userId").val(selectedUserId);
					 $("#userName").val(selectedUserName);
					 return true;
				}else{
					 $("#userId").val("");
					 $("#userName").val("");
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			}
		});
	});
	</script>
</body>
</html>
