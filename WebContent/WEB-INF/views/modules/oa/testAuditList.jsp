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
<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong>流程管理</strong><span
			class="songti">&gt;</span><span>审批测试</span>
		<shiro:hasPermission name="oa:testAudit:edit">
			<b class="fr">
				<a class="min_btn btn_secondary marright" href="${ctx}/oa/testAudit/form">新增</a>
			</b>
		</shiro:hasPermission>
	</div>
	<div class="con">
		<form:form id="searchForm" modelAttribute="testAudit" action="${ctx}/oa/testAudit/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="search">
					<dl>
						<dt>
							<span><input id="userName" name="userName" readonly="readonly" type="text" value="${testAudit.user.name}" placeholder="姓名" class="input_txt w260"/></span>
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
		<thead><tr><th>姓名</th><th>部门</th><th>岗位职级</th><th>调整原因</th><th>申请时间</th><shiro:hasPermission name="oa:testAudit:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="testAudit">
			<tr>
				<td><a href="${ctx}/oa/testAudit/form?id=${testAudit.id}">${testAudit.user.name}</a></td>
				<td>${testAudit.office.name}</td>
				<td>${testAudit.post}</td>
				<td>${testAudit.content}</td>
				<td><fmt:formatDate value="${testAudit.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<shiro:hasPermission name="oa:testAudit:edit"><td>
    				<a href="${ctx}/oa/testAudit/form?id=${testAudit.id}">详情</a>
					<a href="${ctx}/oa/testAudit/delete?id=${testAudit.id}" onclick="return confirmx('确认要删除该审批吗？', this.href)">删除</a>
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
